package model;

import com.avaje.ebean.Model;
import views.formdata.ParamFormData;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by MBS on 06/07/2016.
 */

public class Row extends Model {

    public Long id;

    public String name;

    public String type;


    public boolean selected;


    public Row() {
    }

    public Row(Long id, String name, String type, boolean selected) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.selected = selected;
    }

    public Row(String name) {
        this.name = name;
    }


    public Object getType() {
        return type;
    }

    public boolean isSelected() {
        return selected;
    }

    public Long getId() {
        return id;
    }


    public void setSelected(boolean selected) {
        this.selected = selected;
    }


    public static List<Row> makeInstance(ParamFormData formData) {
        List<Row> rows =  formData.getRows();
        for (Row r : rows) {
            allRowss.add(r);
        }
        return rows;
    }


    public static ParamFormData makeRowFormData(long id) {
        List<Row> list = new ArrayList<>();
        for (Row row : allRowss) {
            if (row.getId() == id) {
                list.add(row);
                return new ParamFormData(list);
            }
        }
        throw new RuntimeException("Couldn't find Row");
    }

    @Override
    public String toString() {
        return "Row{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", selected=" + selected +
                '}';
    }

    /** Fake a database of students. */
    private static List<Row> allRowss = new ArrayList<>();

    /** Populate the fake database with both valid and invalid students, just for tutorial purposes.*/
    static {
        // Valid student. No optional data supplied.
        allRowss.add(new Row(1L,"id","Integer" ,true));
        allRowss.add(new Row(2L,"name","String" ,true));
        // Valid student with optional data.
        //getById(2L).addHobby(Hobby.findHobby("Biking"));
        //getById(2L).addHobby(Hobby.findHobby("Surfing"));
        //getById(2L).addMajor(Major.findMajor("Chemistry"));
        //getById(2L).addMajor(Major.findMajor("Physics"));
        // Invalid student. Password is too short.
        //allRowss.add(new Student(3L, "Frank Bad", "pass", GradeLevel.findLevel("Freshman"), GradePointAverage.findGPA("4.0")));
    }

    /**
     * Find a student instance given the ID.
     * @param id The id of the student.
     * @return The Row instance, or throws a RuntimeException.
     */
    public static Row getById(long id) {
        for (Row row : allRowss) {
            if (row.getId() == id) {
                return row;
            }
        }
        throw new RuntimeException("Couldn't find student");
    }

    public void setType(String type) {
        this.type = type;
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

    public static List<Row> getAllRowss() {
        return allRowss;
    }

    public static void setAllRowss(List<Row> allRowss) {
        Row.allRowss = allRowss;
    }

}
