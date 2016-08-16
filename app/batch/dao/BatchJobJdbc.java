package batch.dao;

import batch.model.BatchExecution;
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
    public List<BatchExecution> selectAllDetail() {
        System.out.println("SELECT ////////////////////////////////////////////////////////////////////////////////////");
      List<BatchExecution> batchExecutions = query("select VERSION,STEP_NAME from BATCH_STEP_EXECUTION", new RowMapper<BatchExecution>() {
          @Override
          public BatchExecution mapRow(ResultSet rs, int rowNum) throws SQLException {
              System.out.println("RS //////////////////////////////////////////////////////"+rs);
             BatchExecution   batchExecution = new BatchExecution();
              batchExecution.setVersion(rs.getLong("VERSION"));
              batchExecution.setStep_name(rs.getString("STEP_NAME"));
              return batchExecution;
          }
      });
        return batchExecutions;
    }


}



