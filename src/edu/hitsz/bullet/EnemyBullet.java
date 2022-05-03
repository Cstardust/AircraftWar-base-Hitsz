package edu.hitsz.bullet;

import edu.hitsz.observer.Observer;

/**
 * @Author hitsz
 */
public class EnemyBullet extends BaseBullet implements Observer {

    public EnemyBullet(int locationX, int locationY, int speedX, int speedY, int power) {
        super(locationX, locationY, speedX, speedY, power);
    }

    @Override
    public int handler(){
        vanish();
        return 0;
    }
}
