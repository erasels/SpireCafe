package spireCafe.cardmods;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import basemod.helpers.TooltipInfo;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.StSLib;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.FleetingField;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.PurgeField;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardHelper;
import spireCafe.Anniv7Mod;

import java.util.ArrayList;
import java.util.List;

public class TransientMod extends AbstractCardModifier {

    public static final String ID = Anniv7Mod.makeID("TransientMod");
    public static final String[] TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;

    public static final int BASE_TRANSIENCE = 5;
    public int transience;

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
        if(transience <= 0){
            PurgeField.purge.set(card, true);
            if (masterCard != null) {
                AbstractDungeon.player.masterDeck.removeCard(masterCard);
            }
        }else{
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
    public void onDrawn(AbstractCard card) {
        if (card.costForTurn > 1) {
            card.costForTurn -= 1;
            card.isCostModifiedForTurn = true;
        }
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }

    @Override
    public void onInitialApplication(AbstractCard card) {
        if(card.cost >= 2){
            card.cost -= 1;
            card.costForTurn = card.cost;
        }
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        return String.format(TEXT[0], rawDescription, transience);
    }

    @Override
    public List<TooltipInfo> additionalTooltips(AbstractCard card) {
        List<TooltipInfo> tooltips = new ArrayList<>();
        tooltips.add(new TooltipInfo(TEXT[1], TEXT[2]));
        return tooltips;
    }
}
