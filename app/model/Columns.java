package model;

import views.formdata.ParamFormData;

import java.util.*;

/**
 * Created by MBS on 07/07/2016.
 */
public class Columns {

    public int id;
    public String name;
    private List<Columns> allColumns = new ArrayList<>();


    public Columns() {
    }

    public Columns(String name) {
        this.name = name;
    }

    public Columns(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }




    public Columns findType(String columnName) {
        System.out.println("********************typeName :" +  columnName);
        for (Columns column : getAllColumns()) {
            if (columnName.equals(column.getName())) {
                return column;
            }
        }
        return null;
    }

    public List<Columns> getAllColumns() {
        return allColumns;
    }

    public void setAllColumns(List<Columns> allColumns) {
        this.allColumns = allColumns;
    }

    @Override
    public String toString() {
        return String.format("[ id: %s  Column: %s]",this.id ,this.name);
    }

    /** Fake a database of hobbies. */


    /** Instantiate the fake database of hobbies. */



    public Map<String, Boolean> makeColumnMap(ParamFormData paramFormData) {
        Map<String, Boolean> columnMap = new LinkedHashMap<>();
        for (Columns col : getAllColumns()) {
            columnMap.put(col.getName(), (paramFormData != null && find(paramFormData.getRows(),col.getName()) ));
        }
        return columnMap;
    }


    public static boolean find(List<Row> rows,String col){
        if(rows != null) {
            for (Row r : rows
                    ) {
                if (r.getName().equals(col)) {
                    return true;
                }

            }
        }
        return  false;
    }



    public static List<String> getNameList() {
        String[] nameArray = {"String", "Integer", "Date", "VARCHAR", "BOOLEAN"};
        return Arrays.asList(nameArray);
    }
}


