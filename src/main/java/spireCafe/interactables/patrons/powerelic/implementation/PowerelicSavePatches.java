package spireCafe.interactables.patrons.powerelic.implementation;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import spireCafe.util.Wiz;

import java.util.ArrayList;

public class PowerelicSavePatches {
    @SpirePatch2(clz = SaveFile.class, method = SpirePatch.CONSTRUCTOR, paramtypez={SaveFile.SaveType.class})
    public static class SaveFilePatches {
        @SpirePrefixPatch
        public static void patch() {
            ////////////for cards transformed into relics/////////////
            for (AbstractRelic relic : Wiz.adp().relics) {
                if (relic instanceof PowerelicRelic) {
                    AbstractCard card = ((PowerelicRelic) relic).capturedCard;
                    if (card != null) {
                        //add card back to the deck so it gets saved properly
                        Wiz.deck().group.add(card);
                    }
                }
            }
            ////////////for relics transformed into cards/////////////
            for (AbstractCard card : Wiz.deck().group) {
                if (card instanceof PowerelicCard) {
                    AbstractRelic relic = ((PowerelicCard) card).capturedRelic;
                    if (relic != null) {
                        boolean relicIsActive = Wiz.adp().relics.contains(relic);
                        PowerelicCard.PowerelicRelicContainmentFields.isActiveBetweenCombats.set(relic,relicIsActive);
                        if (!relicIsActive) {
                            //add relics to the relics list so it gets saved properly
                            Wiz.adp().relics.add(relic);
                        }
                    }
                }
            }
            ////////////for relics that were duplicates of relics transformed into cards/////////////
            for(AbstractRelic relic : Wiz.adp().relics){
                if(PowerelicCard.PowerelicRelicContainmentFields.isContained.get(relic)){
                    PowerelicCard card=PowerelicCard.PowerelicRelicContainmentFields.withinCard.get(relic);
                    if(card!=null && !Wiz.deck().group.contains(card)) {
                        //the relic is already active but it needs a card in order to be saved properly.
                        //  we must flag the relic or card as temporary so that it vanishes at start of next combat.
                        //  atm, we're doing so by using a secondary temporary card class.
                        AbstractCard tempCard = new PowerelicTemporaryDuplicateCard(relic);
                        Wiz.deck().group.add(tempCard);
                        //note that the previous loop meant to flag active relics only checks cards in deck, so set it here too
                        PowerelicCard.PowerelicRelicContainmentFields.isActiveBetweenCombats.set(relic,true);
                    }
                }
            }

            //now that all cards and relics are in the list to be saved, we can assign misc+counter
            for (AbstractRelic relic : Wiz.adp().relics) {
                if (relic instanceof PowerelicRelic) {
                    AbstractCard card = ((PowerelicRelic) relic).capturedCard;
                    if (card != null) {
                        //note that misc 0 is effectively null, so we add 1
                        relic.counter = Wiz.deck().group.indexOf(card) + 1;
                        //Anniv7Mod.logger.info("Save: the power card in position "+relic.counter+" is "+card.toString());
                    }
                }
            }
            for (AbstractCard card : Wiz.deck().group) {
                if (card instanceof PowerelicCard) {
                    AbstractRelic relic = ((PowerelicCard) card).capturedRelic;
                    if (relic != null) {
                        boolean relicIsActive = PowerelicCard.PowerelicRelicContainmentFields.isActiveBetweenCombats.get(relic);
                        //note that misc 0 is effectively null, so we add 1
                        card.misc = Wiz.adp().relics.indexOf(relic) + 1 + (relicIsActive ? 999999999 : 0);
                    }
                }
            }
        }
        @SpirePostfixPatch
        public static void patch2() {
            for (AbstractRelic relic : Wiz.adp().relics) {
                if (relic instanceof PowerelicRelic) {
                    //now that we've saved the captured Power card, remove it from the deck again
                    Wiz.deck().group.remove(((PowerelicRelic)relic).capturedCard);
                }
            }
            for (AbstractCard card : Wiz.deck().group) {
                if (card instanceof PowerelicCard) {
                    AbstractRelic relic = ((PowerelicCard) card).capturedRelic;
                    if (card.misc < 999999999) {
                        //then the captured relic was not active at the moment we saved, so remove it from the active relics again
                        Wiz.adp().relics.remove(relic);
                    }
                }
            }
            //for temporary relics that WERE active at the moment we saved, we just created cards to save them in, so remove those
            Wiz.deck().group.removeIf(card -> card instanceof PowerelicTemporaryDuplicateCard);
        }
    }
    @SpirePatch2(clz = CardCrawlGame.class, method = "loadPlayerSave")
    public static class LoadSavePatches {
            ////////////for cards transformed into relics/////////////
        @SpirePostfixPatch
        public static void patch(){
            for(AbstractRelic relic : Wiz.adp().relics){
                if(relic instanceof PowerelicRelic){
                    if(relic.counter>0){
                        int index=relic.counter-1;
                        AbstractCard card=Wiz.deck().group.get(index);
                        //Anniv7Mod.logger.info("Load: the power card in position "+relic.counter+" is "+card.toString());
                        ((PowerelicRelic)relic).setCardInfo(card);
                    }
                }
            }

            ////////////for relics transformed into cards/////////////
            for(AbstractCard card : Wiz.deck().group){
                if(card instanceof PowerelicCard){
                    if(card.misc>0){
                        int index=card.misc;
                        if(index>=999999999)index-=999999999;
                        index-=1;
                        ((PowerelicCard)card).setRelicInfoFromSavedIndexData(index);
                    }
                }
            }

            ArrayList<AbstractCard> DEBUG_GROUP_PTR = Wiz.deck().group;
            ////////////for relics that were duplicates of relics transformed into cards/////////////
            for(AbstractCard card : Wiz.deck().group) {
                if(card instanceof PowerelicTemporaryDuplicateCard){
                    //note that PowerelicTemporaryDuplicateCard is an instanceof PowerelicCard,
                    //so the relic has already been initialized in the above loop.
                    //Just need to mark the relic as temporary now.
                    AbstractRelic relic=((PowerelicCard)card).capturedRelic;
                    PowerelicCard.PowerelicRelicContainmentFields.withinCard.set(relic,null);
                }
            }

            Wiz.deck().group.removeIf(card -> PowerelicRelic.PowerelicCardContainmentFields.isContained.get(card));
            //unlike cards-turned-relics, relics-turned-cards may be active when the game was saved
            //use misc to track if the relic is active
            Wiz.adp().relics.removeIf(relic -> PowerelicCard.PowerelicRelicContainmentFields.withinCard.get(relic)!=null
                    && PowerelicCard.PowerelicRelicContainmentFields.withinCard.get(relic).misc<999999999);
            Wiz.adp().reorganizeRelics();
            Wiz.deck().group.removeIf(card -> card instanceof PowerelicTemporaryDuplicateCard);
        }
    }
}
