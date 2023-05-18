package edu.hitsz.application;

import edu.hitsz.aircraft.HeroAircraft;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * 英雄机控制类
 * 监听鼠标，控制英雄机的移动
 *
 * @author hitsz
 */
public class HeroController {
    private BaseGame game;
    private HeroAircraft heroAircraft;
    private MouseAdapter mouseAdapter;

    public HeroController(BaseGame game, HeroAircraft heroAircraft){
        this.game = game;
        this.heroAircraft = heroAircraft;

        mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                int x = e.getX();
                int y = e.getY();
                if ( x<30 || x>Main.WINDOW_WIDTH-30 || y<30 || y>Main.WINDOW_HEIGHT-30){
                    // 防止超出边界
                    return;
                }
                // 防止移动过远
                if (Math.abs(x - heroAircraft.getLocationX()) > 20 || Math.abs(y - heroAircraft.getLocationY()) > 30) {
                    return;
                }
                heroAircraft.setLocation(x, y);
            }
        };

        game.addMouseListener(mouseAdapter);
        game.addMouseMotionListener(mouseAdapter);
    }


}
