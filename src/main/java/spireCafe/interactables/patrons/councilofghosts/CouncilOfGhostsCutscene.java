package spireCafe.interactables.patrons.councilofghosts;

import basemod.cardmods.InnateMod;
import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.AutoplayField;
import com.megacrit.cardcrawl.actions.common.ObtainPotionAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.potions.GhostInAJar;
import com.megacrit.cardcrawl.relics.BurningBlood;
import com.megacrit.cardcrawl.relics.Sozu;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import spireCafe.abstracts.AbstractCutscene;
import spireCafe.abstracts.AbstractNPC;
import spireCafe.cardmods.AutoplayMod;
import spireCafe.util.cutsceneStrings.CutsceneStrings;
import spireCafe.util.cutsceneStrings.LocalizedCutsceneStrings;

import static spireCafe.Anniv7Mod.makeID;

public class CouncilOfGhostsCutscene extends AbstractCutscene {
    public static final String ID = makeID(CouncilOfGhostsCutscene.class.getSimpleName());
    private static final CutsceneStrings cutsceneStrings = LocalizedCutsceneStrings.getCutsceneStrings(ID);

    private static final float MAX_HP_COST = 0.5F;
    private static final float HP_COST = 0.25F;
    private final int maxHpCost, hpCost;

    public CouncilOfGhostsCutscene(AbstractNPC character) {
        super(character, cutsceneStrings);

        maxHpCost = AbstractDungeon.player.maxHealth == 1 ? 0 : MathUtils.ceil((float)AbstractDungeon.player.maxHealth * MAX_HP_COST);
        hpCost = MathUtils.ceil((float)AbstractDungeon.player.maxHealth * HP_COST);
    }

    @Override
    protected void onClick() {
        if (dialogueIndex == 0) {
            character.name = CouncilOfGhostsPatron.characterStrings.NAMES[1];
            nextDialogue();
            character.setCutscenePortrait("Ghost1");
        } else if (dialogueIndex == 1) {
            character.name = CouncilOfGhostsPatron.characterStrings.NAMES[2];
            nextDialogue();
            character.setCutscenePortrait("Ghost2");
        } else if (dialogueIndex == 2) {
            character.name = CouncilOfGhostsPatron.characterStrings.NAMES[3];
            nextDialogue();
            character.setCutscenePortrait("Ghost3");
            this.dialog.addDialogOption(OPTIONS[0]).setOptionResult((i)-> { // A deal?
                nextDialogue();
            });
            this.dialog.addDialogOption(OPTIONS[1]).setOptionResult((j)-> { // No thanks
                goToDialogue(16);
            });
        } else if (dialogueIndex == 4) {
            character.name = CouncilOfGhostsPatron.characterStrings.NAMES[2];
            nextDialogue();
            character.setCutscenePortrait("Ghost2");
        } else if (dialogueIndex == 5) {
            character.name = CouncilOfGhostsPatron.characterStrings.NAMES[1];
            nextDialogue();
            character.setCutscenePortrait("Ghost1");
            this.dialog.addDialogOption(OPTIONS[2] + maxHpCost + OPTIONS[3]).setOptionResult((k)-> { // Promise
                character.name = CouncilOfGhostsPatron.characterStrings.NAMES[3];
                nextDialogue();
                character.setCutscenePortrait("Ghost3");
                character.alreadyPerformedTransaction = true;
                AbstractDungeon.player.decreaseMaxHealth(maxHpCost);
                AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float) Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F, new BlankTombstone());
            });
            this.dialog.addDialogOption(OPTIONS[4] + hpCost + OPTIONS[5]).setOptionResult((l)-> { // Sacrifice
                character.name = CouncilOfGhostsPatron.characterStrings.NAMES[2];
                goToDialogue(10);
                character.setCutscenePortrait("Ghost2");
                character.alreadyPerformedTransaction = true;
                AbstractDungeon.player.damage(new DamageInfo((AbstractCreature)null, hpCost));
                if (AbstractDungeon.player.hasRelic(Sozu.ID))
                    AbstractDungeon.player.getRelic(Sozu.ID).flash();
                else
                    AbstractDungeon.player.obtainPotion(new GhostInAJar());
            });
            this.dialog.addDialogOption(OPTIONS[6]).setOptionResult((m)-> { // Leave
                goToDialogue(16);
            });
        } else if (dialogueIndex == 8) {
            nextDialogue();
            character.setCutscenePortrait("Mist");
        } else if (dialogueIndex == 9) {
            endCutscene();
        } else if (dialogueIndex == 11) {
            character.name = CouncilOfGhostsPatron.characterStrings.NAMES[1];
            nextDialogue();
            character.setCutscenePortrait("Ghost1");
        } else if (dialogueIndex == 12) {
            character.name = CouncilOfGhostsPatron.characterStrings.NAMES[2];
            nextDialogue();
            character.setCutscenePortrait("Ghost2");
        } else if (dialogueIndex == 13) {
            nextDialogue();
            character.setCutscenePortrait("Mist");
        } else if (dialogueIndex == 15) {
            endCutscene();
        } else if (dialogueIndex == 16) {
            character.name = CouncilOfGhostsPatron.characterStrings.NAMES[2];
            nextDialogue();
            character.setCutscenePortrait("Ghost2");
        } else if (dialogueIndex == 17) {
            nextDialogue();
            character.setCutscenePortrait("Mist");
        } else
            nextDialogue();
    }

}