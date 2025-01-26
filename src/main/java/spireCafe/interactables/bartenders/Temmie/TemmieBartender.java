package spireCafe.interactables.bartenders.Temmie;

import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import org.apache.commons.lang3.math.NumberUtils;
import spireCafe.Anniv7Mod;
import spireCafe.abstracts.AbstractBartender;
import spireCafe.abstracts.BartenderCutscene;
import spireCafe.cardmods.MoreBlockMod;
import spireCafe.util.TexLoader;
import spireCafe.util.Wiz;
import spireCafe.util.cutsceneStrings.CutsceneStrings;
import spireCafe.util.cutsceneStrings.LocalizedCutsceneStrings;

import java.util.List;
import java.util.Random;

public class TemmieBartender extends AbstractBartender {
    public static final String ID = TemmieBartender.class.getSimpleName();
    private static final CharacterStrings characterStrings = CardCrawlGame.languagePack.getCharacterString(Anniv7Mod.makeID(ID));
    private static final CutsceneStrings cutsceneStrings = LocalizedCutsceneStrings.getCutsceneStrings(Anniv7Mod.makeID(ID + "Cutscene"));

    private static final int FLAKE_COST = 10;
    private static final int FLAKE_PRICE_MULTIPLIER = 2;

    private int flakePrice = FLAKE_COST;

    private static final String[] activationPhrases = {
            "hoi hoi!! welcome to... temmie bar!!!",
            "yay!!! a custOmer!!! temmie sooo happy!!!",
            "u came to drink??? temmie came to... uh... sell drinks!!!",
            "oh wowie!!! another hooman/doge/monster/??? in temmie bar!!!",
            "hoi!!! wanna try da bestest drinks in... uh... ever?!?!",
            "u look like u need... refreshment!!! temmie has da goods!!!",
            "wowwowwow!!! temmie never see customer so cool!!! wanna buy stuff???",
            "drink up!!! wait, u need to buy first... then drink up!!!",
            "heh heh... temmie gots exclusive temmie product... wanna see???",
            "u thirsty?? u hungry??? u just wanna give temmie munnie??? all acceptable!!!"
    };

    private static final String[] flakesPhrases = {
            "hoi!!! temmie flakes!!! taste like... uh... food!!!",
            "wanna eat??? wanna spend??? temmie flakes solve both!!!",
            "great nutrishun!! heal 1 hp!!! only cost... uh... what was price again???",
            "limited edition!!! (but unlimited stock!)",
            "buy now!!! next one even more exclusive!!!",
            "each temmie flake better than last!!! science fact!!!",
            "mmmm… temmie flakes… cronchy, fluffy, and... probably edible!!!",
            "wow!! u already bought one?? wanna spend even more munnie?!?!",
            "heal 1 hp!!! price go up each time!!! perfect investment!!!",
            "buy now or regret later... or buy later... then regret more!!!"
    };

    public TemmieBartender(float animationX, float animationY) {
        super(animationX, animationY, 180.0f, 250.0f);
        name = "Bartender Temmie";
        this.authors = "Darklight";
        this.img = TexLoader.getTexture(Anniv7Mod.makeBartenderPath("temmie/Temmie.png"));
        this.cutscenePortrait = new TextureRegion(TexLoader.getTexture(Anniv7Mod.makeBartenderPath("temmie/Portrait.png")));
    }

    @Override
    public void onInteract() {
        Random rand = new Random();
        int phraseIndex = rand.nextInt(activationPhrases.length);
        CardCrawlGame.sound.play("/anniv7Resources/audio/Temmie-Voice.mp3");
        AbstractDungeon.topLevelEffectsQueue.add(new BartenderCutscene(this, cutsceneStrings));
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
        return (Wiz.p().currentHealth < Wiz.p().maxHealth) ? 1 : 0;
    }

    @Override
    public void applyHealAction() {
        CardCrawlGame.sound.play("SLEEP_1-2");
        Wiz.p().heal(getHealAmount());
        Wiz.p().loseGold(flakePrice);
        flakePrice *= FLAKE_PRICE_MULTIPLIER;
        inHealAction = false;
    }

    @Override
    public void doForSelectedCardsFromSecondAction(List<AbstractCard> selected) {
    }

    @Override
    public String getNoThanksDescription() {
        return cutsceneStrings.OPTIONS[0];
    }

    @Override
    public String getLabelText() {
        return characterStrings.TEXT[0];
    }
}
