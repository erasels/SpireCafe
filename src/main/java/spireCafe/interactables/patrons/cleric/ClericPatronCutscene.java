package spireCafe.interactables.patrons.cleric;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import spireCafe.Anniv7Mod;
import spireCafe.abstracts.AbstractCutscene;
import spireCafe.util.Wiz;
import spireCafe.util.cutsceneStrings.CutsceneStrings;
import spireCafe.util.cutsceneStrings.LocalizedCutsceneStrings;

public class ClericPatronCutscene extends AbstractCutscene{

    public static final String ID = Anniv7Mod.makeID(ClericPatronCutscene.class.getSimpleName());
    private static final CutsceneStrings cutsceneStrings = LocalizedCutsceneStrings.getCutsceneStrings(ID);
    private AbstractRelic lostRelic;
    private int goldCost = 0;
    private int maxHPCost = 0;

    public ClericPatronCutscene(ClericPatron character) {
        super(character, cutsceneStrings);
        this.lostRelic = getLostItem();
        this.goldCost = (int) (Wiz.p().gold * 0.5F);
        this.maxHPCost = (int) (Wiz.p().maxHealth * 0.15F);
    }

    
    @Override
    protected void onClick() {
        switch (this.dialogueIndex) {
            case 1:
                nextDialogue();
                createOptionDialogue();
                break;
            case 3:
            case 4:
            case 5:
                goToDialogue(7);
                break;
            case 6:
            case 9:
                endCutscene();
                break;
            default:
                nextDialogue();
        }
    }
    
    private AbstractRelic getLostItem() {
        if (Wiz.p().relics.isEmpty()) {
            return null;
        }
        ArrayList<AbstractRelic> relics = new ArrayList<>();
        relics.addAll(Wiz.p().relics);
        Collections.shuffle(relics, new Random(AbstractDungeon.miscRng.randomLong()));
        return relics.get(0);
    }
    
    public void createOptionDialogue() {
        this.dialog.clear();
        if (this.lostRelic == null) {
            this.dialog.addDialogOption(OPTIONS[4], true);
        } else {
            String opt0 = String.format(OPTIONS[0], this.lostRelic.name);
            boolean hasRelic = Wiz.p().hasRelic(this.lostRelic.relicId);
            this.dialog.addDialogOption(opt0, !hasRelic, new CompassionMarkRelic()).setOptionResult((i)->{
                character.alreadyPerformedTransaction = true;
                Wiz.p().loseRelic(this.lostRelic.relicId);
                AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2, Settings.HEIGHT / 2, new CompassionMarkRelic());
                goToDialogue(3);
            });
        }

        String opt1 = String.format(OPTIONS[1], this.goldCost);
        boolean hasGold = Wiz.p().gold >= this.goldCost;
        this.dialog.addDialogOption(opt1, !hasGold, new IndulgenceMarkRelic()).setOptionResult((i)->{
            character.alreadyPerformedTransaction = true;
            Wiz.p().loseGold(this.goldCost);
            AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2, Settings.HEIGHT / 2, new IndulgenceMarkRelic());
            goToDialogue(4);
        });
        
        String opt2 = String.format(OPTIONS[2], this.maxHPCost);
        this.dialog.addDialogOption(opt2, new MartyrdomMarkRelic()).setOptionResult((i)->{
            character.alreadyPerformedTransaction = true;
            Wiz.p().decreaseMaxHealth(this.maxHPCost);
            AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2, Settings.HEIGHT / 2, new MartyrdomMarkRelic());
            goToDialogue(5);
        });

        this.dialog.addDialogOption(OPTIONS[3]).setOptionResult((i)->{
            goToDialogue(6);
        });
    }
}
