package spireCafe.interactables.merchants.enchanter.modifiers;

import java.lang.reflect.InvocationTargetException;

import com.megacrit.cardcrawl.cards.AbstractCard;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import spireCafe.Anniv7Mod;

public class MagnetizeMod extends AbstractCardModifier {
    public static final String ID = Anniv7Mod.makeID(LootMod.class.getSimpleName());

    private Class<?> magnetizeMod;

    @Override
    public boolean shouldApply(AbstractCard card) {
        return card.cost != -2 && !CardModifierManager.hasModifier(card, "anniv5:MagnetizedModifier");
    }

    @Override
    public void onInitialApplication(AbstractCard card) {
        super.onInitialApplication(card);
        try {
            magnetizeMod = Class.forName("thePackmaster.cardmodifiers.magnetizepack.MagnetizedModifier");
            CardModifierManager.addModifier(card, (AbstractCardModifier) magnetizeMod.getConstructor(boolean.class).newInstance(false));
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(String.format("Error initializing cardmod: %s", this.getClass().getSimpleName()), e);
        }
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new MagnetizeMod();
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }
    
}
