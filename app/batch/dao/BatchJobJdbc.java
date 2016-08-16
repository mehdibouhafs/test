package batch.dao;

import batch.model.BatchExecution;
import batch.model.BatchExecutionParam;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.atomic.LongAccumulator;

/**
 * Created by MBS on 16/08/2016.
 */

public class BatchJobJdbc extends JdbcTemplate implements BatchJobDao {


    @Override
    public List<BatchExecution> selectAllStepExectuion() {
        System.out.println("SELECT ////////////////////////////////////////////////////////////////////////////////////");
      List<BatchExecution> batchExecutions = query("select * from BATCH_STEP_EXECUTION", new RowMapper<BatchExecution>() {
          @Override
          public BatchExecution mapRow(ResultSet rs, int rowNum) throws SQLException {
             BatchExecution   batchExecution = new BatchExecution();
              batchExecution.setVersion(rs.getLong("VERSION"));
              batchExecution.setStep_name(rs.getString("STEP_NAME"));
              batchExecution.setCommit_count(rs.getLong("COMMIT_COUNT"));
              batchExecution.setEnd_time(rs.getTime("END_TIME"));
              batchExecution.setProcess_skip_count(rs.getLong("PROCESS_SKIP_COUNT"));
              batchExecution.setFilter_count(rs.getLong("FILTER_COUNT"));
              batchExecution.setExit_code(rs.getString("EXIT_CODE"));
              batchExecution.setRead_count(rs.getLong("READ_COUNT"));
              batchExecution.setLast_updated(rs.getTime("LAST_UPDATED"));
              batchExecution.setStatuts(rs.getString("STATUS"));
              batchExecution.setWrite_count(rs.getLong("WRITE_COUNT"));
              batchExecution.setStart_time(rs.getTime("START_TIME"));
              batchExecution.setWrite_skip_count(rs.getLong("WRITE_SKIP_COUNT"));
              batchExecution.setRollback_count(rs.getLong("ROLLBACK_COUNT"));
              batchExecution.setExit_message(rs.getString("EXIT_MESSAGE"));
              return batchExecution;
          }
      });
        return batchExecutions;
    }

    @Override
    public BatchExecution selectStepExecution(Long id) {
        BatchExecution batchExecution = queryForObject(
                "select * from BATCH_STEP_EXECUTION where STEP_EXECUTION_ID = ?",
                new Object[]{1212L},
                new RowMapper<BatchExecution>() {
                    public BatchExecution mapRow(ResultSet rs, int rowNum) throws SQLException {
                        BatchExecution batchExecution1 = new BatchExecution();
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
        return batchExecution;
    }

    @Override
    public Long getLastIDTable(String id,String table) {

            return queryForObject("select MAX ("+id+")from"+table, Long.class);

    }

    @Override
    public BatchExecutionParam selectBatchExecParam(Long id) {
        BatchExecutionParam batchExecutionParam = queryForObject(
                "select first_name, last_name from t_actor where id = ?",
                new Object[]{1212L},
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
        return batchExecutionParam;
    }

    @Override
    public List<BatchExecutionParam> selectAllBatchExecParam() {
        List<BatchExecutionParam> batchExecutionParams = query("select * from BATCH_STEP_EXECUTION", new RowMapper<BatchExecutionParam>() {
            @Override
            public BatchExecutionParam mapRow(ResultSet rs, int rowNum) throws SQLException {
                BatchExecution   batchExecution = new BatchExecution();
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
        return batchExecutionParams;
    }


}



