package edu.hitsz.aircraft;

import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;
import edu.hitsz.bullet.HeroBulletFactory;
import edu.hitsz.shootStrategy.DisperseStrategy;
import edu.hitsz.shootStrategy.StraightShootStrategy;

/**
 * 英雄飞机，游戏玩家操控
 * @author hitsz
 * 单例模式
 */
public class HeroAircraft extends AbstractAircraft {

    /* 攻击方式 */

    /**
     * 子弹一次发射数量
     */
    private int shootNum = 1;

    /**
     * 子弹射击方向 (向下发射：1，向上发射：-1)
     */
    private final int direction = -1;

    /**
     * @param locationX 英雄机位置x坐标
     * @param locationY 英雄机位置y坐标
     * @param speedX 英雄机射出的子弹的基准速度（英雄机无特定速度）
     * @param speedY 英雄机射出的子弹的基准速度（英雄机无特定速度）
     * @param hp    初始生命值
     */
    private HeroAircraft(double locationX, double locationY, double speedX, double speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp, new StraightShootStrategy(-1, new HeroBulletFactory(20)));
    }

    @Override
    public void forward() {
        // 英雄机由鼠标控制，不通过forward函数移动
    }

    public void increaseShootNum() {
        shootNum += 1;
        this.setShootStrategy(new DisperseStrategy(direction, new HeroBulletFactory(20), shootNum));
    }

    private static HeroAircraft instance = null;
    public static HeroAircraft getInstance() {
        if(instance == null) {
            instance = new HeroAircraft(
                    Main.WINDOW_WIDTH / 2,
                    Main.WINDOW_HEIGHT - ImageManager.HERO_IMAGE.getHeight() ,
                    0, 0, 1000
            );
        }
        return instance;
    }
}
