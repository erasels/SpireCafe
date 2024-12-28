package spireCafe.interactables.merchants.fleamerchant;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.cards.red.Intimidate;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.potions.*;
import com.megacrit.cardcrawl.relics.*;
import org.apache.logging.log4j.core.util.Integers;
import spireCafe.Anniv7Mod;
import spireCafe.abstracts.AbstractArticle;
import spireCafe.abstracts.AbstractMerchant;
import spireCafe.interactables.merchants.CardArticle;
import spireCafe.interactables.merchants.PotionArticle;
import spireCafe.interactables.merchants.RelicArticle;
import spireCafe.util.TexLoader;

import java.util.ArrayList;

public class FleaMerchant extends AbstractMerchant {
    public static final String ID = FleaMerchant.class.getSimpleName();
    private static final CharacterStrings characterStrings = CardCrawlGame.languagePack.getCharacterString(Anniv7Mod.makeID(ID));
    private static final Texture BG_TEXTURE = ImageMaster.loadImage("images/npcs/rug/fra.png");
    private final HaggleArticle haggleArticle;

    public FleaMerchant(float animationX, float animationY) {
        super(animationX, animationY, 160.0f, 200.0f);
        this.name = characterStrings.NAMES[0];
        this.authors = "Jack Renoson";
        this.img = TexLoader.getTexture(Anniv7Mod.makeMerchantPath("example/merchant.png"));
        background = new TextureRegion(BG_TEXTURE);
        this.haggleArticle = new HaggleArticle(this, Settings.WIDTH * 0.75F, 164.0F * Settings.yScale);
        articles.add(haggleArticle);
    }

    @Override
    public void rollShop() {

        //Use CardArticle to add cards to your shop. You can override onBuy() and getPriceIcon() to change the price from gold (default) to something else
        AbstractArticle intimidate = new CardArticle("intimidate", this, 320f * Settings.xScale,700f * Settings.yScale, new Intimidate(), 75);
        articles.add(intimidate);


        AbstractPotion p1 = getUsedPotion();
        AbstractArticle potion1 = new PotionArticle("potion1", this, 620f * Settings.xScale, 700f * Settings.yScale, p1, p1.getPrice()){
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
        };
        articles.add(potion1);

        AbstractPotion p2 = getUsedPotion();
        AbstractArticle potion2 = new PotionArticle("potion2", this, 720f * Settings.xScale, 700f * Settings.yScale, p2, p2.getPrice()){
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
        };
        articles.add(potion2);

        AbstractPotion p3 = getUsedPotion();
        AbstractArticle potion3 = new PotionArticle("potion3", this, 820f * Settings.xScale, 700f * Settings.yScale, p3, p3.getPrice()){
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
        };
        articles.add(potion3);

        AbstractArticle relic = new RelicArticle("relic", this, 964.0F * Settings.xScale,364.0F * Settings.scale, AbstractDungeon.returnRandomRelic(AbstractDungeon.returnRandomRelicTier()), 200){
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
        };
        articles.add(relic);

        AbstractArticle primedRelic = new FleaMarketRelicArticle(this, 1, haggleArticle);
        articles.add(primedRelic);

        AbstractArticle usedRelic = new FleaMarketRelicArticle(this, 2, haggleArticle);
        articles.add(usedRelic);
    }

    private AbstractPotion getUsedPotion(){
        ArrayList<AbstractPotion> potions = new ArrayList<>();
        potions.add(new StrengthPotion(){
            @Override
            public int getPotency(int var1){
                return getPotionPotency(this);
            }
            @Override
            public int getPrice(){
                return getPotionPrice(this);
            }
        });
        potions.add(new DexterityPotion(){
            @Override
            public int getPotency(int var1){
                return getPotionPotency(this);
            }
            @Override
            public int getPrice(){
                return getPotionPrice(this);
            }
        });
        potions.add(new FirePotion(){
            @Override
            public int getPotency(int var1){
                return getPotionPotency(this);
            }
            @Override
            public int getPrice(){
                return getPotionPrice(this);
            }
        });
        potions.add(new ExplosivePotion(){
            @Override
            public int getPotency(int var1){
                return getPotionPotency(this);
            }
            @Override
            public int getPrice(){
                return getPotionPrice(this);
            }
        });
        potions.add(new SwiftPotion(){
            @Override
            public int getPotency(int var1){
                return getPotionPotency(this);
            }
            @Override
            public int getPrice(){
                return getPotionPrice(this);
            }
        });
        potions.add(new PoisonPotion(){
            @Override
            public int getPotency(int var1){
                return getPotionPotency(this);
            }
            @Override
            public int getPrice(){
                double mod = (double) (this.getPotency()*this.getPotency())/(this.makeCopy().getPotency()*this.makeCopy().getPotency())*haggleArticle.haggleRate; //^2 to account for poison stacking benefit
                switch (this.rarity) {
                    case COMMON:
                        return (int) (50*mod);
                    case UNCOMMON:
                        return (int) (75*mod);
                    case RARE:
                        return (int) (100*mod);
                    default:
                        return 999;
                }
            }
        });
        potions.add(new BlockPotion(){
            @Override
            public int getPotency(int var1){
                return getPotionPotency(this);
            }
            @Override
            public int getPrice(){
                return getPotionPrice(this);
            }
        });
        potions.add(new RegenPotion(){
            @Override
            public int getPotency(int var1){
                return getPotionPotency(this);
            }
            @Override
            public int getPrice(){
                double mod = (double) (this.getPotency()*this.getPotency())/(this.makeCopy().getPotency()*this.makeCopy().getPotency())*haggleArticle.haggleRate; //^2 to account for regen stacking benefit
                switch (this.rarity) {
                    case COMMON:
                        return (int) (50*mod);
                    case UNCOMMON:
                        return (int) (75*mod);
                    case RARE:
                        return (int) (100*mod);
                    default:
                        return 999;
                }
            }
        });
        potions.add(new HeartOfIron(){
            @Override
            public int getPotency(int var1){
                return getPotionPotency(this);
            }
            @Override
            public int getPrice(){
                return getPotionPrice(this);
            }
        });
        potions.add(new BloodPotion(){
            @Override
            public int getPotency(int var1){
                return getPotionPotency(this);
            }
            @Override
            public int getPrice(){
                return getPotionPrice(this);
            }
        });
        potions.add(new BottledMiracle(){
            @Override
            public int getPotency(int var1){
                return getPotionPotency(this);
            }
            @Override
            public int getPrice(){
                return getPotionPrice(this);
            }
        });
        potions.add(new CunningPotion(){
            @Override
            public int getPotency(int var1){
                return getPotionPotency(this);
            }
            @Override
            public int getPrice(){
                return getPotionPrice(this);
            }
        });
        potions.add(new DistilledChaosPotion(){
            @Override
            public int getPotency(int var1){
                return getPotionPotency(this);
            }
            @Override
            public int getPrice(){
                return getPotionPrice(this);
            }
        });
        potions.add(new EnergyPotion(){
            @Override
            public int getPotency(int var1){
                return getPotionPotency(this);
            }
            @Override
            public int getPrice(){
                return getPotionPrice(this);
            }
        });
        potions.add(new EssenceOfSteel(){
            @Override
            public int getPotency(int var1){
                return getPotionPotency(this);
            }
            @Override
            public int getPrice(){
                return getPotionPrice(this);
            }
        });
        potions.add(new FairyPotion(){
            @Override
            public int getPotency(int var1){
                return getPotionPotency(this);
            }
            @Override
            public int getPrice(){
                double mod = (double) (10+this.getPotency())/(10+this.makeCopy().getPotency())*haggleArticle.haggleRate; //+10 to account for always reviving
                switch (this.rarity) {
                    case COMMON:
                        return (int) (50*mod);
                    case UNCOMMON:
                        return (int) (75*mod);
                    case RARE:
                        return (int) (100*mod);
                    default:
                        return 999;
                }
            }
        });
        potions.add(new FearPotion(){
            @Override
            public int getPotency(int var1){
                return getPotionPotency(this);
            }
            @Override
            public int getPrice(){
                return getPotionPrice(this);
            }
        });
        potions.add(new FocusPotion(){
            @Override
            public int getPotency(int var1){
                return getPotionPotency(this);
            }
            @Override
            public int getPrice(){
                return getPotionPrice(this);
            }
        });
        potions.add(new FruitJuice(){
            @Override
            public int getPotency(int var1){
                return getPotionPotency(this);
            }
            @Override
            public int getPrice(){
                return getPotionPrice(this);
            }
        });
        potions.add(new LiquidBronze(){
            @Override
            public int getPotency(int var1){
                return getPotionPotency(this);
            }
            @Override
            public int getPrice(){
                return getPotionPrice(this);
            }
        });
        potions.add(new PotionOfCapacity(){
            @Override
            public int getPotency(int var1){
                return getPotionPotency(this);
            }
            @Override
            public int getPrice(){
                return getPotionPrice(this);
            }
        });
        potions.add(new SneckoOil(){
            @Override
            public int getPotency(int var1){
                return getPotionPotency(this);
            }
            @Override
            public int getPrice(){
                double mod = (double) (1 + this.getPotency()) /(1+this.makeCopy().getPotency())*haggleArticle.haggleRate; //+1 to account for always getting confused
                switch (this.rarity) {
                    case COMMON:
                        return (int) (50*mod);
                    case UNCOMMON:
                        return (int) (75*mod);
                    case RARE:
                        return (int) (100*mod);
                    default:
                        return 999;
                }
            }
        });
        potions.add(new SpeedPotion(){
            @Override
            public int getPotency(int var1){
                return getPotionPotency(this);
            }
            @Override
            public int getPrice(){
                return getPotionPrice(this);
            }
        });
        potions.add(new WeakenPotion(){
            @Override
            public int getPotency(int var1){
                return getPotionPotency(this);
            }
            @Override
            public int getPrice(){
                return getPotionPrice(this);
            }
        });
        potions.add(new SteroidPotion(){
            @Override
            public int getPotency(int var1){
                return getPotionPotency(this);
            }
            @Override
            public int getPrice(){
                return getPotionPrice(this);
            }
        });
        int r = AbstractDungeon.merchantRng.random(0, potions.size()-1);
        return potions.get(r%2*2+5);
    }
    
    private int getPotionPrice(AbstractPotion p){
        double mod = (double) p.getPotency() /p.makeCopy().getPotency()*haggleArticle.haggleRate;
        switch (p.rarity) {
            case COMMON:
                return (int) (50*mod);
            case UNCOMMON:
                return (int) (75*mod);
            case RARE:
                return (int) (100*mod);
            default:
                return 999;
        }
    }
    
    private int getPotionPotency(AbstractPotion p){
        return AbstractDungeon.merchantRng.random(1, p.makeCopy().getPotency()-1);
    }
}
