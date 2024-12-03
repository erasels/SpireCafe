package spireCafe.interactables.npcs.example;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import spireCafe.abstracts.AbstractCutscene;
import spireCafe.abstracts.AbstractNPC;
import spireCafe.util.cutsceneStrings.CutsceneStrings;
import spireCafe.util.cutsceneStrings.LocalizedCutsceneStrings;

import static spireCafe.Anniv7Mod.makeID;

public class ExamplePatronCutscene extends AbstractCutscene {
    public static final String ID = makeID(ExamplePatronCutscene.class.getSimpleName());
    private static final CutsceneStrings cutsceneStrings = LocalizedCutsceneStrings.getCutsceneStrings(ID);
    private boolean forRemove = false;
    private boolean forUpgrade = false;

    public ExamplePatronCutscene(AbstractNPC character) {
        super(character, cutsceneStrings);
    }

    //Instead of only having one line of dialogue after the transaction this Patron says a different line each time.
    @Override
    public String getBlockingDialogue() {
        int blockingDialogueIndex = character.blockingDialogueIndex;
        String blockingDialogue = cutsceneStrings.BLOCKING_TEXTS[blockingDialogueIndex];
        if(blockingDialogueIndex<cutsceneStrings.BLOCKING_TEXTS.length-1){
            character.blockingDialogueIndex++;
        }
        return blockingDialogue;
    }

    @Override
    protected void onClick() {
        // In the eventstrings, DESCRIPTIONS is an array of all the cutscene text.
        // dialogueIndex is the index of cutscene text the cutscene is currently displaying.
        // The default behavior when the player clicks is to simply display the text in dialogue index + 1.
        // This means that if you properly structure your cutscene strings into sections of
        // subsequent dialogue, you can take advantage of the default behavior to handle most
        // of the transitioning, and simply focus on writing code for handling the points where you
        // want to add player dialog options or exit the cutscene.

        // If you want player dialog options to appear during a specific dialog, you need to "add" the dialog options
        // during the previous dialogue index so that they will show up in the next dialog.
        if (dialogueIndex == 0) {
            // In this example, adding the player dialog option during index 0 will cause them to appear during index 1.
            nextDialogue(); // nextDialogue will transition us from dialogue index 0 to index 1.
            // Adds a player dialog option for upgrading a card. You can specify what happens when the button is clicked with setOptionResult.
            this.dialog.addDialogOption(OPTIONS[0] + FontHelper.colorString(OPTIONS[1], "g")).setOptionResult((i)->{
                nextDialogue();
                // If the player selects an option that provides some benefit (like upgrading a card, removing a card, etc.)
                // then this alreadyPerformedTransaction flag should be set to true. This will prevent the player from
                // interacting further with the NPC so they can't repeatedly perform the same transaction.
                character.alreadyPerformedTransaction = true;
                forUpgrade = true;
                AbstractDungeon.gridSelectScreen.open(AbstractDungeon.player.masterDeck.getUpgradableCards(), 1, OPTIONS[1], true, false, false, false);
            });
            // Adds a player dialog option for removing a card.
            this.dialog.addDialogOption(OPTIONS[2] + FontHelper.colorString(OPTIONS[3], "g")).setOptionResult((i)->{
                // goToDialogue(4) will jump to dialogue index 4 instead of advancing to index + 1.
                // This allows you to "jump" to different sections of cutscene text depending on player input.
                goToDialogue(4);
                character.alreadyPerformedTransaction = true;
                forRemove = true;
                AbstractDungeon.gridSelectScreen.open(AbstractDungeon.player.masterDeck.getPurgeableCards(), 1, OPTIONS[3], false, false, false, true);
            });
            // Adds a player dialog option for declining to do anything.
            this.dialog.addDialogOption(OPTIONS[4]).setOptionResult((i)->{
                goToDialogue(6);
            });
        } else if (dialogueIndex == 3 || dialogueIndex == 5 || dialogueIndex == 6) {
            // Exit the cutscene at any of these dialogue indices.
            endCutscene();
        } else {
            // Default behavior is to simply display the text in the next dialogue index.
            nextDialogue();
        }
    }

    public void update() {
        super.update();
        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty() && forRemove) {
            for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards) {
                AbstractDungeon.effectList.add(new PurgeCardEffect(c));
                AbstractDungeon.player.masterDeck.removeCard(c);
            }
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            backToCutscene();
        }

        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty() && forUpgrade) {
            for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards) {
                c.upgrade();
                AbstractDungeon.player.bottledCardUpgradeCheck(c);
                AbstractDungeon.effectsQueue.add(new ShowCardBrieflyEffect(c.makeStatEquivalentCopy()));
                AbstractDungeon.effectsQueue.add(new UpgradeShineEffect((float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
            }
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            backToCutscene();
        }
    }

}