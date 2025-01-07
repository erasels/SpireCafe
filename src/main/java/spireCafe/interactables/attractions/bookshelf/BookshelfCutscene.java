package spireCafe.interactables.attractions.bookshelf;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import spireCafe.Anniv7Mod;
import spireCafe.abstracts.AbstractCutscene;
import spireCafe.interactables.attractions.bookshelf.pages.AbstractPage;
import spireCafe.util.TexLoader;
import spireCafe.util.cutsceneStrings.CutsceneStrings;
import spireCafe.util.cutsceneStrings.LocalizedCutsceneStrings;

import static spireCafe.Anniv7Mod.makeID;
import static spireCafe.interactables.attractions.bookshelf.BookshelfAttraction.NUM_PAGES;

public class BookshelfCutscene extends AbstractCutscene {
    public static final String ID = makeID(BookshelfCutscene.class.getSimpleName());
    private static final CutsceneStrings cutsceneStrings = LocalizedCutsceneStrings.getCutsceneStrings(ID);
    private static final Texture PAGE_IMG = TexLoader.getTexture(Anniv7Mod.makeAttractionPath("bookshelf/page.png"));
    private static final Color TEXT_COL = new Color(6f/255, 4f/255, 29f/255, 1f);

    private final BookshelfAttraction bookshelf;

    public BookshelfCutscene(BookshelfAttraction character) {
        super(character, cutsceneStrings);
        bookshelf = character;
    }

    @Override
    protected void onClick() {
        if (dialogueIndex == 0) { //Shows first dialogue text and then options on click while staying on the same dialogue
            if(bookshelf.selectedPage == null) { // Only show selected page again when clicked
                for (int i = 0; i < Math.min(NUM_PAGES, bookshelf.selectedPages.size()); i++) {
                    AbstractPage option = ((BookshelfAttraction) character).selectedPages.get(i);
                    this.dialog.addDialogOption(option.getOption()).setOptionResult((o) -> {
                        bookshelf.setSelectedPage(option);
                        CardCrawlGame.sound.play("MAP_OPEN");
                        bookshelf.pageText = option.getText();
                        option.onRead();
                        nextDialogue();
                    });
                }

                //Don't read
                this.dialog.addDialogOption(OPTIONS[0]).setOptionResult((i) -> {
                    nextDialogue();
                });
            } else {
                // Just render page again
                CardCrawlGame.sound.play("MAP_OPEN");
                nextDialogue();
            }
        } else {
            if(bookshelf.selectedPage != null) {
                CardCrawlGame.sound.play("MAP_CLOSE");
            }
            endCutscene();
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        if(bookshelf.selectedPage != null) {
            sb.setColor(Settings.HALF_TRANSPARENT_BLACK_COLOR);
            sb.draw(ImageMaster.WHITE_SQUARE_IMG, 0, 0, Settings.WIDTH, Settings.HEIGHT);
            sb.setColor(Color.WHITE);
            sb.draw(PAGE_IMG, 0f,0f, Settings.WIDTH, Settings.HEIGHT);
            BitmapFont font;
            if(bookshelf.selectedPage.font != null) {
                font = bookshelf.selectedPage.font;
            } else {
                font = FontHelper.turnNumFont;
            }
            FontHelper.renderSmartText(sb, font, bookshelf.pageText, 435f * Settings.xScale, Settings.HEIGHT * 0.9f, 1050f * Settings.scale, 35f * Settings.yScale, TEXT_COL);
            bookshelf.selectedPage.render(sb);
        } else {
            super.render(sb);
        }
    }
}