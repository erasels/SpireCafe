package spireCafe.interactables.merchants.enchanter.modifiers;

import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.green.Prepared;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;

import basemod.ReflectionHacks;
import basemod.abstracts.AbstractCardModifier;
import spireCafe.Anniv7Mod;

public class LootMod extends AbstractCardModifier{
    
    public static final String ID = Anniv7Mod.makeID(LootMod.class.getSimpleName());
    public static final String[] TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;

    public static final int DRAW = 1;
    private boolean modifiedBase;

    @Override
    public void onInitialApplication(AbstractCard card) {
        if (card instanceof Prepared) {
            card.baseMagicNumber += DRAW;
            card.magicNumber += DRAW;
            modifiedBase = true;
            if (!card.upgraded) {
                card.rawDescription = ReflectionHacks.<CardStrings>getPrivateStatic(Prepared.class, "cardStrings").UPGRADE_DESCRIPTION;
                card.initializeDescription();
            }
        }
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        if (!modifiedBase) {
            addToBot(new DrawCardAction(DRAW));
            addToBot(new DiscardAction(AbstractDungeon.player, AbstractDungeon.player, DRAW, false));
        }
    }

    @Override
    public boolean shouldApply(AbstractCard card) {
        return card.cost != -2;
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        if (modifiedBase) {
            return rawDescription;
        }
        return rawDescription + String.format(TEXT[2], DRAW, DRAW);
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new LootMod();
    }
    
    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }
}
