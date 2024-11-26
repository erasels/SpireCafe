package spireCafe.abstracts;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class AbstractNPC extends AbstractCafeInteractable {
    public TextureRegion cutscenePortrait;
    public String name;

    public AbstractNPC(float animationX, float animationY, float hb_w, float hb_h) {
        super(animationX, animationY, hb_w, hb_h);
    }

    public void renderCutscenePortrait(SpriteBatch sb) {

    }
}
