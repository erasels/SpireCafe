package spireCafe.interactables.bartenders.snecko;

import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import spireCafe.Anniv7Mod;
import spireCafe.abstracts.AbstractBartender;
import spireCafe.cardmods.CostMod;
import spireCafe.util.TexLoader;
import spireCafe.util.Wiz;
import spireCafe.util.cutsceneStrings.CutsceneStrings;
import spireCafe.util.cutsceneStrings.LocalizedCutsceneStrings;

import java.util.ArrayList;

public class SneckoBartender extends AbstractBartender {
    public static final String ID = SneckoBartender.class.getSimpleName();
    private static final CharacterStrings characterStrings = CardCrawlGame.languagePack.getCharacterString(Anniv7Mod.makeID(ID));
    private static final CutsceneStrings cutsceneStrings = LocalizedCutsceneStrings.getCutsceneStrings(Anniv7Mod.makeID(ID + "Cutscene"));

    private static final float MIN_SALAD_HEAL = 0.5f;
    private static final float MAX_SALAD_HEAL = 1f;
    private static final int MIN_GUMMY_COST = 50;
    private static final int MAX_GUMMY_COST = 100;

    public boolean firstInteractionWasHeal;
    
    public SneckoBartender(float animationX, float animationY) {
        super(animationX, animationY+50.0F * Settings.yScale, 180.0f, 250.0f);
        name = characterStrings.NAMES[0];
        this.authors = "Jack Renoson";
        //directly copied loadAnimation from AbstractCreature class
        this.atlas = new TextureAtlas(Gdx.files.internal("images/monsters/theCity/reptile/skeleton.atlas"));
        SkeletonJson json = new SkeletonJson(this.atlas);
        json.setScale(Settings.renderScale);
        SkeletonData skeletonData = json.readSkeletonData(Gdx.files.internal("images/monsters/theCity/reptile/skeleton.json"));
        this.skeleton = new Skeleton(skeletonData);
        this.skeleton.setColor(Color.WHITE);
        this.stateData = new AnimationStateData(skeletonData);
        this.state = new AnimationState(this.stateData);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "Idle", true);
        e.setTime(e.getEndTime() * MathUtils.random());
        //////
        this.cutscenePortrait = new TextureRegion(TexLoader.getTexture(Anniv7Mod.makeBartenderPath("snecko/Portrait.png")));
    }

    @Override
    public String getHealOptionDescription() {
        if(getHealOptionCondition()) {
            return String.format(cutsceneStrings.OPTIONS[1], (int) (Wiz.p().maxHealth * MIN_SALAD_HEAL), (int) (Wiz.p().maxHealth * MAX_SALAD_HEAL));
        } else {
            return cutsceneStrings.OPTIONS[2];
        }
    }

    @Override
    public boolean getHealOptionCondition() {
        for(AbstractCard c : AbstractDungeon.player.masterDeck.group){
            if(c.cost < 3 && c.cost >= 0 && !CardModifierManager.hasModifier(c, CostMod.ID)){
                return true;
            }
        }
        return false;
    }

    @Override
    public int getHealAmount() {
        // Heal randomly between 50% and 100% of max HP
        return (int) (Wiz.p().maxHealth * AbstractDungeon.miscRng.random(MIN_SALAD_HEAL, MAX_SALAD_HEAL));
    }

    @Override
    public void applyHealAction() {
        CardCrawlGame.sound.play("POWER_CONFUSION");
        Wiz.p().heal(getHealAmount());
        randomizeCardCost(true);
        inHealAction = false; //Important to set this to false after the logic has concluded
    }

    /**
     * Randomizes the value of a random card in the players deck.
     * If increase, a random card that costs between 0 and 2 is changed to cost a random higher value (max 3).
     * If not increase, a random card that costs more than or equal to 2 is changed to cost a random lower value (min 1).
     * @param increase: Whether the cards cost should increase, or if false should decrease.
     */
    private void randomizeCardCost(boolean increase){
        ArrayList<AbstractCard> candidates = new ArrayList<>();
        for(AbstractCard card : AbstractDungeon.player.masterDeck.group){
            if((increase?(card.cost < 3 && card.cost >= 0):card.cost > 1) && !CardModifierManager.hasModifier(card, CostMod.ID)){
                candidates.add(card);
            }
        }
        AbstractCard chosenCard = candidates.get(AbstractDungeon.miscRng.random(candidates.size()-1));
        int modifier = increase?AbstractDungeon.miscRng.random(1, 3-chosenCard.cost):-AbstractDungeon.miscRng.random(1, chosenCard.cost-1);
        CardModifierManager.addModifier(chosenCard, new CostMod(modifier));
    }

    @Override
    public String getSecondOptionDescription() {
        if(getSecondOptionCondition()) {
            return String.format(cutsceneStrings.OPTIONS[3], MIN_GUMMY_COST, MAX_GUMMY_COST);
        } else {
            return String.format(cutsceneStrings.OPTIONS[4], MAX_GUMMY_COST);
        }
    }

    @Override
    public boolean getSecondOptionCondition() {
        if(Wiz.p().gold >= MAX_GUMMY_COST) {
            for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
                if (c.cost > 1  && !CardModifierManager.hasModifier(c, CostMod.ID)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void applySecondOptionAction() {
        CardCrawlGame.sound.play("MONSTER_SNECKO_GLARE");
        randomizeCardCost(false);
        Wiz.p().loseGold(AbstractDungeon.miscRng.random(MIN_GUMMY_COST, MAX_GUMMY_COST));
        inSecondAction = false; //Important to set this to false after the logic has concluded
    }

    @Override
    public String getNoThanksDescription() {
        return cutsceneStrings.OPTIONS[0];
    }

    @Override
    public String getLabelText() {
        return characterStrings.TEXT[0];
    }

    @Override
    public void onInteract() {
        AbstractDungeon.topLevelEffectsQueue.add(new SneckoBartenderCutscene(this));
    }
}
