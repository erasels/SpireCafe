package spireCafe.interactables.patrons.trashking.relics;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import spireCafe.abstracts.AbstractSCRelic;
import spireCafe.interactables.patrons.trashking.TrashKingPatron;

import java.util.HashSet;

import static spireCafe.Anniv7Mod.makeID;

public class StylePoints extends AbstractSCRelic {
    public static final String ID = makeID(StylePoints.class.getSimpleName());

    public StylePoints() {
        super(ID, TrashKingPatron.assetID, AbstractRelic.RelicTier.SPECIAL, AbstractRelic.LandingSound.SOLID);
    }

    @Override
    public void onVictory() {
        if (AbstractDungeon.getCurrMapNode() != null && AbstractDungeon.getCurrRoom() instanceof MonsterRoomBoss) {
            HashSet<String> uniqueCardIDs = new HashSet<>();
            for (AbstractCard card : AbstractDungeon.player.masterDeck.group) {
                uniqueCardIDs.add(card.cardID);
            }
            int goldGain = uniqueCardIDs.size();
            AbstractDungeon.player.gainGold(goldGain);
            this.flash();
            CardCrawlGame.sound.play("GOLD_GAIN");
        }
    }



    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new StylePoints();
    }
}