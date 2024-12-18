package spireCafe.interactables.attractions.discord_statue.modifiers;

import com.megacrit.cardcrawl.cards.AbstractCard;

import basemod.abstracts.AbstractCardModifier;

//logic for changing rendered size is in a patch (RandomSizeModifierPatch)
public class RandomSizeModifier extends AbstractCardModifier {
    public float size;
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
    public AbstractCardModifier makeCopy() {
        return new RandomSizeModifier(size);
    }

    @Override
    public String identifier(AbstractCard card) {
        return "anniv7:RandomSizeModifier";
    }

}
