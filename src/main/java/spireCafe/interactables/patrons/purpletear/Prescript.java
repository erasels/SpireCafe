package spireCafe.interactables.patrons.purpletear;

import basemod.AutoAdd;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import spireCafe.abstracts.AbstractSCCard;

import static spireCafe.Anniv7Mod.makeID;
import static spireCafe.interactables.patrons.purpletear.PurpleTearPatron.assetID;

@AutoAdd.Ignore
public class Prescript extends AbstractSCCard {
    public final static String ID = makeID(Prescript.class.getSimpleName());

    public Prescript() {
        super(ID, assetID,-2, CardType.SKILL, CardRarity.SPECIAL, CardTarget.NONE, CardColor.COLORLESS);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {

    }

    public void upp() {

    }

    public void upgrade() {
        name = cardStrings.EXTENDED_DESCRIPTION[0];
        this.initializeTitle();
    }
}
