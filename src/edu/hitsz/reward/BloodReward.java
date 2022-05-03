package edu.hitsz.reward;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.application.MusicThread;

/*
道具：血包
作用：加血
 */
public class BloodReward extends AbstractReward {
    private int adds = 80;
    @Override
    public void takeEffect(HeroAircraft heroAircraft) {
        if(MusicThread.getIsMusic()){
            new MusicThread("src/videos/get_supply.wav",false,false).start();
        }
        heroAircraft.addHp(adds);
    }

    public BloodReward(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }
}
