package batch.dao;

import batch.model.Attribute;
import batch.model.Reader;
import batch.model.batch.BatchStepExecution;
import batch.model.batch.BatchExecutionParam;

import java.util.List;
import java.util.Map;

/**
 * Created by MBS on 16/08/2016.
 */
public interface BatchJobDao {
    public boolean createTableOracle(Reader reader, List<Attribute> attributes);
    public List<String> getCommentaires(String table,List<Attribute> attributes);
    public String buildCdata(Reader reader,List<Attribute> attributes);
    public boolean dropTable(String tableName);
    public Map<String,String > dataTable(String table);
    public Boolean executer(String query);
}
