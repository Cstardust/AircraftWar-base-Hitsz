package edu.hitsz.gameTemplate;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.application.HeroController;
import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;
import edu.hitsz.craftFactory.EliteEnemyFactory;
import edu.hitsz.craftFactory.MobEnemyFactory;
import edu.hitsz.gameTemplate.Game;

import javax.imageio.ImageIO;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedHashMap;

public class EasyGame extends Game {
    @Override
    protected void levelUp() {

    }

    @Override
    protected void loadAirCrafts()
    {
        // 周期性执行（控制频率）
        if (timeCountAndNewCycleJudge("normal")) {
            // 新敌机产生
            if (enemyAircrafts.size() < enemyMaxNumber) {
                enemyAircrafts.add(new MobEnemyFactory().createAircraft(mobHp,mobsX,mobsY,mobsNum));
            }
        }
        else if(timeCountAndNewCycleJudge("elite"))
        {
            // 新敌机产生
            if (enemyAircrafts.size() < enemyMaxNumber) {
                enemyAircrafts.add(new EliteEnemyFactory().createAircraft(elitesHp,elitesX,elitesY,elitesNum));
            }
        }

    }

    @Override
    protected void initAirs(){
        //  获取英雄级
        heroAircraft = HeroAircraft.getHeroAircraft(
                Main.WINDOW_WIDTH / 2,Main.WINDOW_HEIGHT - ImageManager.HERO_IMAGE.getHeight(),
                0, 0, heroHp,heroPower,-1,heroNum);
        //  启动英雄机鼠标监听
        new HeroController(this, heroAircraft);
    }


    @Override
    protected void initArgs(){
        //   最大敌机数量
        enemyMaxNumber = 5;
        heroHp = 500;
        heroPower = 30;
        heroNum = 1;

        elitesHp = 50;
        elitesX = 0;
        elitesY = 5;
        elitesNum = 1;

        mobHp = 30;
        mobsX = 0;
        mobsY = 10;
        mobsNum = 0;
        bossLimit = 0;

        BgPath = "src/images/bg.jpg";
        archivalPath = "easyUser.dat";
        try {
            ImageManager.BACKGROUND_IMAGE = ImageIO.read(new FileInputStream(Game.getBgPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        shootDuration = new LinkedHashMap<String,Double>(){
            {
                put("hero",500.0);
                put("enemy",900.0);
            }
        };
        airDuration = new LinkedHashMap<String,Double>(){
            {
                put("normal",600.0);
                put("elite",3000.0);
            }
        };
    }
}
