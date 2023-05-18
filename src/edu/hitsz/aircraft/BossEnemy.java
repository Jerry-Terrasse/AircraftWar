package edu.hitsz.aircraft;

import edu.hitsz.bullet.EnemyBulletFactory;
import edu.hitsz.shootStrategy.DisperseStrategy;
import edu.hitsz.supply.BaseSupply;
import edu.hitsz.supply.BombSupplyFactory;
import edu.hitsz.supply.FireSupplyFactory;
import edu.hitsz.supply.HpSupplyFactory;

import java.util.LinkedList;
import java.util.List;

public class BossEnemy extends AbstractEnemy {
    /**
     * Boss机初始血量
     */
    static private int initHp = 250;
    /**
     * 场上Boss机总数
     */
    static private int count = 0;
    static public int getCount() {
        return count;
    }
    static public int getInitHp() {
        return initHp;
    }

    public BossEnemy(double locationX, double locationY, double speedX, double speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp, new DisperseStrategy(1, new EnemyBulletFactory(20), 3));
        this.score = 200;
        count++;
    }

    @Override
    public List<BaseSupply> produceSupply() {
        BaseSupply supply;
        double vx = speedX;
        double vy = speedY + 5;
        if (Math.random() < 0.5) {
            vx += 3;
        } else {
            vx -= 3;
        }

        List<BaseSupply> supplyList = new LinkedList<>();
        for(int i=0; i<3; ++i) {
            double p = Math.random();
            double deltaVx = (Math.random() * 5);
            double deltaVy = (Math.random() * 3);
            if (p < 0.4) { // 40%概率掉落火力道具
                supply = FireSupplyFactory.getInstance().createSupply(locationX, locationY, vx + deltaVx, vy + deltaVy);
            } else if (p < 0.9) { // 50%概率掉落加血道具
                supply = HpSupplyFactory.getInstance().createSupply(locationX, locationY, vx + deltaVx, vy + deltaVy);
            } else { // 10%概率掉落炸弹道具
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
    }

    @Override
    protected boolean isBoss() {
        return true;
    }

    @Override
    public void onBomb() {
        decreaseHp(200);
    }
}
