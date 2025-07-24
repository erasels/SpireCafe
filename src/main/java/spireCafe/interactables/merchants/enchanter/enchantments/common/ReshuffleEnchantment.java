package spireCafe.interactables.merchants.enchanter.enchantments.common;

import spireCafe.interactables.merchants.enchanter.AbstractEnchantment;
import spireCafe.interactables.merchants.enchanter.EnchanterMerchant.EnchantmentRarity;
import spireCafe.interactables.merchants.enchanter.modifiers.ReshuffleMod;

public class ReshuffleEnchantment extends AbstractEnchantment{

    public ReshuffleEnchantment() {
        super(new ReshuffleMod(), EnchantmentRarity.COMMON);
    }

    @Override
    public String getName() {
        return ReshuffleMod.TEXT[0];
    }

    @Override
    public String getDescription() {
        return makeModLabel(this.cardModifier.getClass()) + ReshuffleMod.TEXT[1];
    }
    
}
