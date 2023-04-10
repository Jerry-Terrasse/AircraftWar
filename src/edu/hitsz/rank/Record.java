package edu.hitsz.rank;

import java.io.Serializable;

public class Record implements Serializable {
    private int record_id = -1;
    private String name;
    private int score;
    public Record(String name, int score, int record_id) {
        this.name = name;
        this.score = score;
        this.record_id = record_id;
    }
    public int getRecord_id() {
        return record_id;
    }
    public String getName() {
        return name;
    }
    public int getScore() {
        return score;
    }
    public void setRecord_id(int id) {
        this.record_id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setScore(int score) {
        this.score = score;
    }
}
