package spireCafe.interactables.bartenders.drinkbot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import spireCafe.Anniv7Mod;
import spireCafe.abstracts.AbstractSCRelic;

public class DrinkInserterRelic extends AbstractSCRelic {

    private static final int TURN_COUNTER = 7;
    private static final float PERCENT_HEAL = 0.2F;
    private static final String ID = Anniv7Mod.makeID(DrinkInserterRelic.class.getSimpleName());


    public DrinkInserterRelic() {
        super(ID, "Drinkbot", RelicTier.SPECIAL, LandingSound.CLINK);
    }

    @Override
    public void onEquip() {
        this.counter = 0;
        resetStats();
    }

    @Override
    public void atTurnStart() {
        if (this.counter < 0) {
            this.counter = TURN_COUNTER;
        } else {
            this.counter++;
        }

        if (this.counter == TURN_COUNTER) {
            this.counter = 0;
            flash();
            AbstractPlayer p = AbstractDungeon.player;
            int heal = (int) ((p.maxHealth - p.currentHealth) * PERCENT_HEAL);
            AbstractDungeon.player.heal(heal);
            addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            increaseStats(heal);
        }
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
