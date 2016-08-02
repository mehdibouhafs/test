package views.formdata;

import java.util.List;

/**
 * Created by bouhafs on 24/07/2016.
 */
public class ParamFormData3 {

    public String table;

    public List<String> type;

    public List<String> size;

    public String dropeTable;

    public ParamFormData3() {
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

    public String getDropeTable() {
        return dropeTable;
    }

    public void setDropeTable(String dropeTable) {
        this.dropeTable = dropeTable;
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
