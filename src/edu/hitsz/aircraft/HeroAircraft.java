package edu.hitsz.aircraft;

import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.HeroBullet;
import edu.hitsz.shootStrategy.Context;
import edu.hitsz.shootStrategy.ShootStrategy;
import edu.hitsz.shootStrategy.StraightShoot;

import java.util.LinkedList;
import java.util.List;

import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * 英雄飞机，游戏玩家操控
 * @author hitsz
 */
public class HeroAircraft extends AbstractAircraft {

    /** 单例模式 */

    private volatile static HeroAircraft hero;  //  volatile 防止发生reorder问题吧
    //  攻击策略
    private Context c = new Context(new StraightShoot());

    private HeroAircraft(int locationX, int locationY, int speedX, int speedY, int hp,int power,int direction,int shootNum){
        super(locationX,locationY,speedX,speedY,hp,power,direction,0,shootNum);
    }
    public static HeroAircraft getHeroAircraft(int locationX, int locationY, int speedX, int speedY, int hp,int power,int direction,int shootNum){
        if(hero==null){ //  上锁
            synchronized (HeroAircraft.class){
                if(hero==null){
                    hero = new HeroAircraft(locationX,locationY,speedX,speedY,hp,power,direction,shootNum);
                }
            }
        }

        return hero;
    }



    public int getPower(){
        return power;
    }

    @Override
    public void forward() {
        // 英雄机由鼠标控制，不通过forward函数移动
    }

    public void setStrategy(ShootStrategy s)
    {
        //  之后会execute
        c.setStrategy(s);
    }

    @Override
    /**
     * 通过射击产生子弹
     * @return 射击出的子弹List
     * 对于每种发射策略，伤害power，方向direction，速度speedX，speedY，以及图片type由飞机类传参控制
     */
    public List<BaseBullet> shoot() {
        return c.executeStrategy(power,direction,getLocationX(),getLocationY(),speedY,speedX,shootNum,HeroAircraft.class.getSimpleName());
    }

    public void addHp(int x)
    {
        hp = min(hp+x,maxHp);
    }
    public void addSpeedY(int x){
        speedY += x;
    }
    public void addPower(int x){
        power+=x;
    }
    public void addNum(int x){
        shootNum += x;
    }
    public int getNum(){return shootNum;}
}
