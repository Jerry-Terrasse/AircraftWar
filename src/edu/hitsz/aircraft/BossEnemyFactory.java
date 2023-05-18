package edu.hitsz.aircraft;

public class BossEnemyFactory extends EnemyFactory {
    public BossEnemyFactory(int hpFactor, double speedFactor) {
        super(hpFactor, speedFactor);
    }
    @Override
    public AbstractEnemy createAircraft(double x, double y, double vx, double vy) {
        return new BossEnemy(x, y, vx*this.SpeedFactor, vy*this.SpeedFactor, BossEnemy.getInitHp()*this.HpFactor);
    }
    @Override
    public void promote() {
        this.HpFactor += 1;
    }
}