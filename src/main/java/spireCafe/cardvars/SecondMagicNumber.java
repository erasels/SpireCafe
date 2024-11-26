package spireCafe.cardvars;

import basemod.abstracts.DynamicVariable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import spireCafe.abstracts.AbstractSCCard;

import static spireCafe.Anniv7Mod.makeID;

public class SecondMagicNumber extends DynamicVariable {

    @Override
    public String key() {
        return makeID("m2");
    }

    @Override
    public boolean isModified(AbstractCard card) {
        if (card instanceof AbstractSCCard) {
            return ((AbstractSCCard) card).isSecondMagicModified;
        }
        return false;
    }

    @Override
    public int value(AbstractCard card) {
        if (card instanceof AbstractSCCard) {
            return ((AbstractSCCard) card).secondMagic;
        }
        return -1;
    }

    public void setIsModified(AbstractCard card, boolean v) {
        if (card instanceof AbstractSCCard) {
            ((AbstractSCCard) card).isSecondMagicModified = v;
        }
    }

    @Override
    public int baseValue(AbstractCard card) {
        if (card instanceof AbstractSCCard) {
            return ((AbstractSCCard) card).baseSecondMagic;
        }
        return -1;
    }

    @Override
    public boolean upgraded(AbstractCard card) {
        if (card instanceof AbstractSCCard) {
            return ((AbstractSCCard) card).upgradedSecondMagic;
        }
        return false;
    }
}