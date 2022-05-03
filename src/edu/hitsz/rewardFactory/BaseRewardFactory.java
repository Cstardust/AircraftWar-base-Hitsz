package edu.hitsz.rewardFactory;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.reward.AbstractReward;

public abstract class BaseRewardFactory{
    protected int speedX = 0;
    protected int speedY = 10;
    public abstract AbstractReward createReward(AbstractAircraft craft);

}
