package spireCafe.interactables.attractions.discord_statue;

import static spireCafe.Anniv7Mod.makeID;

import java.util.Random;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
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

                for (AbstractCard c : Wiz.p().masterDeck.group) {
                    switch (random.nextInt(10)) {
                        case 0:
                            CardModifierManager.addModifier(c, new RandomSizeModifier());
                            showChangedCard(c, random);
                            break;
                        case 1:
                            CardModifierManager.addModifier(c, new RandomColorModifier());
                            showChangedCard(c, random);
                            break;
                        case 2:
                            CardModifierManager.addModifier(c, new RandomColorModifier());
                            showChangedCard(c, random);
                            break;
                        case 3:
                            break;
                        case 4:
                            break;
                        case 5:
                            break;
                        default:
                            CardModifierManager.addModifier(c, new AnagramNameModifier());
                            showChangedCard(c, random);
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

    private void showChangedCard(AbstractCard c, Random random) {
        float x = Settings.WIDTH * 0.5F + random.nextFloat() * Settings.WIDTH * 0.75F - Settings.WIDTH * 0.375F;
        float y = Settings.HEIGHT * 0.5F + random.nextFloat() * Settings.HEIGHT * 0.35F - Settings.HEIGHT * 0.175F;
        AbstractDungeon.topLevelEffectsQueue.add(new ShowCardBrieflyEffect(c.makeStatEquivalentCopy(), x, y));
    }
}