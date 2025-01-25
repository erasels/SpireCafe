package spireCafe.screens;

import basemod.abstracts.CustomScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.shrines.GremlinMatchGame;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

import java.util.ArrayList;

public class CafeMatchAndKeepScreen extends CustomScreen {
    private CardGroup matchCards;
    private AbstractCard.CardRarity rarity;
    private boolean isZanyMode;
    private AbstractCard chosenCard;
    private AbstractCard hoveredCard;
    private AbstractCard lastChosenCard;
    private AbstractCard lastHoveredCard;
    private boolean cardFlipped = false;
    private float waitTimer = 0.0F;
    private boolean cardsMatched = false;
    private float matchAnimationTimer = 0.0F;
    private static final float MATCH_ANIMATION_DURATION = 0.5F;
    private boolean animationStarted = false;
    private int attemptCount = 5;
    private boolean gameDone = false;
    private int goldCost;

    private static Texture backgroundTexture;
    private static TextureRegion backgroundTextureRegion;
    private static boolean textureLoadAttempted = false;

    public CafeMatchAndKeepScreen() {
        loadBackgroundTexture();
    }

    @Override
    public void open(Object... params) {
        if (params == null || params.length == 0 || !(params[0] instanceof GameParams)) {
            return;
        }

        try {
            GameParams gameParams = (GameParams) params[0];
            this.rarity = gameParams.rarity;
            this.isZanyMode = gameParams.isZanyMode;
            this.goldCost = gameParams.goldCost;

            this.matchCards = createMatchGameCards();
            placeCards();

            this.cardsMatched = false;
            this.animationStarted = false;
            this.gameDone = false;
            this.cardFlipped = false;
            this.waitTimer = 0.0F;
            this.attemptCount = 5;

            AbstractDungeon.screen = curScreen();
            AbstractDungeon.isScreenUp = true;
            AbstractDungeon.overlayMenu.showBlackScreen(0.75f);
            AbstractDungeon.overlayMenu.proceedButton.hide();
            AbstractDungeon.overlayMenu.cancelButton.hide();
            AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.INCOMPLETE;

        } catch (Exception e) {
        }
    }

    @Override
    public void close() {
        if (this.matchCards != null) {
            this.matchCards.clear();
            this.matchCards = null;
        }

        this.chosenCard = null;
        this.hoveredCard = null;
        this.lastChosenCard = null;
        this.lastHoveredCard = null;

        this.cardsMatched = false;
        this.cardFlipped = false;
        this.animationStarted = false;
        this.waitTimer = 0.0F;
        this.gameDone = false;

        resetCutsceneState();
    }

    @Override
    public void reopen() {
        AbstractDungeon.isScreenUp = true;
        AbstractDungeon.screen = curScreen();
        AbstractDungeon.overlayMenu.showBlackScreen(0.75f);
        AbstractDungeon.overlayMenu.proceedButton.hide();
        AbstractDungeon.overlayMenu.cancelButton.hide();
    }

    public void dispose() {
        if (backgroundTexture != null) {
            backgroundTexture.dispose();
            backgroundTexture = null;
            backgroundTextureRegion = null;
        }
        textureLoadAttempted = false;
    }

    @Override
    public void update() {
        if (this.matchCards == null) return;

        if (this.waitTimer == 0.0F) {
            updateMatchGameLogic();
        } else {
            this.waitTimer -= Gdx.graphics.getDeltaTime();
            if (this.waitTimer < 0.0F) {
                if (this.gameDone) {
                    close();
                    return;
                }
                if (this.cardsMatched) {
                    handleMatchAnimation();
                } else {
                    handleUnmatchedState();
                }
            }
        }

        this.matchCards.update();
    }

    private void updateMatchGameLogic() {
        this.hoveredCard = null;

        for (AbstractCard c : this.matchCards.group) {
            c.hb.update();
            if (this.hoveredCard == null && c.hb.hovered) {
                c.drawScale = 0.7F;
                c.targetDrawScale = 0.7F;
                this.hoveredCard = c;
                if (InputHelper.justClickedLeft && c.isFlipped) {
                    InputHelper.justClickedLeft = false;
                    c.isFlipped = false;
                    if (!this.cardFlipped) {
                        this.cardFlipped = true;
                        this.chosenCard = c;
                    } else {
                        this.cardFlipped = false;
                        if (this.chosenCard.cardID.equals(c.cardID)) {
                            this.waitTimer = 0.5F;
                            this.cardsMatched = true;
                            this.animationStarted = false;
                            this.chosenCard.targetDrawScale = 0.7F;
                            this.chosenCard.target_x = Settings.WIDTH / 2.0F;
                            this.chosenCard.target_y = Settings.HEIGHT / 2.0F;
                            c.targetDrawScale = 0.7F;
                            c.target_x = Settings.WIDTH / 2.0F;
                            c.target_y = Settings.HEIGHT / 2.0F;
                        } else {
                            this.waitTimer = 1.25F;
                            this.chosenCard.targetDrawScale = 1.0F;
                            c.targetDrawScale = 1.0F;
                        }
                    }
                }
            } else if (c != this.chosenCard) {
                c.targetDrawScale = 0.5F;
            }
        }

        this.matchCards.update();
    }

    private void handleMatchAnimation() {
        if (!this.animationStarted) {
            this.animationStarted = true;
            this.matchAnimationTimer = MATCH_ANIMATION_DURATION;
            AbstractCard cardToObtain = this.chosenCard.makeStatEquivalentCopy();
            AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(
                    cardToObtain,
                    Settings.WIDTH / 2.0F,
                    Settings.HEIGHT / 2.0F
            ));
        } else {
            this.matchAnimationTimer -= Gdx.graphics.getDeltaTime();
            if (this.matchAnimationTimer <= 0.0F) {
                this.cardsMatched = false;

                this.matchCards.group.remove(this.hoveredCard);
                this.matchCards.group.remove(this.chosenCard);

                this.chosenCard = null;
                this.hoveredCard = null;
                this.waitTimer = 0.0F;

                --this.attemptCount;

                if (this.matchCards.isEmpty() || this.attemptCount == 0) {
                    this.gameDone = true;
                    this.waitTimer = 0.1F;
                    AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
                }
            }
        }
    }

    private void handleUnmatchedState() {
        if (this.chosenCard != null && this.hoveredCard != null) {
            if (this.attemptCount > 1) {
                this.chosenCard.isFlipped = true;
                this.hoveredCard.isFlipped = true;
                this.chosenCard.targetDrawScale = 0.5F;
                this.hoveredCard.targetDrawScale = 0.5F;
            } else {
                this.chosenCard.targetDrawScale = 1.0F;
                this.hoveredCard.targetDrawScale = 1.0F;
                this.lastChosenCard = this.chosenCard;
                this.lastHoveredCard = this.hoveredCard;
            }
            this.chosenCard = null;
            this.hoveredCard = null;
        }

        --this.attemptCount;

        if (this.attemptCount <= 0) {
            this.gameDone = true;
            this.waitTimer = 0.1F;
            AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
            return;
        }

        this.waitTimer = 0.0F;
    }

    @Override
    public void render(SpriteBatch sb) {
        if (this.matchCards == null) return;

        if (backgroundTextureRegion != null) {
            sb.setColor(Color.WHITE);
            sb.draw(backgroundTextureRegion,
                    0, 0,
                    0, 0,
                    backgroundTextureRegion.getRegionWidth(),
                    backgroundTextureRegion.getRegionHeight(),
                    Settings.scale, Settings.scale,
                    0);
        }

        for (AbstractCard c : this.matchCards.group) {
            if (c != this.lastChosenCard && c != this.lastHoveredCard &&
                    c != this.hoveredCard && c != this.chosenCard) {
                c.render(sb);
            }
        }

        if (this.lastChosenCard != null) this.lastChosenCard.render(sb);
        if (this.lastHoveredCard != null) this.lastHoveredCard.render(sb);
        if (this.chosenCard != null) this.chosenCard.render(sb);
        if (this.hoveredCard != null) this.hoveredCard.render(sb);

        FontHelper.renderSmartText(
                sb,
                FontHelper.panelNameFont,
                GremlinMatchGame.OPTIONS[3] + this.attemptCount,
                780.0F * Settings.scale,
                80.0F * Settings.scale,
                2000.0F * Settings.scale,
                0.0F,
                Color.WHITE
        );
    }

    private static void loadBackgroundTexture() {
        if (!textureLoadAttempted) {
            textureLoadAttempted = true;
            try {
                backgroundTexture = new Texture(Gdx.files.internal("bottomScene/scene3.jpg"));
                backgroundTextureRegion = new TextureRegion(backgroundTexture, 0, 0, 1920, 1136);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void placeCards() {
        float startX = 640.0F * Settings.xScale;
        float startY = 750.0F * Settings.yScale;
        float xSpacing = 210.0F * Settings.xScale;
        float ySpacing = 230.0F * Settings.yScale;

        for (int i = 0; i < matchCards.size(); i++) {
            AbstractCard card = matchCards.group.get(i);
            int col = i % 4;
            int row = i / 4;

            card.target_x = startX + (col * xSpacing);
            card.target_y = startY - (row * ySpacing);
            card.current_x = card.target_x;
            card.current_y = card.target_y;
            card.targetDrawScale = 0.5F;
            card.drawScale = 0.5F;
            card.isFlipped = true;
        }
    }

    private CardGroup createMatchGameCards() {
        CardGroup matchGameCards = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

        ArrayList<AbstractCard> cardPool = new ArrayList<>();
        if (isZanyMode) {
            for (AbstractCard c : CardLibrary.getAllCards()) {
                if (c.rarity == this.rarity &&
                        !c.hasTag(AbstractCard.CardTags.HEALING) &&
                        c.type != AbstractCard.CardType.STATUS &&
                        c.type != AbstractCard.CardType.CURSE) {
                    cardPool.add(c);
                }
            }
        } else {
            ArrayList<AbstractCard> sourcePool;
            switch (this.rarity) {
                case COMMON:
                    sourcePool = AbstractDungeon.commonCardPool.group;
                    break;
                case UNCOMMON:
                    sourcePool = AbstractDungeon.uncommonCardPool.group;
                    break;
                default:
                    sourcePool = AbstractDungeon.rareCardPool.group;
                    break;
            }
            cardPool.addAll(sourcePool);
        }

        for (int i = 0; i < 4 && !cardPool.isEmpty(); i++) {
            AbstractCard card = cardPool.get(AbstractDungeon.cardRandomRng.random(cardPool.size() - 1));
            matchGameCards.addToTop(card.makeStatEquivalentCopy());
            matchGameCards.addToTop(card.makeStatEquivalentCopy());
            cardPool.remove(card);
        }

        for (int i = 0; i < 2; i++) {
            AbstractCard curse = AbstractDungeon.returnRandomCurse().makeCopy();
            matchGameCards.addToTop(curse);
            matchGameCards.addToTop(curse.makeStatEquivalentCopy());
        }

        matchGameCards.shuffle(AbstractDungeon.cardRandomRng);
        return matchGameCards;
    }

    private void resetCutsceneState() {
        if (AbstractDungeon.getCurrRoom() != null) {
            AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
            AbstractDungeon.getCurrRoom().isBattleOver = true; // hi code reviewers. this line may seem weird but it was necessary to resolve some bugs.
        }
        AbstractDungeon.overlayMenu.hideBlackScreen();
        AbstractDungeon.isScreenUp = false;
        AbstractDungeon.screen = AbstractDungeon.CurrentScreen.NONE;
        AbstractDungeon.previousScreen = null;

        AbstractDungeon.closeCurrentScreen();
    }

    @Override
    public AbstractDungeon.CurrentScreen curScreen() {
        return ScreenEnum.CAFE_MATCH_SCREEN;
    }

    @Override
    public void openingSettings() {
        AbstractDungeon.previousScreen = curScreen();
    }

    @Override
    public void openingDeck() {
        AbstractDungeon.previousScreen = curScreen();
    }

    public static class GameParams {
        public AbstractCard.CardRarity rarity;
        public boolean isZanyMode;
        public int goldCost;

        public GameParams(AbstractCard.CardRarity rarity, boolean isZanyMode, int goldCost) {
            this.rarity = rarity;
            this.isZanyMode = isZanyMode;
            this.goldCost = goldCost;
        }
    }

    public static class ScreenEnum {
        @SpireEnum
        public static AbstractDungeon.CurrentScreen CAFE_MATCH_SCREEN;
    }
}