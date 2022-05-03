package edu.hitsz.bullet;

import edu.hitsz.observer.Observer;

/**
 * @Author hitsz
 */
public class HeroBullet extends BaseBullet implements Observer {

    public HeroBullet(int locationX, int locationY, int speedX, int speedY, int power) {
        super(locationX, locationY, speedX, speedY, power);
    }

    @Override
    public int handler(){
        System.out.println("hero bullet");return 0;}

}
