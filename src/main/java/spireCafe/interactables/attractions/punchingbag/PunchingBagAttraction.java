package spireCafe.interactables.attractions.punchingbag;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.AbstractCard.CardRarity;
import com.megacrit.cardcrawl.cards.AbstractCard.CardTags;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.cards.blue.Claw;
import com.megacrit.cardcrawl.cards.curses.AscendersBane;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.relics.Akabeko;
import com.megacrit.cardcrawl.relics.Girya;
import com.megacrit.cardcrawl.relics.LetterOpener;
import com.megacrit.cardcrawl.relics.PenNib;
import com.megacrit.cardcrawl.relics.PhilosopherStone;
import com.megacrit.cardcrawl.relics.Shuriken;
import com.megacrit.cardcrawl.relics.StrikeDummy;
import com.megacrit.cardcrawl.relics.Vajra;
import com.megacrit.cardcrawl.vfx.combat.DamageNumberEffect;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

import spireCafe.Anniv7Mod;
import spireCafe.abstracts.AbstractAttraction;
import spireCafe.util.TexLoader;

public class PunchingBagAttraction extends AbstractAttraction{

    private final static float HB_X = 170.0F;
    private final static float HB_Y = 315.0F;
    private static final String ID = Anniv7Mod.makeID(PunchingBagAttraction.class.getSimpleName());
    private static final CharacterStrings characterStrings = CardCrawlGame.languagePack.getCharacterString(ID);

    private static int B_ATK = 5;
    private static int B_SKL = 2;
    private static float C_PWR = 1.1F;
    private static float U_PWR = 1.2F;
    private static float R_PWR = 1.3F;
    private static float CURSE = 0.67F;

    public PunchingBagAttraction(float posX, float posY) {
        super (posX, posY, HB_X, HB_Y);
        img = TexLoader.getTexture(Anniv7Mod.makeAttractionPath("punchingbag/punchingbag.png"));
        authors = "Coda";
        name = characterStrings.NAMES[0];
    }

    @Override
    public void renderCutscenePortrait(SpriteBatch sb) {
    }

    @Override
    public void onInteract() {
        int currScore = calculateDamageScore();
        float x = InputHelper.mX;
        float y = InputHelper.mY;
        AttackEffect attckFX = getAttackFX();
        AbstractDungeon.topLevelEffectsQueue.add(new FlashAtkImgEffect(x, y, attckFX));
        AbstractDungeon.topLevelEffectsQueue.add(new DamageNumberEffect(null, x, y, currScore));
    }
    
    public int calculateDamageScore() {
        AbstractPlayer p = AbstractDungeon.player;
        int score = 0;
        float multiplier = 1.0F;
        int clawCount = 0;
        int attackCount = 0;
        int skillCount = 0;
        int strength = 0;

        ArrayList<AbstractCard> deck = p.masterDeck.group;

        if (p.hasRelic(Vajra.ID)) {
            strength += 1;
        }

        if (p.hasRelic(PhilosopherStone.ID)) {
            strength += 1;
        }

        if (p.hasRelic(Girya.ID)) {
            strength += p.getRelic(Girya.ID).counter;
        }

        int cardMulti = 1;
        for (AbstractCard c : deck) {
            int cardScore = 0;
            switch (c.rarity) {
                case COMMON:
                    cardMulti = 2;
                    break;
                case UNCOMMON:
                    cardMulti = 4;
                    break;
                case RARE:
                case SPECIAL:
                    cardMulti = 8;
                    break;
                default:
                    cardMulti = 1;
                    break;
            }
            switch (c.type) {
                case ATTACK:
                    cardScore += (cardMulti * B_ATK) + strength;
                    attackCount += 1;
                    break;
                case SKILL:
                    cardScore += cardMulti * B_SKL;
                    skillCount += 1;
                    break;
                case POWER:
                    if (c.rarity.equals(CardRarity.RARE)) {
                        multiplier *= R_PWR;
                    } else if (c.rarity.equals(CardRarity.UNCOMMON)) {
                        multiplier *= U_PWR;
                    } else {
                        multiplier *= C_PWR;
                    }
                    break;
                case CURSE:
                case STATUS:
                    if (!c.cardID.equals(AscendersBane.ID)) {
                        multiplier *= CURSE;
                    }
                    break;
                default:
                    break;
            }

            // Additive bonuses
            if (c.hasTag(CardTags.STRIKE) && p.hasRelic(StrikeDummy.ID)) {
                score += 2;
            }
            if (c.cardID.equals(Claw.ID)){
                cardScore += (2 + strength) * clawCount;
                clawCount += 1;
            }
            if (p.hasRelic(Shuriken.ID) && c.type.equals(CardType.ATTACK) && attackCount % 3 == 0) {
                strength += 1;
            }
            
            // Multiplier bonuses
            cardScore *= Math.pow(1.5F, c.timesUpgraded);

            if (p.hasRelic(PenNib.ID) && c.type.equals(CardType.ATTACK) && attackCount % 10 == 0) {
                cardScore *= 2;
            }
            score += cardScore;
        }

        // Additive bonuses (Total)
        if (p.hasRelic(Akabeko.ID) && attackCount > 0) {
            score += 8;
        }
        if (p.hasRelic(LetterOpener.ID) && skillCount > 0) {
            score += 5 * (skillCount / 3);
        }

        return (int)(score * multiplier * 10) + MathUtils.random(9);
    }

    private AttackEffect getAttackFX() {
        AttackEffect[] slashFX = AbstractDungeon.player.getSpireHeartSlashEffect();
        return slashFX[MathUtils.random(slashFX.length - 1)];
    }
}
