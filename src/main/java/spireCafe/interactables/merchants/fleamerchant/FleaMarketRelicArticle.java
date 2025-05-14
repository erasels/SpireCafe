package spireCafe.interactables.merchants.fleamerchant;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.relics.*;
import spireCafe.Anniv7Mod;
import spireCafe.abstracts.AbstractArticle;
import spireCafe.abstracts.AbstractMerchant;
import spireCafe.abstracts.AbstractCafeInteractable.FacingDirection;

import java.util.ArrayList;

public class FleaMarketRelicArticle extends AbstractArticle {
    private static final String ID = Anniv7Mod.makeID(FleaMarketRelicArticle.class.getSimpleName());
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ID);
    private static final float RELIC_Y = 364.0F * Settings.scale;
    public final AbstractRelic relic;
    private boolean primed;
    private HaggleArticle haggleArticle;
    private final double priceJitter;
    private int price;

    public FleaMarketRelicArticle(AbstractMerchant merchant, int slot, HaggleArticle haggleArticle) {
        super(ID, merchant, 964.0F * Settings.xScale + 150.0F * slot * Settings.xScale, RELIC_Y, ImageMaster.loadImage("images/relics/"+new PenNib().imgUrl));
        this.primed = slot==2;
        this.haggleArticle = haggleArticle;
        this.relic = primed?getRandomPrimedRelic():getRandomUsedRelic();
        if (this.relic != null) {
            this.itemTexture = new TextureRegion(ImageMaster.loadImage("images/relics/"+relic.imgUrl));
        }
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
        return AbstractDungeon.player.gold >= getModifiedPrice();
    }

    @Override
    public void onBuy() {
        AbstractDungeon.player.loseGold(getModifiedPrice());
        AbstractDungeon.getCurrRoom().relics.add(relic);
        relic.instantObtain(AbstractDungeon.player, AbstractDungeon.player.relics.size(), false);
        relic.flash();
        ((FleaMerchant) merchant).relicCourierCheck(relic, primed?2:1);
    }

    @Override
    public String getTipHeader() {
        return uiStrings.TEXT[0].replace("{0}", relic.name);
    }

    @Override
    public String getTipBody() {
        return uiStrings.TEXT[primed?1:(relic.counter==1?2:3)].replace("{0}", String.valueOf(relic.counter)).replace("{1}", relic.description);
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
        for (int i = primedRelics.size() - 1; i >= 0; i--) {
            if (AbstractDungeon.player.hasRelic(primedRelics.get(i).relicId)) {
                primedRelics.remove(i);
                prCounters.remove(i);
            }
        }
        if (primedRelics.isEmpty()) {
            return null;
        }

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
        for (int i = usedRelics.size() - 1; i >= 0; i--) {
            if (AbstractDungeon.player.hasRelic(usedRelics.get(i).relicId)) {
                usedRelics.remove(i);
                urCounters.remove(i);
            }
        }
        if (usedRelics.isEmpty()) {
            return null;
        }

        int r = AbstractDungeon.merchantRng.random(usedRelics.size()-1);
        AbstractRelic usedRelic = usedRelics.get(r);
        int counter = AbstractDungeon.merchantRng.random(1, urCounters.get(r)-1);
        price = (int) ((FleaMerchant) merchant).jitter((double) (counter * usedRelic.getPrice()) /urCounters.get(r));
        usedRelic.setCounter(counter);
        return usedRelic;
    }

    @Override
    public int getModifiedPrice() {
        float finalPrice = super.getModifiedPrice();
        return (int)(finalPrice*haggleArticle.haggleRate);
    }
}