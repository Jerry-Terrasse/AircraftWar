package edu.hitsz.supply;

public interface SupplyFactory {
    BaseSupply createSupply(int x, int y, int vx, int vy);
}
