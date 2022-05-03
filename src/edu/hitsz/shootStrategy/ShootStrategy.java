package edu.hitsz.shootStrategy;


import edu.hitsz.bullet.BaseBullet;

import java.util.List;

//对于每种发射策略，伤害power，方向direction，速度speedX，speedY，以及图片type由飞机类传参控制
public interface ShootStrategy {
    public List<BaseBullet> doShoot(int power,int direction,int airX,int airY,int spy,int spx,int shootNum , String type);
}
