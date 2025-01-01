package spireCafe.interactables.merchants.packmaster;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.*;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import spireCafe.Anniv7Mod;
import spireCafe.abstracts.AbstractMerchant;
import spireCafe.vfx.TopLevelSpeechEffect;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PackmasterMerchant extends AbstractMerchant {
    public static final String ID = PackmasterMerchant.class.getSimpleName();
    public static final CharacterStrings packmasterStrings = CardCrawlGame.languagePack.getCharacterString(Anniv7Mod.makeID(ID));
    private static final Texture BG_TEXTURE = ImageMaster.loadImage("images/npcs/rug/eng.png");

    private static Class<?> packmasterSkin;
    private static Class<?> abstractSkin;

    public PackmasterMerchant(float animationX, float animationY) {
        super(animationX, animationY, 360.0f, 235.0f);
        this.name = packmasterStrings.NAMES[0];
        this.authors = "modargo";
        this.background = new TextureRegion(BG_TEXTURE);

        Object skin = null;
        String jsonPath = null;
        String atlasPath = null;
        if (Loader.isModLoaded("anniv5")) {
            try {
                packmasterSkin = Class.forName("thePackmaster.skins.instances.PackmasterSkin");
                abstractSkin = Class.forName("thePackmaster.skins.AbstractSkin");
                Constructor<?> constructor = packmasterSkin.getDeclaredConstructor();
                constructor.setAccessible(true);
                skin = constructor.newInstance();
                Method m1 = abstractSkin.getDeclaredMethod("getSkeletonJSONPath");
                Method m2 = abstractSkin.getDeclaredMethod("getSkeletonAtlasPath");
                jsonPath = (String)m1.invoke(skin);
                atlasPath = (String)m2.invoke(skin);
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                     InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
                throw new RuntimeException("Error retrieving classes from Packmaster", e);
            }
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

    @Override
    public boolean canSpawn() {
        return Loader.isModLoaded("anniv5");
    }

    @Override
    public void onInteract() {
        super.onInteract();
    }

    @Override
    public void rollShop() {
    }

    @Override
    public void onCloseShop() {
    }
}
