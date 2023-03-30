package edu.hitsz.aircraft;

public class BossEnemyFactory implements AircraftFactory{
    final static BossEnemyFactory INSTANCE = new BossEnemyFactory();
    private BossEnemyFactory() {}
    public static BossEnemyFactory getInstance() {
        return INSTANCE;
    }
    @Override
    public AbstractAircraft createAircraft(int x, int y, int vx, int vy) {
        return new BossEnemy(x, y, vx, vy, BossEnemy.getInitHp());
    }
}