package spireCafe.interactables.merchants.enchanter.enchantments.uncommon;

import spireCafe.interactables.merchants.enchanter.AbstractEnchantment;
import spireCafe.interactables.merchants.enchanter.EnchanterMerchant.EnchantmentRarity;
import spireCafe.interactables.merchants.enchanter.modifiers.LootMod;

public class LootEnchantment extends AbstractEnchantment {

    public LootEnchantment() {
        super(new LootMod(), EnchantmentRarity.UNCOMMON);
    }

    @Override
    public String getName() {
        return LootMod.TEXT[0];
    }

    @Override
    public String getDescription() {
        return makeModLabel(this.cardModifier.getClass()) + LootMod.TEXT[1];
    }
    
}
