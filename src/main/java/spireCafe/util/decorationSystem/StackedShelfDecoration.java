package spireCafe.util.decorationSystem;

import com.megacrit.cardcrawl.core.Settings;
import spireCafe.Anniv7Mod;
import spireCafe.util.TexLoader;

public class StackedShelfDecoration extends Decoration {
    public StackedShelfDecoration() {
        super("stacked_shelf", TexLoader.getTexture(Anniv7Mod.makeUIPath("decoration/stacked_shelf.png")), 0, 0);
        fixedY = 630f * Settings.scale;
    }
}
