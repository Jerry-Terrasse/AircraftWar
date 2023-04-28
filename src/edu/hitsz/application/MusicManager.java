package edu.hitsz.application;

import java.util.HashMap;
import java.util.Map;

public class MusicManager {
    private static final Map<String, String> map = new HashMap<>();
    static {
        map.put("bgm", "src/videos/bgm.wav");
        map.put("bgm_boss", "src/videos/bgm_boss.wav");
        map.put("bomb_explosion", "src/videos/bomb_explosion.wav");
        map.put("bullet", "src/videos/bullet.wav");
        map.put("bullet_hit", "src/videos/bullet_hit.wav");
        map.put("game_over", "src/videos/game_over.wav");
        map.put("get_supply", "src/videos/get_supply.wav");
    }
    public static String getMusicPath(String name) {
        return map.get(name);
    }
}