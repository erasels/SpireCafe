package spireCafe.interactables.patrons.trashking.relics;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import spireCafe.abstracts.AbstractSCRelic;
import spireCafe.interactables.patrons.trashking.TrashKingPatron;

import static spireCafe.Anniv7Mod.makeID;

public class RingOfTheNoodle extends AbstractSCRelic {
    public static final String ID = makeID(RingOfTheNoodle.class.getSimpleName());
    private static final int INNATE_CARD_THRESHOLD = 3;
    private static final int ADDITIONAL_DRAW = 2;

    public RingOfTheNoodle() {
        super(ID, TrashKingPatron.assetID, AbstractRelic.RelicTier.SPECIAL, AbstractRelic.LandingSound.MAGICAL);
    }

    @Override
    public void atBattleStart() {
        int innateCardCount = 0;
        for (AbstractCard card : AbstractDungeon.player.drawPile.group) {
            if (card.isInnate) {
                innateCardCount++;
            }
        }

        if (innateCardCount >= INNATE_CARD_THRESHOLD) {
            flash();
            addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            AbstractDungeon.actionManager.addToTop(new DrawCardAction(AbstractDungeon.player, ADDITIONAL_DRAW));
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new RingOfTheNoodle();
    }
}