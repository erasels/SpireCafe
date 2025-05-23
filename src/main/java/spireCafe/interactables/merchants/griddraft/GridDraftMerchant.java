package spireCafe.interactables.merchants.griddraft;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.relics.Courier;
import spireCafe.Anniv7Mod;
import spireCafe.abstracts.AbstractArticle;
import spireCafe.abstracts.AbstractMerchant;
import spireCafe.util.TexLoader;
import spireCafe.util.Wiz;

import java.util.ArrayList;
import java.util.Collections;

public class GridDraftMerchant extends AbstractMerchant{

    private static final String ID = GridDraftMerchant.class.getSimpleName();
    private static final CharacterStrings characterStrings = CardCrawlGame.languagePack.getCharacterString(Anniv7Mod.makeID(ID));
    private static final Texture BG_TEXTURE = TexLoader.getTexture(Anniv7Mod.makeMerchantPath("griddraft/rug.png"));
    private static final String MERCHANT_STR = Anniv7Mod.makeMerchantPath("griddraft/green_merchant/");
    private static final float PITCH_VAR = 0.85F;

    private static float HB_W = 160.0F;
    private static float HB_H = 200.0F;
    private AbstractArticle[][] shopGrid;

    public GridDraftMerchant(float animationX, float animationY) {
        super(animationX, animationY, HB_W, HB_H);
        this.shopGrid = new AbstractArticle[3][3];
        this.name = characterStrings.NAMES[0];
        this.authors = "Coda";
        background = new TextureRegion(BG_TEXTURE);

        loadAnimation(MERCHANT_STR + "skeleton.atlas", MERCHANT_STR + "skeleton.json", 1.0F);
        this.state.setAnimation(0, "idle", true);
        this.facingDirection = FacingDirection.FORWARD;
    }

    @Override
    protected void rollShop() {
        
        articles.add(new GridPurchaseArticle(this, 0, true));
        articles.add(new GridPurchaseArticle(this, 1, true));
        articles.add(new GridPurchaseArticle(this, 2, true));
        articles.add(new GridPurchaseArticle(this, 0, false));
        articles.add(new GridPurchaseArticle(this, 1, false));
        articles.add(new GridPurchaseArticle(this, 2, false));
        
        ArrayList<AbstractCard> cards = initCards();
        
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++){
                AbstractArticle gridCardArticle = new GridCardArticle(this, i, j, cards.remove(0), 0);
                this.shopGrid[i][j] = gridCardArticle;
                articles.add(gridCardArticle);
            }
        }
    }

    private ArrayList<AbstractCard> initCards() {
        ArrayList<AbstractCard> ret = new ArrayList<>();
        AbstractCard c;


        for (int i = 0; i < 3; i++) {
            c = AbstractDungeon.getCardFromPool(AbstractDungeon.rollRarity(), AbstractCard.CardType.ATTACK, true).makeCopy();
            while (c.color == AbstractCard.CardColor.COLORLESS) {
                c = AbstractDungeon.getCardFromPool(AbstractDungeon.rollRarity(), AbstractCard.CardType.ATTACK, true).makeCopy();
            }
            ret.add(c);

            c = AbstractDungeon.getCardFromPool(AbstractDungeon.rollRarity(), AbstractCard.CardType.SKILL, true).makeCopy();
            while (c.color == AbstractCard.CardColor.COLORLESS) {
                c = AbstractDungeon.getCardFromPool(AbstractDungeon.rollRarity(), AbstractCard.CardType.SKILL, true).makeCopy();
            }
            ret.add(c);
        }

        c = AbstractDungeon.getCardFromPool(AbstractDungeon.rollRarity(), AbstractCard.CardType.POWER, true).makeCopy();
        while (c.color == AbstractCard.CardColor.COLORLESS) {
            c = AbstractDungeon.getCardFromPool(AbstractDungeon.rollRarity(), AbstractCard.CardType.POWER, true).makeCopy();
        }
        ret.add(c);

        ret.add(AbstractDungeon.getColorlessCardFromPool(AbstractCard.CardRarity.UNCOMMON).makeCopy());
        ret.add(AbstractDungeon.getColorlessCardFromPool(AbstractCard.CardRarity.RARE).makeCopy());

        Collections.shuffle(ret, new java.util.Random(AbstractDungeon.merchantRng.randomLong()));

        return ret;
    }
    @Override
    public void onInteract() {
        super.onInteract();
        int roll = MathUtils.random(2);
        if (roll == 0) {
            CardCrawlGame.sound.playA("VO_MERCHANT_MA", PITCH_VAR);
        } else if (roll == 1) {
            CardCrawlGame.sound.playA("VO_MERCHANT_MB", PITCH_VAR);
        } else {
            CardCrawlGame.sound.playA("VO_MERCHANT_MC", PITCH_VAR);
        }
    }

    @Override
    public void onBuyArticle(AbstractArticle article) {
        if (!(article instanceof GridPurchaseArticle)){
            return;
        }
        int slot = ((GridPurchaseArticle)article).slot;
        boolean isRow = ((GridPurchaseArticle)article).isRow;

        ArrayList<AbstractArticle> tmp = getArticles(slot, isRow);
        for (AbstractArticle a : tmp) {
            if (!(a instanceof PurchasedGridCardArticle)) {
                toRemove.add(a);
            }
        }

        AbstractArticle articleToAdd;
        if (isRow) {
            for (int i = 0; i < 3; i++) {
                if (this.shopGrid[slot][i] instanceof PurchasedGridCardArticle) {
                    continue;
                }
                if (Wiz.p().hasRelic(Courier.ID)) {
                    articleToAdd = new GridCardArticle(this, slot, i, getCourierCard(), 0);
                } else {
                    articleToAdd = new PurchasedGridCardArticle(this, slot, i);
                }
                this.shopGrid[slot][i] = articleToAdd;
                toAdd.add(articleToAdd);
            }
        } else {
            for (int i = 0; i < 3; i++) {
                if (this.shopGrid[i][slot] instanceof PurchasedGridCardArticle) {
                    continue;
                }
                if (Wiz.p().hasRelic(Courier.ID)) {
                    articleToAdd = new GridCardArticle(this, i, slot, getCourierCard(), 0);
                } else {
                    articleToAdd = new PurchasedGridCardArticle(this, i, slot);
                }
                this.shopGrid[i][slot] = articleToAdd;
                toAdd.add(articleToAdd);
            }
        }
    }

    private AbstractCard getCourierCard() {
        ArrayList<CardType> tmpType = new ArrayList<>();
        tmpType.add(CardType.ATTACK);
        tmpType.add(CardType.SKILL);
        tmpType.add(CardType.POWER);
        Collections.shuffle(tmpType, new java.util.Random(AbstractDungeon.merchantRng.randomLong()));
        return AbstractDungeon.getCardFromPool(AbstractDungeon.rollRarity(), tmpType.get(0), true).makeCopy();
    }

    public boolean hasCards(int slot, boolean isRow) {
        ArrayList<AbstractArticle> tmp = getArticles(slot, isRow);
        for (AbstractArticle article : tmp) {
            if (article instanceof GridCardArticle) {
                return true;
            }
        }
        return false;
    }

    public void buyCards(int slot, boolean isRow) {
        ArrayList<AbstractArticle> tmp = getArticles(slot, isRow);
        for (AbstractArticle article : tmp) {
            article.onBuy();
        }        
    }

    public ArrayList<AbstractCard> getCards(int slot, boolean isRow) {
        ArrayList<AbstractArticle> tmp = getArticles(slot, isRow);
        ArrayList<AbstractCard> ret = new ArrayList<>();
        for (AbstractArticle article : tmp) {
            if (article instanceof GridCardArticle) {
                ret.add(((GridCardArticle)article).getCard());
            }
        }

        return ret;
    }
    
    public ArrayList<AbstractArticle> getArticles(int slot, boolean isRow) {
        ArrayList<AbstractArticle> ret = new ArrayList<>();
        if (isRow) {
            for (int i = 0; i < 3; i++) {
                ret.add(this.shopGrid[slot][i]);
            }
        } else {
            for (int i = 0; i < 3; i++) {
                ret.add(this.shopGrid[i][slot]);
            }
        }
        return ret;

    }

    public void cantBuy() {
        int roll = MathUtils.random(2);
        if (roll == 0) {
            CardCrawlGame.sound.playA("VO_MERCHANT_2A", PITCH_VAR);
        } else if (roll == 1) {
            CardCrawlGame.sound.playA("VO_MERCHANT_2B", PITCH_VAR);
        } else {
            CardCrawlGame.sound.playA("VO_MERCHANT_2C", PITCH_VAR);
        }
    }
}
