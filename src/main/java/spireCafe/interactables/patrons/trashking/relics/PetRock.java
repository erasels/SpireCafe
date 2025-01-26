package spireCafe.interactables.patrons.trashking.relics;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import spireCafe.abstracts.AbstractSCRelic;
import spireCafe.interactables.patrons.trashking.TrashKingPatron;

import static spireCafe.Anniv7Mod.makeID;

public class PetRock extends AbstractSCRelic {
    public static final String ID = makeID(PetRock.class.getSimpleName());
    private static final int ENERGY_GAIN = 1;
    private boolean playedCardLastTurn = false;
    public PetRock() {
        super(ID, TrashKingPatron.assetID, AbstractRelic.RelicTier.SPECIAL, AbstractRelic.LandingSound.HEAVY);
    }

    @Override
    public void atTurnStart() {
        if (!playedCardLastTurn) {
            this.flash();
            this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            AbstractDungeon.actionManager.addToTop(new GainEnergyAction(ENERGY_GAIN));
        }
        playedCardLastTurn = false;
    }

    @Override
    public void onUseCard(AbstractCard c, UseCardAction action) {
        playedCardLastTurn = true;
    }

    @Override
    public void atPreBattle() {
        playedCardLastTurn = true;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new PetRock();
    }
}
