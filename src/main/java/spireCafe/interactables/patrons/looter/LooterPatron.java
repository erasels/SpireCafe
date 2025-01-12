package spireCafe.interactables.patrons.looter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.*;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import spireCafe.Anniv7Mod;
import spireCafe.abstracts.AbstractPatron;
import spireCafe.util.TexLoader;

public class LooterPatron extends AbstractPatron {
    public static final String ID = LooterPatron.class.getSimpleName();
    private static final CharacterStrings characterStrings = CardCrawlGame.languagePack.getCharacterString(Anniv7Mod.makeID(ID));

    public static final int MIN_GOLD = 5;
    public static final int MAX_GOLD = 50;
    public AbstractPatron stealTarget;
    public int rewardGold;

    public LooterPatron(float animationX, float animationY) {
        super(animationX, animationY, 160.0f, 200.0f);
        this.name = characterStrings.NAMES[0];
        this.authors = "iry";

        //directly copied loadAnimation from AbstractCreature class
        this.atlas = new TextureAtlas(Gdx.files.internal("images/monsters/theBottom/looter/skeleton.atlas"));
        SkeletonJson json = new SkeletonJson(this.atlas);
        json.setScale(Settings.renderScale);
        SkeletonData skeletonData = json.readSkeletonData(Gdx.files.internal("images/monsters/theBottom/looter/skeleton.json"));
        this.skeleton = new Skeleton(skeletonData);
        this.skeleton.setColor(Color.WHITE);
        this.stateData = new AnimationStateData(skeletonData);
        this.state = new AnimationState(this.stateData);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "idle", true);
        e.setTime(e.getEndTime() * MathUtils.random());
        //////

        this.rewardGold = AbstractDungeon.eventRng.random(MIN_GOLD,MAX_GOLD);
        setCutscenePortrait("Portrait");
    }

    public void setCutscenePortrait(String texture) {
        String resourcePath = String.format("Looter/%s.png", texture);
        this.cutscenePortrait = new TextureRegion(TexLoader.getTexture(Anniv7Mod.makeCharacterPath(resourcePath)));
    }

    public void renderCutscenePortrait(SpriteBatch sb) {
        simpleRenderCutscenePortrait(sb, 1500.0F,-350.0F, 0.0F, 0.0F, 0.0F);
    }

    public void onInteract() {
        AbstractDungeon.topLevelEffectsQueue.add(new LooterCutscene(this));
    }
}
