package spireCafe.interactables.bartenders.drinkbot;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.powers.RepairPower;

import basemod.abstracts.CustomOrb;
import spireCafe.Anniv7Mod;
import spireCafe.util.Wiz;

public class BloodAlcoholOrb extends CustomOrb {

    private static final int TIMER = 3;
    private static final String ID = Anniv7Mod.makeID(BloodAlcoholOrb.class.getSimpleName());
    private static final OrbStrings orbStrings = CardCrawlGame.languagePack.getOrbString(ID);
    private static final String NAME = orbStrings.NAME;
    private static final String[] DESCRIPTIONS = orbStrings.DESCRIPTION;
    private static final String SFX = "SOTE_SFX_FireIgnite_2_v1.ogg";
    private static final String IMG = Anniv7Mod.makeOrbPath("bloodalcohol.png");

    private static final int BASE_PASSIVE = 2;
    private static final int BASE_EVOKE = 0;
    private int timer = 0;

    public BloodAlcoholOrb() {
        super(ID, NAME, BASE_PASSIVE, BASE_EVOKE, "", "", IMG);
        updateDescription();
    }

    @Override
    public AbstractOrb makeCopy() {
        return new BloodAlcoholOrb();
    }

    @Override
    public void onEndOfTurn() {
        this.timer += 1;
        if (this.timer >= TIMER) {
            this.evokeAmount += this.passiveAmount;
            this.timer = 0;
        }
    }

    @Override
    public void onEvoke() {
        if (this.evokeAmount > 0) {
            Wiz.atb(new ApplyPowerAction(Wiz.p(), Wiz.p(), new RepairPower(Wiz.p(), this.evokeAmount), this.evokeAmount));
        }
    }

    @Override
    protected void renderText(SpriteBatch sb) {
        FontHelper.renderFontCentered(sb, FontHelper.cardEnergyFont_L, Integer.toString(this.timer), this.cX + NUM_X_OFFSET,
                this.cY + this.bobEffect.y / 2.0F + NUM_Y_OFFSET, this.c, this.fontScale);
        FontHelper.renderFontCentered(sb, FontHelper.cardEnergyFont_L, Integer.toString(this.evokeAmount), this.cX + NUM_X_OFFSET,
                this.cY + this.bobEffect.y / 2.0F + NUM_Y_OFFSET, new Color(0.7F, 0.0F, 0.0F, this.c.a), this.fontScale);
    }

    @Override
    public void playChannelSFX() {
        CardCrawlGame.sound.play(SFX, 0.1F);
    }

    @Override
    public void applyFocus() {
    }

    @Override
    public void updateDescription() {
        description = "";
    }
    
}
