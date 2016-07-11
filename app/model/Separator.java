package model;

import views.formdata.ParamFormData;

import java.util.*;

/**
 * Created by MBS on 11/07/2016.
 */
public class Separator  {
    public Long id;
    public char name;

    public Separator() {
    }

    public Separator(char name) {
        this.name = name;
    }

    public Separator(char name, Long id) {
        this.name = name;
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public char getName() {
        return name;
    }

    public void setName(char name) {
        this.name = name;
    }

    public Separator (Long id, char name) {
        this.id = id;
        this.name = name;
    }



    public static Separator findType(char separator) {
        System.out.println("********************Type Separator :" +  separator);
        for (Separator separator1 : allSeparators) {
            if (separator1.getName() == separator) {
                return separator1;
            }
        }
        return null;
    }

    public static List<Separator> getAllColumns() {
        return allSeparators;
    }

    public static void setAllSeparators(List<Separator> allSeparators) {
        Separator.allSeparators = allSeparators;
    }

    @Override
    public String toString() {
        return String.format("[ id: %s  Column: %s]",this.id ,this.name);
    }

    /** Fake a database of hobbies. */
    private static List<Separator> allSeparators = new ArrayList<>();

    /** Instantiate the fake database of hobbies. */
    static {
        allSeparators.add(new Separator(1L, ','));
        allSeparators.add(new Separator(2L, ';'));
        allSeparators.add(new Separator(3L, '|'));
        allSeparators.add(new Separator(4L, '?'));
    }


    public static Map<Character, Boolean> makeSeparatorMap(ParamFormData paramFormData) {
        Map<Character, Boolean> columnMap = new LinkedHashMap<>();
        for (Separator sep : allSeparators) {
            columnMap.put(sep.getName(), (paramFormData != null && paramFormData.getSeparator()== sep.getName()));
        }
        return columnMap;
    }

    public static List<Separator> getAllSeparators() {
        return allSeparators;
    }

    public static List<String> getNameList() {
        String[] nameArray = {"String", "Integer", "Date", "VARCHAR", "BOOLEAN"};
        return Arrays.asList(nameArray);
    }
}

