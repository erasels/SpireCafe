package spireCafe.interactables.patrons.trashking.relics;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.PotionSlot;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import spireCafe.abstracts.AbstractSCRelic;
import spireCafe.interactables.patrons.trashking.TrashKingPatron;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import static spireCafe.Anniv7Mod.makeID;

public class ElyphantToothpaste extends AbstractSCRelic {
    public static final String ID = makeID(ElyphantToothpaste.class.getSimpleName());

    public ElyphantToothpaste() {
        super(ID, TrashKingPatron.assetID, AbstractRelic.RelicTier.SPECIAL, AbstractRelic.LandingSound.SOLID);
    }

    @Override
    public void onEquip() {
        int potionsDiscarded = 0;
        for (AbstractPotion potion : new ArrayList<>(AbstractDungeon.player.potions)) {
            if (!(potion instanceof PotionSlot)) {
                AbstractDungeon.player.removePotion(potion);
                potionsDiscarded++;
            }
        }

        ArrayList<AbstractCard> upgradableCards = new ArrayList<>();
        for (AbstractCard card : AbstractDungeon.player.masterDeck.group) {
            if (card.canUpgrade()) {
                upgradableCards.add(card);
            }
        }

        Collections.shuffle(upgradableCards, new Random(AbstractDungeon.miscRng.randomLong()));
        int upgradesToPerform = Math.min(potionsDiscarded, upgradableCards.size());
        boolean anyCardUpgraded = false;

        for (int i = 0; i < upgradesToPerform; i++) {
            AbstractCard cardToUpgrade = upgradableCards.get(i);
            cardToUpgrade.upgrade();
            AbstractDungeon.player.bottledCardUpgradeCheck(cardToUpgrade);
            float xOffset = (i % 2 == 0 ? -1 : 1) * AbstractCard.IMG_WIDTH / 2.0F * (0.5F + 0.5F * new Random().nextFloat()) + 20.0F * Settings.scale;
            float yOffset = (new Random().nextFloat() - 0.5F) * 100.0F * Settings.scale;
            AbstractDungeon.topLevelEffectsQueue.add(new ShowCardBrieflyEffect(cardToUpgrade.makeStatEquivalentCopy(), (float)Settings.WIDTH / 2.0F + xOffset, (float)Settings.HEIGHT / 2.0F + yOffset));
            anyCardUpgraded = true;
        }

        if (anyCardUpgraded) {
            AbstractDungeon.topLevelEffectsQueue.add(new UpgradeShineEffect((float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
        }
    }


    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new ElyphantToothpaste();
    }
}
