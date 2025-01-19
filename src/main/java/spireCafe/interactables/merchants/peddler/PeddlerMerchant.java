package spireCafe.interactables.merchants.peddler;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.Courier;
import spireCafe.Anniv7Mod;
import spireCafe.abstracts.AbstractArticle;
import spireCafe.abstracts.AbstractMerchant;
import spireCafe.interactables.merchants.CardArticle;
import spireCafe.interactables.merchants.PotionArticle;
import spireCafe.interactables.merchants.RelicArticle;
import spireCafe.util.TexLoader;

public class PeddlerMerchant extends AbstractMerchant {
    public static final String ID = Anniv7Mod.makeID(PeddlerMerchant.class.getSimpleName());
    private static final CharacterStrings characterStrings = CardCrawlGame.languagePack.getCharacterString(ID);
    private static final Texture BG_TEXTURE = TexLoader.getTexture(Anniv7Mod.makeMerchantPath("peddlermerchant/sack.png"));

    private static final float TOP_ROW_Y = 760.0F * Settings.yScale;
    private static final float BOTTOM_ROW_Y = 337.0F * Settings.yScale;
    private static final float DRAW_START_X = Settings.WIDTH * 0.16F;
    private static final float POTION_Y = 168.0F * Settings.scale;
    private double speechTimer;
    private NoRefunds noRefundsSign;

    private final float veryFairPriceMult = 1.5f;

    public PeddlerMerchant(float animationX, float animationY) {
        super(animationX + 75.0F * Settings.xScale, animationY - 150.0F * Settings.yScale, 320.0f, 400.0f);
        this.name = characterStrings.NAMES[0];
        this.authors = "Dayvig";
        this.img = TexLoader.getTexture(Anniv7Mod.makeMerchantPath("peddlermerchant/peddlermerchant.png"));
        this.noRefundsSign = new NoRefunds(this, Settings.WIDTH * 0.75F, 164.0F * Settings.yScale);
        articles.add(noRefundsSign);
        background = new TextureRegion(BG_TEXTURE);
        speechTimer = 1.5F;
    }

    @Override
    public void onInteract(){
        super.onInteract();
        introduce();
    }

    public void cantBuy() {
        int roll = MathUtils.random(2);
        if (roll == 0) {
            CardCrawlGame.sound.playA("VO_MERCHANT_2A", 0.2F);
        } else if (roll == 1) {
            CardCrawlGame.sound.playA("VO_MERCHANT_2B", 0.2F);
        } else {
            CardCrawlGame.sound.playA("VO_MERCHANT_2C", 0.2F);
        }
        int i = MathUtils.random(16, 23);
        createSpeechBubble(characterStrings.TEXT[i]);
    }

    public void introduce(){
        int roll = MathUtils.random(2);
        if (roll == 0) {
            CardCrawlGame.sound.playA("VO_MERCHANT_3A", 0.2F);
        } else if (roll == 1) {
            CardCrawlGame.sound.playA("VO_MERCHANT_3B", 0.2F);
        } else {
            CardCrawlGame.sound.playA("VO_MERCHANT_3C", 0.2F);
        }
        int i = MathUtils.random(0, 9);
        createSpeechBubble(characterStrings.TEXT[i]);
    }

    public void noRefunds(){
        int roll = MathUtils.random(2);
        if (roll == 0) {
            CardCrawlGame.sound.playA("VO_MERCHANT_2A", 0.2F);
        } else if (roll == 1) {
            CardCrawlGame.sound.playA("VO_MERCHANT_2B", 0.2F);
        } else {
            CardCrawlGame.sound.playA("VO_MERCHANT_2C", 0.2F);
        }
        int i = MathUtils.random(24, 29);
        createSpeechBubble(characterStrings.TEXT[i]);
    }

    public void sold() {
        int roll = MathUtils.random(2);
        if (roll == 0) {
            CardCrawlGame.sound.playA("VO_MERCHANT_3A", 0.2F);
        } else if (roll == 1) {
            CardCrawlGame.sound.playA("VO_MERCHANT_3B", 0.2F);
        } else {
            CardCrawlGame.sound.playA("VO_MERCHANT_3C", 0.2F);
        }
        createSpeechBubble(characterStrings.TEXT[MathUtils.random(10, 15)]);
    }

    public float veryFairPriceIncrease(int price){
        return (price * veryFairPriceMult) + AbstractDungeon.merchantRng.random((-price/10), (price/10));
    }

    @Override
    public void rollShop() {
        int tmp = (int) (Settings.WIDTH - DRAW_START_X * 2.0F - AbstractCard.IMG_WIDTH_S * 5.0F) / 4;
        float padX = (int) (tmp + AbstractCard.IMG_WIDTH_S) + 10.0F * Settings.scale;
        AbstractCard c;

        //Top row of 5 cards
        for (int i = 0; i < 5; i++) {
            c = CardLibrary.getAnyColorCard(AbstractCard.CardRarity.RARE).makeCopy();
            int place = i;
            AbstractCard finalC = c;
            AbstractArticle card = new CardArticle("PeddlerWare" + place, this, DRAW_START_X + AbstractCard.IMG_WIDTH_S / 2.0F + padX * place, TOP_ROW_Y, finalC, (int)veryFairPriceIncrease(AbstractCard.getPrice(c.rarity))) {
                @Override
                public void onClick() {
                    if (!canBuy()) {
                        cantBuy();
                    } else {
                        sold();
                    }
                    super.onClick();
                }

                @Override
                public void onBuy() {
                    super.onBuy();
                    cardCourierCheck(finalC, place);
                }
            };
            articles.add(card);
        }

        //2 Colorless cards on bottom row
        for (int i = 0; i < 2; i++) {
            c = AbstractDungeon.getColorlessCardFromPool(AbstractCard.CardRarity.RARE).makeCopy();
            int place = i;
            AbstractCard finalC1 = c;
            AbstractArticle ccard = new CardArticle("PeddlerColorlessCard" + place, this, DRAW_START_X + AbstractCard.IMG_WIDTH_S / 2.0F + padX * place, BOTTOM_ROW_Y, finalC1, (int)veryFairPriceIncrease(AbstractCard.getPrice(c.rarity))) {
                @Override
                public void onClick() {
                    if (!canBuy()) {
                        cantBuy();
                    } else {
                        sold();
                    }
                    super.onClick();
                }

                @Override
                public void onBuy() {
                    super.onBuy();
                    cardCourierCheck(finalC1, place);
                }
            };
            articles.add(ccard);
        }

        //Rare Potions
        for (int i = 0; i < 3; i++) {
            AbstractPotion p = AbstractDungeon.returnRandomPotion(AbstractPotion.PotionRarity.RARE, false);
            int place = i;
            PotionArticle potion = new PotionArticle("PeddlerPotion" + place, this, 968.0F * Settings.xScale + 150.0F * place * Settings.xScale, POTION_Y, p, (int)veryFairPriceIncrease(p.getPrice())) {
                @Override
                public void onClick() {
                    if (!canBuy()) {
                        cantBuy();
                    } else {
                        sold();
                    }
                    super.onClick();
                }
                @Override
                public void onBuy() {
                    super.onBuy();
                    potionCourierCheck(place);
                }
            };
            articles.add(potion);
        }

        //Rare Relics
        for (int i = 0; i < 3; i++) {
            AbstractRelic randomRelic = AbstractDungeon.returnRandomRelic(AbstractRelic.RelicTier.RARE);
            int place = i;
            AbstractArticle relic = new RelicArticle("PeddlerRelic" + place, this, 964.0F * Settings.xScale + 150.0F * place * Settings.xScale,364.0F * Settings.scale, randomRelic, (int)veryFairPriceIncrease(randomRelic.getPrice())){
                @Override
                public void onClick() {
                    if (!canBuy()) {
                        cantBuy();
                    } else {
                        sold();
                    }
                    super.onClick();
                }
                @Override
                public void onBuy() {
                    super.onBuy();
                    relicCourierCheck(place);
                }
            };
            articles.add(relic);
        }
    }

    public void cardCourierCheck(AbstractCard c, int i) {
        if (AbstractDungeon.player.hasRelic(Courier.ID)) {
            float y = TOP_ROW_Y;
            String id = "PeddlerWare" + i;
            AbstractCard tempC = CardLibrary.getAnyColorCard(AbstractCard.CardRarity.RARE).makeCopy();
            if (c.color == AbstractCard.CardColor.COLORLESS) {
                tempC = AbstractDungeon.getColorlessCardFromPool(AbstractCard.CardRarity.RARE).makeCopy();
                id = "ColorlessPeddlerWare" + i;
                y = BOTTOM_ROW_Y;
            }
            AbstractCard finalTempC = tempC;
            AbstractArticle tempCard = new CardArticle(id, this, DRAW_START_X + AbstractCard.IMG_WIDTH_S / 2.0F + ((int) ((int) (Settings.WIDTH - DRAW_START_X * 2.0F - AbstractCard.IMG_WIDTH_S * 5.0F) / 4 + AbstractCard.IMG_WIDTH_S) + 10.0F) * Settings.scale * i, y, finalTempC, (int)veryFairPriceIncrease(AbstractCard.getPrice(tempC.rarity))) {
                @Override
                public void onClick() {
                    if (!canBuy()) {
                        cantBuy();
                    } else {
                        sold();
                    }
                    super.onClick();
                }

                @Override
                public void onBuy() {
                    super.onBuy();
                    cardCourierCheck(finalTempC, i);
                }
            };
            toAdd.add(tempCard);
        }
    }

    public void potionCourierCheck(int i){
        if (AbstractDungeon.player.hasRelic(Courier.ID)) {
            AbstractPotion tempP = AbstractDungeon.returnRandomPotion(AbstractPotion.PotionRarity.RARE, false);
            PotionArticle tempPotion = new PotionArticle("PeddlerPotion" + i, this, 968.0F * Settings.xScale + 150.0F * i * Settings.xScale, POTION_Y, tempP, (int) veryFairPriceIncrease(tempP.getPrice())){
                @Override
                public void onClick() {
                    if (!canBuy()) {
                        cantBuy();
                    } else {
                        sold();
                    }
                    super.onClick();
                }
                @Override
                public void onBuy(){
                    super.onBuy();
                    potionCourierCheck(i);
                }
            };
            toAdd.add(tempPotion);
        }
    }

    public void relicCourierCheck(int i){
        if (AbstractDungeon.player.hasRelic(Courier.ID)) {
            AbstractRelic randomRelic = AbstractDungeon.returnRandomRelic(AbstractRelic.RelicTier.RARE);
            AbstractArticle relic = new RelicArticle("PeddlerRelic" + i, this, 964.0F * Settings.xScale,364.0F * Settings.scale, randomRelic, (int) veryFairPriceIncrease(randomRelic.getPrice())){
                @Override
                public void onClick() {
                    if (!canBuy()) {
                        cantBuy();
                    } else {
                        sold();
                    }
                    super.onClick();
                }
                @Override
                public void onBuy(){
                    super.onBuy();
                    relicCourierCheck(i);
                }
            };
            toAdd.add(relic);
        }
    }
}