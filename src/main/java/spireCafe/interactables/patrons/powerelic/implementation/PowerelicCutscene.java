package spireCafe.interactables.patrons.powerelic.implementation;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
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
import spireCafe.interactables.patrons.powerelic.PowerelicAllowlist;
import spireCafe.interactables.patrons.powerelic.PowerelicConfig;
import spireCafe.util.Wiz;
import spireCafe.util.cutsceneStrings.CutsceneStrings;
import spireCafe.util.cutsceneStrings.LocalizedCutsceneStrings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import static spireCafe.Anniv7Mod.makeID;
import static spireCafe.interactables.patrons.powerelic.implementation.PowerelicPatron.getAllConvertiblePowers;

public class PowerelicCutscene extends AbstractCutscene {
    public static final String ID = makeID(PowerelicCutscene.class.getSimpleName());
    private static final CutsceneStrings cutsceneStrings = LocalizedCutsceneStrings.getCutsceneStrings(ID);

    protected boolean alreadyTalkedOnce=false;

    public PowerelicCutscene(AbstractNPC character) {
        super(character, cutsceneStrings);
        if(character instanceof PowerelicPatron) {
            alreadyTalkedOnce = ((PowerelicPatron)character).alreadyTalkedOnce;
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
        if(alreadyTalkedOnce) {
            dialogueIndex = 6;
            if(!character.alreadyPerformedTransaction) {
                setupChoices();
            }
        }
    }

    public void doTheThing(){
        openCardSelectScreen();
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
        }else if(dialogueIndex==4){
            nextDialogue();
            setupChoices();
        }else if (dialogueIndex == 5 || dialogueIndex==6) {
            //do nothing; waiting for player to click a button
        }else if(dialogueIndex==7) {
            character.alreadyPerformedTransaction = true;
            convertSelectedCardsToRelics(selectedCards);
            playEffectsAfterConverting();
            nextDialogue();
        } else if(dialogueIndex==8){
            nextDialogue();
        } else if (dialogueIndex >= 9) {
            endCutscene();
        }
    }

    @Override
    protected void updateDialogueText() {
        String text0 = DESCRIPTIONS[this.dialogueIndex];
        if(dialogueIndex==8 && !selectedCards.isEmpty()){
            ArrayList<String> words = new ArrayList<>(Arrays.asList(selectedCards.get(0).name.split(" ")));
            words.replaceAll(s -> "#y"+s);
            text0=String.format(text0,String.join(" ",words));
        }
        String text = appendSpeakerToDialogue(text0);
        if (this.show) {
            this.show = false;
            this.dialog.show(text);
        } else {
            this.dialog.updateBodyText(text);
        }
    }

    public boolean enableCheck(){
        //if the player has somehow lost their last power card after the event spawned, disable
        //if the player has somehow lost their last relic after the event spawned... proceed anyway
        return !getAllConvertiblePowers().isEmpty();
    }

    private void setupChoices(){
        this.dialog.addDialogOption(OPTIONS[0],!enableCheck()).setOptionResult((i) -> {
            doTheThing();
            nextDialogue();
            goToDialogue(7);
        });
//        this.dialog.addDialogOption(OPTIONS[1],!enableCheck()).setOptionResult((i) -> {
//            character.alreadyPerformedTransaction = true;
//            doTheVerySillyThing();
//            CardCrawlGame.sound.play("ATTACK_MAGIC_BEAM_SHORT");
//            goToDialogue(7);
//        });
        this.dialog.addDialogOption(OPTIONS[1]).setOptionResult((i) -> {
            goToDialogue(10);
        });
    }



    public boolean cardsAreSelected=true;
    public ArrayList<AbstractCard>selectedCards=new ArrayList<>();
    public final int DEFAULT_CARDS_TO_CONVERT = 1;
    public void openCardSelectScreen() {
        this.cardsAreSelected = false;
        CardGroup tmp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        tmp.group = new ArrayList<>(getAllConvertiblePowers());

        if (tmp.group.isEmpty()) {
            //this should only happen via debug commands
            this.cardsAreSelected = true;
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
        if (!this.cardsAreSelected && AbstractDungeon.gridSelectScreen.selectedCards.size() == DEFAULT_CARDS_TO_CONVERT) {
            this.selectedCards=new ArrayList<>(AbstractDungeon.gridSelectScreen.selectedCards);
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            CardCrawlGame.sound.play("ATTACK_MAGIC_BEAM_SHORT");
        }
    }

    public static void convertSelectedCardsToRelics(ArrayList<AbstractCard> selectedCards,boolean skipRelicConversion){
        int relicsToConvert = PowerelicConfig.calculateNumberOfRelicsToConvert(selectedCards);
        for(AbstractCard card : selectedCards){
            card.untip();
            card.unhover();

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

        ArrayList<AbstractRelic>convertibleRelics= PowerelicAllowlist.getAllConvertibleRelics();

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
            //later: perhaps a custom effect to show all the new cards fly into the deck slightly more gracefully
            AbstractDungeon.effectsQueue.add(new ShowCardAndObtainEffect(card, MathUtils.random(0.1F, 0.9F) * (float)Settings.WIDTH, MathUtils.random(0.2F, 0.8F) * (float)Settings.HEIGHT));
        }
        Wiz.adp().relics.removeIf(relic -> (relicsToConvert.contains(relic)));
        Wiz.adp().reorganizeRelics();
    }

    public static void convertAllPowersToRelics(){
        convertSelectedCardsToRelics(new ArrayList<AbstractCard>(getAllConvertiblePowers()));
    }
    public static void convertAllRelicsToPowers(){
        convertRandomRelicsToPowers(Wiz.adp().relics.size());
    }

    public static void playEffectsAfterConverting(){
        AbstractDungeon.effectsQueue.add(new BorderFlashEffect(Color.WHITE));
        CardCrawlGame.sound.play("ATTACK_MAGIC_FAST_1");
    }

}