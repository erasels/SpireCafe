package spireCafe.interactables.merchants.enchanter;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.red.IronWave;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;

import basemod.abstracts.AbstractCardModifier;
import spireCafe.Anniv7Mod;
import spireCafe.abstracts.AbstractArticle;
import spireCafe.abstracts.AbstractMerchant;
import spireCafe.interactables.merchants.enchanter.EnchanterMerchant.EnchantSister;
import spireCafe.util.TexLoader;

public class EnchanterArticle extends AbstractArticle {    
    
    public static final Logger logger = LogManager.getLogger("EnchanterArticle");

    private static final String TEXTURE_PATH = Anniv7Mod.makeMerchantPath("enchanter/scrolls/%s.png");

    private static final String ID = Anniv7Mod.makeID(EnchanterArticle.class.getSimpleName());

    private static final float PRICE_OFFSET = 85f;
    private static final float LABEL_OFFSET = 25f;
    private static final float LABEL_OFFSET_X = 11f;

    protected static AbstractCard tooltipBuddy = new IronWave();
    
    private String name;
    private AbstractEnchantment enchantment;
    private int price;
    public AbstractCardModifier baseModifier;
    public boolean isPurchased = false;



    public EnchanterArticle(AbstractMerchant merchant, AbstractEnchantment enchantment) {
        this(merchant, enchantment, 0, 0);
    }

    public EnchanterArticle(AbstractMerchant merchant, AbstractEnchantment enchantment, float x, float y) {
        super(ID, merchant, x, y, TexLoader.getTexture((String.format(TEXTURE_PATH, getScrollNumber(enchantment)))));
        this.enchantment = enchantment;
        this.baseModifier = enchantment.cardModifier;
        this.name = getTipHeader();
        this.price = rollInitPrice();
    }

    private static int getScrollNumber(AbstractEnchantment enchantment) {
        return Math.floorMod(enchantment.cardModifier.getClass().getName().hashCode(), 12) + 1;
    }

    @Override
    public String getTipHeader() {
        return enchantment.getName();
    }
    
    @Override
    public String getTipBody() {
        return enchantment.getDescription();
    }

    @Override
    public void onClick() {
        super.onClick();
        if (!canBuy()) {
            if (AbstractDungeon.player.gold < getModifiedPrice()){
                ((EnchanterMerchant) merchant).enchanterSpeech(MathUtils.random(5,6), EnchantSister.values()[MathUtils.random(2)]);
            } else {
                ((EnchanterMerchant) merchant).enchanterSpeech(7, EnchantSister.values()[MathUtils.random(2)]);
            }
        }
    }

    @Override
    public boolean canBuy() {
        return (AbstractDungeon.player.gold >= getModifiedPrice() && enchantment.getValidCards().size() != 0);
    }

    @Override
    public void onBuy() {
        CardGroup cards = enchantment.getValidCards();
        AbstractDungeon.topLevelEffectsQueue.add(new EnchantCardEffect(cards, this, getModifiedPrice()));
    }

    @Override
    public void update() {
        super.update();        
    }

    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);
        renderLabel(sb);
    }

    @Override
    public int getBasePrice() {
        return this.price;
    }

    public int rollInitPrice() {
        switch (enchantment.rarity) {
            case COMMON:
                return AbstractDungeon.merchantRng.random(50, 100);
            case UNCOMMON:
                return AbstractDungeon.merchantRng.random(75, 125);
            case RARE:
                return AbstractDungeon.merchantRng.random(100, 150);
            default:
                return AbstractDungeon.merchantRng.random(50, 175);
        }
    }

    public void updateXY(float x, float y) {
        xPos = x - (this.itemTexture.getRegionWidth() / 2);
        yPos = y - (this.itemTexture.getRegionHeight() / 2);
    }

    public void renderLabel(SpriteBatch sb) {
        float priceX = xPos + hb.width/2f + (LABEL_OFFSET_X * scale);
        float priceY = yPos - LABEL_OFFSET * scale;
        FontHelper.renderFontCentered(sb, FontHelper.tipHeaderFont, String.valueOf(this.name), priceX, priceY, canBuy()? Color.WHITE : Color.SALMON);
        hb.render(sb);
    }

    @Override
    public void renderPrice(SpriteBatch sb) {
        int price = getModifiedPrice();
        float priceX = xPos + hb.width/2f + (LABEL_OFFSET_X * scale);
        float priceY = yPos - PRICE_OFFSET * scale;
        float textLength = FontHelper.getWidth(FontHelper.tipHeaderFont, String.valueOf(price), scale);
        if (getPriceIcon() != null) {
            float lineStart = priceX - (textLength + getPriceIcon().getWidth() * scale)/2f;
            sb.draw(getPriceIcon(), lineStart, priceY, getPriceIcon().getWidth() * scale, getPriceIcon().getHeight() * scale);
            FontHelper.renderFont(sb, FontHelper.tipHeaderFont, String.valueOf(price), lineStart + getPriceIcon().getWidth() * scale, priceY + getPriceIcon().getHeight()/2f, canBuy()? Color.WHITE : Color.SALMON);
        } else {
            FontHelper.renderFontCentered(sb, FontHelper.tipHeaderFont, String.valueOf(price), priceX, priceY, canBuy()? Color.WHITE : Color.SALMON);
        }
        hb.render(sb);
    }
}
