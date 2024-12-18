package spireCafe.interactables.merchants.secretshop;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.RelicAboveCreatureEffect;

import spireCafe.Anniv7Mod;
import spireCafe.abstracts.AbstractArticle;
import spireCafe.abstracts.AbstractMerchant;
import spireCafe.util.TexLoader;

public class IdentifyRelicArticle extends AbstractArticle {
    private static final String ID = Anniv7Mod.makeID(IdentifyRelicArticle.class.getSimpleName());
    private static final Texture TEXTURE = TexLoader.getTexture(Anniv7Mod.makeMerchantPath("secretshop/relic.png"));
    private static final float RELIC_Y = 364.0F;
    public AbstractRelic hiddenRelic;
    private int basePrice;

    public IdentifyRelicArticle(AbstractMerchant merchant, int slot, AbstractRelic hiddenRelic, int basePrice) {
        super(ID, merchant, 964.0F * Settings.xScale + 150.0F * slot * Settings.xScale, RELIC_Y, TEXTURE);
        this.hiddenRelic = hiddenRelic;
        this.basePrice = basePrice;
    }

    @Override
    public boolean canBuy() {
       return AbstractDungeon.player.gold >= getModifiedPrice();
    }

    @Override
    public void onBuy() {
        AbstractDungeon.player.loseGold(getModifiedPrice());
        AbstractDungeon.getCurrRoom().relics.add(this.hiddenRelic);
        this.hiddenRelic.instantObtain(AbstractDungeon.player, AbstractDungeon.player.relics.size(), true);
        AbstractDungeon.effectList.add(new RelicAboveCreatureEffect(Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F, hiddenRelic));
    }

    @Override
    public int getBasePrice() {
        return this.basePrice;
    }
}
