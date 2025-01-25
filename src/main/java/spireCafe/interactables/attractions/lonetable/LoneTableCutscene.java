package spireCafe.interactables.attractions.lonetable;

import basemod.BaseMod;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import spireCafe.abstracts.AbstractCutscene;
import spireCafe.abstracts.AbstractNPC;
import spireCafe.screens.CafeMatchAndKeepScreen;
import spireCafe.util.cutsceneStrings.CutsceneStrings;
import spireCafe.util.cutsceneStrings.LocalizedCutsceneStrings;

import static spireCafe.Anniv7Mod.makeID;

public class LoneTableCutscene extends AbstractCutscene {
    public static final String ID = makeID(LoneTableCutscene.class.getSimpleName());
    private static final CutsceneStrings cutsceneStrings = LocalizedCutsceneStrings.getCutsceneStrings(ID);
    private AbstractCard.CardRarity selectedRarity;
    private boolean isZanyMode;

    public LoneTableCutscene(AbstractNPC character) {
        super(character, cutsceneStrings);
    }

    @Override
    protected void onClick() {
        switch (dialogueIndex) {
            case 0:
                character.name = LoneTableAttraction.getCharacterStrings().NAMES[1];
                handleInitialOptions();
                break;
            case 1:
                nextDialogue();
                break;
            case 2:
                handleGameModeSelection();
                break;
            case 3:
                handleNormalModeOptions();
                break;
            case 4:
                handleZanyModeOptions();
                break;
            case 5:
            case 10:
                endCutscene();
                break;
            default:
                nextDialogue();
                break;
        }
    }

    private void handleInitialOptions() {
        this.dialog.addDialogOption(OPTIONS[0]).setOptionResult((i) -> {
            character.setCutscenePortrait("Portrait");
            nextDialogue();
        });
        this.dialog.addDialogOption(OPTIONS[1]).setOptionResult((j) -> {
            endCutscene();
        });
    }

    private void handleGameModeSelection() {
        this.dialog.addDialogOption(OPTIONS[2]).setOptionResult((i) -> {
            isZanyMode = false;
            goToDialogue(3);
        });
        this.dialog.addDialogOption(OPTIONS[3]).setOptionResult((j) -> {
            isZanyMode = true;
            goToDialogue(4);
        });
        this.dialog.addDialogOption(OPTIONS[4]).setOptionResult((k) -> {
            endCutscene();
        });
    }

    private void handleNormalModeOptions() {
        this.dialog.addDialogOption(OPTIONS[5] + " (" + DESCRIPTIONS[8] + ")",
                        AbstractDungeon.player.gold < 120)
                .setOptionResult((i) -> {
                    startMatchAndKeepGame(AbstractCard.CardRarity.RARE, false, 120);
                });
        this.dialog.addDialogOption(OPTIONS[6] + " (" + DESCRIPTIONS[9] + ")",
                        AbstractDungeon.player.gold < 80)
                .setOptionResult((j) -> {
                    startMatchAndKeepGame(AbstractCard.CardRarity.UNCOMMON, false, 80);
                });
        this.dialog.addDialogOption(OPTIONS[7] + " (" + DESCRIPTIONS[10] + ")",
                        AbstractDungeon.player.gold < 40)
                .setOptionResult((k) -> {
                    startMatchAndKeepGame(AbstractCard.CardRarity.COMMON, false, 40);
                });
        this.dialog.addDialogOption(OPTIONS[8]).setOptionResult((l) -> {
            endCutscene();
        });
    }

    private void handleZanyModeOptions() {
        this.dialog.addDialogOption(OPTIONS[5] + " (" + DESCRIPTIONS[11] + ")",
                        AbstractDungeon.player.gold < 60)
                .setOptionResult((i) -> {
                    startMatchAndKeepGame(AbstractCard.CardRarity.RARE, true, 60);
                });
        this.dialog.addDialogOption(OPTIONS[6] + " (" + DESCRIPTIONS[12] + ")",
                        AbstractDungeon.player.gold < 40)
                .setOptionResult((j) -> {
                    startMatchAndKeepGame(AbstractCard.CardRarity.UNCOMMON, true, 40);
                });
        this.dialog.addDialogOption(OPTIONS[7] + " (" + DESCRIPTIONS[13] + ")",
                        AbstractDungeon.player.gold < 20)
                .setOptionResult((k) -> {
                    startMatchAndKeepGame(AbstractCard.CardRarity.COMMON, true, 20);
                });
        this.dialog.addDialogOption(OPTIONS[8]).setOptionResult((l) -> {
            endCutscene();
        });
    }

    private void startMatchAndKeepGame(AbstractCard.CardRarity rarity, boolean isZany, int goldCost) {
        try {
            if (this.dialog != null) {
                this.dialog.clear();
            }

            character.alreadyPerformedTransaction = true;

            AbstractDungeon.player.loseGold(goldCost);

            CafeMatchAndKeepScreen.GameParams params =
                    new CafeMatchAndKeepScreen.GameParams(rarity, isZany, goldCost);

            AbstractDungeon.previousScreen = AbstractDungeon.screen;
            AbstractDungeon.dynamicBanner.hide();

            BaseMod.openCustomScreen(CafeMatchAndKeepScreen.ScreenEnum.CAFE_MATCH_SCREEN, params);

            CafeMatchAndKeepScreen screen = (CafeMatchAndKeepScreen)BaseMod
                    .getCustomScreen(CafeMatchAndKeepScreen.ScreenEnum.CAFE_MATCH_SCREEN);

            if (screen != null) {
                AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.INCOMPLETE;
            }
        } catch (Exception e) {

        }

        if (this.dialog != null) {
            this.dialog.clear();
        }
        endCutscene();
    }

    @Override
    protected void endCutscene() {
        if (this.dialog != null) {
            this.dialog.clear();
        }
        character.name = LoneTableAttraction.getCharacterStrings().NAMES[0];
        character.setCutscenePortrait(null);
        isNPCSpeaking = false;
        super.endCutscene();
    }
}