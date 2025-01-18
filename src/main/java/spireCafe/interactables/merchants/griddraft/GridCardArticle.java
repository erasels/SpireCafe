package spireCafe.interactables.merchants.griddraft;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import spireCafe.abstracts.AbstractMerchant;
import spireCafe.interactables.merchants.CardArticle;

public class GridCardArticle extends CardArticle {

    private static float X_OFFSET = 700.0F * Settings.scale;
    private static float Y_OFFSET = 768.0F * Settings.scale;
    private static float X_PAD = 275.0F * Settings.scale;
    private static float Y_PAD = 300.0F * Settings.scale;
    public static float CARD_SCALE = 0.65F;
    public static float CARD_SCALE_HOVER = 0.75F;

    public int column;
    public int row;
    private AbstractCard card;
    private int priceVar;

    public GridCardArticle(AbstractMerchant merchant, int row, int column, AbstractCard card, int basePrice) {
        super("id" + row + column, merchant, 0, 0, card, basePrice);
        this.card = card;
        this.card.drawScale = CARD_SCALE;
        this.row = row;
        this.column = column;
        updateGrid(row, column);
        this.priceVar = AbstractDungeon.merchantRng.random(-5,5);
    }

    @Override
    public void update() {
        super.update();
        if (this.hb.hovered) {
            this.card.drawScale = CARD_SCALE_HOVER;
        } else {
            this.card.drawScale = CARD_SCALE;
        }
    }

    private void updateGrid(int row, int column) {
        this.xPos = X_OFFSET + column * X_PAD;
        this.yPos = Y_OFFSET - row * Y_PAD;
    }

    @Override
    public void renderPrice(SpriteBatch sb) {
    }

    public AbstractCard getCard() {
        return this.card;
    }

    @Override
    public int getBasePrice() {
        return AbstractCard.getPrice(this.card.rarity) + this.priceVar;
    }

    @Override
    public int getModifiedPrice() {
        return 0; // Cost is handled by buttons
    }

    @Override
    public void onClick() {
        // Purchase handled by buttons
    }
    
}
