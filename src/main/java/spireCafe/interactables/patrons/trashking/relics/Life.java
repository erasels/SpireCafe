package spireCafe.interactables.patrons.trashking.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.ConstrictedPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import spireCafe.abstracts.AbstractSCRelic;
import spireCafe.interactables.patrons.trashking.TrashKingPatron;

import static spireCafe.Anniv7Mod.makeID;

public class Life extends AbstractSCRelic {
    public static final String ID = makeID(Life.class.getSimpleName());

    public Life() {
        super(ID, TrashKingPatron.assetID, AbstractRelic.RelicTier.SPECIAL, AbstractRelic.LandingSound.MAGICAL);
    }

    @Override
    public void atBattleStart() {
        flash();
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new ConstrictedPower(AbstractDungeon.player, AbstractDungeon.player, 1), 1));
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new Life();
    }
}