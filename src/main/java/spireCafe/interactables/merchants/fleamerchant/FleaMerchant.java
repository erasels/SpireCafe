package spireCafe.interactables.merchants.fleamerchant;

import basemod.cardmods.EtherealMod;
import basemod.cardmods.ExhaustMod;
import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.potions.*;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.Courier;
import com.megacrit.cardcrawl.shop.ShopScreen;
import spireCafe.Anniv7Mod;
import spireCafe.abstracts.AbstractArticle;
import spireCafe.abstracts.AbstractMerchant;
import spireCafe.cardmods.WornMod;
import spireCafe.interactables.merchants.CardArticle;
import spireCafe.interactables.merchants.PotionArticle;
import spireCafe.interactables.merchants.RelicArticle;
import spireCafe.patches.PotencySaverPatch;
import spireCafe.util.TexLoader;

import java.util.ArrayList;

public class FleaMerchant extends AbstractMerchant {
    public static final String ID = Anniv7Mod.makeID(FleaMerchant.class.getSimpleName());
    private static final CharacterStrings characterStrings = CardCrawlGame.languagePack.getCharacterString(ID);
    private static final Texture BG_TEXTURE = ImageMaster.loadImage("images/npcs/rug/deu.png");
    private final HaggleArticle haggleArticle;

    private static final float TOP_ROW_Y = 760.0F * Settings.yScale;
    private static final float BOTTOM_ROW_Y = 337.0F * Settings.yScale;
    private static final float DRAW_START_X = Settings.WIDTH * 0.16F;
    private static final float POTION_Y = 168.0F * Settings.scale;
    private double speechTimer;

    private final float upsellValue = 1.1f;

    public FleaMerchant(float animationX, float animationY) {
        super(animationX+75.0F * Settings.xScale, animationY-150.0F * Settings.yScale, 320.0f, 400.0f);
        this.name = characterStrings.NAMES[0];
        this.authors = "Jack Renoson";
        this.img = TexLoader.getTexture(Anniv7Mod.makeMerchantPath("fleamerchant/merchant.png"));
        background = new TextureRegion(BG_TEXTURE);
        this.haggleArticle = new HaggleArticle(this, Settings.WIDTH * 0.75F, 164.0F * Settings.yScale);
        articles.add(haggleArticle);
        speechTimer = 1.5F;
    }

    @Override
    public void rollShop() {
        int tmp = (int)(Settings.WIDTH - DRAW_START_X * 2.0F - AbstractCard.IMG_WIDTH_S * 5.0F) / 4;
        float padX = (int)(tmp + AbstractCard.IMG_WIDTH_S) + 10.0F * Settings.scale;
        AbstractCard c;
        for (int i = 0; i < 2; i++) {
            c = AbstractDungeon.getCardFromPool(AbstractDungeon.rollRarity(), AbstractCard.CardType.ATTACK, true).makeCopy();
            if(c.rarity!= AbstractCard.CardRarity.COMMON){wearCardOut(c);}
            AbstractCard finalC3 = c;
            int finalI = i;
            AbstractArticle card = new CardArticle("colorAttack" + finalI, this, DRAW_START_X + AbstractCard.IMG_WIDTH_S / 2.0F + padX * finalI, TOP_ROW_Y, finalC3, (int) jitter(AbstractCard.getPrice(c.rarity))){
                @Override
                public int getModifiedPrice() {
                    float finalPrice = super.getModifiedPrice();
                    return (int) (finalPrice * haggleArticle.haggleRate);
                }
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
                    cardCourierCheck(finalC3, finalI);
                }
            };
            articles.add(card);
        }
        for (int i = 0; i < 2; i++) {
            c = AbstractDungeon.getCardFromPool(AbstractDungeon.rollRarity(), AbstractCard.CardType.SKILL, true).makeCopy();
            if(c.rarity!= AbstractCard.CardRarity.COMMON){wearCardOut(c);}
            AbstractCard finalC = c;
            int finalI = i;
            AbstractArticle card = new CardArticle("colorSkill" + finalI, this, DRAW_START_X + AbstractCard.IMG_WIDTH_S / 2.0F + padX * (2+ finalI), TOP_ROW_Y, finalC, (int) jitter(AbstractCard.getPrice(c.rarity))){
                @Override
                public int getModifiedPrice() {
                    float finalPrice = super.getModifiedPrice();
                    return (int) (finalPrice * haggleArticle.haggleRate);
                }

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
                    cardCourierCheck(finalC, 2+ finalI);
                }
            };
            articles.add(card);
        }
        c = AbstractDungeon.getCardFromPool(AbstractDungeon.rollRarity(), AbstractCard.CardType.POWER, true).makeCopy();
        if(c.rarity!= AbstractCard.CardRarity.COMMON){wearCardOut(c);}
        AbstractCard finalC1 = c;
        AbstractArticle card = new CardArticle("colorPower", this, DRAW_START_X + AbstractCard.IMG_WIDTH_S / 2.0F + padX * 4, TOP_ROW_Y, finalC1, (int) jitter(AbstractCard.getPrice(c.rarity))){
            @Override
            public int getModifiedPrice() {
                float finalPrice = super.getModifiedPrice();
                return (int) (finalPrice * haggleArticle.haggleRate);
            }

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
                cardCourierCheck(finalC1, 4);
            }
        };
        articles.add(card);
        for (int i = 0; i < 2; i++) {
            c = AbstractDungeon.getColorlessCardFromPool(i==0?AbstractCard.CardRarity.UNCOMMON: AbstractCard.CardRarity.RARE).makeCopy();
            if(c.rarity!= AbstractCard.CardRarity.COMMON){wearCardOut(c);}
            AbstractCard finalC2 = c;
            int finalI = i;
            AbstractArticle ccard = new CardArticle("ColorlessCard" + finalI, this, DRAW_START_X + AbstractCard.IMG_WIDTH_S / 2.0F + padX * finalI, BOTTOM_ROW_Y, finalC2, (int) jitter(AbstractCard.getPrice(c.rarity))){
                @Override
                public int getModifiedPrice() {
                    float finalPrice = super.getModifiedPrice();
                    return (int) (finalPrice * haggleArticle.haggleRate);
                }

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
                    cardCourierCheck(finalC2, finalI);
                }
            };
            articles.add(ccard);
        }

        for (int i = 0; i < 3; i++) {
            AbstractPotion p = getUsedPotion();
            int finalI = i;
            PotionArticle potion = new PotionArticle("potion" + finalI, this, 968.0F * Settings.xScale + 150.0F * finalI * Settings.xScale, POTION_Y, p, p.getPrice()) {
                @Override
                public int getModifiedPrice() {
                    float finalPrice = super.getModifiedPrice();
                    return (int) (finalPrice * haggleArticle.haggleRate);
                }

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
                    potionCourierCheck(finalI);
                }
            };
            articles.add(potion);
        }

        AbstractRelic randomRelic = AbstractDungeon.returnRandomRelicEnd(ShopScreen.rollRelicTier());
        AbstractArticle relic = new RelicArticle("relic0", this, 964.0F * Settings.xScale,364.0F * Settings.scale, randomRelic, (int) jitter(randomRelic.getPrice())){
            @Override
            public int getModifiedPrice() {
                float finalPrice = super.getModifiedPrice();
                return (int)(finalPrice*haggleArticle.haggleRate);
            }

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
		        relicCourierCheck(randomRelic, 0);
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
            public int getPrice(){
                return getPotionPrice(this);
            }
        });
        potions.add(new DexterityPotion(){
            @Override
            public int getPrice(){
                return getPotionPrice(this);
            }
        });
        potions.add(new FirePotion(){
            @Override
            public int getPrice(){
                return getPotionPrice(this);
            }
        });
        potions.add(new ExplosivePotion(){
            @Override
            public int getPrice(){
                return getPotionPrice(this);
            }
        });
        potions.add(new SwiftPotion(){
            @Override
            public int getPrice(){
                return getPotionPrice(this);
            }
        });
        potions.add(new PoisonPotion(){
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
            public int getPrice(){
                return getPotionPrice(this);
            }
        });
        potions.add(new RegenPotion(){
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
            public int getPrice(){
                return getPotionPrice(this);
            }
        });
        potions.add(new BloodPotion(){
            @Override
            public int getPrice(){
                return getPotionPrice(this);
            }
        });
        potions.add(new BottledMiracle(){
            @Override
            public int getPrice(){
                return getPotionPrice(this);
            }
        });
        potions.add(new CunningPotion(){
            @Override
            public int getPrice(){
                return getPotionPrice(this);
            }
        });
        potions.add(new DistilledChaosPotion(){
            @Override
            public int getPrice(){
                return getPotionPrice(this);
            }
        });
        potions.add(new EnergyPotion(){
            @Override
            public int getPrice(){
                return getPotionPrice(this);
            }
        });
        potions.add(new EssenceOfSteel(){
            @Override
            public int getPrice(){
                return getPotionPrice(this);
            }
        });
        potions.add(new FairyPotion(){
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
            public int getPrice(){
                return getPotionPrice(this);
            }
        });
        potions.add(new FocusPotion(){
            @Override
            public int getPrice(){
                return getPotionPrice(this);
            }
        });
        potions.add(new FruitJuice(){
            @Override
            public int getPrice(){
                return getPotionPrice(this);
            }
        });
        potions.add(new LiquidBronze(){
            @Override
            public int getPrice(){
                return getPotionPrice(this);
            }
        });
        potions.add(new PotionOfCapacity(){
            @Override
            public int getPrice(){
                return getPotionPrice(this);
            }
        });
        potions.add(new SneckoOil(){
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
            public int getPrice(){
                return getPotionPrice(this);
            }
        });
        potions.add(new WeakenPotion(){
            @Override
            public int getPrice(){
                return getPotionPrice(this);
            }
        });
        potions.add(new SteroidPotion(){
            @Override
            public int getPrice(){
                return getPotionPrice(this);
            }
        });
        int r = AbstractDungeon.merchantRng.random(0, potions.size()-1);
        AbstractPotion potion = potions.get(r);
        PotencySaverPatch.PotionUseField.isDepleted.set(potion, getPotionPotency(potion));
        potion.name = characterStrings.TEXT[0].replace("{0}", potion.name);
        potion.initializeData();
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

    public double jitter(double d){
        return upsellValue*d*AbstractDungeon.merchantRng.random(0.95F, 1.05F);
    }

    public AbstractCard wearCardOut(AbstractCard c){
        boolean worn = false;
        if(!(c.baseDamage>1 || c.baseBlock>1 || c.baseMagicNumber>1 || !c.exhaust && c.type!= AbstractCard.CardType.POWER || !c.isEthereal)){
            return c;
        }
        while(!worn){
            switch(AbstractDungeon.merchantRng.random(0, 4)){
                case 0: if(c.baseDamage>1){
                    CardModifierManager.addModifier(c, new WornMod(c.rarity == AbstractCard.CardRarity.RARE?0.8f:0.9f, 1, 0));
                    worn = true;
                } break;
                case 1: if(c.baseBlock>1){
                    CardModifierManager.addModifier(c, new WornMod(1, c.rarity == AbstractCard.CardRarity.RARE?0.8f:0.9f, 0));
                    worn = true;
                } break;
                case 2: if(c.baseMagicNumber>1){
                    CardModifierManager.addModifier(c, new WornMod(1, 1, 1));
                    worn = true;
                } break;
                case 3: if(!c.exhaust && c.type!= AbstractCard.CardType.POWER){
                    CardModifierManager.addModifier(c, new ExhaustMod());
                    CardModifierManager.addModifier(c, new WornMod(1, 1, 0));
                    worn = true;
                } break;
                case 4: if(!c.isEthereal){
                    CardModifierManager.addModifier(c, new EtherealMod());
                    CardModifierManager.addModifier(c, new WornMod(1, 1, 0));
                    worn = true;
                } break;
            }
        }
        return c;
    }

    @Override
    public void onInteract() {
        super.onInteract();
        int roll = MathUtils.random(2);
        if (roll == 0) {
            CardCrawlGame.sound.playA("VO_MERCHANT_3A", 3F);
        } else if (roll == 1) {
            CardCrawlGame.sound.playA("VO_MERCHANT_3B", 3F);
        } else {
            CardCrawlGame.sound.playA("VO_MERCHANT_3C", 3F);
        }
        if (haggleArticle.haggleOdds == 0) {
            createSpeechBubble(characterStrings.TEXT[MathUtils.random(1, 9)]);
        } else {
            createSpeechBubble(characterStrings.TEXT[MathUtils.random(3, 11)]);
        }
    }

    public void sold(){
        if(haggleArticle.haggleOdds==0) {
            createSpeechBubble(characterStrings.TEXT[MathUtils.random(34, 41)]);
        } else {
            createSpeechBubble(characterStrings.TEXT[MathUtils.random(36, 43)]);
        }
    }

    public void cantBuy() {
        int roll = MathUtils.random(2);
        if (roll == 0) {
            CardCrawlGame.sound.playA("VO_MERCHANT_2A", 3F);
        } else if (roll == 1) {
            CardCrawlGame.sound.playA("VO_MERCHANT_2B", 3F);
        } else {
            CardCrawlGame.sound.playA("VO_MERCHANT_2C", 3F);
        }
        int i;
        if(haggleArticle.haggleOdds==0) {
            i = MathUtils.random(12, 19);
        } else {
            i = MathUtils.random(14, 21);
        }
        createSpeechBubble(characterStrings.TEXT[i]);
    }

    public void haggleBubble(boolean success){
        if(success){
            int roll = MathUtils.random(5);
            if (roll == 0) {
                CardCrawlGame.sound.playA("VO_MERCHANT_MA", 3F);
            } else if (roll == 1) {
                CardCrawlGame.sound.playA("VO_MERCHANT_MB", 3F);
            } else if (roll == 2) {
                CardCrawlGame.sound.playA("VO_MERCHANT_MC", 3F);
            } else if (roll == 3) {
                CardCrawlGame.sound.playA("VO_MERCHANT_3A", 3F);
            } else if (roll == 4) {
                CardCrawlGame.sound.playA("VO_MERCHANT_3B", 3F);
            } else {
                CardCrawlGame.sound.playA("VO_MERCHANT_3C", 3F);
            }
            createSpeechBubble(characterStrings.TEXT[MathUtils.random(22, 27)]);

        } else {
            int roll = MathUtils.random(2);
            if (roll == 0) {
                CardCrawlGame.sound.playA("VO_MERCHANT_2A", 3F);
            } else if (roll == 1) {
                CardCrawlGame.sound.playA("VO_MERCHANT_2B", 3F);
            } else {
                CardCrawlGame.sound.playA("VO_MERCHANT_2C", 3F);
            }
            createSpeechBubble(characterStrings.TEXT[MathUtils.random(28, 33)]);
        }
    }

    @Override
    public void updateShop(){
        super.updateShop();
        this.speechTimer -= Gdx.graphics.getDeltaTime();
        if (this.speechTimer < 0.0F) {
            if (haggleArticle.haggleOdds == 0) {
                createSpeechBubble(characterStrings.TEXT[MathUtils.random(1, 9)]);
            } else {
                createSpeechBubble(characterStrings.TEXT[MathUtils.random(3, 11)]);
            }
            this.speechTimer = MathUtils.random(40.0F, 60.0F);
        }
    }

    public void cardCourierCheck(AbstractCard c, int i){
        if (AbstractDungeon.player.hasRelic(Courier.ID)) {
            float y = TOP_ROW_Y;
            String id = "ColoredCard" + i;
            float colorlessBump = 1f;
            AbstractCard tempC = AbstractDungeon.getCardFromPool(AbstractDungeon.rollRarity(), c.type, true).makeCopy();
            if(c.color == AbstractCard.CardColor.COLORLESS){
                boolean uncommon = AbstractDungeon.merchantRng.random() > AbstractDungeon.colorlessRareChance;
                tempC = AbstractDungeon.getColorlessCardFromPool(uncommon?AbstractCard.CardRarity.UNCOMMON: AbstractCard.CardRarity.RARE).makeCopy();
                id = "ColorlessCard" + i;
                y = BOTTOM_ROW_Y;
                colorlessBump = 1.2f;
            }
            if(tempC.rarity!= AbstractCard.CardRarity.COMMON){wearCardOut(tempC);}
            AbstractCard finalTempC = tempC;
            AbstractArticle tempCard = new CardArticle(id, this, DRAW_START_X + AbstractCard.IMG_WIDTH_S / 2.0F + ((int)((int)(Settings.WIDTH - DRAW_START_X * 2.0F - AbstractCard.IMG_WIDTH_S * 5.0F) / 4 + AbstractCard.IMG_WIDTH_S) + 10.0F) * Settings.scale * i, y, finalTempC, (int) jitter(colorlessBump*AbstractCard.getPrice(tempC.rarity))){
                @Override
                public int getModifiedPrice() {
                    int finalPrice = super.getModifiedPrice();
                    return (int) (finalPrice * haggleArticle.haggleRate);
                }
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
                    cardCourierCheck(finalTempC, i);
                }
            };
            toAdd.add(tempCard);
        }
    }

    public void potionCourierCheck(int i){
        if (AbstractDungeon.player.hasRelic(Courier.ID)) {
            AbstractPotion tempP = getUsedPotion();
                PotionArticle tempPotion = new PotionArticle("potion" + i, this, 968.0F * Settings.xScale + 150.0F * i * Settings.xScale, POTION_Y, tempP, (int) jitter(tempP.getPrice())){
                    @Override
                    public int getModifiedPrice() {
                        int finalPrice = super.getModifiedPrice();
                        return (int) (finalPrice * haggleArticle.haggleRate);
                    }
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

    public void relicCourierCheck(AbstractRelic r, int i){
        if (AbstractDungeon.player.hasRelic(Courier.ID) || r.relicId.equals(Courier.ID)) {
            AbstractRelic tempR = AbstractDungeon.returnRandomRelicEnd(ShopScreen.rollRelicTier());
            AbstractArticle tempRelic = new RelicArticle("relic" + i, this, 964.0F * Settings.xScale + 150.0F * i * Settings.xScale,364.0F * Settings.scale, tempR, (int) jitter(tempR.getPrice())){
                @Override
                public int getModifiedPrice() {
                    int finalPrice = super.getModifiedPrice();
                    return (int) (finalPrice * haggleArticle.haggleRate);
                }
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
                    relicCourierCheck(r, i);
                    }
                };
            toAdd.add(tempRelic);
        }
    }
}
