package edu.hitsz.shootStrategy;

import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.bullet.HeroBullet;

import java.util.LinkedList;
import java.util.List;

import static java.lang.Math.max;

public class ScatteredShoot implements ShootStrategy{
//    private int shootNum = 3;   //  目前仅支持3
    private int power = 0;
    private int direction = 0;
    private int x = 0;
    private int y = 0;
    private int speedX = 0;
    private int speedY = 0;

    @Override
    public List<BaseBullet> doShoot(int power,int direction, int airX, int airY, int spy, int spx,int shootNum , String type) {
        x = airX;
        y = airY + direction*2;
        speedX = spx;
        speedY = spy + direction*5;
        shootNum = max(3,shootNum);
        int dx[]={-2,0,2};
        List<BaseBullet> res = new LinkedList<>();
        for(int i=0; i<shootNum; i++){
            // 子弹发射位置相对飞机位置向前偏移
            // 多个子弹横向分散
            BaseBullet abstractBullet = null;
            if(type.equals("HeroAircraft"))
            {
                abstractBullet = new HeroBullet(x + (i*2 - shootNum + 1)*10, y, speedX+dx[i%3], speedY, power);
            }
            else if(type.equals("BossEnemy"))
            {
                abstractBullet = new EnemyBullet(x + (i*2 - shootNum + 1)*10, y, speedX+dx[i%3], speedY, power);
            }
            res.add(abstractBullet);
        }
        return res;
    }
}
