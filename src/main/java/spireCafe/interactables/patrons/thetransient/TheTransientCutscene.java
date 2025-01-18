package spireCafe.interactables.patrons.thetransient;

import basemod.ReflectionHacks;
import basemod.cardmods.InnateMod;
import basemod.helpers.CardModifierManager;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.AutoplayField;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.BurningBlood;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import spireCafe.Anniv7Mod;
import spireCafe.abstracts.AbstractCutscene;
import spireCafe.abstracts.AbstractNPC;
import spireCafe.cardmods.AutoplayMod;
import spireCafe.cardmods.TransientMod;
import spireCafe.util.cutsceneStrings.CutsceneStrings;
import spireCafe.util.cutsceneStrings.LocalizedCutsceneStrings;

import static spireCafe.Anniv7Mod.makeID;

public class TheTransientCutscene extends AbstractCutscene {
    public static final String ID = makeID(TheTransientCutscene.class.getSimpleName());
    private static final CutsceneStrings cutsceneStrings = LocalizedCutsceneStrings.getCutsceneStrings(ID);
    private static final int MAX_HP_LOSS = 4;

    public TheTransientCutscene(AbstractNPC character) {
        super(character, cutsceneStrings);
    }

    @Override
    protected void onClick() {
        switch (dialogueIndex){
            case 0:
                nextDialogue();

                this.dialog.addDialogOption(String.format(OPTIONS[0], MAX_HP_LOSS), new BurningBlood(){ //lmao get hijacked idiot relic (altering relic preview text to display transient definition)
                    @Override
                    protected void initializeTips() {
                        this.tips.clear();
                        this.tips.add(new PowerTip(OPTIONS[2], OPTIONS[3]));
                        super.initializeTips();
                    }
                }).setOptionResult((i)->{
                    goToDialogue(2);
                });
                this.dialog.addDialogOption(OPTIONS[1]).setOptionResult((i)-> goToDialogue(7));
                break;
            case 3:
                nextDialogue();

                this.dialog.addDialogOption(OPTIONS[5]).setOptionResult((i)->{
                    AbstractDungeon.player.decreaseMaxHealth(MAX_HP_LOSS);

                    CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
                    for (AbstractCard card : AbstractDungeon.player.masterDeck.group) {
                        if (card.cost != -2 && !CardModifierManager.hasModifier(card, TransientMod.ID)) {
                            group.addToBottom(card);
                        }
                    }
                    AbstractDungeon.gridSelectScreen.open(group, 1, OPTIONS[4], false);

                    character.alreadyPerformedTransaction = true;
                    goToDialogue(5);
                    character.blockingDialogueIndex = 0;
                });
                break;
            case 5:
            case 6:
            case 7:
                endCutscene();
                break;
            default:
                nextDialogue();
                break;

        }
    }

    //grid selection for applying transient mod
    public void update() {
        super.update();
        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            for (int i = 0; i < AbstractDungeon.gridSelectScreen.selectedCards.size(); i++) {
                AbstractCard c = AbstractDungeon.gridSelectScreen.selectedCards.get(i);
                CardModifierManager.addModifier(c, new TransientMod());
            }
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            backToCutscene();
        }
    }
}