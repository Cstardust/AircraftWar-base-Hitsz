package edu.hitsz.reward;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.application.MusicThread;
import edu.hitsz.shootStrategy.ScatteredShoot;
import edu.hitsz.shootStrategy.StraightShoot;

/*
道具：弹药包
作用：打印
 */
public class BulletReward extends AbstractReward {
    private static MusicThread mt = null;
    private volatile static int validTime = 0;              //  累计时间清0
    private static Thread t = null;
    @Override
    public void takeEffect(HeroAircraft heroAircraft) {
        //  6s后恢复
        Runnable r = ()->{
            heroAircraft.setStrategy(new ScatteredShoot());
            try {
                for(int i=0;i<validTime;++i){
                    Thread.sleep(1000*3);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            heroAircraft.setStrategy(new StraightShoot());
            validTime = 0;                      //  累计时间清0
            mt.setLapse(true);                  //  通过标志位 通知音乐线程 结束音乐
        };

        if(t==null||!t.isAlive()){
            ++validTime;
            t = new Thread(r,"bullet_reward thread");
            t.start();
        }
        else if(t.isAlive()){
            ++validTime;
        }

        if(MusicThread.getIsMusic()){
            new MusicThread("src/videos/get_supply.wav",false,false).start();
            if(mt==null||!mt.isAlive()){
                mt = new MusicThread("src/videos/bullet.wav",true,false);
                mt.start();
            }
        }
    }

    public BulletReward(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

}
