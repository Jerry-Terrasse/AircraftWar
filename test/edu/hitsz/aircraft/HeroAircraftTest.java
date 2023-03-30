package edu.hitsz.aircraft;

import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.HeroBullet;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HeroAircraftTest {
    static private HeroAircraft heroAircraft;
    @BeforeAll
    static void setUp() {
        heroAircraft = HeroAircraft.getInstance();
    }

    @AfterAll
    static void tearDown() {
        heroAircraft = null;
    }

    @Test
    @Disabled
    void forward() {
    }

    @Test
    void shoot() {
        List<BaseBullet> bullets = heroAircraft.shoot();
        assertTrue(bullets.size() > 0);
        bullets.forEach(bullet -> assertTrue(bullet instanceof HeroBullet));
        // 测试子弹位置不重叠
        bullets.forEach(bullet1 -> bullets.forEach(bullet2 -> {
            if (bullet1 != bullet2) {
                assertNotEquals(bullet1.getLocationX(), bullet2.getLocationX());
            }
        }));
    }

    @Test
    void increaseShootNum() {
        int nowNum = heroAircraft.shoot().size();
        heroAircraft.increaseShootNum();
        int afterNum = heroAircraft.shoot().size();
        assertEquals(nowNum + 1, afterNum);
    }

    @Test
    void getInstance() {
        assertNotNull(HeroAircraft.getInstance());
    }
}