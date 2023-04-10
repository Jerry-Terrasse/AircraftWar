package edu.hitsz.aircraft;

import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.supply.BaseSupply;
import edu.hitsz.supply.BombSupply;
import edu.hitsz.supply.FireSupply;
import edu.hitsz.supply.HpSupply;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.util.List;

class EliteEnemyTest {
    private AbstractEnemy eliteEnemy;
    @BeforeEach
    void setUp() {
        eliteEnemy = EliteEnemyFactory.getInstance().createAircraft(50, 50, 0, 0);
    }

    @AfterEach
    void tearDown() {
        eliteEnemy = null;
    }

    /**
     * 测试敌机的生命值改变方法
     */
    @ParameterizedTest
    @ValueSource(ints = {0, 2, 3, 100, Integer.MAX_VALUE})
    void changeHp(int final_decrease) {
        int nowHp = eliteEnemy.getHp();
        int maxHp = eliteEnemy.getMaxHp();
        eliteEnemy.decreaseHp(1);
        assertEquals(nowHp - 1, eliteEnemy.getHp());
        eliteEnemy.decreaseHp(5);
        assertEquals(nowHp - 6, eliteEnemy.getHp());
        eliteEnemy.increaseHp(3);
        assertEquals(nowHp - 3, eliteEnemy.getHp());
        eliteEnemy.increaseHp(5);
        assertEquals(maxHp, eliteEnemy.getHp());
        eliteEnemy.decreaseHp(final_decrease);
        if(maxHp - final_decrease <= 0) {
            assertTrue(eliteEnemy.notValid());
        } else {
            assertFalse(eliteEnemy.notValid());
            assertEquals(maxHp - final_decrease, eliteEnemy.getHp());
        }
    }

    @Test
    void crash() {
        double x = eliteEnemy.getLocationX();
        double y = eliteEnemy.getLocationY();
        AbstractAircraft other = EliteEnemyFactory.getInstance().createAircraft(x, y, 0, 0);
        assertTrue(eliteEnemy.crash(other));
    }

    @Test
    void shoot() {
        List<BaseBullet> bullets = eliteEnemy.shoot();
        assertEquals(1, bullets.size());
        assertEquals(EnemyBullet.class, bullets.get(0).getClass());
    }

    @Test
    void produceSupply() {
        List<BaseSupply> supplies = eliteEnemy.produceSupply();
        assertTrue(supplies.size() == 0 || supplies.size() == 1);
        assumeTrue(supplies.size() == 1);
        BaseSupply supply = supplies.get(0);
        assertTrue(
                supply.getClass() == BombSupply.class ||
                supply.getClass() == FireSupply.class ||
                supply.getClass() == HpSupply.class
        );
    }
}