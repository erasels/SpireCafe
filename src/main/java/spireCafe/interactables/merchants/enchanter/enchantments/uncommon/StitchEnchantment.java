package spireCafe.interactables.merchants.enchanter.enchantments.uncommon;

import basemod.BaseMod;
import spireCafe.interactables.merchants.enchanter.AbstractEnchantment;
import spireCafe.interactables.merchants.enchanter.EnchanterMerchant.EnchantmentRarity;
import spireCafe.interactables.merchants.enchanter.modifiers.StitchMod;

public class StitchEnchantment extends AbstractEnchantment{

    public StitchEnchantment() {
        super(new StitchMod(), EnchantmentRarity.UNCOMMON);
    }

    @Override
    public String getName() {
        return BaseMod.getKeywordTitle("anniv5:stitch");
    }

    @Override
    public String getDescription() {
        try {
            Class<?> clz = Class.forName("thePackmaster.actions.needlework.StitchAction");
            return makeModLabel(clz) + BaseMod.getKeywordDescription("anniv5:stitch");
        } catch (ClassNotFoundException e) {
            return makeModLabel(this.cardModifier.getClass()) + BaseMod.getKeywordDescription("anniv5:stitch");
        }
    }
    
}
