package views.formdata;

import play.data.validation.ValidationError;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MBS on 13/07/2016.
 */
public class ParamFormData1 {

    public String tableName;

    public List<String> type;

    public List<String> size;




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

    @Override
    public String toString() {
        return "ParamFormData1{" +
                "table='" + tableName + '\'' +
                ", type=" + type +
                ", size=" + size +
                '}';
    }
}
