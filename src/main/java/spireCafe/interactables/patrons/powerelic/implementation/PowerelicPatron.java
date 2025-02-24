package spireCafe.interactables.patrons.powerelic.implementation;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import spireCafe.Anniv7Mod;
import spireCafe.abstracts.AbstractPatron;
import spireCafe.interactables.patrons.powerelic.PowerelicAllowlist;
import spireCafe.util.TexLoader;

import java.util.ArrayList;

public class PowerelicPatron extends AbstractPatron {
    public static final String ID = PowerelicPatron.class.getSimpleName();
    public static final String assetID = "PowerelicPatron";
    private static final CharacterStrings characterStrings = CardCrawlGame.languagePack.getCharacterString(Anniv7Mod.makeID(ID));

    protected boolean alreadyTalkedOnce=false;
    protected boolean ignoreLooter=false;

    public PowerelicPatron(float animationX, float animationY) {
        super(animationX, animationY, 160.0f, 200.0f);
        this.name = characterStrings.NAMES[0];
        this.authors = "Lua Viper";
        this.animationY=this.animationY-20f;

        img = TexLoader.getTexture(Anniv7Mod.makeCharacterPath("Powerelic/patron.png"));
        cutscenePortrait = new TextureRegion(TexLoader.getTexture(Anniv7Mod.makeCharacterPath("Powerelic/portrait.png")));
    }

    public static boolean canSpawn() {
        return (!PowerelicAllowlist.getAllConvertibleRelics().isEmpty() && !getAllConvertiblePowers().isEmpty());
    }

    @Override
    public void renderCutscenePortrait(SpriteBatch sb) {
        simpleRenderCutscenePortrait(sb, 1560.0F, -200.0F, 0.0F, 0.0F, 0.0F);
    }

    @Override
    public void renderAnimation(SpriteBatch sb) {
        super.renderAnimation(sb);
    }

    public void onInteract() {
        AbstractDungeon.topLevelEffectsQueue.add(new PowerelicCutscene(this));
        alreadyTalkedOnce=true;
    }

    public static ArrayList<AbstractCard> getAllConvertiblePowers(){
        ArrayList<AbstractCard> powers=AbstractDungeon.player.masterDeck.getPurgeableCards().getCardsOfType(AbstractCard.CardType.POWER).group;
        powers.removeIf(card -> (card instanceof PowerelicCard));
        return powers;
    }
}
