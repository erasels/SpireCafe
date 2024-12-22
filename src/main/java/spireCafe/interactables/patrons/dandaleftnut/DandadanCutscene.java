package spireCafe.interactables.patrons.dandaleftnut;

import static spireCafe.Anniv7Mod.makeID;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer.PlayerClass;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.potions.FirePotion;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

import spireCafe.abstracts.AbstractCutscene;
import spireCafe.abstracts.AbstractNPC;
import spireCafe.util.Wiz;
import spireCafe.util.cutsceneStrings.CutsceneStrings;
import spireCafe.util.cutsceneStrings.LocalizedCutsceneStrings;

public class DandadanCutscene extends AbstractCutscene {
    public static final String ID = makeID(DandadanCutscene.class.getSimpleName());
    private static final CutsceneStrings cutsceneStrings = LocalizedCutsceneStrings.getCutsceneStrings(ID);

    public DandadanCutscene(AbstractNPC character) {
        super(character, cutsceneStrings);
    }

    @Override
    protected void onClick() {
        if (dialogueIndex == 1) {
            nextDialogue();
            int maxHPLoss = AbstractDungeon.actNum == 1 ? (int) (0.1 * Wiz.p().maxHealth)
                    : (int) (0.05 * Wiz.p().maxHealth);
            this.dialog.addDialogOption(OPTIONS[0] + FontHelper.colorString(String.format(OPTIONS[1], maxHPLoss), "r"))
                    .setOptionResult((i) -> {
                        character.alreadyPerformedTransaction = true;
                        nextDialogue();
                        Wiz.p().decreaseMaxHealth(maxHPLoss);
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2, Settings.HEIGHT / 2,
                                new GoldenBallRelic());
                    });
            boolean disableOption = Wiz.p().gold < 10;
            this.dialog.addDialogOption(OPTIONS[2] + FontHelper.colorString(OPTIONS[3], "r"), disableOption)
                    .setOptionResult((i) -> {
                        goToDialogue(5);
                        character.alreadyPerformedTransaction = true;
                        Wiz.p().loseGold(10);
                        // add 1 orb slot if player has none
                        if (Wiz.p().chosenClass != PlayerClass.DEFECT && Wiz.p().masterMaxOrbs == 0) {
                            Wiz.p().masterMaxOrbs = 1;
                        }
                        AbstractDungeon.topLevelEffectsQueue.add(new ShowCardAndObtainEffect(new GoldenBallLightning(),
                                Settings.WIDTH / 2.0F - AbstractCard.IMG_WIDTH / 2.0F - 30.0F * Settings.scale,
                                Settings.HEIGHT / 2.0F));
                    });
            disableOption = Wiz.p().gold < 20;
            this.dialog.addDialogOption(OPTIONS[4] + FontHelper.colorString(OPTIONS[5], "r"), disableOption)
                    .setOptionResult((i) -> {
                        goToDialogue(7);
                        character.alreadyPerformedTransaction = true;
                        Wiz.p().loseGold(20);
                        Wiz.p().obtainPotion(new FirePotion());

                    });
            this.dialog.addDialogOption(OPTIONS[6]).setOptionResult((i) -> {
                goToDialogue(9);
            });
        } else if (dialogueIndex == 4 || dialogueIndex == 6 || dialogueIndex == 8) {
            endCutscene();
        } else {
            nextDialogue();
        }
    }

}
