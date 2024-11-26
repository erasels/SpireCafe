package spireCafe;

import basemod.AutoAdd;
import basemod.BaseMod;
import basemod.ModPanel;
import basemod.helpers.RelicType;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.Exordium;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import spireCafe.abstracts.AbstractSCRelic;
import spireCafe.cardvars.SecondDamage;
import spireCafe.cardvars.SecondMagicNumber;
import spireCafe.interactables.TestEvent;
import spireCafe.util.TexLoader;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Consumer;

@SuppressWarnings({"unused"})
@SpireInitializer
public class Anniv7Mod implements
        EditCardsSubscriber,
        EditRelicsSubscriber,
        EditStringsSubscriber,
        EditKeywordsSubscriber,
        PostInitializeSubscriber,
        AddAudioSubscriber {

    public static final Logger logger = LogManager.getLogger("SpireCafe");

    public static Settings.GameLanguage[] SupportedLanguages = {
            Settings.GameLanguage.ENG
    };

    public static Anniv7Mod thismod;
    public static SpireConfig modConfig = null;

    public static final String modID = "anniv7";

    private static final String ATTACK_S_ART = modID + "Resources/images/512/attack.png";
    private static final String SKILL_S_ART = modID + "Resources/images/512/skill.png";
    private static final String POWER_S_ART = modID + "Resources/images/512/power.png";
    private static final String CARD_ENERGY_S = modID + "Resources/images/512/energy.png";
    private static final String TEXT_ENERGY = modID + "Resources/images/512/text_energy.png";
    private static final String ATTACK_L_ART = modID + "Resources/images/1024/attack.png";
    private static final String SKILL_L_ART = modID + "Resources/images/1024/skill.png";
    private static final String POWER_L_ART = modID + "Resources/images/1024/power.png";

    public static boolean initializedStrings = false;

    public static final Map<String, Keyword> keywords = new HashMap<>();

    public static List<?> unfilteredAllZones = new ArrayList<>();
    private static Map<String, ?> zonePackages = new HashMap<>();
    public static Map<String, Set<String>> zoneEvents = new HashMap<>();


    public static String makeID(String idText) {
        return modID + ":" + idText;
    }


    public Anniv7Mod() {
        BaseMod.subscribe(this);
    }

    public static String makePath(String resourcePath) {
        return modID + "Resources/" + resourcePath;
    }

    public static String makeImagePath(String resourcePath) {
        return modID + "Resources/images/" + resourcePath;
    }

    public static String makeCharacterPath(String resourcePath) {
        return modID + "Resources/images/characters/" + resourcePath;
    }

    public static String makeUIPath(String resourcePath) {
        return modID + "Resources/images/ui/" + resourcePath;
    }

    public static String makeRelicPath(String resourcePath) {
        return modID + "Resources/images/relics/" + resourcePath;
    }

    public static String makeMonsterPath(String resourcePath) {
        return modID + "Resources/images/monsters/" + resourcePath;
    }

    public static String makePowerPath(String resourcePath) {
        return modID + "Resources/images/powers/" + resourcePath;
    }

    public static String makeCardPath(String resourcePath) {
        return modID + "Resources/images/cards/" + resourcePath;
    }

    public static String makeShaderPath(String resourcePath) {
        return modID + "Resources/shaders/" + resourcePath;
    }

    public static String makeOrbPath(String resourcePath) {
        return modID + "Resources/images/orbs/" + resourcePath;
    }

    public static String makeEventPath(String resourcePath) {
        return modID + "Resources/images/events/" + resourcePath;
    }

    public static String makeBackgroundPath(String resourcePath) {
        return modID + "Resources/images/backgrounds/" + resourcePath;
    }

    public static void initialize() {
        thismod = new Anniv7Mod();

        try {
            Properties defaults = new Properties();
            //defaults.put("active", "TRUE");
            modConfig = new SpireConfig(modID, "anniv7Config", defaults);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*private void loadZones() {
        new AutoAdd(modID)
                .packageFilter(Anniv7Mod.class)
                .any(AbstractZone.class, (info, zone)->{
                    if (!info.ignore) {
                        String pkg = zone.getClass().getName();
                        int lastSeparator = pkg.lastIndexOf('.');
                        if (lastSeparator >= 0) pkg = pkg.substring(0, lastSeparator);
                        unfilteredAllZones.add(zone);
                        zonePackages.put(pkg, zone);
                    }
                });
        logger.info("Found zone classes with AutoAdd: " + unfilteredAllZones.size());
    }*/

    @Override
    public void receiveEditRelics() {
        new AutoAdd(modID)
                .packageFilter(Anniv7Mod.class)
                .any(AbstractSCRelic.class, (info, relic) -> {
                    if (relic.color == null) {
                            BaseMod.addRelic(relic, RelicType.SHARED);
                    } else {
                        BaseMod.addRelicToCustomPool(relic, relic.color);
                    }
                    if (!info.seen) {
                        UnlockTracker.markRelicAsSeen(relic.relicId);
                    }
                });
    }

    @Override
    public void receiveEditCards() {
        new AutoAdd(modID)
                .packageFilter(Anniv7Mod.class)
                .setDefaultSeen(true)
                .cards();

        BaseMod.addDynamicVariable(new SecondMagicNumber());
        BaseMod.addDynamicVariable(new SecondDamage());
    }

    @Override
    public void receivePostInitialize() {
        initializedStrings = true;
        //unfilteredAllZones.forEach(AbstractZone::loadStrings);
        //unfilteredAllZones.sort(Comparator.comparing(c->c.id));
        addPotions();
        addSaveFields();
        initializeConfig();
        initializeSavedData();
        BaseMod.addEvent(TestEvent.ID, TestEvent.class, Exordium.ID);
    }

    public static void addPotions() {
        if (Loader.isModLoaded("widepotions")) {
            Consumer<String> whitelist = getWidePotionsWhitelistMethod();

        }


    }

    public static void addSaveFields() {

    }

    private static Consumer<String> getWidePotionsWhitelistMethod() {
        // To avoid the need for a dependency of any kind, we call Wide Potions through reflection
        try {
            Method whitelistMethod = Class.forName("com.evacipated.cardcrawl.mod.widepotions.WidePotionsMod").getMethod("whitelistSimplePotion", String.class);
            return s -> {
                try {
                    whitelistMethod.invoke(null, s);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                    throw new RuntimeException("Error trying to whitelist wide potion for " + s, e);
                }
            };
        } catch (NoSuchMethodException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not find method WidePotionsMod.whitelistSimplePotion", e);
        }
    }

    @Deprecated
    private String getLangString() {
        for (Settings.GameLanguage lang : SupportedLanguages) {
            if (lang.equals(Settings.language)) {
                return Settings.language.name().toLowerCase(Locale.ROOT);
            }
        }
        return "eng";
    }

    @Override
    public void receiveEditStrings() {
        loadStrings("eng");
        loadZoneStrings(unfilteredAllZones, "eng");
        if (Settings.language != Settings.GameLanguage.ENG)
        {
            loadStrings(Settings.language.toString().toLowerCase());
            loadZoneStrings(unfilteredAllZones, Settings.language.toString().toLowerCase());
        }
    }


    private void loadStrings(String langKey) {
        if (!Gdx.files.internal(modID + "Resources/localization/" + langKey + "/").exists()) return;
        loadStringsFile(langKey, CharacterStrings.class);
        loadStringsFile(langKey, CardStrings.class);
        loadStringsFile(langKey, RelicStrings.class);
        loadStringsFile(langKey, PowerStrings.class);
        loadStringsFile(langKey, UIStrings.class);
        loadStringsFile(langKey, StanceStrings.class);
        loadStringsFile(langKey, OrbStrings.class);
        loadStringsFile(langKey, PotionStrings.class);
        loadStringsFile(langKey, EventStrings.class);
        loadStringsFile(langKey, MonsterStrings.class);
    }

    public void loadZoneStrings(Collection<?> zones, String langKey) {
//        for (AbstractZone zone : zones) {
//            String languageAndZone = langKey + "/" + zone.id;
//            String filepath = modID + "Resources/localization/" + languageAndZone;
//            if (!Gdx.files.internal(filepath).exists()) {
//                continue;
//            }
//            logger.info("Loading strings for zone " + zone.id + "from \"resources/localization/" + languageAndZone + "\"");
//
//            loadStringsFile(languageAndZone, CardStrings.class);
//            loadStringsFile(languageAndZone, RelicStrings.class);
//            loadStringsFile(languageAndZone, PowerStrings.class);
//            loadStringsFile(languageAndZone, UIStrings.class);
//            loadStringsFile(languageAndZone, StanceStrings.class);
//            loadStringsFile(languageAndZone, OrbStrings.class);
//            loadStringsFile(languageAndZone, PotionStrings.class);
//            loadStringsFile(languageAndZone, EventStrings.class);
//            loadStringsFile(languageAndZone, MonsterStrings.class);
//        }
    }

    private void loadStringsFile(String key, Class<?> stringType) {
        String filepath = modID + "Resources/localization/" + key + "/" + stringType.getSimpleName().replace("Strings", "strings") + ".json";
        if (Gdx.files.internal(filepath).exists()) {
            BaseMod.loadCustomStringsFile(stringType, filepath);
        }
    }

    @Override
    public void receiveEditKeywords() {
        loadKeywords("eng");
        if (Settings.language != Settings.GameLanguage.ENG) {
            loadKeywords(Settings.language.toString().toLowerCase());
        }
    }

    private void loadKeywords(String langKey) {
        String filepath = modID + "Resources/localization/" + langKey + "/Keywordstrings.json";
        Gson gson = new Gson();
        List<Keyword> keywords = new ArrayList<>();
        if (Gdx.files.internal(filepath).exists()) {
            String json = Gdx.files.internal(filepath).readString(String.valueOf(StandardCharsets.UTF_8));
            keywords.addAll(Arrays.asList(gson.fromJson(json, Keyword[].class)));
        }
//        for (AbstractZone zone : unfilteredAllZones) {
//            String languageAndZone = langKey + "/" + zone.id;
//            String zoneJson = modID + "Resources/localization/" + languageAndZone + "/Keywordstrings.json";
//            FileHandle handle = Gdx.files.internal(zoneJson);
//            if (handle.exists()) {
//                logger.info("Loading keywords for zone " + zone.id + "from \"resources/localization/" + languageAndZone + "\"");
//                zoneJson = handle.readString(String.valueOf(StandardCharsets.UTF_8));
//                keywords.addAll(Arrays.asList(gson.fromJson(zoneJson, Keyword[].class)));
//            }
//        }

        for (Keyword keyword : keywords) {
            BaseMod.addKeyword(modID, keyword.PROPER_NAME, keyword.NAMES, keyword.DESCRIPTION);
            if (!keyword.ID.isEmpty())
            {
                Anniv7Mod.keywords.put(keyword.ID, keyword);
            }
        }
    }

    @Override
    public void receiveAddAudio() {

    }

    private ModPanel settingsPanel;

    private void initializeConfig() {
//        UIStrings configStrings = CardCrawlGame.languagePack.getUIString(makeID("ConfigMenuText"));
//
//        Texture badge = TexLoader.getTexture(makeImagePath("ui/badge.png"));
//
//        settingsPanel = new ModPanel();
//
//        BaseMod.registerModBadge(badge, configStrings.TEXT[0], configStrings.TEXT[1], configStrings.TEXT[2], settingsPanel);
    }

    private void initializeSavedData() {

    }
}



