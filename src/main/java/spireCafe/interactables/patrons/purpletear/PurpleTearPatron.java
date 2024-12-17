package spireCafe.interactables.patrons.purpletear;

import basemod.animations.SpriterAnimation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import spireCafe.Anniv7Mod;
import spireCafe.abstracts.AbstractPatron;
import spireCafe.util.TexLoader;

public class PurpleTearPatron extends AbstractPatron {
    public static final String ID = PurpleTearPatron.class.getSimpleName();
    public static final String assetID = "PurpleTear";
    private static final CharacterStrings characterStrings = CardCrawlGame.languagePack.getCharacterString(Anniv7Mod.makeID(ID));

    public PurpleTearPatron(float animationX, float animationY) {
        super(animationX, animationY, 160.0f, 220.0f);
        this.name = characterStrings.NAMES[0];
        this.authors = "Darkglade";
        this.animation = new SpriterAnimation(Anniv7Mod.makeCharacterPath("PurpleTear/Spriter/PurpleTear.scml"));
        setCutscenePortrait("Portrait1");
    }

    public void setCutscenePortrait(String texture) {
        String resourcePath = String.format("PurpleTear/%s.png", texture);
        this.cutscenePortrait = new TextureRegion(TexLoader.getTexture(Anniv7Mod.makeCharacterPath(resourcePath)));
    }

    public void renderCutscenePortrait(SpriteBatch sb) {
        simpleRenderCutscenePortrait(sb, 1560.0F,0.0F, 0.0F, 0.0F, 0.0F);
    }

    public void onInteract() {
        setCutscenePortrait("Portrait1");
        AbstractDungeon.topLevelEffectsQueue.add(new PurpleTearCutscene(this));
    }

    @Override
    public boolean canSpawn() {
        return PurpleTearCutscene.canSpawn();
    }
}
