package spireCafe.abstracts;

import basemod.AutoAdd;
import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.BlightStrings;
import spireCafe.Anniv7Mod;
import spireCafe.util.TexLoader;

@AutoAdd.Ignore
public abstract class AbstractSCBlight extends AbstractBlight {
    public BlightStrings blightStrings;
    public boolean usedUp;
    public boolean isChestBlight = false;
    protected String imgUrl;

    public AbstractSCBlight(String id, String textureString) {
        super(id, "", "", "", true);

        blightStrings = CardCrawlGame.languagePack.getBlightString(id);
        description = getDescription();
        name = blightStrings.NAME;

        imgUrl = textureString;
        img = TexLoader.getTexture(Anniv7Mod.makeImagePath(("blights/" + imgUrl)));
        outlineImg = TexLoader.getTexture(Anniv7Mod.makeImagePath(("blights/outline/" + imgUrl)));

        this.tips.clear();
        this.tips.add(new PowerTip(name, description));
    }

    public void usedUp() {
        setCounter(-1);
        usedUp = true;
        description = getUsedUpMsg();
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        initializeTips();
        img = TexLoader.getTexture(Anniv7Mod.makeImagePath(("blights/used/" + imgUrl)));
    }

    public String getDescription() {
        return blightStrings.DESCRIPTION[0];
    }

    public String getUsedUpMsg() {
        return blightStrings.DESCRIPTION[1];
    }

    public abstract AbstractBlight makeCopy();
}