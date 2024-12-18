package spireCafe.interactables.attractions.bookshelf;

import basemod.AutoAdd;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import spireCafe.Anniv7Mod;
import spireCafe.CafeRoom;
import spireCafe.abstracts.AbstractAttraction;
import spireCafe.abstracts.AbstractCafeInteractable;
import spireCafe.interactables.attractions.bookshelf.pages.AbstractPage;
import spireCafe.util.TexLoader;
import spireCafe.util.WeightedList;

import java.util.*;

import static spireCafe.Anniv7Mod.modID;

public class BookshelfAttraction extends AbstractAttraction {
    private static final int NUM_PAGES = 3;

    public static final String ID = BookshelfAttraction.class.getSimpleName();
    public static final List<AbstractPage> allPages = new ArrayList<>();

    public List<AbstractPage> selectedPages = new ArrayList<>(NUM_PAGES);

    public BookshelfAttraction(float animationX, float animationY) {
        super(animationX, animationY, 300, 400);
        img = TexLoader.getTexture(Anniv7Mod.makeAttractionPath("bookshelf/bookshelf.png"));
        authors = "Gk";
        name = "Bookshelf"; //TODO: Localize

        if(allPages.isEmpty()) {
            new AutoAdd(modID)
                    .packageFilter(AbstractPage.class)
                    .any(AbstractPage.class, (info, page) -> {
                        if(!info.ignore) {
                            allPages.add(page);
                        }
                    });
        }
    }

    @Override
    public void renderCutscenePortrait(SpriteBatch sb) {}

    @Override
    public void onInteract() {
        List<AbstractCafeInteractable> inhabitants = ((CafeRoom) AbstractDungeon.getCurrRoom().event).getCurrentInhabitants();
        //TODO: Exclude seen pages if there are enough unseen and mark as seen
        WeightedList<AbstractPage> pages = new WeightedList<>();

        for(AbstractPage p : allPages) {
            if(p.canSpawn()) {
                pages.add(p, p.preferredSpawn(inhabitants) ? 6 : 1);
            }
        }

        for (int i = 0; i < NUM_PAGES; i++) {
            selectedPages.add(pages.getRandom(AbstractDungeon.miscRng, true));
        }
    }
}
