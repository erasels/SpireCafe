package spireCafe.cardmods;

import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;
import spireCafe.Anniv7Mod;

public class CostMod extends AbstractCardModifier {
    public static String ID = Anniv7Mod.makeID(CostMod.class.getSimpleName());
    public int costChange;

    public CostMod(int costChange) {
        this.costChange = costChange;
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }

    @Override
    public void onInitialApplication(AbstractCard card) {
        card.cost += this.costChange;
        card.costForTurn += this.costChange;
        card.name += "?";
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new CostMod(this.costChange);
    }
}
