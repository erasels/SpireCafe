package spireCafe.interactables.attractions.bookshelf;

import basemod.AutoAdd;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import spireCafe.Anniv7Mod;
import spireCafe.CafeRoom;
import spireCafe.abstracts.AbstractAttraction;
import spireCafe.abstracts.AbstractCafeInteractable;
import spireCafe.interactables.attractions.bookshelf.pages.AbstractPage;
import spireCafe.util.TexLoader;
import spireCafe.util.WeightedList;

import java.io.IOException;
import java.util.*;

import static spireCafe.Anniv7Mod.makeID;
import static spireCafe.Anniv7Mod.modID;

public class BookshelfAttraction extends AbstractAttraction {
    public static final String PAGE_CONFIG_KEY = "seenPages";
    protected static final int NUM_PAGES = 3;

    public static final String ID = BookshelfAttraction.class.getSimpleName();
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(makeID(ID));
    public static final List<AbstractPage> allPages = new ArrayList<>();

    public List<AbstractPage> selectedPages = new ArrayList<>(NUM_PAGES);
    protected AbstractPage selectedPage = null;
    protected String pageText;

    public BookshelfAttraction(float animationX, float animationY) {
        super(animationX, animationY, 350, 470);
        img = TexLoader.getTexture(Anniv7Mod.makeAttractionPath("bookshelf/bookshelf.png"));
        authors = "Gk";
        name = uiStrings.TEXT[0];

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
        if(selectedPages.isEmpty()) {
            List<AbstractCafeInteractable> inhabitants = ((CafeRoom) AbstractDungeon.getCurrRoom().event).getCurrentInhabitants();
            List<String> seenPages = getSeenPages();
            WeightedList<AbstractPage> pages = new WeightedList<>();
            List<AbstractPage> filteredPages = new ArrayList<>();

            for (AbstractPage p : allPages) {
                if (p.canSpawn()) {
                    if (!seenPages.contains(p.id)) {
                        pages.add(p, p.preferredSpawn(inhabitants) ? 30 : 5); // Might want to add some dynamic logic in case there are a lot of pages
                    } else {
                        filteredPages.add(p);
                    }
                }
            }
            if (pages.size() < NUM_PAGES) {
                for (AbstractPage fp : filteredPages) {
                    pages.add(fp, fp.preferredSpawn(inhabitants) ? 5 : 1); // Reduced so that the last few pages have a higher chance to be seen
                }
            }

            for (int i = 0; i < Math.min(NUM_PAGES, pages.size()); i++) {
                AbstractPage p = pages.getRandom(AbstractDungeon.miscRng, true);
                selectedPages.add(p);
            }
        }

        AbstractDungeon.topLevelEffects.add(new BookshelfCutscene(this));
    }

    public void setSelectedPage(AbstractPage selection) {
        selectedPage = selection;
        addSeenPage(selection.id);
    }

    public static List<String> getSeenPages() {
        List<String> seenPages = new ArrayList<>();
        if (Anniv7Mod.modConfig != null) {
            seenPages = Arrays.asList(Anniv7Mod.modConfig.getString(PAGE_CONFIG_KEY).split(","));
        }
        return seenPages;
    }

    public static boolean hasSeenPage(String pageId) {
        return Anniv7Mod.modConfig != null &&
                Anniv7Mod.modConfig.getString(PAGE_CONFIG_KEY).contains(pageId+",");
    }

    public static void addSeenPage(String pageId) {
        if (Anniv7Mod.modConfig != null) {
            if(!hasSeenPage(pageId)) {
                String pageList = Anniv7Mod.modConfig.getString(PAGE_CONFIG_KEY);
                Anniv7Mod.modConfig.setString(PAGE_CONFIG_KEY, pageList + pageId + ",");
                try {
                    Anniv7Mod.modConfig.save();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
