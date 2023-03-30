package edu.hitsz.aircraft;

public class EliteEnemyFactory implements AircraftFactory{
    final static EliteEnemyFactory INSTANCE = new EliteEnemyFactory();
    private EliteEnemyFactory() {}
    public static EliteEnemyFactory getInstance() {
        return INSTANCE;
    }
    @Override
    public AbstractAircraft createAircraft(int x, int y, int vx, int vy) {
        return new EliteEnemy(x, y, vx, vy, 50);
    }
}