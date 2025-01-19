package spireCafe.interactables.patrons.dandadan;

import basemod.abstracts.CustomSavable;
import com.megacrit.cardcrawl.rewards.RewardItem;

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
        } else if (RightballPotionPatch.potionReward != null) {
            return ((RightballPotion) RightballPotionPatch.potionReward.potion).returnChance;
        }
        return 0;
    }

}
