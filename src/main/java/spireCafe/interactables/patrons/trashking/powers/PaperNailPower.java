package spireCafe.interactables.patrons.trashking.powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import spireCafe.abstracts.AbstractSCPower;
import spireCafe.interactables.patrons.trashking.TrashKingPatron;

import static spireCafe.Anniv7Mod.makeID;

public class PaperNailPower extends AbstractSCPower {
    public static String POWER_ID = makeID(PaperNailPower.class.getSimpleName());
    public static PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static String[] DESCRIPTIONS = strings.DESCRIPTIONS;

    public PaperNailPower(AbstractCreature owner, int amount) {
        super(POWER_ID, strings.NAME, TrashKingPatron.assetID, AbstractPower.PowerType.BUFF, false, owner, amount);
    }

    public void checkAndRemovePower() {
        if (this.owner.currentHealth > 1 && this.owner.hasPower(PaperNailPower.POWER_ID)) {
            this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, PaperNailPower.POWER_ID));
        }
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type) {
        if (type == DamageInfo.DamageType.NORMAL && this.owner.currentHealth == 1) {
            return (float) (damage * Math.pow(3, this.amount));
        }
        return damage;
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0];
    }
}
