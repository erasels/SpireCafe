package spireCafe.interactables.merchants.fleamerchant;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.relics.Courier;
import com.megacrit.cardcrawl.relics.MembershipCard;
import spireCafe.abstracts.AbstractMerchant;
import spireCafe.interactables.merchants.PotionArticle;

public class HaggleablePotionArticle extends PotionArticle {
    private int haggleRate;

    public HaggleablePotionArticle(String id, AbstractMerchant merchant, float x, float y, AbstractPotion potion, int basePrice, int haggleRate) {
        super(id, merchant, x, y, potion, basePrice);
        this.haggleRate = haggleRate;
    }

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
        return (int)finalPrice*haggleRate;
    }


}
