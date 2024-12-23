package spireCafe;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.events.RoomEventDialog;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import spireCafe.abstracts.*;
import spireCafe.interactables.AuthorsNotSetException;
import spireCafe.interactables.NameNotSetException;
import spireCafe.util.TexLoader;
import spireCafe.util.decorationSystem.DecorationSystem;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CafeRoom extends AbstractEvent {
    public static final String ID = Anniv7Mod.makeID(CafeRoom.class.getSimpleName());
    public static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(Anniv7Mod.makeID("CafeUI"));

    public static final int NUM_PATRONS = 3;
    public static boolean isInteracting = false;
    public static float originalPlayerDrawX;
    public static float originalPlayerDrawY;
    // Used for initilizing the cafe with devcommands
    public static String[] devCommandPatrons = new String[CafeRoom.NUM_PATRONS];
    public static String devCommandAttraction = null;
    public static String devCommandMerchant = null;
    public static String devCommandBartender = null;

    private final ArrayList<AbstractNPC> npcs = new ArrayList<>();
    private AbstractMerchant merchant;
    public AbstractBartender bartender;
    private AbstractAttraction attraction;
    private Texture barBackgroundImage, barImg;
    private DecorationSystem decoSystem;

    public boolean darkBg;

    public CafeRoom() {
        this.body = "";
        AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.EVENT;
        this.hasDialog = true;
        this.hasFocus = true;
        darkBg = AbstractDungeon.miscRng.randomBoolean();
        if(darkBg) {
            this.barBackgroundImage = TexLoader.getTexture(Anniv7Mod.makeUIPath("barbackground_dark.png"));
        } else {
            this.barBackgroundImage = TexLoader.getTexture(Anniv7Mod.makeUIPath("barbackground_light.png"));
        }
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

        Class<? extends AbstractCafeInteractable> bartenderClz;
        if (devCommandBartender != null) {
            bartenderClz = Anniv7Mod.interactableClasses.get(devCommandBartender);
        } else {
            Collections.shuffle(possibleBartenders, new java.util.Random(rng.randomLong()));
            bartenderClz = possibleBartenders.get(0);
        }
        this.bartender = (AbstractBartender) createInteractable(bartenderClz, 1650 * Settings.xScale, AbstractDungeon.floorY + 5 * Settings.yScale);
        checkNameAndAuthors(bartender, bartenderClz);
        Anniv7Mod.currentRunSeenInteractables.add(bartender.id);

        //TODO: Fix position logic so overlap is accounted for and prevented
        Collections.shuffle(possiblePatrons, new java.util.Random(rng.randomLong()));
        for (int i = 0; i < NUM_PATRONS && i < possiblePatrons.size(); i++) {
            float x = (1000 + i * 200.0f) * Settings.xScale;
            float y = AbstractDungeon.floorY - 15 * Settings.yScale;
            Class<? extends AbstractCafeInteractable> patronClz;
            if (devCommandPatrons[i] != null){
                patronClz = Anniv7Mod.interactableClasses.get(devCommandPatrons[i]);
            } else {
                patronClz = possiblePatrons.get(i);
            }
            AbstractNPC patron = (AbstractNPC) createInteractable(patronClz, x, y);
            checkNameAndAuthors(patron, patronClz);
            this.npcs.add(patron);
            Anniv7Mod.currentRunSeenInteractables.add(patron.id);
        }

        Class<? extends AbstractCafeInteractable> attractionClz;
        if (devCommandAttraction != null){
            attractionClz = Anniv7Mod.interactableClasses.get(devCommandAttraction);
        } else {
            Collections.shuffle(possibleAttractions, new java.util.Random(rng.randomLong()));
            attractionClz = possibleAttractions.get(0);
        }
        this.attraction = (AbstractAttraction) createInteractable(attractionClz, 600 * Settings.xScale, AbstractDungeon.floorY - 15 * Settings.yScale);
        checkNameAndAuthors(attraction, attractionClz);
        Anniv7Mod.currentRunSeenInteractables.add(attraction.id);

        Class<? extends AbstractCafeInteractable> merchantClz;
        if (devCommandMerchant != null){
            merchantClz = Anniv7Mod.interactableClasses.get(devCommandMerchant);
        } else {
            Collections.shuffle(possibleMerchants, new java.util.Random(rng.randomLong()));
            merchantClz = possibleMerchants.get(0);
        }
        this.merchant = (AbstractMerchant) createInteractable(merchantClz, 200 * Settings.xScale, AbstractDungeon.floorY - 15 * Settings.yScale);
        checkNameAndAuthors(merchant, merchantClz);
        merchant.initialize();
        Anniv7Mod.currentRunSeenInteractables.add(merchant.id);
        decoSystem = new DecorationSystem();
    }

    @Override
    public void update() {
        super.update();
        if (!RoomEventDialog.waitForInput) {
            this.buttonEffect(this.roomEventText.getSelectedOption());
        }
        //Updating NPCs first for the isInteracting flag to be set correctly. Baartender's hitbox overlaps
        for (AbstractNPC npc : npcs) {
            npc.update();
        }
        bartender.update();
        attraction.update();
        merchant.update();
        decoSystem.update();
        isInteracting = false;
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
    }

    private void checkNameAndAuthors(AbstractCafeInteractable interactable, Class<? extends AbstractCafeInteractable> interactableClz){
        if(interactable.name == null || interactable.name.isEmpty()){
            throw new NameNotSetException(interactableClz);
        }
        if(interactable.authors == null || interactable.authors.isEmpty()){
            throw new AuthorsNotSetException(interactableClz);
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(Color.WHITE);
        sb.setBlendFunction(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
        sb.draw(barBackgroundImage, 0, 0, Settings.WIDTH, Settings.HEIGHT);
        decoSystem.render(sb);
        bartender.renderAnimation(sb);
        //draw bar
        sb.draw(this.barImg, 800 * Settings.xScale, AbstractDungeon.floorY, (float) this.barImg.getWidth()  * 2.4f * Settings.scale, (float) this.barImg.getHeight() * 1f * Settings.scale);

        for (AbstractNPC npc : npcs) {
            npc.renderAnimation(sb);
        }
        attraction.renderAnimation(sb);
        merchant.renderAnimation(sb);
        sb.draw(AbstractDungeon.player.shoulder2Img, 0.0F, 0.0F, 1920.0F / 2.2f * Settings.scale, 1136.0F / 2.2f * Settings.scale);
    }

    // Use this to get everyone that's currently in the café
    public List<AbstractCafeInteractable> getCurrentInhabitants() {
        List<AbstractCafeInteractable> inhabitants = new ArrayList<>();
        inhabitants.add(bartender);
        inhabitants.addAll(npcs);
        inhabitants.add(attraction);
        inhabitants.add(merchant);
        return inhabitants;
    }

    @Override
    public void dispose() {
        super.dispose();
        decoSystem.dispose();
    }

    //Only redecorates for now, but could be expanded for a dev command
    public void reroll() {
        decoSystem.redecorate();
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
