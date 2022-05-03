package edu.hitsz.gameTemplate;

import edu.hitsz.application.GameUI;
import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;
import edu.hitsz.application.MusicThread;
import edu.hitsz.aircraft.*;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.observer.Observer;
import edu.hitsz.reward.AbstractReward;
import edu.hitsz.reward.BombReward;
import edu.hitsz.rewardFactory.BloodRewardFactory;
import edu.hitsz.rewardFactory.BombRewardFactory;
import edu.hitsz.rewardFactory.BulletRewardFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;

/**
 * 游戏主面板，游戏启动
 * @author hitsz
 */
public abstract class Game extends JPanel {

    /**
     * Scheduled 线程池，用于任务调度
     */

    private final ScheduledExecutorService executorService;
    protected HeroAircraft heroAircraft;
    protected final ArrayList<AbstractAircraft> enemyAircrafts;
    private int backGroundTop = 0;
    protected final int ShootInterval = 40;                 //  发射计时单位
    protected final int airInterval = 40;                  //  飞行计时单位
    protected final ArrayList<BaseBullet> heroBullets;
    protected final ArrayList<BaseBullet> enemyBullets;
    protected final ArrayList<AbstractReward> leftRewards;
    protected static int score = 0;
    protected int cntBoss = 0;
    protected int recordOfBoss = 0;
    protected int totalTime;            //  总计时间 用于升级
    //   子弹计时器
    protected Map<String,Double> shootCycle = new LinkedHashMap<String,Double>(){
        {
            put("hero",0.0);
            put("enemy",0.0);
        }
    };

    protected Map<String,Double> airCycle = new LinkedHashMap<String,Double>(){
        {
            put("normal",0.0);
            put("elite",0.0);
        }
    };

    //  在Game子类中需要设置的
    protected Map<String,Double> shootDuration;     //  设计周期
    protected Map<String,Double> airDuration;       //  飞行周期
    protected static String BgPath;                 //  背景路径
    protected String archivalPath;                  //  存档路径
    protected int enemyMaxNumber;                   //  最大敌机数量
    protected int levelTime;                        //  升级周期
    //  飞机的参数，在Game的子类里需要设置他们。
    protected int heroHp;
    protected int heroPower;
    protected int heroNum;
    protected int elitesHp;
    protected int elitesX;
    protected int elitesY;
    protected int elitesNum;
    protected int bossThreshold;                    //  Boss机出现间隔
    protected int bossHp;
    protected int bosssX;
    protected int bosssY;
    protected int bosssNum;
    protected int mobHp;
    protected int mobsX;
    protected int mobsY;
    protected int mobsNum;
    protected int bossLimit;

    public Game() {
        enemyAircrafts = new ArrayList<>();
        heroBullets = new ArrayList<>();
        enemyBullets = new ArrayList<>();
        leftRewards = new ArrayList<>();
        //Scheduled 线程池，用于定时任务调度
        executorService = new ScheduledThreadPoolExecutor(1);
        initArgs();
        initAirs();
    }

    /**
     * 模板方法
     * 游戏启动入口，执行游戏逻辑
     * Game是一个线程啊。
     * executorService.scheduleWithFixedDelay(task);
     * 将Game类中的task任务放到cpu上去跑
     * 这样用Runnable接口 和 继承Thread重写run函数是一样的
     * 都是把本类中写的一些任务放到cpu上去跑
     */
    public void action() {

        // 定时任务：绘制、对象产生、碰撞判定、击毁及结束判定
        Runnable task = () -> {

            //  加载敌机
            loadAirCrafts();

            //  加载子弹
            loadBullets();

            //  子弹移动
            bulletsMoveAction();

            //  礼物移动
            rewardMoveAction();

            //  飞机移动
            aircraftsMoveAction();

            //  撞击检测
            crashCheckAction();

            //  后处理
            postProcessAction();

            //  每个时刻重绘界面
            repaint();

            //  游戏结束检查
            isEnd();

            //  游戏升级
            levelUp();
        };

        /**
         * 以固定延迟时间进行执行
         * 本次任务执行完成后，需要延迟设定的延迟时间，才会执行新的任务
         */
        executorService.scheduleWithFixedDelay(task, airInterval, airInterval, TimeUnit.MILLISECONDS);

    }



    //***********************
    //      Action 各部分
    //***********************

    //  升级 延迟到子类实现
    protected abstract void levelUp();

    //  生产敌机  延迟到子类实现
    protected abstract void loadAirCrafts();

    //  设置参数 延迟到子类实现
    protected abstract void initArgs();

    //  初始化英雄机 延迟到子类实现
    protected abstract void initAirs();

    public void isEnd()
    {
        totalTime += airInterval;
        if (heroAircraft.getHp() <= 0) {
            // 游戏结束 bgm停止；结束音乐开始
            executorService.shutdown();
            if (MusicThread.getIsMusic()) {
                MusicThread.setIsEnd(true);
                new MusicThread("src/videos/game_over.wav", false, true).start();
            }
            GameUI.rankingUI();
            System.out.println("Game Over!");
        }
    }


    public void loadBullets()
    {
        for (String key : shootCycle.keySet()) {
            shootCycle.put(key,shootCycle.get(key)+ShootInterval);
            Double st = shootCycle.get(key);
            Double dur = shootDuration.get(key);
            if(st>=dur){
                if("enemy".equals(key)) {
                    enemyShootAction();
                }
                else{
                    heroShootAction();
                }
                shootCycle.put(key,st%dur);
            }
        }
    }

    private void heroShootAction() {
        // 英雄射击
        heroBullets.addAll(heroAircraft.shoot());
    }


    private void enemyShootAction() {
        // TODO 敌机射击
        for (AbstractAircraft airEnemy : enemyAircrafts) {
            enemyBullets.addAll(airEnemy.shoot());
        }
    }


    //  不变的代码 计算飞机air是否应该产生
    protected boolean timeCountAndNewCycleJudge(String air) {
        Double t = airCycle.get(air);
        airCycle.put(air,t+airInterval);
        Double duration = airDuration.get(air);
        if (airCycle.get(air) >= duration && airCycle.get(air) - airInterval < airCycle.get(air)) {
            // 跨越到新的周期
            airCycle.put(air,airCycle.get(air)%duration);
            return true;
        } else {
            return false;
        }
    }


    private void bulletsMoveAction() {
        for (BaseBullet bullet : heroBullets) {
            bullet.forward();
        }
        for (BaseBullet bullet : enemyBullets) {
            bullet.forward();
        }
    }

    private void rewardMoveAction(){
        for(AbstractReward reward : leftRewards)
        {
            reward.forward();
        }
    }

    private void aircraftsMoveAction() {
        for (AbstractAircraft enemyAircraft : enemyAircrafts) {
            enemyAircraft.forward();
        }
    }


    /**
     * 碰撞检测：
     * 1. 敌机攻击英雄
     * 2. 英雄攻击/撞击敌机
     * 3. 英雄获得补给
     */
    private void crashCheckAction() {
        // TODO 敌机子弹攻击英雄
        for (BaseBullet bullet : enemyBullets) {
            if (bullet.notValid()) {
                continue;
            }
            if (heroAircraft.crash(bullet)) {
                // 英雄机撞击到敌军子弹
                // 英雄机损失一定生命值 子弹消失、英雄小于0则死亡
                heroAircraft.decreaseHp(bullet.getPower());
                bullet.vanish();
            }
        }
        // 英雄子弹攻击敌机
        for (BaseBullet bullet : heroBullets) {
            if (bullet.notValid()) {
                continue;
            }
            for (AbstractAircraft enemyAircraft : enemyAircrafts) {
                if (enemyAircraft.notValid()) {
                    // 已被其他子弹击毁的敌机，不再检测
                    // 避免多个子弹重复击毁同一敌机的判定
                    continue;
                }
                if (enemyAircraft.crash(bullet)) {
                    // 敌机撞击到英雄机子弹
                    // 背景音乐
                    // 敌机损失一定生命值
                    if(MusicThread.getIsMusic()){
                        new MusicThread("src/videos/bullet_hit.wav",false,false).start();
                    }
                    enemyAircraft.decreaseHp(bullet.getPower());    //  敌机损失一定生命值 有可能isVaild = false
                    bullet.vanish();
                    if (enemyAircraft.notValid()) {
                        // TODO 获得分数，产生道具补给
                        // 将补给放入left_rewards容器
                        int random = new Random().nextInt(4);
                        if((enemyAircraft instanceof EliteEnemy && random<3) || (enemyAircraft instanceof BossEnemy))
                        {
                            if(enemyAircraft instanceof BossEnemy)
                            {
                                --cntBoss;
                            }
                            //  随机产生三种道具之一：血包 弹药包、炸药包
                            switch(random)
                            {
                                case 0:
                                {
                                    leftRewards.add(new BloodRewardFactory().
                                            createReward(enemyAircraft));
                                    break;
                                }
                                case 1:
                                {
                                    leftRewards.add(new BombRewardFactory().
                                            createReward(enemyAircraft));
                                    break;
                                }
                                case 2:
                                {
                                    leftRewards.add(new BulletRewardFactory().
                                            createReward(enemyAircraft));
                                    break;
                                }
                                default:
                                {
                                    break;
                                }
                            }
                        }
                        score += enemyAircraft.getVal();
                    }
                }
                // 英雄机 与 敌机 相撞，均损毁
                if (enemyAircraft.crash(heroAircraft) || heroAircraft.crash(enemyAircraft)) {
                    enemyAircraft.vanish();
                    heroAircraft.decreaseHp(Integer.MAX_VALUE);
                }
            }
        }


        // Todo: 我方获得道具，道具生效
        for(AbstractReward reward : leftRewards)
        {
            if(reward.notValid()) {
                continue;
            }
            if (heroAircraft.crash(reward)) {
                //  本方吃到奖励
                if(reward instanceof BombReward) {
                    ArrayList<Observer> o = new ArrayList<Observer>();
                    for(AbstractAircraft a:enemyAircrafts){
                        o.add((Observer) a);
                    }
                    for(BaseBullet e:enemyBullets){
                        o.add((Observer) e);
                    }
                    ((BombReward)reward).addObs(o);
                }

                reward.takeEffect(heroAircraft);
                reward.vanish();
            }
        }
    }

    /**
     * 后处理：
     * 1. 删除无效的子弹
     * 2. 删除无效的敌机
     * 3. 检查英雄机生存
     * 4. 检查奖励是否被已经失效（被吃掉或者出界）
     * <p>
     * 无效的原因可能是撞击或者飞出边界
     */
    private void postProcessAction() {
        enemyBullets.removeIf(AbstractFlyingObject::notValid);
        heroBullets.removeIf(AbstractFlyingObject::notValid);
        enemyAircrafts.removeIf(AbstractFlyingObject::notValid);
        leftRewards.removeIf(AbstractFlyingObject::notValid);
    }


    //***********************
    //      Paint 各部分
    //***********************

    /**
     * 重写paint方法
     * 通过重复调用paint方法，实现游戏动画
     *
     * @param  g
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);

        // 绘制背景,图片滚动
        g.drawImage(ImageManager.BACKGROUND_IMAGE, 0, this.backGroundTop - Main.WINDOW_HEIGHT, null);
        g.drawImage(ImageManager.BACKGROUND_IMAGE, 0, this.backGroundTop, null);
        this.backGroundTop += 1;
        if (this.backGroundTop == Main.WINDOW_HEIGHT) {
            this.backGroundTop = 0;
        }

        // 先绘制子弹，后绘制飞机
        // 这样子弹显示在飞机的下层
        paintImageWithPositionRevised(g, enemyBullets);
        paintImageWithPositionRevised(g, heroBullets);

        paintImageWithPositionRevised(g, enemyAircrafts);
        paintImageWithPositionRevised(g,leftRewards);
        g.drawImage(ImageManager.HERO_IMAGE, heroAircraft.getLocationX() - ImageManager.HERO_IMAGE.getWidth() / 2,
                heroAircraft.getLocationY() - ImageManager.HERO_IMAGE.getHeight() / 2, null);

        //绘制得分和生命值
        paintScoreAndLife(g);

    }

    private void paintImageWithPositionRevised(Graphics g, List<? extends AbstractFlyingObject> objects) {
        if (objects.size() == 0) {
            return;
        }

        for (AbstractFlyingObject object : objects) {
            BufferedImage image = object.getImage();
            assert image != null : objects.getClass().getName() + " has no image! ";
            g.drawImage(image, object.getLocationX() - image.getWidth() / 2,
                    object.getLocationY() - image.getHeight() / 2, null);
        }
    }

    private void paintScoreAndLife(Graphics g) {
        int x = 10;
        int y = 25;
        g.setColor(new Color(16711680));
        g.setFont(new Font("SansSerif", Font.BOLD, 22));
        g.drawString("SCORE:" + score, x, y);
        y = y + 20;
        g.drawString("LIFE:" + this.heroAircraft.getHp(), x, y);
    }

    public static void addScore(int x){
        score += x;
    }

    public int getScore() {
        return score;
    }

    protected boolean isBoss()
    {
        //  同一时刻只有一个Boss机
        if(cntBoss>=bossLimit) {
            return false;
        }
        //  是否又该出现Boss机
        int t = score / bossThreshold;
        if(t<=recordOfBoss) {
            return false;
        }
        recordOfBoss = t;           //  更新
        ++cntBoss;
        return true;
    }
    public String getArchivalPath() {
        return archivalPath;
    }
    public static String getBgPath() {
        return BgPath;
    }
}
