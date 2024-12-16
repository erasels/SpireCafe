package spireCafe;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.events.RoomEventDialog;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import spireCafe.abstracts.*;
import spireCafe.util.TexLoader;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CafeRoom extends AbstractEvent {
    public static final String ID = Anniv7Mod.makeID(CafeRoom.class.getSimpleName());
    public static final int NUM_PATRONS = 3;
    private final ArrayList<AbstractNPC> npcs = new ArrayList<>();
    private AbstractMerchant merchant;
    private AbstractBartender bartender;
    private AbstractAttraction attraction;
    private Texture barBackgroundImage;
    private Texture barImg;
    public static float originalPlayerDrawX;
    public static float originalPlayerDrawY;
    // Used for initilizing the cafe with devcommands
    public static String[] devCommandPatrons = new String[CafeRoom.NUM_PATRONS];

    public CafeRoom() {
        this.body = "";
        AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.EVENT;
        this.hasDialog = true;
        this.hasFocus = true;
        this.barBackgroundImage = TexLoader.getTexture(Anniv7Mod.makeUIPath("barbackground.jpg"));
        this.barImg = TexLoader.getTexture(Anniv7Mod.makeUIPath("bar.png"));
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
    public void onEnterRoom() {
        //Move Combat Sprite off screen
        originalPlayerDrawX = AbstractDungeon.player.drawX;
        originalPlayerDrawY = AbstractDungeon.player.drawY;
        AbstractDungeon.player.drawX = -9000.0f;
        AbstractDungeon.player.drawY = -9000.0f;

        // 1 Bartender, 1 Merchant, ~3 total patrons and attractions (at least one of each)
        // To prevent running out of interactables on endless, if any of the possible lists are empty or the amount of
        // patrons/attractions isn't enough, we should clear  Anniv7Mod.currentRunSeenInteractables and try again
        // (But doing that needs to wait until we have enough of everything for a single run to not see duplicates)
        com.megacrit.cardcrawl.random.Random rng = AbstractDungeon.miscRng;
        List<Class<? extends AbstractCafeInteractable>> possibleBartenders = getPossibilities(AbstractBartender.class);
        List<Class<? extends AbstractCafeInteractable>> possibleMerchants = getPossibilities(AbstractMerchant.class);
        List<Class<? extends AbstractCafeInteractable>> possiblePatrons = getPossibilities(AbstractPatron.class);
        List<Class<? extends AbstractCafeInteractable>> possibleAttractions = getPossibilities(AbstractAttraction.class);

        Collections.shuffle(possibleBartenders, new java.util.Random(rng.randomLong()));
        this.bartender = (AbstractBartender) createInteractable(possibleBartenders.get(0), 1200 * Settings.xScale, AbstractDungeon.floorY + 100 * Settings.yScale);
        Anniv7Mod.currentRunSeenInteractables.add(bartender.id);

        //TODO: Fix position logic so overlap is accounted for and prevented
        Collections.shuffle(possiblePatrons, new java.util.Random(rng.randomLong()));
        for (int i = 0; i < NUM_PATRONS && i < possiblePatrons.size(); i++) {
            float x = (1000 + i * 200.0f) * Settings.xScale;
            float y = AbstractDungeon.floorY;
            AbstractNPC patron;
            if (devCommandPatrons[i] != null){
                patron = (AbstractNPC) createInteractable(Anniv7Mod.interactableClasses.get(devCommandPatrons[i]), x, y);
            } else {
                patron = (AbstractNPC) createInteractable(possiblePatrons.get(i), x, y);
            }
            this.npcs.add(patron);
            Anniv7Mod.currentRunSeenInteractables.add(patron.id);
        }

        Collections.shuffle(possibleAttractions, new java.util.Random(rng.randomLong()));
        this.attraction = (AbstractAttraction) createInteractable(possibleAttractions.get(0), 600 * Settings.xScale, AbstractDungeon.floorY);
        Anniv7Mod.currentRunSeenInteractables.add(attraction.id);

        Collections.shuffle(possibleMerchants, new java.util.Random(rng.randomLong()));
        this.merchant = (AbstractMerchant) createInteractable(possibleMerchants.get(0), 200 * Settings.xScale, AbstractDungeon.floorY);
        merchant.initialize();
        Anniv7Mod.currentRunSeenInteractables.add(merchant.id);
    }

    @Override
    public void update() {
        super.update();
        if (!RoomEventDialog.waitForInput) {
            this.buttonEffect(this.roomEventText.getSelectedOption());
        }
        bartender.update();
        for (AbstractNPC npc : npcs) {
            npc.update();
        }
        attraction.update();
        merchant.update();
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(Color.WHITE);
        sb.setBlendFunction(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
        sb.draw(barBackgroundImage, 0, 0, Settings.WIDTH, Settings.HEIGHT);
        bartender.renderAnimation(sb);
        sb.draw(this.barImg, 800 * Settings.xScale, AbstractDungeon.floorY, (float) this.barImg.getWidth() * 2 * Settings.scale, (float) this.barImg.getHeight() * 2 * Settings.scale);
        for (AbstractNPC npc : npcs) {
            npc.renderAnimation(sb);
        }
        attraction.renderAnimation(sb);
        merchant.renderAnimation(sb);
        sb.draw(AbstractDungeon.player.shoulder2Img, 0.0F, 0.0F, 1920.0F / 2 * Settings.scale, 1136.0F / 2 * Settings.scale);
    }


    //Remove Event Text Shadow
    @Override
    public void updateDialog() {
    }

    @Override
    public void renderText(SpriteBatch sb) {
    }

    @Override
    public void renderRoomEventPanel(SpriteBatch sb) {
    }
}
