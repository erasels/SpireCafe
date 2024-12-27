package spireCafe.interactables.patrons.dandadan;

import static spireCafe.Anniv7Mod.makeID;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.blue.BallLightning;
import com.megacrit.cardcrawl.characters.AbstractPlayer.PlayerClass;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.potions.PotionSlot;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

import spireCafe.Anniv7Mod;
import spireCafe.abstracts.AbstractCutscene;
import spireCafe.abstracts.AbstractNPC;
import spireCafe.util.TexLoader;
import spireCafe.util.Wiz;
import spireCafe.util.cutsceneStrings.CutsceneStrings;
import spireCafe.util.cutsceneStrings.LocalizedCutsceneStrings;

public class DandadanCutscene extends AbstractCutscene {
    public static final String ID = makeID(DandadanCutscene.class.getSimpleName());
    private static final CutsceneStrings cutsceneStrings = LocalizedCutsceneStrings.getCutsceneStrings(ID);

    private int maxHPLoss;

    public DandadanCutscene(AbstractNPC character) {
        super(character, cutsceneStrings);
    }

    @Override
    protected void onClick() {
        if (dialogueIndex == 1) {
            nextDialogue();
            maxHPLoss = AbstractDungeon.actNum == 1 ? (int) (0.1 * Wiz.p().maxHealth)
                    : (int) (0.05 * Wiz.p().maxHealth);
            this.dialog
                    .addDialogOption(OPTIONS[0] + FontHelper.colorString(String.format(OPTIONS[1], maxHPLoss), "r"))
                    .setOptionResult((i) -> {
                        character.alreadyPerformedTransaction = true;
                        nextDialogue();
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2, Settings.HEIGHT / 2,
                                new GoldenBallRelic());
                        ((DandadanPatron) character).disappear();
                        character.cutscenePortrait = new TextureRegion(
                                TexLoader.getTexture(Anniv7Mod.makeCharacterPath("Dandadan/Empty.png")));
                    });
            int goldForBallLightningCard = Wiz.p().masterMaxOrbs == 0 ? 69 : 10;
            boolean disableOption = Wiz.p().gold < goldForBallLightningCard;
            this.dialog
                    .addDialogOption(OPTIONS[2] + String.format(FontHelper.colorString(OPTIONS[3], "r"),
                            goldForBallLightningCard), disableOption, new BallLightning())
                    .setOptionResult((i) -> {
                        goToDialogue(5);
                        character.alreadyPerformedTransaction = true;
                        Wiz.p().loseGold(goldForBallLightningCard);
                        // add 1 orb slot if player has none
                        if (Wiz.p().chosenClass != PlayerClass.DEFECT && Wiz.p().masterMaxOrbs == 0) {
                            Wiz.p().masterMaxOrbs = 1;
                        }
                        AbstractDungeon.topLevelEffectsQueue.add(new ShowCardAndObtainEffect(new GoldenBallLightning(),
                                Settings.WIDTH / 2.0F - AbstractCard.IMG_WIDTH / 2.0F - 30.0F * Settings.scale,
                                Settings.HEIGHT / 2.0F));
                    });
            disableOption = Wiz.p().gold < 20;
            this.dialog
                    .addDialogOption(OPTIONS[4] + FontHelper.colorString(OPTIONS[5], "r"), disableOption,
                            new RightballPotion())
                    .setOptionResult((i) -> {
                        if (Wiz.p().hasPotion(PotionSlot.POTION_ID)) {
                            goToDialogue(7);
                            character.alreadyPerformedTransaction = true;
                            Wiz.p().loseGold(20);
                            Wiz.p().obtainPotion(new RightballPotion());
                        } else {
                            AbstractDungeon.topPanel.flashRed();
                        }
                    });
            this.dialog.addDialogOption(OPTIONS[6]).setOptionResult((i) -> {
                goToDialogue(9);
            });
        } else if (dialogueIndex == 4) {
            AbstractRelic ball = Wiz.p().getRelic(GoldenBallRelic.ID);
            if (ball != null) {
                ((GoldenBallRelic) ball).speak(DESCRIPTIONS[10], 3.0F);
            }
            endCutscene();
        }
        else if (dialogueIndex == 6 || dialogueIndex == 8 || dialogueIndex == 9) {
            endCutscene();
        } else if (dialogueIndex == 3) {
            Wiz.p().decreaseMaxHealth(maxHPLoss);
            nextDialogue();
        } else {
            nextDialogue();
        }
    }

}
