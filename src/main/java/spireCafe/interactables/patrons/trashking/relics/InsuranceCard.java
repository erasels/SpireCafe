package spireCafe.interactables.patrons.trashking.relics;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import spireCafe.abstracts.AbstractSCRelic;
import spireCafe.interactables.patrons.trashking.TrashKingPatron;

import static spireCafe.Anniv7Mod.makeID;

public class InsuranceCard extends AbstractSCRelic {
    public static final String ID = makeID(InsuranceCard.class.getSimpleName());
    private static final int GOLD_GAIN = 150;

    public InsuranceCard() {
        super(ID, TrashKingPatron.assetID, AbstractRelic.RelicTier.SPECIAL, AbstractRelic.LandingSound.FLAT);
    }

    @Override
    public void onEnterRoom(AbstractRoom room) {
        if (AbstractDungeon.player.gold == 0) {
            this.flash();
            CardCrawlGame.sound.play("GOLD_GAIN");
            AbstractDungeon.player.gainGold(GOLD_GAIN);
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new InsuranceCard();
    }
}
