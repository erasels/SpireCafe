package spireCafe.interactables.bartenders.vampista;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.colorless.Bite;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import spireCafe.Anniv7Mod;
import spireCafe.abstracts.AbstractBartender;
import spireCafe.abstracts.BartenderCutscene;
import spireCafe.util.TexLoader;
import spireCafe.util.Wiz;
import spireCafe.util.cutsceneStrings.CutsceneStrings;
import spireCafe.util.cutsceneStrings.LocalizedCutsceneStrings;

import java.util.Iterator;
import java.util.List;

public class VampistaBartender extends AbstractBartender {
    public static final String ID = VampistaBartender.class.getSimpleName();
    private static final CharacterStrings characterStrings = CardCrawlGame.languagePack.getCharacterString(Anniv7Mod.makeID(ID));
    private static final CutsceneStrings cutsceneStrings = LocalizedCutsceneStrings.getCutsceneStrings(Anniv7Mod.makeID(ID + "Cutscene"));

    private static final int BITE_COST = 12;
    private static final int HEAL_COST = 4;

    public VampistaBartender(float animationX, float animationY) {
        super(animationX, animationY, 180.0f, 250.0f);
        name = characterStrings.NAMES[0];
        this.authors = "Gk";
        this.img = TexLoader.getTexture(Anniv7Mod.makeBartenderPath("vampista/image.png"));
        this.cutscenePortrait = new TextureRegion(TexLoader.getTexture(Anniv7Mod.makeBartenderPath("vampista/Portrait.png")));
    }

    @Override
    public String getHealOptionDescription() {
        return String.format(cutsceneStrings.OPTIONS[1], HEAL_COST);
    }

    @Override
    public boolean getHealOptionCondition() {
        return Wiz.p().maxHealth > HEAL_COST;
    }

    @Override
    public int getHealAmount() {
        return Wiz.p().maxHealth;
    }

    @Override
    public void applyHealAction() {
        CardCrawlGame.sound.play("EVENT_VAMP_BITE");
        Wiz.p().heal(getHealAmount());
        Wiz.p().decreaseMaxHealth(HEAL_COST);
        inHealAction = false; //Important to set this to false after the logic has concluded
    }

    @Override
    public String getSecondOptionDescription() {
        return String.format(cutsceneStrings.OPTIONS[2], BITE_COST);
    }

    @Override
    public boolean getSecondOptionCondition() {
        return Wiz.p().maxHealth > BITE_COST && Wiz.deck().getPurgeableCards().size() >= 3;
    }

    @Override
    public void applySecondOptionAction() {
        CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (AbstractCard card : Wiz.deck().getPurgeableCards().group) {
            group.addToBottom(card);
        }
        if(!group.isEmpty()) {
            int num = Math.min(group.size(), 3);
            AbstractDungeon.gridSelectScreen.open(group, num, String.format(characterStrings.TEXT[1], num), false);
            Wiz.p().decreaseMaxHealth(BITE_COST);
        }
    }

    @Override
    public void doForSelectedCardsFromSecondAction(List<AbstractCard> selected) {
        CardCrawlGame.sound.play("EVENT_VAMP_BITE");
        int numberOfCards = selected.size();

        // Calculate spacing between cards
        float totalWidth = numberOfCards * AbstractCard.IMG_WIDTH + (numberOfCards - 1) * (20.0f * Settings.scale);
        float startX = (Settings.WIDTH - totalWidth) / 2.0f; // Center the cards horizontally
        float centerY = Settings.HEIGHT / 2.0f;

        Iterator<AbstractCard> cIter = selected.iterator();
        int index = 0;

        while (cIter.hasNext()) {
            AbstractCard removedCard = cIter.next();
            Wiz.deck().removeCard(removedCard);

            // Calculate the X position for the new card
            float xPosition = startX + index * (AbstractCard.IMG_WIDTH + 20.0f);
            AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(new Bite(), xPosition, centerY));
            index++;
        }

        inSecondAction = false;
    }

    @Override
    public String getNoThanksDescription() {
        return cutsceneStrings.OPTIONS[0];
    }

    @Override
    public String getLabelText() {
        return characterStrings.TEXT[0];
    }

    /*
     * With the current configuration you don't need to do this, as the naming matches the assumed default.
     * I just do this because I like to separate the options for tht cutscene from characterstrings.
     */
    public void onInteract() {
        AbstractDungeon.topLevelEffectsQueue.add(new BartenderCutscene(this, cutsceneStrings));
    }
}
