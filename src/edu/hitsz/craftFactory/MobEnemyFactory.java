package edu.hitsz.craftFactory;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.MobEnemy;
import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;

public class MobEnemyFactory extends BaseCraftFactory{

    @Override
    public AbstractAircraft createAircraft(int hp,int speedX,int speedY,int shootNum) {
        val = 20;
        direction = 1;
        power = 0;
        return new MobEnemy(locationX,locationY,speedX,speedY,hp,power,direction,val,shootNum);
    }
}
