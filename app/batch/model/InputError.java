package batch.model;

import batch.model.batch.BatchJobExecution;
import com.avaje.ebean.Model;
import play.data.format.Formats;
import play.data.validation.Constraints;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.Constraint;

import java.util.Date;

/**
 * Created by bouhafs on 11/08/2016.
 */

@Entity
@Table(name = "A_MBS_INPUT_ERRORS")
public class InputError extends Model {

    @Id
    public Long id;

    @Constraints.Required
    public int lineNumber;

    @Constraints.Required
    public String line;


    @Constraints.Required
    public String messages;

    /*@Formats.DateTime(pattern="dd/MM/yyyy hh:mm:ss")
    public Date datee;*/

    public Long job_execution_id;

    public InputError(Long id, int lineNumber, String line,Long job_execution_id) {
        this.id = id;
        this.lineNumber = lineNumber;
        this.line = line;
        this.job_execution_id = job_execution_id;
    }

    public InputError() {
    }

    public static Finder<Long, InputError> find = new Finder<Long,InputError>(InputError.class);


}
