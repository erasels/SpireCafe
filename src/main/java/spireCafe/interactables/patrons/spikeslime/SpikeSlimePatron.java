package spireCafe.interactables.patrons.spikeslime;

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

/*
    iry - SpikeSlimePatron:
    Help choking(?) slime for healing/meat on the bone at the cost of slimes for your next 6/99 combats.
 */
public class SpikeSlimePatron extends AbstractPatron {
    public static final String ID = SpikeSlimePatron.class.getSimpleName();
    private static final CharacterStrings characterStrings = CardCrawlGame.languagePack.getCharacterString(Anniv7Mod.makeID(ID));

    public static final String assetID = "SpikeSlime";
    public int healAmount;

    public SpikeSlimePatron(float animationX, float animationY) {
        super(animationX, animationY, 160.0f, 200.0f);
        this.name = characterStrings.NAMES[0];
        this.authors = "iry";

        //set heal amount on initialize, so it is independent of max hp changes in the cafe
        this.healAmount = AbstractDungeon.player.maxHealth/4;

        //directly copied loadAnimation from AbstractCreature class
        this.atlas = new TextureAtlas(Gdx.files.internal("images/monsters/theBottom/slimeAltM/skeleton.atlas"));
        SkeletonJson json = new SkeletonJson(this.atlas);
        json.setScale(Settings.renderScale);
        SkeletonData skeletonData = json.readSkeletonData(Gdx.files.internal("images/monsters/theBottom/slimeAltM/skeleton.json"));
        this.skeleton = new Skeleton(skeletonData);
        this.skeleton.setColor(Color.WHITE);
        this.stateData = new AnimationStateData(skeletonData);
        this.state = new AnimationState(this.stateData);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "idle", true);
        e.setTime(e.getEndTime() * MathUtils.random());
        //////

        this.cutscenePortrait = new TextureRegion(TexLoader.getTexture(Anniv7Mod.makeCharacterPath("SpikeSlime/Portrait3.png")));
    }

    public void setCutscenePortrait(String texture) {
        String resourcePath = String.format("SpikeSlime/%s.png", texture);
        this.cutscenePortrait = new TextureRegion(TexLoader.getTexture(Anniv7Mod.makeCharacterPath(resourcePath)));
    }

    public void renderCutscenePortrait(SpriteBatch sb) {
        simpleRenderCutscenePortrait(sb, 1600.0F,175.0F, 0.0F, 0.0F, 0.0F);
    }

    public void onInteract() {
        AbstractDungeon.topLevelEffectsQueue.add(new SpikeSlimeCutscene(this));
    }
}
