package spireCafe.interactables.attractions.jukebox;

import basemod.BaseMod;
import spireCafe.abstracts.AbstractSCClickableRelic;
import spireCafe.screens.JukeboxScreen;
import static spireCafe.Anniv7Mod.makeID;


public class JukeboxRelic extends AbstractSCClickableRelic {


    public static final String ID = makeID(spireCafe.interactables.attractions.jukebox.JukeboxRelic.class.getSimpleName());

    public JukeboxRelic() {
        super(ID, "JukeboxRelic", RelicTier.SPECIAL, LandingSound.HEAVY);

    }

    @Override
    public void onRightClick() {
        BaseMod.openCustomScreen(JukeboxScreen.ScreenEnum.JUKEBOX_SCREEN);
    }
    @Override
    public void onEquip() {
        JukeboxScreen.isCoinSlotClicked = true; // Set the flag when the relic is equipped
    }

    @Override
    public void onUnequip() {
        JukeboxScreen.isCoinSlotClicked = false; // Reset the flag when the relic is removed
    }
}