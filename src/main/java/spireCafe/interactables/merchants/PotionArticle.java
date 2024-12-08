package spireCafe.interactables.merchants;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.PotionSlot;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import spireCafe.abstracts.AbstractArticle;
import spireCafe.abstracts.AbstractMerchant;

public class PotionArticle extends AbstractArticle {
    private AbstractPotion potion;
    private int basePrice;

    public PotionArticle(String id, AbstractMerchant merchant, float x, float y, AbstractPotion potion, int basePrice) {
        super(id, merchant);
        xPos = x;
        yPos = y;
        this.potion = potion;
        potion.posX = xPos + 32f; //this offset is absolute in AbstractPotion, so no scaling here either
        potion.posY = yPos + 32f;
        this.basePrice = basePrice;
        hb = new Hitbox(ImageMaster.POTION_ANVIL_CONTAINER.getWidth() * Settings.scale, ImageMaster.POTION_ANVIL_CONTAINER.getHeight() * Settings.scale);
    }

    @Override
    public boolean canBuy() {
        return AbstractDungeon.player.gold >= getModifiedPrice();
    }

    //We override this because we have to use obtainPotion() as part of the logic, we keep onBuy as a way to change the way the price works
    @Override
    public void onClick() {
        if (canBuy()) {
            if (AbstractDungeon.player.obtainPotion(potion)) {
                onBuy();
                merchant.onBuyArticle(this);
            }
        }
    }

    @Override
    public void onBuy() {
        AbstractDungeon.player.loseGold(getModifiedPrice());
    }

    @Override
    public int getBasePrice() {
        return basePrice;
    }

    @Override
    public void update() {
        potion.update();
        super.update();
        potion.scale = scale;
    }

    @Override
    public void renderItem(SpriteBatch sb) {
        potion.shopRender(sb);
        if (hb.hovered) {
            TipHelper.queuePowerTips((float) InputHelper.mX + 50.0F * Settings.scale, (float) InputHelper.mY + 50.0F * Settings.scale, potion.tips);
        }
    }
}
