package batch.dao;

import batch.model.batch.BatchStepExecution;
import batch.model.batch.BatchExecutionParam;

import java.util.List;

/**
 * Created by MBS on 16/08/2016.
 */
public interface BatchJobDao {
    public  List<BatchStepExecution> selectAllStepExectuion();
    public BatchStepExecution selectStepExecution(Long id);

    public Long getLastIDTable(String id,String table);

    public BatchExecutionParam selectBatchExecParam(Long id);

    public List<BatchExecutionParam> selectAllBatchExecParam();
}
