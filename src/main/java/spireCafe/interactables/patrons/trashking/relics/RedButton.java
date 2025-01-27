package spireCafe.interactables.patrons.trashking.relics;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import spireCafe.abstracts.AbstractSCRelic;
import spireCafe.interactables.patrons.trashking.TrashKingPatron;

import static spireCafe.Anniv7Mod.makeID;

public class RedButton extends AbstractSCRelic {
    public static final String ID = makeID(RedButton.class.getSimpleName());
    private static final int DAMAGE = 15;
    private boolean usedThisCombat = false;

    public RedButton() {
        super(ID, TrashKingPatron.assetID, AbstractRelic.RelicTier.SPECIAL, AbstractRelic.LandingSound.HEAVY);
    }

    @Override
    public void atBattleStart() {
        if (!usedThisCombat && (
                AbstractDungeon.getCurrRoom().eliteTrigger ||
                        AbstractDungeon.getMonsters().monsters.stream().anyMatch(m -> m.type == AbstractMonster.EnemyType.BOSS)
        )) {
            this.flash();
            AbstractMonster target = AbstractDungeon.getMonsters().getRandomMonster(true);
            AbstractDungeon.actionManager.addToBottom(new DamageAction(
                    target,
                    new DamageInfo(AbstractDungeon.player, DAMAGE, DamageInfo.DamageType.THORNS),
                    AbstractGameAction.AttackEffect.FIRE
            ));
            this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            grayscale = true;
            usedThisCombat = true;
        }
    }

    @Override
    public void onEnterRoom(AbstractRoom room) {
        grayscale = false;
        usedThisCombat = false;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new RedButton();
    }
}