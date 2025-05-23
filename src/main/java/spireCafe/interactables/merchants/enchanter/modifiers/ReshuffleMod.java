package spireCafe.interactables.merchants.enchanter.modifiers;

import java.util.Collections;
import java.util.List;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.TooltipInfo;
import spireCafe.Anniv7Mod;


public class ReshuffleMod extends AbstractCardModifier {
    
    private static final String ID = Anniv7Mod.makeID(ReshuffleMod.class.getSimpleName());
    public static final String[] TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    
    @Override
    public void onInitialApplication(AbstractCard card) {
        card.shuffleBackIntoDrawPile = true;
    }

    public List<TooltipInfo> additionalTooltips(AbstractCard card) {
        return Collections.singletonList(new TooltipInfo(TEXT[3], TEXT[1]));
   }

    @Override
    public boolean shouldApply(AbstractCard card) {
        return card.cost != -2 && card.type != AbstractCard.CardType.POWER
            && !card.shuffleBackIntoDrawPile && !card.exhaust;
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        return rawDescription + TEXT[2];
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new ReshuffleMod();
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }
}
