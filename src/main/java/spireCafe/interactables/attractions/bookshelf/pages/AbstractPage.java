package spireCafe.interactables.attractions.bookshelf.pages;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;
import spireCafe.abstracts.AbstractCafeInteractable;

import java.util.List;

public abstract class AbstractPage {
    public final String id;
    protected final UIStrings uiStrings;

    //id should match name of the
    public AbstractPage(String id) {
        this.id = id;
        uiStrings = CardCrawlGame.languagePack.getUIString(id);
    }

    public abstract String getText();

    // If you want to do something when this page is selected, use this
    public void onRead() {

    }

    // Return true if this page should be able to spawn currently
    public boolean canSpawn() {
        return true;
    }

    // Return true to make this much more likely to show up.
    // Makes most sense to conditionally increase texts about inhabitants that are currently in the cafe
    public boolean preferredSpawn(List<AbstractCafeInteractable> currentInhabitants) {
        return false;
    }

    // Use this in case you want to render an effect or image onto the page
    public void render(SpriteBatch sb) {

    }
}
