package spireCafe.util.decorationSystem;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import spireCafe.CafeRoom;
import spireCafe.abstracts.AbstractCutscene;

public class Decoration {
    public Texture img;
    public Hitbox hb;
    public float width, height;
    public float x, y;
    public String id;
    public boolean isLarge;
    public float fixedY = 0, fixedX = 0;

    protected String clickSound = "UI_CLICK_1"; // Default sound
    protected boolean shakeEnabled = true; // Shake effect toggle
    protected float shakeDuration = 0f;
    protected float shakeOffsetX = 0f;
    protected float shakeOffsetY = 0f;

    public Decoration(String id, Texture img, float x, float y) {
        this.id = id;
        this.img = img;
        this.x = x;
        this.y = y;
        width = img.getWidth() * Settings.scale;
        height = img.getHeight() * Settings.scale;
        hb = new Hitbox(x, y, width, height);

        // Determine if the asset is large based on the original texture dimensions
        isLarge = (img.getWidth() * img.getHeight()) > 25000;
    }

    public void update() {
        hb.update();

        if (!AbstractDungeon.isScreenUp && !CafeRoom.isInteracting && !AbstractCutscene.isInCutscene && hb.hovered) {
            if (InputHelper.justClickedLeft) {
                onClick();
            }
        }

        if (shakeEnabled && shakeDuration > 0) {
            shakeDuration -= Gdx.graphics.getDeltaTime();

            // Generate small random offsets within the shake range
            shakeOffsetX = (float) ((Math.random() * 10 - 5) * Settings.scale);
            shakeOffsetY = (float) ((Math.random() * 10 - 5) * Settings.scale);

            if (shakeDuration <= 0) {
                // Reset offsets when shaking ends
                shakeOffsetX = 0f;
                shakeOffsetY = 0f;
            }
        }
    }

    public void render(SpriteBatch sb) {
        float renderX = x + shakeOffsetX;
        float renderY = y + shakeOffsetY;

        sb.draw(img, renderX, renderY, width, height);
        hb.render(sb);
    }

    public void move(float x, float y) {
        this.x = x;
        this.y = y;
        hb.move(x + width / 2f, y + height / 2f);
    }

    public void onClick() {
        if (shakeDuration < 0.1f && clickSound != null && !clickSound.isEmpty()) {
            CardCrawlGame.sound.play(clickSound);
        }

        if (shakeEnabled) {
            shakeDuration = 0.2f;
        }
    }

    public boolean isXFixed() {
        return fixedX > 0;
    }

    public boolean isYFixed() {
        return fixedY > 0;
    }
}
