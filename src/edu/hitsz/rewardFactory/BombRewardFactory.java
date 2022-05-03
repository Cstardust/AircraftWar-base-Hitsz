package edu.hitsz.rewardFactory;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.reward.AbstractReward;
import edu.hitsz.reward.BombReward;

public class BombRewardFactory extends BaseRewardFactory {
    @Override
    public AbstractReward createReward(AbstractAircraft craft) {
        return new BombReward(craft.getLocationX(),craft.getLocationY(),speedX,speedY);
    }
}
