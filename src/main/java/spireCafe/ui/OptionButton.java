package spireCafe.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class OptionButton {
    private static final float OPTION_SPACING_Y;
    private static final float TEXT_ADJUST_X;
    private static final float TEXT_ADJUST_Y;
    public String msg;
    private Color textColor;
    private Color boxColor;
    private float x;
    private float y;
    public Hitbox hb;
    private float animTimer;
    private float alpha;
    private static final Color TEXT_ACTIVE_COLOR;
    private static final Color TEXT_INACTIVE_COLOR;
    private static final Color TEXT_DISABLED_COLOR;
    private final Color boxInactiveColor;
    public boolean pressed;
    public boolean isDisabled;
    public int slot;
    private final AbstractCard cardToPreview;
    private final AbstractRelic relicToPreview;
    private final AbstractPotion potionToPreview;
    public Dialog dialog;
    public BiConsumer<Dialog, Integer> optionResult;

    public OptionButton(Dialog dialog, int slot, String msg, boolean isDisabled, AbstractCard previewCard, AbstractRelic previewRelic, AbstractPotion previewPotion) {
        this.dialog = dialog;
        this.textColor = new Color(0.0F, 0.0F, 0.0F, 0.0F);
        this.boxColor = new Color(0.0F, 0.0F, 0.0F, 0.0F);
        this.y = -9999.0F * Settings.scale;
        this.animTimer = 0.5F;
        this.alpha = 0.0F;
        this.boxInactiveColor = new Color(0.2F, 0.25F, 0.25F, 0.0F);
        this.pressed = false;
        this.x = Settings.WIDTH / 2.0f;

        this.slot = slot;
        this.isDisabled = isDisabled;
        this.cardToPreview = previewCard;
        this.relicToPreview = previewRelic;
        this.potionToPreview = previewPotion;
        if (isDisabled) {
            this.msg = this.stripColor(msg);
        } else {
            this.msg = msg;
        }

        this.hb = new Hitbox(892.0F * Settings.xScale, 80.0F * Settings.yScale);
    }

    public OptionButton(Dialog dialog, int slot, String msg, AbstractCard previewCard, AbstractRelic previewRelic) {
        this(dialog, slot, msg, false, previewCard, previewRelic, null);
    }

    public OptionButton(Dialog dialog, int slot, String msg, boolean isDisabled, AbstractCard previewCard) {
        this(dialog, slot, msg, isDisabled, previewCard, null, null);
    }

    public OptionButton(Dialog dialog, int slot, String msg, boolean isDisabled, AbstractRelic previewRelic) {
        this(dialog, slot, msg, isDisabled, null, previewRelic, null);
    }

    public OptionButton(Dialog dialog, int slot, String msg, boolean isDisabled, AbstractPotion previewPotion) {
        this(dialog, slot, msg, isDisabled, null, null, previewPotion);
    }

    public OptionButton(Dialog dialog, int slot, String msg) {
        this(dialog, slot, msg, false, null, null, null);
    }

    public OptionButton(Dialog dialog, int slot, String msg, boolean isDisabled) {
        this(dialog, slot, msg, isDisabled, null, null, null);
    }

    public OptionButton(Dialog dialog, int slot, String msg, AbstractCard previewCard) {
        this(dialog, slot, msg, false, previewCard);
    }

    public OptionButton(Dialog dialog, int slot, String msg, AbstractRelic previewRelic) {
        this(dialog, slot, msg, false, previewRelic);
    }

    public OptionButton(Dialog dialog, int slot, String msg, AbstractPotion previewPotion) {
        this(dialog, slot, msg, false, previewPotion);
    }

    public void setOptionResult(Consumer<Integer> optionResult) {
        this.optionResult = (dialog, i) -> {
            optionResult.accept(i);
        };
    }

    private String stripColor(String input) {
        input = input.replace("#r", "");
        input = input.replace("#g", "");
        input = input.replace("#b", "");
        input = input.replace("#y", "");
        return input;
    }

    public void calculateY(int numOptions) {
        this.y = Settings.HEIGHT / 2.0f;
        this.y += (float)this.slot * OPTION_SPACING_Y;
        this.y -= (float)(numOptions - 1) / 2 * OPTION_SPACING_Y;

        this.hb.move(this.x, this.y);
    }

    public void update(int numOptions) {
        this.calculateY(numOptions);
        this.hoverAndClickLogic();
        this.updateAnimation();
    }

    private void updateAnimation() {
        if (this.animTimer != 0.0F) {
            this.animTimer -= Gdx.graphics.getDeltaTime();
            if (this.animTimer < 0.0F) {
                this.animTimer = 0.0F;
            }

            this.alpha = Interpolation.exp5In.apply(0.0F, 1.0F, 1.0F - this.animTimer / 0.5F);
        }

        this.textColor.a = this.alpha;
        this.boxColor.a = this.alpha;
    }

    private void hoverAndClickLogic() {
        this.hb.update();
        if (this.hb.hovered && InputHelper.justClickedLeft) {
            InputHelper.justClickedLeft = false;
            this.hb.clickStarted = true;
        }

        if (this.hb.clicked) {
            this.hb.clicked = false;
            this.pressed = true;
            if (!this.isDisabled) {
                this.optionResult.accept(this.dialog, slot);
            }
        }

        if (!this.isDisabled) {
            if (this.hb.hovered) {
                this.textColor = TEXT_ACTIVE_COLOR;
                this.boxColor = Color.WHITE.cpy();
            } else {
                this.textColor = TEXT_INACTIVE_COLOR;
                this.boxColor = new Color(0.4F, 0.4F, 0.4F, 1.0F);
            }
        } else {
            this.textColor = TEXT_DISABLED_COLOR;
            this.boxColor = this.boxInactiveColor;
        }

        if (this.hb.hovered) {
            if (!this.isDisabled) {
                this.textColor = TEXT_ACTIVE_COLOR;
            } else {
                this.textColor = Color.GRAY.cpy();
            }
        } else if (!this.isDisabled) {
            this.textColor = TEXT_ACTIVE_COLOR;
        } else {
            this.textColor = Color.GRAY.cpy();
        }

    }

    public void render(SpriteBatch sb) {
        float scale = Settings.scale;
        float xScale = Settings.xScale;
        if (this.hb.clickStarted) {
            scale *= 0.99F;
            xScale *= 0.99F;
        }

        if (this.isDisabled) {
            sb.setColor(Color.WHITE);
            sb.draw(ImageMaster.EVENT_BUTTON_DISABLED, this.x - 445.0F, this.y - 38.5F, 445.0F, 38.5F, 890.0F, 77.0F, xScale, scale, 0.0F, 0, 0, 890, 77, false, false);
        } else {
            sb.setColor(this.boxColor);
            sb.draw(ImageMaster.EVENT_BUTTON_ENABLED, this.x - 445.0F, this.y - 38.5F, 445.0F, 38.5F, 890.0F, 77.0F, xScale, scale, 0.0F, 0, 0, 890, 77, false, false);
            sb.setBlendFunction(GL30.GL_SRC_ALPHA, GL30.GL_ONE); // Additive Mode
            sb.setColor(new Color(1.0F, 1.0F, 1.0F, 0.15F));
            sb.draw(ImageMaster.EVENT_BUTTON_ENABLED, this.x - 445.0F, this.y - 38.5F, 445.0F, 38.5F, 890.0F, 77.0F, xScale, scale, 0.0F, 0, 0, 890, 77, false, false);
            sb.setBlendFunction(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA); // NORMAL
        }

        float maxTextLength = 800.0F * Settings.xScale;
        float textWidth = FontHelper.getSmartWidth(FontHelper.largeDialogOptionFont, this.msg, (float)Settings.WIDTH, 0.0F);
        float textX = Interpolation.linear.apply(this.x + 445 * Settings.xScale, this.x, textWidth / maxTextLength);
        if (textWidth > maxTextLength) {
            FontHelper.renderSmartText(sb, FontHelper.smallDialogOptionFont, this.msg, textX + TEXT_ADJUST_X*0.8f, this.y + TEXT_ADJUST_Y, (float)Settings.WIDTH, 0.0F, this.textColor);
        } else {
            FontHelper.renderSmartText(sb, FontHelper.largeDialogOptionFont, this.msg, textX + TEXT_ADJUST_X, this.y + TEXT_ADJUST_Y, (float)Settings.WIDTH, 0.0F, this.textColor);
        }

        this.hb.render(sb);
    }

    public void renderCardPreview(SpriteBatch sb) {
        if (this.cardToPreview != null && this.hb.hovered) {
            this.cardToPreview.current_x = this.x + this.hb.width / 1.75F;
            if (this.y < this.cardToPreview.hb.height / 2.0F + 5.0F) {
                this.y = this.cardToPreview.hb.height / 2.0F + 5.0F;
            }

            this.cardToPreview.current_y = this.y;
            this.cardToPreview.render(sb);
        }
    }

    public void renderRelicPreview(SpriteBatch sb) {
        if (!Settings.isControllerMode && this.relicToPreview != null && this.hb.hovered) {
            TipHelper.queuePowerTips(470.0F * Settings.scale, (float)InputHelper.mY + TipHelper.calculateToAvoidOffscreen(this.relicToPreview.tips, (float)InputHelper.mY), this.relicToPreview.tips);
        }
    }

    public void renderPotionPreview(SpriteBatch sb) {
        if (!Settings.isControllerMode && this.potionToPreview != null && this.hb.hovered) {
            TipHelper.queuePowerTips(470.0F * Settings.scale,
                    (float) InputHelper.mY
                            + TipHelper.calculateToAvoidOffscreen(this.potionToPreview.tips, (float) InputHelper.mY),
                    this.potionToPreview.tips);
        }
    }

    static {
        OPTION_SPACING_Y = -82.0F * Settings.scale;
        TEXT_ADJUST_X = -415.0F * Settings.xScale;
        TEXT_ADJUST_Y = 10.0F * Settings.scale;
        TEXT_ACTIVE_COLOR = Color.WHITE.cpy();
        TEXT_INACTIVE_COLOR = new Color(0.8F, 0.8F, 0.8F, 1.0F);
        TEXT_DISABLED_COLOR = Color.FIREBRICK.cpy();
    }
}
