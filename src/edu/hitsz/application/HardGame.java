package edu.hitsz.application;

import edu.hitsz.aircraft.BossEnemyFactory;
import edu.hitsz.aircraft.EliteEnemyFactory;
import edu.hitsz.aircraft.MobEnemyFactory;
import edu.hitsz.rank.RecordDaoImpl;

public class HardGame extends BaseGame {
    public HardGame() {
        super();
        this.difficulty = 3;
        this.backGroundImage = ImageManager.BACKGROUND_IMAGE_HARD;

        this.enemyMaxNumber = 7;
        this.EnemyHpFactor = 2;
        this.EnemySpeedFactor = 1.5;
        this.EliteEnemyProbability = 0.2;
        this.BossScoreThresh = 200;

        this.eliteEnemyFactory = new EliteEnemyFactory(this.EnemyHpFactor, this.EnemySpeedFactor);
        this.mobEnemyFactory = new MobEnemyFactory(this.EnemyHpFactor, this.EnemySpeedFactor);
        this.bossEnemyFactory = new BossEnemyFactory(1, this.EnemySpeedFactor); // Boss血量不进行翻倍

        this.recordDao = new RecordDaoImpl(String.format("rank_data_%d.dat", this.difficulty));
    }

    @Override
    protected void BossPromote() {
        System.out.println("Boss promoted!");
        this.bossEnemyFactory.promote();
    }
    @Override
    protected void ElitePromote() {
        if(this.time - this.eliteLastPromotionTime > 20000) {
            this.eliteEnemyFactory.promote();
            this.eliteLastPromotionTime = this.time;
            System.out.println("Elite enemy promoted!");
        }
    }
}