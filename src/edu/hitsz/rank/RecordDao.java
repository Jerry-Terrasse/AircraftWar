package edu.hitsz.rank;

import java.util.List;
import java.util.Optional;

public interface RecordDao {
    Optional<Record> findById(int record_id);
    List<Record> getAll();
    void doAdd(Record record);
    void doDelete(int record_id);
    void save();
}
