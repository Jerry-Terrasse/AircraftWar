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
public class BaseGame extends JPanel implements EnemyVanishListener {
    protected int difficulty = -1;
    protected int EnemyHpFactor = 1;
    protected double EnemySpeedFactor = 1;
    protected int BossScoreThresh = Integer.MAX_VALUE;
    protected double EliteEnemyProbability = 0.1;
    protected double BossEnemyProbability = 0.02;
    protected int eliteLastPromotionTime = 0;


    protected int backGroundTop = 0;
    protected BufferedImage backGroundImage = ImageManager.BACKGROUND_IMAGE_EASY;

    /**
     * Scheduled 线程池，用于任务调度
     */
    protected final ScheduledExecutorService executorService;

    /**
     * 时间间隔(ms)，控制刷新频率
     */
    protected int timeInterval = 40;

    protected final HeroAircraft heroAircraft;
    protected final List<AbstractEnemy> enemyAircrafts;
    protected final List<BaseBullet> heroBullets;
    protected final List<BaseBullet> enemyBullets;

    protected final List<BaseSupply> supplies;
    protected final List<BaseSupply> suppliesToBeAdd;

    protected EnemyFactory mobEnemyFactory, eliteEnemyFactory, bossEnemyFactory;

    protected final WorldHandle world;

    protected RecordDao recordDao;

    /**
     * 屏幕中出现的敌机最大数量
     */
    protected int enemyMaxNumber = 3;

    /**
     * 当前得分
     */
    protected int score = 0;
    /**
     * 当前时刻
     */
    protected int time = 0;

    /**
     * 周期（ms)
     * 指示子弹的发射、敌机的产生频率
     */
    protected int cycleDuration = 300;
    protected int cycleTime = 0;

    /**
     * 游戏结束标志
     */
    protected boolean gameOverFlag = false;
    protected boolean withMusic = false;

    public BaseGame() {
        heroAircraft = HeroAircraft.getInstance();

        enemyAircrafts = new LinkedList<>();
        heroBullets = new LinkedList<>();
        enemyBullets = new LinkedList<>();
        supplies = new LinkedList<>();
        suppliesToBeAdd = new LinkedList<>();

        mobEnemyFactory = new MobEnemyFactory(EnemyHpFactor, EnemySpeedFactor);
        eliteEnemyFactory = new EliteEnemyFactory(EnemyHpFactor, EnemySpeedFactor);
        bossEnemyFactory = new BossEnemyFactory(EnemyHpFactor, EnemySpeedFactor);

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

    protected void BossPromote() {}
    protected void ElitePromote() {}

    /**
     * 游戏启动入口，执行游戏逻辑
     */
    public void action() {
        if(withMusic) {
            bgmThread.start();
            bulletMusicThread.start();
        }

        // 定时任务：绘制、对象产生、碰撞判定、击毁及结束判定
        Runnable task = () -> {
            try { // Capture Exceptions and Errors

                time += timeInterval;

                ElitePromote();


                // 周期性执行（控制频率）
                if (timeCountAndNewCycleJudge()) {
//                    System.out.println(time);
                    // 新敌机产生
                    if (enemyAircrafts.size() < enemyMaxNumber) {
                        AbstractEnemy newEnemy;
                        double enemyX = Math.random() * (Main.WINDOW_WIDTH - ImageManager.MOB_ENEMY_IMAGE.getWidth());
                        double enemyY = Math.random() * Main.WINDOW_HEIGHT * 0.05;
                        double enemyVx = 0;
                        double enemyVy = 5;

                        // 若满足Boss出现条件，则产生Boss
                        if(score > this.BossScoreThresh && BossEnemy.getCount() == 0 && Math.random() < this.BossEnemyProbability) {
                            enemyY += 5;
                            enemyVx = (Math.random() < 0.5 ? 1 : -1) * 1;
                            newEnemy = bossEnemyFactory.createAircraft(enemyX, enemyY, enemyVx, 0);

                            BossPromote();

                            // play boss bgm
                            if(withMusic) {
                                bgmThread.setPaused(true);
                                bossBgmThread = new MusicThread(MusicManager.getMusicPath("bgm_boss"), true);
                                bossBgmThread.start();
                            }
                        } else if (Math.random() < this.EliteEnemyProbability) {
//                            System.out.println(Math.random());
//                            System.out.println("111" + this.EliteEnemyProbability);
                            newEnemy = eliteEnemyFactory.createAircraft(enemyX, enemyY, enemyVx, enemyVy);
                        } else {
//                            System.out.println("MobMob");
                            newEnemy = mobEnemyFactory.createAircraft(enemyX, enemyY, enemyVx, enemyVy);
                        }
                        newEnemy.addEnemyVanishListener(this);
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

    protected void gameOver() {
        gameOverFlag = true;
        System.out.println("Game Over!");

        if(withMusic) {
            MusicThread musicThread = new MusicThread(MusicManager.getMusicPath("game_over"), false);
            musicThread.start();

            try {
                bulletMusicThread.setStopped();
                bulletMusicThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {
                bgmThread.setStopped();
                bgmThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if(bossBgmThread != null) {
                try {
                    bossBgmThread.setStopped();
                    bossBgmThread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        // get username
        String name = JOptionPane.showInputDialog(String.format("游戏结束，你的得分为%d\n请输入你的名字记录得分：", score));
        recordDao.doAdd(new Record(name, score, -1));

        List<Record> recordList = recordDao.getAll();
        for(Record record: recordList) {
            System.out.printf("Id: %d\tName: %s\tScore: %d%n", record.getRecord_id(), record.getName(), record.getScore());
        }

        RankTable rankTable = new RankTable(recordDao, this.difficulty);
        Main.cardPanel.add(rankTable.getMainPanel(), "rankTable");
        Main.cardLayout.last(Main.cardPanel);

//        System.exit(0);
    }

    //***********************
    //      Action 各部分
    //***********************

    protected boolean timeCountAndNewCycleJudge() {
        cycleTime += timeInterval;
        if (cycleTime >= cycleDuration && cycleTime - timeInterval < cycleTime) {
            // 跨越到新的周期
            cycleTime %= cycleDuration;
            return true;
        } else {
            return false;
        }
    }

    protected void shootAction() {
//        MusicThread musicThread = new MusicThread(MusicManager.getMusicPath("bullet"), false);
//        musicThread.start();
        for (AbstractEnemy enemy: enemyAircrafts) {
            enemyBullets.addAll(enemy.shoot());
        }

        // 英雄射击
        heroBullets.addAll(heroAircraft.shoot());
    }


    protected void bulletsMoveAction() {
        for (BaseBullet bullet : heroBullets) {
            bullet.forward();
        }
        for (BaseBullet bullet : enemyBullets) {
            bullet.forward();
        }
    }

    protected void suppliesMoveAction() {
        for (BaseSupply supply : supplies) {
            supply.forward();
        }
    }

    protected void aircraftsMoveAction() {
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
    protected void crashCheckAction() {
        // 敌机子弹攻击英雄
        for (BaseBullet bullet : enemyBullets) {
            if(bullet.notValid() || heroAircraft.notValid()) {
                continue;
            }
            if(heroAircraft.crash(bullet)) { // crashed
                playBulletHitMusic();
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
                    if(withMusic) {
                        playBulletHitMusic();
                    }
                    // 敌机撞击到英雄机子弹
                    // 敌机损失一定生命值
                    enemyAircraft.decreaseHp(bullet.getPower());
                    bullet.vanish();
//                    if (enemyAircraft.notValid()) {
//                        // 获得分数，产生道具补给
//                        score += 10;
//                        supplies.addAll(enemyAircraft.produceSupply());
//
//                        if(enemyAircraft instanceof BossEnemy) {
//                            if(withMusic) {
//                                try {
//                                    bossBgmThread.setStopped();
//                                    bossBgmThread.join();
//                                } catch (InterruptedException e) {
//                                    e.printStackTrace();
//                                }
//                                bossBgmThread = null;
//                                bgmThread.setPaused(false);
//                            }
//                        }
//                    }
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
    protected void postProcessAction() {
        enemyBullets.removeIf(AbstractFlyingObject::notValid);
        heroBullets.removeIf(AbstractFlyingObject::notValid);
        enemyAircrafts.removeIf(AbstractFlyingObject::notValid);
        supplies.removeIf(AbstractFlyingObject::notValid);
        supplies.addAll(suppliesToBeAdd);
        suppliesToBeAdd.clear();
    }

    public void onEnemyVanish(int increaseScore, boolean isBoss, List<BaseSupply> dropSupplies) {
        score += increaseScore;
        suppliesToBeAdd.addAll(dropSupplies);

        if(isBoss) {
            if(withMusic) {
                try {
                    bossBgmThread.setStopped();
                    bossBgmThread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                bossBgmThread = null;
                bgmThread.setPaused(false);
            }
        }
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
        g.drawImage(this.backGroundImage, 0, this.backGroundTop - Main.WINDOW_HEIGHT, null);
        g.drawImage(this.backGroundImage, 0, this.backGroundTop, null);
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

    protected void paintImageWithPositionRevised(Graphics g, List<? extends AbstractFlyingObject> objects) {
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

    protected void paintScoreAndLife(Graphics g) {
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

    public void setWithMusic(boolean withMusic) {
        this.withMusic = withMusic;
        world.setWithMusic(withMusic);
    }
    public boolean getWithMusic() {
        return this.withMusic;
    }
    void playBulletHitMusic() {
        MusicThread musicThread = new MusicThread(MusicManager.getMusicPath("bullet_hit"), false);
        musicThread.start();
    }
    MusicThread bgmThread = new MusicThread(MusicManager.getMusicPath("bgm"), true);
    MusicThread bulletMusicThread = new MusicThread(MusicManager.getMusicPath("bullet"), true);
    MusicThread bossBgmThread = null;
}
