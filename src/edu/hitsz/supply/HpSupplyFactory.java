package edu.hitsz.supply;

public class HpSupplyFactory implements SupplyFactory{
    private static final HpSupplyFactory INSTANCE = new HpSupplyFactory();
    private HpSupplyFactory() {}
    public static HpSupplyFactory getInstance() {
        return INSTANCE;
    }
    @Override
    public BaseSupply createSupply(int x, int y, int vx, int vy) {
        return new HpSupply(x, y, vx, vy, 20);
    }
}
