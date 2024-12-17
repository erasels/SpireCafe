package spireCafe.interactables.attractions.makeup;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ShaderHelper;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.rooms.RestRoom;
import spireCafe.Anniv7Mod;
import spireCafe.abstracts.AbstractAttraction;
import spireCafe.util.TexLoader;
import spireCafe.vfx.SparkleEffect;

public class MakeupTableAttraction extends AbstractAttraction {
    public static final String ID = MakeupTableAttraction.class.getSimpleName();
    private static final CharacterStrings characterStrings = CardCrawlGame.languagePack.getCharacterString(Anniv7Mod.makeID(ID));
    public static boolean isAPrettySparklingPrincess = false;
    private static float sparkleTimer = 0, sparkleInterval = 0.5f;

    public MakeupTableAttraction(float animationX, float animationY) {
        super(animationX, animationY, 300, 400);
        img = TexLoader.getTexture(Anniv7Mod.makeAttractionPath("makeup/table.png"));
        authors = "God-king Jack Zanders, Hero of Gondor, Fearless Fighter for Jerkish Ham.";
        name = characterStrings.NAMES[0];
    }

    @Override
    public void renderCutscenePortrait(SpriteBatch sb) {
        //No portrait, it's the character thinking if they want to be a pretty princess.
    }

    @Override
    public void renderAnimation(SpriteBatch sb) {
        if(alreadyPerformedTransaction)
            ShaderHelper.setShader(sb, ShaderHelper.Shader.GRAYSCALE);
        super.renderAnimation(sb);
        if(alreadyPerformedTransaction)
            ShaderHelper.setShader(sb, ShaderHelper.Shader.DEFAULT);
    }

    @Override
    public void onInteract() {
        if(!alreadyPerformedTransaction)
            AbstractDungeon.topLevelEffectsQueue.add(new MakeupCutscene(this));
    }

    @SpirePatch2(clz = AbstractPlayer.class, method = "render")
    public static class SparklePrincessPatch {
        @SpirePostfixPatch
        public static void patch(AbstractPlayer __instance) {
            if (isAPrettySparklingPrincess && !(AbstractDungeon.getCurrRoom() instanceof RestRoom)) {
                sparkleTimer -= Gdx.graphics.getDeltaTime();
                if(sparkleTimer <= 0) {
                    sparkleTimer = sparkleInterval;
                    AbstractDungeon.effectList.add(new SparkleEffect(__instance.hb, Color.PINK));
                }
            }
        }
    }
}