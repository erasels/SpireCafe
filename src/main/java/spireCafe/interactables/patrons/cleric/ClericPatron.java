package spireCafe.interactables.patrons.cleric;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import spireCafe.Anniv7Mod;
import spireCafe.abstracts.AbstractPatron;
import spireCafe.util.TexLoader;
import spireCafe.util.Wiz;

public class ClericPatron extends AbstractPatron {

    private static final float hb_w = 90.0F;
    private static final float hb_h = 250.0F;
    private static final String ID = Anniv7Mod.makeID(ClericPatron.class.getSimpleName());
    private static final CharacterStrings characterStrings = CardCrawlGame.languagePack.getCharacterString(ID);
    private static final String RESOURCE_PATH = Anniv7Mod.makeCharacterPath("Cleric/");
    public AbstractRelic lostRelic;

    public ClericPatron(float animationX, float animationY) {
        super(animationX, animationY, hb_w, hb_h);
        this.name = characterStrings.NAMES[0];
        this.authors = "Coda";

        loadAnimation(RESOURCE_PATH + "skeleton.atlas", RESOURCE_PATH + "skeleton.json", 1.0F);
        this.state.setAnimation(0, "idle", true);
        this.cutscenePortrait = new TextureRegion(TexLoader.getTexture(Anniv7Mod.makeCharacterPath("Cleric/portrait.png")));

        this.lostRelic = getLostItem();
    }

    @Override
    public void renderCutscenePortrait(SpriteBatch sb) {
        simpleRenderCutscenePortrait(sb, 1560.0F,0.0F, 0.0F, 0.0F, 0.0F);
    }

    @Override
    public void onInteract() {
        AbstractDungeon.topLevelEffectsQueue.add(new ClericPatronCutscene(this));
    }
    
    public static boolean canSpawn() {
        // Only spawn after Act 2.
        return AbstractDungeon.floorNum >= 34;
    }

    public AbstractRelic getLostItem() {
        if (Wiz.p().relics.isEmpty()) {
            return null;
        }
        ArrayList<AbstractRelic> relics = new ArrayList<>();
        relics.addAll(Wiz.p().relics);
        Collections.shuffle(relics, new Random(AbstractDungeon.miscRng.randomLong()));
        return relics.get(0);
    }
}
