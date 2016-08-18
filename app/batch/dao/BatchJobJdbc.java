package batch.dao;

import batch.model.batch.BatchStepExecution;
import batch.model.batch.BatchExecutionParam;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by MBS on 16/08/2016.
 */

public class BatchJobJdbc extends JdbcTemplate implements BatchJobDao {


    @Override
    public List<BatchStepExecution> selectAllStepExectuion() {
        /*
        System.out.println("SELECT ////////////////////////////////////////////////////////////////////////////////////");
      List<BatchStepExecution> batchStepExecutions = query("select * from BATCH_STEP_EXECUTION", new RowMapper<BatchStepExecution>() {
          @Override
          public BatchStepExecution mapRow(ResultSet rs, int rowNum) throws SQLException {
             BatchStepExecution batchStepExecution = new BatchStepExecution();
              batchStepExecution.setStep_execution_id(rs.getLong("STEP_EXECUTION_ID"));
              batchStepExecution.setVersion(rs.getLong("VERSION"));
              batchStepExecution.setStep_name(rs.getString("STEP_NAME"));
           //   batchExecution.setJob_execution_id(rs.getLong("JOB_EXECUTION_ID"));
              batchStepExecution.setCommit_count(rs.getLong("COMMIT_COUNT"));
              batchStepExecution.setEnd_time(rs.getTime("END_TIME"));
              batchStepExecution.setProcess_skip_count(rs.getLong("PROCESS_SKIP_COUNT"));
              batchStepExecution.setFilter_count(rs.getLong("FILTER_COUNT"));
              batchStepExecution.setExit_code(rs.getString("EXIT_CODE"));
              batchStepExecution.setRead_count(rs.getLong("READ_COUNT"));
              batchStepExecution.setRead_skip_count(rs.getLong("READ_SKIP_COUNT"));
              batchStepExecution.setLast_updated(rs.getTime("LAST_UPDATED"));
              //batchExecution.setStatuts(rs.getString("STATUS"));
              batchStepExecution.setWrite_count(rs.getLong("WRITE_COUNT"));
              batchStepExecution.setStart_time(rs.getTime("START_TIME"));
              batchStepExecution.setWrite_skip_count(rs.getLong("WRITE_SKIP_COUNT"));
              batchStepExecution.setRollback_count(rs.getLong("ROLLBACK_COUNT"));
              batchStepExecution.setExit_message(rs.getString("EXIT_MESSAGE"));
              return batchStepExecution;
          }
      });*/
        return null;//batchStepExecutions;
    }

    @Override
    public BatchStepExecution selectStepExecution(Long id) {
        /*BatchExecution batchExecution = queryForObject(
                "select * from BATCH_STEP_EXECUTION where STEP_EXECUTION_ID = ?",
                new Object[]{id},
                new RowMapper<BatchExecution>() {
                    public BatchExecution mapRow(ResultSet rs, int rowNum) throws SQLException {
                        BatchExecution batchExecution1 = new BatchExecution();
                        batchExecution1.setStep_execution_id(rs.getLong("STEP_EXECUTION_ID"));
                       // batchExecution1.setJob_execution_id(rs.getLong("JOB_EXECUTION_ID"));
                        batchExecution1.setVersion(rs.getLong("VERSION"));
                        batchExecution1.setStep_name(rs.getString("STEP_NAME"));
                        batchExecution1.setCommit_count(rs.getLong("COMMIT_COUNT"));
                        batchExecution1.setEnd_time(rs.getTime("END_TIME"));
                        batchExecution1.setProcess_skip_count(rs.getLong("PROCESS_SKIP_COUNT"));
                        batchExecution1.setFilter_count(rs.getLong("FILTER_COUNT"));
                        batchExecution1.setExit_code(rs.getString("EXIT_CODE"));
                        batchExecution1.setRead_count(rs.getLong("READ_COUNT"));
                        batchExecution1.setLast_updated(rs.getTime("LAST_UPDATED"));
                        batchExecution1.setStatuts(rs.getString("STATUS"));
                        batchExecution1.setWrite_count(rs.getLong("WRITE_COUNT"));
                        batchExecution1.setStart_time(rs.getTime("START_TIME"));
                        batchExecution1.setWrite_skip_count(rs.getLong("WRITE_SKIP_COUNT"));
                        batchExecution1.setRollback_count(rs.getLong("ROLLBACK_COUNT"));
                        batchExecution1.setExit_message(rs.getString("EXIT_MESSAGE"));
                        return batchExecution1;
                    }
                });
        return batchExecution;*/
       return  null;//BatchStepExecution.find.byId(1L);
    }

    @Override
    public Long getLastIDTable(String id,String table) {

            return queryForObject("select MAX ("+id+")from"+table, Long.class);

    }

    @Override
    public BatchExecutionParam selectBatchExecParam(Long id) {
       /* BatchExecutionParam batchExecutionParam = queryForObject(
                "select * from BATCH_JOB_EXECUTION_PARAMS where JOB_EXECUTION_ID = ? AND KEY_NAME <> ?",
                new Object[]{id,"time"},
                new RowMapper<BatchExecutionParam>() {
                    public BatchExecutionParam mapRow(ResultSet rs, int rowNum) throws SQLException {
                        BatchExecutionParam batchExecutionParam1 = new BatchExecutionParam();
                        batchExecutionParam1.setJob_execution_id(rs.getLong("JOB_EXECUTION_ID"));
                        batchExecutionParam1.setDate_val(rs.getDate("DATE_VAL"));
                        batchExecutionParam1.setIdentifying(rs.getString("IDENTIFYING").charAt(0));
                        batchExecutionParam1.setKey_name(rs.getString("KEY_NAME"));
                        batchExecutionParam1.setLong_val(rs.getLong("LONG_VAL"));
                        batchExecutionParam1.setDouble_val(rs.getDouble("DOUBLE_VAL"));
                        batchExecutionParam1.setString_val(rs.getString("STRING_VAL"));
                        batchExecutionParam1.setType_cd(rs.getString("TYPE_CD"));
                        return batchExecutionParam1;
                    }
                });
        return batchExecutionParam;*/
        return null;//BatchExecutionParam.find.byId(id);
    }

    @Override
    public List<BatchExecutionParam> selectAllBatchExecParam() {
        /*List<BatchExecutionParam> batchExecutionParams = query("select * from BATCH_JOB_EXECUTION_PARAMS ", new RowMapper<BatchExecutionParam>() {
            @Override
            public BatchExecutionParam mapRow(ResultSet rs, int rowNum) throws SQLException {
                BatchExecutionParam batchExecutionParam1 = new BatchExecutionParam();
                batchExecutionParam1.setJob_execution_id(rs.getLong("JOB_EXECUTION_ID"));
                batchExecutionParam1.setDate_val(rs.getDate("DATE_VAL"));
                batchExecutionParam1.setIdentifying(rs.getString("IDENTIFYING").charAt(0));
                batchExecutionParam1.setKey_name(rs.getString("KEY_NAME"));
                batchExecutionParam1.setLong_val(rs.getLong("LONG_VAL"));
                batchExecutionParam1.setDouble_val(rs.getDouble("DOUBLE_VAL"));
                batchExecutionParam1.setString_val(rs.getString("STRING_VAL"));
                batchExecutionParam1.setType_cd(rs.getString("TYPE_CD"));
                return batchExecutionParam1;
            }
        });*/
        return null;//batchExecutionParams;
    }


}



