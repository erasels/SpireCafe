package spireCafe.interactables.merchants;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.vfx.FastCardObtainEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import spireCafe.abstracts.AbstractArticle;
import spireCafe.abstracts.AbstractMerchant;

public class CardArticle extends AbstractArticle {
    private static final float PRICE_OFFSET = 50f;
    private AbstractCard card;
    private int basePrice;

    public CardArticle(String id, AbstractMerchant merchant, float x, float y, AbstractCard card, int basePrice) {
        super(id, merchant);
        xPos = x;
        yPos = y;
        hb = card.hb;
        this.card = card;
        this.basePrice = basePrice;
    }

    @Override
    public boolean canBuy() {
        return AbstractDungeon.player.gold >= getModifiedPrice();
    }

    @Override
    public void onBuy() {
        AbstractDungeon.player.loseGold(getModifiedPrice());
        AbstractDungeon.topLevelEffectsQueue.add(new FastCardObtainEffect(card, xPos, yPos));
    }

    @Override
    public int getBasePrice() {
        return basePrice;
    }

    @Override
    public void update() {

        card.current_x = xPos;
        card.current_y = yPos;
        card.target_x = xPos;
        card.target_y = yPos;
        card.update();
        card.updateHoverLogic();

        //I wouldn't be surprised if hitboxes had something to not have to deal with this isGettingClicked flag, but I didn't find it
        if (card.hb.hovered && InputHelper.justClickedLeft) {
            isGettingClicked = true;
        }
        if (card.hb.hovered && InputHelper.justReleasedClickLeft && isGettingClicked) {
            onClick();
        }
        if (!(card.hb.hovered && InputHelper.isMouseDown)) {
            isGettingClicked = false;
        }
    }

    @Override
    public void renderItem(SpriteBatch sb) {
        card.render(sb);
        if (card.hoverTimer > 0.3f) {
            card.renderCardTip(sb);
        }
    }

    @Override
    public void renderPrice(SpriteBatch sb) {
        int price = getModifiedPrice();
        float priceX = xPos;
        float priceY = yPos - PRICE_OFFSET - card.hb.height/2f;
        float textLength = FontHelper.getWidth(FontHelper.tipHeaderFont, String.valueOf(price), card.drawScale);
        if (getPriceIcon() != null) {
            float lineStart = priceX - (textLength + getPriceIcon().getWidth())/2f;
            sb.draw(getPriceIcon(), lineStart, priceY, getPriceIcon().getWidth() * card.drawScale, getPriceIcon().getHeight() * card.drawScale);
            FontHelper.renderFont(sb, FontHelper.tipHeaderFont, String.valueOf(price), lineStart + getPriceIcon().getWidth() * card.drawScale, priceY + getPriceIcon().getHeight()/2f, canBuy()? Color.WHITE : Color.SALMON);
        } else {
            FontHelper.renderFontCentered(sb, FontHelper.tipHeaderFont, String.valueOf(price), priceX, priceY, canBuy()? Color.WHITE : Color.SALMON);
        }
    }
}
