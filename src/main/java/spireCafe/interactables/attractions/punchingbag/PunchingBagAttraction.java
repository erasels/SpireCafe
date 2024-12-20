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
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.CharacterStrings;
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
        int score = 0;
        float multiplier = 1.0F;

        ArrayList<AbstractCard> deck = AbstractDungeon.player.masterDeck.group;

        int cardMulti = 1;
        for (AbstractCard c : deck) {
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
                    score += cardMulti * B_ATK;
                    break;
                case SKILL:
                    score += cardMulti * B_SKL;
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
                    multiplier *= CURSE;
                    break;
                default:
                    break;
            }
        }

        return (int)(score * multiplier * 10) + MathUtils.random(9);
    }

    private AttackEffect getAttackFX() {
        AttackEffect[] slashFX = AbstractDungeon.player.getSpireHeartSlashEffect();
        return slashFX[MathUtils.random(slashFX.length - 1)];
    }
}
