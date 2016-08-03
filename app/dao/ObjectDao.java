package dao;

import java.util.List;
import java.util.Map;

/**
 * Created by MBS on 20/07/2016.
 */


public interface ObjectDao {
    public boolean createTable(String name, Map<String,String> columnsTable);
    public boolean dropTable(String name);
    public Map<String,String > dataTable(String table);
}
