package edu.hitsz.aircraft;

import edu.hitsz.application.MusicThread;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.observer.Observer;
import edu.hitsz.shootStrategy.Context;
import edu.hitsz.shootStrategy.ScatteredShoot;

import java.util.List;

public class BossEnemy extends AbstractAircraft implements Observer {

    /** 攻击方式 */

    private Context c = new Context(new ScatteredShoot());
    private MusicThread mt;
    public BossEnemy(int locationX, int locationY, int speedX, int speedY, int hp,int power,int direction,int val,int shootNum) {
        super(locationX, locationY, speedX, speedY, hp,power,direction,val,shootNum);
        if(MusicThread.getIsMusic()){
            mt = new MusicThread("src/videos/bgm_boss.wav",false,false);
            mt.start();
        }
    }

    @Override
    public List<BaseBullet> shoot() {
        if(!isValid) {
            return null;
        }
        return c.executeStrategy(power,direction,getLocationX(),getLocationY(),speedY,speedX,shootNum,BossEnemy.class.getSimpleName());
    }

    @Override
    public void vanish() {
        if(mt!=null)
        {
            mt.setIsLoop(false);
            mt.setLapse(true);
        }
        isValid = false;
    }

    @Override
    public int handler(){
        return 0;
    }

}
