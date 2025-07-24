package spireCafe.interactables.merchants.enchanter.modifiers;

import com.evacipated.cardcrawl.mod.stslib.StSLib;
import com.evacipated.cardcrawl.mod.stslib.actions.common.AllEnemyApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;

import basemod.abstracts.AbstractCardModifier;
import spireCafe.Anniv7Mod;

public class NoxiousMod extends AbstractCardModifier {

    private static final String ID = Anniv7Mod.makeID(NoxiousMod.class.getSimpleName());
    public static final String[] TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    
    private int count;

    public NoxiousMod(int count) {
        this.count = count;
    }

    @Override
    public boolean shouldApply(AbstractCard card) {
        return card.cost != -2;
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        return rawDescription + String.format(TEXT[2], count);
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        this.addToBot(new AllEnemyApplyPowerAction(AbstractDungeon.player, count, (m) -> new PoisonPower(m, AbstractDungeon.player, count)));
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new NoxiousMod(count);
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }
    
}
