package model;

import views.formdata.ParamFormData;

import java.util.*;

/**
 * Created by MBS on 07/07/2016.
 */
public class Type {

    public Long id;
    public String name;


    public Type() {
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

    public Type(Long id, String name) {
        this.id = id;
        this.name = name;
    }



   public static Map<String, Boolean> makeTypeMap(ParamFormData paramFormData) {
        Map<String, Boolean> typeMap = new LinkedHashMap<>();
        for (Type type  : allTypes) {
            typeMap.put(type.getName(), (paramFormData != null &&  find(paramFormData.getRows(),type.getName())));
        }
        return typeMap;
    }

    public static boolean find(List<Row> rows,String type){
        if(rows != null) {
            for (Row r : rows
                    ) {
                if (r.getType().equals(type)) {
                    return true;
                }
            }
        }
        return  false;
    }

    public static Type findType(String typeName) {
        System.out.println("********************typeName :" +  typeName);
        for (Type type : allTypes) {
            if (typeName.equals(type.getName())) {
                return type;
            }
        }
        return null;
    }


    public static List<Type> getAllTypes() {
        return allTypes;
    }

    public static void setAllTypes(List<Type> allTypes) {
        Type.allTypes = allTypes;
    }

    @Override
    public String toString() {
        return String.format("[Type %s]", this.name);
    }

    /** Fake a database of hobbies. */
    private static List<Type> allTypes = new ArrayList<>();

    /** Instantiate the fake database of hobbies. */
    static {
        allTypes.add(new Type(1L, "String"));
        allTypes.add(new Type(2L, "Integer"));
        allTypes.add(new Type(3L, "Boolean"));
        allTypes.add(new Type(4L, "Date"));
    }
}
