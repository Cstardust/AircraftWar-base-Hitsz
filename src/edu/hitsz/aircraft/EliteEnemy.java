package edu.hitsz.aircraft;

import edu.hitsz.application.Main;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.observer.Observer;
import edu.hitsz.shootStrategy.Context;
import edu.hitsz.shootStrategy.ShootStrategy;
import edu.hitsz.shootStrategy.StraightShoot;

import java.util.List;

public class EliteEnemy extends AbstractAircraft implements Observer {

    /** 攻击方式 */
    private Context c = new Context(new StraightShoot());

    public EliteEnemy(int locationX, int locationY, int speedX, int speedY, int hp,int power,int direction,int val,int shootNum) {
        super(locationX, locationY, speedX, speedY, hp,power,direction,val,shootNum);
    }


    public void setStrategy(ShootStrategy s)
    {
        c.setStrategy(s);
    }

    @Override
    public void forward() {
        super.forward();
        // 判定 y 轴向下飞行出界
        if (locationY >= Main.WINDOW_HEIGHT ) {
            vanish();
        }
    }
    @Override
    public List<BaseBullet> shoot() {
        if(!isValid) {
            return null;
        }
        return c.executeStrategy(power,direction,getLocationX(),getLocationY(),speedY,speedX,shootNum,EliteEnemy.class.getSimpleName());
    }

    @Override
    public int handler(){
        vanish();
        return getVal();
    }
}
