package spireCafe.interactables.attractions.jukebox;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CharacterStrings;

import spireCafe.Anniv7Mod;
import spireCafe.abstracts.AbstractAttraction;

public class JukeboxAttraction extends AbstractAttraction {
    public static final String ID = JukeboxAttraction.class.getSimpleName();
    private static final CharacterStrings characterStrings = CardCrawlGame.languagePack.getCharacterString(Anniv7Mod.makeID(ID));
    private static final String RESOURCE_PATH = Anniv7Mod.makeAttractionPath("jukebox/skeleton/");

    public JukeboxAttraction(float animationX, float animationY) {
        super(animationX, animationY, 230, 245);
        authors = "Ninja Puppy";
        name = characterStrings.NAMES[0];

        loadAnimation(RESOURCE_PATH + "skeleton.atlas", RESOURCE_PATH + "skeleton.json", 1.0F);
        this.state.setAnimation(0, "idle", true);
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
