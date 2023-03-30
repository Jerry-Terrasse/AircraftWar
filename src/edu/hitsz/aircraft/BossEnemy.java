package edu.hitsz.aircraft;

import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.supply.BaseSupply;
import edu.hitsz.supply.BombSupplyFactory;
import edu.hitsz.supply.FireSupplyFactory;
import edu.hitsz.supply.HpSupplyFactory;

import java.util.LinkedList;
import java.util.List;

public class BossEnemy extends AbstractAircraft {
    private final int bulletPower = 20;

    /**
     * 生成概率
     */
    static private final double PROBABILITY = 0.05;
    static private final int scoreThreshold = 100;
    /**
     * Boss机初始血量
     */
    static private int initHp = 1000;
    /**
     * 场上Boss机总数
     */
    static private int count = 0;

    static public double getProbability() {
        return PROBABILITY;
    }
    static public int getScoreThreshold() {
        return scoreThreshold;
    }
    static public int getCount() {
        return count;
    }
    static public int getInitHp() {
        return initHp;
    }

    public BossEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
        count++;
    }
    
    @Override
    public List<BaseBullet> shoot() {
        List<BaseBullet> res = new LinkedList<>();
        int x = this.getLocationX();
        int y = this.getLocationY() + 5;
        int[] speedX = {-3, 0, 3};
        int speedY = 5;
        for(int i = 0; i < 3; i++) {
            BaseBullet bullet = new EnemyBullet(x, y, getSpeedX() + speedX[i], getSpeedY() + speedY, bulletPower);
            res.add(bullet);
        }
        return res;
    }
    @Override
    public List<BaseSupply> produceSupply() {
        BaseSupply supply;
        int vx = speedX;
        int vy = speedY + 5;
        if (Math.random() < 0.5) {
            vx += 3;
        } else {
            vx -= 3;
        }

        List<BaseSupply> supplyList = new LinkedList<>();
        for(int i=0; i<3; ++i) {
            double p = Math.random();
            int deltaVx = (int) (Math.random() * 5);
            int deltaVy = (int) (Math.random() * 3);
            if (p < 1. / 3) { // 30%概率掉落火力道具
                supply = FireSupplyFactory.getInstance().createSupply(locationX, locationY, vx + deltaVx, vy + deltaVy);
            } else if (p < 2. / 3) { // 30%概率掉落加血道具
                supply = HpSupplyFactory.getInstance().createSupply(locationX, locationY, vx + deltaVx, vy + deltaVy);
            } else { // 30%概率掉落炸弹道具
                supply = BombSupplyFactory.getInstance().createSupply(locationX, locationY, vx + deltaVx, vy + deltaVy);
            }
            supplyList.add(supply);
        }
        return supplyList;
    }

    @Override
    public void vanish() {
        super.vanish();
        count--;

        // Boss机死亡后，下一架Boss机血量增加翻倍
        initHp *= 2;
    }
}
