package spireCafe.abstracts;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.helpers.FontHelper;

public abstract class AbstractBartender extends AbstractNPC {
    // Flags to track if options have been used
    public boolean healUsed = false;
    public boolean secondUsed = false;

    public AbstractBartender(float animationX, float animationY, float hb_w, float hb_h) {
        super(animationX, animationY, hb_w, hb_h);
        shouldShowSpeechBubble = false;
    }

    @Override
    public void renderAnimation(SpriteBatch sb) {
        super.renderAnimation(sb);
        FontHelper.renderFontCentered(sb, FontHelper.buttonLabelFont, "Test", hitbox.cX, animationY + hitbox.height, Color.WHITE);
    }

    /**
     * Define the text of the heal option. Should be formatted similarly to event options.
     * e.g. Heal [getHealAmount()] HP. Lose 100 Gold.
     */
    protected abstract String getHealOptionDescription();

    /**
     * Define how much the heal will restore.
     * Contributors can return a fixed amount or a computed value (like heal a percentage of missing HP).
     */
    protected abstract int getHealAmount();

    /**
     * Actually apply the heal and the cost here.
     * For example:
     * - Remove gold equal to cost.
     * - Heal the player by getHealAmount().
     * This method is called when the player confirms the heal option.
     */
    protected abstract void applyHealAction();

    /**
     * Optionally define a second gameplay affecting option.
     * If not needed, just return null or an empty string.
     */
    protected String getSecondOptionDescription() {
        return null;
    }

    /**
     * Apply the second option's effect here if defined.
     */
    protected void applySecondOptionAction() {
        // Default does nothing
    }

    @Override
    public void renderCutscenePortrait(SpriteBatch sb) {
        // By default, just position similarly to patrons.
        simpleRenderCutscenePortrait(sb, 1560.0F, 0.0F, 0.0F, 0.0F, 0.0F);
    }

    /**
     * Code that gets called when the bartender is clicked. By default, this will start the cutscene.
     * Override this and add your own Cutscene here if you made one, or use the different overload for BartenderCutscene
     */
    public void onInteract() {
        AbstractDungeon.topLevelEffectsQueue.add(new BartenderCutscene(this));
    }
}

