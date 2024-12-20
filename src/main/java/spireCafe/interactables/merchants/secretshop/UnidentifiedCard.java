package spireCafe.interactables.merchants.secretshop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import basemod.AutoAdd;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.red.IronWave;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import spireCafe.Anniv7Mod;
import spireCafe.abstracts.AbstractSCCard;

@AutoAdd.Ignore
public class UnidentifiedCard extends AbstractSCCard{

    private static final int COST = -2;
    private static final CardType TYPE = CardType.STATUS;
    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final String ZONE = "SecretShopMerchant" ;
    private static final String ID = Anniv7Mod.makeID(UnidentifiedCard.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;

    public AbstractCard hiddenCard = new IronWave();
    public EnumSet<IdentifiedField> identified = EnumSet.noneOf(IdentifiedField.class);
    public boolean isFullyIdentified = false;

    public UnidentifiedCard() {
        super(ID, ZONE, COST, TYPE, RARITY, TARGET);
    }

    @Override
    public void upp() {
    }

    @Override
    public void use(AbstractPlayer arg0, AbstractMonster arg1) {
    }
    
    public void identify(){
        ArrayList<IdentifiedField> idList = new ArrayList<>();

        if (!identified.contains(IdentifiedField.COST)){
            idList.add(IdentifiedField.COST);
        }
        if (!identified.contains(IdentifiedField.TYPE)){
            idList.add(IdentifiedField.TYPE);
        }
        if (!identified.contains(IdentifiedField.RARITY)){
            idList.add(IdentifiedField.RARITY);
        }

        if (idList.isEmpty()){
            this.isFullyIdentified = true;
            return;
        }

        Collections.shuffle(idList, new java.util.Random(AbstractDungeon.miscRng.randomLong()));
        IdentifiedField toID = idList.get(0);
        identified.add(toID);

        updateDescription();
    }

    private void updateDescription() {

        // Cost
        this.rawDescription = EXTENDED_DESCRIPTION[1];
        if (identified.contains(IdentifiedField.COST)) {
            if (this.hiddenCard.cost == -1) {
                this.rawDescription += EXTENDED_DESCRIPTION[4];
            } else if (this.hiddenCard.cost == -2) {
                this.rawDescription += EXTENDED_DESCRIPTION[5];
            } else {
                this.rawDescription += this.hiddenCard.cost + " NL ";
            }
        } else {
            this.rawDescription += EXTENDED_DESCRIPTION[0];
        }

        // Type
        this.rawDescription += EXTENDED_DESCRIPTION[2];
        if (identified.contains(IdentifiedField.TYPE)) {
            if (this.hiddenCard.type == CardType.SKILL) {
                this.rawDescription += EXTENDED_DESCRIPTION[6];
            } else if (this.hiddenCard.type == CardType.ATTACK) {
                this.rawDescription += EXTENDED_DESCRIPTION[7];
            } else if (this.hiddenCard.type == CardType.POWER) {
                this.rawDescription += EXTENDED_DESCRIPTION[8];
            } else {
                this.rawDescription += EXTENDED_DESCRIPTION[9];
            }
        } else {
            this.rawDescription += EXTENDED_DESCRIPTION[0];
        }

        // Rarity
        this.rawDescription += EXTENDED_DESCRIPTION[3];
        if (identified.contains(IdentifiedField.RARITY)){
            if (this.hiddenCard.rarity == CardRarity.COMMON) {
                this.rawDescription += EXTENDED_DESCRIPTION[10];
            } else if (this.hiddenCard.rarity == CardRarity.UNCOMMON) {
                this.rawDescription += EXTENDED_DESCRIPTION[11];
            } else if (this.hiddenCard.rarity == CardRarity.RARE) {
                this.rawDescription += EXTENDED_DESCRIPTION[12];
            } else {
                this.rawDescription += EXTENDED_DESCRIPTION[13];
            }
        } else {
            this.rawDescription += EXTENDED_DESCRIPTION[0];
        }

        initializeDescription();
    }

}