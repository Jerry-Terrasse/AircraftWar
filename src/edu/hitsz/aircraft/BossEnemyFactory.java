package edu.hitsz.aircraft;

public class BossEnemyFactory implements EnemyFactory {
    final static BossEnemyFactory INSTANCE = new BossEnemyFactory();
    private BossEnemyFactory() {}
    public static BossEnemyFactory getInstance() {
        return INSTANCE;
    }
    @Override
    public AbstractEnemy createAircraft(int x, int y, int vx, int vy) {
        return new BossEnemy(x, y, vx, vy, BossEnemy.getInitHp());
    }
}