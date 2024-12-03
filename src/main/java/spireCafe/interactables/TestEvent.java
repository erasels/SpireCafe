package spireCafe.interactables;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.events.RoomEventDialog;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import spireCafe.Anniv7Mod;
import spireCafe.abstracts.AbstractMerchant;
import spireCafe.abstracts.AbstractNPC;
import spireCafe.interactables.merchants.example.ExampleMerchant;
import spireCafe.interactables.npcs.koishi.KoishiPatron;
import spireCafe.interactables.npcs.example.ExamplePatron;
import spireCafe.interactables.npcs.marisa.MarisaPatron;

import java.util.ArrayList;

public class TestEvent extends AbstractEvent {

    public static final String ID = Anniv7Mod.makeID(TestEvent.class.getSimpleName());
    ArrayList<AbstractNPC> npcs = new ArrayList<>();
    AbstractMerchant merchant;

    public TestEvent() {
        this.body = "";
        AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.EVENT;
        this.hasDialog = true;
        this.hasFocus = true;
        npcs.add(new KoishiPatron(1200.0F * Settings.scale, AbstractDungeon.floorY));
        npcs.add(new MarisaPatron(1400.0F * Settings.scale, AbstractDungeon.floorY));
        npcs.add(new ExamplePatron(1000.0F * Settings.scale, AbstractDungeon.floorY));
        merchant = new ExampleMerchant(1400.0F * Settings.scale, AbstractDungeon.floorY + 400f);
        merchant.rollShop();
    }

    @Override
    public void onEnterRoom() {
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
        merchant.update();
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
    }

    @Override
    public void render(SpriteBatch sb) {
        for (AbstractNPC npc : npcs) {
            npc.renderAnimation(sb);
        }
        merchant.renderAnimation(sb);
    }
}
