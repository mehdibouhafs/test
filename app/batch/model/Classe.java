package batch.model;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.JsonIgnore;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Created by MBS on 12/08/2016.
 */
@Entity
@Table(name="A_MBS3_CLASSE")
public class Classe extends Model {

    @Id
    @Constraints.Required
    @Column(unique=true)
    public String className;
    public String user_email;
    public int viewed;


    @Transient
    public int nbNotViewed;



    public static Model.Finder<String,Classe> find = new Model.Finder(String.class, Classe.class);


    public static void dropClasseAndAttribute(String className){
        Attribute.dropInvolving(className);
        find.byId(className).delete();
    }

    public static  boolean isExistClasse(String classeName){
        Classe  classe= find.byId(classeName);
        if(classe !=null){
            return true;
        }else{
            return false;
        }
    }



    public Classe() {
       this.viewed = 0;
    }


    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

}
