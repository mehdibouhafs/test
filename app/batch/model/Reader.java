package batch.model;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by MBS on 19/08/2016.
 */

@Entity
@Table(name="a_mbs3_reader")
public class Reader extends Model  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    public String filePath;
    public String separator;
    public int nbLineToSkip;
    @Transient
    @JsonIgnore
    public List<String> cols;
    @Transient
    @JsonIgnore
    public String packageClasse;
    @Transient
    @JsonIgnore
    public Class<?> classeGenerate = Object.class;
    public String classeName;
    public String columns;
    public String emailUser;
    public Date dateCreation;
    public Date dateLancement;
    public String tableName;
    public boolean executed;
    public boolean resultat;
    public String executed_by;
    public Long jobId;
    public Long nbLinesSuccess;
    public Long nbLinesFailed;


    public static Finder<Long, Reader> find = new Finder<Long, Reader>(Reader.class);

    public Reader() {
        executed = false;
        resultat = false;
        nbLinesFailed = 0L;
        nbLinesSuccess = 0L;
    }

    public static Long maxId(){
        System.out.println("find row count"  + find.findRowCount());
        Reader reader = find.orderBy("id").findList().get(find.orderBy("id").findList().size()-1);
        return reader.id;
    }




    public static Reader getbyClasse(String classeName){
        return find.where().eq("classeName",classeName).findUnique();
    }

    public static  List<Reader> getReaderCSV(){
        List<Reader> readers = new ArrayList<>();

        for(Reader reader : find.all()){
            if(reader.filePath.contains(".csv")){
                readers.add(reader);
            }
        }
        return  readers;
    }

    public static  List<Reader> getReaderXMl(){
        List<Reader> readers = new ArrayList<>();

        for(Reader reader : find.all()){
            if(reader.filePath.contains(".xml")){
                readers.add(reader);
            }
        }
        return  readers;
    }



    public static List<Reader> getByUser(String email){
        return find.where().eq("email_user",email).findList();
    }

    public static List<Reader> getByUserAndCompleted(String email){
        return find.where().eq("email_user",email).eq("resultat",1).findList();
    }

    public static List<Reader> getByUserAndNotCompleted(String email){
        return find.where().eq("email_user",email).eq("resultat",0).eq("date_lancement",null).findList();
    }






    public static List<Attribute> getAttributeByReader(Long id){
        Reader reader = Reader.find.byId(id);
        return  Attribute.findInvolving(reader.classeName);
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
                "dateCreation=" + dateCreation +
                '}';
    }
}
