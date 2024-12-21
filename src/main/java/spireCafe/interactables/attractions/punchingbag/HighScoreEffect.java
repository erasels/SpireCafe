package spireCafe.interactables.attractions.punchingbag;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

import spireCafe.Anniv7Mod;

public class HighScoreEffect extends AbstractGameEffect {
    public static final String ID = Anniv7Mod.makeID(HighScoreEffect.class.getSimpleName());
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ID);
    private static final String[] TEXT = uiStrings.TEXT;
    private static final float Y_OFFSET = 150.0F * Settings.scale;
    public static final float DURATION = 3.0F; 

    private float y;
    private float x;

    public HighScoreEffect(float xPos, float yPos) {
        CardCrawlGame.sound.play("UNLOCK_PING");
        this.duration = DURATION;
        this.startingDuration = DURATION;
        this.x = xPos;
        this.y = yPos;
        this.color = Settings.GOLD_COLOR.cpy();
    }

    @Override
    public void update() {
        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.0F) {
            this.isDone = true;
            this.duration = 0.0F;
        }

        if (this.duration > 2.5) {
            this.color.a = Interpolation.pow2In.apply(1.0F, 0.0F, (this.duration - 2.5F));
        } else if (this.duration < 0.5F) {
            this.color.a = Interpolation.pow2In.apply(0.0F, 1.0F, this.duration * 2.0F);
        }
    }

    @Override
    public void dispose() {}

    @Override
    public void render(SpriteBatch sb) {
        FontHelper.renderFontCentered(sb, FontHelper.buttonLabelFont, TEXT[0], this.x, this.y, this.color);
    }
    
}
