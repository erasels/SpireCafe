package spireCafe.abstracts;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import spireCafe.vfx.SpeechBubble;

public abstract class AbstractNPC extends AbstractCafeInteractable {
    public TextureRegion cutscenePortrait;
    public boolean alreadyPerformedTransaction = false;
    public int blockingDialogueIndex = 0;
    public SpeechBubble speechBubble;
    public float speechBubbleScaleTimer = 0.3f;
    public boolean shouldShowSpeechBubble = true;

    public AbstractNPC(float animationX, float animationY, float hb_w, float hb_h) {
        super(animationX, animationY, hb_w, hb_h);
        speechBubble = new SpeechBubble(this.hitbox.cX, this.hitbox.y + this.hitbox.height, speechBubbleScaleTimer);
    }

    public void setCutscenePortrait(String texture) {

    }

    public abstract void renderCutscenePortrait(SpriteBatch sb);

    public void simpleRenderCutscenePortrait(SpriteBatch sb, float xOffset, float yOffset, float originX, float originY, float rotation) {
        sb.draw(cutscenePortrait, (xOffset - (cutscenePortrait.getRegionWidth() / 2.0F)) * Settings.scale, yOffset * Settings.scale, originX, originY, cutscenePortrait.getRegionWidth(), cutscenePortrait.getRegionHeight(), Settings.scale, Settings.scale, rotation);
    }

    @Override
    public void update() {
        super.update();
        if(shouldShowSpeechBubble) {
            this.speechBubble.update();
            if (this.hitbox.hovered && this.clickable && !AbstractDungeon.isScreenUp && !AbstractCutscene.isInCutscene) {
                speechBubble.isGrowing = true;
            } else {
                speechBubble.isGrowing = false;
            }
        }
    }

    @Override
    public void renderAnimation(SpriteBatch sb) {
        super.renderAnimation(sb);
        if(shouldShowSpeechBubble) {
            this.speechBubble.render(sb);
        }
    }
}
