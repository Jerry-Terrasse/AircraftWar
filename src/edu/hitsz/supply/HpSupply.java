package edu.hitsz.supply;

import edu.hitsz.application.WorldHandle;

public class HpSupply extends BaseSupply {
    private int increase = 20;

    public HpSupply(int X, int Y, int speedX, int speedY, int increase) {
        super(X, Y, speedX, speedY);
        this.increase = increase;
    }

    @Override
    public void takeEffect(WorldHandle world) {
        super.takeEffect(world);
        world.increaseHeroHp(increase);
    }
}
