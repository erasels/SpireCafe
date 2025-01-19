package spireCafe.interactables.patrons.thetransient;

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
import spireCafe.interactables.patrons.thetransient.TheTransientCutscene;
import spireCafe.util.TexLoader;

public class TheTransientPatron extends AbstractPatron {
    public static final String ID = TheTransientPatron.class.getSimpleName();
    private static final CharacterStrings characterStrings = CardCrawlGame.languagePack.getCharacterString(Anniv7Mod.makeID(ID));
    public float cutsceneAlpha = 1.0f;
    public float animationAlpha = 1.0f;
    public float targetAnimationAlpha = 1.0f;

    public TheTransientPatron(float animationX, float animationY) {
        super(animationX, animationY, 160.0f, 200.0f);
        this.name = characterStrings.NAMES[0];
        this.authors = "iry";

        //directly copied loadAnimation from AbstractCreature class
        this.atlas = new TextureAtlas(Gdx.files.internal("images/monsters/theForest/transient/skeleton.atlas"));
        SkeletonJson json = new SkeletonJson(this.atlas);
        json.setScale(Settings.renderScale * 0.60f);
        SkeletonData skeletonData = json.readSkeletonData(Gdx.files.internal("images/monsters/theForest/transient/skeleton.json"));
        this.skeleton = new Skeleton(skeletonData);
        this.skeleton.setColor(Color.WHITE);
        this.stateData = new AnimationStateData(skeletonData);
        this.state = new AnimationState(this.stateData);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "Idle", true);
        e.setTime(e.getEndTime() * MathUtils.random());
        //////

        this.cutscenePortrait = new TextureRegion(TexLoader.getTexture(Anniv7Mod.makeCharacterPath("TheTransient/Portrait.png")));
    }

    public void setCutscenePortrait(String texture) {
        String resourcePath = String.format("TheTransient/%s.png", texture);
        this.cutscenePortrait = new TextureRegion(TexLoader.getTexture(Anniv7Mod.makeCharacterPath(resourcePath)));
    }

    @Override
    public void renderAnimation(SpriteBatch sb) {
        super.renderAnimation(sb);
    }

    public void postCutsceneFade(){
        targetAnimationAlpha = cutsceneAlpha;
    }

    @Override
    public void update() {
        super.update();
        animationAlpha = MathUtils.lerp(animationAlpha, targetAnimationAlpha, Gdx.graphics.getDeltaTime());
        this.skeleton.setColor(new Color(1,1,1,animationAlpha));
    }

    public void renderCutscenePortrait(SpriteBatch sb) {
        Color c = sb.getColor();
        sb.setColor(new Color(1, 1, 1, cutsceneAlpha));
        simpleRenderCutscenePortrait(sb, 1500.0F,-150.0F, 0.0F, 0.0F, 0.0F);
        sb.setColor(c);
    }

    public void onInteract() {
        AbstractDungeon.topLevelEffectsQueue.add(new TheTransientCutscene(this));
    }
}
