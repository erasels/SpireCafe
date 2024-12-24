package spireCafe.interactables.patrons.dandadan;

import static spireCafe.Anniv7Mod.makeRelicPath;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.tempCards.Shiv;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DuplicationPower;

import basemod.abstracts.CustomSavable;
import basemod.helpers.CardModifierManager;
import spireCafe.Anniv7Mod;
import spireCafe.abstracts.AbstractSCRelic;
import spireCafe.util.TexLoader;
import spireCafe.util.Wiz;

public class GoldenBallRelic extends AbstractSCRelic implements ClickableRelic, CustomSavable<Integer> {

    public static final String ID = Anniv7Mod.makeID(GoldenBallRelic.class.getSimpleName());

    private static final int GHOSTS_TO_ACTIVATE = 12;
    private static final int MILESTONE_1 = 4;
    private static final int MILESTONE_2 = 8;
    private int ghostsPlayed;
    private static Texture noShine, smallShine, medShine, largeShine;

    static {
        noShine = TexLoader.getTexture(makeRelicPath("Dandadan/GoldenBallRelic.png"));
        noShine.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        smallShine = TexLoader.getTexture(makeRelicPath("Dandadan/smallShine.png"));
        smallShine.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        medShine = TexLoader.getTexture(makeRelicPath("Dandadan/medShine.png"));
        medShine.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        largeShine = TexLoader.getTexture(makeRelicPath("Dandadan/largeShine.png"));
        largeShine.setFilter(TextureFilter.Linear, TextureFilter.Linear);
    }

    private Random rnd;

    public GoldenBallRelic() {
        super(ID, "Dandadan", RelicTier.SPECIAL, LandingSound.CLINK);
        rnd = new Random();
    }

    @Override
    public void atBattleStartPreDraw() {
        CardGroup drawPileCopy = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        drawPileCopy.group.addAll(Wiz.p().drawPile.group);
        Collections.shuffle(drawPileCopy.group);

        ArrayList<Integer> ghostIndices = new ArrayList<>();
        ghostIndices.add(0);
        ghostIndices.add(1);
        ghostIndices.add(2);

        int i = 0;
        while (ghostIndices.size() > 0 && i < drawPileCopy.size()) {
            AbstractCard c = drawPileCopy.group.get(i);
            // it'd be nice to not apply to unplayable cards
            if (c.type != AbstractCard.CardType.CURSE
                    || c.type == AbstractCard.CardType.CURSE && c.isEthereal == false) {
                CardModifierManager.addModifier(c, new GhostModifier(ghostIndices.remove(0)));
            }
            i++;
        }
        if (ghostsPlayed == -1) {
            flash();
            Wiz.atb(new RelicAboveCreatureAction(Wiz.p(), this));
            Wiz.applyToSelf(new DuplicationPower(Wiz.p(), 1));
        }
    }

    @Override
    public String getUpdatedDescription() {
        if (ghostsPlayed != -1) {
            return DESCRIPTIONS[0] + String.format(DESCRIPTIONS[1], ghostsPlayed);
        } else {
            return DESCRIPTIONS[0] + DESCRIPTIONS[2];
        }
    }

    @Override
    public void onRightClick() {
        speak("TESTING ~TESTING~ #rTESTING #yTESTING #bTESTING ", 3.0F);
        AbstractCard c = new Shiv();
        CardModifierManager.addModifier(c, new GhostModifier());
        Wiz.makeInHand(c);

    }

    public void speak(String msg, float duration) {
        boolean flipX = (this.hb.cX <= Settings.WIDTH * 0.70F);
        float draw_x;
        if (flipX) {
            draw_x = hb.cX + 20.0F * Settings.scale;
        } else {
            draw_x = hb.cX - 20.0F * Settings.scale;
        }
        AbstractDungeon.topLevelEffectsQueue.add(0, new TopLeftSpeechBubble(draw_x, hb.cY - 295.0F * Settings.scale, duration, msg, flipX));
    }

    @Override
    public void onUseCard(AbstractCard targetCard, UseCardAction useCardAction) {
        if (CardModifierManager.hasModifier(targetCard, GhostModifier.ID)) {
            if (ghostsPlayed != -1) {
                ghostsPlayed++;
                updateGhosts();
            }
        }
    }

    private void updateGhosts() {
        if (ghostsPlayed == -1) { // relic is active
            this.description = DESCRIPTIONS[0] + DESCRIPTIONS[2];
            this.setTexture(largeShine);
        } else {
            if (ghostsPlayed >= GHOSTS_TO_ACTIVATE) { // relic just activated
                ghostsPlayed = -1;
                CardCrawlGame.sound.play("ORB_PLASMA_EVOKE");
                flash();
                updateGhosts();
            } else { // relic is inactive
                if (ghostsPlayed == MILESTONE_2) {
                    this.setTexture(medShine);
                    flash();
                } else if (ghostsPlayed == MILESTONE_1) {
                    this.setTexture(smallShine);
                    flash();
                }
                this.description = DESCRIPTIONS[0] + String.format(DESCRIPTIONS[1], ghostsPlayed);
            }
        }
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        this.initializeTips();
    }

    @Override
    public void onLoad(Integer x) {
        if (x == null) {
            return;
        }
        ghostsPlayed = x;
        updateGhosts();
    }

    @Override
    public Integer onSave() {
        return ghostsPlayed;
    }

    @Override
    public void onPlayCard(AbstractCard c, AbstractMonster m) {
        if (CardModifierManager.hasModifier(c, GhostModifier.ID)) {
            int randomLineIndex = rnd.nextInt(8) + 3;
            speak(DESCRIPTIONS[randomLineIndex], 3);
        }
    }

    @Override
    public void onObtainCard(AbstractCard c) {
        super.onObtainCard(c);
    }

    @Override
    public void onPreviewObtainCard(AbstractCard c) {
        super.onPreviewObtainCard(c);
    }

    @Override
    public void atBattleStart() {
        super.atBattleStart();
    }

    @Override
    public void onBloodied() {
        speak("You're mine now...", 3);
    }

    @Override
    public void onEnterRestRoom() {
        super.onEnterRestRoom();
    }

    @Override
    public void onChestOpen(boolean bossChest) {
        super.onChestOpen(bossChest);
    }
}
