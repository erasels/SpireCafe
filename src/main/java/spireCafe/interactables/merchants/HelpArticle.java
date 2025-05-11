package spireCafe.interactables.merchants;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;

import basemod.devcommands.Help;
import spireCafe.Anniv7Mod;
import spireCafe.abstracts.AbstractArticle;
import spireCafe.abstracts.AbstractMerchant;
import spireCafe.util.TexLoader;

public class HelpArticle extends AbstractArticle {
    
    private static final String ID = Anniv7Mod.makeID(HelpArticle.class.getSimpleName());
    private static final Texture TEXTURE = TexLoader.getTexture(Anniv7Mod.makeUIPath("question_mark.png"));
    private String header;
    private String body;
    public boolean tipLocationFixed;
    public float fixedX;
    public float fixedY;

    public HelpArticle(AbstractMerchant merchant, float x, float y, String header, String body) {
        super(ID, merchant, x, y, TEXTURE);
        this.header = header;
        this.body = body;
    }

    public HelpArticle(AbstractMerchant merchant, float x, float y, String header, String body, boolean fixedTipLocation, float tipX, float tipY) {
        this(merchant, x, y, header, body);
        this.tipLocationFixed = fixedTipLocation;
        this.fixedX = tipX;
        this.fixedY = tipY;
    }

    @Override
    public void onClick() {}

    @Override
    public boolean canBuy() {
        return false;
    }

    @Override
    public String getTipHeader() {
        return header;
    }

    @Override
    public String getTipBody() {
        return body;
    }

    @Override
    public void render(SpriteBatch sb) {
        if (this.tipLocationFixed) {
            sb.setColor(Color.WHITE);
            renderItem(sb);

            if ((getTipHeader() != null || getTipBody() != null) && hb.hovered) {
                TipHelper.renderGenericTip(fixedX * Settings.xScale, fixedY * Settings.yScale, getTipHeader(), getTipBody());
            }
        } else {
            super.render(sb);
        }
    }

    @Override
    public void onBuy() {}

    @Override
    public int getBasePrice() {
        return -1;
    }

    @Override
    public void renderPrice(SpriteBatch sb) {}
    
}
