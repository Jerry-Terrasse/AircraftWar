package edu.hitsz.supply;

import edu.hitsz.application.WorldHandle;

public class HpSupply extends BaseSupply {
    private int increase = 20;

    public HpSupply(int X, int Y, int speedX, int speedY) {
        super(X, Y, speedX, speedY);
    }

    @Override
    public void takeEffect(WorldHandle world) {
        super.takeEffect(world);
        world.increaseHeroHp(increase);
    }
}
