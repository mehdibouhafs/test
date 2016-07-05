package model;

import com.avaje.ebean.Model;

import javax.persistence.Entity;
import java.util.HashMap;
import java.util.List;

/**
 * Created by MBS on 05/07/2016.
 */

@Entity
public class ReaderGenerique extends Model {

    private int id;

    private List<String> firstLine;//separe par , id,firstName,lastName,date

    //private Object person;

    private int lineToSkip;

    private HashMap<String,Object> values = new HashMap<>();

    public ReaderGenerique() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<String> getFirstLine() {
        return firstLine;
    }

    public void setFirstLine(List<String> firstLine) {
        this.firstLine = firstLine;
    }

    public int getLineToSkip() {
        return lineToSkip;
    }

    public void setLineToSkip(int lineToSkip) {
        this.lineToSkip = lineToSkip;
    }
}
