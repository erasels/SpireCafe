package spireCafe.interactables.merchants.enchanter.modifiers;

import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.AutoplayField;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import basemod.abstracts.AbstractCardModifier;
import spireCafe.Anniv7Mod;

public class AutoplayAndDrawMod extends AbstractCardModifier {
    
    private static final String ID = Anniv7Mod.makeID(AutoplayAndDrawMod.class.getSimpleName());
    public static final String[] TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }

    @Override
    public boolean shouldApply(AbstractCard card) {
        return AutoplayField.autoplay.get(card) && card.cost >= 0;
    }

    @Override
    public void onDrawn(AbstractCard card) {
        AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
            @Override
            public void update() {
                if (AbstractDungeon.player.hand.contains(card)) {
                    AbstractDungeon.actionManager.addToTop(new NewQueueCardAction(card, AbstractDungeon.getCurrRoom().monsters.getRandomMonster(null, true, AbstractDungeon.cardRandomRng)));
                }
                this.isDone = true;
            }
        });
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        addToBot(new DrawCardAction(1));
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new AutoplayAndDrawMod();
    }
    
    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        return TEXT[2] + rawDescription + String.format(TEXT[3], 1);
    }
}
