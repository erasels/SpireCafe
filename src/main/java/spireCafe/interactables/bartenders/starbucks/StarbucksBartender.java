package spireCafe.interactables.bartenders.starbucks;

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

public class StarbucksBartender extends AbstractBartender {
    public static final String ID = StarbucksBartender.class.getSimpleName();
    private static final CharacterStrings characterStrings = CardCrawlGame.languagePack.getCharacterString(Anniv7Mod.makeID(ID));
    private static final CutsceneStrings cutsceneStrings = LocalizedCutsceneStrings.getCutsceneStrings(Anniv7Mod.makeID(ID + "Cutscene"));

    private static final int MILK_COST = 25;
    private static final int GOLD_LOSS = 70;

    public StarbucksBartender(float animationX, float animationY) {
        super(animationX, animationY, 180.0f, 250.0f);
        name = characterStrings.NAMES[0];
        this.authors = "Gk";
        this.img = TexLoader.getTexture(Anniv7Mod.makeBartenderPath("starbucks/image.png"));
        this.cutscenePortrait = new TextureRegion(TexLoader.getTexture(Anniv7Mod.makeBartenderPath("starbucks/Portrait.png")));
    }

    @Override
    public String getHealOptionDescription() {
        return String.format(cutsceneStrings.OPTIONS[1], getHealAmount(), GOLD_LOSS);
    }

    @Override
    public int getHealAmount() {
        // Heal 50% of max HP or 10, whichever is larger
        return (int) NumberUtils.max(Wiz.p().maxHealth * 0.5, 10);
    }

    @Override
    public void applyHealAction() {
        CardCrawlGame.sound.play("SLEEP_1-2");
        Wiz.p().heal(getHealAmount());
        Wiz.p().loseGold(GOLD_LOSS);
        inHealAction = false; //Important to set this to false after the logic has concluded
    }

    @Override
    public String getSecondOptionDescription() {
        return String.format(cutsceneStrings.OPTIONS[2], MILK_COST);
    }

    @Override
    public void applySecondOptionAction() {
        CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (AbstractCard card : AbstractDungeon.player.masterDeck.group) {
            if (card.baseBlock > 0) {
                group.addToBottom(card);
            }
        }
        if(!group.isEmpty()) {
            AbstractDungeon.gridSelectScreen.open(group, 1, characterStrings.TEXT[1], false);
            Wiz.p().loseGold(MILK_COST);
        }
    }

    @Override
    public void doForSelectedCardsFromSecondAction(List<AbstractCard> selected) {
        AbstractCard c = selected.get(0);
        AbstractDungeon.effectList.add(new UpgradeShineEffect(c.current_x, c.current_y));
        CardModifierManager.addModifier(c, new MoreBlockMod());
        inSecondAction = false; //Important to set this to false after the logic has concluded
    }

    @Override
    public String getNoThanksDescription() {
        return cutsceneStrings.OPTIONS[0];
    }

    @Override
    public String getLabelText() {
        return characterStrings.TEXT[0];
    }

    /*
     * With the current configuration you don't need to do this, as the naming matches the assumed default.
     * I just do this because I like to separate the options for tht cutscene from characterstrings.
     */
    public void onInteract() {
        AbstractDungeon.topLevelEffectsQueue.add(new BartenderCutscene(this, cutsceneStrings));
    }
}
