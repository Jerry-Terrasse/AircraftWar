package edu.hitsz.shootStrategy;

import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.BaseBulletFactory;

import java.util.List;

public abstract class BaseShootStrategy {
    protected int direction;
    protected int velocity;
    protected BaseBulletFactory bulletFactory;
    public BaseShootStrategy(int direction, BaseBulletFactory bulletFactory) {
        this.direction = direction;
        this.bulletFactory = bulletFactory;
        this.velocity = 3;
    }
    public abstract List<BaseBullet> shoot(int x, int y, int vx, int vy);
}