package spireCafe.abstracts;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import spireCafe.vfx.SpeechBubble;

public abstract class AbstractNPC extends AbstractCafeInteractable {
    public TextureRegion cutscenePortrait;
    public String name;
    public SpeechBubble speechBubble;
    public float speechBubbleScaleTimer = 0.3f;

    public AbstractNPC(float animationX, float animationY, float hb_w, float hb_h) {
        super(animationX, animationY, hb_w, hb_h);
        speechBubble = new SpeechBubble(animationX, animationY + hb_h, speechBubbleScaleTimer, "test");
    }

    public void renderCutscenePortrait(SpriteBatch sb) {

    }

    public void setCutscenePortrait(String texture) {

    }

    @Override
    public void update() {
        super.update();
        this.speechBubble.update();
        if (this.hitbox.hovered && this.clickable && !AbstractDungeon.isScreenUp && !AbstractCutscene.isInCutscene) {
            speechBubble.isGrowing = true;
        } else {
            speechBubble.isGrowing = false;
        }
    }

    @Override
    public void renderAnimation(SpriteBatch sb) {
        super.renderAnimation(sb);
        this.speechBubble.render(sb);
    }
}
