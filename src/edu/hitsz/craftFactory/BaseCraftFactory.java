package edu.hitsz.craftFactory;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;

public abstract class BaseCraftFactory {

    //  坐标、得分、子弹伤害、方向 使用默认数据。不让用户自定义了。怪麻烦的，也没啥视觉效果。
    //  如果想交给用户，那么再改工厂的接口即可。

    protected int locationX = (int) ( Math.random() * (Main.WINDOW_WIDTH - ImageManager.MOB_ENEMY_IMAGE.getWidth()))*1;
    protected int locationY = (int) (Math.random() * Main.WINDOW_HEIGHT * 0.2)*1;
    protected int direction ;
    protected int val;
    protected int power ;
    public abstract AbstractAircraft createAircraft(int hp,int speedX,int speedY,int shootNum);
}
