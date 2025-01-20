package spireCafe.interactables.patrons.spiomesmanifestation;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import spireCafe.abstracts.AbstractSCRelic;

import static spireCafe.Anniv7Mod.makeID;


public class BiomesExplorationMap extends AbstractSCRelic {
    public static final String ID = makeID(spireCafe.interactables.patrons.spiomesmanifestation.BiomesExplorationMap.class.getSimpleName());

    public BiomesExplorationMap() {
        super(ID, SpiomesManifestationPatron.assetID, RelicTier.SPECIAL, LandingSound.SOLID);
    }

    @Override
    public void justEnteredRoom(AbstractRoom room) {
        if (SpiomesManifestationPatron.currentNodeInZone()) {
            AbstractDungeon.player.increaseMaxHp(1, true);
            this.flash();
        }
    }

    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}

