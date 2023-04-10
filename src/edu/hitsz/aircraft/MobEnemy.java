package edu.hitsz.aircraft;

import edu.hitsz.application.Main;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.shootStrategy.NoShootStrategy;

import java.util.LinkedList;
import java.util.List;

/**
 * 普通敌机
 * 不可射击
 *
 * @author hitsz
 */
public class MobEnemy extends AbstractEnemy {

    public MobEnemy(double locationX, double locationY, double speedX, double speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp, new NoShootStrategy());
    }

    @Override
    public void forward() {
        super.forward();
        // 判定 y 轴向下飞行出界
        if (locationY >= Main.WINDOW_HEIGHT ) {
            vanish();
        }
    }

    @Override
    public List<BaseBullet> shoot_() {
        return new LinkedList<>();
    }

}
