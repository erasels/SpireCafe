package spireCafe.interactables.patrons.powerelic.implementation;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.BottledTornado;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import com.megacrit.cardcrawl.vfx.combat.IntenseZoomEffect;
import spireCafe.CafeRoom;
import spireCafe.abstracts.AbstractCafeInteractable;
import spireCafe.abstracts.AbstractCutscene;
import spireCafe.abstracts.AbstractNPC;
import spireCafe.interactables.patrons.looter.LooterCutscene;
import spireCafe.interactables.patrons.looter.LooterPatron;
import spireCafe.interactables.patrons.powerelic.PowerelicAllowlist;
import spireCafe.interactables.patrons.powerelic.PowerelicConfig;
import spireCafe.interactables.patrons.trashking.TrashKingPatron;
import spireCafe.ui.Dialog;
import spireCafe.util.Wiz;
import spireCafe.util.cutsceneStrings.CutsceneStrings;
import spireCafe.util.cutsceneStrings.LocalizedCutsceneStrings;

import java.util.*;

import static spireCafe.Anniv7Mod.makeID;
import static spireCafe.interactables.patrons.powerelic.implementation.PowerelicPatron.getAllConvertiblePowers;

public class PowerelicCutscene extends AbstractCutscene {
    public static final String ID = makeID(PowerelicCutscene.class.getSimpleName());
    private static final CutsceneStrings cutsceneStrings = LocalizedCutsceneStrings.getCutsceneStrings(ID);
    protected boolean alreadyTalkedOnce=false;
    public int looterGold=0;
    public boolean ignoreLooter=false;
    public boolean trashKingExists=false;
    public AbstractNPC character;
    public boolean snootBoop=false;
    public PowerelicCutscene(AbstractNPC character, boolean snootBoop) {
        super(character, cutsceneStrings);
        this.snootBoop=snootBoop;
        this.character=character;
        if(character instanceof PowerelicPatron) {
            alreadyTalkedOnce = ((PowerelicPatron)character).alreadyTalkedOnce;
            ignoreLooter = ((PowerelicPatron)character).ignoreLooter;
        }
        looterGold=0;
        if(AbstractDungeon.getCurrRoom().event instanceof CafeRoom){
            CafeRoom cafe = (CafeRoom)AbstractDungeon.getCurrRoom().event;
            List<AbstractCafeInteractable> inhabitants = cafe.getCurrentInhabitants();
            for(AbstractCafeInteractable i : inhabitants){
                if(!ignoreLooter && i instanceof LooterPatron){
                    if(((LooterPatron)i).stealTarget == null)
                        LooterCutscene.generateStealTarget((LooterPatron)i);
                    if(((LooterPatron)i).stealTarget == character)
                        looterGold = ((LooterPatron)i).rewardGold;
                }
                if(i instanceof TrashKingPatron){
                    trashKingExists=true;
                }
            }
        }
        if(snootBoop){
            //note that this only applies if showBlockingDialogue doesn't activate, so we need to check for it there too
            setupSnootBoopEasterEgg();
        }else{
            setupConversationStart(false);
        }
    }
    public void setupSnootBoopEasterEgg(){
        character.cutscenePortrait=null;
        playSnootBoopEasterEggEffects();
        dialogueIndex=26;
    }

    public void setupConversationStart(boolean useGoto){
        dialogueIndex = 0;
        String cClass = Wiz.adp().chosenClass.toString();
        if(cClass.equals("IRONCLAD") || cClass.equals("THE_SILENT") || cClass.equals("WATCHER")){
            dialogueIndex=1;
        }else if(cClass.equals("DEFECT")){
            dialogueIndex=2;
        }else{
            dialogueIndex=3;
        }
        if(alreadyTalkedOnce) {
            if(!ViolescentShard.getOutfoxedStatus()) {
                dialogueIndex = 6;
            }else{
                dialogueIndex = 23;
            }
        }
        if(alreadyPerformedTransaction){
            ((PowerelicPatron)character).alreadyShowedBlockingDialogue=true;
            super.dialogueIndex=character.blockingDialogueIndex+27;
        }
        if(useGoto){
            goToDialogue(dialogueIndex);
        }
        if(dialogueIndex==6 && !character.alreadyPerformedTransaction) {
            setupChoices();
        }else if(dialogueIndex==23 && !character.alreadyPerformedTransaction) {
            setupALLChoices();
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
            ViolescentShard.setOutfoxedStatus(true);
            convertSelectedCardsToRelics(selectedCards);
            playEffectsAfterConverting();
            nextDialogue();
        } else if(dialogueIndex==8){
            nextDialogue();
        } else if (dialogueIndex == 9 || dialogueIndex == 10) {
            endCutscene();
        } else if(dialogueIndex==11){
            nextDialogue();
        } else if(dialogueIndex==12){
            nextDialogue();
            setupRelicChoices();
        } else if(dialogueIndex==14){
            goToDialogue(16);
            this.dialog.addDialogOption(OPTIONS[7]).setOptionResult((i) -> {
                goToDialogue(19);
            });
        } else if(dialogueIndex==19){
            looterGold=0;
            if(character instanceof PowerelicPatron)
            {
                ((PowerelicPatron)character).ignoreLooter=true;
                ignoreLooter=true;
            }
            nextDialogue();
            setupRelicChoices();
        } else if(dialogueIndex==22||dialogueIndex==24||dialogueIndex==25){
            endCutscene();
        } else if(dialogueIndex==26){
            character.cutscenePortrait=((PowerelicPatron)character).standardCutscenePortrait;
            if(!((PowerelicPatron)character).alreadyShowedBlockingDialogue){
                setupConversationStart(true);
            }else{
                endCutscene();
            }
        } else if(dialogueIndex>=27){
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
        if(dialogueIndex==14){
            text0=DESCRIPTIONS[14]
                    +(PowerelicConfig.RELIC_COST+looterGold)
                    +DESCRIPTIONS[15];
        }
        if(dialogueIndex==16){
            text0=DESCRIPTIONS[16]
                    +PowerelicConfig.RELIC_COST
                    +DESCRIPTIONS[17]
                    +looterGold
                    +DESCRIPTIONS[18];
        }
        if(dialogueIndex==20){
            text0=DESCRIPTIONS[20]
                    +PowerelicConfig.RELIC_COST
                    +DESCRIPTIONS[21];
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
        if(!ViolescentShard.getOutfoxedStatus()) {
            this.dialog.addDialogOption(OPTIONS[2],true).setOptionResult((i) -> {
            });
            this.dialog.addDialogOption(OPTIONS[1]).setOptionResult((i) -> {
                goToDialogue(10);
            });
        }else{
            this.dialog.addDialogOption(OPTIONS[3],false).setOptionResult((i) -> {
                character.blockingDialogueIndex=1;
                goToDialogue(11);
            });
        }
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

    public void playSnootBoopEasterEggEffects(){
        Hitbox snootHb=((PowerelicPatron)character).snootHitbox;
        AbstractDungeon.effectsQueue.add(new IntenseZoomEffect(snootHb.x+snootHb.width/2, snootHb.y+snootHb.height/2,false));
        AbstractDungeon.effectsQueue.add(new CustomHeartBuffEffect(snootHb.x+snootHb.width/2, snootHb.y+snootHb.height/2));
    }

    private void setupRelicChoices(){
        int displayedCost=PowerelicConfig.RELIC_COST+looterGold;
        this.dialog.addDialogOption(OPTIONS[4]+displayedCost+OPTIONS[5],Wiz.adp().gold<displayedCost, new ViolescentShard()).setOptionResult((i) -> {
            buyRelic();
            goToDialogue(22);
        });
        if(looterGold>0){
            this.dialog.addDialogOption(OPTIONS[6]).setOptionResult((i) -> {
                nextDialogue();
                goToDialogue(14);
            });
        }else{
            this.dialog.addDialogOption(OPTIONS[1]).setOptionResult((i) -> {
                nextDialogue();
                goToDialogue(trashKingExists?25:24);
            });
        }
    }

    private void setupALLChoices(){
        this.dialog.addDialogOption(OPTIONS[0],!enableCheck()).setOptionResult((i) -> {
            doTheThing();
            nextDialogue();
            goToDialogue(7);
        });
        int displayedCost=PowerelicConfig.RELIC_COST+looterGold;
        this.dialog.addDialogOption(OPTIONS[4]+displayedCost+OPTIONS[5],Wiz.adp().gold<displayedCost,new ViolescentShard()).setOptionResult((i) -> {
            buyRelic();
            goToDialogue(22);
        });
        this.dialog.addDialogOption(OPTIONS[1]).setOptionResult((i) -> {
            nextDialogue();
            goToDialogue(trashKingExists?25:24);
        });
    }

    public void buyRelic(){
        character.alreadyPerformedTransaction = true;
        Wiz.adp().loseGold(PowerelicConfig.RELIC_COST+looterGold);
        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2f, Settings.HEIGHT / 2f, new ViolescentShard());
    }
    @Override
    public void update() {
        if (!shouldRunCutscene()) {
            return;
        }
        if (this.show) {
            AbstractDungeon.overlayMenu.showBlackScreen(blackScreenValue);
            isInCutscene = true;
            //this cutscene uses custom logic for whether to display blocking dialogue
            updateDialogueText();
        }
        this.hb.update();
        if (Dialog.optionList.isEmpty()) {
            if (this.hb.hovered && InputHelper.justClickedLeft) {
                InputHelper.justClickedLeft = false;
                this.hb.clickStarted = true;
            }
            if (this.hb.clicked) {
                this.hb.clicked = false;
                //we don't use the blocking dialogue system, so we need to call endCutscene manually
                onClick();
            }
        }
        this.dialog.update();

        if (!this.cardsAreSelected && AbstractDungeon.gridSelectScreen.selectedCards.size() == DEFAULT_CARDS_TO_CONVERT) {
            this.selectedCards=new ArrayList<>(AbstractDungeon.gridSelectScreen.selectedCards);
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            CardCrawlGame.sound.play("ATTACK_MAGIC_BEAM_SHORT");
        }
    }

}