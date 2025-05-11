package spireCafe.interactables.merchants.enchanter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon.CurrentScreen;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;

import basemod.BaseMod;
import basemod.helpers.CardModifierManager;
import spireCafe.Anniv7Mod;
import spireCafe.screens.CafeMerchantScreen;

public class EnchantCardEffect extends AbstractGameEffect{
    
    public static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(Anniv7Mod.makeID(EnchantCardEffect.class.getSimpleName()));
    
    private CardGroup cards;
    private EnchanterArticle article;
    private int cost;
    private boolean gridScreenOpened = false;
    // private boolean merchantScreenOpened = false;


    public EnchantCardEffect(CardGroup cards, EnchanterArticle article, int cost) {
        this.cards = cards;
        this.article = article;
        this.cost = cost;
        this.duration = 0.0F;
    }
    @Override
    public void update() {
        if (!this.gridScreenOpened) {
            this.gridScreenOpened = true;
            AbstractDungeon.gridSelectScreen.open(cards, 1, uiStrings.TEXT[0], false, false, true, false);
        }

        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards) {
                CardModifierManager.addModifier(c, article.baseModifier.makeCopy());
                showChangedCard(c);
            }
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            article.merchant.articles.remove(article); // lmao
            AbstractDungeon.player.loseGold(this.cost);
            this.duration -= Gdx.graphics.getDeltaTime();
            ((EnchanterMerchant) article.merchant).hasPurchasedSuccesfully = true;
        }

        if (AbstractDungeon.screen == CurrentScreen.NONE){
            this.duration -= Gdx.graphics.getDeltaTime();
        }
        
        if (this.duration < 0.0F) {
            BaseMod.openCustomScreen(CafeMerchantScreen.ScreenEnum.CAFE_MERCHANT_SCREEN, article.merchant);
            AbstractDungeon.overlayMenu.showBlackScreen();
            this.isDone = true;
        }
    }

    @Override
    public void dispose() {}

    @Override
    public void render(SpriteBatch arg0) {}

    private void showChangedCard(AbstractCard c) {
        float x = Settings.WIDTH * 0.5F + MathUtils.random.nextFloat() * Settings.WIDTH * 0.75F - Settings.WIDTH * 0.375F;
        float y = Settings.HEIGHT * 0.5F + MathUtils.random.nextFloat() * Settings.HEIGHT * 0.35F - Settings.HEIGHT * 0.175F;
        AbstractDungeon.topLevelEffectsQueue.add(new ShowCardBrieflyEffect(c.makeStatEquivalentCopy(), x, y));
    }
}
