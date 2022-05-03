package edu.hitsz.gameTemplate;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.application.HeroController;
import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;
import edu.hitsz.craftFactory.BossEnemyFactory;
import edu.hitsz.craftFactory.EliteEnemyFactory;
import edu.hitsz.craftFactory.MobEnemyFactory;
import edu.hitsz.gameTemplate.Game;

import javax.imageio.ImageIO;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedHashMap;

import static java.lang.Math.max;
import static java.lang.Math.min;


public class MediumGame extends Game {

    @Override
    protected void levelUp() {
        if(totalTime%levelTime==0 || totalTime>levelTime){
            System.out.println("难度升高");
            System.out.println("界面内最大敌机数量+1上升至"+enemyMaxNumber);
            System.out.println("普通敌机出现周期缩短10% ; 纵向速度+1,上限为15");
            System.out.println("精英飞机血量上限+10上升至" +elitesHp + " ; 精英敌机出现周期缩短10%" +  " ; 精英机发射子弹速度加快5%" + " ; 精英机子弹数量+1,上限为3" + "横向速度+1,上限为3");
            System.out.println("英雄伤害+2 ; 英雄机发射速度加快8%" );
            System.out.println("Boss机出现的得分阈值降低10，下限为200");
            System.out.println();
            totalTime /= levelTime;
            //  飞机数量
            enemyMaxNumber += 1;

            //  mob出现周期
            airDuration.put("normal",airDuration.get("normal")*0.9);
            mobsY = min(mobHp+1,15);

            //  精英飞机
            elitesHp += 10;
            elitesNum = min(3,elitesNum+1);
            elitesX = min(elitesX+1,3);
            airDuration.put("elite",airDuration.get("elite")*0.9);
            shootDuration.put("enemy",shootDuration.get("enemy")*0.95);

            //  英雄机
            heroAircraft.addPower(2);
            shootDuration.put("hero",shootDuration.get("hero")*0.92);
            if(heroAircraft.getNum()<2){
                heroAircraft.addNum(1);
            }
            //  Boss机阈值
            bossThreshold = max(bossThreshold-10,200);
        }
    }

    @Override
    protected void loadAirCrafts() {
        // 周期性执行（控制频率）
        if (timeCountAndNewCycleJudge("normal")) {
            // 新敌机产生
            if (enemyAircrafts.size() < enemyMaxNumber) {
                enemyAircrafts.add(new MobEnemyFactory().createAircraft(mobHp,mobsX,mobsY,bosssNum));
            }
        }
        else if(timeCountAndNewCycleJudge("elite"))
        {
            // 新敌机产生
            if (enemyAircrafts.size() < enemyMaxNumber) {
                enemyAircrafts.add(new EliteEnemyFactory().createAircraft(elitesHp,elitesX,elitesY,elitesNum));
            }
        }

        if(isBoss()){
            if(enemyAircrafts.size()<enemyMaxNumber){
                enemyAircrafts.add(new BossEnemyFactory().createAircraft(bossHp,bosssX,bosssY,bosssNum));
            }
        }
    }

    @Override
    protected void initArgs() {
        enemyMaxNumber = 10;
        heroHp = 3000;
        heroPower = 50;
        heroNum = 1;

        elitesHp = 60;
        elitesX = 5;
        elitesY = 5;
        elitesNum = 1;

        mobHp = 40;
        mobsX = 0;
        mobsY = 10;
        mobsNum = 0;

        bossThreshold = 300;
        bossHp = 300;
        bosssX = 2;
        bosssY = 0;
        bosssNum = 3;
        bossLimit = 1;

        levelTime = 5000;
        BgPath = "src/images/bg2.jpg";
        archivalPath = "mediumUser.dat";
        try {
            ImageManager.BACKGROUND_IMAGE = ImageIO.read(new FileInputStream(Game.getBgPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        shootDuration = new LinkedHashMap<String,Double>(){
            {
                put("hero",600.0);
                put("enemy",700.0);
            }
        };
        airDuration = new LinkedHashMap<String,Double>(){
            {
                put("normal",600.0);
                put("elite",2000.0);
            }
        };
    }


    @Override
    protected void initAirs() {
        //  获取英雄级
        heroAircraft = HeroAircraft.getHeroAircraft(
                Main.WINDOW_WIDTH / 2,Main.WINDOW_HEIGHT - ImageManager.HERO_IMAGE.getHeight(),
                0, 1, heroHp,heroPower,-1,heroNum);
        //  启动英雄机鼠标监听
        new HeroController(this, heroAircraft);
    }
}
