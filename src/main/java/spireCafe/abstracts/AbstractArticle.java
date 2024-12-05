package spireCafe.abstracts;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.relics.Courier;
import com.megacrit.cardcrawl.relics.MembershipCard;

public abstract class AbstractArticle {

    private static final float HOVERED_SCALE = 1.1f;
    private static final float PRICE_OFFSET = 60f;

    public String articleId;
    public AbstractMerchant merchant;

    public Hitbox hb;
    public boolean isGettingClicked = false;
    public boolean isGettingRightClicked = false;
    public float xPos;
    public float yPos;

    public float scale;

    public TextureRegion itemTexture;

    public AbstractArticle(String id, AbstractMerchant merchant) {
        articleId = id;
        this.merchant = merchant;
    }

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

    //return true if the player should be able to buy the article (also changes the text color of the price)
    public abstract boolean canBuy();

    //Should both give the player what they bought and take the price from them
    public abstract void onBuy();

    //Price before ascension and relic-related gold cost modifiers
    public abstract int getBasePrice();

    //Price after ascension and relic-related modifiers (actual cost)
    //Only override if they should be different than normal
    public int getModifiedPrice() {
        float finalPrice = getBasePrice();
        if (AbstractDungeon.ascensionLevel >= 16) {
            finalPrice = finalPrice * 1.1f;
        }
        if (AbstractDungeon.player.hasRelic(MembershipCard.ID)) {
            finalPrice = finalPrice * 0.5f;
        }
        if (AbstractDungeon.player.hasRelic(Courier.ID)) {
            finalPrice = finalPrice * 0.8f;
        }
        return (int)finalPrice;
    }

    public String getTipHeader() {
        return null;
    }

    public String getTipBody() {
        return null;
    }

    public Texture getPriceIcon() {
        return ImageMaster.UI_GOLD;
    }


    public void onClick() {
        if (canBuy()) {
            onBuy();
            merchant.onBuyArticle(this);
        }
    }

    public void onRightClick() {

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
        if (hb.hovered && InputHelper.justClickedRight) {
            isGettingRightClicked = true;
        }
        if (hb.hovered && InputHelper.justReleasedClickRight && isGettingRightClicked) {
            onRightClick();
        }
        if (!(hb.hovered && InputHelper.isMouseDown_R)) {
            isGettingRightClicked = false;
        }
    }

    public void render(SpriteBatch sb) {
        sb.setColor(Color.WHITE);
        renderItem(sb);
        renderPrice(sb);

        if ((getTipHeader() != null || getTipBody() != null) && hb.hovered) {
            TipHelper.renderGenericTip(InputHelper.mX + 38f * Settings.scale, InputHelper.mY, getTipHeader(), getTipBody());
        }
    }

    public void renderItem(SpriteBatch sb) {
        if (itemTexture != null) {
            sb.draw(itemTexture, xPos, yPos, itemTexture.getRegionWidth()/2f, itemTexture.getRegionHeight()/2f, itemTexture.getRegionWidth(), itemTexture.getRegionHeight(), scale, scale,0);
        }
    }

    public void renderPrice(SpriteBatch sb) {
        int price = getModifiedPrice();
        float priceX = xPos + hb.width/2f;
        float priceY = yPos - PRICE_OFFSET * scale;
        float textLength = FontHelper.getWidth(FontHelper.tipHeaderFont, String.valueOf(price), scale);
        if (getPriceIcon() != null) {
            float lineStart = priceX - (textLength + getPriceIcon().getWidth())/2f;
            sb.draw(getPriceIcon(), lineStart, priceY, getPriceIcon().getWidth() * scale, getPriceIcon().getHeight() * scale);
            FontHelper.renderFont(sb, FontHelper.tipHeaderFont, String.valueOf(price), lineStart + getPriceIcon().getWidth() * scale, priceY + getPriceIcon().getHeight()/2f, canBuy()? Color.WHITE : Color.SALMON);
        } else {
            FontHelper.renderFontCentered(sb, FontHelper.tipHeaderFont, String.valueOf(price), priceX, priceY, canBuy()? Color.WHITE : Color.SALMON);
        }
    }
}
