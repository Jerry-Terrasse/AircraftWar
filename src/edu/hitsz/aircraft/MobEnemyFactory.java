package edu.hitsz.aircraft;

public class MobEnemyFactory implements EnemyFactory {
    final static MobEnemyFactory INSTANCE = new MobEnemyFactory();
    private MobEnemyFactory() {}
    public static MobEnemyFactory getInstance() {
        return INSTANCE;
    }
    @Override
    public AbstractEnemy createAircraft(int x, int y, int vx, int vy) {
        return new MobEnemy(x, y, vx, vy, 30);
    }
}