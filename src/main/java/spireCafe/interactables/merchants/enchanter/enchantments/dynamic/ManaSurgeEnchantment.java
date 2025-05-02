package spireCafe.interactables.merchants.enchanter.enchantments.dynamic;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import basemod.ReflectionHacks;
import basemod.abstracts.AbstractCardModifier;
import spireCafe.interactables.merchants.enchanter.AbstractEnchantment;
import spireCafe.interactables.merchants.enchanter.EnchanterMerchant.EnchantmentRarity;

public class ManaSurgeEnchantment extends AbstractEnchantment{

    private Class<?> manaSurgeZone;

    public ManaSurgeEnchantment(AbstractCardModifier cardModifier, EnchantmentRarity rarity) {
        super(cardModifier, rarity);
    }

    @Override
    public String getName() {
        try {
            manaSurgeZone = Class.forName("spireMapOverhaul.zones.manasurge.ManaSurgeZone");
            Method m = manaSurgeZone.getMethod("getKeywordProper", String.class);
            String modifierID = ReflectionHacks.getPrivateStatic(cardModifier.getClass(), "ID");
            return (String) m.invoke(null, modifierID);
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return cardModifier.additionalTooltips(tooltipBuddy).get(0).title;
    }

    @Override
    public String getDescription() {
        return makeModLabel(this.cardModifier.getClass()) + cardModifier.additionalTooltips(tooltipBuddy).get(0).description;
    }
    
}
