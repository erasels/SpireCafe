package spireCafe.interactables.patrons.trashking.relics;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import spireCafe.abstracts.AbstractSCRelic;
import spireCafe.interactables.patrons.trashking.TrashKingPatron;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import static spireCafe.Anniv7Mod.makeID;

public class HeirloomFork extends AbstractSCRelic {
    public static final String ID = makeID(HeirloomFork.class.getSimpleName());

    public HeirloomFork() {
        super(ID, TrashKingPatron.assetID, AbstractRelic.RelicTier.SPECIAL, AbstractRelic.LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new HeirloomFork();
    }

    public void onRemoveCurseFromDeck() {
        ArrayList<AbstractCard> basicCards = new ArrayList<>();
        for (AbstractCard card : AbstractDungeon.player.masterDeck.group) {
            if (card.rarity == AbstractCard.CardRarity.BASIC && card.canUpgrade()) {
                basicCards.add(card);
            }
        }

        if (!basicCards.isEmpty()) {
            this.flash();
            Collections.shuffle(basicCards, new Random(AbstractDungeon.miscRng.randomLong()));
            AbstractCard cardToUpgrade = basicCards.get(0);
            cardToUpgrade.upgrade();
            AbstractDungeon.player.bottledCardUpgradeCheck(cardToUpgrade);
            AbstractDungeon.topLevelEffectsQueue.add(new ShowCardBrieflyEffect(cardToUpgrade.makeStatEquivalentCopy(), (float) Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
            AbstractDungeon.topLevelEffectsQueue.add(new UpgradeShineEffect((float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
        }
    }
}
