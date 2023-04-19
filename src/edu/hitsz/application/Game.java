package edu.hitsz.application;

import edu.hitsz.aircraft.*;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.rank.Record;
import edu.hitsz.rank.RecordDao;
import edu.hitsz.rank.RecordDaoImpl;
import edu.hitsz.supply.BaseSupply;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 游戏主面板，游戏启动
 *
 * @author hitsz
 */
public class Game extends JPanel {

    private int backGroundTop = 0;

    /**
     * Scheduled 线程池，用于任务调度
     */
    private final ScheduledExecutorService executorService;

    /**
     * 时间间隔(ms)，控制刷新频率
     */
    private int timeInterval = 40;

    private final HeroAircraft heroAircraft;
    private final List<AbstractEnemy> enemyAircrafts;
    private final List<BaseBullet> heroBullets;
    private final List<BaseBullet> enemyBullets;

    private final List<BaseSupply> supplies;

    private final EnemyFactory mobEnemyFactory, eliteEnemyFactory;

    private final WorldHandle world;

    private final RecordDao recordDao;

    /**
     * 屏幕中出现的敌机最大数量
     */
    private int enemyMaxNumber = 5;

    /**
     * 当前得分
     */
    private int score = 0;
    /**
     * 当前时刻
     */
    private int time = 0;

    /**
     * 周期（ms)
     * 指示子弹的发射、敌机的产生频率
     */
    private int cycleDuration = 300;
    private int cycleTime = 0;

    /**
     * 游戏结束标志
     */
    private boolean gameOverFlag = false;
    private boolean withMusic = false;

    public Game() {
        heroAircraft = HeroAircraft.getInstance();

        enemyAircrafts = new LinkedList<>();
        heroBullets = new LinkedList<>();
        enemyBullets = new LinkedList<>();
        supplies = new LinkedList<>();

        mobEnemyFactory = MobEnemyFactory.getInstance();
        eliteEnemyFactory = EliteEnemyFactory.getInstance();

        world = new WorldHandle(heroAircraft, enemyAircrafts, heroBullets, enemyBullets, supplies);

        recordDao = new RecordDaoImpl("rank_data.dat");

        /**
         * Scheduled 线程池，用于定时任务调度
         * 关于alibaba code guide：可命名的 ThreadFactory 一般需要第三方包
         * apache 第三方库： org.apache.commons.lang3.concurrent.BasicThreadFactory
         */
        this.executorService = new ScheduledThreadPoolExecutor(1,
                new BasicThreadFactory.Builder().namingPattern("game-action-%d").daemon(true).build());

        //启动英雄机鼠标监听
        new HeroController(this, heroAircraft);

    }

    /**
     * 游戏启动入口，执行游戏逻辑
     */
    public void action() {

        // 定时任务：绘制、对象产生、碰撞判定、击毁及结束判定
        Runnable task = () -> {
            try { // Capture Exceptions and Errors

                time += timeInterval;

                // 周期性执行（控制频率）
                if (timeCountAndNewCycleJudge()) {
                    System.out.println(time);
                    // 新敌机产生
                    if (enemyAircrafts.size() < enemyMaxNumber) {
                        AbstractEnemy newEnemy;
                        double enemyX = Math.random() * (Main.WINDOW_WIDTH - ImageManager.MOB_ENEMY_IMAGE.getWidth());
                        double enemyY = Math.random() * Main.WINDOW_HEIGHT * 0.05;
                        double enemyVx = 0;
                        double enemyVy = 5;

                        // 若满足Boss出现条件，则产生Boss
                        if(score > BossEnemy.getScoreThreshold() && BossEnemy.getCount() == 0 && Math.random() < BossEnemy.getProbability()) {
                            enemyY += 5;
                            enemyVx = (Math.random() < 0.5 ? 1 : -1) * 1;
                            newEnemy = BossEnemyFactory.getInstance().createAircraft(enemyX, enemyY, enemyVx, 0);
                        } else if (Math.random() < EliteEnemy.getProbability()) {
                            newEnemy = eliteEnemyFactory.createAircraft(enemyX, enemyY, enemyVx, enemyVy);
                        } else {
                            newEnemy = mobEnemyFactory.createAircraft(enemyX, enemyY, enemyVx, enemyVy);
                        }
                        enemyAircrafts.add(newEnemy);
                    }
                    // 飞机射出子弹
                    shootAction();
                }

                // 子弹移动
                bulletsMoveAction();

                // 补给移动
                suppliesMoveAction();

                // 飞机移动
                aircraftsMoveAction();

                // 撞击检测
                crashCheckAction();

                // 后处理
                postProcessAction();

                //每个时刻重绘界面
                repaint();

                // 游戏结束检查英雄机是否存活
                if (heroAircraft.getHp() <= 0) {
                    // 游戏结束
                    executorService.shutdown();
                    gameOver();
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }

        };

        /**
         * 以固定延迟时间进行执行
         * 本次任务执行完成后，需要延迟设定的延迟时间，才会执行新的任务
         */
        executorService.scheduleWithFixedDelay(task, timeInterval, timeInterval, TimeUnit.MILLISECONDS);

    }

    private void gameOver() {
        gameOverFlag = true;
        System.out.println("Game Over!");

        recordDao.doAdd(new Record("user", score, -1));

        List<Record> recordList = recordDao.getAll();
        for(Record record: recordList) {
            System.out.println(String.format("Id: %d\tName: %s\tScore: %d", record.getRecord_id(), record.getName(), record.getScore()));
        }

        RankTable rankTable = new RankTable(recordDao);
        Main.cardPanel.add(rankTable.getMainPanel(), "rankTable");
        Main.cardLayout.last(Main.cardPanel);

//        System.exit(0);
    }

    //***********************
    //      Action 各部分
    //***********************

    private boolean timeCountAndNewCycleJudge() {
        cycleTime += timeInterval;
        if (cycleTime >= cycleDuration && cycleTime - timeInterval < cycleTime) {
            // 跨越到新的周期
            cycleTime %= cycleDuration;
            return true;
        } else {
            return false;
        }
    }

    private void shootAction() {
        for (AbstractEnemy enemy: enemyAircrafts) {
            enemyBullets.addAll(enemy.shoot());
        }

        // 英雄射击
        heroBullets.addAll(heroAircraft.shoot());
    }


    private void bulletsMoveAction() {
        for (BaseBullet bullet : heroBullets) {
            bullet.forward();
        }
        for (BaseBullet bullet : enemyBullets) {
            bullet.forward();
        }
    }

    private void suppliesMoveAction() {
        for (BaseSupply supply : supplies) {
            supply.forward();
        }
    }

    private void aircraftsMoveAction() {
        for (AbstractEnemy enemyAircraft : enemyAircrafts) {
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
        // 敌机子弹攻击英雄
        for (BaseBullet bullet : enemyBullets) {
            if(bullet.notValid() || heroAircraft.notValid()) {
                continue;
            }
            if(heroAircraft.crash(bullet)) { // crashed
                heroAircraft.decreaseHp(bullet.getPower());
                bullet.vanish();
            }
        }

        // 英雄子弹攻击敌机
        for (BaseBullet bullet : heroBullets) {
            if (bullet.notValid()) {
                continue;
            }
            for (AbstractEnemy enemyAircraft : enemyAircrafts) {
                if (enemyAircraft.notValid()) {
                    // 已被其他子弹击毁的敌机，不再检测
                    // 避免多个子弹重复击毁同一敌机的判定
                    continue;
                }
                if (enemyAircraft.crash(bullet)) {
                    // 敌机撞击到英雄机子弹
                    // 敌机损失一定生命值
                    enemyAircraft.decreaseHp(bullet.getPower());
                    bullet.vanish();
                    if (enemyAircraft.notValid()) {
                        // 获得分数，产生道具补给
                        score += 10;
                        supplies.addAll(enemyAircraft.produceSupply());
                    }
                }
            }
        }

        // 英雄撞击敌机
        for (AbstractEnemy enemy: enemyAircrafts) {
            if(enemy.notValid() || heroAircraft.notValid()) {
                continue;
            }
            // 英雄机 与 敌机 相撞，均损毁
            if (enemy.crash(heroAircraft) || heroAircraft.crash(enemy)) {
                enemy.vanish();
                heroAircraft.decreaseHp(Integer.MAX_VALUE);
            }
        }

        // 我方获得道具，道具生效
        for (BaseSupply supply : supplies) {
            if(supply.notValid()) {
                continue;
            }
            if(heroAircraft.crash(supply)) { // take effect
                supply.takeEffect(world);
                supply.vanish();
            }
        }
    }

    /**
     * 后处理：
     * 1. 删除无效的子弹
     * 2. 删除无效的敌机
     * <p>
     * 无效的原因可能是撞击或者飞出边界
     */
    private void postProcessAction() {
        enemyBullets.removeIf(AbstractFlyingObject::notValid);
        heroBullets.removeIf(AbstractFlyingObject::notValid);
        enemyAircrafts.removeIf(AbstractFlyingObject::notValid);
        supplies.removeIf(AbstractFlyingObject::notValid);
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

        // 先绘制子弹和补给，后绘制飞机
        // 这样子弹显示在飞机的下层
        paintImageWithPositionRevised(g, enemyBullets);
        paintImageWithPositionRevised(g, heroBullets);

        paintImageWithPositionRevised(g, supplies);

        paintImageWithPositionRevised(g, enemyAircrafts);

        g.drawImage(ImageManager.HERO_IMAGE, (int)(heroAircraft.getLocationX() - ImageManager.HERO_IMAGE.getWidth() / 2),
                (int)(heroAircraft.getLocationY() - ImageManager.HERO_IMAGE.getHeight() / 2), null);

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
            g.drawImage(image, (int)(object.getLocationX() - image.getWidth() / 2),
                    (int)(object.getLocationY() - image.getHeight() / 2), null);
        }
    }

    private void paintScoreAndLife(Graphics g) {
        int x = 10;
        int y = 25;
        g.setColor(new Color(16711680));
        g.setFont(new Font("SansSerif", Font.BOLD, 22));
        g.drawString("SCORE:" + this.score, x, y);
        y = y + 20;
        g.drawString("LIFE:" + this.heroAircraft.getHp(), x, y);
    }

    //***********************
    //      Music 各部分
    //***********************

    void setWithMusic(boolean withMusic) {
        this.withMusic = withMusic;
    }
}
