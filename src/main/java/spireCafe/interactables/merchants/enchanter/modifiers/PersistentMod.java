package spireCafe.interactables.merchants.enchanter.modifiers;

import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.PersistFields;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;

import basemod.abstracts.AbstractCardModifier;
import spireCafe.Anniv7Mod;

public class PersistentMod extends AbstractCardModifier {

    private static final String ID = Anniv7Mod.makeID(PersistentMod.class.getSimpleName());
    public static final String[] TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;

    private int counter;
    
    public PersistentMod(int count) {
        this.counter = count;
    }

    @Override
    public void onInitialApplication(AbstractCard card) {
        PersistFields.setBaseValue(card, this.counter);
    }

    @Override
    public boolean shouldApply(AbstractCard card) {
        return PersistFields.basePersist.get(card) <= 0;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new PersistentMod(counter);
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        return rawDescription + String.format(TEXT[2], this.counter);
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }
    
}
