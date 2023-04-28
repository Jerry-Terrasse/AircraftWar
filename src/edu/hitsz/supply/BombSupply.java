package edu.hitsz.supply;

import edu.hitsz.application.MusicManager;
import edu.hitsz.application.MusicThread;
import edu.hitsz.application.WorldHandle;

public class BombSupply extends BaseSupply{
    public BombSupply(double x, double y, double speedX, double speedY) {
        super(x, y, speedX, speedY);
    }

    @Override
    public void takeEffect(WorldHandle world) {
        super.takeEffect(world);
        world.clearEnemies();
        world.clearBullets();
        if(world.getWithMusic()) {
            MusicThread musicThread = new MusicThread(MusicManager.getMusicPath("bomb_explosion"), false);
            musicThread.start();
        }
//        world.clearSupplies();
    }
}
