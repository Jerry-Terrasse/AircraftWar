package edu.hitsz.application;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.AbstractEnemy;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.HeroBulletFactory;
import edu.hitsz.shootStrategy.DisperseStrategy;
import edu.hitsz.shootStrategy.StraightShootStrategy;
import edu.hitsz.supply.BaseSupply;

import java.util.List;

public class WorldHandle {
    private final HeroAircraft hero;
    private final List<AbstractEnemy> enemies;
    private final List<BaseBullet> heroBullets;
    private final List<BaseBullet> enemyBullets;
    private final List<BaseSupply> supplies;
    private boolean withMusic = false;

    public WorldHandle(
            HeroAircraft hero,
            List<AbstractEnemy> enemyAircrafts,
            List<BaseBullet> heroBullets,
            List<BaseBullet> enemyBullets,
            List<BaseSupply> supplies
    ) {
        this.hero = hero;
        this.enemies = enemyAircrafts;
        this.heroBullets = heroBullets;
        this.enemyBullets = enemyBullets;
        this.supplies = supplies;
    }

    public void increaseHeroHp(int increase) {
        hero.increaseHp(increase);
    }

    public void clearEnemies() {
        enemies.forEach((item) -> {
            item.vanish();
        });
    }
    public void clearBullets() {
        heroBullets.forEach((item) -> {
            item.vanish();
        });
        enemyBullets.forEach((item) -> {
            item.vanish();
        });
    }
    public void clearSupplies() {
        supplies.forEach((item) -> {
            item.vanish();
        });
    }
    public void increaseHeroFire() {
        hero.setShootStrategy(new DisperseStrategy(-1, new HeroBulletFactory(20), 3));
        long time_stamp = System.currentTimeMillis();
        hero.setPromotionTS(time_stamp);
        Runnable r = () -> {
            try {
                Thread.sleep(5000);
                if(hero.getPromotionTS() == time_stamp) {
                    hero.setShootStrategy(new StraightShootStrategy(-1, new HeroBulletFactory(20)));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
        new Thread(r).start();
    }

    public void setWithMusic(boolean withMusic) {
        this.withMusic = withMusic;
    }
    public boolean getWithMusic() {
        return withMusic;
    }
}