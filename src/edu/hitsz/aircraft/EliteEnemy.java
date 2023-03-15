package edu.hitsz.aircraft;

import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.supply.BaseSupply;
import edu.hitsz.supply.BombSupply;
import edu.hitsz.supply.FireSupply;
import edu.hitsz.supply.HpSupply;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class EliteEnemy extends AbstractAircraft {
    private int bulletPower = 20;
    private int direction = 1;
    static private double probability = 0.2; // 生成概率

    static public double getProbability() {
        return probability;
    }

    public EliteEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
    }
    
    @Override
    public List<BaseBullet> shoot() {
        List<BaseBullet> res = new LinkedList<>();
        int x = this.getLocationX();
        int y = this.getLocationY() + direction*2;
        int speedX = 0;
        int speedY = this.getSpeedY() + direction*5;
        BaseBullet bullet = new EnemyBullet(x, y, speedX, speedY, bulletPower);
        res.add(bullet);
        return res;
    }

    static int supply_type = 0; // 按顺序依次切换补给种类
    @Override
    public List<BaseSupply> produceSupply() {
        BaseSupply supply;
        int vx = speedX;
        if(Math.random() < 0.5) {
            vx += 3;
        } else {
            vx -= 3;
        }
        switch (supply_type % 3) {
            case 0:
                supply = new HpSupply(locationX, locationY, vx, speedY);
                break;
            case 1:
                supply = new FireSupply(locationX, locationY, vx, speedY);
                break;
            case 2:
                supply = new BombSupply(locationX, locationY, vx, speedY);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + supply_type % 3);
        }
        supply_type++;

        List<BaseSupply> supplyList = new LinkedList<>();
        supplyList.add(supply);
        return supplyList;
    }
}
