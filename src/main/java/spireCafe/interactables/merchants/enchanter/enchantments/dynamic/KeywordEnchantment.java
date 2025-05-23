package spireCafe.interactables.merchants.enchanter.enchantments.dynamic;

import basemod.BaseMod;
import basemod.abstracts.AbstractCardModifier;
import spireCafe.interactables.merchants.enchanter.AbstractEnchantment;
import spireCafe.interactables.merchants.enchanter.EnchanterMerchant.EnchantmentRarity;

public class KeywordEnchantment extends AbstractEnchantment{

    private String keywordString;

    public KeywordEnchantment(AbstractCardModifier cardModifier, EnchantmentRarity rarity, String keywordString) {
        super(cardModifier, rarity);
        this.keywordString = keywordString;
    }

    @Override
    public String getName() {
        return BaseMod.getKeywordTitle(keywordString);
    }

    @Override
    public String getDescription() {
        return makeModLabel(this.cardModifier.getClass()) + BaseMod.getKeywordDescription(keywordString);
    }

    @Override
    public String toString() {
        return super.toString() + ": " + this.keywordString;
    }
    
}
