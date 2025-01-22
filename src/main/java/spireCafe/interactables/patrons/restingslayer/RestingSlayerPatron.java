package spireCafe.interactables.patrons.restingslayer;

import basemod.BaseMod;
import basemod.ReflectionHacks;
import basemod.abstracts.CustomPlayer;
import basemod.animations.AbstractAnimation;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.SkeletonJson;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import spireCafe.Anniv7Mod;
import spireCafe.abstracts.AbstractPatron;
import spireCafe.util.TexLoader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.stream.Collectors;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.eventRng;

public class RestingSlayerPatron extends AbstractPatron {
    public static final String ID = RestingSlayerPatron.class.getSimpleName();
    private static final CharacterStrings characterStrings = CardCrawlGame.languagePack.getCharacterString(Anniv7Mod.makeID(ID));
    public final AbstractPlayer slayer;
    private ArrayList<AbstractCard> cards = new ArrayList<>(); // Potential cards the slayer may offer, should have one card of each available rarity.
    private ArrayList<AbstractRelic> relics = new ArrayList<>(); // Potential relics the slayer may offer, should have one relic of each available tier.
    private ArrayList<AbstractRelic> playerRelics = new ArrayList<>(); // Potential relics the player has the slayer may request, should have one relic of each available tier.
    public ArrayList<AbstractCard> offeredCards = new ArrayList<>(); // Cards the slayer is currently offering and showing a trade for.
    public ArrayList<AbstractRelic> offeredRelics = new ArrayList<>(); // Relics the slayer is currently offering and showing a trade for.
    public ArrayList<AbstractRelic> requestedRelics = new ArrayList<>(); // Relics the player has and the slayer is currently showing a trade for.
    public boolean hasBasic;
    public boolean hasCommon;
    public boolean hasUncommon;
    public boolean hasRare;
    public Random restingSlayerRng;

    public RestingSlayerPatron(float animationX, float animationY) {
        super(animationX, animationY, 160.0f, 200.0f);

        restingSlayerRng = new Random(eventRng.randomLong());

        HashSet<String> blockedCharacters = new HashSet<>(Arrays.asList(
                "THE_PACKMASTER",
                "THE_SISTERS",
                "Librarian",
                "THE_RAINBOW",
                "Tuner_CLASS"
        ));
        ArrayList<AbstractPlayer> options = CardCrawlGame.characterManager.getAllCharacters().stream()
                .filter(p -> p.chosenClass != AbstractDungeon.player.chosenClass
                        && !blockedCharacters.contains(p.chosenClass.name()))
                .collect(Collectors.toCollection(ArrayList::new));
        slayer = !options.isEmpty() ? options.get(restingSlayerRng.random(options.size() - 1)) : AbstractDungeon.player;
        name = characterStrings.NAMES[0].replace("{0}", slayer.getLocalizedCharacterName().replace(characterStrings.NAMES[1], ""));
        authors = "Jack Renoson";

        try {
            //directly copied loadAnimation from AbstractCreature class
            if(slayer instanceof CustomPlayer){
                AbstractAnimation anim = ReflectionHacks.getPrivate(slayer, CustomPlayer.class, "animation");
                if(anim.type() == AbstractAnimation.Type.SPRITE){
                    animation = anim;
                } else {
                    this.atlas = ReflectionHacks.getPrivate(slayer, AbstractCreature.class, "atlas");
                    SkeletonJson json = new SkeletonJson(this.atlas);
                    json.setScale(Settings.renderScale);
                    this.skeleton = ReflectionHacks.getPrivate(slayer, AbstractCreature.class, "skeleton");
                    if(skeleton != null) {
                        this.skeleton.setColor(Color.WHITE);
                        this.stateData = ReflectionHacks.getPrivate(slayer, AbstractCreature.class, "stateData");
                        this.state = new AnimationState(this.stateData);
                    } else {
                        img = TexLoader.getTexture(Anniv7Mod.makeCharacterPath("ExampleNPC/image.png"));
                    }
                }
            } else {
                this.atlas = ReflectionHacks.getPrivate(slayer, AbstractCreature.class, "atlas");
                SkeletonJson json = new SkeletonJson(this.atlas);
                json.setScale(Settings.renderScale);
                this.skeleton = ReflectionHacks.getPrivate(slayer, AbstractCreature.class, "skeleton");
                this.skeleton.setColor(Color.WHITE);
                this.stateData = ReflectionHacks.getPrivate(slayer, AbstractCreature.class, "stateData");
                this.state = new AnimationState(this.stateData);
            }
            //////
        }
        catch (Exception e) {
            throw new RuntimeException("Error loading animation for character: " + slayer.chosenClass.name(), e);
        }

        generateCards();
        generateRelics();
        updateOffer();
    }

    public void setCutscenePortrait(String texture) {
    }

    public void renderCutscenePortrait(SpriteBatch sb) {
    }

    public void onInteract() {
        AbstractDungeon.topLevelEffectsQueue.add(new RestingSlayerCutscene(this));
    }

    /**
     Capitalizes string. Specifically changes the letter after each non-letter character to be a Capital, and the other letters to be non-capital.
     @param s: String to be capitalized
     @return : Capitalized String
     */
    public static String capitalize(String s){
        char[] chars = s.toLowerCase().toCharArray();
        boolean previousCharIsLetter = false;
        for (int i = 0; i < chars.length; i++) {
            if(Character.isLetter(chars[i])) {
                if(!previousCharIsLetter) {
                    chars[i] = Character.toUpperCase(chars[i]);
                }
                previousCharIsLetter = true;
            } else {
                previousCharIsLetter = false;
            }
        }
        return String.valueOf(chars);
    }

    /**
     Sets the list this.cards to be equal to a random card of each rarity of the slayers color.
     */
    private void generateCards() {
        boolean onlyStrikesAndDefends = true;
        CardGroup basics = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        CardGroup commons = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        CardGroup uncommons = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        CardGroup rares = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        slayer.initializeStarterDeck();

        for(AbstractCard c : slayer.masterDeck.group){
            if(!basics.contains(c) && c.rarity == AbstractCard.CardRarity.BASIC){
                basics.addToTop(c);
                if(!(c.hasTag(AbstractCard.CardTags.STARTER_STRIKE) || c.hasTag(AbstractCard.CardTags.STARTER_DEFEND))){
                    onlyStrikesAndDefends = false;
                    break;
                }
            }
        }
        if(!onlyStrikesAndDefends){ // If there is a non-Strike, non-Defend basic card, always offer that instead of a Strike or Defend
            basics.clear();
            for(AbstractCard c : slayer.masterDeck.group){
                if(!basics.contains(c) && c.rarity == AbstractCard.CardRarity.BASIC && !(c.hasTag(AbstractCard.CardTags.STARTER_STRIKE) || c.hasTag(AbstractCard.CardTags.STARTER_DEFEND))){
                    basics.addToTop(c);
                }
            }
        }


        ArrayList<AbstractCard> tmpCardList = new ArrayList<>();
        slayer.getCardPool(tmpCardList);
        for (AbstractCard c : tmpCardList) {
            switch (c.rarity) {
                case COMMON: commons.addToTop(c); break;
                case UNCOMMON: uncommons.addToTop(c); break;
                case RARE: rares.addToTop(c); break;
                default: break;
            }
        }

        cards = new ArrayList<>();
        if(!basics.isEmpty()) cards.add(basics.getRandomCard(restingSlayerRng));
        if(!commons.isEmpty()) cards.add(commons.getRandomCard(restingSlayerRng));
        if(!uncommons.isEmpty()) cards.add(uncommons.getRandomCard(restingSlayerRng));
        if(!rares.isEmpty()) cards.add(rares.getRandomCard(restingSlayerRng));
    }

    /**
     Sets the list this.relics to be equal to a random relic, specific to the slayer, of each tier.
     Also sets the list this.playerRelics to be equal to a list of nulls, with the same size as this.relics.
     */
    private void generateRelics() {
        relics = new ArrayList<>();
        ArrayList<AbstractRelic> tempRelics = new ArrayList<>();
        switch(slayer.getCardColor()){
            case RED: tempRelics = RelicLibrary.redList; break;
            case GREEN: tempRelics = RelicLibrary.greenList; break;
            case BLUE: tempRelics = RelicLibrary.blueList; break;
            case PURPLE: tempRelics = RelicLibrary.whiteList; break;
            default:
                ArrayList<AbstractRelic> finalTempRelics = tempRelics;
                BaseMod.getRelicsInCustomPool(slayer.getCardColor()).forEach((s, r) -> finalTempRelics.add(r)); break;
        }
        ArrayList<AbstractRelic> commons = new ArrayList<>();
        ArrayList<AbstractRelic> uncommons = new ArrayList<>();
        ArrayList<AbstractRelic> rares = new ArrayList<>();
        ArrayList<AbstractRelic> shops = new ArrayList<>();
        for (AbstractRelic r : tempRelics) {
            switch (r.tier) {
                case COMMON: commons.add(r); break;
                case UNCOMMON: uncommons.add(r); break;
                case RARE: rares.add(r); break;
                case SHOP: shops.add(r); break;
                default: break;
            }
        }
        if(!slayer.getStartingRelics().isEmpty()) {
            relics.add(RelicLibrary.getRelic(slayer.getStartingRelics().get(0)));
            playerRelics.add(null);
        }
        if(!commons.isEmpty()) {
            relics.add(commons.get(restingSlayerRng.random(0, commons.size() - 1)));
            playerRelics.add(null);
        }
        if(!uncommons.isEmpty()) {
            relics.add(uncommons.get(restingSlayerRng.random(0, uncommons.size() - 1)));
            playerRelics.add(null);
        }
        if(!rares.isEmpty()) {
            relics.add(rares.get(restingSlayerRng.random(0, rares.size() - 1)));
            playerRelics.add(null);
        }
        if(!shops.isEmpty()) {
            relics.add(shops.get(restingSlayerRng.random(0, shops.size() - 1)));
            playerRelics.add(null);
        }
    }

    /**
     Update the list playerRelics, which starts as a list of nulls, such that it contains a random relic the player has of each tier.
     If the player does not have any relics of a tier, it stays null.
     @return nr: amount of relics trades that can be offered = (additions to playerRelics) + (amount of relics already offered)
     */
    public int updatePlayerRelics(){
        int nr = 0;
        ArrayList<AbstractRelic> candidates;
        for(int i = 0; i<relics.size(); i++){
            if(playerRelics.get(i) == null) {
                candidates = new ArrayList<>();
                for (AbstractRelic relic : AbstractDungeon.player.relics) {
                    if (relic.tier == relics.get(i).tier) {
                        candidates.add(relic);
                    }
                }
                if (!candidates.isEmpty()){
                    playerRelics.set(i, candidates.get(restingSlayerRng.random(0, candidates.size() - 1)));
                    nr++;
                }
            }
        }
        return nr + offeredRelics.size();
    }

    /**
     Update the booleans hasRarity, to be true if the player has atleast one card of the rarity
     @return nr: amount of card trades that can be offered = number of true booleans
     */
    public int updateHasRarities(){
        hasBasic = false;
        hasCommon = false;
        hasUncommon = false;
        hasRare = false;
        int nr = 0;
        for(AbstractCard c : AbstractDungeon.player.masterDeck.group){
            switch(c.rarity){
                case BASIC: if(!hasBasic){
                    hasBasic = true;
                    nr++;
                } break;
                case COMMON: if(!hasCommon){
                    hasCommon = true;
                    nr++;
                } break;
                case UNCOMMON: if(!hasUncommon){
                    hasUncommon = true;
                    nr++;
                } break;
                case RARE: if(!hasRare){
                    hasRare = true;
                    nr++;
                } break;
                default: break;
            }
        }
        return nr;
    }

    /**
     Update the offers, by checking if any current offers are invalid, and then adding new offers.
     Offers are added when possible in order, first adding up to 2 card offers, then 1 relic offer, then randomly filled up to 5.
     Lastly, the offers are sorted.
     */
    public void updateOffer(){
        int nrCards = updateHasRarities();
        checkOffer();
        int nrRelics = updatePlayerRelics();

        while(offeredCards.size()<2 && !cards.isEmpty() && nrCards>offeredCards.size() && offeredCards.size()+offeredRelics.size()<5) {
            int c;
            do {
                c = restingSlayerRng.random(cards.size() - 1);
            } while (!hasRarity(cards.get(c)));
            offeredCards.add(cards.remove(c));
        }

        if(offeredRelics.isEmpty() && !relics.isEmpty() && nrRelics>0 && offeredCards.size()<5){
            int r;
            do {
                r = restingSlayerRng.random(relics.size()-1);
            } while (playerRelics.get(r)==null);
            offeredRelics.add(relics.remove(r));
            requestedRelics.add(playerRelics.remove(r));
        }

        while(offeredCards.size() + offeredRelics.size() < 5){
            if(nrCards>offeredCards.size()){
                if(nrRelics>offeredRelics.size()){
                    if(restingSlayerRng.randomBoolean()){
                        int c;
                        do {
                            c = restingSlayerRng.random(cards.size() - 1);
                        } while (!hasRarity(cards.get(c)));
                        offeredCards.add(cards.remove(c));
                    } else {
                        int r;
                        do {
                            r = restingSlayerRng.random(relics.size()-1);
                        } while (playerRelics.get(r)==null);
                        offeredRelics.add(relics.remove(r));
                        requestedRelics.add(playerRelics.remove(r));
                    }
                } else {
                    int c;
                    do {
                        c = restingSlayerRng.random(cards.size() - 1);
                    } while (!hasRarity(cards.get(c)));
                    offeredCards.add(cards.remove(c));
                }
            } else if (nrRelics>offeredRelics.size()){
                int r;
                do {
                    r = restingSlayerRng.random(relics.size()-1);
                } while (playerRelics.get(r)==null);
                offeredRelics.add(relics.remove(r));
                requestedRelics.add(playerRelics.remove(r));
            } else {break;}
        }

        sortOffer();
    }

    /**
     The order of the offers is sorted to be first relic trades, then card trades, with lower rarities first.
     This is done by sorting offeredRelics, requestedRelics and offeredCards.
     */
    private void sortOffer(){
        Collections.sort(offeredRelics, (r1, r2) -> {
            if(r1.tier == AbstractRelic.RelicTier.SHOP || (r1.tier == AbstractRelic.RelicTier.RARE && r2.tier != AbstractRelic.RelicTier.SHOP) || (r1.tier == AbstractRelic.RelicTier.UNCOMMON && r2.tier == AbstractRelic.RelicTier.COMMON) || r2.tier == AbstractRelic.RelicTier.STARTER){
                return 1;
            } return -1;
        });
        Collections.sort(requestedRelics, (r1, r2) -> {
            if(r1.tier == AbstractRelic.RelicTier.SHOP || (r1.tier == AbstractRelic.RelicTier.RARE && r2.tier != AbstractRelic.RelicTier.SHOP) || (r1.tier == AbstractRelic.RelicTier.UNCOMMON && r2.tier == AbstractRelic.RelicTier.COMMON) || r2.tier == AbstractRelic.RelicTier.STARTER){
                return 1;
            } return -1;
        });
        Collections.sort(offeredCards, (c1, c2) -> {
            if(c1.rarity == AbstractCard.CardRarity.RARE || (c1.rarity == AbstractCard.CardRarity.UNCOMMON && c2.rarity != AbstractCard.CardRarity.RARE) || c2.rarity == AbstractCard.CardRarity.BASIC){
                return 1;
            } return -1;
        });
    }

    /**
     Checks if the player has a card with the same rarity as c.
     @param c: Card to check rarity of
     @return hasRarity: boolean that is equal to true if the player has a card of rarity
     */
    private boolean hasRarity(AbstractCard c) {
        switch(c.rarity){
            case BASIC: return hasBasic;
            case COMMON: return hasCommon;
            case UNCOMMON: return hasUncommon;
            case RARE: return hasRare;
            default: return false;
        }
    }

    /**
     Checks if any current offers are invalid, and if so, removes any invalid offers, returning them to the list of potential offers for future use.
     This may be because the player might have removed all cards of a rarity or a relic since last interacting with this patron.
     */
    private void checkOffer(){
        ArrayList<Integer> relicsToRemove = new ArrayList<>();
        for(int i = offeredRelics.size()-1; i>=0; i--){
            if(!AbstractDungeon.player.relics.contains(requestedRelics.get(i))){
                relicsToRemove.add(i);
            }
        }
        for(int i : relicsToRemove){
            requestedRelics.remove(i);
            playerRelics.add(null);
            relics.add(offeredRelics.remove(i));
        }

        ArrayList<Integer> cardsToRemove = new ArrayList<>();
        for(int i = offeredCards.size()-1; i>=0; i--){
            if(!hasRarity(offeredCards.get(i))){
                cardsToRemove.add(i);
            }
        }
        for(int i : cardsToRemove){
            cards.add(offeredCards.remove(i));
        }
    }
}
