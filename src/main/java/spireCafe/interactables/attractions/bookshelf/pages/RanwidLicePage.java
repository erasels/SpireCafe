package spireCafe.interactables.attractions.bookshelf.pages;

import spireCafe.Anniv7Mod;
import spireCafe.abstracts.AbstractCafeInteractable;
import spireCafe.interactables.patrons.redlouse.RedLousePatron;

import java.util.List;

public class RanwidLicePage extends AbstractPage {
    private static final String ID = Anniv7Mod.makeID(RanwidLicePage.class.getSimpleName());

    public RanwidLicePage() {
        super(ID);
    }

    @Override
    public String getOption() {
        return uiStrings.TEXT[0];
    }

    @Override
    public String getText() {
        StringBuilder textMaker = new StringBuilder();
        textMaker.append(uiStrings.TEXT[0]); // Title
        textMaker.append(uiStrings.TEXT[1]); // New lines
        textMaker.append(uiStrings.TEXT[2]); // Entry
        return textMaker.toString();
    }

    @Override
    public boolean preferredSpawn(List<AbstractCafeInteractable> currentInhabitants) {
        return currentInhabitants.stream().anyMatch(p -> p instanceof RedLousePatron);
    }
}
