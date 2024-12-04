package spireCafe.interactables;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.events.RoomEventDialog;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import spireCafe.Anniv7Mod;
import spireCafe.abstracts.*;
import spireCafe.abstracts.AbstractMerchant;
import spireCafe.abstracts.AbstractNPC;
import spireCafe.interactables.merchants.example.ExampleMerchant;
import spireCafe.interactables.npcs.koishi.KoishiPatron;
import spireCafe.interactables.npcs.example.ExamplePatron;
import spireCafe.interactables.npcs.marisa.MarisaPatron;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

public class TestEvent extends AbstractEvent {
    public static final String ID = Anniv7Mod.makeID(TestEvent.class.getSimpleName());
    private final ArrayList<AbstractNPC> npcs = new ArrayList<>();

    public TestEvent() {
        this.body = "";
        AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.EVENT;
        this.hasDialog = true;
        this.hasFocus = true;
    }

    @Override
    public void onEnterRoom() {
        // TODO: Fill out the rest of these once we have them implemented
        // 1 Bartender, 1 Merchant, ~3 total patrons and attractions (at least one of each)
        // To prevent running out of interactables on endless, if any of the possible lists are empty or the amount of
        // patrons/attractions isn't enough, we should clear  Anniv7Mod.currentRunSeenInteractables and try again
        // (But doing that needs to wait until we have enough of everything for a single run to not see duplicates)
        com.megacrit.cardcrawl.random.Random rng = AbstractDungeon.miscRng;
        List<Class<? extends AbstractCafeInteractable>> possibleBartenders = getPossibilities(AbstractBartender.class);
        List<Class<? extends AbstractCafeInteractable>> possibleMerchants = getPossibilities(AbstractMerchant.class);
        List<Class<? extends AbstractCafeInteractable>> possiblePatrons = getPossibilities(AbstractPatron.class);
        List<Class<? extends AbstractCafeInteractable>> possibleAttractions = getPossibilities(AbstractAttraction.class);

        int numPatrons = 3;
        Collections.shuffle(possiblePatrons, new java.util.Random(rng.randomLong()));
        for (int i = 0; i < numPatrons && i < possiblePatrons.size(); i++) {
            float x = (1000 + i * 200.0f) * Settings.xScale;
            float y = AbstractDungeon.floorY;
            AbstractNPC patron = (AbstractNPC)createInteractable(possiblePatrons.get(i), x, y);
            this.npcs.add(patron);
            Anniv7Mod.currentRunSeenInteractables.add(patron.id);
        }
    }

    private static List<Class<? extends AbstractCafeInteractable>> getPossibilities(Class<? extends AbstractCafeInteractable> clz) {
        return Anniv7Mod.interactableClasses.entrySet().stream()
                .filter(entry -> clz.isAssignableFrom(entry.getValue()))
                .filter(entry -> !Anniv7Mod.currentRunSeenInteractables.contains(entry.getKey()))
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
