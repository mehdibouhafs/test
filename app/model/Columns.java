package model;

import views.formdata.ParamFormData;

import java.util.*;

/**
 * Created by MBS on 07/07/2016.
 */
public class Columns {

    public Long id;
    public String name;


    public Columns() {
    }

    public Columns(String name) {
        this.name = name;
    }

    public Columns(String name, Long id) {
        this.name = name;
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Columns (Long id, String name) {
        this.id = id;
        this.name = name;
    }



    public static Columns findType(String columnName) {
        System.out.println("********************typeName :" +  columnName);
        for (Columns column : allColumns) {
            if (columnName.equals(column.getName())) {
                return column;
            }
        }
        return null;
    }

    public static List<Columns> getAllColumns() {
        return allColumns;
    }

    public static void setAllColumns(List<Columns> allColumns) {
        Columns.allColumns = allColumns;
    }

    @Override
    public String toString() {
        return String.format("[ id: %s  Column: %s]",this.id ,this.name);
    }

    /** Fake a database of hobbies. */
    private static List<Columns> allColumns = new ArrayList<>();

    /** Instantiate the fake database of hobbies. */
    static {
        allColumns.add(new Columns(1L, "id"));
        allColumns.add(new Columns(2L, "firstName"));
        allColumns.add(new Columns(3L, "lastName"));
        allColumns.add(new Columns(4L, "date"));
    }


    public static Map<String, Boolean> makeColumnMap(ParamFormData paramFormData) {
        Map<String, Boolean> columnMap = new LinkedHashMap<>();
        for (Columns col : allColumns) {
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


