package spireCafe.interactables.patrons.dandadan;

import com.megacrit.cardcrawl.rewards.RewardItem;

import basemod.abstracts.CustomSavable;

public class RightBallPotionSavable implements CustomSavable<Integer> {

    @Override
    public void onLoad(Integer chance) {
        if (chance == null || chance == 0) {
            return;
        } else {
            RightballPotionPatch.potionReward = new RewardItem(new RightballPotion(chance));
        }
    }

    @Override
    public Integer onSave() {
        if (RightballPotionPatch.rbp != null) {
            return RightballPotionPatch.rbp.returnChance;
        } else if (RightballPotionPatch.potionReward != null ) {
            if (RightballPotionPatch.potionReward.potion instanceof RightballPotion) {
                return ((RightballPotion) RightballPotionPatch.potionReward.potion).returnChance;
            }
        }
        return 0;
    }

}
