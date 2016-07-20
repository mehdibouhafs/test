package views.formdata;

import java.util.List;

/**
 * Created by MBS on 13/07/2016.
 */
public class ParamFormData1 {

    public String tableName;

    public List<String> type;

    public List<String> size;


    public ParamFormData1() {
    }

    public ParamFormData1(List<String> type, String tableName) {
        this.type = type;
        this.tableName = tableName;
    }

    public List<String> getSize() {
        return size;
    }

    public void setSize(List<String> size) {
        this.size = size;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<String> getType() {
        return type;
    }

    public void setType(List<String> type) {
        this.type = type;
    }

    public ParamFormData1(String tableName) {
        this.tableName = tableName;
    }
}
