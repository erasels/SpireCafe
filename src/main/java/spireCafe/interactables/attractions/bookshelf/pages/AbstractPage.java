package spireCafe.interactables.attractions.bookshelf.pages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import spireCafe.abstracts.AbstractCafeInteractable;

import java.util.List;

import static spireCafe.Anniv7Mod.modID;

public abstract class AbstractPage {
    public final String id;
    protected final UIStrings uiStrings;
    public final BitmapFont font;

    //id should match name of the
    public AbstractPage(String id) {
        this(id, null);
    }

    public AbstractPage(String id, String pathToFont) {
        this.id = id;
        uiStrings = CardCrawlGame.languagePack.getUIString(id);
        font = getFont(pathToFont);
    }

    //Use this to decide how the option to read this page should be worded
    public abstract String getOption();

    // Use this to decide the text that is shown on your page
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

    private BitmapFont getFont(String pathToFont) {
        if(pathToFont == null || pathToFont.isEmpty()) {
            return null;
        }
        final BitmapFont font;
        FileHandle FONT_FILE = Gdx.files.internal(pathToFont);
        FreeTypeFontGenerator g = new FreeTypeFontGenerator(FONT_FILE);
        FreeTypeFontGenerator.FreeTypeFontParameter p = new FreeTypeFontGenerator.FreeTypeFontParameter();
        p.characters = "";
        p.incremental = true;
        p.size = Math.round(50.0f * Settings.scale);
        p.gamma = 1.2F;
        p.minFilter = Texture.TextureFilter.Linear;
        p.magFilter = Texture.TextureFilter.Linear;
        g.scaleForPixelHeight(p.size);
        font = g.generateFont(p);
        font.setUseIntegerPositions(false);
        (font.getData()).markupEnabled = true;
        if (LocalizedStrings.break_chars != null)
            (font.getData()).breakChars = LocalizedStrings.break_chars.toCharArray();
        (font.getData()).fontFile = FONT_FILE;
        System.out.println(font);
        return font;
    }
}
