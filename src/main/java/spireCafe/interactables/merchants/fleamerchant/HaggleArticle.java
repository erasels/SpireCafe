package spireCafe.interactables.merchants.fleamerchant;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.UIStrings;
import spireCafe.Anniv7Mod;
import spireCafe.abstracts.AbstractArticle;
import spireCafe.abstracts.AbstractMerchant;
import spireCafe.interactables.merchants.secretshop.SecretShopMerchant;
import spireCafe.util.TexLoader;
import spireCafe.util.Wiz;

public class HaggleArticle extends AbstractArticle{
    private static final String ID = Anniv7Mod.makeID(HaggleArticle.class.getSimpleName());
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ID);
    private static final Texture HAGGLE_TEXTURE = TexLoader.getTexture(Anniv7Mod.makeMerchantPath("fleamerchant/haggleOption.png"));
    private static final Texture NO_HAGGLE_TEXTURE = TexLoader.getTexture(Anniv7Mod.makeMerchantPath("fleamerchant/AngryFlo.png"));
    public double haggleRate = 1;
    public double haggleOdds = 0.8;

    public HaggleArticle(AbstractMerchant merchant, float x, float y) {
        super(ID, merchant, x, y, NO_HAGGLE_TEXTURE);
    }
    
    @Override
    public boolean canBuy() {
        return haggleOdds > 0;
    }

    @Override
    public void onClick() {
        if(canBuy()) {
            if (AbstractDungeon.merchantRng.random() <= haggleOdds) {
                haggleOdds -= 0.1;
                haggleRate -= 0.1;
                ((FleaMerchant) merchant).haggleBubble(true);
                return;
            }
            haggleOdds = 0;
            haggleRate = 1.5;
        }
        ((FleaMerchant) merchant).haggleBubble(false);
    }

    @Override
    public void onBuy() {
        // Should never trigger
    }

    @Override
    public int getBasePrice() {
        return (int) (haggleOdds*100); //use cost to showcase current haggle odds
    }

    @Override
    public int getModifiedPrice() {
        return getBasePrice();
    }

    @Override
    public Texture getPriceIcon() {
        return ImageMaster.RUN_HISTORY_MAP_ICON_EVENT;
    }

    @Override
    public void render(SpriteBatch sb) {
        if (haggleOdds>0){
            itemTexture = new TextureRegion(HAGGLE_TEXTURE);
        } else {
            itemTexture = new TextureRegion(NO_HAGGLE_TEXTURE);
        }
        super.render(sb);
    }

    @Override
    public String getTipHeader() {
        return uiStrings.TEXT[0];
    }

    @Override
    public String getTipBody() {
        if(haggleOdds>0) {
            return uiStrings.TEXT[1] + (int) (100*haggleOdds) + uiStrings.TEXT[2] + (int) (100 - 100*haggleRate) + uiStrings.TEXT[3];
        } return uiStrings.TEXT[4] + (int) (100*haggleRate) + uiStrings.TEXT[5];
    }

    public void renderPrice(SpriteBatch sb) {
        String price = String.valueOf(getModifiedPrice()) + "%";
        float priceX = xPos + hb.width/2f;
        float priceY = yPos - 60f * scale;
        float textLength = FontHelper.getWidth(FontHelper.tipHeaderFont, price, scale);
        if (getPriceIcon() != null) {
            float lineStart = priceX - (textLength + getPriceIcon().getWidth() * scale)/2f;
            sb.draw(getPriceIcon(), lineStart, priceY, getPriceIcon().getWidth() * scale, getPriceIcon().getHeight() * scale);
            FontHelper.renderFont(sb, FontHelper.tipHeaderFont, price, lineStart + getPriceIcon().getWidth() * scale, priceY + getPriceIcon().getHeight()/2f, canBuy()? Color.WHITE : Color.SALMON);
        } else {
            FontHelper.renderFontCentered(sb, FontHelper.tipHeaderFont, price, priceX, priceY, canBuy()? Color.WHITE : Color.SALMON);
        }
        hb.render(sb);
    }
}
