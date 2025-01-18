package spireCafe.interactables.attractions.jukebox;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CharacterStrings;

import basemod.animations.SpriterAnimation;
import spireCafe.Anniv7Mod;
import spireCafe.abstracts.AbstractAttraction;

public class JukeboxAttraction extends AbstractAttraction {
    public static final String ID = JukeboxAttraction.class.getSimpleName();
    private static final CharacterStrings characterStrings = CardCrawlGame.languagePack.getCharacterString(Anniv7Mod.makeID(ID));

    public JukeboxAttraction(float animationX, float animationY) {
        super(animationX, animationY, 302, 285);
        this.animation = new SpriterAnimation(
                Anniv7Mod.makeAttractionPath("jukebox/JukeboxIdle.scml"));
        authors = "Ninja Puppy";
        name = characterStrings.NAMES[0];
    }

    @Override
    public void renderCutscenePortrait(SpriteBatch sb) {

    }

    @Override
    public void renderAnimation(SpriteBatch sb) {
        super.renderAnimation(sb);
    }


    @Override
    public void onInteract() {

        if (!alreadyPerformedTransaction) {
            AbstractDungeon.topLevelEffectsQueue.add(new JukeboxCutscene(this));
        }
    }
}
