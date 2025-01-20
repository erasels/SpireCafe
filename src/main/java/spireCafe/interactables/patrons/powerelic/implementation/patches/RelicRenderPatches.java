package spireCafe.interactables.patrons.powerelic.implementation.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.GameCursor;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import spireCafe.Anniv7Mod;
import spireCafe.interactables.patrons.powerelic.implementation.PowerelicCard;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;

public class RelicRenderPatches {
    @SpirePatch(clz = AbstractRelic.class, method = "renderInTopPanel")
    public static class RenderPatch {
        @SpirePrefixPatch
        public static SpireReturn<Void> Patch(AbstractRelic __instance) {
            if(PowerelicCard.PowerelicRelicContainmentFields.isContained.get(__instance)){
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
    }
    @SpirePatch(clz = AbstractRelic.class, method = "renderTip")
    public static class RenderTipPatch {
        @SpirePrefixPatch
        public static SpireReturn<Void> Patch(AbstractRelic __instance) {
            if(PowerelicCard.PowerelicRelicContainmentFields.isContained.get(__instance)){
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
    }
    @SpirePatch(clz=AbstractRelic.class,method="update")
    public static class HideMagnifyingCursorPatch{
        @SpireInstrumentPatch
        public static ExprEditor Bar() {
            return new ExprEditor() {
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getClassName().equals(GameCursor.class.getName()) && m.getMethodName().equals("changeType")) {
                        m.replace("{ if(!"+RelicContainmentDetection.class.getName()+".isContained(this)) { $proceed($$); } }");
                    }
                }
            };
        }
    }
    @SpirePatch(clz=AbstractRelic.class,method="updateRelicPopupClick")
    public static class DisableClickPatch{
        @SpirePrefixPatch
        public static SpireReturn<Void> Patch(AbstractRelic __instance) {
            if(PowerelicCard.PowerelicRelicContainmentFields.isContained.get(__instance)){
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
    }


    public static void moveInvisibleRelicsToEnd(AbstractPlayer __instance){
        ArrayList<AbstractRelic> invisibleRelics = new ArrayList<>();
        for(AbstractRelic relic : __instance.relics){
            if(RelicContainmentDetection.isContained(relic)){
                invisibleRelics.add(relic);
            }
        }
        __instance.relics.removeIf(relic -> RelicContainmentDetection.isContained(relic));
        __instance.relics.addAll(invisibleRelics);
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "reorganizeRelics")
    public static class ReorganizePrefix {
        @SpirePrefixPatch
        public static void Patch(AbstractPlayer __instance) {
            try {
                moveInvisibleRelicsToEnd(__instance);
            }catch(ConcurrentModificationException e){
                Anniv7Mod.logger.info("RelicRenderPatches: comodexception while trying to rearrange relics.");
            }
        }
    }

    //later: investigate ways to autoreorganize as soon as a relic is picked up
    //  -- at the moment, we're scared of possible side effects



}
