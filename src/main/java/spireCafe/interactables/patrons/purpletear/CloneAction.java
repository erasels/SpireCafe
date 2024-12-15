package spireCafe.interactables.patrons.purpletear;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;

import java.util.ArrayList;

import static spireCafe.util.Wiz.adp;

public class CloneAction extends AbstractGameAction {
    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    private final int dupeAmount;
    private int highestCost = -2;
    private final ArrayList<AbstractCard> cannotDuplicate = new ArrayList<>();

    public CloneAction(int amount) {
        this.setValues(AbstractDungeon.player, AbstractDungeon.player, amount);
        this.actionType = ActionType.DRAW;
        this.duration = 0.25F;
        this.dupeAmount = amount;
    }

    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            for (AbstractCard card : adp().hand.group) {
                if (card.costForTurn > highestCost) {
                    highestCost = card.costForTurn;
                }
            }
            for (AbstractCard card : adp().hand.group) {
                if (!this.isCloneable(card)) {
                    this.cannotDuplicate.add(card);
                }
            }
            if (this.cannotDuplicate.size() == adp().hand.group.size()) {
                this.isDone = true;
                return;
            }
            if (adp().hand.group.size() - this.cannotDuplicate.size() == 1) {
                for (AbstractCard card : adp().hand.group) {
                    if (this.isCloneable(card)) {
                        for(int i = 0; i < this.dupeAmount; i++) {
                            this.addToTop(new MakeTempCardInHandAction(card.makeStatEquivalentCopy()));
                        }
                        this.isDone = true;
                        return;
                    }
                }
            }
            adp().hand.group.removeAll(this.cannotDuplicate);
            if (adp().hand.group.size() > 1) {
                AbstractDungeon.handCardSelectScreen.open(TEXT[0], 1, false, false, false, false);
                this.tickDuration();
                return;
            }

            if (adp().hand.group.size() == 1) {
                for(int i = 0; i < this.dupeAmount; ++i) {
                    this.addToTop(new MakeTempCardInHandAction(adp().hand.getTopCard().makeStatEquivalentCopy()));
                }
                this.returnCards();
                this.isDone = true;
            }
        }

        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            for (AbstractCard card : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                this.addToTop(new MakeTempCardInHandAction(card.makeStatEquivalentCopy()));
                for(int i = 0; i < this.dupeAmount; i++) {
                    this.addToTop(new MakeTempCardInHandAction(card.makeStatEquivalentCopy()));
                }
            }

            this.returnCards();
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
            AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
            this.isDone = true;
        }

        this.tickDuration();
    }

    private void returnCards() {
        for (AbstractCard c : this.cannotDuplicate) {
            adp().hand.addToTop(c);
        }
        adp().hand.refreshHandLayout();
    }

    private boolean isCloneable(AbstractCard card) {
        return card.costForTurn == highestCost && card.costForTurn >= 0;
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("DualWieldAction");
        TEXT = uiStrings.TEXT;
    }
}
