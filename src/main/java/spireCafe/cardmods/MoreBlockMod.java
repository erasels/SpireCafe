package spireCafe.cardmods;

import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;
import spireCafe.Anniv7Mod;

public class MoreBlockMod extends AbstractCardModifier {
    public static String ID = Anniv7Mod.makeID(MoreBlockMod.class.getSimpleName());

    public MoreBlockMod() {
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }

    @Override
    public void onInitialApplication(AbstractCard card) {
        card.baseBlock += 2;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new MoreBlockMod();
    }
}
