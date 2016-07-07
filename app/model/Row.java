package model;

import com.avaje.ebean.Model;
import views.formdata.ParamFormData;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by MBS on 06/07/2016.
 */

public class Row extends Model {

    public Long id;

    public Columns name;

    public Type type;


    public boolean selected;


    public Row() {
    }

    public Row(Long id, Columns name, Type type, boolean selected) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.selected = selected;
    }

    @Override
    public String toString() {
        return String.format("[Row name: '%s' type: '%s' selected: %s]", this.getName(),
                this.getType(), this.isSelected());
    }

    public Columns getName() {
        return name;
    }

    public void setName(Columns name) {
        this.name = name;
    }

    public Object getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
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


    public static Rows makeInstance(ParamFormData formData) {
        Rows rows = new Rows();
        Row row = new Row();
        row.name =  Columns.findType(formData.name);
        row.type = Type.findType(formData.type);
        row.selected = Boolean.valueOf(formData.bools);
        rows.setTable(formData.tableName);
        rows.getRow().add(row);
        return rows;
    }


    public static ParamFormData makeRowFormData(long id) {
        for (Row row : allRowss) {
            if (row.getId() == id) {
                return new ParamFormData(row.name, row.type, row.selected);
            }
        }
        throw new RuntimeException("Couldn't find student");
    }




    /** Fake a database of students. */
    private static List<Row> allRowss = new ArrayList<>();

    /** Populate the fake database with both valid and invalid students, just for tutorial purposes.*/
    static {
        // Valid student. No optional data supplied.
        allRowss.add(new Row(1L,Columns.findType("id"),Type.findType("Integer") ,true));
        allRowss.add(new Row(2L,Columns.findType("firstName"),Type.findType("String"),true));
        allRowss.add(new Row(3L,Columns.findType("lastName"),Type.findType("String"),true));
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


}
