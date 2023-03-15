package edu.hitsz.supply;

import edu.hitsz.application.WorldHandle;

public class BombSupply extends BaseSupply{
    public BombSupply(int X, int Y, int speedX, int speedY) {
        super(X, Y, speedX, speedY);
    }

    @Override
    public void takeEffect(WorldHandle world) {
        super.takeEffect(world);
        world.clearEnemies();
        world.clearBullets();
//        world.clearSupplies();
    }
}
