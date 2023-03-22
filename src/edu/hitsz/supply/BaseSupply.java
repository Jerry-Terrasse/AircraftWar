package edu.hitsz.supply;

import edu.hitsz.application.Main;
import edu.hitsz.application.WorldHandle;
import edu.hitsz.basic.AbstractFlyingObject;

public abstract class BaseSupply extends AbstractFlyingObject {

    public BaseSupply(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void forward() {
        super.forward();

        // 判定 y 轴出界
        if (speedY > 0 && locationY >= Main.WINDOW_HEIGHT ) {
            // 向下飞行出界
            vanish();
        } else if (locationY <= 0){
            // 向上飞行出界
            vanish();
        }
        System.out.println(speedY);
    }

    public void takeEffect(WorldHandle world) {
        System.out.println(this.getClass().getName() + "active!");
    }
}
