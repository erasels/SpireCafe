package spireCafe.interactables.patrons.trashking.relics;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import spireCafe.abstracts.AbstractSCRelic;
import spireCafe.interactables.patrons.trashking.TrashKingPatron;

import static spireCafe.Anniv7Mod.makeID;

public class Popcorn extends AbstractSCRelic {
    public static final String ID = makeID(Popcorn.class.getSimpleName());
    private static final int BLOCK_AMOUNT = 3;

    public Popcorn() {
        super(ID, TrashKingPatron.assetID, AbstractRelic.RelicTier.SPECIAL, AbstractRelic.LandingSound.FLAT);
    }

    @Override
    public void onPlayerEndTurn() {
        if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            if (AbstractDungeon.player.drawPile.isEmpty()) {
                this.flash();
                this.addToBot(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, BLOCK_AMOUNT));
                this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            }
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new Popcorn();
    }
}
