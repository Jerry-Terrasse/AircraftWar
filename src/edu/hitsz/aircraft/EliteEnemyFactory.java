package edu.hitsz.aircraft;

public class EliteEnemyFactory implements AircraftFactory{
    final static EliteEnemyFactory instance = new EliteEnemyFactory();
    private EliteEnemyFactory() {}
    public static EliteEnemyFactory getInstance() {
        return instance;
    }
    @Override
    public AbstractAircraft createAircraft(int x, int y, int vx, int vy) {
        return new EliteEnemy(x, y, vx, vy, 50);
    }
}