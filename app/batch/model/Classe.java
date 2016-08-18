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

    @OneToMany
    @JsonIgnore
    public Collection<Attribute> attributes;

    public static Model.Finder<String,Classe> find = new Model.Finder(String.class, Classe.class);


    public Classe() {
    }


    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Collection<Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(Collection<Attribute> attributes) {
        this.attributes = attributes;
    }
}
