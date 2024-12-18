package spireCafe.interactables.attractions.bookshelf;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import spireCafe.abstracts.AbstractCutscene;
import spireCafe.abstracts.AbstractNPC;
import spireCafe.interactables.attractions.bookshelf.pages.AbstractPage;
import spireCafe.interactables.attractions.makeup.MakeupCutscene;
import spireCafe.interactables.attractions.makeup.MakeupTableAttraction;
import spireCafe.util.cutsceneStrings.CutsceneStrings;
import spireCafe.util.cutsceneStrings.LocalizedCutsceneStrings;

import static spireCafe.Anniv7Mod.makeID;

public class BookshelfCutscene extends AbstractCutscene {
    public static final String ID = makeID(BookshelfCutscene.class.getSimpleName());
    private static final CutsceneStrings cutsceneStrings = LocalizedCutsceneStrings.getCutsceneStrings(ID);

    private AbstractPage selectedPage = null;
    private String pageText;

    public BookshelfCutscene(BookshelfAttraction character) {
        super(character, cutsceneStrings);
    }

    @Override
    protected void onClick() {
        if (dialogueIndex == 0) { //Shows first dialogue text and then options on click while staying on the same dialogue
            for (int i = 0; i < BookshelfAttraction.NUM_PAGES; i++) {
                AbstractPage option = ((BookshelfAttraction)character).selectedPages.get(i);
                this.dialog.addDialogOption(option.getOption()).setOptionResult((o)->{
                    selectedPage = option;
                    CardCrawlGame.sound.play("MAP_OPEN");
                    pageText = option.getText();
                    option.onRead();
                });
            }

            //Don't read
            this.dialog.addDialogOption(OPTIONS[0]).setOptionResult((i)->{
                nextDialogue();
            });
        } else {
            if(selectedPage != null) {
                CardCrawlGame.sound.play("MAP_CLOSE");
            }
            endCutscene();
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        if(selectedPage != null) {
            //TODO: Render page, then text
            selectedPage.render(sb);
        } else {
            super.render(sb);
        }
    }
}