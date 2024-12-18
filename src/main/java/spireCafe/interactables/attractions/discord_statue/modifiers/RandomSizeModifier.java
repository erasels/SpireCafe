package spireCafe.interactables.attractions.discord_statue.modifiers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;

import basemod.abstracts.AbstractCardModifier;

public class RandomSizeModifier extends AbstractCardModifier {
    private float size;
    private float prevDrawScale;
    private boolean drawScaleChanged;
    private static final float MAX_SIZE = 1.3f;
    private static final float MIN_SIZE = 0.5f;

    public RandomSizeModifier(float size) {
        this.size = size;
    }

    public RandomSizeModifier() {
        if (this.size == 0)
            this.size = MIN_SIZE + (MAX_SIZE - MIN_SIZE) * (float) Math.random();
    }

    @Override
    public void onRender(AbstractCard card, SpriteBatch sb) {
        drawScaleChanged = card.targetDrawScale != prevDrawScale;
        if (drawScaleChanged)
            card.targetDrawScale *= size;

        prevDrawScale = card.targetDrawScale;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new RandomSizeModifier(size);
    }

}
