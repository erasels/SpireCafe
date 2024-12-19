package spireCafe.interactables.patrons.redlouse;

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

public class RedLousePatron extends AbstractPatron {
    public static final String ID = RedLousePatron.class.getSimpleName();
    private static final CharacterStrings characterStrings = CardCrawlGame.languagePack.getCharacterString(Anniv7Mod.makeID(ID));

    public RedLousePatron(float animationX, float animationY) {
        super(animationX, animationY, 160.0f, 200.0f);
        this.name = characterStrings.NAMES[0];
        this.authors = "iry";

        //directly copied loadAnimation from AbstractCreature class
        this.atlas = new TextureAtlas(Gdx.files.internal("images/monsters/theBottom/louseRed/skeleton.atlas"));
        SkeletonJson json = new SkeletonJson(this.atlas);
        json.setScale(Settings.renderScale);
        SkeletonData skeletonData = json.readSkeletonData(Gdx.files.internal("images/monsters/theBottom/louseRed/skeleton.json"));
        this.skeleton = new Skeleton(skeletonData);
        this.skeleton.setColor(Color.WHITE);
        this.stateData = new AnimationStateData(skeletonData);
        this.state = new AnimationState(this.stateData);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "idle", true);
        e.setTime(e.getEndTime() * MathUtils.random());
        //////

        this.cutscenePortrait = new TextureRegion(TexLoader.getTexture(Anniv7Mod.makeCharacterPath("RedLouse/Portrait.png")));
    }

    public void setCutscenePortrait(String texture) {
        String resourcePath = String.format("RedLouse/%s.png", texture);
        this.cutscenePortrait = new TextureRegion(TexLoader.getTexture(Anniv7Mod.makeCharacterPath(resourcePath)));
    }

    public void curlUp(){
        this.state.setAnimation(0, "transitiontoclosed", false);
        this.state.addAnimation(0, "idle closed", true, 0.0F);
    }

    public void renderCutscenePortrait(SpriteBatch sb) {
        simpleRenderCutscenePortrait(sb, 1600.0F,175.0F, 0.0F, 0.0F, 0.0F);
    }

    public void onInteract() {
        AbstractDungeon.topLevelEffectsQueue.add(new RedLouseCutscene(this));
    }
}
