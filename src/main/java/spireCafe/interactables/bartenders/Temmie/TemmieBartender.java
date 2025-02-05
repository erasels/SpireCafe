package spireCafe.interactables.bartenders.Temmie;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import spireCafe.Anniv7Mod;
import spireCafe.abstracts.AbstractBartender;
import spireCafe.abstracts.BartenderCutscene;
import spireCafe.util.TexLoader;
import spireCafe.util.Wiz;
import spireCafe.util.cutsceneStrings.CutsceneStrings;
import spireCafe.util.cutsceneStrings.LocalizedCutsceneStrings;

import java.util.List;

public class TemmieBartender extends AbstractBartender {
    public static final String ID = TemmieBartender.class.getSimpleName();
    private static final CharacterStrings characterStrings = CardCrawlGame.languagePack.getCharacterString(Anniv7Mod.makeID(ID));
    private static final CutsceneStrings cutsceneStrings = LocalizedCutsceneStrings.getCutsceneStrings(Anniv7Mod.makeID(ID + "Cutscene"));

    private static final int FLAKE_COST = 1; // Starting cost for a Temmie Flake
    private static final int FLAKE_PRICE_MULTIPLIER = 1; // No price increase for repeat purchases
    private int flakePrice = FLAKE_COST; // Current price of Temmie Flake

    public TemmieBartender(float animationX, float animationY) {
        super(animationX, animationY, 180.0f, 250.0f);
        name = "Bartender Temmie";
        this.authors = "Darklight";
        this.img = TexLoader.getTexture(Anniv7Mod.makeBartenderPath("temmie/Temmie.png"));
        this.cutscenePortrait = new TextureRegion(TexLoader.getTexture(Anniv7Mod.makeBartenderPath("temmie/Portrait.png")));
    }

    @Override
    public void onInteract() {
        // Debug logging
        System.out.println("Interacting with Temmie Bartender");

        // Play Temmie voice when bartender is clicked
        CardCrawlGame.sound.play(Anniv7Mod.makeID("audio/Temmie-Voice2.ogg"));

        // Show the bartender cutscene
        AbstractDungeon.topLevelEffectsQueue.add(new BartenderCutscene(this, cutsceneStrings));
    }

    @Override
    public String getNoThanksDescription() {
        // Play sound when declining the offer
        CardCrawlGame.sound.play(Anniv7Mod.makeID("audio/Temmie-Voice2.ogg"));
        return cutsceneStrings.OPTIONS[0];
    }

    @Override
    public void applyHealAction() {
        // Play sound when the player buys Temmie Flakes
        CardCrawlGame.sound.play(Anniv7Mod.makeID("audio/Temmie-Voice2.ogg"));

        // Apply healing and gold deduction
        Wiz.p().heal(getHealAmount());
        Wiz.p().loseGold(flakePrice);
    }

    @Override
    public void applySecondOptionAction() {
        // Play sound when selecting a secondary option (if applicable)
        CardCrawlGame.sound.play(Anniv7Mod.makeID("audio/Temmie-Voice2.ogg"));

        // Important to reset this action flag
        inSecondAction = false;
    }

    @Override
    public String getHealOptionDescription() {
        return String.format(cutsceneStrings.OPTIONS[1], getHealAmount(), flakePrice);
    }

    @Override
    public boolean getHealOptionCondition() {
        return Wiz.p().gold >= flakePrice;
    }

    @Override
    public int getHealAmount() {
        return (Wiz.p().currentHealth < Wiz.p().maxHealth) ? 10 : 0;
    }

    @Override
    public void doForSelectedCardsFromSecondAction(List<AbstractCard> selected) {}

    @Override
    public String getLabelText() {
        return characterStrings.TEXT[0];
    }
}
