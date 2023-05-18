package edu.hitsz.application;

import edu.hitsz.rank.RecordDaoImpl;

public class EasyGame extends BaseGame {
    public EasyGame() {
        super();
        this.difficulty = 1;
        this.backGroundImage = ImageManager.BACKGROUND_IMAGE_EASY;

        this.recordDao = new RecordDaoImpl(String.format("rank_data_%d.dat", this.difficulty));
    }
}
