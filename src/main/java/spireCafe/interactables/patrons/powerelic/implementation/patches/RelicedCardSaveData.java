package spireCafe.interactables.patrons.powerelic.implementation.patches;

import basemod.abstracts.AbstractCardModifier;

import java.io.Serializable;
import java.util.ArrayList;

public class RelicedCardSaveData implements Serializable {
    public String cardID;
    public int timesUpgraded;
    public int misc;
    public ArrayList<AbstractCardModifier> cardModifiers;
    public RelicedCardSaveData(String cardID,int timesUpgraded,int misc,ArrayList<AbstractCardModifier> cardModifiers){
        this.cardID=cardID;
        this.timesUpgraded=timesUpgraded;
        this.misc=misc;
        this.cardModifiers=cardModifiers;
    }
}