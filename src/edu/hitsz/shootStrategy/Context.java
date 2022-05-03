package edu.hitsz.shootStrategy;

import edu.hitsz.bullet.BaseBullet;

import java.util.List;

public class Context {
    private ShootStrategy strategy;
    public Context(ShootStrategy s)
    {
        strategy = s;
    }
    public void setStrategy(ShootStrategy s)
    {
        strategy = s;
    }
    public List<BaseBullet> executeStrategy(int pw, int dir, int airX, int airY, int spy,int spx,int shootNum,String type)
    {
        return strategy.doShoot(pw,dir,airX,airY,spy,spx,shootNum,type);
    }
}
