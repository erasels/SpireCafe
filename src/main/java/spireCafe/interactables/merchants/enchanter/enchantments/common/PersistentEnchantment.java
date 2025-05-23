package spireCafe.interactables.merchants.enchanter.enchantments.common;

import spireCafe.interactables.merchants.enchanter.AbstractEnchantment;
import spireCafe.interactables.merchants.enchanter.EnchanterMerchant.EnchantmentRarity;
import spireCafe.interactables.merchants.enchanter.modifiers.PersistentMod;

public class PersistentEnchantment extends AbstractEnchantment {

    public PersistentEnchantment() {
        super(new PersistentMod(2), EnchantmentRarity.COMMON);
    }

    @Override
    public String getName() {
        return PersistentMod.TEXT[0];
    }

    @Override
    public String getDescription() {
        return makeModLabel(this.cardModifier.getClass()) + PersistentMod.TEXT[1];
    }
    
}
