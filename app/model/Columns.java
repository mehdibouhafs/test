package model;

import views.formdata.ParamFormData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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



    /**
     * Create a map of hobby name -> boolean including all known hobbies
     * and setting the boolean to true if a given hobby is associated with the passed student.
     * @param paramFormData A student who may have zero or more hobbies, or null to create a hobby list
     * with all unchecked boxes.
     * @return A map of type names to booleans indicating the hobbies associated with the paramFormData.
     */
    public static Map<String, Boolean> makeTypeMap(ParamFormData paramFormData) {
        Map<String, Boolean> typeMap = new HashMap<String, Boolean>();
        for (Columns column  : allColumns) {
            typeMap.put(column.getName(), (paramFormData == null) ? false : (paramFormData.type !=null && paramFormData.type.equals(column.getName())));
        }
        return typeMap;
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
}


