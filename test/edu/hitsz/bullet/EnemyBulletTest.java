package edu.hitsz.bullet;

import edu.hitsz.application.Main;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class EnemyBulletTest {
    private EnemyBullet enemyBullet;
    private int vx = 0;
    private int vy = 1;

    @BeforeEach
    void setUp() {
        enemyBullet = new EnemyBullet(10, 10, vx, vy, 0);
    }

    @AfterEach
    void tearDown() {
        enemyBullet = null;
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 3, 10, 9999})
    void forward(int steps) {
        int x = enemyBullet.getLocationX();
        int y = enemyBullet.getLocationY();
        for (int i = 0; i < steps; i++) {
            enemyBullet.forward();
            x += vx;
            y += vy;
        }
        if(0 <= x && x <= Main.WINDOW_WIDTH && 0 <= y && y <= Main.WINDOW_HEIGHT) {
            assertFalse(enemyBullet.notValid());
            assertEquals(x, enemyBullet.getLocationX());
            assertEquals(y, enemyBullet.getLocationY());
        } else {
            assertTrue(enemyBullet.notValid());
        }
    }

    @Test
    void vanish() {
        enemyBullet.vanish();
        assertTrue(enemyBullet.notValid());
    }
}