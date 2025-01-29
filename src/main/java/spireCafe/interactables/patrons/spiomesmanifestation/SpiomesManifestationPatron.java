package spireCafe.interactables.patrons.spiomesmanifestation;

import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import spireCafe.Anniv7Mod;
import spireCafe.abstracts.AbstractPatron;
import spireCafe.util.TexLoader;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static spireCafe.Anniv7Mod.*;
import static spireCafe.interactables.patrons.missingno.MissingnoUtil.initGlitchShader;

public class SpiomesManifestationPatron extends AbstractPatron {
    public static final String ID = SpiomesManifestationPatron.class.getSimpleName();
    private static final CharacterStrings characterStrings = CardCrawlGame.languagePack.getCharacterString(Anniv7Mod.makeID(ID));
    private static final UIStrings authorsString = CardCrawlGame.languagePack.getUIString(makeID("Authors"));


    private static ShaderProgram glitchShader = null;
    private float wavy_y;
    private float wavyHelper;
    private final float WAVY_DISTANCE = 2.0F * Settings.scale;

    private static Class<?> anniv6;
    private static Class<?> abstractZone;
    private static Class<?> betterMapGenerator;
    private static Class<?> anniv6Wiz;
    private static Method getCurZone;
    public List<Object> availableBiomes;
    public boolean hasChosenBiome = false;

    public static final String assetID = "SpiomesManifestation";
    public static String queuedBiomeID;

    static {
        if (Loader.isModLoaded("anniv6")) {
            try {
                abstractZone = Class.forName("spireMapOverhaul.abstracts.AbstractZone");
                anniv6 = Class.forName("spireMapOverhaul.SpireAnniversary6Mod");
                betterMapGenerator = Class.forName("spireMapOverhaul.BetterMapGenerator");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                throw new RuntimeException("Error retrieving classes from Spiomes", e);
            }
        }
    }

    public SpiomesManifestationPatron(float animationX, float animationY) {
        super(animationX, animationY, 160.0f, 200.0f);
        this.name = characterStrings.NAMES[0];
        this.authors = "Mindbomber";
        this.img = TexLoader.getTexture(Anniv7Mod.makeCharacterPath("SpiomesManifestation/SpiomesHerald.png"));
        this.cutscenePortrait = new TextureRegion(TexLoader.getTexture(Anniv7Mod.makeCharacterPath("SpiomesManifestation/SpiomesHerald.png")));


        this.availableBiomes = rollBiomes();
    }

    public List<Object> rollBiomes(){
        ArrayList<Object> allZones = new ArrayList<>(ReflectionHacks.getPrivateStatic(anniv6, "unfilteredAllZones"));
        ArrayList<Object> activeZones = new ArrayList<>(ReflectionHacks.getPrivateStatic(betterMapGenerator, "activeZones"));
        HashSet<String> currentRunAllZoneNames = new HashSet<>(ReflectionHacks.getPrivateStatic(anniv6, "currentRunAllZones"));
        HashSet<String> currentRunSeenZoneNames = new HashSet<>(ReflectionHacks.getPrivateStatic(anniv6, "currentRunSeenZones"));
        ArrayList<Object> possibleBiomes = new ArrayList<>();
        boolean currentRunNoRepeatZones = ReflectionHacks.getPrivateStatic(anniv6, "currentRunNoRepeatZones");
        for (Object biome : allZones) {
            if (currentRunAllZoneNames.contains(getBiomeId(biome))
                    && !activeZones.contains(biome)
                    && !(currentRunNoRepeatZones && currentRunSeenZoneNames.contains(getBiomeId(biome)))
                    && getBiomeCanSpawn(biome)) {
                possibleBiomes.add(biome);
            }
        }
        Collections.shuffle(possibleBiomes, new java.util.Random(AbstractDungeon.miscRng.randomLong()));
        return possibleBiomes.subList(0, 3);
    }

    public static void addBiomeToNextMap(Object obj){
        ArrayList<Object> queuedZone = ReflectionHacks.getPrivateStatic(betterMapGenerator, "queueCommandZones");
        Method copy = ReflectionHacks.getCachedMethod(obj.getClass(), "copy");
        queuedZone.clear();
        try {
            queuedZone.add(copy.invoke(obj));
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        queuedBiomeID = getBiomeId(obj);
    }

    public String getBiomeName(Object biome){
        try {
            return (String)ReflectionHacks.getCachedField(abstractZone, "name").get(biome);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean getBiomeCanSpawn(Object biome){
        try {
            Method canSpawn = ReflectionHacks.getCachedMethod(biome.getClass(), "canSpawn");
            if(canSpawn!=null){
                return (Boolean) canSpawn.invoke(biome);
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            return false;
        }
        return false;
    }

    public static String getBiomeId(Object biome){
        try {
            return (String)ReflectionHacks.getCachedField(abstractZone, "id").get(biome);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object getBiomeById(String id) {
        if (Loader.isModLoaded("anniv6")) {
            ArrayList<Object> allZones = new ArrayList<>(ReflectionHacks.getPrivateStatic(anniv6, "unfilteredAllZones"));
            for (Object zone : allZones){
                if(getBiomeId(zone).equals(id)){
                    return zone;
                }
            }
        }
        return null;
    }

    public static boolean currentNodeInZone() {

        try {
            anniv6Wiz = Class.forName("spireMapOverhaul.util.Wiz");
            getCurZone = anniv6Wiz.getDeclaredMethod("getCurZone");
            return getCurZone.invoke(null) != null;
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean canSpawn() {
        return Loader.isModLoaded("anniv6");
    }


    public void renderCutscenePortrait(SpriteBatch sb) {
        simpleRenderCutscenePortrait(sb, 1560.0F,0.0F, 0.0F, 0.0F, 0.0F);
    }

    @Override
    public void renderAnimation(SpriteBatch sb) {
        if(shouldShowSpeechBubble) {
            this.speechBubble.render(sb);
        }
        sb.setColor(Color.WHITE);
        if(!Anniv7Mod.getDisableShadersConfig()) {
            glitchShader = initGlitchShader(glitchShader);
            sb.setShader(glitchShader);
            glitchShader.setUniformf("u_time", (time % 10) + 150);
            glitchShader.setUniformf("u_shake_power", 0.010f);
            glitchShader.setUniformf("u_shake_rate", shake_rate.get());
            glitchShader.setUniformf("u_shake_speed", shake_speed.get());
            glitchShader.setUniformf("u_shake_block_size", shake_block_size.get());
            glitchShader.setUniformf("u_shake_color_rate", shake_color_rate.get());
        }
        sb.draw(this.img, this.animationX - (float) this.img.getWidth() * Settings.scale / 2.0F, this.animationY + this.wavy_y, (float) this.img.getWidth() * Settings.scale, (float) this.img.getHeight() * Settings.scale, 0, 0, this.img.getWidth(), this.img.getHeight(), this.flipHorizontal, this.flipVertical);
        if(!Anniv7Mod.getDisableShadersConfig()) {
            sb.setShader(null);
        }
        this.hitbox.render(sb);

        if(showTooltip && !AbstractDungeon.isScreenUp){
            String tooltipBody = authorsString.TEXT[0] + this.authors;
            float boxWidth = 320.0F * Settings.scale;

            float tooltipX = Settings.WIDTH - boxWidth - 20.0f * Settings.scale;
            float tooltipY = 0.85f * Settings.HEIGHT - 20.0f * Settings.scale;

            TipHelper.renderGenericTip(tooltipX, tooltipY, name, tooltipBody);
        }
    }
    @Override
    public void update() {
        super.update();
        this.wavyHelper += Gdx.graphics.getDeltaTime() * 2.0F;
        this.wavy_y = MathUtils.sin(this.wavyHelper) * WAVY_DISTANCE;
    }

    public void onInteract() {
        AbstractDungeon.topLevelEffectsQueue.add(new SpiomesManifestationCutscene(this));
    }
}