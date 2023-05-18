package edu.hitsz.application;

import edu.hitsz.aircraft.BossEnemyFactory;
import edu.hitsz.aircraft.EliteEnemyFactory;
import edu.hitsz.aircraft.MobEnemyFactory;
import edu.hitsz.rank.RecordDaoImpl;

public class NormalGame extends BaseGame {
    public NormalGame() {
        super();
        this.difficulty = 2;
        this.backGroundImage = ImageManager.BACKGROUND_IMAGE_NORMAL;

        this.enemyMaxNumber = 5;
        this.EnemyHpFactor = 1;
        this.EnemySpeedFactor = 1.3;
        this.EliteEnemyProbability = 0.15;
        this.BossScoreThresh = 200;

        this.eliteEnemyFactory = new EliteEnemyFactory(this.EnemyHpFactor, this.EnemySpeedFactor);
        this.mobEnemyFactory = new MobEnemyFactory(this.EnemyHpFactor, this.EnemySpeedFactor);
        this.bossEnemyFactory = new BossEnemyFactory(1, this.EnemySpeedFactor); // Boss血量不进行翻倍

        this.recordDao = new RecordDaoImpl(String.format("rank_data_%d.dat", this.difficulty));
    }
}
