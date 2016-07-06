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

    public List<Row> row = new ArrayList<>();


    public List<Row> getRow() {
        return row;
    }

    public void setRow(List<Row> row) {
        this.row = row;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }



    public String afficher(){
        String s = "";
        for (Row r:row
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
