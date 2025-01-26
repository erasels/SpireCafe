package spireCafe.interactables.patrons.trashking.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import spireCafe.abstracts.AbstractSCRelic;
import spireCafe.interactables.patrons.trashking.TrashKingPatron;

import static spireCafe.Anniv7Mod.makeID;

public class RayOfMoonlight extends AbstractSCRelic {
    public static final String ID = makeID(RayOfMoonlight.class.getSimpleName());
    private static final int ARTIFACT_AMOUNT = 2;

    public RayOfMoonlight() {
        super(ID, TrashKingPatron.assetID, AbstractRelic.RelicTier.SPECIAL, AbstractRelic.LandingSound.MAGICAL);
    }

    @Override
    public void atBattleStart() {
        boolean hasRare = false;
        for (AbstractCard card : AbstractDungeon.player.masterDeck.group) {
            if (card.rarity == AbstractCard.CardRarity.RARE) {
                hasRare = true;
                break;
            }
        }

        if (!hasRare) {
            this.flash();
            this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                    new ArtifactPower(AbstractDungeon.player, ARTIFACT_AMOUNT), ARTIFACT_AMOUNT));
            this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new RayOfMoonlight();
    }
}
