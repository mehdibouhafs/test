package batch.model;

import com.avaje.ebean.Model;

import javax.persistence.*;
import java.util.List;

/**
 * Created by MBS on 19/08/2016.
 */

@Entity
@Table(name="a_mbs3_reader")
public class Reader extends Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    public String filePath;
    public String separator;
    public int nbLineToSkip;
    @Transient
    public List<String> cols;
    @Transient
    public String fragmentRootName;
    @Transient
    public String packageClasse;
    @Transient
    public Class<?> classeGenerate = Object.class;

    public String classeName;

    @Transient
    public String cData;
    public String columns;
    public Long idAttribute;
    public String emailUser;





    public static Finder<Long, Reader> find = new Finder<Long, Reader>(Reader.class);

    public Reader() {
    }

    public static Reader getReaderUser(String email) {
        //return find.where().eq("email", email).eq("password", password).findUnique();
        System.out.println("GetReaderUser  = " + email);
        Reader reader = Reader.find.where().eq("EMAIL_USER", email).findUnique();
        if (reader != null) {
            return reader;
        } else {
            return null;
        }
    }






    @Override
    public String toString() {
        return "Reader{" +
                "id=" + id +
                ", filePath='" + filePath + '\'' +
                ", separator='" + separator + '\'' +
                ", nbLineToSkip=" + nbLineToSkip +
                ", cols=" + cols +
                ", idAttribute=" + idAttribute +
                ", emailUser='" + emailUser + '\'' +
                '}';
    }
}
