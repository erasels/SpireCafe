package spireCafe.interactables.patrons.dandadan;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.blue.BallLightning;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.potions.PotionSlot;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import spireCafe.Anniv7Mod;
import spireCafe.abstracts.AbstractCutscene;
import spireCafe.abstracts.AbstractNPC;
import spireCafe.util.TexLoader;
import spireCafe.util.Wiz;
import spireCafe.util.cutsceneStrings.CutsceneStrings;
import spireCafe.util.cutsceneStrings.LocalizedCutsceneStrings;

import static spireCafe.Anniv7Mod.makeID;

public class DandadanCutscene extends AbstractCutscene {
    public static final String ID = makeID(DandadanCutscene.class.getSimpleName());
    private static final CutsceneStrings cutsceneStrings = LocalizedCutsceneStrings.getCutsceneStrings(ID);

    private int maxHPLoss;

    private static final int STEAL_BALL_START = 4;
    private static final int STEAL_BALL_SPEAK = 7;
    private static final int BALL_LIGHTNING = 9;
    private static final int RIGHTBALL_POTION_START = 11;
    private static final int RIGHTBALL_POTION_END = 13;
    private static final int END_DIALOGUE = 15;

    private static final int STEAL_BALL_OPTION = 0;
    private static final int BALL_LIGHTNING_OPTION = 1;
    private static final int POTION_OPTION = 2;
    private static final int END_OPTION = 3;

    private static final TextureRegion EMPTY_PORTRAIT = new TextureRegion(
            TexLoader.getTexture(Anniv7Mod.makeCharacterPath("Dandadan/Empty.png")));

    public DandadanCutscene(AbstractNPC character) {
        super(character, cutsceneStrings);
    }

    @Override
    protected void onClick() {
        switch (dialogueIndex) {
            case 1:
                nextDialogue();
                maxHPLoss = AbstractDungeon.actNum == 1 ? (int) (0.1 * Wiz.p().maxHealth)
                        : (int) (0.05 * Wiz.p().maxHealth);
                this.dialog
                        .addDialogOption(String.format(OPTIONS[STEAL_BALL_OPTION], maxHPLoss))
                        .setOptionResult((i) -> {
                            character.alreadyPerformedTransaction = true;
                            goToDialogue(STEAL_BALL_START);
                            AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2, Settings.HEIGHT / 2,
                                    new GoldenBallRelic());
                            ((DandadanPatron) character).disappear();
                            character.cutscenePortrait = EMPTY_PORTRAIT;
                        });
                int goldForBallLightningCard = Wiz.p().masterMaxOrbs == 0 ? 69 : 10;
                boolean disableOption = Wiz.p().gold < goldForBallLightningCard;
                this.dialog
                        .addDialogOption(String.format(OPTIONS[BALL_LIGHTNING_OPTION], goldForBallLightningCard),
                                disableOption, new BallLightning())
                        .setOptionResult((i) -> {
                            goToDialogue(BALL_LIGHTNING);
                            character.alreadyPerformedTransaction = true;
                            Wiz.p().loseGold(goldForBallLightningCard);
                            AbstractDungeon.topLevelEffectsQueue
                                    .add(new ShowCardAndObtainEffect(
                                            new GoldenBallLightning(), Settings.WIDTH / 2.0F
                                                    - AbstractCard.IMG_WIDTH / 2.0F - 30.0F * Settings.scale,
                                            Settings.HEIGHT / 2.0F));
                        });
                int goldForPotion = 60;
                disableOption = Wiz.p().gold < goldForPotion;
                this.dialog
                        .addDialogOption(OPTIONS[POTION_OPTION], disableOption, new RightballPotion())
                        .setOptionResult((i) -> {
                            if (Wiz.p().hasPotion(PotionSlot.POTION_ID)) {
                                goToDialogue(RIGHTBALL_POTION_START);
                                character.alreadyPerformedTransaction = true;
                                Wiz.p().loseGold(goldForPotion);
                                Wiz.p().obtainPotion(new RightballPotion());
                            } else {
                                AbstractDungeon.topPanel.flashRed();
                            }
                        });
                this.dialog.addDialogOption(OPTIONS[END_OPTION]).setOptionResult((i) -> {
                    goToDialogue(END_DIALOGUE);
                });
                break;
            case STEAL_BALL_START + 1:
                Wiz.p().decreaseMaxHealth(maxHPLoss);
                AbstractDungeon.effectList.add(new BorderFlashEffect(Color.RED));
                nextDialogue();
                break;
            case STEAL_BALL_START + 2:
                AbstractRelic ball = Wiz.p().getRelic(GoldenBallRelic.ID);
                if (ball != null) {
                    ((GoldenBallRelic) ball).speak(DESCRIPTIONS[STEAL_BALL_SPEAK], 3.0F);
                }
                endCutscene();
                break;
            case RIGHTBALL_POTION_END:
            case BALL_LIGHTNING:
                endCutscene();
                break;
            
            default:
                nextDialogue();
                break;
        }
    }

}
