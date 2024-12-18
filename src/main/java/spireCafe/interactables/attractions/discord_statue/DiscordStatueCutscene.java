package spireCafe.interactables.attractions.discord_statue;

import static spireCafe.Anniv7Mod.makeID;

import java.util.Random;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;

import basemod.animations.SpriterAnimation;
import basemod.helpers.CardModifierManager;
import spireCafe.Anniv7Mod;
import spireCafe.abstracts.AbstractCutscene;
import spireCafe.abstracts.AbstractNPC;
import spireCafe.interactables.attractions.discord_statue.modifiers.AnagramNameModifier;
import spireCafe.interactables.attractions.discord_statue.modifiers.RandomColorModifier;
import spireCafe.interactables.attractions.discord_statue.modifiers.RandomSizeModifier;
import spireCafe.util.Wiz;
import spireCafe.util.cutsceneStrings.CutsceneStrings;
import spireCafe.util.cutsceneStrings.LocalizedCutsceneStrings;

public class DiscordStatueCutscene extends AbstractCutscene {
    public static final String ID = makeID(DiscordStatueCutscene.class.getSimpleName());
    private static final CutsceneStrings cutsceneStrings = LocalizedCutsceneStrings.getCutsceneStrings(ID);

    public DiscordStatueCutscene(AbstractNPC character) {
        super(character, cutsceneStrings);
    }

    @Override
    protected void onClick() {
        if (dialogueIndex == 0) {
            this.dialog.addDialogOption(OPTIONS[0]).setOptionResult((i) -> {
                nextDialogue();
                character.alreadyPerformedTransaction = true;
                AbstractDungeon.effectList.add(new BorderFlashEffect(Color.WHITE));
                CardCrawlGame.sound.play("ORB_LIGHTNING_PASSIVE");
                AbstractDungeon.effectList
                        .add(new LightningEffect(character.animationX, character.animationY + 325.0F));
                character.animation = new SpriterAnimation(
                        Anniv7Mod.makeAttractionPath("discord_statue/DiscordStatueCracking.scml"));
                AbstractDungeon.topLevelEffectsQueue.add(new DiscordStatueStopCrackingEffect(character));

                Random random = new Random();
                int randomNumber;

                for (AbstractCard c : Wiz.p().masterDeck.group) {
                    randomNumber = random.nextInt(10);
                    switch (randomNumber) {
                        case 0:
                            CardModifierManager.addModifier(c, new RandomSizeModifier());
                            break;
                        case 1:
                            CardModifierManager.addModifier(c, new RandomColorModifier());
                            break;
                        case 2:
                            CardModifierManager.addModifier(c, new RandomColorModifier());
                            break;
                        case 3:
                            break;
                        case 4:
                            break;
                        case 5:
                            break;
                        default:
                            CardModifierManager.addModifier(c, new AnagramNameModifier());
                            break;
                    }
                }
            });

            this.dialog.addDialogOption(OPTIONS[1]).setOptionResult((i) -> {
                goToDialogue(2);
            });
        } else {
            endCutscene();
        }
    }
}