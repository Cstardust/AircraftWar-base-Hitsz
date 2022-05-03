package edu.hitsz.reward;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.application.Main;
import edu.hitsz.basic.AbstractFlyingObject;

import java.util.ArrayList;

/*
道具类
*/
public abstract class AbstractReward extends AbstractFlyingObject {

    //  道具作用
    public abstract void takeEffect(HeroAircraft heroAircraft);

    public AbstractReward(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void forward() {
        super.forward();

        // 判定 x 轴出界
        if (locationX <= 0 || locationX >= Main.WINDOW_WIDTH) {
            vanish();
        }

        // 判定 y 轴出界
        if (speedY > 0 && locationY >= Main.WINDOW_HEIGHT ) {
            // 向下飞行出界
            vanish();
        }else if (locationY <= 0){
            // 向上飞行出界
            vanish();
        }
    }

}
