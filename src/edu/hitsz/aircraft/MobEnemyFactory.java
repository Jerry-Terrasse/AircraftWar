package edu.hitsz.aircraft;

public class MobEnemyFactory implements AircraftFactory{
    final static MobEnemyFactory instance = new MobEnemyFactory();
    private MobEnemyFactory() {}
    public static MobEnemyFactory getInstance() {
        return instance;
    }
    @Override
    public AbstractAircraft createAircraft(int x, int y, int vx, int vy) {
        return new MobEnemy(x, y, vx, vy, 30);
    }
}