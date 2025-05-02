package spireCafe.interactables.merchants.enchanter.enchantments.uncommon;

import spireCafe.interactables.merchants.enchanter.AbstractEnchantment;
import spireCafe.interactables.merchants.enchanter.EnchanterMerchant.EnchantmentRarity;
import spireCafe.interactables.merchants.enchanter.modifiers.NoxiousMod;

public class NoxiousEnchantment extends AbstractEnchantment {

    public NoxiousEnchantment() {
        super(new NoxiousMod(2), EnchantmentRarity.UNCOMMON);
    }

    @Override
    public String getName() {
        return NoxiousMod.TEXT[0];
    }

    @Override
    public String getDescription() {
        return makeModLabel(this.cardModifier.getClass()) + NoxiousMod.TEXT[1];
    }
    
}
