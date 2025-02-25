package spireCafe.interactables.bartenders.drinkbot;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import spireCafe.Anniv7Mod;
import spireCafe.abstracts.AbstractSCRelic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DrinkInserterRelic extends AbstractSCRelic {

    private static final int TURN_COUNTER = 5;
    private static final float PERCENT_HEAL = 0.25F;
    private static final String ID = Anniv7Mod.makeID(DrinkInserterRelic.class.getSimpleName());
    private boolean usedThisCombat = false;


    public DrinkInserterRelic() {
        super(ID, "Drinkbot", RelicTier.SPECIAL, LandingSound.CLINK);
    }

    @Override
    public void onEquip() {
        this.counter = -1;
        resetStats();
    }

    @Override
    public void atPreBattle() {
        this.usedThisCombat = false;
    }

    @Override
    public void atBattleStart() {
        this.counter = 0;
    }

    @Override
    public void onPlayerEndTurn() {
        if (this.usedThisCombat) {
            return;
        }
        if (this.counter < 0) {
            this.counter = TURN_COUNTER;
        } else {
            this.counter++;
        }

        if (this.counter == TURN_COUNTER) {
            flash();
            this.counter = -1;
            this.usedThisCombat = true;
            this.grayscale = true;
            this.pulse = false;

            AbstractPlayer p = AbstractDungeon.player;
            int heal = (int) ((p.maxHealth - p.currentHealth) * PERCENT_HEAL);
            AbstractDungeon.player.heal(heal);
            addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            increaseStats(heal);
        } else if (this.counter == TURN_COUNTER - 1) {
            beginPulse();
            this.pulse = true;
        }
    }

    @Override
    public void justEnteredRoom(AbstractRoom room) {
        this.grayscale = false;
    }

    @Override
    public void onVictory() {
        this.pulse = false;
        this.counter = -1;
    }

    private static final Map<String, Integer> stats = new HashMap<>();
    private static final String HP_GAINED = "hpgained";
    
    private void increaseStats(int heal) {
        stats.put(HP_GAINED, stats.getOrDefault(HP_GAINED, 0) + heal);
    }

    public String getStatsDescription() {
        if (stats.get(HP_GAINED) == null) {
            return "";
        }
        return String.format(DESCRIPTIONS[1], stats.get(HP_GAINED));
    }

    public String getExtendedStatsDescription(int totalCombats, int totalTurns) {
        return getStatsDescription();
    }

    public void resetStats() {
        stats.put(HP_GAINED,0);
    }

    public JsonElement onSaveStats() {
        Gson gson = new Gson();
        List<Integer> statsToSave = new ArrayList<>();
        statsToSave.add(stats.get(HP_GAINED));
        return gson.toJsonTree(statsToSave);
    }

    public void onLoadStats(JsonElement jsonElement) {
        if (jsonElement != null) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            stats.put(HP_GAINED, jsonArray.get(0).getAsInt());
        } else {
            resetStats();
        }
    }
    
}
