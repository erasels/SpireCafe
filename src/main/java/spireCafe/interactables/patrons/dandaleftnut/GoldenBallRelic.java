package spireCafe.interactables.patrons.dandaleftnut;

import java.util.ArrayList;
import java.util.Collections;

import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.tempCards.Shiv;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.PowerTip;
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
        counter = GHOSTS_TO_ACTIVATE;
        grayscale = true;
    }

    @Override
    public void atBattleStartPreDraw() {
        CardGroup drawPileCopy = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        drawPileCopy.group.addAll(Wiz.p().drawPile.group);
        Collections.shuffle(drawPileCopy.group);

        ArrayList<Integer> ghostIndices = new ArrayList<>();
        ghostIndices.add(0);
        ghostIndices.add(1);
        ghostIndices.add(2);

        int i = 0;
        while (ghostIndices.size() > 0 && i < drawPileCopy.size()) {
            AbstractCard c = drawPileCopy.group.get(i);
            if (c.type != AbstractCard.CardType.CURSE || c.type == AbstractCard.CardType.CURSE && c.isEthereal == false) {
                CardModifierManager.addModifier(c, new GhostModifier(ghostIndices.remove(0)));
            }
            i++;
        }
        if (!grayscale) {
            Wiz.atb(new RelicAboveCreatureAction(Wiz.p(), this));
            Wiz.applyToSelf(new DuplicationPower(Wiz.p(), 1));
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + String.format(DESCRIPTIONS[1], GHOSTS_TO_ACTIVATE);
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
        if (grayscale && counter > 1) {
            counter--;
            this.description = DESCRIPTIONS[0] + String.format(DESCRIPTIONS[1], counter);
            this.tips.clear();
            this.tips.add(new PowerTip(this.name, this.description));
            this.initializeTips();
        }
        if (counter == 1) {
            counter = -1;
            grayscale = false;
            CardCrawlGame.sound.play("ORB_PLASMA_EVOKE");
            this.description = DESCRIPTIONS[0] + DESCRIPTIONS[2];
            this.tips.clear();
            this.tips.add(new PowerTip(this.name, this.description));
            this.initializeTips();
            flash();
        }
    }
}
