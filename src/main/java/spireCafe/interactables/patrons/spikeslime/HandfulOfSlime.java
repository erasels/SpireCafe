package spireCafe.interactables.patrons.spikeslime;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.status.Slimed;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import spireCafe.abstracts.AbstractSCRelic;

import static spireCafe.Anniv7Mod.makeID;

public class HandfulOfSlime extends AbstractSCRelic {
    public static final String ID = makeID(HandfulOfSlime.class.getSimpleName());

    public static final int SMALL_SLIMY = 6;
    public static final int BIG_SLIMY = 99;

    public HandfulOfSlime() {
        this(SMALL_SLIMY);
    }

    public HandfulOfSlime(int sliminess) {
        super(ID, SpikeSlimePatron.assetID, AbstractRelic.RelicTier.SPECIAL, AbstractRelic.LandingSound.SOLID);
        this.counter = sliminess;

        updateNameAndDescription();
    }

    public void atBattleStart() {
        if (this.counter > 0) {
            this.flash();
            this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            this.addToBot(new MakeTempCardInDrawPileAction(new Slimed(), 1, true, true));
            this.setCounter(this.counter-1);
            updateNameAndDescription();
        }
    }

    //neow's logic
    public void setCounter(int setCounter) {
        this.counter = setCounter;
        if (setCounter <= 0) {
            this.usedUp();
        }
    }

    //have relic description update to the remaining count since the patron can add it at different amounts
    public void updateNameAndDescription(){
        this.description = this.getUpdatedDescription();
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        this.initializeTips();
    }

    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + this.counter + DESCRIPTIONS[1];
    }
}
