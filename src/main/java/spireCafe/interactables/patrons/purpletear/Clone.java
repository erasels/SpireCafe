package spireCafe.interactables.patrons.purpletear;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import spireCafe.abstracts.AbstractSCCard;

import static spireCafe.Anniv7Mod.makeID;
import static spireCafe.interactables.patrons.purpletear.PurpleTearPatron.assetID;

public class Clone extends AbstractSCCard {
    public final static String ID = makeID(Clone.class.getSimpleName());

    public Clone() {
        super(ID, assetID,1, CardType.SKILL, CardRarity.SPECIAL, CardTarget.NONE, CardColor.COLORLESS);
        selfRetain = true;
        exhaust = true;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new CloneAction(1));
    }

    public void upp() {
        upgradeBaseCost(0);
    }
}
