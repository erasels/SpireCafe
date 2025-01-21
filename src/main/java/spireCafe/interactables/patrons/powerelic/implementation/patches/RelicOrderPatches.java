package spireCafe.interactables.patrons.powerelic.implementation.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import spireCafe.util.Wiz;

public class RelicOrderPatches {
    //Add a call to reorganizeRelics after every single call to AbstractRelic.obtain and instantObtain.
    //I'm not 110% confident that there are no ill effects from doing so.
    @SpirePatch2(clz = AbstractRelic.class, method = "obtain")
    public static class ObtainPatch{
        @SpirePostfixPatch
        public static void Patch(AbstractRelic __instance){
            //reorganizeRelics has the side effect of snapping a relic to its new slot,
            //effectively cancelling its obtain animation.  So preserve/restore that here.
            float x=__instance.currentX;
            float y=__instance.currentY;
            if(Wiz.adp()!=null)Wiz.adp().reorganizeRelics();
            __instance.currentX=x;
            __instance.currentY=y;
        }
    }
    @SpirePatch2(clz = AbstractRelic.class, method = "instantObtain", paramtypez = {})
    public static class InstantObtainPatch{
        @SpirePostfixPatch
        public static void Patch(){
            if(Wiz.adp()!=null)Wiz.adp().reorganizeRelics();
        }
    }
    @SpirePatch2(clz = AbstractRelic.class, method = "instantObtain", paramtypez = {AbstractPlayer.class, int.class, boolean.class})
    public static class InstantObtainPatch2{
        @SpirePostfixPatch
        public static void Patch(){
            if(Wiz.adp()!=null)Wiz.adp().reorganizeRelics();
        }
    }
}
