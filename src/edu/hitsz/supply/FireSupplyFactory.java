package edu.hitsz.supply;

public class FireSupplyFactory implements SupplyFactory{
    private static final FireSupplyFactory instance = new FireSupplyFactory();
    private FireSupplyFactory() {}
    public static FireSupplyFactory getInstance() {
        return instance;
    }
    @Override
    public BaseSupply createSupply(int x, int y, int vx, int vy) {
        return new FireSupply(x, y, vx, vy);
    }
}
