package spireCafe.interactables.patrons.trashking.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.LoseDexterityPower;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import spireCafe.abstracts.AbstractSCRelic;
import spireCafe.interactables.patrons.trashking.TrashKingPatron;

import static spireCafe.Anniv7Mod.makeID;

public class NapiersBones extends AbstractSCRelic {
    public static final String ID = makeID(NapiersBones.class.getSimpleName());
    private static final int TEMP_STRENGTH_AMOUNT = 1;
    private static final int TEMP_DEXTERITY_AMOUNT = 1;
    private boolean triggeredThisCombat = false;

    public NapiersBones() {
        super(ID, TrashKingPatron.assetID, AbstractRelic.RelicTier.SPECIAL, AbstractRelic.LandingSound.CLINK);
    }

    @Override
    public void atBattleStart() {
        triggeredThisCombat = false;
    }

    @Override
    public void onShuffle() {
        if (!triggeredThisCombat) {
            this.flash();
            this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                    new StrengthPower(AbstractDungeon.player, TEMP_STRENGTH_AMOUNT), TEMP_STRENGTH_AMOUNT));
            this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                    new LoseStrengthPower(AbstractDungeon.player, TEMP_STRENGTH_AMOUNT), TEMP_STRENGTH_AMOUNT));
            this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                    new DexterityPower(AbstractDungeon.player, TEMP_DEXTERITY_AMOUNT), TEMP_DEXTERITY_AMOUNT));
            this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                    new LoseDexterityPower(AbstractDungeon.player, TEMP_DEXTERITY_AMOUNT), TEMP_DEXTERITY_AMOUNT));
            this.grayscale = true;
            triggeredThisCombat = true;
        }
    }

    public void justEnteredRoom(AbstractRoom room) {
        this.grayscale = false;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new NapiersBones();
    }
}
