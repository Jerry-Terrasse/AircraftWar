package edu.hitsz.supply;

import edu.hitsz.application.WorldHandle;

public class FireSupply extends BaseSupply {
    public FireSupply(int x, int y, int speedX, int speedY) {
        super(x, y, speedX, speedY);
    }

    @Override
    public void takeEffect(WorldHandle world) {
        super.takeEffect(world);
        world.increaseHeroFire();
    }
}
