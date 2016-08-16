package batch.dao;

import batch.model.BatchExecution;
import batch.model.BatchExecutionParam;

import java.util.List;

/**
 * Created by MBS on 16/08/2016.
 */
public interface BatchJobDao {
    public  List<BatchExecution> selectAllStepExectuion();
    public  BatchExecution selectStepExecution(Long id);

    public Long getLastIDTable(String id,String table);

    public BatchExecutionParam selectBatchExecParam(Long id);

    public List<BatchExecutionParam> selectAllBatchExecParam();
}
