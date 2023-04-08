package edu.hitsz.bullet;

public class EnemyBulletFactory extends BaseBulletFactory {
    public EnemyBulletFactory(int power) {
        super(power);
    }

    @Override
    public BaseBullet createBullet(int locationX, int locationY, int speedX, int speedY) {
        return new EnemyBullet(locationX, locationY, speedX, speedY, power);
    }
}
