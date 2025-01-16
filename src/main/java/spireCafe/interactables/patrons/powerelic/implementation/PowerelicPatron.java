package spireCafe.interactables.patrons.powerelic.implementation;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
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
    protected static final int DEFAULT_GOLD_COST=300;
    protected int goldCost;

    public PowerelicPatron(float animationX, float animationY) {
        super(animationX, animationY, 160.0f, 200.0f);
        this.name = characterStrings.NAMES[0];
        this.authors = "Lua Viper";

        img = TexLoader.getTexture(Anniv7Mod.makeCharacterPath("Powerelic/patron.png"));
        cutscenePortrait = new TextureRegion(TexLoader.getTexture(Anniv7Mod.makeCharacterPath("Powerelic/portrait.png")));
        goldCost=DEFAULT_GOLD_COST;
        if (!Settings.isDailyRun)
            goldCost = MathUtils.round(goldCost * AbstractDungeon.merchantRng
                    .random(0.95F, 1.05F));
        goldCost=0; //we're not yet sure whether we actually need a gold cost.
    }

    public static boolean canSpawn() {
        return (!PowerelicAllowlist.getAllConvertibleRelics().isEmpty() && !getAllConvertiblePowers().isEmpty());
    }

    @Override
    public void renderCutscenePortrait(SpriteBatch sb) {
        simpleRenderCutscenePortrait(sb, 1560.0F, 0.0F, 0.0F, 0.0F, 0.0F);
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
