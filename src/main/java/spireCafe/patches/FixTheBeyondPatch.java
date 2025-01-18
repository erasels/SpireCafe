package spireCafe.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.TheBeyond;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.rooms.EmptyRoom;

import java.util.ArrayList;

@SpirePatch(clz = TheBeyond.class, method = SpirePatch.CONSTRUCTOR, paramtypez = { AbstractPlayer.class, ArrayList.class } )
public class FixTheBeyondPatch {
    @SpirePostfixPatch
    public static void fixTheBeyond(TheBeyond __instance, AbstractPlayer p, ArrayList<String> theList) {
        // Both TheCity and custom ActLikeIt acts (https://github.com/a-personal-account/ActLikeIt/blob/master/src/main/java/actlikeit/dungeons/CustomDungeon.java#L106)
        // set the current map node to an empty room when they're instantiated. This behavior affects several things in
        // the Cafe, specifically the logic for leaving it (which checks the current room) and the UI after leaving (this
        // is what makes the cafe stop rendering the background and keeps things like the proceed button hidden).
        // We want all this to work consistently, so here we patch TheBeyond to work like other acts do.
        AbstractDungeon.currMapNode = new MapRoomNode(0, -1);
        AbstractDungeon.currMapNode.room = new EmptyRoom();
    }
}
