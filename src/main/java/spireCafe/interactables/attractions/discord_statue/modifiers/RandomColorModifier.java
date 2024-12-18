package spireCafe.interactables.attractions.discord_statue.modifiers;

import java.util.Random;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardColor;

import basemod.abstracts.AbstractCardModifier;

public class RandomColorModifier extends AbstractCardModifier {
    private CardColor color;

    public RandomColorModifier(CardColor color) {
        this.color = color;
    }

    public RandomColorModifier() {
        if (this.color == null)
        this.color = getRandomColor();
    }

    private CardColor getRandomColor() {
        CardColor[] colors = CardColor.values();
        int randomIndex = new Random().nextInt(colors.length);
        return colors[randomIndex];
    }

    @Override
    public void onInitialApplication(AbstractCard card) {
        card.color = color;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new RandomColorModifier(color);
    }

}
