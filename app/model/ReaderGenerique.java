package model;

import com.avaje.ebean.Model;

import javax.persistence.Entity;
import java.util.HashMap;
import java.util.List;

/**
 * Created by MBS on 05/07/2016.
 */

public class ReaderGenerique extends Model {

    private int id;

    private Rows firstLine;//separe par , id,firstName,lastName,date

    //private Object person;

    private int lineToSkip;


    public ReaderGenerique() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Rows getFirstLine() {
        return firstLine;
    }

    public void setFirstLine(Rows firstLine) {
        this.firstLine = firstLine;
    }

    public int getLineToSkip() {
        return lineToSkip;
    }

    public void setLineToSkip(int lineToSkip) {
        this.lineToSkip = lineToSkip;
    }
}