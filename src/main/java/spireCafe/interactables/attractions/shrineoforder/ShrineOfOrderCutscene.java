package spireCafe.interactables.attractions.shrineoforder;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import spireCafe.abstracts.AbstractCutscene;
import spireCafe.abstracts.AbstractNPC;
import spireCafe.util.Wiz;
import spireCafe.util.cutsceneStrings.CutsceneStrings;
import spireCafe.util.cutsceneStrings.LocalizedCutsceneStrings;

import java.util.ArrayList;

import static spireCafe.Anniv7Mod.makeID;

public class ShrineOfOrderCutscene extends AbstractCutscene {
    public static final String ID = makeID(ShrineOfOrderCutscene.class.getSimpleName());
    private static final CutsceneStrings cutsceneStrings = LocalizedCutsceneStrings.getCutsceneStrings(ID);

    public ShrineOfOrderCutscene(AbstractNPC character) {
        super(character, cutsceneStrings);
    }

    @Override
    protected void onClick() {
        if (dialogueIndex == 0) {
            this.dialog.addDialogOption(OPTIONS[0]).setOptionResult((i) -> {
                goToDialogue(1);
                this.dialog.addDialogOption(OPTIONS[1] + FontHelper.colorString(OPTIONS[2], "y"), checkRarity(AbstractCard.CardRarity.COMMON)).setOptionResult((j) -> {
                    goToDialogue(2);
                    character.alreadyPerformedTransaction = true;
                    AbstractDungeon.effectList.add(new BorderFlashEffect(Color.WHITE));
                    OrderDeckForRarity(AbstractCard.CardRarity.COMMON);
                });
                this.dialog.addDialogOption(OPTIONS[4] + FontHelper.colorString(OPTIONS[5], "y"), checkRarity(AbstractCard.CardRarity.UNCOMMON)).setOptionResult((j) -> {
                    goToDialogue(2);
                    character.alreadyPerformedTransaction = true;
                    AbstractDungeon.effectList.add(new BorderFlashEffect(Color.WHITE));
                    OrderDeckForRarity(AbstractCard.CardRarity.UNCOMMON);
                });
                this.dialog.addDialogOption(OPTIONS[6] + FontHelper.colorString(OPTIONS[7], "y"), checkRarity(AbstractCard.CardRarity.RARE)).setOptionResult((j) -> {
                    goToDialogue(2);
                    character.alreadyPerformedTransaction = true;
                    AbstractDungeon.effectList.add(new BorderFlashEffect(Color.WHITE));
                    OrderDeckForRarity(AbstractCard.CardRarity.RARE);
                });
                this.dialog.addDialogOption(OPTIONS[8] + FontHelper.colorString(OPTIONS[9], "y"), checkBasic()).setOptionResult((j) -> {
                    goToDialogue(5);
                    character.alreadyPerformedTransaction = true;
                    AbstractDungeon.effectList.add(new BorderFlashEffect(Color.WHITE));
                    OrderOneBasic();
                });

                this.dialog.addDialogOption(OPTIONS[3]).setOptionResult((j) -> {
                    goToDialogue(4);
                });
            });

            this.dialog.addDialogOption(OPTIONS[3]).setOptionResult((i) -> {
                goToDialogue(3);
            });
        }
        else {
            endCutscene();
        }
    }

    private void OrderDeckForRarity(AbstractCard.CardRarity rarity){
        ArrayList<AbstractCard> cards = new ArrayList<>();
        for (AbstractCard c : Wiz.p().masterDeck.group){
            if (c.rarity == rarity){
                cards.add(c);
            }
        }
        if (!cards.isEmpty()) {
            AbstractCard RandomCard = cards.get(AbstractDungeon.cardRandomRng.random(cards.size()-1));
            for (AbstractCard c : cards){
                if (c != RandomCard) {
                    Wiz.p().masterDeck.removeCard(c);
                    AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(RandomCard.makeStatEquivalentCopy(), (float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
                }
            }
        }
    }
    private void OrderOneBasic(){
        CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (AbstractCard card : Wiz.p().masterDeck.group) {
            if (card.rarity == AbstractCard.CardRarity.BASIC) {
                group.addToTop(card);
            }
        }
        if (!group.isEmpty()) {
            AbstractDungeon.gridSelectScreen.open(group, 1, OPTIONS[10], false);
        }
    }

    public void update() {
        super.update();
        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            for (int i = 0; i < AbstractDungeon.gridSelectScreen.selectedCards.size(); i++) {
                AbstractCard c = AbstractDungeon.gridSelectScreen.selectedCards.get(i);
                ArrayList<AbstractCard> cards = new ArrayList<>();
                for (AbstractCard c2 : Wiz.p().masterDeck.group){
                    if (c2.rarity == AbstractCard.CardRarity.COMMON){
                        cards.add(c2);
                    }
                }
                if (!cards.isEmpty()) {
                    AbstractCard RandomCard = cards.get(AbstractDungeon.cardRandomRng.random(cards.size()-1));
                    Wiz.p().masterDeck.removeCard(c);
                    AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(RandomCard.makeStatEquivalentCopy(), (float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
                }
            }
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            backToCutscene();
        }
    }
    private boolean checkRarity(AbstractCard.CardRarity rarity){
        ArrayList<AbstractCard> cards = new ArrayList<>();
        for (AbstractCard c : Wiz.p().masterDeck.group){
            if (c.rarity == rarity){
                cards.add(c);
                if (cards.size() >= 2) {
                    return false;
                }
            }
        }
        return true;
    }
    private boolean checkBasic(){
        int cards = 0;
        for (AbstractCard c : Wiz.p().masterDeck.group){
            if (c.rarity == AbstractCard.CardRarity.BASIC){
                cards++;
                break;
            }
        }
        for (AbstractCard c : Wiz.p().masterDeck.group){
            if (c.rarity == AbstractCard.CardRarity.COMMON){
                cards++;
                break;
            }
        }
        if(cards == 2){return false;}
        return true;
    }
}