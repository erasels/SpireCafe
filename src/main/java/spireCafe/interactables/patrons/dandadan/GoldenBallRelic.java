package spireCafe.interactables.patrons.dandadan;

import static spireCafe.Anniv7Mod.makeRelicPath;

import java.util.Collections;
import java.util.Random;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.beyond.AwakenedOne;
import com.megacrit.cardcrawl.monsters.beyond.Donu;
import com.megacrit.cardcrawl.monsters.beyond.TimeEater;
import com.megacrit.cardcrawl.monsters.city.SphericGuardian;
import com.megacrit.cardcrawl.powers.DuplicationPower;
import com.megacrit.cardcrawl.relics.Girya;
import com.megacrit.cardcrawl.relics.PeacePipe;
import com.megacrit.cardcrawl.relics.Shovel;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import com.megacrit.cardcrawl.rooms.TreasureRoomBoss;

import basemod.abstracts.CustomSavable;
import basemod.helpers.CardModifierManager;
import spireCafe.Anniv7Mod;
import spireCafe.abstracts.AbstractSCRelic;
import spireCafe.util.TexLoader;
import spireCafe.util.Wiz;

public class GoldenBallRelic extends AbstractSCRelic implements CustomSavable<Integer> {

    public static final String ID = Anniv7Mod.makeID(GoldenBallRelic.class.getSimpleName());

    private static final int GHOSTS_TO_ACTIVATE = 12;
    private static final int MILESTONE_1 = 4;
    private static final int MILESTONE_2 = 8;
    private int ghostsPlayed, randomLineIndex;
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
        Collections.shuffle(drawPileCopy.group, new Random(AbstractDungeon.cardRandomRng.randomLong()));

        int ghostIndex = 0;
        for (AbstractCard c : drawPileCopy.group) {
            if (ghostIndex >= 3)
                break;
            if (c.type != AbstractCard.CardType.CURSE && c.cost != -2 && c.costForTurn != -2) {
                CardModifierManager.addModifier(c, new GhostModifier(ghostIndex++));
            }
        }

        if (ghostsPlayed == -1) {
            flash();
            Wiz.atb(new RelicAboveCreatureAction(Wiz.p(), this));
            Wiz.applyToSelf(new DuplicationPower(Wiz.p(), 1));
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + (ghostsPlayed == -1 ? DESCRIPTIONS[2] : DESCRIPTIONS[1]);
    }

    public void speak(String msg, float duration) {
        boolean flipX = (this.hb.cX <= Settings.WIDTH * 0.70F);
        float draw_x;
        if (flipX) {
            draw_x = hb.cX + 20.0F * Settings.scale;
        } else {
            draw_x = hb.cX - 20.0F * Settings.scale;
        }
        AbstractDungeon.topLevelEffectsQueue
                .add(0, new TopLeftSpeechBubble(draw_x, hb.cY - 295.0F * Settings.scale, duration, msg, flipX));
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
                this.description = DESCRIPTIONS[0] + DESCRIPTIONS[1];
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
            if (ghostsPlayed != -1) {
                ghostsPlayed++;
                if (ghostsPlayed == GHOSTS_TO_ACTIVATE) {
                    updateGhosts();
                    speak(DESCRIPTIONS[32], 2.5f);
                    return;
                }
                updateGhosts();
            }
            if (c.isInAutoplay) {
                return;
            }
            if (ghostsPlayed == -1) {
                if (rnd.nextBoolean()) {
                    return;
                }
                randomLineIndex = 10;
            } else if (ghostsPlayed >= MILESTONE_2) {
                randomLineIndex = rnd.nextInt(2) + 8;
            } else if (ghostsPlayed >= MILESTONE_1) {
                randomLineIndex = rnd.nextInt(2) + 6;
            } else {
                randomLineIndex = rnd.nextInt(3) + 3;
            }
            speak(DESCRIPTIONS[randomLineIndex], 2.5f);
        }
    }

    @Override
    public void onObtainCard(AbstractCard c) {
        if (rnd.nextBoolean()) {
            return;
        }
        if (AbstractDungeon.getCurrRoom() != null && !(AbstractDungeon.getCurrRoom() instanceof MonsterRoom
                || AbstractDungeon.getCurrRoom() instanceof TreasureRoomBoss)) {
            return;
        }
        if (ghostsPlayed != -1) {
            randomLineIndex = rnd.nextInt(2) + 11;
        } else {
            randomLineIndex = rnd.nextInt(2) + 13;
        }
        speak(DESCRIPTIONS[randomLineIndex], 2.5f);
    }

    @Override
    public void onBloodied() {
        if (ghostsPlayed != -1) {
            randomLineIndex = rnd.nextInt(2) + 15;
        } else {
            randomLineIndex = rnd.nextInt(2) + 17;
        }
        speak(DESCRIPTIONS[randomLineIndex], 2.5f);
    }

    @Override
    public void atBattleStart() {
        if (Wiz.getEnemies().stream().anyMatch(m -> m.id.equals(SphericGuardian.ID))) {
            randomLineIndex = 23;
        } else if (Wiz.getEnemies().stream().anyMatch(m -> m.id.equals(Donu.ID))) {
            randomLineIndex = 24;
        } else if (Wiz.getEnemies().stream().anyMatch(m -> m.id.equals(TimeEater.ID))) {
            randomLineIndex = 25;
        } else if (Wiz.getEnemies().stream().anyMatch(m -> m.id.equals(AwakenedOne.ID))) {
            randomLineIndex = 26;
        } else if (rnd.nextBoolean()) {
            return;
        } else {
            if (ghostsPlayed != -1) {
                randomLineIndex = rnd.nextInt(2) + 19;
            } else {
                randomLineIndex = rnd.nextInt(2) + 21;
            }
        }

        speak(DESCRIPTIONS[randomLineIndex], 2.5f);
    }

    @Override
    public void onEnterRestRoom() {
        randomLineIndex = rnd.nextInt(2) + 27;

        if (Wiz.p().hasRelic(Shovel.ID)) {
            randomLineIndex = 29;
        } else if (Wiz.p().hasRelic(Girya.ID)) {
            if (rnd.nextBoolean()) {
                randomLineIndex = 30;
            }
        } else if (Wiz.p().hasRelic(PeacePipe.ID)) {
            if (rnd.nextBoolean()) {
                randomLineIndex = 31;
            }
        }
        speak(DESCRIPTIONS[randomLineIndex], 2.5f);
    }
}
