package spireCafe.util.decorationSystem;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import spireCafe.Anniv7Mod;
import spireCafe.CafeRoom;
import spireCafe.util.TexLoader;

public class BarSignDecoration extends Decoration {
    private static final Texture left = TexLoader.getTexture(Anniv7Mod.makeUIPath("sign_left.png"));
    private static final Texture right = TexLoader.getTexture(Anniv7Mod.makeUIPath("sign_right.png"));
    private static final Texture middle = TexLoader.getTexture(Anniv7Mod.makeUIPath("sign_middle.png"));

    private float textWidth = -1f;

    public BarSignDecoration() {
        super("bar_sign", TexLoader.getTexture(Anniv7Mod.makeUIPath("sign.png")), 0, 0);
        move(950 * Settings.xScale, (882f - img.getHeight()) * Settings.yScale);
    }

    @Override
    public void update()
    {
        if (textWidth < 0) {
            textWidth = FontHelper.getWidth(FontHelper.buttonLabelFont, getText(), 1f);
            hb.width = textWidth + 64 * Settings.scale;
            width = hb.width;
            hb.move(hb.cX, hb.cY);
            x = hb.x;
            y = hb.y;
        }

        super.update();
    }

    @Override
    public void render(SpriteBatch sb) {
        float renderX = x + shakeOffsetX;
        float renderY = y + shakeOffsetY;

        float middleWidth = width - (left.getWidth() + right.getWidth()) * Settings.scale;
        sb.draw(
                left,
                renderX,
                renderY,
                left.getWidth() * Settings.scale,
                left.getHeight() * Settings.scale
        );
        sb.draw(
                right,
                renderX + width - right.getWidth() * Settings.scale,
                renderY,
                right.getWidth() * Settings.scale,
                right.getHeight() * Settings.scale
        );
        sb.draw(
                middle,
                renderX + left.getWidth() * Settings.scale,
                renderY,
                middleWidth,
                middle.getHeight() * Settings.scale
        );

        FontHelper.renderFontCentered(
                sb,
                FontHelper.buttonLabelFont,
                getText(),
                hb.cX,
                renderY + hb.height / 4f,
                Settings.CREAM_COLOR
        );

        hb.render(sb);
    }

    private String getText() {
        return ((CafeRoom) AbstractDungeon.getCurrRoom().event).bartender.getLabelText();
    }
}
