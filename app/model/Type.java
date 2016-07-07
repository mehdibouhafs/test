package model;

import views.formdata.ParamFormData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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



    /**
     * Create a map of hobby name -> boolean including all known hobbies
     * and setting the boolean to true if a given hobby is associated with the passed student.
     * @param paramFormData A student who may have zero or more hobbies, or null to create a hobby list
     * with all unchecked boxes.
     * @return A map of type names to booleans indicating the hobbies associated with the paramFormData.
     */
    public static Map<String, Boolean> makeTypeMap(ParamFormData paramFormData) {
        Map<String, Boolean> typeMap = new HashMap<String, Boolean>();
        for (Type type  : allTypes) {
            typeMap.put(type.getName(), (paramFormData == null) ? false : (paramFormData.type !=null && paramFormData.type.equals(type.getName())));
        }
        return typeMap;
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
