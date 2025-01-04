package spireCafe.cardmods;

import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;
import spireCafe.Anniv7Mod;

public class WornMod extends AbstractCardModifier {
    public static String ID = Anniv7Mod.makeID(WornMod.class.getSimpleName());
    private int damageDown;
    private int blockDown;
    private int magicDown;

    public WornMod(int damageDown, int blockDown, int magicDown) {
        this.damageDown = damageDown;
        this.blockDown = blockDown;
        this.magicDown = magicDown;
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }

    @Override
    public void onInitialApplication(AbstractCard card) {
        card.baseDamage -= damageDown;
        card.baseBlock -= blockDown;
        card.baseMagicNumber -= magicDown;
        card.rarity = AbstractCard.CardRarity.COMMON;
        card.name = "Worn " + card.name;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new WornMod(damageDown, blockDown, magicDown);
    }
}
