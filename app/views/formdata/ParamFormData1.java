package views.formdata;

import play.data.validation.ValidationError;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MBS on 13/07/2016.
 */
public class ParamFormData1 {

    public String tableName;

    public List<String> cols;

    //public List<String> columns;

    //public String filePath;

    public List<Integer> id;

    public String typeXML;

    public List<String> primaryKey;

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

    public List<String> getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(List<String> primaryKey) {
        this.primaryKey = primaryKey;
    }

    public String getTypeXML() {
        return typeXML;
    }

    public void setTypeXML(String typeXML) {
        this.typeXML = typeXML;
    }

    public List<Integer> getId() {
        return id;
    }

    public void setId(List<Integer> id) {
        this.id = id;
    }


    public List<String> getCols() {
        return cols;
    }

    public void setCols(List<String> cols) {
        this.cols = cols;
    }

    @Override
    public String toString() {
        return "ParamFormData1{" +
                "tableName='" + tableName + '\'' +
                ", cols=" + cols +
                ", id=" + id +
                ", typeXML='" + typeXML + '\'' +
                ", primaryKey=" + primaryKey +
                ", type=" + type +
                ", size=" + size +
                '}';
    }
}
