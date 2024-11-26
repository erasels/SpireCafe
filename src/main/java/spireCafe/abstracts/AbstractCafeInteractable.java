package spireCafe.abstracts;

import basemod.animations.AbstractAnimation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.input.InputHelper;

public abstract class AbstractCafeInteractable {
    public AbstractAnimation animation;
    protected Hitbox hitbox;

    public float animationX;
    public float animationY;
    public boolean clickable = true;

    public AbstractCafeInteractable(float animationX, float animationY, float hb_w, float hb_h, float hb_x_offset, float hb_y_offset) {
        this.animationX = animationX;
        this.animationY = animationY;
        this.hitbox = new Hitbox(this.animationX + (hb_x_offset * Settings.scale), this.animationY + (hb_y_offset * Settings.scale), hb_w, hb_h);
    }

    public AbstractCafeInteractable(float animationX, float animationY, float hb_w, float hb_h) {
        this(animationX, animationY, hb_w, hb_h, -hb_w / 2, 0.0f);
    }

    public abstract void onInteract();

    public void update() {
        this.hitbox.update();
        if (this.hitbox.hovered && InputHelper.justClickedLeft && this.clickable && !AbstractDungeon.isScreenUp && !AbstractCutscene.isInCutscene) {
            this.onInteract();
        }
    }

    public void renderAnimation(SpriteBatch sb) {
        if (animation != null) {
            animation.renderSprite(sb, animationX, animationY);
        }
        this.hitbox.render(sb);
    }
}
