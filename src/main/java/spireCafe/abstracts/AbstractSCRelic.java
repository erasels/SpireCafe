package spireCafe.abstracts;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import spireCafe.util.TexLoader;

import static spireCafe.Anniv7Mod.makeRelicPath;
import static spireCafe.Anniv7Mod.modID;

public abstract class AbstractSCRelic extends CustomRelic {
    public AbstractCard.CardColor color;

    public AbstractSCRelic(String setId, AbstractRelic.RelicTier tier, AbstractRelic.LandingSound sfx) {
        this(setId, null, tier, sfx);
    }

    public AbstractSCRelic(String setId, String zoneID, AbstractRelic.RelicTier tier, AbstractRelic.LandingSound sfx) {
        super(setId, TexLoader.getTexture(makeRelicPath((zoneID != null ? zoneID + "/" : "") + setId.replace(modID + ":", "") + ".png")), tier, sfx);
        outlineImg = TexLoader.getTexture(makeRelicPath((zoneID != null ? zoneID + "/" : "") + setId.replace(modID + ":", "") + "Outline.png"));
    }

    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}