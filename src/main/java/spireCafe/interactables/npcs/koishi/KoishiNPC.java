package spireCafe.interactables.npcs.koishi;

import basemod.animations.SpriterAnimation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import spireCafe.Anniv7Mod;
import spireCafe.abstracts.AbstractNPC;
import spireCafe.util.TexLoader;

public class KoishiNPC extends AbstractNPC {
    public static final String ID = KoishiNPC.class.getSimpleName();
    private static final CharacterStrings characterStrings = CardCrawlGame.languagePack.getCharacterString(Anniv7Mod.makeID(ID));

    public KoishiNPC(float animationX, float animationY) {
        super(animationX, animationY, 160.0f, 200.0f);
        this.name = characterStrings.NAMES[0];
        this.animation = new SpriterAnimation(Anniv7Mod.makeCharacterPath("Koishi/Spriter/KoishiAnimation.scml"));
        this.animation.setFlip(true, false);
        setCutscenePortrait("Portrait1");
    }

    public void renderCutscenePortrait(SpriteBatch sb) {
        sb.draw(cutscenePortrait, (1560.0F - (cutscenePortrait.getRegionWidth() / 2.0F)) * Settings.scale, 0 * Settings.scale, 0.0F, 0.0F, cutscenePortrait.getRegionWidth(), cutscenePortrait.getRegionHeight(), Settings.scale, Settings.scale, 0.0F);
    }

    public void setCutscenePortrait(String texture) {
        String resourcePath = String.format("Koishi/%s.png", texture);
        this.cutscenePortrait = new TextureRegion(TexLoader.getTexture(Anniv7Mod.makeCharacterPath(resourcePath)));
    }

    public void onInteract() {
        setCutscenePortrait("Portrait1");
        AbstractDungeon.topLevelEffectsQueue.add(new KoishiCutscene(this));
    }
}
