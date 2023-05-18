package edu.hitsz.aircraft;

public class MobEnemyFactory extends EnemyFactory {
    public MobEnemyFactory(int hpFactor, double speedFactor) {
        super(hpFactor, speedFactor);
    }
    @Override
    public AbstractEnemy createAircraft(double x, double y, double vx, double vy) {
        return new MobEnemy(x, y, vx*this.SpeedFactor, vy*this.SpeedFactor, 10*this.HpFactor);
    }
    @Override
    public void promote() {}
}