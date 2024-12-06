package spireCafe.interactables.merchants;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.FastCardObtainEffect;
import spireCafe.abstracts.AbstractArticle;
import spireCafe.abstracts.AbstractMerchant;

public class RelicArticle extends AbstractArticle {
    private AbstractRelic relic;
    private int basePrice;

    public RelicArticle(String id, AbstractMerchant merchant, float x, float y, AbstractRelic relic, int basePrice) {
        super(id, merchant);
        xPos = x;
        yPos = y;
        this.relic = relic;
        this.basePrice = basePrice;
        itemTexture = new TextureRegion(relic.img);
        hb = new Hitbox(relic.img.getWidth()/2f* Settings.scale, relic.img.getHeight()/2f * Settings.scale);
    }

    @Override
    public boolean canBuy() {
        return AbstractDungeon.player.gold >= getModifiedPrice();
    }

    @Override
    public void onBuy() {
        AbstractDungeon.player.loseGold(getModifiedPrice());
        AbstractDungeon.getCurrRoom().relics.add(this.relic);
        this.relic.instantObtain(AbstractDungeon.player, AbstractDungeon.player.relics.size(), true);
        this.relic.flash();
    }

    @Override
    public int getBasePrice() {
        return basePrice;
    }

    @Override
    public void onRightClick() {
        CardCrawlGame.relicPopup.open(relic);
    }

    @Override
    public void renderItem(SpriteBatch sb) {
        if (itemTexture != null) {
            sb.draw(itemTexture, xPos - itemTexture.getRegionWidth()/4f, yPos - itemTexture.getRegionWidth()/4f, itemTexture.getRegionWidth()/2f, itemTexture.getRegionHeight()/2f, itemTexture.getRegionWidth(), itemTexture.getRegionHeight(), scale, scale,0);
        }
        if (hb.hovered) {
            relic.renderTip(sb);
        }
    }
}
