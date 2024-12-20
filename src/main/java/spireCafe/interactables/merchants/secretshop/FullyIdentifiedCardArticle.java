package spireCafe.interactables.merchants.secretshop;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.Courier;

import spireCafe.Anniv7Mod;
import spireCafe.abstracts.AbstractMerchant;
import spireCafe.interactables.merchants.CardArticle;

public class FullyIdentifiedCardArticle extends CardArticle{
    public static final String ID = Anniv7Mod.makeID(FullyIdentifiedCardArticle.class.getSimpleName());
    
    private IdentifyArticle identifyArticle;
    private SecretShopMerchant ssMerchant;

    public FullyIdentifiedCardArticle(AbstractMerchant merchant, IdentifyArticle identifyArticle, float x, float y, AbstractCard card, int basePrice) {
        super(ID, merchant, x, y, card, basePrice);
        this.identifyArticle = identifyArticle;
        this.ssMerchant = (SecretShopMerchant) merchant;
    }

    @Override
    public void onClick() {
        if (!canBuy()) {
            this.ssMerchant.cantBuy();
        }
        super.onClick();
    }

    @Override
    public void onBuy() {
        super.onBuy();

        if (!AbstractDungeon.player.hasRelic(Courier.ID)) {
            return;
        }

        boolean isColorless = AbstractDungeon.miscRng.randomBoolean(0.2f);
        AbstractCard c = SecretShopMerchant.getCard(isColorless);
        UnidentifiedCard uC = new UnidentifiedCard();
        CardArticle identifyCardArticle = new IdentifyCardArticle(merchant, identifyArticle, xPos, yPos, uC, c, SecretShopMerchant.setCardBasePrice());
        merchant.toAdd.add(identifyCardArticle);
    }
    
}
