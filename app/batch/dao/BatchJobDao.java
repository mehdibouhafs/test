package batch.dao;

import batch.model.BatchExecution;

import java.util.List;

/**
 * Created by MBS on 16/08/2016.
 */
public interface BatchJobDao {
    public  List<BatchExecution> selectAllDetail();
}
