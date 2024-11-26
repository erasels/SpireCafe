package spireCafe.cardvars;

import basemod.abstracts.DynamicVariable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import spireCafe.abstracts.AbstractSCCard;

import static spireCafe.Anniv7Mod.makeID;

public class SecondDamage extends DynamicVariable {

    @Override
    public String key() {
        return makeID("sd");
    }

    @Override
    public boolean isModified(AbstractCard card) {
        if (card instanceof AbstractSCCard) {
            return ((AbstractSCCard) card).isSecondDamageModified;
        }
        return false;
    }

    public void setIsModified(AbstractCard card, boolean v) {
        if (card instanceof AbstractSCCard) {
            ((AbstractSCCard) card).isSecondDamageModified = v;
        }
    }

    @Override
    public int value(AbstractCard card) {
        if (card instanceof AbstractSCCard) {
            return ((AbstractSCCard) card).secondDamage;
        }
        return -1;
    }

    @Override
    public int baseValue(AbstractCard card) {
        if (card instanceof AbstractSCCard) {
            return ((AbstractSCCard) card).baseSecondDamage;
        }
        return -1;
    }

    @Override
    public boolean upgraded(AbstractCard card) {
        if (card instanceof AbstractSCCard) {
            return ((AbstractSCCard) card).upgradedSecondDamage;
        }
        return false;
    }
}