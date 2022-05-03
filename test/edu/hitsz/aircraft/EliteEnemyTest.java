package edu.hitsz.aircraft;

import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EliteEnemyTest {
private EliteEnemy e;
    @BeforeEach
    void setUp() {
        int val = 10;
        e = new EliteEnemy(
                (int) ( Math.random() * (Main.WINDOW_WIDTH - ImageManager.MOB_ENEMY_IMAGE.getWidth()))*1,
                (int) (Math.random() * Main.WINDOW_HEIGHT * 0.2)*1,
                0, 5, 50,40,1,val,1);
    }

    @AfterEach
    void tearDown() {
        e = null;
    }

    @Test
    void getHp() {
        e.decreaseHp(5);
        System.out.println("----Test getHp method executed----");
        assertEquals(45,e.getHp());
    }

    @Test
    void getSpeedY() {
        System.out.println("----Test getSpeedY method executed----");
        assertEquals(5,e.getSpeedY());
    }

}