package spireCafe.interactables.merchants.griddraft;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import spireCafe.Anniv7Mod;
import spireCafe.abstracts.AbstractArticle;
import spireCafe.abstracts.AbstractMerchant;
import spireCafe.util.TexLoader;
import spireCafe.util.Wiz;

import java.util.ArrayList;

public class GridPurchaseArticle extends AbstractArticle {

    public boolean isRow;
    public int slot;

    private static final Texture ROW_IDLE = TexLoader.getTexture(Anniv7Mod.makeMerchantPath("griddraft/button_tall_idle.png"));
    private static final Texture ROW_HOVER = TexLoader.getTexture(Anniv7Mod.makeMerchantPath("griddraft/button_tall_hover.png"));
    private static final Texture COLUMN_IDLE = TexLoader.getTexture(Anniv7Mod.makeMerchantPath("griddraft/button_idle.png"));
    private static final Texture COLUMN_HOVER = TexLoader.getTexture(Anniv7Mod.makeMerchantPath("griddraft/button_hover.png"));

    private static float COLUMN_X_OFFSET = 581.0F * Settings.scale;
    private static float COLUMN_Y_OFFSET = 926.0F * Settings.scale;
    private static float X_PAD = 275.0F * Settings.scale;
    
    private static float ROW_X_OFFSET = 511.0F * Settings.scale;
    private static float ROW_Y_OFFSET = 649.0F * Settings.scale;
    private static float Y_PAD = 300.0F * Settings.scale;

    public GridPurchaseArticle(AbstractMerchant merchant, int slot, boolean isRow) {
        super(slot + ":" + (isRow ? "R" : "C"), merchant);
        this.slot = slot;
        this.isRow = isRow;

        this.itemTexture = this.isRow ? new TextureRegion(ROW_IDLE) : new TextureRegion(COLUMN_IDLE);

        if (isRow) {
            this.hb = new Hitbox(66, 246);
        } else {
            this.hb = new Hitbox(246, 66);
        }
        placeButton();
    }

    private void placeButton() {
        if (isRow){
            this.xPos = ROW_X_OFFSET;
            this.yPos = ROW_Y_OFFSET - this.slot * Y_PAD;
        } else {
            this.xPos = COLUMN_X_OFFSET + this.slot * X_PAD;
            this.yPos = COLUMN_Y_OFFSET;
        }
    }

    @Override
    public boolean canBuy() {
        if (!((GridDraftMerchant)this.merchant).hasCards(slot, isRow)) {
            return false;
        }
        if (Wiz.p().gold < getModifiedPrice()) {
            return false;
        }
        return true;
    }

    @Override
    public void update() {
        super.update();
        scale = Settings.scale;
        if (this.hb.hovered) {
            this.itemTexture = this.isRow ? new TextureRegion(ROW_HOVER) : new TextureRegion(COLUMN_HOVER);
        } else {
            this.itemTexture = this.isRow ? new TextureRegion(ROW_IDLE) : new TextureRegion(COLUMN_IDLE);
        }
    }

    @Override
    public void onClick() {
        if (!canBuy()) {
            ((GridDraftMerchant)this.merchant).cantBuy();
        }
        super.onClick();
    }

    @Override
    public void onBuy() {
        Wiz.p().loseGold(getModifiedPrice());
        ((GridDraftMerchant)this.merchant).buyCards(slot, isRow);
    }

    @Override
    public int getBasePrice() {
        ArrayList<AbstractArticle> tmp = ((GridDraftMerchant)this.merchant).getArticles(slot, isRow);
        int baseCost = 0;
        for (AbstractArticle article : tmp) {
            baseCost += article.getBasePrice();
        }
        baseCost *= 0.5F;
        return baseCost;
    }

    @Override
    public Texture getPriceIcon() {
        if (this.isRow) {
            return TexLoader.getTexture(Anniv7Mod.makeMerchantPath("griddraft/gold_vertical.png"));
        } else {
            return ImageMaster.UI_GOLD;
        }
    }

    @Override
    public void renderPrice(SpriteBatch sb) {
        if (!((GridDraftMerchant)this.merchant).hasCards(slot, isRow)) {
            return;
        }
        int price = getModifiedPrice();
        float priceX = xPos;
        float priceY = yPos;
        if (isRow) {
            priceX += 50 * scale;
            priceY += 60 * scale;
        } else {
            priceX += 118 * scale;
            priceY += 0;
        }
        float textLength = FontHelper.getWidth(FontHelper.tipHeaderFont, String.valueOf(price), scale);
        float lineStart = priceX - (textLength + getPriceIcon().getWidth() * scale)/2f;
        if (isRow) {
            float offset = 10.0F;
            sb.draw(getPriceIcon(), lineStart, priceY, getPriceIcon().getWidth() * scale, getPriceIcon().getHeight() * scale);
            FontHelper.renderRotatedText(sb, FontHelper.tipHeaderFont, String.valueOf(price), lineStart + getPriceIcon().getWidth()/2f * scale, priceY + offset + getPriceIcon().getHeight(), 0.0F, 0.0F, 90.0F, true, canBuy()? Color.WHITE : Color.SALMON);
        } else {
            float offset = 7.0F;
            sb.draw(getPriceIcon(), lineStart, priceY, getPriceIcon().getWidth() * scale, getPriceIcon().getHeight() * scale);
            FontHelper.renderFont(sb, FontHelper.tipHeaderFont, String.valueOf(price), lineStart + getPriceIcon().getWidth() * scale, priceY + offset + getPriceIcon().getHeight()/2f, canBuy()? Color.WHITE : Color.SALMON);
        }
        hb.render(sb);
    }
    
}
