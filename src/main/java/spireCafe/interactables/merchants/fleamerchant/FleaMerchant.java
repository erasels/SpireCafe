package spireCafe.interactables.merchants.fleamerchant;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.cards.red.Intimidate;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.potions.*;
import com.megacrit.cardcrawl.relics.*;
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

    public FleaMerchant(float animationX, float animationY) {
        super(animationX, animationY, 160.0f, 200.0f);
        this.name = characterStrings.NAMES[0];
        this.authors = "Jack Renoson";
        this.img = TexLoader.getTexture(Anniv7Mod.makeMerchantPath("example/merchant.png"));
        background = new TextureRegion(BG_TEXTURE);
    }

    @Override
    public void rollShop() {
        HaggleArticle haggleArticle = new HaggleArticle(this, Settings.WIDTH * 0.75F, 164.0F * Settings.yScale);
        articles.add(haggleArticle);

        //Use CardArticle to add cards to your shop. You can override onBuy() and getPriceIcon() to change the price from gold (default) to something else
        AbstractArticle intimidate = new CardArticle("intimidate", this, 320f * Settings.xScale,700f * Settings.yScale, new Intimidate(), 75);
        articles.add(intimidate);


        AbstractArticle potion1 = new PotionArticle("potion1", this, 620f * Settings.xScale, 700f * Settings.yScale, getUsedPotion(), 55){
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

        AbstractArticle potion2 = new PotionArticle("potion2", this, 720f * Settings.xScale, 700f * Settings.yScale, getUsedPotion(), 55){
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

        AbstractArticle potion3 = new PotionArticle("potion3", this, 820f * Settings.xScale, 700f * Settings.yScale, getUsedPotion(), 55){
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
                return 1;
            }});
        potions.add(new DexterityPotion(){
            @Override
            public int getPotency(int var1){
                return 1;
            }});
        potions.add(new FirePotion(){
            @Override
            public int getPotency(int var1){
                return 10;
            }});
        potions.add(new ExplosivePotion(){
            @Override
            public int getPotency(int var1){
                return 5;
            }});
        potions.add(new SwiftPotion(){
            @Override
            public int getPotency(int var1){
                return 2;
            }});
        potions.add(new PoisonPotion(){
            @Override
            public int getPotency(int var1){
                return 4;
            }});
        potions.add(new BlockPotion(){
            @Override
            public int getPotency(int var1){
                return 6;
            }});
        potions.add(new RegenPotion(){
            @Override
            public int getPotency(int var1){
                return 3;
            }});
        potions.add(new HeartOfIron(){
            @Override
            public int getPotency(int var1){
                return 3;
            }});
        potions.add(new BloodPotion(){
            @Override
            public int getPotency(int var1){
                return 10;
            }});
        potions.add(new BottledMiracle(){
            @Override
            public int getPotency(int var1){
                return 1;
            }});
        potions.add(new CunningPotion(){
            @Override
            public int getPotency(int var1){
                return 2;
            }});
        potions.add(new DistilledChaosPotion(){
            @Override
            public int getPotency(int var1){
                return 2;
            }});
        potions.add(new EnergyPotion(){
            @Override
            public int getPotency(int var1){
                return 1;
            }});
        potions.add(new EssenceOfSteel(){
            @Override
            public int getPotency(int var1){
                return 2;
            }});
        potions.add(new FairyPotion(){
            @Override
            public int getPotency(int var1){
                return 15;
            }});
        potions.add(new FearPotion(){
            @Override
            public int getPotency(int var1){
                return 2;
            }});
        potions.add(new FocusPotion(){
            @Override
            public int getPotency(int var1){
                return 1;
            }});
        potions.add(new FruitJuice(){
            @Override
            public int getPotency(int var1){
                return 3;
            }});
        potions.add(new LiquidBronze(){
            @Override
            public int getPotency(int var1){
                return 2;
            }});
        potions.add(new PotionOfCapacity(){
            @Override
            public int getPotency(int var1){
                return 1;
            }});
        potions.add(new SneckoOil(){
            @Override
            public int getPotency(int var1){
                return 3;
            }});
        potions.add(new SpeedPotion(){
            @Override
            public int getPotency(int var1){
                return 3;
            }});
        potions.add(new WeakenPotion(){
            @Override
            public int getPotency(int var1){
                return 2;
            }});
        potions.add(new SteroidPotion(){
            @Override
            public int getPotency(int var1){
                return 3;
            }});
        int r = AbstractDungeon.merchantRng.random(potions.size());
        return potions.get(r);

    }
}
