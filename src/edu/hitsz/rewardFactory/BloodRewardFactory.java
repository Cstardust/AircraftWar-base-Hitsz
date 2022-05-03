package edu.hitsz.rewardFactory;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.reward.AbstractReward;
import edu.hitsz.reward.BloodReward;

public class BloodRewardFactory extends BaseRewardFactory {
    @Override
    public BloodReward createReward(AbstractAircraft craft) {
        return new BloodReward(craft.getLocationX(),craft.getLocationY(),speedX,speedY);
    }
}
