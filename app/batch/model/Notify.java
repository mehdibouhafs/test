package batch.model;

import com.avaje.ebean.Model;
import play.data.validation.Constraints;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.List;

/**
 * Created by MBS on 16/08/2016.
 */

@Entity
@Table(name="A_MBS3_Notify")
public class Notify extends Model {

    @Id
    public Long id;

    @Constraints.Required
    public String message;

    @ManyToMany
    public List<User> user;


    public Boolean viewed;

}
