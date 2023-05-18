package edu.hitsz.aircraft;

import edu.hitsz.bullet.EnemyBulletFactory;
import edu.hitsz.shootStrategy.DisperseStrategy;
import edu.hitsz.shootStrategy.StraightShootStrategy;

import java.util.Random;

public class EliteEnemyFactory extends EnemyFactory {
    public EliteEnemyFactory(int hpFactor, double speedFactor) {
        super(hpFactor, speedFactor);
    }
    @Override
    public AbstractEnemy createAircraft(double x, double y, double vx, double vy) {
        AbstractEnemy enemy = new EliteEnemy(x, y, vx*this.SpeedFactor, vy*this.SpeedFactor, 40 * HpFactor);
        if(shootNum > 1) {
            enemy.setShootStrategy(new DisperseStrategy(1, new EnemyBulletFactory(20), new Random().nextInt(shootNum+1)));
        }
        return enemy;
    }
    private int shootNum = 1;
    @Override
    public void promote() {
        shootNum += 1;
    }
}