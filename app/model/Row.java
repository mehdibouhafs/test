package model;

import com.avaje.ebean.Model;

import javax.persistence.Entity;

/**
 * Created by MBS on 06/07/2016.
 */

public class Row extends Model {

    public String name;

    public String type;

    public String size;

    public boolean selected;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }



    @Override
    public String toString() {
        return "Row{" +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", size=" + size +
                ", selected=" + selected +
                '}';
    }
}
