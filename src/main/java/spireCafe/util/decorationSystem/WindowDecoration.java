package spireCafe.util.decorationSystem;

import com.megacrit.cardcrawl.core.Settings;
import spireCafe.Anniv7Mod;
import spireCafe.util.TexLoader;

public class WindowDecoration extends Decoration {
    public WindowDecoration(String imgName) {
        super(imgName, TexLoader.getTexture(Anniv7Mod.makeUIPath("decoration/"+imgName+".png")), 0, 0);
        fixedY = 594 * Settings.scale;
        clickSound = "POTION_DROP_2";
    }
}
