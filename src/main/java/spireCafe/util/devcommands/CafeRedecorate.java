package spireCafe.util.devcommands;

import basemod.devcommands.ConsoleCommand;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import spireCafe.CafeRoom;
import spireCafe.patches.CafeEntryExitPatch;

import java.util.ArrayList;

public class CafeRedecorate extends ConsoleCommand {

    public CafeRedecorate() {
        maxExtraTokens = 0;
        minExtraTokens = 0;
        requiresPlayer = true;
        simpleCheck = true;
    }

    @Override
    protected void execute(String[] tokens, int depth) {
        if(CafeEntryExitPatch.inCafe()) {
            ((CafeRoom)AbstractDungeon.getCurrRoom().event).reroll();
        }
    }

    @Override
    protected void errorMsg() {
        Cafe.cmdHelp();
    }

}
