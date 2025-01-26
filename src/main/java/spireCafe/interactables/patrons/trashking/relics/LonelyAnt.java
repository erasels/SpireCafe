package spireCafe.interactables.patrons.trashking.relics;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import spireCafe.abstracts.AbstractSCRelic;
import spireCafe.interactables.patrons.trashking.TrashKingPatron;

import static spireCafe.Anniv7Mod.makeID;

public class LonelyAnt extends AbstractSCRelic {
    public static final String ID = makeID(LonelyAnt.class.getSimpleName());
    private static final int TEMP_STRENGTH_AMOUNT = 1;

    public LonelyAnt() {
        super(ID, TrashKingPatron.assetID, AbstractRelic.RelicTier.SPECIAL, AbstractRelic.LandingSound.FLAT);
    }

    @Override
    public void atTurnStartPostDraw() {
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                isDone = true;  // Make sure to mark this action as complete after execution.

                boolean hasUncommonOrRare = false;

                // Check each card in the hand for Uncommon or Rare rarity.
                for (AbstractCard card : AbstractDungeon.player.hand.group) {
                    if (card.rarity == AbstractCard.CardRarity.UNCOMMON || card.rarity == AbstractCard.CardRarity.RARE) {
                        hasUncommonOrRare = true;
                        break;
                    }
                }

                // Apply effects if no Uncommon or Rare cards are present.
                if (!hasUncommonOrRare) {
                    flash();
                    addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, LonelyAnt.this));
                    addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                            new StrengthPower(AbstractDungeon.player, TEMP_STRENGTH_AMOUNT), TEMP_STRENGTH_AMOUNT));
                    addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                            new LoseStrengthPower(AbstractDungeon.player, TEMP_STRENGTH_AMOUNT), TEMP_STRENGTH_AMOUNT));
                }
            }
        });
    }


    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new LonelyAnt();
    }
}
