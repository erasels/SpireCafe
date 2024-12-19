package spireCafe.interactables.attractions.bookshelf.pages;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import spireCafe.Anniv7Mod;
import spireCafe.abstracts.AbstractCafeInteractable;
import spireCafe.interactables.merchants.snackmaster.SnackmasterMerchant;
import spireCafe.util.TexLoader;

import java.util.List;

public class RecipePage extends AbstractPage {
    private static final Texture IMG = TexLoader.getTexture(Anniv7Mod.makeAttractionPath("bookshelf/pages/foodStain.png"));
    private static final String ID = Anniv7Mod.makeID(RecipePage.class.getSimpleName());

    public RecipePage() {
        super(ID);
    }

    @Override
    public String getOption() {
        return uiStrings.TEXT[0].substring(0, uiStrings.TEXT[0].lastIndexOf(" "));
    }

    @Override
    public String getText() {
        int recipeIndex = AbstractDungeon.miscRng.random(2);
        StringBuilder textMaker = new StringBuilder();
        textMaker.append(uiStrings.TEXT[0]).append(recipeIndex); // Title
        textMaker.append(uiStrings.TEXT[1]); //New lines
        textMaker.append(uiStrings.TEXT[2 + recipeIndex]); // Recipe
        /*textMaker.append(uiStrings.TEXT[1]);
        textMaker.append(uiStrings.TEXT[uiStrings.TEXT.length-1]);*/
        return textMaker.toString();
    }

    @Override
    public boolean preferredSpawn(List<AbstractCafeInteractable> currentInhabitants) {
        return currentInhabitants.stream().anyMatch(p -> p instanceof SnackmasterMerchant);
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.draw(IMG, Settings.WIDTH * 0.66f, 100f * Settings.yScale, IMG.getWidth()*Settings.scale, IMG.getHeight()* Settings.scale);
    }
}
