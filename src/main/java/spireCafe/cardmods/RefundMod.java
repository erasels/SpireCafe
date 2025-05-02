package spireCafe.cardmods;

import com.evacipated.cardcrawl.mod.stslib.actions.common.RefundAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;

import basemod.abstracts.AbstractCardModifier;
import spireCafe.Anniv7Mod;

public class RefundMod extends AbstractCardModifier {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(Anniv7Mod.makeID(RefundMod.class.getSimpleName()));

    private int refundAmount;

    public RefundMod(int refundAmount) {
        this.refundAmount = refundAmount;
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        addToBot(new RefundAction(card, this.refundAmount));
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        return String.format(uiStrings.TEXT[0], this.refundAmount) +  rawDescription;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new RefundMod(refundAmount);
    }
    
}
