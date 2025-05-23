package spireCafe.interactables.merchants.enchanter.enchantments.rare;

import spireCafe.interactables.merchants.enchanter.AbstractEnchantment;
import spireCafe.interactables.merchants.enchanter.EnchanterMerchant.EnchantmentRarity;
import spireCafe.interactables.merchants.enchanter.modifiers.AutoplayAndDrawMod;

public class AutoplayEnchantment extends AbstractEnchantment{

    public AutoplayEnchantment() {
        super(new AutoplayAndDrawMod(), EnchantmentRarity.RARE);
    }

    @Override
    public String getName() {
        return AutoplayAndDrawMod.TEXT[0];
    }

    @Override
    public String getDescription() {
        return makeModLabel(this.cardModifier.getClass()) + AutoplayAndDrawMod.TEXT[1];
    }
    
}
