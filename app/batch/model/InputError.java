package batch.model;

import com.avaje.ebean.Model;
import net.sf.ehcache.config.PersistenceConfiguration;
import play.data.format.Formats;
import play.data.validation.Constraints;

import javax.persistence.Entity;
import javax.persistence.Id;

import java.util.Date;

/**
 * Created by bouhafs on 11/08/2016.
 */

@Entity
public class InputError extends Model {

    @Id
    public Long id;

    @Constraints.Required
    public int lineNumber;

    @Constraints.Required
    public String line;


    @Formats.DateTime(pattern="dd/MM/yyyy hh:mm:ss")
    public Date date;

    public static Finder<Long, InputError> find = new Finder<Long,InputError>(InputError.class);


}
