package spireCafe.interactables.merchants.secretshop;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

import spireCafe.Anniv7Mod;
import spireCafe.abstracts.AbstractMerchant;
import spireCafe.interactables.merchants.CardArticle;


public class IdentifyCardArticle extends CardArticle{
    
    private static final String ID = Anniv7Mod.makeID(IdentifyCardArticle.class.getSimpleName());
    private AbstractCard hiddenCard;
    private UnidentifiedCard unidentifiedCard;
    private IdentifyArticle identifyArticle;

    public IdentifyCardArticle(AbstractMerchant merchant, IdentifyArticle identifyArticle, float x, float y, UnidentifiedCard unidentifiedCard, int basePrice) {
        super(ID, merchant, x, y, unidentifiedCard, basePrice);
        this.unidentifiedCard = unidentifiedCard;
        this.hiddenCard = unidentifiedCard.hiddenCard;
        this.identifyArticle = identifyArticle;
    }

    @Override
    public void onClick() {
        if (identifyArticle.isIdentifyMode) {
            identifyCard();
        } else {
            super.onClick();
        }
    }

    private void identifyCard() {
        if (!identifyArticle.canBuy()) {
            return;
        }
        identifyArticle.onBuy();
        unidentifiedCard.identify();
        if (unidentifiedCard.isFullyIdentified) {
            this.merchant.toRemove.add(this);
            CardArticle hiddenCardArticle = new CardArticle(articleId, this.merchant, this.xPos, this.yPos, this.hiddenCard.makeCopy(), getBasePrice());
            this.merchant.toAdd.add(hiddenCardArticle);
        }
    }

    @Override
    public void onBuy() {
        AbstractDungeon.player.loseGold(getModifiedPrice());
        AbstractDungeon.topLevelEffectsQueue.add(new ShowCardAndObtainEffect(this.hiddenCard.makeCopy(), Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
    }

}
