package spireCafe.abstracts;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.core.Settings;

public abstract class AbstractNPC extends AbstractCafeInteractable {
    public TextureRegion cutscenePortrait;
    public String name;
    public boolean hasInteracted = false;

    public AbstractNPC(float animationX, float animationY, float hb_w, float hb_h) {
        super(animationX, animationY, hb_w, hb_h);
        name = "test"; //TODO: Add String Loader
    }

    public abstract void renderCutscenePortrait(SpriteBatch sb);

    public void simpleRenderCutscenePortrait(SpriteBatch sb, float xOffset, float yOffset, float originX, float originY, float rotation) {
        sb.draw(cutscenePortrait, (xOffset - (cutscenePortrait.getRegionWidth() / 2.0F)) * Settings.scale, yOffset * Settings.scale, originX, originY, cutscenePortrait.getRegionWidth(), cutscenePortrait.getRegionHeight(), Settings.scale, Settings.scale, rotation);
    }
}
