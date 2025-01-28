package spireCafe.interactables.patrons.trashking.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import spireCafe.abstracts.AbstractSCRelic;
import spireCafe.interactables.patrons.trashking.TrashKingPatron;

import static spireCafe.Anniv7Mod.makeID;

public class FistOfThePugilist extends AbstractSCRelic {
    public static final String ID = makeID(FistOfThePugilist.class.getSimpleName());
    private static final int TEMP_STRENGTH_GAIN = 1;

    public FistOfThePugilist() {
        super(ID, TrashKingPatron.assetID, AbstractRelic.RelicTier.SPECIAL, AbstractRelic.LandingSound.HEAVY);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new FistOfThePugilist();
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m) {
        if (card.hasTag(AbstractCard.CardTags.STRIKE)) {
            this.flash();
            this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            AbstractDungeon.actionManager.addToBottom(
                    new ApplyPowerAction(
                            AbstractDungeon.player,
                            AbstractDungeon.player,
                            new StrengthPower(AbstractDungeon.player, TEMP_STRENGTH_GAIN),
                            TEMP_STRENGTH_GAIN
                    )
            );
            AbstractDungeon.actionManager.addToBottom(
                    new ApplyPowerAction(
                            AbstractDungeon.player,
                            AbstractDungeon.player,
                            new LoseStrengthPower(AbstractDungeon.player, TEMP_STRENGTH_GAIN),
                            TEMP_STRENGTH_GAIN
                    )
            );
        }
    }

}
