package spireCafe.interactables.patrons.trashking.relics;

import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.PotionSlot;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import spireCafe.abstracts.AbstractSCRelic;
import spireCafe.interactables.patrons.trashking.TrashKingPatron;

import static spireCafe.Anniv7Mod.makeID;

public class SlimeUvula extends AbstractSCRelic {
    public static final String ID = makeID(SlimeUvula.class.getSimpleName());

    private static final int HEAL = 2;

    public SlimeUvula() {
        super(ID, TrashKingPatron.assetID, AbstractRelic.RelicTier.SPECIAL, AbstractRelic.LandingSound.FLAT);
    }

    @Override
    public void onVictory() {
        boolean allPotionsFilled = true;
        for (AbstractPotion potion : AbstractDungeon.player.potions) {
            if (potion instanceof PotionSlot) {
                allPotionsFilled = false;
                break;
            }
        }

        if (allPotionsFilled) {
            AbstractPlayer p = AbstractDungeon.player;
            this.flash();
            this.addToTop(new RelicAboveCreatureAction(p, this));
            if (p.currentHealth > 0) {
                p.heal(HEAL);
            }
        }
    }


    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new SlimeUvula();
    }
}