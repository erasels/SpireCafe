package spireCafe.interactables.patrons.powerelic.debug;

import basemod.DevConsole;
import basemod.patches.com.megacrit.cardcrawl.screens.compendium.CardLibraryScreen.NoCompendium;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import spireCafe.abstracts.AbstractSCRelic;
import spireCafe.interactables.patrons.powerelic.PowerelicPatron;

import java.util.ArrayList;

import static spireCafe.Anniv7Mod.makeID;

@NoCompendium
public class PowerelicDebugRelic extends AbstractSCRelic {

    public static final String ID = makeID(PowerelicDebugRelic.class.getSimpleName());

    int relicsToConvert=-1;
    public PowerelicDebugRelic() {
        this(-1);
    }
    public PowerelicDebugRelic(int relicsToConvert) {
        super(ID, null, RelicTier.SPECIAL, LandingSound.MAGICAL);
        if(relicsToConvert>=0)this.relicsToConvert=relicsToConvert;
    }
    private boolean isDone=false;
    private boolean cardsSelected = true;
    final int DEFAULT_CARDS_TO_CONVERT=1;
    final String TEMP="[DNT] Choose 1 Power card to become a Relic.";
    public void onEquip() {
        this.cardsSelected = false;
        CardGroup tmp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        tmp.group = new ArrayList<>(PowerelicPatron.getAllConvertiblePowers());

        if (tmp.group.isEmpty()) {
            this.cardsSelected = true;
            convertSelectedCardsToRelics(tmp.group);
            DevConsole.log("You don't have any convertible Powers.");
        } else {
//            if (tmp.group.size() <= DEFAULT_CARDS_TO_CONVERT) {
//                convertSelectedCardsToRelics(tmp.group);
//            } else
            if (!AbstractDungeon.isScreenUp) {
                AbstractDungeon.gridSelectScreen.open(tmp, DEFAULT_CARDS_TO_CONVERT, TEMP, false, false, false, false);
            } else {
                AbstractDungeon.dynamicBanner.hide();
                AbstractDungeon.previousScreen = AbstractDungeon.screen;
                AbstractDungeon.gridSelectScreen.open(tmp, DEFAULT_CARDS_TO_CONVERT, TEMP, false, false, false, false);
            }
        }
    }

    public void update() {
        super.update();
        if (!this.cardsSelected && AbstractDungeon.gridSelectScreen.selectedCards.size() == DEFAULT_CARDS_TO_CONVERT) {
            convertSelectedCardsToRelics(AbstractDungeon.gridSelectScreen.selectedCards);
        }
    }

    public void convertSelectedCardsToRelics(ArrayList<AbstractCard>group){
        if(!isDone) {
            isDone = true;
            AbstractDungeon.effectsQueue.add(new PowerelicDebugRemovalEffect());
            AbstractDungeon.effectsQueue.add(new PowerelicDebugConversionEffect(group, relicsToConvert));
        }
    }



    public AbstractRelic makeCopy() {
        return new PowerelicDebugRelic();
    }
    public AbstractRelic makeCopy(int relicsToConvert) {
        return new PowerelicDebugRelic(relicsToConvert);
    }
}
