package edu.hitsz.rank;

import java.io.Serializable;
import java.util.Date;

public class Record implements Serializable {
    private int record_id = -1;
    private String name;
    private int score;
    private Date date;
    public Record(String name, int score, int record_id) {
        this.name = name;
        this.score = score;
        this.record_id = record_id;
        this.date = new Date();
    }
    public int getRecord_id() {
        return record_id;
    }
    public String getName() {
        return name;
    }
    public Date getDate() {
        return date;
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

    /* 为可视化组件提供的方法 */
    static public String[] getColumnNames()
    {
        return new String[]{"名次", "玩家名", "得分", "记录时间", "记录ID"};
    }
//    public String[] getRowData()
//    {
//        return new String[]{name, String.valueOf(score), date.toString()};
//    }
}
