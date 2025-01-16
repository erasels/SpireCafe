package spireCafe.interactables.patrons.restingslayer;

import basemod.BaseMod;
import basemod.ReflectionHacks;
import basemod.abstracts.CustomPlayer;
import basemod.animations.AbstractAnimation;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import spireCafe.Anniv7Mod;
import spireCafe.abstracts.AbstractPatron;
import spireCafe.util.TexLoader;

import java.awt.image.AreaAveragingScaleFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.eventRng;
import static org.apache.commons.lang3.math.NumberUtils.max;
import static org.apache.commons.lang3.math.NumberUtils.min;

public class RestingSlayerPatron extends AbstractPatron {
    public static final String ID = RestingSlayerPatron.class.getSimpleName();
    private static final CharacterStrings characterStrings = CardCrawlGame.languagePack.getCharacterString(Anniv7Mod.makeID(ID));
    public final AbstractPlayer slayer;
    private ArrayList<AbstractCard> cards = new ArrayList<>();
    private ArrayList<AbstractRelic> relics = new ArrayList<>();
    private ArrayList<AbstractRelic> playerRelics = new ArrayList<>();
    public ArrayList<AbstractCard> offeredCards = new ArrayList<>();
    public ArrayList<AbstractRelic> offeredRelics = new ArrayList<>();
    public ArrayList<AbstractRelic> requestedRelics = new ArrayList<>();
    public boolean hasBasic;
    public boolean hasCommon;
    public boolean hasUncommon;
    public boolean hasRare;



    public RestingSlayerPatron(float animationX, float animationY) {
        super(animationX, animationY, 160.0f, 200.0f);
        ArrayList<AbstractPlayer> options = CardCrawlGame.characterManager.getAllCharacters().stream()
                .filter(p -> p.chosenClass != AbstractDungeon.player.chosenClass
                        && !p.chosenClass.name().equals("THE_PACKMASTER")
                        && !p.chosenClass.name().equals("THE_SISTERS")
                        && !p.chosenClass.name().equals("Librarian")
                        && !p.chosenClass.name().equals("THE_RAINBOW "))
                .collect(Collectors.toCollection(ArrayList::new));
        slayer = !options.isEmpty() ? options.get(AbstractDungeon.eventRng.random(options.size() - 1)) : AbstractDungeon.player;
        name = characterStrings.NAMES[0].replace("{0}", slayer.getLocalizedCharacterName().replace(characterStrings.NAMES[1], ""));
        authors = "Jack Renoson";

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

    private void generateCards() {
        CardGroup basics = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        CardGroup commons = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        CardGroup uncommons = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        CardGroup rares = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        slayer.initializeStarterDeck();

        for(AbstractCard c : slayer.masterDeck.group){
            if(!basics.contains(c) && c.rarity == AbstractCard.CardRarity.BASIC){
                basics.addToTop(c);
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
        if(!basics.isEmpty()) cards.add(basics.getRandomCard(eventRng));
        if(!commons.isEmpty()) cards.add(commons.getRandomCard(eventRng));
        if(!uncommons.isEmpty()) cards.add(uncommons.getRandomCard(eventRng));
        if(!rares.isEmpty()) cards.add(rares.getRandomCard(eventRng));
    }

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
            relics.add(commons.get(eventRng.random(0, commons.size() - 1)));
            playerRelics.add(null);
        }
        if(!uncommons.isEmpty()) {
            relics.add(uncommons.get(eventRng.random(0, uncommons.size() - 1)));
            playerRelics.add(null);
        }
        if(!rares.isEmpty()) {
            relics.add(rares.get(eventRng.random(0, rares.size() - 1)));
            playerRelics.add(null);
        }
        if(!shops.isEmpty()) {
            relics.add(shops.get(eventRng.random(0, shops.size() - 1)));
            playerRelics.add(null);
        }
    }

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
                    playerRelics.set(i, candidates.get(eventRng.random(0, candidates.size() - 1)));
                    nr++;
                }
            }
        }
        return nr + offeredRelics.size();
    }

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

    public void updateOffer(){
        int nrCards = updateHasRarities();
        checkOffer();
        int nrRelics = updatePlayerRelics();

        while(offeredCards.size()<2 && !cards.isEmpty() && nrCards>offeredCards.size() && offeredCards.size()+offeredRelics.size()<5) {
            int c;
            do {
                c = eventRng.random(cards.size() - 1);
            } while (!hasRarity(cards.get(c)));
            offeredCards.add(cards.remove(c));
        }

        if(offeredRelics.isEmpty() && !relics.isEmpty() && nrRelics>0 && offeredCards.size()<5){
            int r;
            do {
                r = eventRng.random(relics.size()-1);
            } while (playerRelics.get(r)==null);
            offeredRelics.add(relics.remove(r));
            requestedRelics.add(playerRelics.remove(r));
        }

        while(offeredCards.size() + offeredRelics.size() < 5){
            if(nrCards>offeredCards.size()){
                if(nrRelics>offeredRelics.size()){
                    if(0==eventRng.random(0, 1)){
                        int c;
                        do {
                            c = eventRng.random(cards.size() - 1);
                        } while (!hasRarity(cards.get(c)));
                        offeredCards.add(cards.remove(c));
                    } else {
                        int r;
                        do {
                            r = eventRng.random(relics.size()-1);
                        } while (playerRelics.get(r)==null);
                        offeredRelics.add(relics.remove(r));
                        requestedRelics.add(playerRelics.remove(r));
                    }
                } else {
                    int c;
                    do {
                        c = eventRng.random(cards.size() - 1);
                    } while (!hasRarity(cards.get(c)));
                    offeredCards.add(cards.remove(c));
                }
            } else if (nrRelics>offeredRelics.size()){
                int r;
                do {
                    r = eventRng.random(relics.size()-1);
                } while (playerRelics.get(r)==null);
                offeredRelics.add(relics.remove(r));
                requestedRelics.add(playerRelics.remove(r));
            } else {break;}
        }

        sortOffer();
    }

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

    private boolean hasRarity(AbstractCard c) {
        switch(c.rarity){
            case BASIC: return hasBasic;
            case COMMON: return hasCommon;
            case UNCOMMON: return hasUncommon;
            case RARE: return hasRare;
            default: return false;
        }
    }

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
