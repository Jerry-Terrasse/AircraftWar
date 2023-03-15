package edu.hitsz.supply;

import edu.hitsz.application.WorldHandle;

public class FireSupply extends BaseSupply {
    public FireSupply(int X, int Y, int speedX, int speedY) {
        super(X, Y, speedX, speedY);
    }

    @Override
    public void takeEffect(WorldHandle world) {
        super.takeEffect(world);
        world.increaseHeroFire();
    }
}
