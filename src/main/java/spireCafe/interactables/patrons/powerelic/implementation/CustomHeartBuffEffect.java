//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package spireCafe.interactables.patrons.powerelic.implementation;

import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.SwirlyBloodEffect;

public class CustomHeartBuffEffect extends AbstractGameEffect {

    public EffectColor effectType = EffectColor.GOLD;
    public enum EffectColor {
        GOLD,REDFOX,RANDOM,PASTEL
    }


    float x;
    float y;

    public CustomHeartBuffEffect(float x, float y) {
        this.x = x;
        this.y = y;
        this.duration = 0.5F;
        this.scale = 0.0F;
    }

    public void update() {
        if (this.duration == 0.5F) {
            CardCrawlGame.sound.playA("BUFF_2", -0.6F);
        }

        this.scale -= Gdx.graphics.getDeltaTime();
        if (this.scale < 0.0F) {
            this.scale = 0.05F;
            {
                SwirlyBloodEffect sbe = new SwirlyBloodEffect(this.x + MathUtils.random(-150.0F, 150.0F) * Settings.scale, this.y + MathUtils.random(-150.0F, 150.0F) * Settings.scale);
                Color c = effectColor();
                ReflectionHacks.setPrivate(sbe, AbstractGameEffect.class,"color",c);
                AbstractDungeon.effectsQueue.add(sbe);
            }
            {
                SwirlyBloodEffect sbe = new SwirlyBloodEffect(this.x + MathUtils.random(-150.0F, 150.0F) * Settings.scale, this.y + MathUtils.random(-150.0F, 150.0F) * Settings.scale);
                Color c = effectColor();
                ReflectionHacks.setPrivate(sbe, AbstractGameEffect.class,"color",c);
                AbstractDungeon.effectsQueue.add(sbe);
            }
        }

        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.0F) {
            this.isDone = true;
        }

    }

    public void render(SpriteBatch sb) {
    }

    public void dispose() {
    }

    public Color effectColor(){
        if(effectType==EffectColor.GOLD)
            return new Color(MathUtils.random(0.8F, 1.0F), MathUtils.random(0.6F, 0.8F), 0.3F, 0.25F);
        else if(effectType==EffectColor.REDFOX)
            return new Color(MathUtils.random(0.8F, 1.0F), MathUtils.random(0.1F, 0.3F), 0.0F, 0.25F);
        else if(effectType==EffectColor.RANDOM)
            return new Color(MathUtils.random(0.0F, 1.0F), MathUtils.random(0.0F, 1.0F), MathUtils.random(0.0F, 1.0F), 0.25F);
        else //PASTEL
            return new Color(MathUtils.random(0.3F, 1.0F), MathUtils.random(0.3F, 1.0F), MathUtils.random(0.3F, 1.0F), 0.25F);
    }
}

