package edu.hitsz.aircraft;

public interface EnemyFactory {
    /**
     * 创建飞机
     * @param x x轴坐标
     * @param y y轴坐标
     * @param vx x轴速度
     * @param vy y轴速度
     * @return 飞机
     */
    AbstractEnemy createAircraft(int x, int y, int vx, int vy);
}
