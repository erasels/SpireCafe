package spireCafe.interactables.patrons.spiomesmanifestation;


import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import spireCafe.abstracts.AbstractCutscene;
import spireCafe.abstracts.AbstractNPC;
import spireCafe.util.cutsceneStrings.CutsceneStrings;
import spireCafe.util.cutsceneStrings.LocalizedCutsceneStrings;

import static spireCafe.Anniv7Mod.makeID;

public class SpiomesManifestationCutscene extends AbstractCutscene {
    public static final String ID = makeID(SpiomesManifestationCutscene.class.getSimpleName());
    private static final CutsceneStrings cutsceneStrings = LocalizedCutsceneStrings.getCutsceneStrings(ID);
    private final SpiomesManifestationPatron patron;

    private static final int MAX_HP_COST = 5;

    public SpiomesManifestationCutscene(AbstractNPC character) {
        super(character, cutsceneStrings);
        patron = (SpiomesManifestationPatron) character;
    }

    @Override
    protected void onClick() {
        switch (this.dialogueIndex) {
            case 0:
                if (patron.hasChosenBiome) {
                   goToDialogue(7);
                } else {
                  nextDialogue();
                }
                break;
            case 3:
                nextDialogue();
                createSpiomesChoiceOptions();
                break;
            case 5:
            case 6:
                goToDialogue(7);
                break;
            case 7:
                nextDialogue();
                createRelicChoiceOptions();
                break;
            case 9:
            case 10:
                endCutscene();
                break;
            default:
                nextDialogue();
        }
    }

    public void createSpiomesChoiceOptions() {
        this.dialog.clear();
        for (Object biome : patron.availableBiomes) {
            String opt = OPTIONS[0].replace("{0}", patron.getBiomeName(biome));
            this.dialog.addDialogOption(opt).setOptionResult(i -> {
                patron.addBiomeToNextMap(biome);
                patron.hasChosenBiome=true;
                goToDialogue(5);
            });
        }
        this.dialog.addDialogOption(OPTIONS[1]).setOptionResult(i -> {
            goToDialogue(6);
        });
    }

    public void createRelicChoiceOptions() {
        this.dialog.clear();
        SpiomesManifestationPatron patron = (SpiomesManifestationPatron) character;
        String opt = OPTIONS[2].replace("{0}", String.valueOf(MAX_HP_COST));
        this.dialog.addDialogOption(opt, new BiomesExplorationMap()).setOptionResult(i -> {
            AbstractDungeon.player.decreaseMaxHealth(MAX_HP_COST);
            AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2, Settings.HEIGHT / 2, new BiomesExplorationMap());
            goToDialogue(9);
            character.alreadyPerformedTransaction = true;
        });
        this.dialog.addDialogOption(OPTIONS[3]).setOptionResult(i -> {
            goToDialogue(10);
        });
    }
}