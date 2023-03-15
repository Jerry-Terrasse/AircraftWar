package edu.hitsz.application;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.supply.BaseSupply;

import java.util.List;

public class WorldHandle {
    private final HeroAircraft hero;
    private final List<AbstractAircraft> enemies;
    private final List<BaseBullet> heroBullets;
    private final List<BaseBullet> enemyBullets;
    private final List<BaseSupply> supplies;

    public WorldHandle(
            HeroAircraft hero,
            List<AbstractAircraft> enemyAircrafts,
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
        hero.increaseShootNum();
    }
}