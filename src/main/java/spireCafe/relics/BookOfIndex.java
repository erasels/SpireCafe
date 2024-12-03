package spireCafe.relics;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.EnergizedPower;
import spireCafe.abstracts.AbstractSCRelic;
import spireCafe.cards.Prescript;

import static spireCafe.util.Wiz.*;

public class BookOfIndex extends AbstractSCRelic {

    public static final String ID = BookOfIndex.class.getSimpleName();
    private AbstractCard prescript;
    private AbstractCard.CardType wantType;
    private int num;
    private boolean success;
    private boolean failure;

    public BookOfIndex() {
        super(ID, RelicTier.SPECIAL, LandingSound.HEAVY);
    }

    @Override
    public void atTurnStart() {
        this.flash();
        generatePrescript();
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card.type == wantType && num == AbstractDungeon.actionManager.cardsPlayedThisTurn.size()) {
            this.flash();
            atb(new RelicAboveCreatureAction(adp(), this));
            applyToSelf(new EnergizedPower(adp(), 1));
            success = true;
        } else if (num >= AbstractDungeon.actionManager.cardsPlayedThisTurn.size()) {
            this.flash();
            failure = true;
        }
        if (success || failure) {
            prescript.rawDescription = generatedPrescriptString();
            prescript.initializeDescription();
        }
    }

    private void generatePrescript() {
        success = false;
        failure = false;
        prescript = new Prescript();
        num = AbstractDungeon.miscRng.random(1, 3);
        if (AbstractDungeon.miscRng.randomBoolean()) {
            wantType = AbstractCard.CardType.ATTACK;
        } else {
            wantType = AbstractCard.CardType.SKILL;
        }
        prescript.rawDescription = generatedPrescriptString();
        prescript.initializeDescription();
    }

    private String generatedPrescriptString() {
        String type;
        if (wantType == AbstractCard.CardType.ATTACK) {
            type = DESCRIPTIONS[4];
        } else {
            type = DESCRIPTIONS[5];
        }
        String result = "";
        if (success) {
            result = DESCRIPTIONS[7];
        }
        if (failure) {
            result = DESCRIPTIONS[8];
        }
        return String.format(DESCRIPTIONS[6] + type + result, DESCRIPTIONS[num]);
    }

    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);
        if (prescript != null) {
            float drawScale = 0.65f;
            float offsetX1 = 300.0F * Settings.scale;
            float offsetY = 100.0F * Settings.scale;
            AbstractCard card = prescript;
            card.drawScale = drawScale;
            card.current_x = this.hb.x;
            card.current_y = this.hb.y + offsetY;
            card.render(sb);
        }
    }
}
