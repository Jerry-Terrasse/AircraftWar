package edu.hitsz.supply;

public interface SupplyFactory {
/**
     * 创建补给
     * @param x x轴坐标
     * @param y y轴坐标
     * @param vx x轴速度
     * @param vy y轴速度
     * @return 补给
     */
    BaseSupply createSupply(int x, int y, int vx, int vy);
}
