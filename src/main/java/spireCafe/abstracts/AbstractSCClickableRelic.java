package spireCafe.abstracts;

import com.evacipated.cardcrawl.mod.stslib.patches.HitboxRightClick;
import com.megacrit.cardcrawl.core.CardCrawlGame;


public abstract class AbstractSCClickableRelic extends AbstractSCRelic {

    public AbstractSCClickableRelic(String setId, String assetID, RelicTier tier, LandingSound sfx) {
        super(setId, assetID, tier, sfx);
    }

    @Override
    public void update() {
        super.update();
        if (CardCrawlGame.isInARun()) {
            clickUpdate();
        }
    }

    private void clickUpdate() {
        if (HitboxRightClick.rightClicked.get(this.hb)) {
            onRightClick();
        }
    }

    public abstract void onRightClick();

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
