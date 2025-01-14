package spireCafe.interactables.patrons.cleric;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;

import spireCafe.Anniv7Mod;
import spireCafe.abstracts.AbstractSCRelic;
import spireCafe.util.Wiz;

public class MartyrdomMarkRelic extends AbstractSCRelic {

    private static final String ID = Anniv7Mod.makeID(MartyrdomMarkRelic.class.getSimpleName());

    public MartyrdomMarkRelic() {
        super(ID, "ClericPatron", RelicTier.SPECIAL, LandingSound.MAGICAL);
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (info.owner != null && info.type != DamageInfo.DamageType.HP_LOSS && info.type != DamageInfo.DamageType.THORNS && damageAmount > 1) {
            flash();
            addToBot(new GainBlockAction(Wiz.p(), Wiz.p(), damageAmount));
        }
        return damageAmount;
    }
}
