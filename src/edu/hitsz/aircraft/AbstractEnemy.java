package edu.hitsz.aircraft;

import edu.hitsz.shootStrategy.BaseShootStrategy;
import edu.hitsz.supply.BaseSupply;

import java.util.LinkedList;
import java.util.List;

public abstract class AbstractEnemy extends AbstractAircraft {
    public AbstractEnemy(int locationX, int locationY, int speedX, int speedY, int hp, BaseShootStrategy shootStrategy) {
        super(locationX, locationY, speedX, speedY, hp, shootStrategy);
    }

    public List<BaseSupply> produceSupply() {
        return new LinkedList<>();
    }
}