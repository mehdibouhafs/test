package batch.dao;

import batch.model.Attribute;
import batch.model.batch.BatchStepExecution;
import batch.model.batch.BatchExecutionParam;

import java.util.List;
import java.util.Map;

/**
 * Created by MBS on 16/08/2016.
 */
public interface BatchJobDao {
    public boolean createTableOracle(String name, Map<String,String> columnsTable,List<Attribute> attributes);
    public boolean dropTable(String name);
    public Map<String,String > dataTable(String table);
    public String getCdata();
}
