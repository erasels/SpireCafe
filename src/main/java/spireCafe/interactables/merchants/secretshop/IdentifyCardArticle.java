package spireCafe.interactables.merchants.secretshop;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.Courier;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

import spireCafe.Anniv7Mod;
import spireCafe.abstracts.AbstractMerchant;
import spireCafe.interactables.merchants.CardArticle;


public class IdentifyCardArticle extends CardArticle{

    private static final String ID = Anniv7Mod.makeID(IdentifyCardArticle.class.getSimpleName());
    private AbstractCard hiddenCard;
    private UnidentifiedCard unidentifiedCard;
    private IdentifyArticle identifyArticle;
    private SecretShopMerchant ssMerchant;

    public IdentifyCardArticle(AbstractMerchant merchant, IdentifyArticle identifyArticle, float x, float y, UnidentifiedCard unidentifiedCard, AbstractCard hiddenCard, int basePrice) {
        super(ID, merchant, x, y, unidentifiedCard, basePrice);
        this.unidentifiedCard = unidentifiedCard;
        this.hiddenCard = hiddenCard;
        this.identifyArticle = identifyArticle;
        this.unidentifiedCard.hiddenCard = hiddenCard;
        this.ssMerchant = (SecretShopMerchant) merchant;
    }

    @Override
    public void onClick() {
        if (identifyArticle.isIdentifyMode) {
            identifyCard();
        } else {
            if (!canBuy()) {
                this.ssMerchant.cantBuy();
            }
            super.onClick();
        }
    }

    private void identifyCard() {
        if (!identifyArticle.canBuy()) {
            this.ssMerchant.cantIdentify();
            return;
        }
        identifyArticle.onBuy();
        unidentifiedCard.identify();
        unidentifiedCard.identify();
        if (unidentifiedCard.isFullyIdentified) {
            this.merchant.toRemove.add(this);
            FullyIdentifiedCardArticle hiddenCardArticle = new FullyIdentifiedCardArticle(this.merchant, this.identifyArticle, this.xPos, this.yPos, this.hiddenCard, getBasePrice());
            this.merchant.toAdd.add(hiddenCardArticle);
        }
    }

    @Override
    public void onBuy() {
        AbstractDungeon.player.loseGold(getModifiedPrice());
        AbstractDungeon.topLevelEffectsQueue.add(new ShowCardAndObtainEffect(this.hiddenCard, Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));

        if (!AbstractDungeon.player.hasRelic(Courier.ID)) {
            return;
        }

        boolean isColorless = AbstractDungeon.miscRng.random() < 0.2F;
        AbstractCard c = SecretShopMerchant.getCard(isColorless);
        UnidentifiedCard uC = new UnidentifiedCard();
        CardArticle identifyCardArticle = new IdentifyCardArticle(merchant, identifyArticle, xPos, yPos, uC, c, SecretShopMerchant.setCardBasePrice());
        merchant.toAdd.add(identifyCardArticle);
    }

}
