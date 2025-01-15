package spireCafe.interactables.patrons.powerelic;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.BottledTornado;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import spireCafe.abstracts.AbstractCutscene;
import spireCafe.abstracts.AbstractNPC;
import spireCafe.util.Wiz;
import spireCafe.util.cutsceneStrings.CutsceneStrings;
import spireCafe.util.cutsceneStrings.LocalizedCutsceneStrings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import static spireCafe.Anniv7Mod.makeID;

public class PowerelicCutscene extends AbstractCutscene {
    public static final String ID = makeID(PowerelicCutscene.class.getSimpleName());
    private static final CutsceneStrings cutsceneStrings = LocalizedCutsceneStrings.getCutsceneStrings(ID);

    protected boolean alreadyTalkedOnce=false;
    protected int goldCost=PowerelicPatron.DEFAULT_GOLD_COST;
    protected static final int EXTRA_COST_AFTER_ENERGY_TOTAL=1;

    public PowerelicCutscene(AbstractNPC character) {
        super(character, cutsceneStrings);
        if(character instanceof PowerelicPatron) {
            alreadyTalkedOnce = ((PowerelicPatron)character).alreadyTalkedOnce;
            goldCost = ((PowerelicPatron)character).goldCost;
        }

        dialogueIndex = 0;
        String cClass= Wiz.adp().chosenClass.toString();
        if(cClass.equals("IRONCLAD") || cClass.equals("THE_SILENT") || cClass.equals("WATCHER")){
            dialogueIndex=1;
        }else if(cClass.equals("DEFECT")){
            dialogueIndex=2;
        }else{
            dialogueIndex=3;
        }
//        if(alreadyTalkedOnce) {
//            dialogueIndex = 5;
//            if(!character.alreadyPerformedTransaction) {
//                setupChoices();
//            }
//        }
    }

    public void doTheThing(){
        openCardSelectScreen();
        CardCrawlGame.sound.play("ATTACK_MAGIC_BEAM_SHORT");
    }

    public static void doTheVerySillyThing(){
        convertAllPowersToRelics();
        convertAllRelicsToPowers();
        playEffectsAfterConverting();
    }

    @Override
    protected void onClick() {
        if (dialogueIndex <= 3) {
            goToDialogue(4);
            setupChoices();
        }else if (dialogueIndex == 4) {
            nextDialogue();
        }else if (dialogueIndex == 5){
            doTheVerySillyThing();
            nextDialogue();
        } else if (dialogueIndex >= 6) {
            endCutscene();
        }
    }

    public boolean enableCheck(){
        if(Wiz.p().gold < goldCost)return false;
        if(Wiz.deck().getCardsOfType(AbstractCard.CardType.POWER).isEmpty())return false;
        return true;
    }

    private void setupChoices(){
        this.dialog.addDialogOption(OPTIONS[0],!enableCheck()).setOptionResult((i) -> {
            character.alreadyPerformedTransaction = true;
            doTheThing();
            //Wiz.p().loseGold(goldCost);
            goToDialogue(6);
            //CardCrawlGame.sound.play("ATTACK_MAGIC_BEAM_SHORT");
        });
        this.dialog.addDialogOption(OPTIONS[1],!enableCheck()).setOptionResult((i) -> {
            character.alreadyPerformedTransaction = true;
            CardCrawlGame.sound.play("ATTACK_MAGIC_BEAM_SHORT");
            //Wiz.p().loseGold(goldCost);
            goToDialogue(5);
        });
        this.dialog.addDialogOption(OPTIONS[2]).setOptionResult((i) -> {
            goToDialogue(7);
        });
    }



    public boolean cardsSelected=true;
    public final int DEFAULT_CARDS_TO_CONVERT =1;
    public void openCardSelectScreen() {
        this.cardsSelected = false;
        CardGroup tmp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        tmp.group = new ArrayList<>(PowerelicPatron.getAllConvertiblePowers());

        if (tmp.group.isEmpty()) {
            this.cardsSelected = true;
            convertSelectedCardsToRelics(tmp.group);
        } else {
            if (!AbstractDungeon.isScreenUp) {
                AbstractDungeon.gridSelectScreen.open(tmp, DEFAULT_CARDS_TO_CONVERT, this.DESCRIPTIONS[0], false, false, false, false);
            } else {
                AbstractDungeon.dynamicBanner.hide();
                AbstractDungeon.previousScreen = AbstractDungeon.screen;
                AbstractDungeon.gridSelectScreen.open(tmp, DEFAULT_CARDS_TO_CONVERT, this.DESCRIPTIONS[0], false, false, false, false);
            }
        }
    }

    public void update() {
        super.update();
        if (!this.cardsSelected && AbstractDungeon.gridSelectScreen.selectedCards.size() == DEFAULT_CARDS_TO_CONVERT) {
            convertSelectedCardsToRelics(AbstractDungeon.gridSelectScreen.selectedCards);
            playEffectsAfterConverting();
        }
    }

    public static void convertSelectedCardsToRelics(ArrayList<AbstractCard> selectedCards,boolean skipRelicConversion){
        int relicsToConvert=0;
        for(AbstractCard card : selectedCards){
            card.untip();
            card.unhover();
            int effectivePowerCost=card.cost;
            if(effectivePowerCost<0)effectivePowerCost=Wiz.adp().energy.energyMaster;
            relicsToConvert+=(effectivePowerCost+EXTRA_COST_AFTER_ENERGY_TOTAL);

            //As a side effect, the card is automatically captured within the new relic (must remove from deck manually, below)
            AbstractRelic relic = new PowerelicRelic(card);
            AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F, relic);
            if(card.inBottleTornado){
                Wiz.adp().loseRelic(BottledTornado.ID);
            }
        }
        Wiz.deck().group.removeIf(card -> selectedCards.contains(card));
        if(!skipRelicConversion) {
            convertRandomRelicsToPowers(relicsToConvert);
        }
        AbstractDungeon.gridSelectScreen.selectedCards.clear();
    }
    public static void convertSelectedCardsToRelics(ArrayList<AbstractCard> selectedCards){
        convertSelectedCardsToRelics(selectedCards,false);
    }

    public static void convertRandomRelicsToPowers(int amount){

        ArrayList<AbstractRelic>convertibleRelics=PowerelicAllowlist.getAllConvertibleRelics();

        Collections.shuffle(convertibleRelics, new Random(AbstractDungeon.miscRng.randomLong()));
        if(amount>convertibleRelics.size())
            amount=convertibleRelics.size();
        ArrayList<AbstractRelic> relicsToConvert = new ArrayList<>(convertibleRelics.subList(0, amount));
        convertListOfRelicsToPowers(relicsToConvert);
    }

    public static void convertListOfRelicsToPowers(ArrayList<AbstractRelic> relicsToConvert){
        ArrayList<AbstractCard>newCards=new ArrayList<>();
        for(AbstractRelic relic : relicsToConvert){
            //As a side effect, the relic is automatically captured within the new card (must remove from list manually)
            PowerelicCard card = PowerelicCard.fromActiveRelic(relic);
            newCards.add(card);
            card.cardIsFreshlyConvertedFromRelic=true;
            //later: perhaps a custom effect to show all the new cards fly into the deck slightly more gracefully
            AbstractDungeon.effectsQueue.add(new ShowCardAndObtainEffect(card, Settings.WIDTH * .5F, Settings.HEIGHT * .5f));
        }
        Wiz.adp().relics.removeIf(relic -> (relicsToConvert.contains(relic)));
        Wiz.adp().reorganizeRelics();
    }

    public static void convertAllPowersToRelics(){
        convertSelectedCardsToRelics(new ArrayList<AbstractCard>(PowerelicPatron.getAllConvertiblePowers()));
    }
    public static void convertAllRelicsToPowers(){
        convertRandomRelicsToPowers(Wiz.adp().relics.size());
    }

    public static void playEffectsAfterConverting(){
        AbstractDungeon.effectsQueue.add(new BorderFlashEffect(Color.WHITE));
        CardCrawlGame.sound.play("ATTACK_MAGIC_FAST_1");
    }

}