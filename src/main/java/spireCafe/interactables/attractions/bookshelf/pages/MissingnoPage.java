package spireCafe.interactables.attractions.bookshelf.pages;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import spireCafe.Anniv7Mod;
import spireCafe.interactables.patrons.missingno.MarkovChain;
import spireCafe.interactables.patrons.missingno.MissingnoPatron;
import spireCafe.util.TexLoader;

import static spireCafe.Anniv7Mod.modID;
import static spireCafe.interactables.patrons.missingno.MarkovChain.MarkovType.PAGE;

public class MissingnoPage extends AbstractPage {
    private static final Texture IMG = TexLoader.getTexture(Anniv7Mod.makeAttractionPath("bookshelf/pages/foodStain.png"));
    private static final String ID = Anniv7Mod.makeID(MissingnoPage.class.getSimpleName());

    private static final String PATH_TO_FONT = modID + "Resources/fonts/missingno/Gridlockd.ttf";

    public MissingnoPage() {
        super(ID, PATH_TO_FONT);
    }

    @Override
    public String getOption() {
        return MarkovChain.getInstance(PAGE).generateText(5, 10);
    }

    @Override
    public String getText() {
        return MarkovChain.getInstance(PAGE).generateText(100, 400);
    }

    @Override
    public boolean canSpawn() {
        return Anniv7Mod.getSeenInteractables().contains(MissingnoPatron.ID);
    }


    @Override
    public void render(SpriteBatch sb) {
        sb.draw(IMG, Settings.WIDTH * 0.66f, 100f * Settings.yScale, IMG.getWidth()*Settings.scale, IMG.getHeight()* Settings.scale);
    }
}
