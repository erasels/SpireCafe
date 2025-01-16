package spireCafe.interactables.patrons.restingslayer;

import basemod.BaseMod;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.red.Bash;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import spireCafe.abstracts.AbstractCutscene;
import spireCafe.util.Wiz;
import spireCafe.util.cutsceneStrings.CutsceneStrings;
import spireCafe.util.cutsceneStrings.LocalizedCutsceneStrings;

import java.util.ArrayList;
import java.util.HashMap;

import static com.megacrit.cardcrawl.cards.AbstractCard.CardRarity.*;
import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.eventRng;
import static spireCafe.Anniv7Mod.makeID;
import static spireCafe.interactables.patrons.restingslayer.RestingSlayerPatron.capitalize;

public class RestingSlayerCutscene extends AbstractCutscene {
    public static final String ID = makeID(RestingSlayerCutscene.class.getSimpleName());
    private static final CutsceneStrings cutsceneStrings = LocalizedCutsceneStrings.getCutsceneStrings(ID);
    private ArrayList<AbstractCard> cards;
    private ArrayList<AbstractRelic> relics;
    private ArrayList<AbstractRelic> playerRelics;

    public RestingSlayerCutscene(RestingSlayerPatron slayer) {
        super(slayer, cutsceneStrings);
    }

    //Instead of only having one line of dialogue after the transaction this Patron says a different line each time.
    @Override
    public String getBlockingDialogue() {
        return cutsceneStrings.BLOCKING_TEXTS[0];
    }

    @Override
    protected void onClick() {
        if (dialogueIndex == 0) {
            RestingSlayerPatron slayer = (RestingSlayerPatron) character;
            slayer.updateOffer();
            this.cards = slayer.offeredCards;
            this.relics = slayer.offeredRelics;
            this.playerRelics = slayer.requestedRelics;
            nextDialogue();
            for (int r = 0; r < relics.size(); r++) {
                this.dialog.addDialogOption(OPTIONS[0].replace("{0}", capitalize(relics.get(r).tier.name())).replace("{1}", FontHelper.colorString(playerRelics.get(r).name, "r")).replace("{2}", FontHelper.colorString(relics.get(r).name, "g")), relics.get(r)).setOptionResult((i) -> {
                    nextDialogue();
                    character.alreadyPerformedTransaction = true;
                    for (int j = 0; j < AbstractDungeon.player.relics.size(); ++j) {
                        if (AbstractDungeon.player.relics.get(j).relicId.equals(playerRelics.get(i).relicId)) {
                            relics.get(i).makeCopy().instantObtain(AbstractDungeon.player, j, true);
                            break;
                        }
                    }
                });
            }
            for (AbstractCard card : cards) {
                this.dialog.addDialogOption(OPTIONS[1].replace("{0}", capitalize(card.rarity.name())).replace("{1}", FontHelper.colorString(capitalize(card.rarity.name()), "r")).replace("{2}", FontHelper.colorString(capitalize(card.name), "g")), card).setOptionResult((i) -> {
                    nextDialogue();
                    character.alreadyPerformedTransaction = true;
                    CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
                    for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
                        if (c.rarity == card.rarity) {
                            group.addToBottom(c);
                        }
                    }
                    if (!group.isEmpty()) {
                        AbstractDungeon.gridSelectScreen.open(group, 1, OPTIONS[2].replace("{0}", capitalize(card.rarity.name())), false);
                        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(card, Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
                    }
                });
            }
            this.dialog.addDialogOption(OPTIONS[OPTIONS.length - 1]).setOptionResult((i) -> {
                goToDialogue(3);
            });
        } else if(dialogueIndex==2){
            goToDialogue(99);
        } else {
            // Default behavior is to simply display the text in the next dialogue index.
            nextDialogue();
        }
    }


    @Override
    public void update() {
        super.update();
        if(!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            AbstractCard c = AbstractDungeon.gridSelectScreen.selectedCards.get(0);
            AbstractDungeon.player.masterDeck.removeCard(c);
            AbstractDungeon.gridSelectScreen.selectedCards.remove(c);
        }
    }

}