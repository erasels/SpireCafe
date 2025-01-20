package spireCafe.cardmods;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import basemod.helpers.TooltipInfo;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.StSLib;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.PurgeField;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import spireCafe.Anniv7Mod;

import java.util.ArrayList;
import java.util.List;

/*
    Transient Mod:
    Card Exhausts and permanently removed after played 5 times.
    If card is non-starter, base cost reduced by 1.
    Card is rendered with a white glow and is more transparent the more it is played.
 */
public class TransientMod extends AbstractCardModifier {

    public static final String ID = Anniv7Mod.makeID("TransientMod");
    public static final String[] TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;

    public static final int BASE_TRANSIENCE = 5;
    public int transience;
    public boolean costReduced = false;

    public TransientMod(){
        this(BASE_TRANSIENCE);
    }
    public TransientMod(int newTransience){
        this.transience = newTransience;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new TransientMod(transience);
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        action.exhaustCard = true;
        transience -= 1;
        card.initializeDescription();

        AbstractCard masterCard = StSLib.getMasterDeckEquivalent(card);
        if(transience <= 0){ //if 0: remove card
            PurgeField.purge.set(card, true);
            if (masterCard != null) {
                AbstractDungeon.player.masterDeck.removeCard(masterCard);
            }
        }else{ //if not 0: reduce transient count
            if (masterCard != null) {
                for(AbstractCardModifier masterMod: CardModifierManager.modifiers(masterCard)){
                    if(masterMod instanceof TransientMod){
                        ((TransientMod) masterMod).transience = Math.min(((TransientMod) masterMod).transience, this.transience);
                        masterCard.initializeDescription();
                    }
                }
            }
        }
    }

    //white glow, dimmed for each play of card
    @Override
    public Color getGlow(AbstractCard card) {
        Color newColor = new Color(Color.GRAY);
        float dimmingFactor = ((float)transience)/BASE_TRANSIENCE;
        newColor.r *= dimmingFactor;
        newColor.g *= dimmingFactor;
        newColor.b *= dimmingFactor;
        return newColor;
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }

    //cost reduction if not starter
    @Override
    public void onInitialApplication(AbstractCard card) {
        if(card.rarity != AbstractCard.CardRarity.BASIC && card.cost >= 1){
            card.cost -= 1;
            card.costForTurn = card.cost;
            costReduced = true;
        }
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        return String.format(TEXT[0], rawDescription, transience);
    }

    // add tooltip to describe transient mod,
    // if card is not starter, add extra blurb about cost reduction
    @Override
    public List<TooltipInfo> additionalTooltips(AbstractCard card) {
        List<TooltipInfo> tooltips = new ArrayList<>();
        String desc = TEXT[2];
        if(costReduced) desc += TEXT[3];
        tooltips.add(new TooltipInfo(TEXT[1], desc));
        return tooltips;
    }

    // render card with transparency
    @Override
    public void onUpdate(AbstractCard card) {
        if(!card.fadingOut){
            float ratio = (float)transience/BASE_TRANSIENCE;
            card.transparency = 0.3f + 0.5f*ratio;
        }
    }
}
