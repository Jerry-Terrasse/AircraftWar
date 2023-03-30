package edu.hitsz.supply;

public class BombSupplyFactory implements SupplyFactory{
    private static final BombSupplyFactory INSTANCE = new BombSupplyFactory();
    private BombSupplyFactory() {}
    public static BombSupplyFactory getInstance() {
        return INSTANCE;
    }
    @Override
    public BaseSupply createSupply(int x, int y, int vx, int vy) {
        return new BombSupply(x, y, vx, vy);
    }
}
