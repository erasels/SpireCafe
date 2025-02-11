package spireCafe.interactables.patrons.trashking;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.potions.PotionSlot;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import spireCafe.abstracts.AbstractCutscene;
import spireCafe.abstracts.AbstractNPC;
import spireCafe.interactables.patrons.trashking.relics.*;
import spireCafe.util.cutsceneStrings.CutsceneStrings;
import spireCafe.util.cutsceneStrings.LocalizedCutsceneStrings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import static spireCafe.Anniv7Mod.makeID;

public class TrashKingCutscene extends AbstractCutscene {
    public static final String ID = makeID(TrashKingCutscene.class.getSimpleName());
    private static final CutsceneStrings cutsceneStrings = LocalizedCutsceneStrings.getCutsceneStrings(ID);

    public static final String[] TRASH_KING_RELICS = {
            DesignersBrush.ID, SlimeUvula.ID, Life.ID, Pebbles.ID, ArtOfPeace.ID,
            PetRock.ID, PaperNail.ID, SuperheroComic.ID, RingOfTheNoodle.ID,
            Supplements.ID, HospitalBill.ID, WellDoneSteak.ID, LonelyAnt.ID, NapiersBones.ID,
            LostPenny.ID, Popcorn.ID, InsuranceCard.ID, TyrtleShell.ID, RayOfMoonlight.ID,
            StylePoints.ID, Loan.ID, PaintBucket.ID, ElyphantToothpaste.ID, HeirloomFork.ID,
            FistOfThePugilist.ID, RedButton.ID, Neurotoxin.ID
    };

    public TrashKingCutscene(AbstractNPC character) {
        super(character, cutsceneStrings);
    }

    @Override
    protected void onClick() {
        switch (dialogueIndex) {
            case 0:
                handleInitialOptions();
                break;
            case 1:
                handleOfferOptions();
                break;
            case 2:
                handleSecondaryOfferOptions();
                break;
            case 8:
                endCutscene();
                break;
            default:
                endCutscene();
                break;
        }
    }

    private void handleInitialOptions() {
        this.dialog.addDialogOption(OPTIONS[0]).setOptionResult((i) -> {
            CardCrawlGame.sound.play("POWER_ENTANGLED", 0.05F);
            character.setCutscenePortrait("Portrait3");
            nextDialogue();
        });
        this.dialog.addDialogOption(OPTIONS[1]).setOptionResult((i) -> {
            goToDialogue(4);
            endCutscene();
        });
    }

    private void handleOfferOptions() {
        character.setCutscenePortrait("Portrait4");
        this.dialog.addDialogOption(String.format(OPTIONS[2], 5),
                        AbstractDungeon.player.gold < 5)
                .setOptionResult((i) -> {
                    handleRelicPurchase(5, 1);
                });
        this.dialog.addDialogOption(OPTIONS[3]).setOptionResult((i) -> {
            character.setCutscenePortrait("Portrait5");
            this.dialogueIndex = 1;
            goToDialogue(5);
            this.dialogueIndex = 2;
        });
        this.dialog.addDialogOption(OPTIONS[6]).setOptionResult((i) -> {
            endCutscene();
        });
    }

    private void handleSecondaryOfferOptions() {
        this.dialog.addDialogOption(String.format(OPTIONS[4], 50),
                        AbstractDungeon.player.gold < 50)
                .setOptionResult((i) -> {
                    handleRelicPurchase(50, 2);
                });
        this.dialog.addDialogOption(OPTIONS[5]).setOptionResult((i) -> {
            goToDialogue(3);
            endCutscene();
        });
    }

    private void handleRelicPurchase(int cost, int relicCount) {
        AbstractDungeon.player.loseGold(cost);
        character.alreadyPerformedTransaction = true;

        ArrayList<AbstractRelic> givenRelics = new ArrayList<>();

        ArrayList<String> availableRelics = new ArrayList<>(Arrays.asList(TRASH_KING_RELICS));
        availableRelics.removeIf(relicId ->
                AbstractDungeon.player.hasRelic(relicId) ||
                        (relicId.equals(ElyphantToothpaste.ID) && !hasAnyPotions())
        );
        Collections.shuffle(availableRelics, new Random(AbstractDungeon.miscRng.randomLong()));

        for (int i = 0; i < relicCount && i < availableRelics.size(); i++) {
            AbstractRelic relic = RelicLibrary.getRelic(availableRelics.get(i)).makeCopy();
            AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2, Settings.HEIGHT / 2, relic);
            givenRelics.add(relic);
        }

        if (relicCount == 2) {
            for (AbstractRelic relic : givenRelics) {
                if (relic.relicId.equals(ElyphantToothpaste.ID) || relic.relicId.equals(Loan.ID)) {
                    relic.onEquip(); // onEquip wasn't working in the two-relic purchase scenario, for some reason. Have to call it manually
                }
            }
        }

        if (relicCount == 1) {
            goToDialogue(2);
            this.dialogueIndex = 8;
        } else {
            goToDialogue(7);
            this.dialogueIndex = 8;
        }
    }

    private boolean hasAnyPotions() {
        return AbstractDungeon.player.potions.stream()
                .anyMatch(potion -> !(potion instanceof PotionSlot));
    }

    @Override
    protected void endCutscene() {
        if (this.dialog != null) {
            this.dialog.clear();
        }
        character.setCutscenePortrait("Portrait2");
        super.endCutscene();
    }
}