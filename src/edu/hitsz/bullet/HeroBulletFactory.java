package edu.hitsz.bullet;

public class HeroBulletFactory extends BaseBulletFactory {
    public HeroBulletFactory(int power) {
        super(power);
    }

    @Override
    public BaseBullet createBullet(int locationX, int locationY, int speedX, int speedY) {
        return new HeroBullet(locationX, locationY, speedX, speedY, power);
    }
}