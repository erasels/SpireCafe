package spireCafe.interactables.bartenders.Temmie;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import spireCafe.Anniv7Mod;
import spireCafe.abstracts.AbstractBartender;
import spireCafe.abstracts.BartenderCutscene;
import spireCafe.interactables.attractions.jukebox.JukeboxRelic;
import spireCafe.util.TexLoader;
import spireCafe.util.Wiz;
import spireCafe.util.cutsceneStrings.CutsceneStrings;
import spireCafe.util.cutsceneStrings.LocalizedCutsceneStrings;

import java.util.List;

public class TemmieBartender extends AbstractBartender {
    private long lastClickTime = 0;
    private static final long COOLDOWN_MS = 3000;

    public static final String ID = TemmieBartender.class.getSimpleName();
    private static final CharacterStrings characterStrings = CardCrawlGame.languagePack.getCharacterString(Anniv7Mod.makeID(ID));
    private static final CutsceneStrings cutsceneStrings = LocalizedCutsceneStrings.getCutsceneStrings(Anniv7Mod.makeID(ID + "Cutscene"));

    public static final String TEMMIE_VOICE = Anniv7Mod.makeID("temmie_voice2.mp3");
    private static final int FLAKE_BASE_COST = 1;
    private static final int COLLEGE_FUND_COST = 10;
    private static final int COLLEGE_GOAL = 500; // Total gold required to send Temmie to college

    int totalDonations = 0; // Tracks total gold donated in the current run
    private boolean readyForArmor = false; // Becomes true when the donation goal is reached
    boolean hasReceivedArmor = false; // Becomes true when armor is given
    private boolean isCutsceneActive = false;

    public TemmieBartender(float animationX, float animationY) {
        super(animationX, animationY, 180.0f, 250.0f);
        name = "Bartender Temmie";
        this.authors = "Darklight";
        this.img = TexLoader.getTexture(Anniv7Mod.makeBartenderPath("temmie/Temmie.png"));
        this.cutscenePortrait = new TextureRegion(TexLoader.getTexture(Anniv7Mod.makeBartenderPath("temmie/Portrait.png")));
    }

    /** Handles interaction with Temmie. */
    @Override
    public void onInteract() {
        System.out.println("Interacting with Temmie Bartender");
        playTemmie();

        if (!isCutsceneActive) {
            openTemmieShop();
        }
    }
    /** Returns the "No Thanks" option description. */
    @Override
    public String getNoThanksDescription() {
        playTemmie();
        return "No Thanks";
    }

    /** Handles healing, deducting gold, and keeping the options available. */
    @Override
    public void applyHealAction() {
        playTemmie();
        Wiz.p().heal(getHealAmount());
        Wiz.p().loseGold(getFlakeCost()); // Ensures flake price updates each time
        checkIfCutsceneShouldEnd(); // Check if we should exit
    }


    /** Handles donation logic, upgrading to "Send Temmie to College" when 500g is reached. */
    @Override
    public void applySecondOptionAction() {
        playTemmie();
        if (!readyForArmor) {
            if (Wiz.p().gold >= COLLEGE_FUND_COST) {
                Wiz.p().loseGold(COLLEGE_FUND_COST);
                totalDonations += COLLEGE_FUND_COST;
            }

            if (totalDonations >= COLLEGE_GOAL) {
                readyForArmor = true;
            }

            // Ensures the cutscene stays active until armor is awarded
            secondUsed = totalDonations < COLLEGE_GOAL;
        } else {
            giveTemmieArmor();
            secondUsed = true; // Hide donation after armor is awarded
        }
        checkIfCutsceneShouldEnd();

    }

    private void checkIfCutsceneShouldEnd() {
        boolean healAvailable = getHealOptionCondition();
        boolean donationAvailable = getSecondOptionCondition();
        if (!healAvailable && !donationAvailable) {
            if (!isCutsceneActive) return; // Prevents multiple exits
            endTemmieCutscene();
        }
    }
    private void endTemmieCutscene() {
        AbstractDungeon.topLevelEffectsQueue.removeIf(effect -> effect instanceof TemmieBartenderCutscene);
        AbstractDungeon.overlayMenu.cancelButton.show("Return");

        // Ensure no orphaned UI states remain
        AbstractDungeon.dynamicBanner.hide();
        AbstractDungeon.effectList.clear();

        isCutsceneActive = false;
    }


    /** Returns the dynamically updated heal option description. */
    @Override
    public String getHealOptionDescription() {
        return String.format(cutsceneStrings.OPTIONS[1], getHealAmount(), getFlakeCost());
    }

    /** Ensures the heal option is available only if the player has enough gold. */
    @Override
    public boolean getHealOptionCondition() {
        return Wiz.p().gold >= getFlakeCost();
    }

    /** Dynamically calculates the heal amount, increasing with each donation. */
    @Override
    public int getHealAmount() {
        return 10 + (totalDonations / COLLEGE_FUND_COST); // Increases as donations grow
    }

    /** Dynamically adjusts the cost of Flakes based on donations. */
    public int getFlakeCost() {
        return FLAKE_BASE_COST + (totalDonations / COLLEGE_FUND_COST); // Increases every 10 donations
    }

    /** Returns the dynamically updated college donation option. */
    @Override
    public String getSecondOptionDescription() {
        if (hasReceivedArmor) {
            return ""; // Hide the option once Temmie has gone to college
        } else if (readyForArmor) {
            return "Send Temmie to College (Gain Temmie Armor)";
        } else {
            return String.format("Donate to College Fund (%d Gold) [%d/%d]", COLLEGE_FUND_COST, totalDonations, COLLEGE_GOAL);
        }
    }

    /** Ensures donation option is available unless Temmie has gone to college. */
    @Override
    public boolean getSecondOptionCondition() {
        return !hasReceivedArmor && Wiz.p().gold >= COLLEGE_FUND_COST;
    }

    /** Required method for card selection in donations (unused for Temmie). */
    @Override
    public void doForSelectedCardsFromSecondAction(List<AbstractCard> selected) {}

    /** Returns the bartender's label text. */
    @Override
    public String getLabelText() {
        return characterStrings.TEXT[0];
    }

    /** Opens the Temmie shop by starting the bartender cutscene. */
    private void openTemmieShop() {
        // Clear any existing Temmie cutscene to prevent text stacking
        AbstractDungeon.topLevelEffectsQueue.removeIf(effect -> effect instanceof TemmieBartenderCutscene);

        // Now add the new cutscene
        AbstractDungeon.topLevelEffectsQueue.add(new TemmieBartenderCutscene(this, cutsceneStrings));
    }



    /** Plays the Temmie sound effect with a cooldown to prevent overlap. */
    public void playTemmie() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastClickTime >= COOLDOWN_MS) {
            CardCrawlGame.sound.play(TEMMIE_VOICE);
            lastClickTime = currentTime;
        }
    }

    /** Awards the Temmie Armor relic when the player clicks "Send Temmie to College". */
    private void giveTemmieArmor() {
        hasReceivedArmor = true; // Prevents additional armor from being awarded
        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(
                (float) (Settings.WIDTH / 2),
                (float) (Settings.HEIGHT / 2),
                new JukeboxRelic());

        System.out.println("Temmie has gone to college! Player received Temmie Armor.");
    }
}
