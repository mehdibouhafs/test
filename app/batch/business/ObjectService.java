package batch.business;

import java.util.Map;

/**
 * Created by MBS on 08/08/2016.
 */
public interface ObjectService {
    public boolean createTable(String name, Map<String,String> columnsTable);
    public boolean dropTable(String name);
    public Map<String,String > dataTable(String table);
}
