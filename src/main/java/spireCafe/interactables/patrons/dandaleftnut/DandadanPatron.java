package spireCafe.interactables.patrons.dandaleftnut;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import spireCafe.Anniv7Mod;
import spireCafe.abstracts.AbstractPatron;
import spireCafe.util.TexLoader;

public class DandadanPatron extends AbstractPatron {

    public static final String ID = DandadanPatron.class.getSimpleName();
    private static final CharacterStrings characterStrings = CardCrawlGame.languagePack.getCharacterString(Anniv7Mod.makeID(ID));
    public DandadanPatron(float animationX, float animationY) {
        super(animationX, animationY, 180, 180);
        this.name = characterStrings.NAMES[0];
        this.authors = "Indi, Keurodz";
            // IMPLEMENT LOGIC FOR SETTING ANIM OR IMG im not too sure
        this.img = TexLoader.getTexture(Anniv7Mod.makeCharacterPath("Dandaleftnut/Dandadanleftnut.png"));
            // Fix file path for this when we have image file
        this.cutscenePortrait = new TextureRegion(TexLoader.getTexture(Anniv7Mod.makeCharacterPath("Dandaleftnut/BallPortrait.png")));

    }

    public void onInteract() {
        AbstractDungeon.topLevelEffectsQueue.add(new DandadanCutscene(this));
    }

    public void renderCutscenePortrait(SpriteBatch sb) {
    // Placeholder value for the xOffset
        simpleRenderCutscenePortrait(sb, 1680F, 20.0F, 0.0F,0.0F, 0.0F);
    }
}
