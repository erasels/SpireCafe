package spireCafe.interactables.patrons.trashking.relics;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import spireCafe.abstracts.AbstractSCRelic;
import spireCafe.interactables.patrons.trashking.TrashKingPatron;

import static spireCafe.Anniv7Mod.makeID;

public class Loan extends AbstractSCRelic {
    public static final String ID = makeID(Loan.class.getSimpleName());
    private static final int INITIAL_GOLD_GAIN = 200;
    private static final int GOLD_LOSS_PER_TURN = 10;
    private static final int TOTAL_TURNS = 23;

    public Loan() {
        super(ID, TrashKingPatron.assetID, AbstractRelic.RelicTier.SPECIAL, AbstractRelic.LandingSound.FLAT);
        this.counter = TOTAL_TURNS;
    }

    @Override
    public void onEquip() {
        CardCrawlGame.sound.play("GOLD_GAIN");
        AbstractDungeon.player.gainGold(INITIAL_GOLD_GAIN);
    }

    @Override
    public void atTurnStart() {
        if (this.counter > 0) {
            this.flash();
            AbstractDungeon.player.loseGold(GOLD_LOSS_PER_TURN);
            this.counter--;
        }
        if (this.counter == 0) {
            this.grayscale = true;
            this.counter = -1;
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new Loan();
    }
}
