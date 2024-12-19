package spireCafe.interactables.merchants.secretshop;

import java.util.ArrayList;
import java.util.Collections;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardRarity;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.AbstractRelic.RelicTier;
import com.megacrit.cardcrawl.ui.DialogWord.AppearEffect;
import com.megacrit.cardcrawl.vfx.ShopSpeechBubble;
import com.megacrit.cardcrawl.vfx.SpeechTextEffect;

import spireCafe.Anniv7Mod;
import spireCafe.abstracts.AbstractMerchant;
import spireCafe.util.TexLoader;

public class SecretShopMerchant extends AbstractMerchant {

    private static final float TOP_ROW_Y = 760.0F * Settings.yScale;
    private static final float BOTTOM_ROW_Y = 337.0F * Settings.yScale;
    private static final float DRAW_START_X = Settings.WIDTH * 0.16F;
    
    private static final String ID = SecretShopMerchant.class.getSimpleName();
    private static final CharacterStrings characterStrings = CardCrawlGame.languagePack.getCharacterString(Anniv7Mod.makeID(ID));
    private static final String[] TEXT = characterStrings.TEXT;
    private static final Texture RUG_TEXTURE = TexLoader.getTexture(Anniv7Mod.makeMerchantPath("secretshop/rug.png"));
    private static final String MERCHANT_STR = Anniv7Mod.makeMerchantPath("secretshop/red_merchant/");

    private static final float SPEECH_DUR = 4.0F;
    private static final float PITCH_VAR = 0.4F;
    private static final float SPEECH_TEXT_R_X = 164.0F * Settings.scale;
    private static final float SPEECH_TEXT_L_X = -166.0F * Settings.scale;
    private static final float SPEECH_TEXT_Y = 126.0F * Settings.scale;
    private float idleSpeechTimer = 0.0F;
    private ShopSpeechBubble speechBubble = null;
    private SpeechTextEffect speechText = null;

    public ArrayList<AbstractCard> cards = new ArrayList<>();
    public ArrayList<AbstractRelic> relics = new ArrayList<>();
    public ArrayList<AbstractPotion> potions = new ArrayList<>();
    public boolean identifyMode;
    public IdentifyArticle idArticle;


    public SecretShopMerchant(float animX, float animY) {
        super(animX, animY, 150.0f, 175.0f);
        this.name = characterStrings.NAMES[0];
        this.authors = "Coda";
        background = new TextureRegion(RUG_TEXTURE);
        this.identifyMode = false;
        loadAnimation(MERCHANT_STR + "skeleton.atlas", MERCHANT_STR + "skeleton.json", 1.0F);
        this.state.setAnimation(0, "idle", true);
        
    }
    
    @Override
    protected void rollShop() {
        float xPos;
        float yPos;

        xPos = Settings.WIDTH * 0.75F;
        yPos = 164.0F * Settings.yScale;
        this.idArticle = new IdentifyArticle(this, xPos, yPos);
        articles.add(this.idArticle);

        initCards();
        int cost;
        int tmp = (int)(Settings.WIDTH - DRAW_START_X * 2.0F - AbstractCard.IMG_WIDTH_S * 5.0F) / 4; // ???
        float padX = (int)(tmp + AbstractCard.IMG_WIDTH_S) + 10.0F * Settings.scale;
        for (int i = 0; i < 5; i++) {
            xPos = DRAW_START_X + AbstractCard.IMG_WIDTH_S / 2.0F + padX * i;
            yPos = TOP_ROW_Y;
            UnidentifiedCard unidentifiedCard = new UnidentifiedCard();
            unidentifiedCard.targetDrawScale = 0.75F;
            cost = setCardBasePrice();
            IdentifyCardArticle tmpArticle = new IdentifyCardArticle(this, this.idArticle, xPos, yPos, unidentifiedCard, this.cards.get(i), cost);
            articles.add(tmpArticle);
        }
        for (int i = 0; i < 2; i++) {
            xPos = DRAW_START_X + AbstractCard.IMG_WIDTH_S / 2.0F + padX * i;
            yPos = BOTTOM_ROW_Y;
            UnidentifiedCard unidentifiedCard = new UnidentifiedCard();
            unidentifiedCard.targetDrawScale = 0.75F;
            cost = setCardBasePrice();
            IdentifyCardArticle tmpArticle = new IdentifyCardArticle(this, this.idArticle, xPos, yPos, unidentifiedCard, this.cards.get(i + 5), cost);
            articles.add(tmpArticle);
        }

        initRelics();
        for (int i = 0; i < relics.size(); i++) {
            IdentifyRelicArticle tmpArticle = new IdentifyRelicArticle(this, i, this.relics.get(i), setRelicBasePrice());
            articles.add(tmpArticle);
        }

        initPotions();
        for (int i = 0; i < potions.size(); i++) {
            IdentifyPotionArticle tmpArticle = new IdentifyPotionArticle(this, i, this.potions.get(i), setPotionBasePrice());
            articles.add(tmpArticle);
        }

    }

    @Override
    public void update() {
        super.update();
        updateSpeech();
    }
        
    private void initCards() {
        AbstractCard c;
        
        for (int i = 0; i < 5; i++) {
            c = getCard(false);
            this.cards.add(c);
        }
        
        for (int i = 0; i < 2; i++) {
            c = getCard(true);
            this.cards.add(c);
        }

        Collections.shuffle(this.cards, new java.util.Random(AbstractDungeon.merchantRng.randomLong()));
    }

    private void initRelics() {
        AbstractRelic r;
        for (int i = 0; i < 3; i++) {
            if (i != 2){ 
                int roll = AbstractDungeon.merchantRng.random(99);
                RelicTier tmpTier = RelicTier.COMMON;
                if (roll >= 48) {
                    tmpTier = RelicTier.UNCOMMON;
                }
                if (roll >= 82) {
                    tmpTier = RelicTier.RARE;
                }
                r = AbstractDungeon.returnRandomRelicEnd(tmpTier);
            } else {
                r = AbstractDungeon.returnRandomRelicEnd(AbstractRelic.RelicTier.SHOP);
            }
            this.relics.add(r);
        }
        Collections.shuffle(this.relics, new java.util.Random(AbstractDungeon.merchantRng.randomLong()));
    }

    private void initPotions() {
        for (int i = 0; i < 3; i++) {
            potions.add(AbstractDungeon.returnRandomPotion());
        }
    }

    public static int setCardBasePrice() {
        int ret = (int)(75 * AbstractDungeon.merchantRng.random(0.2F, 0.6F));
        return ret;
    }

    public static int setRelicBasePrice() {
        int ret = (int)(125 * AbstractDungeon.merchantRng.random(0.6F, 0.75F));
        return ret;
    }

    public static int setPotionBasePrice() {
        int ret = (int)(75 * AbstractDungeon.merchantRng.random(0.5F, 0.75F));
        return ret;
    }

    public static AbstractCard getCard(boolean isColorless) {
        AbstractCard c;
        CardRarity tmpRarity;
        
        if (isColorless) {
            tmpRarity = CardRarity.UNCOMMON;
            if (AbstractDungeon.merchantRng.random() < AbstractDungeon.colorlessRareChance) {
                tmpRarity = CardRarity.RARE;
            }
            c = AbstractDungeon.getColorlessCardFromPool(tmpRarity).makeCopy();
        } else {
            ArrayList<CardType> tmpType = new ArrayList<>();
            tmpType.add(CardType.ATTACK);
            tmpType.add(CardType.SKILL);
            tmpType.add(CardType.POWER);
            Collections.shuffle(tmpType, new java.util.Random(AbstractDungeon.merchantRng.randomLong()));

            Collections.shuffle(tmpType, new java.util.Random(AbstractDungeon.merchantRng.randomLong()));
            c = AbstractDungeon.getCardFromPool(AbstractDungeon.rollRarity(), tmpType.get(0), true).makeCopy();
        }

        return c;
    }

    public void cantBuy() {
        int roll = MathUtils.random(2);
        if (roll == 0) {
            CardCrawlGame.sound.play("VO_MERCHANT_2A", PITCH_VAR);
        } else if (roll == 1) {
            CardCrawlGame.sound.play("VO_MERCHANT_2B", PITCH_VAR);
        } else {
            CardCrawlGame.sound.play("VO_MERCHANT_2C", PITCH_VAR);
        }
        int i = MathUtils.random(14, 21);
        createSpeechBubble(TEXT[i]);
    }

    public void cantIdentify() {
        int roll = MathUtils.random(2);
        if (roll == 0) {
            CardCrawlGame.sound.play("VO_MERCHANT_2A", PITCH_VAR);
        } else if (roll == 1) {
            CardCrawlGame.sound.play("VO_MERCHANT_2B", PITCH_VAR);
        } else {
            CardCrawlGame.sound.play("VO_MERCHANT_2C", PITCH_VAR);
        }
        int i = MathUtils.random(7, 13);
        createSpeechBubble(TEXT[i]);
    }
        
    private void createSpeechBubble(String msg) {
        if (this.speechBubble != null) {
            if (this.speechBubble.duration > 0.3F) {
                this.speechBubble.duration = 0.3F;
                this.speechText.duration = 0.3F;
            }
        }
        boolean isRight = MathUtils.randomBoolean();
        float x = MathUtils.random(660.0F, 1260.0F) * Settings.scale;
        float y = Settings.HEIGHT - 380.0F * Settings.scale;
        this.speechBubble = new ShopSpeechBubble(x, y, SPEECH_DUR, msg, isRight);
        float offset_x = isRight ? SPEECH_TEXT_R_X : SPEECH_TEXT_L_X;
        this.speechText = new SpeechTextEffect(x + offset_x, y + SPEECH_TEXT_Y, SPEECH_DUR, msg, AppearEffect.BUMP_IN);
        AbstractDungeon.topLevelEffectsQueue.add(this.speechBubble);
        AbstractDungeon.topLevelEffectsQueue.add(this.speechText);

    }

    private void updateSpeech() {
        if (this.speechBubble != null) {
            this.speechBubble.update();
            if (this.speechBubble.hb.hovered && this.speechBubble.duration > 0.3F) {
                this.speechBubble.duration = 0.3F;
                this.speechText.duration = 0.3F;
            }
            if (this.speechBubble.isDone) {
                this.speechBubble = null;
            }
        }
        if (speechText != null) {
            this.speechText.update();
            if (this.speechText.isDone) {
                this.speechText = null;
            }
        }

        this.idleSpeechTimer -= Gdx.graphics.getDeltaTime();
        if (this.speechBubble == null && this.speechText == null && this.idleSpeechTimer <= 0.0f) {
            this.idleSpeechTimer = MathUtils.random(40.0f, 60.0f);
            int roll = MathUtils.random(5);
            if (roll == 0) {
                CardCrawlGame.sound.play("VO_MERCHANT_MA", PITCH_VAR);
            } else if (roll == 1) {
                CardCrawlGame.sound.play("VO_MERCHANT_MB", PITCH_VAR);
            } else if (roll == 2) {
                CardCrawlGame.sound.play("VO_MERCHANT_MC", PITCH_VAR);
            } else if (roll == 3) {
                CardCrawlGame.sound.play("VO_MERCHANT_3A", PITCH_VAR);
            } else if (roll == 4) {
                CardCrawlGame.sound.play("VO_MERCHANT_3B", PITCH_VAR);
            } else {
                CardCrawlGame.sound.play("VO_MERCHANT_3C", PITCH_VAR);
            }
            createSpeechBubble(TEXT[MathUtils.random(0,6)]);
        }
    }

    // public void createSpeechBubble(String msg) {
    //     if (AbstractDungeon.topLevelEffects.stream().noneMatch(e -> e instanceof TopLevelSpeechEffect)) {
    //         // float x = MathUtils.random(660.0F, 1260.0F) * Settings.scale;
    //         float x = Settings.WIDTH - 1500.0F * Settings.scale;
    //         float y = Settings.HEIGHT - MathUtils.random(150.0F, 930.0F) * Settings.scale;
            
    //         AbstractDungeon.topLevelEffects.add(new TopLevelSpeechEffect(x, y, 4.0F, msg, false));
    //     }
    // }
    
}
