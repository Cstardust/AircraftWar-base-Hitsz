package edu.hitsz.gameTemplate;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.application.HeroController;
import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;
import edu.hitsz.craftFactory.BossEnemyFactory;
import edu.hitsz.craftFactory.EliteEnemyFactory;
import edu.hitsz.craftFactory.MobEnemyFactory;

import javax.imageio.ImageIO;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedHashMap;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class HellGame extends Game {
    @Override
    protected void levelUp() {
        if(totalTime%levelTime==0 || totalTime>levelTime){
            System.out.println("难度升高");
            System.out.println("界面内最大敌机数量上升至"+enemyMaxNumber);
            System.out.println("普通飞机血量上限+20上升至" +mobHp + " ; 普通敌机出现周期缩短20%" + "普通机横向速度+1，上限为10 ; 纵向速度+1，上限为12");
            System.out.println("精英飞机血量上限+50上升至" +elitesHp + " ; 精英敌机出现周期缩短30%" +  " ; 精英机发射子弹速度加快3%" + " ; 精英机子弹数量+1,上限为3 ; "+"精英机横向速度+1，上限为5 ; 纵向速度+1，上限为10");
            System.out.println("英雄机血量+50上升至"+heroAircraft.getHp() + " ; 英雄伤害+5" + " ; 英雄子弹+1,上限为3");
            System.out.println("Boss机出现的得分阈值降低10，下限为200 ; 界面允许的Boss机数量+1,上限为2");
            System.out.println();
            totalTime /= levelTime;
            //  最大数量
            enemyMaxNumber += 1;

            //  Boss、普通、精英产生周期
            airDuration.put("normal",airDuration.get("normal")*0.8);
            airDuration.put("elite",airDuration.get("elite")*0.7);
            shootDuration.put("enemy",shootDuration.get("enemy")*0.97);
            shootDuration.put("hero",shootDuration.get("hero")*0.95);
            bossThreshold = max(bossThreshold-10,200);
            bossLimit = min(bossLimit+1,2);

            //  mob生命 速度
            mobHp += 20;
            mobsY = min(mobsY+1,12);
            mobsX = min(mobsX+1,10);

            //   英雄机子弹声明、速度、伤害。补充血量
            heroAircraft.addHp(50);
            heroAircraft.addPower(5);
            if(heroAircraft.getNum()<3){
                heroAircraft.addNum(1);
            }

            //   精英机生命，子弹数量
            elitesNum = min(3,elitesNum+1);
            elitesHp += 50;
            elitesX = min(elitesX+1,5);
            elitesY = min(elitesY+1,10);
        }
    }

    @Override
    protected void loadAirCrafts() {
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

        if(isBoss()){
            //   Boss机生命，子弹数量
            bossHp += 500;
            bosssNum = min(6,bosssNum+1);
            System.out.println("Boss血量上升至"+bossHp + " ; Boss子弹数量上升至"+bosssNum+ ",上限为6");
            System.out.println();
            if(enemyAircrafts.size()<enemyMaxNumber){
                enemyAircrafts.add(new BossEnemyFactory().createAircraft(bossHp,bosssX,bosssY,bosssNum));
            }
        }
    }

    @Override
    protected void initArgs() {
        enemyMaxNumber = 10;
        heroHp = 2000;
        heroPower = 40;
        heroNum = 1;

        elitesHp = 60;
        elitesX = 2;
        elitesY = 5;
        elitesNum = 1;

        mobHp = 40;
        mobsX = 5;
        mobsY = 8;
        mobsNum = 0;

        bossThreshold = 200;
        bossHp = 300;
        bosssX = 2;
        bosssY = 0;
        bosssNum = 3;
        bossLimit = 1;

        levelTime = 8000;   //  升级时间
        BgPath = "src/images/bg5.jpg";
        archivalPath = "difficultUser.dat";
        //  没招了。写这里。防止加载错
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
