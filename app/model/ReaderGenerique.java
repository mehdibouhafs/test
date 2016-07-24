package model;

import com.avaje.ebean.Model;

import javax.persistence.Entity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by MBS on 05/07/2016.
 */

public class ReaderGenerique extends Model {

    private int id;

    private String[] columns;//separe par , id,firstName,lastName,date

    //private Object person;

    private int lineToSkip;

    private String filePath;

    private String table;

    private Class<?> classs = Object.class;

    public Map<String,String> columnsTable;

    public  String cData ;


    private Map<String, Class<?>> properties;

    public ReaderGenerique() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getcData() {
        return cData;
    }

    public void setcData(String cData) {
        this.cData = cData;
    }

    public String[] getColumns() {
        return columns;
    }

    public void setColumns(String[] columns) {
        this.columns = columns;
    }

    public int getLineToSkip() {
        return lineToSkip;
    }

    public void setLineToSkip(int lineToSkip) {
        this.lineToSkip = lineToSkip;
    }

    public Class<?> getClasss() {
        return classs;
    }

    public void setClasss(Class<?> classs) {
        classs = classs;
    }

    public Map<String, Class<?>> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Class<?>> properties) {
        this.properties = properties;
    }

    public Map<String, String> getColumnsTable() {
        return columnsTable;
    }

    public void setColumnsTable(Map<String, String> columnsTable) {
        this.columnsTable = columnsTable;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
