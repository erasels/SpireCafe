package spireCafe.patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.EventRoom;
import com.megacrit.cardcrawl.rooms.TreasureRoomBoss;
import com.megacrit.cardcrawl.ui.buttons.ProceedButton;
import com.megacrit.cardcrawl.vfx.combat.StrikeEffect;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.NotFoundException;
import javassist.expr.ExprEditor;
import javassist.expr.Instanceof;
import javassist.expr.MethodCall;
import spireCafe.interactables.TestEvent;
import spireCafe.util.ActUtil;

public class CafeEntryExitPatch {
    public static int HP_COST_PERCENT = 33;

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

        AbstractDungeon.currMapNode.room = new CafeEventRoom(room);
        AbstractDungeon.getCurrRoom().onPlayerEntry();
        AbstractDungeon.rs = AbstractDungeon.RenderScene.NORMAL;

        AbstractDungeon.combatRewardScreen.clear();
        AbstractDungeon.previousScreen = null;
        AbstractDungeon.closeCurrentScreen();

        AbstractDungeon.fadeOut();
        AbstractDungeon.waitingOnFadeOut = true;
    }

    private static void healBeforeCafe() {
        // Since we want the normal end of act healing to instead take place before entering the cafe, and for a percent
        // of the resulting HP to be deducted as the entry cost to the cafe, we disable the normal end of act healing
        // (see DisableNormalEndOfActHealingPatch) and replicate the logic, with a multiplier to apply the cafe entry cost
        // If the net change is positive, we apply it in a single heal. If the net change is negative, we directly modify
        // the player's current health (bypassing anything that might modify healing, damage, or HP loss).
        float entryCostMultiplier = (100 - HP_COST_PERCENT) / 100.0f;
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
            this.event = new TestEvent();
            this.event.onEnterRoom();
        }
    }
}
