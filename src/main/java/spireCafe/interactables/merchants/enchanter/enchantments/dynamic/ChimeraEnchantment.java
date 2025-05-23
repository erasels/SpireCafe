package spireCafe.interactables.merchants.enchanter.enchantments.dynamic;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.CardGroup.CardGroupType;

import basemod.abstracts.AbstractCardModifier;
import spireCafe.interactables.merchants.enchanter.AbstractEnchantment;
import spireCafe.interactables.merchants.enchanter.EnchanterMerchant;
import spireCafe.interactables.merchants.enchanter.EnchanterMerchant.EnchantmentRarity;

public class ChimeraEnchantment extends AbstractEnchantment{

    private Class<?> chimeraAbstractAugment;

    public ChimeraEnchantment(AbstractCardModifier cardModifier, EnchantmentRarity rarity) {
        super(cardModifier, rarity);
    }

    @Override
    public CardGroup getValidCards() {
        CardGroup cards = super.getValidCards();
        CardGroup newCards = new CardGroup(CardGroupType.UNSPECIFIED);
        try {
            chimeraAbstractAugment = Class.forName("CardAugments.cardmods.AbstractAugment");
            Method m = chimeraAbstractAugment.getMethod("canApplyTo", AbstractCard.class);
            
            for (AbstractCard c : cards.group) {
                if ((boolean) m.invoke(cardModifier, c)) {
                    newCards.addToBottom(c);
                }
            }
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            throw new RuntimeException("Error retrieving card mods from Chimera Cards", e);
        }
        return newCards;
    }

    @Override
    public String getName() {
        String header = "";
        try {
            chimeraAbstractAugment = Class.forName("CardAugments.cardmods.AbstractAugment");
            Method m = chimeraAbstractAugment.getMethod("modifyName", String.class, AbstractCard.class);
            header = ((String) m.invoke(this.cardModifier, "", tooltipBuddy)).replace("  ", " ").trim();
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalAccessException | InvocationTargetException e) {
            EnchanterMerchant.logger.warn(String.format("Error generating name for %s, using class name instead...", cardModifier.getClass().getSimpleName()));
            e.printStackTrace();
        }

        if (header.isEmpty()) {
            header = cardModifier.getClass().getSimpleName();
        }
        return header;
    }

    @Override
    public String getDescription() {
        String body = "";
        try {
            chimeraAbstractAugment = Class.forName("CardAugments.cardmods.AbstractAugment");
            Method m = chimeraAbstractAugment.getMethod("getAugmentDescription");
            body = makeModLabel(cardModifier.getClass()) + m.invoke(cardModifier);
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalAccessException | InvocationTargetException e) {
            EnchanterMerchant.logger.warn(String.format("Error generating description for %s, leaving blank...", cardModifier.getClass().getSimpleName()));
            e.printStackTrace();
            return body;
        }
        if (body.endsWith(" NL ")) {
            body = body.substring(0, body.length()-4);
        }
        return body;
    }
    
    @Override
    public String toString() {
        return super.toString() + ": " + this.cardModifier;
    }
}
