package spireCafe.interactables.patrons.trashking.relics;

import basemod.helpers.CardPowerTip;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.curses.Clumsy;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import spireCafe.abstracts.AbstractSCRelic;
import spireCafe.interactables.patrons.trashking.TrashKingPatron;

import java.util.ArrayList;

import static spireCafe.Anniv7Mod.makeID;

public class ToyBrick extends AbstractSCRelic {
    public static final String ID = makeID(ToyBrick.class.getSimpleName());

    public ToyBrick() {
        super(ID, TrashKingPatron.assetID, AbstractRelic.RelicTier.SPECIAL, AbstractRelic.LandingSound.FLAT);
        tips.add(new CardPowerTip(new Clumsy()));
    }

    @Override
    public void onEquip() {
        final ArrayList<AbstractCard> cardsToRemove = new ArrayList<>();

        // Collect all Curse cards to be replaced
        for (AbstractCard card : AbstractDungeon.player.masterDeck.group) {
            if (card.type == AbstractCard.CardType.CURSE) {
                cardsToRemove.add(card);
            }
        }

        if (!cardsToRemove.isEmpty()) {
            flash();

            for (AbstractCard oldCard : cardsToRemove) {
                // Properly remove each Curse card using the removeCard method
                AbstractDungeon.player.masterDeck.removeCard(oldCard);

                // Show and obtain a Clumsy card for each Curse card replaced
                AbstractCard newCard = new Clumsy();
                float effectPosX = (float) Settings.WIDTH / 2.0F + (MathUtils.random(-0.2f, 0.2f) * Settings.WIDTH);
                float effectPosY = (float) Settings.HEIGHT / 2.0F + (MathUtils.random(-0.2f, 0.2f) * Settings.HEIGHT);
                AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(newCard, effectPosX, effectPosY));
            }
        }
    }


    @Override
    public boolean canSpawn() {
        for (AbstractCard card : AbstractDungeon.player.masterDeck.group) {
            if (card.type == AbstractCard.CardType.CURSE) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new ToyBrick();
    }
}