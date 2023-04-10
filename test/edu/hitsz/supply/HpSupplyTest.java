package edu.hitsz.supply;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.application.Main;
import edu.hitsz.application.WorldHandle;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

import java.util.LinkedList;
import java.util.List;

class HpSupplyTest {
    static WorldHandle world;
    static HeroAircraft heroAircraft;
    BaseSupply supply;
    int x = 50, y = 50, vx = 1, vy = 1;
    int increase = 20;
    @BeforeAll
    static void setUp() {
        heroAircraft = HeroAircraft.getInstance();
        world = new WorldHandle(heroAircraft, new LinkedList<>(), new LinkedList<>(), new LinkedList<>(), new LinkedList<>());
    }

    @BeforeEach
    void createSupply() {
        supply = new HpSupply(x, y, vx, vy, increase);
    }

    @AfterAll
    static void tearDown() {
        world = null;
        heroAircraft = null;
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 10, 20, 30, 40, 50, 60})
    void takeEffect(int decrease) {
        heroAircraft.decreaseHp(decrease);
        assumeFalse(heroAircraft.notValid());
        int nowHp = heroAircraft.getHp();
        supply.takeEffect(world);
        if(nowHp + increase > heroAircraft.getMaxHp()) {
            assertEquals(heroAircraft.getMaxHp(), heroAircraft.getHp());
        } else {
            assertEquals(nowHp + increase, heroAircraft.getHp());
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 3, 10, 9999})
    void forward(int steps) {
        double x = supply.getLocationX();
        double y = supply.getLocationY();
        for (int i = 0; i < steps; i++) {
            supply.forward();
            x += vx;
            y += vy;
            if(x <= 0 || x >= Main.WINDOW_WIDTH) {
                vx = -vx;
            }
            if(y <= 0 || y >= Main.WINDOW_HEIGHT) {
                assertTrue(supply.notValid());
            }
            assumeFalse(supply.notValid());
        }
        assertEquals(x, supply.getLocationX());
        assertEquals(y, supply.getLocationY());
    }

    @Test
    void vanish() {
        supply.vanish();
        assertTrue(supply.notValid());
    }
}