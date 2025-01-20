package spireCafe.interactables.attractions.bookshelf.pages;

import spireCafe.Anniv7Mod;

public class RanwidTheMawPage extends AbstractPage {
    private static final String ID = Anniv7Mod.makeID(RanwidTheMawPage.class.getSimpleName());

    public RanwidTheMawPage() {
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
}
