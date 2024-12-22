package spireCafe.util.decorationSystem;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.input.InputHelper;

public class Decoration {
    public Texture img;
    public Hitbox hb;
    public float width, height;
    public float x, y;
    public String id;

    public Decoration(String id, Texture img, float x, float y) {
        this.id = id;
        this.img = img;
        this.x = x;
        this.y = y;
        width = img.getWidth() * Settings.scale;
        height = img.getHeight() * Settings.scale;
        hb = new Hitbox(x, y, width, height);
    }

    public void update() {
        hb.update();
        if (hb.hovered) {
            if (InputHelper.justClickedLeft) {
                onClick();
            }
        }
    }

    public void render(SpriteBatch sb) {
        sb.draw(img, x, y, width, height);
        hb.render(sb);
    }

    public void move(float x, float y) {
        this.x = x;
        this.y = y;
        hb.move(x + width/2f, y + height /2f);
    }

    // Override this to make the decoration have some kind of click effect, like the things in HS
    public void onClick() {}
}
