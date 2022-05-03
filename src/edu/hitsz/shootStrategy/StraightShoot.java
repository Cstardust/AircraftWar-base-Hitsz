package edu.hitsz.shootStrategy;

import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.bullet.HeroBullet;

import java.util.LinkedList;
import java.util.List;

/**
 * @author dell
 * 直射
 */
public class StraightShoot implements ShootStrategy{
//    private int shootNum = 1;
//    private int power = 30;
//    private int direction = 0;
    private int x = 0;
    private int y = 0;
    private int speedX = 0;
    private int speedY = 0;

    @Override
    public List<BaseBullet> doShoot(int power,int direction,int airX,int airY,int spy,int spx,int shootNum,String type) {
//        power = pw;
//        direction = dir;
        x = airX;
        y = airY + direction*2;
        speedX = spx;
        speedY = spy + direction*5;
        List<BaseBullet> res = new LinkedList<>();
        for(int i=0; i<shootNum; i++){
            // 子弹发射位置相对飞机位置向前偏移
            // 多个子弹横向分散
            BaseBullet abstractBullet = null;
            if(type.equals("HeroAircraft"))
            {
                abstractBullet = new HeroBullet(x + (i*2 - shootNum + 1)*10, y, speedX, speedY, power);
            }
            else if(type.equals("EliteEnemy"))
            {
                abstractBullet = new EnemyBullet(x + (i*2 - shootNum + 1)*10, y, speedX, speedY, power);
            }
            else
            {
                System.out.println("others");
            }
            res.add(abstractBullet);
        }
        return res;
    }
}

