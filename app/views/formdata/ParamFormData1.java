package views.formdata;

import play.data.validation.ValidationError;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MBS on 13/07/2016.
 */
public class ParamFormData1 {

    public String table;

    public List<String> type;

    public List<String> size;

    public ParamFormData1() {
        table="";
    }


    public List<String> getSize() {
        return size;
    }

    public void setSize(List<String> size) {
        this.size = size;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
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
                "table='" + table + '\'' +
                ", type=" + type +
                ", size=" + size +
                '}';
    }
}
