package spireCafe.interactables.bartenders.starbucks;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import org.apache.commons.lang3.math.NumberUtils;
import spireCafe.Anniv7Mod;
import spireCafe.abstracts.AbstractBartender;
import spireCafe.util.TexLoader;
import spireCafe.util.Wiz;

public class StarbucksBartender extends AbstractBartender {
    public static final String ID = StarbucksBartender.class.getSimpleName();
    private static final CharacterStrings characterStrings = CardCrawlGame.languagePack.getCharacterString(Anniv7Mod.makeID(ID));

    private static final int GOLD_LOSS = 70;

    public StarbucksBartender(float animationX, float animationY) {
        super(animationX, animationY, 180.0f, 250.0f);
        name = characterStrings.NAMES[0];
        this.img = TexLoader.getTexture(Anniv7Mod.makeBartenderPath("starbucks/image.png"));
        this.cutscenePortrait = new TextureRegion(TexLoader.getTexture(Anniv7Mod.makeBartenderPath("starbucks/Portrait.png")));
    }

    @Override
    protected String getHealOptionDescription() {
        return String.format(characterStrings.TEXT[0], getHealAmount(), GOLD_LOSS);
    }

    @Override
    protected int getHealAmount() {
        // Heal 50% of max HP or 10, whichever is larger
        return (int) NumberUtils.max(Wiz.p().maxHealth * 0.5, 10);
    }

    @Override
    protected void applyHealAction() {
        CardCrawlGame.sound.play("SLEEP_1-2");
        Wiz.p().heal(getHealAmount());
        Wiz.p().loseGold(GOLD_LOSS);
    }
}
