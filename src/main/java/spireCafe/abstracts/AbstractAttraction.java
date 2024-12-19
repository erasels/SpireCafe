package spireCafe.abstracts;

public abstract class AbstractAttraction extends AbstractNPC {
    public AbstractAttraction(float animationX, float animationY, float hb_w, float hb_h) {
        super(animationX, animationY, hb_w, hb_h);
        shouldShowSpeechBubble = false;
    }
}
