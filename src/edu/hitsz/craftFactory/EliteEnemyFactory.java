package edu.hitsz.craftFactory;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.EliteEnemy;
import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;

public class EliteEnemyFactory extends BaseCraftFactory{
    /** 攻击方式 */
    @Override
    public AbstractAircraft createAircraft(int hp,int speedX,int speedY,int shootNum) {
        val = 50;
        power = 30;
        direction = 1;
        return new EliteEnemy(locationX,locationY,speedX,speedY,hp,power,direction,val,shootNum);
    }
}