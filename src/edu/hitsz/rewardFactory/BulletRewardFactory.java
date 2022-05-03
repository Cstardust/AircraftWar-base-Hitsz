package edu.hitsz.rewardFactory;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.reward.AbstractReward;
import edu.hitsz.reward.BulletReward;

public class BulletRewardFactory extends BaseRewardFactory {
    @Override
    public AbstractReward createReward(AbstractAircraft craft) {
        return new BulletReward(craft.getLocationX(),craft.getLocationY(),speedX,speedY);
    }
}
