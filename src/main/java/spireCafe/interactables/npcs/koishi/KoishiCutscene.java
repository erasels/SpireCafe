package spireCafe.interactables.npcs.koishi;

import basemod.cardmods.InnateMod;
import basemod.helpers.CardModifierManager;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.AutoplayField;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import spireCafe.abstracts.AbstractCutscene;
import spireCafe.abstracts.AbstractNPC;
import spireCafe.cardmods.AutoplayMod;

import static spireCafe.Anniv7Mod.makeID;

public class KoishiCutscene extends AbstractCutscene {
    public static final String ID = makeID(KoishiCutscene.class.getSimpleName());
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

    private static final float MAX_HP_COST = 0.06F;
    private static final float HIGH_ASC_MAX_HP_COST = 0.08F;
    private final int maxhpCost;
    private boolean forRemove = false;
    private boolean forAugment = false;

    public KoishiCutscene(AbstractNPC character) {
        super(character, eventStrings);
        if (AbstractDungeon.ascensionLevel >= 15) {
            maxhpCost = (int)(HIGH_ASC_MAX_HP_COST * AbstractDungeon.player.maxHealth);
        } else {
            maxhpCost = (int)(MAX_HP_COST * AbstractDungeon.player.maxHealth);
        }
    }

    @Override
    protected void onClick() {
        if (dialogueIndex == 1) {
            nextDialogue();
            character.setCutscenePortrait("Portrait4");
            this.dialog.addDialogOption(OPTIONS[0]).setOptionResult((i)->{
                nextDialogue();
                character.setCutscenePortrait("Portrait2");
                this.dialog.addDialogOption(OPTIONS[1]).setOptionResult((j)->{
                    nextDialogue();
                    this.dialog.addDialogOption(OPTIONS[3] + FontHelper.colorString(OPTIONS[4], "g") + " " + FontHelper.colorString(OPTIONS[5], "r")).setOptionResult((l)->{
                        nextDialogue();
                        forAugment = true;
                        CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
                        for (AbstractCard card : AbstractDungeon.player.masterDeck.group) {
                            if (card.cost != -2 && !AutoplayField.autoplay.get(card) && !CardModifierManager.hasModifier(card, AutoplayMod.ID)) {
                                group.addToBottom(card);
                            }
                        }
                        AbstractDungeon.gridSelectScreen.open(group, 1, OPTIONS[4] + " " + OPTIONS[5], false);
                    });
                    this.dialog.addDialogOption(OPTIONS[6]).setOptionResult((m)-> {
                        goToDialogue(7);
                        character.setCutscenePortrait("Portrait3");
                    });
                });
                this.dialog.addDialogOption(OPTIONS[2]).setOptionResult((k)->{
                    goToDialogue(8);
                    character.setCutscenePortrait("Portrait4");
                });
            });
        } else if (dialogueIndex == 8) {
            nextDialogue();
            this.dialog.addDialogOption(OPTIONS[7] + FontHelper.colorString(OPTIONS[8] + maxhpCost + OPTIONS[9], "r") + " " + FontHelper.colorString(OPTIONS[10], "g")).setOptionResult((i)->{
                nextDialogue();
                AbstractDungeon.player.decreaseMaxHealth(maxhpCost);
                forRemove = true;
                AbstractDungeon.gridSelectScreen.open(AbstractDungeon.player.masterDeck.getPurgeableCards(), 1, OPTIONS[10], false, false, false, true);
            });
            this.dialog.addDialogOption(OPTIONS[11]).setOptionResult((j)->{
                goToDialogue(13);
                character.setCutscenePortrait("Portrait4");
            });
            character.setCutscenePortrait("Portrait5");
        } else if (dialogueIndex == 6 || dialogueIndex == 7 || dialogueIndex == 12) {
            endCutscene();
        } else if (dialogueIndex == 10) {
            nextDialogue();
            character.setCutscenePortrait("Portrait8");
        } else {
            nextDialogue();
        }
    }

    public void update() {
        super.update();
        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty() && forRemove) {
            for (int i = 0; i < AbstractDungeon.gridSelectScreen.selectedCards.size(); i++) {
                AbstractCard c = AbstractDungeon.gridSelectScreen.selectedCards.get(i);
                AbstractDungeon.effectList.add(new PurgeCardEffect(c));
                AbstractDungeon.player.masterDeck.removeCard(c);
            }
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            backToCutscene();
        }

        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty() && forAugment) {
            for (int i = 0; i < AbstractDungeon.gridSelectScreen.selectedCards.size(); i++) {
                AbstractCard c = AbstractDungeon.gridSelectScreen.selectedCards.get(i);
                CardModifierManager.addModifier(c, new InnateMod());
                CardModifierManager.addModifier(c, new AutoplayMod());
            }
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            backToCutscene();
        }
    }

}