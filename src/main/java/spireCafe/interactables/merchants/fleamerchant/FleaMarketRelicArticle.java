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
    private final double priceJitter;
    private int price;

    public FleaMarketRelicArticle(AbstractMerchant merchant, int slot, HaggleArticle haggleArticle) {
        super(ID, merchant, 964.0F * Settings.xScale + 150.0F * slot * Settings.xScale, RELIC_Y, ImageMaster.loadImage("images/relics/"+new PenNib().imgUrl));
        this.primed = slot==2;
        this.haggleArticle = haggleArticle;
        this.relic = primed?getRandomPrimedRelic():getRandomUsedRelic();
        this.itemTexture = new TextureRegion(ImageMaster.loadImage("images/relics/"+relic.imgUrl));
        priceJitter = AbstractDungeon.merchantRng.random(0.95F, 1.05F);
    }

    @Override
    public void onClick() {
        if (!canBuy()) {
            ((FleaMerchant) merchant).cantBuy();
        } else {
            ((FleaMerchant) merchant).sold();
        }
        super.onClick();
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
        return uiStrings.TEXT[primed?1:3] + relic.counter + uiStrings.TEXT[primed?2:(relic.counter==1?4:5)] + relic.description;
    }

    @Override
    public int getBasePrice() {
        return price;
    }

    public AbstractRelic getRandomPrimedRelic(){
        ArrayList<AbstractRelic> primedRelics = new ArrayList<>();
        ArrayList<Integer> prCounters = new ArrayList<>();
        primedRelics.add(new PenNib());         prCounters.add(10);
        primedRelics.add(new Nunchaku());       prCounters.add(10);
        primedRelics.add(new InkBottle());      prCounters.add(10);
        primedRelics.add(new IncenseBurner());  prCounters.add(6);
        primedRelics.add(new TinyChest());      prCounters.add(4);
        primedRelics.add(new HappyFlower());    prCounters.add(3);

        int r = AbstractDungeon.merchantRng.random(0, primedRelics.size()-1);
        AbstractRelic primedRelic = primedRelics.get(r);
        int counter = AbstractDungeon.merchantRng.random(1, prCounters.get(r)-1);
        price = primedRelic.getPrice() + 60*(counter/prCounters.get(r));
        primedRelic.setCounter(counter);
        return primedRelic;
    }

    public AbstractRelic getRandomUsedRelic(){
        ArrayList<AbstractRelic> usedRelics = new ArrayList<>();
        ArrayList<Integer> urCounters = new ArrayList<>();
        usedRelics.add(new Matryoshka());   urCounters.add(2);
        usedRelics.add(new Omamori());      urCounters.add(2);
        usedRelics.add(new WingBoots());    urCounters.add(3);

        int r = AbstractDungeon.merchantRng.random(usedRelics.size()-1);
        AbstractRelic usedRelic = usedRelics.get(r);
        int counter = AbstractDungeon.merchantRng.random(1, urCounters.get(r)-1);
        price = (counter*usedRelic.getPrice()/urCounters.get(r));
        usedRelic.setCounter(counter);
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
        return (int)(finalPrice*haggleArticle.haggleRate*priceJitter);
    }
}