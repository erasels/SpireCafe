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

    private static final String TEMMIE_VOICE = Anniv7Mod.makeID("temmie_voice2.mp3");
    private static final int FLAKE_COST = 1;
    private static final int COLLEGE_FUND_COST = 100;
    private static final int FLAKE_PRICE_MULTIPLIER = 1;
    private int flakePrice = FLAKE_COST;

    private int collegeFundDonations = 0;

    public TemmieBartender(float animationX, float animationY) {
        super(animationX, animationY, 180.0f, 250.0f);
        name = "Bartender Temmie";
        this.authors = "Darklight";
        this.img = TexLoader.getTexture(Anniv7Mod.makeBartenderPath("temmie/Temmie.png"));
        this.cutscenePortrait = new TextureRegion(TexLoader.getTexture(Anniv7Mod.makeBartenderPath("temmie/Portrait.png")));
    }

    @Override
    public void onInteract() {
        System.out.println("Interacting with Temmie Bartender");
        CardCrawlGame.sound.play(TEMMIE_VOICE);
        AbstractDungeon.topLevelEffectsQueue.add(new BartenderCutscene(this, cutsceneStrings));
    }

    @Override
    public String getNoThanksDescription() {
        CardCrawlGame.sound.play(TEMMIE_VOICE);
        return String.format("No Thanks", COLLEGE_FUND_COST);
    }

    @Override
    public void applyHealAction() {
        CardCrawlGame.sound.play(TEMMIE_VOICE);
        Wiz.p().heal(getHealAmount());
        Wiz.p().loseGold(flakePrice);
        inHealAction = false;
    }

    @Override
    public void applySecondOptionAction() {
        CardCrawlGame.sound.play(TEMMIE_VOICE);
        Wiz.p().loseGold(COLLEGE_FUND_COST);
        collegeFundDonations++;
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
        int baseHeal = (Wiz.p().currentHealth < Wiz.p().maxHealth) ? 10 : 0;
        return baseHeal + collegeFundDonations;
    }

    @Override
    public String getSecondOptionDescription() {
        return String.format("Donate to College Fund (%d Gold)", COLLEGE_FUND_COST);
    }

    @Override
    public boolean getSecondOptionCondition() {
        return Wiz.p().gold >= COLLEGE_FUND_COST;
    }

    @Override
    public void doForSelectedCardsFromSecondAction(List<AbstractCard> selected) {}

    @Override
    public String getLabelText() {
        return characterStrings.TEXT[0];
    }
}