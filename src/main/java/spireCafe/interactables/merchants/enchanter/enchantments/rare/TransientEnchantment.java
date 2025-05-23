package spireCafe.interactables.merchants.enchanter.enchantments.rare;

import spireCafe.Anniv7Mod;
import spireCafe.cardmods.TransientMod;
import spireCafe.interactables.merchants.enchanter.AbstractEnchantment;
import spireCafe.interactables.merchants.enchanter.EnchanterMerchant.EnchantmentRarity;
import spireCafe.util.cutsceneStrings.LocalizedCutsceneStrings;

public class TransientEnchantment extends AbstractEnchantment{

    private static final String[] transientStr = LocalizedCutsceneStrings.getCutsceneStrings(Anniv7Mod.makeID("TheTransientCutscene")).OPTIONS;

    public TransientEnchantment() {
        super(new TransientMod(), EnchantmentRarity.RARE);
    }

    @Override
    public String getName() {
        return transientStr[2];
    }

    @Override
    public String getDescription() {
        return makeModLabel(this.cardModifier.getClass()) + transientStr[3];
    }
    
}
