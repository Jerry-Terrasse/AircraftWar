package edu.hitsz.rank;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

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
        if(this.records.isEmpty()) {
            return 1;
        }
        return this.records.get(this.records.size()-1).getRecord_id() + 1;
    }

    @Override
    public void doAdd(Record record) {
        if(record.getRecord_id() == -1) {
            record.setRecord_id(get_new_id());
        }
        this.records.add(record);
    }

    @Override
    public void doDelete(int record_id) {
        this.records.removeIf(record -> record.getRecord_id() == record_id);
    }
}
