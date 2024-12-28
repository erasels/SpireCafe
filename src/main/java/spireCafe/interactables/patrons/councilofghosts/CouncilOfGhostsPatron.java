package spireCafe.interactables.patrons.councilofghosts;

import basemod.animations.SpriterAnimation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import spireCafe.Anniv7Mod;
import spireCafe.abstracts.AbstractPatron;
import spireCafe.interactables.patrons.koishi.KoishiCutscene;
import spireCafe.util.TexLoader;

public class CouncilOfGhostsPatron extends AbstractPatron {
    public static final String ID = CouncilOfGhostsPatron.class.getSimpleName();
    public static final CharacterStrings characterStrings = CardCrawlGame.languagePack.getCharacterString(Anniv7Mod.makeID(ID));

    public static final String assetID = "CouncilOfGhosts";

    public CouncilOfGhostsPatron(float animationX, float animationY) {
        super(animationX, animationY, 160.0f, 210.0f);
        this.name = characterStrings.NAMES[0];
        this.authors = "MaybeLaterx";
        this.animation = new SpriterAnimation(Anniv7Mod.makeCharacterPath("CouncilOfGhosts/Spriter/CouncilOfGhosts.scml"));
        this.animation.setFlip(true, false);
        setCutscenePortrait("Mist");
    }

    public void setCutscenePortrait(String texture) {
        String resourcePath = String.format("CouncilOfGhosts/%s.png", texture);
        this.cutscenePortrait = new TextureRegion(TexLoader.getTexture(Anniv7Mod.makeCharacterPath(resourcePath)));
    }

    public void renderCutscenePortrait(SpriteBatch sb) {
        simpleRenderCutscenePortrait(sb, 1560.0F,0.0F, 0.0F, 0.0F, 0.0F);
    }

    public void onInteract() {
        if (alreadyPerformedTransaction) {
            setCutscenePortrait("Mist");
        } else {
            setCutscenePortrait("Mist");
        }
        AbstractDungeon.topLevelEffectsQueue.add(new CouncilOfGhostsCutscene(this));
    }
}
