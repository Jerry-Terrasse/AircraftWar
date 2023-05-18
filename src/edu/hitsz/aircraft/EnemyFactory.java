package edu.hitsz.aircraft;

public abstract class EnemyFactory {
    protected int HpFactor;
    protected double SpeedFactor;
    protected EnemyFactory(int hpFactor, double speedFactor) {
        this.HpFactor = hpFactor;
        this.SpeedFactor = speedFactor;
    }
    /**
     * 创建飞机
     * @param x x轴坐标
     * @param y y轴坐标
     * @param vx x轴速度
     * @param vy y轴速度
     * @return 飞机
     */
    abstract public AbstractEnemy createAircraft(double x, double y, double vx, double vy);
    abstract public void promote();
}
