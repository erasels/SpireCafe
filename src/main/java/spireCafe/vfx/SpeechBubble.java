package spireCafe.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;

public class SpeechBubble {
    private static final float SHADOW_OFFSET;
    private static final float WAVY_DISTANCE;
    private float shadow_offset;
    private float x;
    private float y;
    private float wavy_y;
    private float wavyHelper;
    private float scaleTimer;
    private float minScaleTimer;
    private float currScaleTimer;
    private Color shadowColor;
    private Color color;
    public float scale;
    public boolean isGrowing = false;

    public SpeechBubble(float x, float y, float scaleTimer, String msg) {
        this.shadow_offset = 0.0F;
        this.scaleTimer = scaleTimer;
        this.minScaleTimer = scaleTimer / 6;
        this.shadowColor = new Color(0.0F, 0.0F, 0.0F, 0.0F);
        this.x = x - 50.0F * Settings.scale;
        this.y = y;
        this.color = new Color(0.8F, 0.9F, 0.9F, 1.0F);
    }

    public void update() {
        this.updateScale();
        this.wavyHelper += Gdx.graphics.getDeltaTime() * 4.0F;
        this.wavy_y = MathUtils.sin(this.wavyHelper) * WAVY_DISTANCE;
        this.shadow_offset = MathUtils.lerp(this.shadow_offset, SHADOW_OFFSET, Gdx.graphics.getDeltaTime() * 4.0F);
    }

    private void updateScale() {
        if (isGrowing) {
            this.currScaleTimer += Gdx.graphics.getDeltaTime();
        } else {
            this.currScaleTimer -= Gdx.graphics.getDeltaTime();
        }
        if (this.currScaleTimer < minScaleTimer) {
            this.currScaleTimer = minScaleTimer;
        }
        if (this.currScaleTimer > scaleTimer) {
            this.currScaleTimer = scaleTimer;
        }
        this.scale = Interpolation.linear.apply(Settings.scale / 5.0f, Settings.scale / 2.0F, this.currScaleTimer / 0.3F);
    }

    public void render(SpriteBatch sb) {
        this.shadowColor.a = this.color.a / 4.0F;
        sb.setColor(this.shadowColor);
        sb.draw(ImageMaster.SPEECH_BUBBLE_IMG, this.x - 512.0F + this.shadow_offset, this.y - 256.0F + this.wavy_y - this.shadow_offset, 512.0F, 256.0F, 512.0F, 512.0F, this.scale, this.scale, 0, 0, 0, 512, 512, true, false);
        sb.setColor(this.color);
        sb.draw(ImageMaster.SPEECH_BUBBLE_IMG, this.x - 512.0F, this.y - 256.0F + this.wavy_y, 512.0F, 256.0F, 512.0F, 512.0F, this.scale, this.scale, 0, 0, 0, 512, 512, true, false);
    }

    public void dispose() {
    }

    static {
        SHADOW_OFFSET = 16.0F * Settings.scale;
        WAVY_DISTANCE = 2.0F * Settings.scale;
    }
}
