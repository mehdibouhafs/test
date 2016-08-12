package batch.model;

import com.avaje.ebean.Model;
import play.data.validation.Constraints;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by MBS on 12/08/2016.
 */
@Entity
@Table(name="A_MBS3_CLASSE")
public class Classe extends Model {
    @Id
    public Long id;
    @Constraints.Required
    public String ClassName;

    @OneToMany
    public Collection<Attribute> attributes;


}
