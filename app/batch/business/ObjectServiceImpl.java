package batch.business;

import batch.dao.ObjectDao;

import java.util.Map;

/**
 * Created by MBS on 08/08/2016.
 */

public class ObjectServiceImpl implements ObjectService{

    private ObjectDao objectDao;

    public ObjectServiceImpl(ObjectDao objectDao) {
        this.objectDao = objectDao;
    }

    @Override
    public boolean createTable(String name, Map<String, String> columnsTable) {
        return objectDao.createTable(name,columnsTable);
    }

    @Override
    public boolean dropTable(String name) {
        return objectDao.dropTable(name);
    }

    @Override
    public Map<String, String> dataTable(String table) {
        return dataTable(table);
    }
}
