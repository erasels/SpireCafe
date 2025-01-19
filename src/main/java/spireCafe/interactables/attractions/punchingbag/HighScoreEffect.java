package spireCafe.interactables.attractions.punchingbag;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
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
    private static final float OFFSET_Y = 300.0F * Settings.scale;
    private static final float OFFSET_X = 25.0F * Settings.scale;
    public static final float DURATION = 2.0F; 

    private float y;
    private float initY;
    private float x;
    private float xVar;

    public HighScoreEffect(float xPos, float yPos) {
        CardCrawlGame.sound.play("UNLOCK_PING");
        this.duration = DURATION;
        this.startingDuration = DURATION;
        this.x = xPos;
        this.initY = yPos;
        this.y = yPos;
        this.color = Settings.GOLD_COLOR.cpy();
        this.xVar = MathUtils.random(-OFFSET_X, OFFSET_X);
    }

    @Override
    public void update() {
        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.0F) {
            this.isDone = true;
            this.duration = 0.0F;
        }

        if (this.duration > 1.8) {
            this.y = Interpolation.bounceOut.apply(this.initY + OFFSET_Y, this.initY, (this.duration - 1.8F));
        }
        if (this.duration > 1.5) {
            this.color.a = Interpolation.pow2In.apply(1.0F, 0.0F, (this.duration - 1.5F));
        } else if (this.duration < 0.5F) {
            this.color.lerp(Color.WHITE, this.duration + 0.5F);
            this.color.a = Interpolation.pow2In.apply(0.0F, 1.0F, this.duration * 2.0F);
        }
    }

    @Override
    public void dispose() {}

    @Override
    public void render(SpriteBatch sb) {
        FontHelper.renderFontCentered(sb, FontHelper.buttonLabelFont, TEXT[0], this.x + this.xVar, this.y, this.color);
    }
    
}
