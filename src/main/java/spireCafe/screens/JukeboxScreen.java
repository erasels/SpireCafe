package spireCafe.screens;

import basemod.abstracts.CustomScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.ConfigUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.ui.buttons.LargeDialogOptionButton;
import java.util.function.Consumer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
public class JukeboxScreen extends CustomScreen {
    private static final String BACKGROUND_PATH = "anniv7Resources/images/attractions/jukebox/Background.png";
    private static final String BUTTON_PATH = "anniv7Resources/images/attractions/jukebox/Button.png";
    private static final String BUTTONGLOW_PATH = "anniv7Resources/images/attractions/jukebox/ButtonGlow.png";
    private static final String BUTTONHOVER_PATH = "anniv7Resources/images/attractions/jukebox/ButtonHover.png";
    private static final String BUTTONLONG_PATH = "anniv7Resources/images/attractions/jukebox/ButtonLong.png";
    private static final String BUTTONON_PATH = "anniv7Resources/images/attractions/jukebox/ButtonOn.png";
    private static final String BUTTONOFF_PATH = "anniv7Resources/images/attractions/jukebox/ButtonOff.png";
    private static final String BUTTONDISABLED_PATH = "anniv7Resources/images/attractions/jukebox/ButtonflipN.png";
    private static final String WINDOW_PATH = "anniv7Resources/images/attractions/jukebox/Window.png";
    private static final String CUSTOM_MUSIC_FOLDER = ConfigUtils.CONFIG_DIR + File.separator + "anniv7/cafe_jukebox";

    private static final int BUTTONS_PER_PAGE = 5;

    private final Texture buttonDisabledTexture;

    private final Texture backgroundTexture;
    private final Texture buttonTexture;
    private final Texture buttonglowTexture;
    private final Texture buttonhoverTexture;
    private final Texture buttonlongTexture;
    private final Texture buttononTexture;
    private final Texture buttonoffTexture;
    private final Texture windowTexture;

    private final List<LargeDialogOptionButton> buttons = new ArrayList<>();
    private final List<String> predefinedTracks = new ArrayList<>();
    private final List<String> customTracks = new ArrayList<>();
    private final List<String> allTracks = new ArrayList<>();
    private final List<Boolean> buttonHoverStates = new ArrayList<>();

    private final List<Boolean> buttonClickedStates = new ArrayList<>();
    private final LargeDialogOptionButton nextButton = new LargeDialogOptionButton(999, "Next");
    private final LargeDialogOptionButton previousButton = new LargeDialogOptionButton(998, "Previous");
    public static boolean isPlaying = false;

    private int currentPage = 0;
    private static Music nowPlayingSong = null;
    private String textField = "Waiting for Song";
    private int glowingButtonIndex = -1;
    public static boolean overrideEnabled = false;
    public static boolean shopOverride = false;
    public static boolean shrineOverride = false;
    public static boolean bossOverride = false;
    public static boolean eliteOverride = false;
    public static boolean eventOverride = false;
    private Preferences preferences = Gdx.app.getPreferences("jukeboxOverrides");

    public JukeboxScreen() {
        backgroundTexture = new Texture(BACKGROUND_PATH);
        buttonTexture = new Texture(BUTTON_PATH);
        buttonglowTexture = new Texture(BUTTONGLOW_PATH);
        buttonhoverTexture = new Texture(BUTTONHOVER_PATH);
        buttonlongTexture = new Texture(BUTTONLONG_PATH);
        buttononTexture = new Texture(BUTTONON_PATH);
        buttonoffTexture = new Texture(BUTTONOFF_PATH);
        windowTexture = new Texture(WINDOW_PATH);
        buttonDisabledTexture = new Texture(BUTTONDISABLED_PATH);


        initializePredefinedTracks();
        loadCustomTracks();
        combineTracks();
        createButtons();
        initializeOverrides();
    }
    private void initializeOverrides() {
        overrideEnabled = preferences.getBoolean("overrideEnabled", false);
        shopOverride = preferences.getBoolean("shopOverride", false);
        shrineOverride = preferences.getBoolean("shrineOverride", false);
        bossOverride = preferences.getBoolean("bossOverride", false);
        eliteOverride = preferences.getBoolean("eliteOverride", false);
        eventOverride = preferences.getBoolean("eventOverride", false);
    }
    private void saveOverrides() {
        preferences.putBoolean("overrideEnabled", overrideEnabled);
        preferences.putBoolean("shopOverride", shopOverride);
        preferences.putBoolean("shrineOverride", shrineOverride);
        preferences.putBoolean("bossOverride", bossOverride);
        preferences.putBoolean("eliteOverride", eliteOverride);
        preferences.putBoolean("eventOverride", eventOverride);
        preferences.flush();
    }
    private void initializePredefinedTracks() {
        predefinedTracks.add("SHOP");
        predefinedTracks.add("SHRINE");
        predefinedTracks.add("MINDBLOOM");
        predefinedTracks.add("BOSS_BOTTOM");
        predefinedTracks.add("BOSS_CITY");
        predefinedTracks.add("BOSS_BEYOND");
        predefinedTracks.add("BOSS_ENDING");
        predefinedTracks.add("ELITE");
        predefinedTracks.add("CREDITS");
    }
    private void loadCustomTracks() {
        customTracks.clear();

        File customFolder = new File(CUSTOM_MUSIC_FOLDER);
        if (!customFolder.exists()) {
            System.out.println("Custom music folder not found: " + CUSTOM_MUSIC_FOLDER);
            boolean created = customFolder.mkdirs(); // Attempt to create the folder
            if (created) {
                System.out.println("Custom music folder created: " + CUSTOM_MUSIC_FOLDER);
            } else {
                System.err.println("Failed to create custom music folder.");
                return;
            }
        }

        // Ensure Cafe_Theme.mp3 is present
        File cafeThemeFile = new File(customFolder, "Cafe_Theme.mp3");
        if (!cafeThemeFile.exists()) {
            System.out.println("Cafe_Theme.mp3 not found in custom folder. Adding default file...");
            copyDefaultCafeTheme(cafeThemeFile);
        } else {
            System.out.println("Cafe_Theme.mp3 already exists in custom folder.");
        }

        // Load files from the custom folder
        File[] files = customFolder.listFiles();
        if (files != null) {
            System.out.println("Scanning custom music folder for audio files...");
            for (File file : files) {
                if (file.isFile() && isValidAudioFile(file.getName())) {
                    String trimmedName = file.getName()
                            .replaceAll("\\.(ogg|mp3|wav)$", "") // Remove extensions
                            .replace("_", " ");                 // Replace underscores with spaces
                    customTracks.add(trimmedName);
                    System.out.println("Custom track added: " + trimmedName);
                } else {
                    System.out.println("Invalid or unsupported file skipped: " + file.getName());
                }
            }
        }
    }

    // Helper method to copy Cafe_Theme.mp3 to the custom folder
    private void copyDefaultCafeTheme(File destination) {
        try {
            FileHandle sourceFile = Gdx.files.internal("anniv7Resources/audio/Cafe_Theme.mp3");
            if (sourceFile.exists()) {
                FileHandle destFile = Gdx.files.absolute(destination.getAbsolutePath());
                sourceFile.copyTo(destFile);
                System.out.println("Successfully copied Cafe_Theme.mp3 to: " + destination.getAbsolutePath());
            } else {
                System.err.println("Default Cafe_Theme.mp3 not found in resources.");
            }
        } catch (Exception e) {
            System.err.println("Failed to copy Cafe_Theme.mp3 to custom folder.");
            e.printStackTrace();
        }
    }

    // Helper method to check for valid audio file extensions
    private boolean isValidAudioFile(String fileName) {
        // Convert to lowercase to handle case-insensitive matching
        String lowerCaseName = fileName.toLowerCase();
        return lowerCaseName.endsWith(".ogg") ||
                lowerCaseName.endsWith(".mp3") ||
                lowerCaseName.endsWith(".wav");
    }

    private void combineTracks() {
        allTracks.clear();
        allTracks.addAll(predefinedTracks); // Add predefined tracks
        allTracks.addAll(customTracks);     // Add custom tracks

        System.out.println("Predefined tracks: " + predefinedTracks);
        System.out.println("Custom tracks: " + customTracks);
        System.out.println("All tracks combined: " + allTracks);


    }
    private void createButtons() {
        buttons.clear();
        buttonClickedStates.clear();
        buttonHoverStates.clear(); // Initialize hover states
        // Determine the range of tracks for the current page
        int startIndex = currentPage * (BUTTONS_PER_PAGE * 2); // Two columns per page
        int endIndex = Math.min(startIndex + (BUTTONS_PER_PAGE * 2), allTracks.size());

        System.out.println("Creating buttons for page: " + currentPage + " (Tracks " + startIndex + " to " + (endIndex - 1) + ")");


        for (int i = startIndex; i < endIndex; i++) {
            String trackName = allTracks.get(i);
            buttons.add(new LargeDialogOptionButton(i, trackName));
            buttonClickedStates.add(false);
            buttonHoverStates.add(false);
        }
    }
    public void open() {
        AbstractDungeon.screen = curScreen();
        AbstractDungeon.isScreenUp = true;
        AbstractDungeon.overlayMenu.cancelButton.show("Close Jukebox");
        AbstractDungeon.overlayMenu.hideBlackScreen();

        if (nowPlayingSong != null) {
            textField = "Now Playing: " + nowPlayingSong;
            glowingButtonIndex = allTracks.indexOf(nowPlayingSong);
        } else {
            textField = "Waiting for Song";
            glowingButtonIndex = -1;
        }
    }

    @Override
    public void reopen() {
        open();
    }

    @Override
    public void close() {
        AbstractDungeon.screen = AbstractDungeon.previousScreen != null
                ? AbstractDungeon.previousScreen
                : AbstractDungeon.CurrentScreen.NONE;
        AbstractDungeon.isScreenUp = false;
        AbstractDungeon.overlayMenu.hideBlackScreen();
    }

    @Override
    public AbstractDungeon.CurrentScreen curScreen() {
        return ScreenEnum.JUKEBOX_SCREEN;
    }
    @Override
    public void update() {
        // Define song selection buttons
        float leftColumnX = 408f * Settings.scale; // X position for the left column
        float rightColumnX = 808f * Settings.scale; // X position for the right column
        float startingY = 613f * Settings.scale;   // Starting Y position for buttons
        float buttonSpacing = 85f * Settings.scale; // Vertical spacing between buttons
        float baseX = 530f; // Starting X position
        float baseY = 250f; // Y position
        float horizontalSpacing = 160f; // Spacing between buttons
        float buttonWidth = 64f * Settings.scale;
        float buttonHeight = 42f * Settings.scale;

        updatePaginationButtons();

        boolean[] states = {overrideEnabled, shopOverride, shrineOverride, bossOverride, eliteOverride, eventOverride};
        Consumer<Boolean>[] actions = new Consumer[]{
                (value) -> overrideEnabled = (boolean) value,
                (value) -> shopOverride = (boolean) value,
                (value) -> shrineOverride = (boolean) value,
                (value) -> bossOverride = (boolean) value,
                (value) -> eliteOverride = (boolean) value,
                (value) -> eventOverride = (boolean) value
        };
        for (int i = 0; i < buttons.size(); i++) {
            LargeDialogOptionButton button = buttons.get(i);

            // Determine x and y based on the column (left or right)
            float x = (i < BUTTONS_PER_PAGE) ? leftColumnX : rightColumnX;
            float y = startingY - ((i % BUTTONS_PER_PAGE) * buttonSpacing);

            // Define hitbox dimensions
            float hitboxWidth = 300f * Settings.scale;
            float hitboxHeight = 70f * Settings.scale;
            float hitboxX = x + (400f * Settings.scale - hitboxWidth) / 2f;
            float hitboxY = y + (200f * Settings.scale - hitboxHeight) / 2f;

            Hitbox buttonHitbox = new Hitbox(hitboxX, hitboxY, hitboxWidth, hitboxHeight);
            buttonHitbox.update();

            // Update hover state
            buttonHoverStates.set(i, buttonHitbox.hovered);

            if (buttonHitbox.hovered && InputHelper.justClickedLeft) {
                // Mark the clicked button and reset others
                for (int j = 0; j < buttonClickedStates.size(); j++) {
                    buttonClickedStates.set(j, false); // Reset all to false
                }
                buttonClickedStates.set(i, true); // Set the clicked button to true
                handleButtonClick(button); // Trigger click behavior
            }
        }
        for (int i = 0; i < states.length; i++) {
            float buttonX = baseX + (i * horizontalSpacing);
            float buttonY = (baseY - 25f) * Settings.scale;
            float adjustedHeight = (buttonHeight +5f) * Settings.scale;

            Hitbox overridehitbox = new Hitbox(buttonX, buttonY, buttonWidth, adjustedHeight);
            overridehitbox.update();


            if (overridehitbox.hovered && InputHelper.justClickedLeft) {
                actions[i].accept(!states[i]);
                saveOverrides();
                System.out.println("Override button clicked: " + i); // Debugging log
            }
        }
    }

    private String formatTrackName(String trackName) {
        // Remove the file extension and replace underscores with spaces
        trackName = trackName.replaceAll("\\.ogg|\\.mp3|\\.wav", "").replace("_", " ");
        // Capitalize each word
        String[] words = trackName.split(" ");
        StringBuilder formattedName = new StringBuilder();
        for (String word : words) {
            if (!word.isEmpty()) {
                formattedName.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1).toLowerCase())
                        .append(" ");
            }
        }
        return formattedName.toString().trim();
    }
    private void updatePaginationButtons() {
        int totalPages = (int) Math.ceil((double) allTracks.size() / (BUTTONS_PER_PAGE * 2));

        // Next button
        float nextX = 1208f * Settings.scale;
        float nextY = 100f * Settings.scale;
        Hitbox nextButtonHitbox = new Hitbox(nextX - 50f * Settings.scale, nextY, 300f * Settings.scale, 85f * Settings.scale);
        nextButtonHitbox.update();

        if (currentPage < totalPages - 1 && nextButtonHitbox.hovered && InputHelper.justClickedLeft) {
            currentPage++;
            createButtons(); // Refresh buttons for the new page
            System.out.println("Navigating to Next Page: " + currentPage);
        }

        // Previous button
        float prevX = 508f * Settings.scale;
        float prevY = 100f * Settings.scale;
        Hitbox prevButtonHitbox = new Hitbox(prevX - 50f * Settings.scale, prevY, 300f * Settings.scale, 85f * Settings.scale);
        prevButtonHitbox.update();

        if (currentPage > 0 && prevButtonHitbox.hovered && InputHelper.justClickedLeft) {
            currentPage--;
            createButtons(); // Refresh buttons for the previous page
            System.out.println("Navigating to Previous Page: " + currentPage);
        }
    }
    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(Color.WHITE);
        sb.draw(backgroundTexture, 0, 0, Settings.WIDTH, Settings.HEIGHT);

        // Render song selection buttons
        float leftColumnX = 408f * Settings.scale; // X position for the left column
        float rightColumnX = 808f * Settings.scale; // X position for the right column
        float startingY = 613f * Settings.scale;   // Starting Y position
        float buttonSpacing = 85f * Settings.scale; // Spacing between buttons

        for (int i = 0; i < buttons.size(); i++) {
            LargeDialogOptionButton button = buttons.get(i);

            // Determine x and y based on the column (left or right)
            float x = (i < BUTTONS_PER_PAGE) ? leftColumnX : rightColumnX;
            float y = startingY - ((i % BUTTONS_PER_PAGE) * buttonSpacing);

            // Draw the visual button
            float visualWidth = 400f * Settings.scale;
            float visualHeight = 200f * Settings.scale;
            sb.draw(buttonTexture, x, y, visualWidth, visualHeight);

            // Highlight if clicked
            if (buttonClickedStates.get(i)) {
                sb.draw(buttonglowTexture, x, y, visualWidth, visualHeight);
            }
            // Highlight if hovered
            else if (buttonHoverStates.get(i)) {
                sb.draw(buttonhoverTexture, x, y, visualWidth, visualHeight);
            }

            // Render the formatted button text
            String displayName = formatTrackName(button.msg);
            FontHelper.renderFontCentered(sb, FontHelper.buttonLabelFont, displayName,
                    x + (visualWidth / 2f), y + (visualHeight / 2f), Color.WHITE);

            float hitboxWidth = 300f * Settings.scale; // Match hitbox size
            float hitboxHeight = 70f * Settings.scale;
            float hitboxX = x + (visualWidth - hitboxWidth) / 2f;
            float hitboxY = y + (visualHeight - hitboxHeight) / 2f;
            Hitbox debugHitbox = new Hitbox(hitboxX, hitboxY, hitboxWidth, hitboxHeight);
            debugHitbox.render(sb);
        }

        // Render pagination buttons
        int totalPages = (int) Math.ceil((double) allTracks.size() / (BUTTONS_PER_PAGE * 2));
        float nextX = 1208f * Settings.scale;
        float nextY = 100f * Settings.scale;
        float prevX = 508f * Settings.scale;
        float prevY = 100f * Settings.scale;

        // Next button
        if (currentPage < totalPages - 1) {
            renderPaginationButton(nextX, nextY, buttonlongTexture, "Next", sb, () -> {
                currentPage++;
                createButtons(); // Refresh buttons for the new page
                System.out.println("Navigating to Next Page: " + currentPage);
            });
        }

        // Previous button
        if (currentPage > 0) {
            renderPaginationButton(prevX, prevY, buttonlongTexture, "Previous", sb, () -> {
                currentPage--;
                createButtons(); // Refresh buttons for the previous page
                System.out.println("Navigating to Previous Page: " + currentPage);
            });
        }

        // Render window and text field
        float windowX = (Settings.WIDTH / 2f);
        float windowY = (Settings.HEIGHT / 2f);
        sb.draw(windowTexture, windowX - 450f, windowY + 170f, 900f * Settings.scale, 250f * Settings.scale);
        FontHelper.renderFontCentered(sb, FontHelper.buttonLabelFont, textField, Settings.WIDTH / 2f, Settings.HEIGHT - (242f * Settings.scale), Color.GOLD);

        // Render override settings
        addOverrideSettingsToJukebox(sb);
    }

    private void renderPaginationButton(float x, float y, Texture texture, String label, SpriteBatch sb, Runnable onClick) {
        // Define button dimensions
        float buttonWidth = 300f * Settings.scale;
        float buttonHeight = 85f * Settings.scale;
        float drawX = x - 50f * Settings.scale; // Center the button visually
        float drawY = y; // Base Y position for the button

        // Create and update the hitbox
        Hitbox hb = new Hitbox(drawX, drawY, buttonWidth, buttonHeight);
        hb.update();

        // Draw the button texture
        sb.draw(texture, drawX, drawY, buttonWidth, buttonHeight);

        // Determine the text color based on hover state
        Color textColor = hb.hovered ? Settings.GOLD_COLOR : Color.WHITE;

        // Render the button label
        FontHelper.renderFontCentered(sb, FontHelper.buttonLabelFont, label, drawX + (buttonWidth / 2), drawY + (buttonHeight / 2), textColor);
        // Render the hitbox for debugging
        hb.render(sb);
    }

    private void addOverrideSettingsToJukebox(SpriteBatch sb) {
        // Initial position for the buttons and text
        float baseX = 530.0f; // Starting X position for the first button
        float baseY = 250.0f; // Y position for both buttons and text
        float horizontalSpacing = 160.0f; // Spacing between buttons

        // Render each button with its corresponding text
        renderOverrideOption(sb, "Override Music", baseX - 10f * Settings.scale, baseY - 30f * Settings.scale, overrideEnabled, false, (value) -> {
            overrideEnabled = value; // Update overrideEnabled
            if (!overrideEnabled) {
                // Automatically disable all dependent buttons
                shopOverride = false;
                shrineOverride = false;
                bossOverride = false;
                eliteOverride = false;
                eventOverride = false;
            }
            saveOverrides();
        });

        renderOverrideOption(sb, "Shop", baseX + horizontalSpacing - 10f * Settings.scale, baseY - 30f * Settings.scale, shopOverride, overrideEnabled, (value) -> {
            if (overrideEnabled) {
                shopOverride = value;
                saveOverrides();
            }
        });

        renderOverrideOption(sb, "Shrine", baseX + (2 * horizontalSpacing) - 10f * Settings.scale, baseY - 30f * Settings.scale, shrineOverride, overrideEnabled, (value) -> {
            if (overrideEnabled) {
                shrineOverride = value;
                saveOverrides();
            }
        });

        renderOverrideOption(sb, "Boss", baseX + (3 * horizontalSpacing) - 10f * Settings.scale, baseY - 30f * Settings.scale, bossOverride, overrideEnabled, (value) -> {
            if (overrideEnabled) {
                bossOverride = value;
                saveOverrides();
            }
        });

        renderOverrideOption(sb, "Elite", baseX + (4 * horizontalSpacing) - 10f * Settings.scale, baseY - 30f * Settings.scale, eliteOverride, overrideEnabled, (value) -> {
            if (overrideEnabled) {
                eliteOverride = value;
                saveOverrides();
            }
        });

        renderOverrideOption(sb, "Event", baseX + (5 * horizontalSpacing) - 10f * Settings.scale, baseY - 30f * Settings.scale, eventOverride, overrideEnabled, (value) -> {
            if (overrideEnabled) {
                eventOverride = value;
                saveOverrides();
            }
        });
    }
    private void renderOverrideOption(SpriteBatch sb, String label, float buttonX, float buttonY, boolean isActive, boolean isParentEnabled, Consumer<Boolean> onClick) {
        float buttonWidth = 64f * Settings.scale;
        float buttonHeight = 42f * Settings.scale;
        boolean isDisabled = !isParentEnabled && !label.equals("Override Music");

        Texture currentTexture = isDisabled ? buttonDisabledTexture : (isActive ? buttononTexture : buttonoffTexture);
        sb.draw(currentTexture, buttonX, buttonY, buttonWidth, buttonHeight);

        // Vanilla Hitbox logic
        Hitbox hitbox = new Hitbox(buttonX, buttonY, buttonWidth, buttonHeight);
        hitbox.render(sb);

        if (!isDisabled && InputHelper.justClickedLeft && hitbox.hovered) {
            onClick.accept(!isActive);
        }

        FontHelper.renderFontCentered(sb, FontHelper.charDescFont, label, buttonX + (buttonWidth / 2f), buttonY + (buttonHeight + 20f), Settings.CREAM_COLOR);
    }
    @Override
    public void openingSettings() {
        AbstractDungeon.previousScreen = curScreen();
    }

    @Override
    public boolean allowOpenDeck() {
        return true;
    }

    @Override
    public void openingDeck() {
        AbstractDungeon.previousScreen = curScreen();
    }

    @Override
    public boolean allowOpenMap() {
        return false;
    }

    private void handleButtonClick(LargeDialogOptionButton button) {
        String selectedTrack = allTracks.get(button.slot);
        textField = "Now Playing: " + selectedTrack;

        if (predefinedTracks.contains(selectedTrack)) {
            // Play predefined track
            String trackFileName = getTrackFileName(selectedTrack);
            System.out.println("Playing predefined track: " + trackFileName);
            stopCurrentMusic();
            CardCrawlGame.music.playTempBgmInstantly(trackFileName, true);
            isPlaying = true;
        } else {
            // Play custom track
            String originalTrackFileName = getOriginalFileName(selectedTrack);
            if (originalTrackFileName == null) {
                System.err.println("File not found for track: " + selectedTrack);
                return;
            }
            String trackPath = CUSTOM_MUSIC_FOLDER + File.separator + originalTrackFileName;
            playTempBgm(trackPath);

        }
    }

    private String getTrackFileName(String key) {
        switch (key) {
            case "SHOP":
                return "STS_Merchant_NewMix_v1.ogg";
            case "SHRINE":
                return "STS_Shrine_NewMix_v1.ogg";
            case "MINDBLOOM":
                return "STS_Boss1MindBloom_v1.ogg";
            case "BOSS_BOTTOM":
                return "STS_Boss1_NewMix_v1.ogg";
            case "BOSS_CITY":
                return "STS_Boss2_NewMix_v1.ogg";
            case "BOSS_BEYOND":
                return "STS_Boss3_NewMix_v1.ogg";
            case "BOSS_ENDING":
                return "STS_Boss4_v6.ogg";
            case "ELITE":
                return "STS_EliteBoss_NewMix_v1.ogg";
            case "CREDITS":
                return "STS_Credits_v5.ogg";
            default:
                throw new IllegalArgumentException("Unknown track key: " + key);
        }
    }
    private String getOriginalFileName(String displayName) {
        File customFolder = new File(CUSTOM_MUSIC_FOLDER);
        if (!customFolder.exists()) return null;

        File[] files = customFolder.listFiles();
        if (files == null) return null;

        for (File file : files) {
            String trimmedName = file.getName()
                    .replaceAll("\\.(ogg|mp3|wav)$", "") // Remove extensions
                    .replace("_", " ");                 // Replace underscores with spaces
            if (trimmedName.equals(displayName)) {
                return file.getName(); // Return the exact file name with extension
            }
        }
        return null; // File not found
    }

    private void playTempBgm(String trackName) {
        try {
            // Construct the correct file path
            File file = new File(trackName);
            if (!file.exists()) {
                System.out.println("File not found: " + file.getAbsolutePath());
                return;
            }

            stopCurrentMusic();
            FileHandle fileHandle = Gdx.files.absolute(file.getAbsolutePath());
            nowPlayingSong = Gdx.audio.newMusic(fileHandle);
            nowPlayingSong.setLooping(true);
            nowPlayingSong.setVolume(Settings.MUSIC_VOLUME);
            nowPlayingSong.play();
            System.out.println("Playing custom track: " + file.getAbsolutePath());
        } catch (Exception e) {
            System.err.println("Failed to play music: " + trackName);
            e.printStackTrace();
        }
    }

    public static void stopCurrentMusic() {
        if (nowPlayingSong != null) {
            nowPlayingSong.stop();
            nowPlayingSong.dispose();
            nowPlayingSong = null;
        } else if (isPlaying) {
            CardCrawlGame.music.silenceTempBgmInstantly();
            CardCrawlGame.music.silenceBGM();
            isPlaying = false;
        }
    }

    public static class ScreenEnum {
        @SpireEnum
        public static AbstractDungeon.CurrentScreen JUKEBOX_SCREEN;
    }
}