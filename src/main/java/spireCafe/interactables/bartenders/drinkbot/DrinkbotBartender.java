package spireCafe.interactables.bartenders.drinkbot;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import org.apache.commons.lang3.math.NumberUtils;
import spireCafe.Anniv7Mod;
import spireCafe.abstracts.AbstractBartender;
import spireCafe.util.TexLoader;
import spireCafe.util.cutsceneStrings.CutsceneStrings;
import spireCafe.util.cutsceneStrings.LocalizedCutsceneStrings;

public class DrinkbotBartender extends AbstractBartender {

    public static final String ID = DrinkbotBartender.class.getSimpleName();
    private static final CharacterStrings characterStrings = CardCrawlGame.languagePack.getCharacterString(Anniv7Mod.makeID(ID));
    private static final String BARTENDER_STR = Anniv7Mod.makeBartenderPath("drinkbot/");
    private static final CutsceneStrings cutsceneStrings = LocalizedCutsceneStrings.getCutsceneStrings(Anniv7Mod.makeID(ID + "Cutscene"));

    private static final float HB_W = 240.0F;
    private static final float HB_H = 272.0F;
    private static final int BASE_COST = 70;
    private static final int BASE2_COST = 200;
    public int serialNum = 0;
    public int nemesisNum = 0;
    public int amountSpent = 0;
    private int secondCost = 0;
    public boolean hasPurchased = false;
    public boolean hasVisited = false;
    
    public DrinkbotBartender(float animationX, float animationY) {
        super(animationX, animationY, HB_W, HB_H);
        this.serialNum = MathUtils.random(100, 999);
        this.nemesisNum = MathUtils.random(10, 999);
        this.secondCost = BASE2_COST + AbstractDungeon.miscRng.random(0, 25);
        
        name = characterStrings.NAMES[0] + serialNum;
        this.authors = "Coda";
        
        loadAnimation(BARTENDER_STR + "skeleton.atlas", BARTENDER_STR + "skeleton.json", 1.0F);
        this.state.setAnimation(0, "idle", true);

        this.cutscenePortrait = new TextureRegion(TexLoader.getTexture(Anniv7Mod.makeBartenderPath("drinkbot/portrait.png")));
        
    }

    @Override
    public String getHealOptionDescription() {
        return String.format(cutsceneStrings.OPTIONS[1], getHealAmount(), BASE_COST);
    }

    @Override
    public int getHealAmount() {
        return (int) NumberUtils.max(AbstractDungeon.player.maxHealth * 0.5, 10);
    }

    @Override
    public void applyHealAction() {
        CardCrawlGame.sound.play("SLEEP_1-2");
        AbstractDungeon.player.heal(getHealAmount());
        AbstractDungeon.player.loseGold(BASE_COST);
        inHealAction = false;
    }

    @Override
    public boolean getHealOptionCondition() {
        return AbstractDungeon.player.gold >= BASE_COST;
    }

    @Override
    public String getSecondOptionDescription() {
        return String.format(cutsceneStrings.OPTIONS[2], this.secondCost);
    }

    @Override
    public void applySecondOptionAction() {
        CardCrawlGame.sound.play("SLEEP_1-2");
        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2, Settings.HEIGHT / 2, new DrinkInserterRelic());
        AbstractDungeon.player.loseGold(this.secondCost);
        inSecondAction = false;
    }

    @Override
    public boolean getSecondOptionCondition() {
        return AbstractDungeon.player.gold >= this.secondCost;
    }


    @Override
    public String getNoThanksDescription() {
        return cutsceneStrings.OPTIONS[0];
    }

    @Override
    public String getLabelText() {
        return characterStrings.TEXT[0];
    }

    @Override
    public void onInteract() {
        AbstractDungeon.topLevelEffectsQueue.add(new DrinkbotCutscene(this, cutsceneStrings));
        this.hasVisited = true;
    }

}
