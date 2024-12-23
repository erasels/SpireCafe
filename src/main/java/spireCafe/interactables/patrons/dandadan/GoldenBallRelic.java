package spireCafe.interactables.patrons.dandadan;

import java.util.ArrayList;
import java.util.Collections;

import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
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
        super(ID, "Dandadan", RelicTier.SPECIAL, LandingSound.CLINK);
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
            if (c.type != AbstractCard.CardType.CURSE
                    || c.type == AbstractCard.CardType.CURSE && c.isEthereal == false) {
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
        if (grayscale) {
            return DESCRIPTIONS[0] + String.format(DESCRIPTIONS[1], counter);
        } else {
            return DESCRIPTIONS[0] + DESCRIPTIONS[2];
        }
    }

    @Override
    public void onRightClick() {
        speak("TESTING ~TESTING~ #rTESTING #yTESTING #bTESTING ", 3.0F);
    }

    public void speak(String msg, float duration) {
        boolean flipX = (this.hb.cX <= Settings.WIDTH * 0.70F);
        float draw_x;
        if (flipX) {
            draw_x = hb.cX + 20.0F * Settings.scale;
        } else {
            draw_x = hb.cX - 20.0F * Settings.scale;
        }
        AbstractDungeon.effectList.add(new TopLeftSpeechBubble(draw_x, hb.cY - 295.0F * Settings.scale, duration, msg, flipX));
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
        } else if (counter == 1) {
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
