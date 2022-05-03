package edu.hitsz.aircraft;

import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BossEnemyTest {
private BossEnemy be;
    @BeforeEach
    void setUp() {
        int locationX = (int) ( Math.random() * (Main.WINDOW_WIDTH - ImageManager.MOB_ENEMY_IMAGE.getWidth()))*1;
        int locationY = (int) (Math.random() * Main.WINDOW_HEIGHT * 0.2)*1;
        int speedX = 5;
        int hp = 300;
         int power = 80;       //子弹伤害
         int direction = 1;  //子弹射击方向 (向上发射：1，向下发射：-1)
        int val = 100;
        be = new BossEnemy(locationX,locationY,speedX,0,hp,power,direction,val,1);
    }

    @AfterEach
    void tearDown() {
        be = null;
    }

    @Test
    void decreaseHp() {
        System.out.println("-----Test decreaseHp method-----");
        assertEquals(50,be.decreaseHp(250));
    }

    @Test
   void notValid() {
       System.out.println("-----Test noValid method-----");
       assertEquals(false,be.notValid());
    }

}