package spireCafe.interactables.attractions.bookshelf.pages;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import spireCafe.Anniv7Mod;
import spireCafe.abstracts.AbstractCafeInteractable;
import spireCafe.interactables.merchants.snackmaster.SnackmasterMerchant;

import java.util.List;

public class RecipePage extends AbstractPage {
    private static final String ID = Anniv7Mod.makeID(RecipePage.class.getSimpleName());
    public RecipePage() {
        super(ID);
    }

    @Override
    public String getOption() {
        return uiStrings.TEXT[0];
    }

    @Override
    public String getText() {
        int recipeIndex = AbstractDungeon.miscRng.random(2);
        StringBuilder textMaker = new StringBuilder();
        textMaker.append(uiStrings.TEXT[0]); // Title
        textMaker.append(uiStrings.TEXT[1]); //New lines
        textMaker.append(uiStrings.TEXT[2 + recipeIndex]); // Recipe
        if(AbstractDungeon.miscRng.randomBoolean()) {
            textMaker.append(uiStrings.TEXT[1]);
            textMaker.append(uiStrings.TEXT[uiStrings.TEXT.length-1]);
        }
        return textMaker.toString();
    }

    @Override
    public boolean preferredSpawn(List<AbstractCafeInteractable> currentInhabitants) {
        return currentInhabitants.stream().anyMatch(p -> p instanceof SnackmasterMerchant);
    }
}
