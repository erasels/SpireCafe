package spireCafe.util.decorationSystem;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import spireCafe.Anniv7Mod;
import spireCafe.CafeRoom;
import spireCafe.util.TexLoader;

public class BarSignDecoration extends Decoration {
    public BarSignDecoration() {
        super("bar_sign", TexLoader.getTexture(Anniv7Mod.makeUIPath("sign.png")), 0, 0);
        move(950 * Settings.xScale, (882f - img.getHeight()) * Settings.yScale);
    }

    @Override
    public void render(SpriteBatch sb) {
        float renderX = x + shakeOffsetX;
        float renderY = y + shakeOffsetY;

        sb.draw(img, renderX, renderY, width, height);
        FontHelper.renderFontCentered(
                sb,
                FontHelper.buttonLabelFont,
                ((CafeRoom) AbstractDungeon.getCurrRoom().event).bartender.getLabelText(),
                renderX + ((img.getWidth() * Settings.scale) / 2f),
                renderY + ((img.getHeight() / 4f) * Settings.scale),
                Settings.CREAM_COLOR
        );

        hb.render(sb);
    }
}
