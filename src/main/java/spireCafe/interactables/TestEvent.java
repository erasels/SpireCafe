package spireCafe.interactables;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.events.RoomEventDialog;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import spireCafe.Anniv7Mod;
import spireCafe.abstracts.*;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

public class TestEvent extends AbstractEvent {

    public static final String ID = Anniv7Mod.makeID(TestEvent.class.getSimpleName());
    ArrayList<AbstractNPC> npcs = new ArrayList<>();

    public TestEvent() {
        this.body = "";
        AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.EVENT;
        this.hasDialog = true;
        this.hasFocus = true;
    }

    @Override
    public void onEnterRoom() {
        // TODO: Fill out the rest of these once we have them implemented
        // TODO: Also handle non-patron NPCs?
        List<Class<? extends AbstractCafeInteractable>> possibleBartenders = getPossibilities(AbstractBartender.class);
        List<Class<? extends AbstractCafeInteractable>> possibleMerchants = getPossibilities(AbstractMerchant.class);
        List<Class<? extends AbstractCafeInteractable>> possiblePatrons = getPossibilities(AbstractPatron.class);
        List<Class<? extends AbstractCafeInteractable>> possibleAttractions = getPossibilities(AbstractAttraction.class);

        int numPatrons = 3;
        Collections.shuffle(possiblePatrons, new java.util.Random(AbstractDungeon.miscRng.randomLong()));
        for (int i = 0; i < numPatrons && i < possiblePatrons.size(); i++) {
            float x = (1000 + i * 200.0f) * Settings.xScale;
            float y = AbstractDungeon.floorY;
            AbstractNPC patron = (AbstractNPC)createInteractable(possiblePatrons.get(i), x, y);
            this.npcs.add(patron);
        }
    }

    private static List<Class<? extends AbstractCafeInteractable>> getPossibilities(Class<? extends AbstractCafeInteractable> clz) {
        //TODO: Track this as part of the save file and pull the previously seen interactables here
        HashSet<String> seenInteractableIDs = new HashSet<>();
        return Anniv7Mod.interactableClasses.entrySet().stream()
                .filter(entry -> clz.isAssignableFrom(entry.getValue()))
                .filter(entry -> !seenInteractableIDs.contains(entry.getKey()))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    private static AbstractCafeInteractable createInteractable(Class<? extends AbstractCafeInteractable> clz, float x, float y) {
        try {
            return clz.getConstructor(float.class, float.class).newInstance(x, y);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update() {
        super.update();
        if (!RoomEventDialog.waitForInput) {
            this.buttonEffect(this.roomEventText.getSelectedOption());
        }
        for (AbstractNPC npc : npcs) {
            npc.update();
        }
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
    }

    @Override
    public void render(SpriteBatch sb) {
        for (AbstractNPC npc : npcs) {
            npc.renderAnimation(sb);
        }
    }
}
