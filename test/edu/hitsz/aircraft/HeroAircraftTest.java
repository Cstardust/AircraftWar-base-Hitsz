package edu.hitsz.aircraft;

import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static java.lang.Math.min;
import static org.junit.jupiter.api.Assertions.*;

class HeroAircraftTest {
    private HeroAircraft ha;
    @BeforeEach
    void setUp() {
        ha = HeroAircraft.getHeroAircraft(Main.WINDOW_WIDTH / 2,
                Main.WINDOW_HEIGHT - ImageManager.HERO_IMAGE.getHeight() ,
                0, 0, 200,0,-1,1);
    }

    @AfterEach
    void tearDown() {
        ha = null;
    }

    @Test
    void addHp() {
        System.out.println("----Test AddHp method executed----");
        ha.addHp(50);
        assertEquals(200,ha.getHp());
    }

    @Test
    void getHeroAircraft() {
        System.out.println("----Test getHeroAircraft method executed----");
        assertEquals(ha,HeroAircraft.getHeroAircraft(Main.WINDOW_WIDTH / 2,
                Main.WINDOW_HEIGHT - ImageManager.HERO_IMAGE.getHeight() ,
                0, 0, 200,0,-1,1));
    }
}