package spireCafe.interactables.patrons.packmistress;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.*;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import spireCafe.Anniv7Mod;
import spireCafe.abstracts.AbstractPatron;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PackmistressPatron extends AbstractPatron {
    public static final String ID = PackmistressPatron.class.getSimpleName();
    private static final CharacterStrings characterStrings = CardCrawlGame.languagePack.getCharacterString(Anniv7Mod.makeID(ID));

    private static Class<?> packmistressSkin;
    private static Class<?> abstractSkin;

    public PackmistressPatron(float animationX, float animationY) {
        super(animationX, animationY, 160.0f, 200.0f);
        this.name = characterStrings.NAMES[0];
        this.authors = "modargo";

        Object skin = null;
        String jsonPath = null;
        String atlasPath = null;
        if (Loader.isModLoaded("anniv5")) {
            try {
                packmistressSkin = Class.forName("thePackmaster.skins.instances.PackmistressSkin");
                abstractSkin = Class.forName("thePackmaster.skins.AbstractSkin");
                Constructor<?> constructor = packmistressSkin.getDeclaredConstructor();
                constructor.setAccessible(true);
                skin = constructor.newInstance();
                Method m1 = abstractSkin.getDeclaredMethod("getSkeletonJSONPath");
                Method m2 = abstractSkin.getDeclaredMethod("getSkeletonAtlasPath");
                jsonPath = (String)m1.invoke(skin);
                atlasPath = (String)m2.invoke(skin);
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
                throw new RuntimeException("Error retrieving classes from Packmaster", e);
            }

            this.atlas = new TextureAtlas(Gdx.files.internal(atlasPath));
            SkeletonJson json = new SkeletonJson(this.atlas);
            json.setScale(Settings.renderScale);
            SkeletonData skeletonData = json.readSkeletonData(Gdx.files.internal(jsonPath));
            this.skeleton = new Skeleton(skeletonData);
            this.skeleton.setColor(Color.WHITE);
            this.stateData = new AnimationStateData(skeletonData);
            this.state = new AnimationState(this.stateData);
            AnimationState.TrackEntry e = this.state.setAnimation(0, "Idle", true);
            e.setTime(e.getEndTime() * MathUtils.random());
        }
    }

    @Override
    public boolean canSpawn() {
        return Loader.isModLoaded("anniv5");
    }

    public void setCutscenePortrait(String texture) {
    }

    public void renderCutscenePortrait(SpriteBatch sb) {
    }

    public void onInteract() {
        AbstractDungeon.topLevelEffectsQueue.add(new PackmistressCutscene(this));
    }
}
