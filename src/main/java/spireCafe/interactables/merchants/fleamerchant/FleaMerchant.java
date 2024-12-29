package spireCafe.interactables.merchants.fleamerchant;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.colorless.Blind;
import com.megacrit.cardcrawl.cards.red.Intimidate;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GameDictionary;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.potions.*;
import com.megacrit.cardcrawl.relics.*;
import com.megacrit.cardcrawl.shop.ShopScreen;
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
    private static final Texture BG_TEXTURE = ImageMaster.loadImage("images/npcs/rug/deu.png");
    private final HaggleArticle haggleArticle;

    private static final float TOP_ROW_Y = 760.0F * Settings.yScale;
    private static final float BOTTOM_ROW_Y = 337.0F * Settings.yScale;
    private static final float DRAW_START_X = Settings.WIDTH * 0.16F;
    private static final float POTION_Y = 168.0F * Settings.scale;

    public FleaMerchant(float animationX, float animationY) {
        super(animationX, animationY, 160.0f, 200.0f);
        this.name = characterStrings.NAMES[0];
        this.authors = "Jack Renoson";
        this.img = TexLoader.getTexture(Anniv7Mod.makeMerchantPath("fleamerchant/merchant.png"));
        background = new TextureRegion(BG_TEXTURE);
        this.haggleArticle = new HaggleArticle(this, Settings.WIDTH * 0.75F, 164.0F * Settings.yScale);
        articles.add(haggleArticle);
    }

    @Override
    public void rollShop() {
        int tmp = (int)(Settings.WIDTH - DRAW_START_X * 2.0F - AbstractCard.IMG_WIDTH_S * 5.0F) / 4;
        float padX = (int)(tmp + AbstractCard.IMG_WIDTH_S) + 10.0F * Settings.scale;
        AbstractCard c;
        for (int i = 0; i < 2; i++) {
            c = AbstractDungeon.getCardFromPool(AbstractDungeon.rollRarity(), AbstractCard.CardType.ATTACK, true).makeCopy();
            AbstractArticle card = new CardArticle("colorAttack" + i, this, DRAW_START_X + AbstractCard.IMG_WIDTH_S / 2.0F + padX * i, TOP_ROW_Y, c, (int) jitter(AbstractCard.getPrice(c.rarity))){
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
                    return (int) (finalPrice * haggleArticle.haggleRate);
                }
            };
            articles.add(card);
        }
        for (int i = 0; i < 2; i++) {
            c = AbstractDungeon.getCardFromPool(AbstractDungeon.rollRarity(), AbstractCard.CardType.SKILL, true).makeCopy();
            AbstractArticle card = new CardArticle("colorSkill" + i, this, DRAW_START_X + AbstractCard.IMG_WIDTH_S / 2.0F + padX * (2+i), TOP_ROW_Y, c, (int) jitter(AbstractCard.getPrice(c.rarity))){
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
                    return (int) (finalPrice * haggleArticle.haggleRate);
                }
            };
            articles.add(card);
        }
        c = AbstractDungeon.getCardFromPool(AbstractDungeon.rollRarity(), AbstractCard.CardType.POWER, true).makeCopy();
        AbstractArticle card = new CardArticle("colorPower", this, DRAW_START_X + AbstractCard.IMG_WIDTH_S / 2.0F + padX * 4, TOP_ROW_Y, c, (int) jitter(AbstractCard.getPrice(c.rarity))){
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
                return (int) (finalPrice * haggleArticle.haggleRate);
            }
        };
        articles.add(card);

        for (int i = 0; i < 2; i++) {
            c = AbstractDungeon.getColorlessCardFromPool(i==0?AbstractCard.CardRarity.UNCOMMON: AbstractCard.CardRarity.RARE).makeCopy();
            AbstractArticle ccard = new CardArticle("ColorlessCard" + i, this, DRAW_START_X + AbstractCard.IMG_WIDTH_S / 2.0F + padX * i, BOTTOM_ROW_Y, c, (int) jitter(AbstractCard.getPrice(c.rarity))){
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
                    return (int) (finalPrice * haggleArticle.haggleRate);
                }
            };
            articles.add(ccard);
        }

        for (int i = 0; i < 3; i++) {
            AbstractPotion p = getUsedPotion();
            PotionArticle potion = new PotionArticle("potion", this, 968.0F * Settings.xScale + 150.0F * i * Settings.xScale, POTION_Y, p, p.getPrice()) {
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
                    return (int) (finalPrice * haggleArticle.haggleRate);
                }
            };
            articles.add(potion);
        }

        AbstractRelic r = AbstractDungeon.returnRandomRelicEnd(ShopScreen.rollRelicTier());
        AbstractArticle relic = new RelicArticle("relic", this, 964.0F * Settings.xScale,364.0F * Settings.scale, r, (int) jitter(r.getPrice())){
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
            private int tempPotency = getPotionPotency(this);
            @Override
            public int getPotency(int var1){
                return tempPotency;
            }
            @Override
            public int getPrice(){
                return getPotionPrice(this);
            }
        });
        potions.add(new DexterityPotion(){
            private int tempPotency = getPotionPotency(this);
            @Override
            public int getPotency(int var1){
                return tempPotency;
            }
            @Override
            public int getPrice(){
                return getPotionPrice(this);
            }
        });
        potions.add(new FirePotion(){
            private int tempPotency = getPotionPotency(this);
            @Override
            public int getPotency(int var1){
                return tempPotency;
            }
            @Override
            public int getPrice(){
                return getPotionPrice(this);
            }
        });
        potions.add(new ExplosivePotion(){
            private int tempPotency = getPotionPotency(this);
            @Override
            public int getPotency(int var1){
                return tempPotency;
            }
            @Override
            public int getPrice(){
                return getPotionPrice(this);
            }
        });
        potions.add(new SwiftPotion(){
            private int tempPotency = getPotionPotency(this);
            @Override
            public int getPotency(int var1){
                return tempPotency;
            }
            @Override
            public int getPrice(){
                return getPotionPrice(this);
            }
        });
        potions.add(new PoisonPotion(){
            private int tempPotency = getPotionPotency(this);
            @Override
            public int getPotency(int var1){
                return tempPotency;
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
            private int tempPotency = getPotionPotency(this);
            @Override
            public int getPotency(int var1){
                return tempPotency;
            }
            @Override
            public int getPrice(){
                return getPotionPrice(this);
            }
        });
        potions.add(new RegenPotion(){
            private int tempPotency = getPotionPotency(this);
            @Override
            public int getPotency(int var1){
                return tempPotency;
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
            private int tempPotency = getPotionPotency(this);
            @Override
            public int getPotency(int var1){
                return tempPotency;
            }
            @Override
            public int getPrice(){
                return getPotionPrice(this);
            }
        });
        potions.add(new BloodPotion(){
            private int tempPotency = getPotionPotency(this);
            @Override
            public int getPotency(int var1){
                return tempPotency;
            }
            @Override
            public int getPrice(){
                return getPotionPrice(this);
            }
        });
        potions.add(new BottledMiracle(){
            private int tempPotency = getPotionPotency(this);
            @Override
            public int getPotency(int var1){
                return tempPotency;
            }
            @Override
            public int getPrice(){
                return getPotionPrice(this);
            }
        });
        potions.add(new CunningPotion(){
            private int tempPotency = getPotionPotency(this);
            @Override
            public int getPotency(int var1){
                return tempPotency;
            }
            @Override
            public int getPrice(){
                return getPotionPrice(this);
            }
        });
        potions.add(new DistilledChaosPotion(){
            private int tempPotency = getPotionPotency(this);
            @Override
            public int getPotency(int var1){
                return tempPotency;
            }
            @Override
            public int getPrice(){
                return getPotionPrice(this);
            }
        });
        potions.add(new EnergyPotion(){
            private int tempPotency = getPotionPotency(this);
            @Override
            public int getPotency(int var1){
                return tempPotency;
            }
            @Override
            public int getPrice(){
                return getPotionPrice(this);
            }
        });
        potions.add(new EssenceOfSteel(){
            private int tempPotency = getPotionPotency(this);
            @Override
            public int getPotency(int var1){
                return tempPotency;
            }
            @Override
            public int getPrice(){
                return getPotionPrice(this);
            }
        });
        potions.add(new FairyPotion(){
            private int tempPotency = getPotionPotency(this);
            @Override
            public int getPotency(int var1){
                return tempPotency;
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
            private int tempPotency = getPotionPotency(this);
            @Override
            public int getPotency(int var1){
                return tempPotency;
            }
            @Override
            public int getPrice(){
                return getPotionPrice(this);
            }
        });
        potions.add(new FocusPotion(){
            private int tempPotency = getPotionPotency(this);
            @Override
            public int getPotency(int var1){
                return tempPotency;
            }
            @Override
            public int getPrice(){
                return getPotionPrice(this);
            }
        });
        potions.add(new FruitJuice(){
            private int tempPotency = getPotionPotency(this);
            @Override
            public int getPotency(int var1){
                return tempPotency;
            }
            @Override
            public int getPrice(){
                return getPotionPrice(this);
            }
        });
        potions.add(new LiquidBronze(){
            private int tempPotency = getPotionPotency(this);
            @Override
            public int getPotency(int var1){
                return tempPotency;
            }
            @Override
            public int getPrice(){
                return getPotionPrice(this);
            }
        });
        potions.add(new PotionOfCapacity(){
            private int tempPotency = getPotionPotency(this);
            @Override
            public int getPotency(int var1){
                return tempPotency;
            }
            @Override
            public int getPrice(){
                return getPotionPrice(this);
            }
        });
        potions.add(new SneckoOil(){
            private int tempPotency = getPotionPotency(this);
            @Override
            public int getPotency(int var1){
                return tempPotency;
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
            private int tempPotency = getPotionPotency(this);
            @Override
            public int getPotency(int var1){
                return tempPotency;
            }
            @Override
            public int getPrice(){
                return getPotionPrice(this);
            }
        });
        potions.add(new WeakenPotion(){
            private int tempPotency = getPotionPotency(this);
            @Override
            public int getPotency(int var1){
                return tempPotency;
            }
            @Override
            public int getPrice(){
                return getPotionPrice(this);
            }
        });
        potions.add(new SteroidPotion(){
            private int tempPotency = getPotionPotency(this);
            @Override
            public int getPotency(int var1){
                return tempPotency;
            }
            @Override
            public int getPrice(){
                return getPotionPrice(this);
            }
        });
        int r = AbstractDungeon.merchantRng.random(0, potions.size()-1);
        AbstractPotion potion = potions.get(r);
        potion.initializeData();
        potion.name = characterStrings.TEXT[0] + potion.name;
        return potion;
    }
    
    private int getPotionPrice(AbstractPotion p){
        double mod = jitter((double) p.getPotency() /p.makeCopy().getPotency()*haggleArticle.haggleRate);
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

    private double jitter(double d){
        return d*AbstractDungeon.merchantRng.random(0.95F, 1.05F);
    }
}
