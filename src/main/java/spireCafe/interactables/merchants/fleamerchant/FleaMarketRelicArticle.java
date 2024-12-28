package spireCafe.interactables.merchants.fleamerchant;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.relics.*;
import com.megacrit.cardcrawl.shop.ShopScreen;
import com.megacrit.cardcrawl.vfx.RelicAboveCreatureEffect;
import spireCafe.Anniv7Mod;
import spireCafe.abstracts.AbstractArticle;
import spireCafe.abstracts.AbstractMerchant;
import spireCafe.interactables.merchants.secretshop.SecretShopMerchant;
import spireCafe.util.TexLoader;

import java.util.ArrayList;

public class FleaMarketRelicArticle extends AbstractArticle {
    private static final String ID = Anniv7Mod.makeID(FleaMarketRelicArticle.class.getSimpleName());
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ID);
    private static final float RELIC_Y = 364.0F * Settings.scale;
    private final AbstractRelic relic;
    private boolean primed;
    private HaggleArticle haggleArticle;

    public FleaMarketRelicArticle(AbstractMerchant merchant, int slot, HaggleArticle haggleArticle) {
        super(ID, merchant, 964.0F * Settings.xScale + 150.0F * slot * Settings.xScale, RELIC_Y, ImageMaster.loadImage("images/relics/"+new PenNib().imgUrl));
        this.primed = slot==2;
        this.haggleArticle = haggleArticle;
        this.relic = primed?getRandomPrimedRelic():getRandomUsedRelic();
        this.itemTexture = new TextureRegion(ImageMaster.loadImage("images/relics/"+relic.imgUrl));
    }

    @Override
    public boolean canBuy() {
        return AbstractDungeon.player.gold > getModifiedPrice();
    }

    @Override
    public void onBuy() {
        AbstractDungeon.player.loseGold(getModifiedPrice());
        AbstractDungeon.getCurrRoom().relics.add(relic);
        relic.instantObtain(AbstractDungeon.player, AbstractDungeon.player.relics.size(), false);
        relic.flash();
    }

    @Override
    public String getTipHeader() {
        return uiStrings.TEXT[0] + relic.name;
    }

    @Override
    public String getTipBody() {
        return uiStrings.TEXT[primed?1:2] + relic.description;
    }

    @Override
    public int getBasePrice() {
        return primed?250:100;
    }

    public AbstractRelic getRandomPrimedRelic(){
        ArrayList<AbstractRelic> primedRelics = new ArrayList<>();
        ArrayList<Integer> prCounters = new ArrayList<>();
        primedRelics.add(new PenNib());         prCounters.add(9);
        primedRelics.add(new Nunchaku());       prCounters.add(9);
        primedRelics.add(new InkBottle());      prCounters.add(9);
        primedRelics.add(new IncenseBurner());  prCounters.add(5);
        primedRelics.add(new TinyChest());      prCounters.add(3);
        primedRelics.add(new HappyFlower());    prCounters.add(2);

        int r = AbstractDungeon.merchantRng.random(0, primedRelics.size()-1);
        AbstractRelic primedRelic = primedRelics.get(r);
        primedRelic.setCounter(prCounters.get(r));
        return primedRelic;
    }

    public AbstractRelic getRandomUsedRelic(){
        ArrayList<AbstractRelic> usedRelics = new ArrayList<>();
        ArrayList<Integer> urCounters = new ArrayList<>();
        usedRelics.add(new Matryoshka());   urCounters.add(1);
        usedRelics.add(new Omamori());      urCounters.add(1);
        usedRelics.add(new WingBoots());    urCounters.add(2);

        int r = AbstractDungeon.merchantRng.random(0, usedRelics.size()-1);
        AbstractRelic usedRelic = usedRelics.get(r);
        usedRelic.setCounter(urCounters.get(r));
        return usedRelic;
    }

    @Override
    public int getModifiedPrice() {
        float finalPrice = getBasePrice();
        if (AbstractDungeon.ascensionLevel >= 16) {
            finalPrice = finalPrice * 1.1f;
        }
        if (AbstractDungeon.player.hasRelic(MembershipCard.ID)) {
            finalPrice = finalPrice * 0.5f;
        }
        if (AbstractDungeon.player.hasRelic(Courier.ID)) {
            finalPrice = finalPrice * 0.8f;
        }
        return (int)(finalPrice*haggleArticle.haggleRate);
    }
}