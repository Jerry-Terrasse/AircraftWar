package edu.hitsz.shootStrategy;

import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.BaseBulletFactory;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class StraightShootStrategy extends BaseShootStrategy {
    public StraightShootStrategy(int direction, BaseBulletFactory bulletFactory) {
        super(direction, bulletFactory);
    }
    @Override
    public List<BaseBullet> shoot(int x, int y, int vx, int vy) {
        List<BaseBullet> bullets = new LinkedList<>();
        int bulletVx = 0;
        int bulletVy = vy + direction * velocity;
        bullets.add(bulletFactory.createBullet(x, y, bulletVx, bulletVy));
        return bullets;
    }
}
