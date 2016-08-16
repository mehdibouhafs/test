package batch.model;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.JsonIgnore;
import play.data.validation.Constraints;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
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
    public Long id;
    @Constraints.Required
    public String className;

    @OneToMany(mappedBy = "classe")
    @JsonIgnore
    public Collection<Attribute> attributes;

    public static Model.Finder<Long,Classe> find = new Model.Finder(Long.class, Classe.class);


    public Classe() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
