package spireCafe.interactables.merchants.enchanter.enchantments.common;

import basemod.BaseMod;
import spireCafe.interactables.merchants.enchanter.AbstractEnchantment;
import spireCafe.interactables.merchants.enchanter.EnchanterMerchant.EnchantmentRarity;
import spireCafe.interactables.merchants.enchanter.modifiers.MagnetizeMod;

public class MagnetizeEnchantment extends AbstractEnchantment{

    public MagnetizeEnchantment() {
        super(new MagnetizeMod(), EnchantmentRarity.COMMON);
    }

    @Override
    public String getName() {
        return BaseMod.getKeywordTitle("anniv5:magnetized");
    }

    @Override
    public String getDescription() {
        return makeModLabel(this.cardModifier.getClass()) + BaseMod.getKeywordDescription("anniv5:magnetized") + " " + BaseMod.getKeywordDescription("anniv5:polarity");
    }
    
}
