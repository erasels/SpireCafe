package spireCafe.cardmods;

import basemod.abstracts.AbstractCardModifier;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import spireCafe.Anniv7Mod;
import spireCafe.patches.CardsDrawnDuringTurnPatch;

    
public class BlessedMod extends AbstractCardModifier {
    public static final String ID = Anniv7Mod.makeID(BlessedMod.class.getSimpleName());
    private static final String[] TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    private boolean isMiracle = false;

    @Override
    public void onDrawn(AbstractCard card) {
        if (CardsDrawnDuringTurnPatch.CARDS_DRAWN.isEmpty() && !card.freeToPlay()){
            card.freeToPlayOnce = true;
            this.isMiracle = true;
            CardCrawlGame.sound.play("HEAL_2");
        }
    }

    @Override
    public void atEndOfTurn(AbstractCard card, CardGroup group) {
        if (this.isMiracle) {
            card.freeToPlayOnce = false;
        }
        this.isMiracle = false;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new BlessedMod();
    }

    @Override
    public Color getGlow(AbstractCard card) {
        if (this.isMiracle) {
            return Color.GOLD;
        } else {
            return null;
        }
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        return TEXT[0] + rawDescription;
    }
    
}
