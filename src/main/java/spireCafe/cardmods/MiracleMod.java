package spireCafe.cardmods;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;

import basemod.abstracts.AbstractCardModifier;
import spireCafe.Anniv7Mod;
import spireCafe.patches.CardsDrawnDuringTurnPatch;

    
public class MiracleMod extends AbstractCardModifier {
    public static final String ID = Anniv7Mod.makeID(MiracleMod.class.getSimpleName());
    private static final String[] TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    private boolean isMiracle = false;

    @Override
    public void onDrawn(AbstractCard card) {
        if (CardsDrawnDuringTurnPatch.CARDS_DRAWN.isEmpty() && !card.freeToPlay()){
            card.freeToPlayOnce = true;
            this.isMiracle = true;
        }
    }

    @Override
    public void atEndOfTurn(AbstractCard card, CardGroup group) {
        if (this.isMiracle) {
            card.freeToPlayOnce = false;
        }
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new MiracleMod();
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        return TEXT[0] + rawDescription;
    }
    
}
