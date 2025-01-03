package spireCafe.interactables.patrons.packmistress;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.curses.Clumsy;
import com.megacrit.cardcrawl.cards.curses.Writhe;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import spireCafe.abstracts.AbstractCutscene;
import spireCafe.abstracts.AbstractNPC;
import spireCafe.util.cutsceneStrings.CutsceneStrings;
import spireCafe.util.cutsceneStrings.LocalizedCutsceneStrings;

import static spireCafe.Anniv7Mod.makeID;

public class PackmistressCutscene extends AbstractCutscene {
    public static final String ID = makeID(PackmistressCutscene.class.getSimpleName());
    private static final CutsceneStrings cutsceneStrings = LocalizedCutsceneStrings.getCutsceneStrings(ID);

    private final AbstractRelic relic1;
    private final AbstractRelic relic2;
    private final int maxHp;
    private final AbstractCard curse;

    public PackmistressCutscene(AbstractNPC character) {
        super(character, cutsceneStrings);
        this.relic1 = RelicLibrary.getRelic("anniv5:HandyHaversack");
        this.relic2 = RelicLibrary.getRelic("anniv5:PMBoosterBox");
        this.maxHp = (int)(AbstractDungeon.player.maxHealth * 0.2f);
        this.curse = new Clumsy();
    }

    @Override
    protected void onClick() {
        if (dialogueIndex == 0) {
            nextDialogue();
        }
        else if (dialogueIndex == 1) {
            nextDialogue();
            this.dialog.addDialogOption(OPTIONS[0].replace("{0}", this.relic1.name).replace("{1}", this.maxHp + ""), this.relic1).setOptionResult((i)->{
                AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float)(Settings.WIDTH / 2), (float)(Settings.HEIGHT / 2), this.relic1);
                AbstractDungeon.player.decreaseMaxHealth(this.maxHp);
                character.alreadyPerformedTransaction = true;
                goToDialogue(3);
            });
            this.dialog.addDialogOption(OPTIONS[1].replace("{0}", this.relic2.name), this.relic2).setOptionResult((i)->{
                AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float)(Settings.WIDTH / 2), (float)(Settings.HEIGHT / 2), this.relic2);
                AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(this.curse, (float)(Settings.WIDTH / 2), (float)(Settings.HEIGHT / 2)));
                character.alreadyPerformedTransaction = true;
                goToDialogue(3);
            });
            this.dialog.addDialogOption(OPTIONS[2]).setOptionResult((i)-> goToDialogue(4));
        } else if (dialogueIndex >= 2) {
            endCutscene();
        }
    }
}