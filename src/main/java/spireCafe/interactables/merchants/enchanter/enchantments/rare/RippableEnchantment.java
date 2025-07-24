package spireCafe.interactables.merchants.enchanter.enchantments.rare;

import basemod.BaseMod;
import spireCafe.interactables.merchants.enchanter.AbstractEnchantment;
import spireCafe.interactables.merchants.enchanter.EnchanterMerchant.EnchantmentRarity;
import spireCafe.interactables.merchants.enchanter.modifiers.RipMod;

public class RippableEnchantment extends AbstractEnchantment{

    public RippableEnchantment() {
        super(new RipMod(), EnchantmentRarity.RARE);
    }

    @Override
    public String getName() {
        return BaseMod.getKeywordTitle("anniv5:rippable");
    }

    @Override
    public String getDescription() {
        try {
            Class<?> clz = Class.forName("thePackmaster.cardmodifiers.rippack.RippableModifier");
            return makeModLabel(clz) + BaseMod.getKeywordDescription("anniv5:rippable");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(String.format("Error generating enchantment description for %s", this.getClass().getSimpleName()), e);
        }
    }
    
}
