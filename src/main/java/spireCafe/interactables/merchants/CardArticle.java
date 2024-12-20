package spireCafe.interactables.merchants;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.vfx.FastCardObtainEffect;
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

        //can't super.update() because it would update the hb twice
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

    @Override
    public void onRightClick() {
        CardCrawlGame.cardPopup.open(card);
    }

    @Override
    public void renderItem(SpriteBatch sb) {
        card.render(sb);
        card.renderCardTip(sb);
    }

    @Override
    public void renderPrice(SpriteBatch sb) {
        sb.setColor(Color.WHITE);
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
