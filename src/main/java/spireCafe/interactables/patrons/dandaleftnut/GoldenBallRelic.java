package spireCafe.interactables.patrons.dandaleftnut;

import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.tempCards.Shiv;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.powers.DuplicationPower;

import basemod.helpers.CardModifierManager;
import spireCafe.Anniv7Mod;
import spireCafe.abstracts.AbstractSCRelic;
import spireCafe.util.Wiz;

public class GoldenBallRelic extends AbstractSCRelic implements ClickableRelic {

    public static final String ID = Anniv7Mod.makeID(GoldenBallRelic.class.getSimpleName());
    private static final int GHOSTS_TO_ACTIVATE = 12;

    public GoldenBallRelic() {
        super(ID, "Dandaleftnut", RelicTier.SPECIAL, LandingSound.CLINK);
        counter = 0;
        grayscale = true;
    }

    @Override
    public void atBattleStartPreDraw() {
        if (grayscale) {
            for (AbstractCard c : Wiz.p().drawPile.group) {
                if (c instanceof Shiv) {
                    CardModifierManager.addModifier(c, new GhostModifier());
                }
            }
        } else {
            Wiz.applyToSelf(new DuplicationPower(Wiz.p(), 1));
        }

    }

    @Override
    public void onRightClick() {
        AbstractCard c = new Shiv();
        CardModifierManager.addModifier(c, new GhostModifier());
        Wiz.makeInHand(c);
    }

    @Override
    public void onUseCard(AbstractCard targetCard, UseCardAction useCardAction) {
        if (CardModifierManager.hasModifier(targetCard, GhostModifier.ID)) {
            incrementCounter();
        }
    }

    private void incrementCounter() {
        if (grayscale && counter < GHOSTS_TO_ACTIVATE) {
            counter++;
        }
        if (counter >= GHOSTS_TO_ACTIVATE) {
            counter = -1;
            grayscale = false;
            CardCrawlGame.sound.play("ORB_PLASMA_EVOKE");
            flash();
        }
    }
}
