package spireCafe.interactables.npcs.purpletear;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.EnergizedPower;
import spireCafe.abstracts.AbstractSCRelic;

import static spireCafe.Anniv7Mod.makeID;
import static spireCafe.interactables.npcs.purpletear.PurpleTearPatron.assetID;
import static spireCafe.util.Wiz.*;

public class BookOfIndex extends AbstractSCRelic {

    public static final String ID = makeID(BookOfIndex.class.getSimpleName());
    private AbstractCard prescript;
    private AbstractCard.CardType wantType;
    private int num;
    private boolean success;
    private boolean failure;

    public BookOfIndex() {
        super(ID, assetID, RelicTier.SPECIAL, LandingSound.HEAVY);
    }

    @Override
    public void atTurnStart() {
        this.flash();
        generatePrescript();
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (AbstractDungeon.actionManager.cardsPlayedThisTurn.size() > num) {
            return;
        }
        if (card.type == wantType && num == AbstractDungeon.actionManager.cardsPlayedThisTurn.size()) {
            this.flash();
            atb(new RelicAboveCreatureAction(adp(), this));
            applyToSelf(new EnergizedPower(adp(), 1));
            success = true;
        } else if (AbstractDungeon.actionManager.cardsPlayedThisTurn.size() >= num) {
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
        num = AbstractDungeon.miscRng.random(2) + 1;
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
    public void onVictory() {
        prescript = null;
    }

    @Override
    public void renderInTopPanel(SpriteBatch sb) {
        super.render(sb);
        if (prescript != null) {
            float drawScale = 0.65f;
            float offsetX = 40.0F * Settings.scale;
            float offsetY = -150.0F * Settings.scale;
            AbstractCard card = prescript;
            card.drawScale = drawScale;
            card.current_x = this.hb.x + offsetX;
            card.current_y = this.hb.y + offsetY;
            card.render(sb);
        }
    }
}
