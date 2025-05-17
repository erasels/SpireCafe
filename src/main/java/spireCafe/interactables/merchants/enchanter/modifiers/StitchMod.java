package spireCafe.interactables.merchants.enchanter.modifiers;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.core.AbstractCreature;

import basemod.BaseMod;
import basemod.abstracts.AbstractCardModifier;

public class StitchMod extends AbstractCardModifier{

    private static final String ID = StitchMod.class.getSimpleName();
    
    private Class<?> stitchAction;

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }

    @Override
    public boolean shouldApply(AbstractCard card) {
        return card.cost != -2 && card.type != CardType.POWER && !card.cardID.matches("anniv5:(Backstitch|Knot|Patchwork|Pincushion)");
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        super.onUse(card, target, action);
        try {
            stitchAction = Class.forName("thePackmaster.actions.needlework.StitchAction");
            Constructor<?> constructor = stitchAction.getConstructor(AbstractCard.class);
            
            addToBot((AbstractGameAction) constructor.newInstance(card));
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
            throw new RuntimeException(String.format("Error initializing cardmod: %s", this.getClass().getSimpleName()), e);
        }
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        return "anniv5:" + BaseMod.getKeywordProper("anniv5:stitch") + " NL " + rawDescription;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new StitchMod();
    }
    
}
