package spireCafe;

import basemod.AutoAdd;
import basemod.BaseMod;
import basemod.ModPanel;
import basemod.abstracts.CustomSavable;
import basemod.devcommands.ConsoleCommand;
import basemod.helpers.RelicType;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import imgui.ImGui;
import imgui.type.ImFloat;
import javassist.CtClass;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import spireCafe.abstracts.AbstractCafeInteractable;
import spireCafe.abstracts.AbstractSCRelic;
import spireCafe.cardvars.SecondDamage;
import spireCafe.cardvars.SecondMagicNumber;
import spireCafe.interactables.attractions.makeup.MakeupTableAttraction;
import spireCafe.interactables.patrons.missingno.MissingnoUtil;
import spireCafe.screens.CafeMerchantScreen;
import spireCafe.ui.FixedModLabeledToggleButton.FixedModLabeledToggleButton;
import spireCafe.util.TexLoader;
import spireCafe.util.cutsceneStrings.CutsceneStrings;
import spireCafe.util.cutsceneStrings.LocalizedCutsceneStrings;
import spireCafe.util.devcommands.Cafe;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Consumer;

import static spireCafe.interactables.attractions.bookshelf.BookshelfAttraction.PAGE_CONFIG_KEY;
import static spireCafe.interactables.patrons.missingno.MissingnoPatches.*;
import static spireCafe.patches.CafeEntryExitPatch.CAFE_ENTRY_SOUND_KEY;

@SuppressWarnings({"unused"})
@SpireInitializer
public class Anniv7Mod implements
        EditCardsSubscriber,
        EditRelicsSubscriber,
        EditStringsSubscriber,
        EditKeywordsSubscriber,
        PostInitializeSubscriber,
        AddAudioSubscriber,
        PostUpdateSubscriber,
        ImGuiSubscriber {

    public static final Logger logger = LogManager.getLogger("SpireCafe");

    public static Settings.GameLanguage[] SupportedLanguages = {
            Settings.GameLanguage.ENG
    };

    public static Anniv7Mod thismod;
    public static SpireConfig modConfig = null;
    public static HashSet<String> currentRunSeenInteractables = null;
    public static ArrayList<String> allTimeSeenInteractables = null;

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

    public static List<String> unfilteredAllInteractableIDs = new ArrayList<>();
    public static HashMap<String, Class<? extends AbstractCafeInteractable>> interactableClasses = new HashMap<>();


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

    public static String makeMerchantPath(String resourcePath) {
        return modID + "Resources/images/merchants/" + resourcePath;
    }

    public static String makeBartenderPath(String resourcePath) {
        return modID + "Resources/images/bartenders/" + resourcePath;
    }

    public static String makeAttractionPath(String resourcePath) {
        return modID + "Resources/images/attractions/" + resourcePath;
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
            defaults.put("cafeEntryCost", "TRUE");
            defaults.put("disableShaders", "FALSE");
            defaults.put("seenInteractables", "");
            defaults.put(PAGE_CONFIG_KEY, "");
            modConfig = new SpireConfig(modID, "anniv7Config", defaults);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadInteractables() {
        AutoAdd autoAdd = new AutoAdd(modID)
                .packageFilter(Anniv7Mod.class);

        Class<?> type = AbstractCafeInteractable.class;
        Collection<CtClass> foundClasses = autoAdd.findClasses(type);

        for (CtClass ctClass : foundClasses) {
            boolean ignore = ctClass.hasAnnotation(AutoAdd.Ignore.class);
            if (!ignore) {
                String id = ctClass.getSimpleName();
                unfilteredAllInteractableIDs.add(id);
                try {
                    Class<? extends AbstractCafeInteractable> interactableClass = (Class<? extends AbstractCafeInteractable>) Loader.getClassPool().getClassLoader().loadClass(ctClass.getName());
                    interactableClasses.put(id, interactableClass);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        logger.info("Found interactable classes with AutoAdd: " + unfilteredAllInteractableIDs.size());
    }

    public static ArrayList<String> getSeenInteractables() {
        if (modConfig == null) return new ArrayList<>();
        return new ArrayList<>(Arrays.asList(modConfig.getString("seenInteractables").split(",")));
    }

    public static void saveSeenInteractables(ArrayList<String> input) throws IOException {
        if (modConfig == null) return;
        modConfig.setString("seenInteractables", String.join(",", input));
        modConfig.save();
    }

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
        addPotions();
        addSaveFields();
        initializeConfig();
        initializeSavedData();
        BaseMod.addEvent(CafeRoom.ID, CafeRoom.class, "CafeDungeon");
        BaseMod.addCustomScreen(new CafeMerchantScreen());
        ConsoleCommand.addCommand("cafe", Cafe.class);
    }

    public static void addPotions() {
        if (Loader.isModLoaded("widepotions")) {
            Consumer<String> whitelist = getWidePotionsWhitelistMethod();

        }
    }

    public static final ImFloat shake_power = new ImFloat(0.007f);
    public static final ImFloat shake_rate = new ImFloat(0.1f);
    public static final ImFloat shake_speed = new ImFloat(2f);
    public static final ImFloat shake_block_size = new ImFloat(0.001f);
    public static final ImFloat shake_color_rate = new ImFloat(0.004f);

    @Override
    public void receiveImGui() {
        ImGui.sliderFloat("Glitch Power", shake_power.getData(), 0, 1f);
        ImGui.sliderFloat("Glitch Rate", shake_rate.getData(), 0, 1);
        ImGui.sliderFloat("Glitch Speed", shake_speed.getData(), 0, 10);
        ImGui.sliderFloat("Glitch Block Size", shake_block_size.getData(), 0, 1.0f);
        ImGui.sliderFloat("Glitch Color Rate", shake_color_rate.getData(), 0, 0.01f);
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
        loadInteractables();

        loadStrings("eng");
        loadInteractableStrings(unfilteredAllInteractableIDs, "eng");
        if (Settings.language != Settings.GameLanguage.ENG)
        {
            loadStrings(Settings.language.toString().toLowerCase());
            loadInteractableStrings(unfilteredAllInteractableIDs, Settings.language.toString().toLowerCase());
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
        loadStringsFile(langKey, BlightStrings.class);
    }

    public void loadInteractableStrings(Collection<String> interactableIDs, String langKey) {
        for (String id : interactableIDs) {
            String languageAndInteractable = langKey + "/" + id;
            String filepath = modID + "Resources/localization/" + languageAndInteractable;
            if (!Gdx.files.internal(filepath).exists()) {
                continue;
            }
            logger.info("Loading strings for interactable " + id + " from \"resources/localization/" + languageAndInteractable + "\"");

            loadStringsFile(languageAndInteractable, CharacterStrings.class);
            loadStringsFile(languageAndInteractable, CardStrings.class);
            loadStringsFile(languageAndInteractable, RelicStrings.class);
            loadStringsFile(languageAndInteractable, PowerStrings.class);
            loadStringsFile(languageAndInteractable, UIStrings.class);
            loadStringsFile(languageAndInteractable, StanceStrings.class);
            loadStringsFile(languageAndInteractable, OrbStrings.class);
            loadStringsFile(languageAndInteractable, PotionStrings.class);
            loadCutsceneStringsFile(languageAndInteractable, CutsceneStrings.class);
            loadStringsFile(languageAndInteractable, MonsterStrings.class);
            loadStringsFile(languageAndInteractable, BlightStrings.class);
        }
    }

    private void loadStringsFile(String key, Class<?> stringType) {
        String filepath = modID + "Resources/localization/" + key + "/" + stringType.getSimpleName().replace("Strings", "strings") + ".json";
        if (Gdx.files.internal(filepath).exists()) {
            BaseMod.loadCustomStringsFile(stringType, filepath);
        }
    }

    private void loadCutsceneStringsFile(String key, Class<?> stringType) {
        String filepath = modID + "Resources/localization/" + key + "/" + stringType.getSimpleName().replace("Strings", "strings") + ".json";
        if (Gdx.files.internal(filepath).exists()) {
            LocalizedCutsceneStrings.loadCutsceneStringsFile(filepath);
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
        BaseMod.addAudio(CAFE_ENTRY_SOUND_KEY, makePath("audio/cafe_entry_door_chime.mp3"));
        BaseMod.addAudio(POKE1, makePath("audio/poke1.mp3"));
        BaseMod.addAudio(POKE2, makePath("audio/poke2.mp3"));
        BaseMod.addAudio(POKE3, makePath("audio/poke3.mp3"));
        BaseMod.addAudio(POKE4, makePath("audio/poke4.mp3"));
        BaseMod.addAudio(POKE5, makePath("audio/poke5.mp3"));
        BaseMod.addAudio(POKE6, makePath("audio/poke6.mp3"));
        BaseMod.addAudio(POKE7, makePath("audio/poke7.mp3"));
        BaseMod.addAudio(POKE8, makePath("audio/poke8.mp3"));
        BaseMod.addAudio(POKE9, makePath("audio/poke9.mp3"));
    }

    public static float time = 0f;
    @Override
    public void receivePostUpdate() {
        time += Gdx.graphics.getRawDeltaTime();
        MissingnoUtil.doMissingnoStuff();
    }

    private ModPanel settingsPanel;

    private static final float ENTRYCOST_CHECKBOX_X = 400f;
    private static final float ENTRYCOST_CHECKBOX_Y = 685f;
    private static final float SHADERS_CHECKBOX_SERIES_X = 400f;
    private static final float SHADERS_CHECKBOX_Y = 600f;


    private void initializeConfig() {
        UIStrings configStrings = CardCrawlGame.languagePack.getUIString(makeID("ConfigMenuText"));

        Texture badge = TexLoader.getTexture(makeImagePath("ui/badge.png"));

        settingsPanel = new ModPanel();
        FixedModLabeledToggleButton cafeEntryCostToggle = new FixedModLabeledToggleButton(configStrings.TEXT[3], ENTRYCOST_CHECKBOX_X, ENTRYCOST_CHECKBOX_Y, Color.WHITE, FontHelper.tipBodyFont, getCafeEntryCostConfig(), null,
                (label) -> {},
                (button) -> setCafeEntryCostConfig(button.enabled));
        settingsPanel.addUIElement(cafeEntryCostToggle);

        FixedModLabeledToggleButton disableShaders = new FixedModLabeledToggleButton(configStrings.TEXT[4], SHADERS_CHECKBOX_SERIES_X, SHADERS_CHECKBOX_Y, Color.WHITE, FontHelper.tipBodyFont, getDisableShadersConfig(), null,
                (label) -> {},
                (button) -> setDisableShadersConfig(button.enabled));
        settingsPanel.addUIElement(disableShaders);


        BaseMod.registerModBadge(badge, configStrings.TEXT[0], configStrings.TEXT[1], configStrings.TEXT[2], settingsPanel);
    }

    public static boolean getCafeEntryCostConfig() {
        return modConfig != null && modConfig.getBool("cafeEntryCost");
    }

    public static void setCafeEntryCostConfig(boolean bool) {
        if (modConfig != null) {
            modConfig.setBool("cafeEntryCost", bool);
            try {
                modConfig.save();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean getDisableShadersConfig() {
        return modConfig != null && modConfig.getBool("disableShaders");
    }

    public static void setDisableShadersConfig(boolean bool) {
        if (modConfig != null) {
            modConfig.setBool("disableShaders", bool);
            try {
                modConfig.save();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void initializeSavedData() {
    }

    public static void addSaveFields() {
        BaseMod.addSaveField(SavableCurrentRunSeenInteractables.SaveKey, new SavableCurrentRunSeenInteractables());
        BaseMod.addSaveField(makeID("AppliedMakeup"), new CustomSavable<Boolean>() {
            @Override
            public Boolean onSave() {
                return MakeupTableAttraction.isAPrettySparklingPrincess;
            }

            @Override
            public void onLoad(Boolean state) {
                MakeupTableAttraction.isAPrettySparklingPrincess = state;
            }
        });
    }

    public static class SavableCurrentRunSeenInteractables implements CustomSavable<HashSet<String>> {
        public final static String SaveKey = "CurrentRunSeenInteractables";

        @Override
        public HashSet<String> onSave() {
            return currentRunSeenInteractables;
        }

        @Override
        public void onLoad(HashSet<String> s) {
            currentRunSeenInteractables = s == null ? new HashSet<>() : s;
        }
    }

}



