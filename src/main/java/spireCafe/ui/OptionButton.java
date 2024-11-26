package spireCafe.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic;

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
    public Dialog dialog;

    public OptionButton(Dialog dialog, int slot, String msg, boolean isDisabled, AbstractCard previewCard, AbstractRelic previewRelic) {
        this.dialog = dialog;
        this.textColor = new Color(0.0F, 0.0F, 0.0F, 0.0F);
        this.boxColor = new Color(0.0F, 0.0F, 0.0F, 0.0F);
        this.y = -9999.0F * Settings.scale;
        this.animTimer = 0.5F;
        this.alpha = 0.0F;
        this.boxInactiveColor = new Color(0.2F, 0.25F, 0.25F, 0.0F);
        this.pressed = false;
        this.x = 860.0F * Settings.xScale;

        this.slot = slot;
        this.isDisabled = isDisabled;
        this.cardToPreview = previewCard;
        this.relicToPreview = previewRelic;
        if (isDisabled) {
            this.msg = this.stripColor(msg);
        } else {
            this.msg = msg;
        }

        this.hb = new Hitbox(892.0F * Settings.xScale, 80.0F * Settings.yScale);
    }

    public OptionButton(Dialog dialog, int slot, String msg, AbstractCard previewCard, AbstractRelic previewRelic) {
        this(dialog, slot, msg, false, previewCard, previewRelic);
    }

    public OptionButton(Dialog dialog, int slot, String msg, boolean isDisabled, AbstractCard previewCard) {
        this(dialog, slot, msg, isDisabled, previewCard, null);
    }

    public OptionButton(Dialog dialog, int slot, String msg, boolean isDisabled, AbstractRelic previewRelic) {
        this(dialog, slot, msg, isDisabled, null, previewRelic);
    }

    public OptionButton(Dialog dialog, int slot, String msg) {
        this(dialog, slot, msg, false, null, null);
    }

    public OptionButton(Dialog dialog, int slot, String msg, boolean isDisabled) {
        this(dialog, slot, msg, isDisabled, null, null);
    }

    public OptionButton(Dialog dialog, int slot, String msg, AbstractCard previewCard) {
        this(dialog, slot, msg, false, previewCard);
    }

    public OptionButton(Dialog dialog, int slot, String msg, AbstractRelic previewRelic) {
        this(dialog, slot, msg, false, previewRelic);
    }

    private String stripColor(String input) {
        input = input.replace("#r", "");
        input = input.replace("#g", "");
        input = input.replace("#b", "");
        input = input.replace("#y", "");
        return input;
    }

    public void calculateY(int numOptions) {
        this.y = Settings.OPTION_Y - 24.0F * Settings.scale;
        this.y += (float)this.slot * OPTION_SPACING_Y;
        this.y -= (float)numOptions * OPTION_SPACING_Y;

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
            this.dialog.onOptionClick(this.slot);
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
            sb.setBlendFunction(770, 1);
            sb.setColor(new Color(1.0F, 1.0F, 1.0F, 0.15F));
            sb.draw(ImageMaster.EVENT_BUTTON_ENABLED, this.x - 445.0F, this.y - 38.5F, 445.0F, 38.5F, 890.0F, 77.0F, xScale, scale, 0.0F, 0, 0, 890, 77, false, false);
            sb.setBlendFunction(770, 771);
        }

        if (FontHelper.getSmartWidth(FontHelper.largeDialogOptionFont, this.msg, (float)Settings.WIDTH, 0.0F) > 800.0F * Settings.xScale) {
            FontHelper.renderSmartText(sb, FontHelper.smallDialogOptionFont, this.msg, this.x + TEXT_ADJUST_X, this.y + TEXT_ADJUST_Y, (float)Settings.WIDTH, 0.0F, this.textColor);
        } else {
            FontHelper.renderSmartText(sb, FontHelper.largeDialogOptionFont, this.msg, this.x + TEXT_ADJUST_X, this.y + TEXT_ADJUST_Y, (float)Settings.WIDTH, 0.0F, this.textColor);
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

    static {
        OPTION_SPACING_Y = -82.0F * Settings.scale;
        TEXT_ADJUST_X = -400.0F * Settings.xScale;
        TEXT_ADJUST_Y = 10.0F * Settings.scale;
        TEXT_ACTIVE_COLOR = Color.WHITE.cpy();
        TEXT_INACTIVE_COLOR = new Color(0.8F, 0.8F, 0.8F, 1.0F);
        TEXT_DISABLED_COLOR = Color.FIREBRICK.cpy();
    }
}
