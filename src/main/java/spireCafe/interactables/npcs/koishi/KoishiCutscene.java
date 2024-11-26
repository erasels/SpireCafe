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
            super.onClick();
            this.dialog.addDialogOption(OPTIONS[0]);
        } else if (dialogueIndex == 8) {
            super.onClick();
            this.dialog.addDialogOption(OPTIONS[7] + FontHelper.colorString(OPTIONS[8] + maxhpCost + OPTIONS[9], "r") + " " + FontHelper.colorString(OPTIONS[10], "g"));
            this.dialog.addDialogOption(OPTIONS[11]);
        } else if (dialogueIndex == 6 || dialogueIndex == 7 || dialogueIndex == 12) {
            endCutscene();
        } else {
            super.onClick();
        }
    }

    @Override
    public void onOptionClick(int slot) {
        if (dialogueIndex == 2) {
            super.onClick();
            this.dialog.addDialogOption(OPTIONS[1]);
            this.dialog.addDialogOption(OPTIONS[2]);
        } else if (dialogueIndex == 3) {
            if (slot == 0) {
                super.onClick();
                this.dialog.addDialogOption(OPTIONS[3] + FontHelper.colorString(OPTIONS[4], "g") + " " + FontHelper.colorString(OPTIONS[5], "r"));
                this.dialog.addDialogOption(OPTIONS[6]);
            } else {
                super.onClick(8);
            }
        } else if (dialogueIndex == 4) {
            if (slot == 0) {
                super.onClick();
                forAugment = true;
                CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
                for (AbstractCard card : AbstractDungeon.player.masterDeck.group) {
                    if (card.cost != -2 && !AutoplayField.autoplay.get(card) && !CardModifierManager.hasModifier(card, AutoplayMod.ID)) {
                        group.addToBottom(card);
                    }
                }
                AbstractDungeon.gridSelectScreen.open(group, 1, OPTIONS[4] + " " + OPTIONS[5], false);
            } else {
                super.onClick(7);
            }
        } else if (dialogueIndex == 9) {
            if (slot == 0) {
                super.onClick();
                AbstractDungeon.player.decreaseMaxHealth(maxhpCost);
                forRemove = true;
                AbstractDungeon.gridSelectScreen.open(AbstractDungeon.player.masterDeck.getPurgeableCards(), 1, OPTIONS[10], false, false, false, true);
            } else {
                super.onClick(13);
            }
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