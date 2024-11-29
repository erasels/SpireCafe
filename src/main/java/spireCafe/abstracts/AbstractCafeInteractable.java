package spireCafe.abstracts;

import basemod.animations.AbstractAnimation;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.input.InputHelper;

public abstract class AbstractCafeInteractable {
    public AbstractAnimation animation;
    public Texture img;
    protected Hitbox hitbox;

    public float animationX;
    public float animationY;
    public boolean clickable = true;

    public AbstractCafeInteractable(float animationX, float animationY, float hb_w, float hb_h, float hb_x_offset, float hb_y_offset) {
        this.animationX = animationX;
        this.animationY = animationY;
        this.hitbox = new Hitbox(this.animationX + (hb_x_offset * Settings.scale), this.animationY + (hb_y_offset * Settings.scale), hb_w * Settings.scale, hb_h * Settings.scale);
    }

    public AbstractCafeInteractable(float animationX, float animationY, float hb_w, float hb_h) {
        this(animationX, animationY, hb_w, hb_h, -hb_w / 2, 0.0f);
    }

    public abstract void onInteract();

    public boolean canSpawn(){
        return true;
    }

    public void update() {
        this.hitbox.update();
        if (this.hitbox.hovered && InputHelper.justClickedLeft && this.clickable && !AbstractDungeon.isScreenUp && !AbstractCutscene.isInCutscene) {
            this.onInteract();
        }
    }

    public void renderAnimation(SpriteBatch sb) {
        sb.setColor(Color.WHITE);
        if (animation != null) {
            animation.renderSprite(sb, animationX, animationY);
        } else if (this.img != null) {
            sb.draw(this.img, this.animationX - (float)this.img.getWidth() * Settings.scale / 2.0F, this.animationY, (float)this.img.getWidth() * Settings.scale, (float)this.img.getHeight() * Settings.scale, 0, 0, this.img.getWidth(), this.img.getHeight(), false, false);
        }
        this.hitbox.render(sb);
    }
}
