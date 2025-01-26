package spireCafe.interactables.patrons.trashking.relics;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import spireCafe.abstracts.AbstractSCRelic;
import spireCafe.interactables.patrons.trashking.TrashKingPatron;

import static spireCafe.Anniv7Mod.makeID;

public class DesignersBrush extends AbstractSCRelic {
    public static final String ID = makeID(DesignersBrush.class.getSimpleName());

    public DesignersBrush() {
        super(ID, TrashKingPatron.assetID, AbstractRelic.RelicTier.SPECIAL, AbstractRelic.LandingSound.MAGICAL);
    }

    @Override
    public void atBattleStart() {
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                isDone = true;
                boolean hasUpgradedCard = false;
                for (AbstractCard c : AbstractDungeon.player.hand.group) {
                    if (c.upgraded) {
                        hasUpgradedCard = true;
                        break;
                    }
                }

                if (!hasUpgradedCard) {
                    addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, DesignersBrush.this));
                    for (AbstractCard c : AbstractDungeon.player.hand.group) {
                        c.upgrade();
                        c.superFlash();
                    }
                    grayscale = true; // Set the relic to grayscale
                }
            }
        });
    }

    @Override
    public void onEnterRoom(AbstractRoom room) { // Reset grayscale when entering a new room
        grayscale = false;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new DesignersBrush();
    }
}
