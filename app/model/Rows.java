package model;

import com.avaje.ebean.Model;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by MBS on 06/07/2016.
 */

public class Rows extends Model {

    public String table;

    public List<Row> rows = new ArrayList<>();


    public Rows(List<Row> rows, String table) {
        this.rows = rows;
        this.table = table;
    }

    public Rows() {
    }

    public List<Row> getRow() {
        return rows;
    }

    public void setRow(List<Row> row) {
        this.rows = row;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }


    public String afficher(){
        String s = "";
        for (Row r:rows
             ) {
            s = s+r.toString();
        }
        return s;
    }

    @Override
    public String toString() {
        return "Rows{" +
                "table='" + table + '\''+
                afficher()+
                '}';
    }
}
