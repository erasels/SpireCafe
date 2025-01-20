package spireCafe.interactables.patrons.dandadan;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
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
        this.img = TexLoader.getTexture(Anniv7Mod.makeCharacterPath("Dandadan/Ball.png"));
        this.cutscenePortrait = new TextureRegion(TexLoader.getTexture(Anniv7Mod.makeCharacterPath("Dandadan/BallPortrait.png")));

    }

    @Override
    public void onInteract() {
        AbstractDungeon.topLevelEffectsQueue.add(new DandadanCutscene(this));
    }

    @Override
    public void renderCutscenePortrait(SpriteBatch sb) {
        simpleRenderCutscenePortrait(sb, 1680F, 20.0F, 0.0F,0.0F, 0.0F);
    }

    public void disappear() {
        this.hitbox.x = -9999F * Settings.scale;
        this.hitbox.y = -9999F * Settings.scale;
        this.animationX = -9999F * Settings.scale;
        this.animationY = -9999F * Settings.scale;
        this.shouldShowSpeechBubble = false;
    }
}
