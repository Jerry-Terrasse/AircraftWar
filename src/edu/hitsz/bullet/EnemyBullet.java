package edu.hitsz.bullet;

import edu.hitsz.supply.BombListener;
import edu.hitsz.supply.BombSupply;

/**
 * @Author hitsz
 */
public class EnemyBullet extends BaseBullet implements BombListener {

    public EnemyBullet(double locationX, double locationY, double speedX, double speedY, int power) {
        super(locationX, locationY, speedX, speedY, power);
        BombSupply.addBombListener(this);
    }

    public void onBomb() {
        vanish();
    }
}
