package spireCafe.abstracts;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.input.InputHelper;

public abstract class AbstractArticle {

    private static final float HOVERED_SCALE = 1.1f;
    private static final float PRICE_OFFSET = 75f;

    public String articleId;
    public AbstractMerchant merchant;

    public Hitbox hb;
    public boolean isGettingClicked = false;
    public float xPos;
    public float yPos;

    private float scale;

    public TextureRegion itemTexture;

    public int price = -1;
    public Texture priceIcon;

    public AbstractArticle(String id, AbstractMerchant merchant, float x, float y, float hbWidth, float hbHeight) {
        articleId = id;
        this.merchant = merchant;
        xPos = x;
        yPos = y;
        hb = new Hitbox(x, y, hbWidth, hbHeight);
    }

    public AbstractArticle(String id, AbstractMerchant merchant, float x, float y, Texture itemTexture) {
        articleId = id;
        this.merchant = merchant;
        xPos = x;
        yPos = y;
        this.itemTexture = new TextureRegion(itemTexture);
        hb = new Hitbox(this.itemTexture.getRegionWidth(), this.itemTexture.getRegionHeight());
    }


    public abstract boolean canBuy();

    public abstract void onBuy();

    public void onClick() {
        if (canBuy()) {
            onBuy();
            merchant.onBuyArticle(this);
        }
    }


    public void update() {
        hb.update(xPos, yPos);
        scale = hb.hovered ? Settings.scale * HOVERED_SCALE : Settings.scale;

        //I wouldn't be surprised if hitboxes had something to not have to deal with this isGettingClicked flag, but I didn't find it
        if (hb.hovered && InputHelper.justClickedLeft) {
            isGettingClicked = true;
        }
        if (hb.hovered && InputHelper.justReleasedClickLeft && isGettingClicked) {
            onClick();
        }
        if (!(hb.hovered && InputHelper.isMouseDown)) {
            isGettingClicked = false;
        }
    }

    public void render(SpriteBatch sb) {
        renderItem(sb);
        renderPrice(sb);
    }

    public void renderItem(SpriteBatch sb) {
        if (itemTexture != null) {
            sb.draw(itemTexture, xPos, yPos, itemTexture.getRegionWidth()/2f, itemTexture.getRegionHeight()/2f, itemTexture.getRegionWidth(), itemTexture.getRegionHeight(), scale, scale,0);
        }
    }

    public void renderPrice(SpriteBatch sb) {
        float priceX = xPos + hb.width/2f;
        float priceY = yPos - PRICE_OFFSET * scale;
        float textLength = FontHelper.getWidth(FontHelper.tipHeaderFont, String.valueOf(price), scale);
        if (priceIcon != null) {
            float lineStart = priceX - (textLength + priceIcon.getWidth())/2f;
            sb.draw(priceIcon, lineStart, priceY, priceIcon.getWidth() * scale, priceIcon.getHeight() * scale);
            FontHelper.renderFont(sb, FontHelper.tipHeaderFont, String.valueOf(price), lineStart + priceIcon.getWidth() * scale, priceY + priceIcon.getHeight()/2f, canBuy()? Color.WHITE : Color.SALMON);
        } else {
            FontHelper.renderFontCentered(sb, FontHelper.tipHeaderFont, String.valueOf(price), priceX, priceY, canBuy()? Color.WHITE : Color.SALMON);
        }
    }
}
