package spireCafe.interactables.merchants.enchanter;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.cards.CardGroup.CardGroupType;
import com.megacrit.cardcrawl.cards.red.IronWave;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import basemod.patches.whatmod.WhatMod;
import spireCafe.interactables.merchants.enchanter.EnchanterMerchant.EnchantmentRarity;

public abstract class AbstractEnchantment {
    public static AbstractCard tooltipBuddy = new IronWave();

    public AbstractCardModifier cardModifier;
    public EnchantmentRarity rarity;
    
    public AbstractEnchantment(AbstractCardModifier cardModifier, EnchantmentRarity rarity) {
        this.cardModifier = cardModifier;
        this.rarity = rarity;
    }
    
    public CardGroup getValidCards() {
        CardGroup cards = new CardGroup(CardGroupType.UNSPECIFIED);
        for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
            if (CardModifierManager.modifiers(c).isEmpty() && c.type != CardType.CURSE && c.cost != -2) {
                if (cardModifier.shouldApply(c)) {
                    cards.addToTop(c);
                }
            }
        }
        return cards;
    }

    public abstract String getName();

    public abstract String getDescription();

    public static String makeModLabel(Class<?> clz) {
        StringBuilder label = new StringBuilder();
        label.setLength(0);
        for (String w : WhatMod.findModName(clz).split(" ")){
            label.append("#p").append(w).append(" ");
        }
        return label.toString().trim() + " NL ";
    }
    
}
