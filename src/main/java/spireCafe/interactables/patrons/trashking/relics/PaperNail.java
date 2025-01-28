package spireCafe.interactables.patrons.trashking.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import spireCafe.abstracts.AbstractSCRelic;
import spireCafe.interactables.patrons.trashking.powers.PaperNailPower;
import spireCafe.interactables.patrons.trashking.TrashKingPatron;

import static spireCafe.Anniv7Mod.makeID;

public class PaperNail extends AbstractSCRelic {
    public static final String ID = makeID(PaperNail.class.getSimpleName());

    public PaperNail() {
        super(ID, TrashKingPatron.assetID, AbstractRelic.RelicTier.SPECIAL, AbstractRelic.LandingSound.FLAT);
    }

    @Override
    public void atBattleStart() {
        if (AbstractDungeon.player.currentHealth == 1) {
            AbstractDungeon.actionManager.addToBottom(
                    new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new PaperNailPower(AbstractDungeon.player, 1), 1));
        }
    }

    @Override
    public void onLoseHp(int damageAmount) {
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            if (AbstractDungeon.player.currentHealth - damageAmount == 1 && !AbstractDungeon.player.hasPower(PaperNailPower.POWER_ID)) {
                flash();
                addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
                AbstractDungeon.actionManager.addToBottom(
                        new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new PaperNailPower(AbstractDungeon.player, 1), 1));
            }
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new PaperNail();
    }
}