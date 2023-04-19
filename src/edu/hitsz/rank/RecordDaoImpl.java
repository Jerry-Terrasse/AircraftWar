package edu.hitsz.rank;

import java.io.*;
import java.util.*;

public class RecordDaoImpl implements RecordDao {
    String filename;
    List<Record> records;
    public RecordDaoImpl(String filename) {
        this.filename = filename;
        this.records = new LinkedList<>();
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename));
            this.records = (List<Record>) in.readObject();
            in.close();
        } catch (FileNotFoundException ignored) {

        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
    @Override
    public void save() {
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename));
            out.writeObject(this.records);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//    @Override
//    protected void finalize() {
//        try {
//            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename));
//            out.writeObject(this.records);
//            out.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
    @Override
    public Optional<Record> findById(int record_id) {
        for(Record record: this.records) {
            if(record.getRecord_id() == record_id) {
                return Optional.of(record);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Record> getAll() {
        return this.records;
    }

    protected int get_new_id() {
        int max_id = 0;
        for(Record record: this.records) {
            if(record.getRecord_id() > max_id) {
                max_id = record.getRecord_id();
            }
        }
        return max_id + 1;
    }

    @Override
    public void doAdd(Record record) {
        if(record.getRecord_id() == -1) {
            record.setRecord_id(get_new_id());
        }
        this.records.add(record);
        save();
    }

    @Override
    public void doDelete(int record_id) {
        this.records.removeIf(record -> record.getRecord_id() == record_id);
        save();
    }

    /* 为可视化组件提供的方法 */
    @Override
    public String[][] getTableData()
    {
        this.records.sort((Record a, Record b) -> {
            if (a.getScore() == b.getScore()) {
                return 0;
            }
            return a.getScore() > b.getScore() ? -1 : 1;
        });
        String[][] data = new String[this.records.size()][5];
        for(int i = 0; i < this.records.size(); i++) {
            data[i] = new String[]{String.valueOf(i + 1), this.records.get(i).getName(), String.valueOf(this.records.get(i).getScore()), this.records.get(i).getDate().toString(), String.valueOf(this.records.get(i).getRecord_id())};
        }
        return data;
    }
}
