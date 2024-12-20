package spireCafe.interactables.merchants.secretshop;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.relics.Courier;

import spireCafe.Anniv7Mod;
import spireCafe.abstracts.AbstractArticle;
import spireCafe.abstracts.AbstractMerchant;
import spireCafe.util.TexLoader;

public class IdentifyPotionArticle extends AbstractArticle{
    private static final String ID = Anniv7Mod.makeID(IdentifyPotionArticle.class.getSimpleName());
    private static final Texture TEXTURE = TexLoader.getTexture(Anniv7Mod.makeMerchantPath("secretshop/potion.png"));
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ID);
    private static final float POTION_Y = 168.0F * Settings.scale;;
    public AbstractPotion hiddenPotion;
    private int basePrice;
    private int slot;
    private SecretShopMerchant ssMerchant;

    public IdentifyPotionArticle(AbstractMerchant merchant, int slot, AbstractPotion hiddenPotion, int basePrice) {
        super(ID, merchant, 968.0F * Settings.xScale + 150.0F * slot * Settings.xScale, POTION_Y, TEXTURE);
        this.hiddenPotion = hiddenPotion;
        this.basePrice = basePrice;
        this.slot = slot;
        this.ssMerchant = (SecretShopMerchant) merchant;
    }

    @Override
    public boolean canBuy() {
        return AbstractDungeon.player.gold >= getModifiedPrice();
    }

    @Override
    public void onClick() {
        if (!canBuy()) {
            this.ssMerchant.cantBuy();
            return;
        }
        if (!AbstractDungeon.player.obtainPotion(this.hiddenPotion)) {
            return;
        }
        onBuy();
        merchant.onBuyArticle(this);
    }

    @Override
    public void onBuy() {
        AbstractDungeon.player.loseGold(getModifiedPrice());

        if (!AbstractDungeon.player.hasRelic(Courier.ID)) {
            return;
        }

        AbstractPotion p = AbstractDungeon.returnRandomPotion();
        IdentifyPotionArticle tmpArticle = new IdentifyPotionArticle(this.merchant, this.slot, p, SecretShopMerchant.setPotionBasePrice());
        this.merchant.toAdd.add(tmpArticle);
    }

    @Override
    public int getBasePrice() {
        return this.basePrice;
    }

    @Override
    public String getTipHeader() {
        return uiStrings.TEXT[0];
    }

    @Override
    public String getTipBody() {
        return uiStrings.TEXT[1];
    }
    
}
