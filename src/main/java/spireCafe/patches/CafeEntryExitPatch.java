package spireCafe.patches;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.EventRoom;
import com.megacrit.cardcrawl.rooms.TreasureRoomBoss;
import com.megacrit.cardcrawl.scenes.AbstractScene;
import com.megacrit.cardcrawl.ui.buttons.ProceedButton;
import com.megacrit.cardcrawl.vfx.combat.StrikeEffect;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.NotFoundException;
import javassist.expr.ExprEditor;
import javassist.expr.Instanceof;
import javassist.expr.MethodCall;
import spireCafe.Anniv7Mod;
import spireCafe.CafeRoom;
import spireCafe.scene.CafeScene;
import spireCafe.util.ActUtil;

import java.io.IOException;

import static spireCafe.Anniv7Mod.allTimeSeenInteractables;
import static spireCafe.Anniv7Mod.makeID;

public class CafeEntryExitPatch {
    public static final String CAFE_ENTRY_SOUND_KEY = makeID("CafeEntry");
    public static int HP_COST_PERCENT = 33;
    private static AbstractScene originalScene;

    @SpirePatch(clz = ProceedButton.class, method = "goToNextDungeon")
    public static class CafeEntryNormal {
        @SpireInsertPatch(locator = Locator.class)
        public static SpireReturn<Void> cafeEntry(ProceedButton __instance, AbstractRoom room) {
            if (!Loader.isModLoaded("actlikeit") && shouldEnterCafe()) {
                enterCafe(room);
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractDungeon.class, "fadeOut");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch(cls = "actlikeit.patches.GoToNextDungeonPatch", method = "Insert", requiredModId = "actlikeit")
    public static class CafeEntryActLikeIt {
        @SpirePrefixPatch
        public static SpireReturn<SpireReturn<Void>> cafeEntry(ProceedButton __instance, AbstractRoom room) {
            if (shouldEnterCafe()) {
                enterCafe(room);
                return SpireReturn.Return(SpireReturn.Return());
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = ProceedButton.class, method = "update")
    public static class CafeExitPatch {
        @SpireInstrumentPatch
        public static ExprEditor cafeExit () {
            return new ExprEditor() {
                public void edit(Instanceof io) throws CannotCompileException {
                    try {
                        if (io.getType().getName().equals(TreasureRoomBoss.class.getName())) {
                            io.replace(String.format("{ $_ = $proceed($$) || %1$s.inCafe(); }", CafeEntryExitPatch.class.getName()));
                        }
                    } catch (NotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }

                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getClassName().equals(ProceedButton.class.getName()) && m.getMethodName().equals("goToNextDungeon")) {
                        m.replace(String.format("{ if(%1$s.inCafe()) { $proceed(%1$s.getOriginalRoom()); } else { $proceed($$); } }", CafeEntryExitPatch.class.getName()));
                    }
                }
            };
        }
    }

    @SpirePatch(clz = AbstractDungeon.class, method = "closeCurrentScreen")
    public static class CafeShowProceedButtonPatch {
        @SpirePostfixPatch
        public static void showProceedButton() {
            // Many screens (e.g. GridCardSelectScreen) hide the proceed button when opened, so we make it visible again
            if (inCafe()) {
                AbstractDungeon.overlayMenu.proceedButton.show();
            }
        }
    }

    @SpirePatch(clz = ProceedButton.class, method = "goToNextDungeon")
    public static class CafeExitMarkCompletePatch {
        @SpirePrefixPatch
        public static void markComplete(ProceedButton proceedButton) {
            if (inCafe()) {
                AbstractDungeon.currMapNode.room.phase = AbstractRoom.RoomPhase.COMPLETE;
                AbstractDungeon.player.drawX = CafeRoom.originalPlayerDrawX;
                AbstractDungeon.player.drawY = CafeRoom.originalPlayerDrawY;
                AbstractDungeon.scene = originalScene;

                AbstractDungeon.currMapNode.room.event.dispose();

                modifyProceedButton(ReflectionHacks.getPrivateStatic(ProceedButton.class, "DRAW_Y"), true);

                if(allTimeSeenInteractables != null) {
                    try {
                        Anniv7Mod.saveSeenInteractables(allTimeSeenInteractables);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    @SpirePatch(clz = AbstractDungeon.class, method = "updateFading")
    public static class CafeDontGoToNextRoomAfterFadeOutPatch {
        @SpireInstrumentPatch
        public static ExprEditor dontGoToNextRoomAfterFadeOut () {
            return new ExprEditor() {
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getClassName().equals(AbstractDungeon.class.getName()) && m.getMethodName().equals("nextRoomTransition")) {
                        m.replace(String.format("{ if(!%1$s.inCafe()) { $proceed($$); } }", CafeEntryExitPatch.class.getName()));
                    }
                }
            };
        }
    }

    public static boolean inCafe() {
        return AbstractDungeon.currMapNode != null && AbstractDungeon.currMapNode.room instanceof CafeEventRoom;
    }

    public static AbstractRoom getOriginalRoom() {
        return inCafe() ? ((CafeEventRoom)AbstractDungeon.currMapNode.room).originalRoom : null;
    }

    private static boolean shouldEnterCafe() {
        return !inCafe() && ActUtil.getRealActNum() <= 2;
    }

    private static void enterCafe(AbstractRoom room) {
        healBeforeCafe();
        originalScene = AbstractDungeon.scene;
        AbstractDungeon.scene = new CafeScene();
        AbstractDungeon.currMapNode.room = new CafeEventRoom(room);
        AbstractDungeon.getCurrRoom().onPlayerEntry();
        CardCrawlGame.sound.play(CAFE_ENTRY_SOUND_KEY);
        AbstractDungeon.rs = AbstractDungeon.RenderScene.NORMAL;

        modifyProceedButton(120f * Settings.scale, false);

        AbstractDungeon.combatRewardScreen.clear();
        AbstractDungeon.previousScreen = null;
        AbstractDungeon.closeCurrentScreen();

        AbstractDungeon.fadeOut();
        setFadeTimer();
        AbstractDungeon.waitingOnFadeOut = true;

    }

    private static void healBeforeCafe() {
        // Since we want the normal end of act healing to instead take place before entering the cafe, and for a percent
        // of the resulting HP to be deducted as the entry cost to the cafe, we disable the normal end of act healing
        // (see DisableNormalEndOfActHealingPatch) and replicate the logic, with a multiplier to apply the cafe entry cost
        // If the net change is positive, we apply it in a single heal. If the net change is negative, we directly modify
        // the player's current health (bypassing anything that might modify healing, damage, or HP loss).
        float entryCostMultiplier = Anniv7Mod.getCafeEntryCostConfig() ? (100 - HP_COST_PERCENT) / 100.0f : 1.0f;
        float normalHealAmount = (AbstractDungeon.ascensionLevel >= 5 ? 0.75f : 1.0f) * (AbstractDungeon.player.maxHealth - AbstractDungeon.player.currentHealth);
        int healthChange = (int)((entryCostMultiplier * (AbstractDungeon.player.currentHealth + normalHealAmount)) - AbstractDungeon.player.currentHealth);
        if (healthChange > 0) {
            AbstractDungeon.player.heal(healthChange);
        }
        else {
            AbstractDungeon.player.currentHealth += healthChange;
            AbstractDungeon.effectList.add(new StrikeEffect(AbstractDungeon.player, AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, -healthChange));
        }
    }

    private static void setFadeTimer() {
        // Normally fast mode makes the fade out/fade in animation nearly instant (0.001 seconds), which looks slightly
        // odd with the transition to the cafe. We make things a bit less jarring by giving it a longer animation time
        // than normal (though still substantially shorter than the 0.8 seconds when not in fast mode).
        if (Settings.FAST_MODE) {
            ReflectionHacks.setPrivateStatic(AbstractDungeon.class, "fadeTimer", 0.1f);
        }
    }

    private static class CafeEventRoom extends EventRoom {
        public AbstractRoom originalRoom;
        private boolean startedFadeIn;

        public CafeEventRoom(AbstractRoom originalRoom) {
            this.originalRoom = originalRoom;
        }

        @Override
        public void render(SpriteBatch sb) {
            // We continue to render the original room until the fade out finishes to avoid a jarring transition
            if (!this.startedFadeIn) {
                this.originalRoom.render(sb);
            }
            else {
                super.render(sb);
            }
        }

        @Override
        public void update() {
            // We continue to render the original room until the fade out finishes to avoid a jarring transition
            if (!this.startedFadeIn) {
                if (!AbstractDungeon.isFadingOut) {
                    AbstractDungeon.fadeIn();
                    setFadeTimer();
                    startedFadeIn = true;
                }
                this.originalRoom.update();
            }
            else {
                super.update();
            }
        }

        @Override
        public void onPlayerEntry() {
            this.event = new CafeRoom();
            this.event.onEnterRoom();
        }
    }

    private static void modifyProceedButton(float position, boolean resetLabel) {
        ProceedButton btn = AbstractDungeon.overlayMenu.proceedButton;

        ReflectionHacks.setPrivate(btn, ProceedButton.class, "current_y", position);
        Hitbox btnHb = ReflectionHacks.getPrivate(btn, ProceedButton.class, "hb");
        btnHb.move(ReflectionHacks.getPrivate(btn, ProceedButton.class, "target_x"), position);

        if(resetLabel) {
            btn.setLabel(ProceedButton.TEXT[0]);
        } else {
            btn.setLabel(CafeRoom.uiStrings.TEXT[1]);
        }
    }
}
