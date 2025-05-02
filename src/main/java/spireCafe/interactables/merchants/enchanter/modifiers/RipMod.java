package spireCafe.interactables.merchants.enchanter.modifiers;

import java.lang.reflect.InvocationTargetException;

import com.megacrit.cardcrawl.cards.AbstractCard;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import spireCafe.Anniv7Mod;

public class RipMod extends AbstractCardModifier {

    private static final String ID = Anniv7Mod.makeID(RipMod.class.getSimpleName());
    private Class<?> rippableModifier;

    // I don't understand how isInherit works on CardMods and after trying for a bit, I realized I do not care.

    @Override
    public boolean shouldApply(AbstractCard card) {
        return !CardModifierManager.hasModifier(card, "anniv5:RippableModifier");
    }

    @Override
    public void onInitialApplication(AbstractCard card) {
        super.onInitialApplication(card);
        try {
            rippableModifier = Class.forName("thePackmaster.cardmodifiers.rippack.RippableModifier");
            CardModifierManager.addModifier(card, (AbstractCardModifier) rippableModifier.getConstructor(boolean.class).newInstance(false));
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new RipMod();
    }
    
    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }
}
