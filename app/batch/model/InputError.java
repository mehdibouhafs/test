package batch.model;

import batch.model.batch.BatchJobExecution;
import com.avaje.ebean.Model;
import play.data.format.Formats;
import play.data.validation.Constraints;

import javax.persistence.*;
import javax.validation.Constraint;

import java.util.Date;
import java.util.List;

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
    public String filee;

    @Constraints.Required
    public String columne;

    @Constraints.Required
    public String cause;


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

    public static List<InputError> findByJobExecutionId(Long id){
        return  find.where().eq("job_execution_id",id).findList();
    }

    @Override
    public String toString() {
        return "InputError{" +
                "id=" + id +
                ", lineNumber=" + lineNumber +
                ", line='" + line + '\'' +
                ", file='" + filee + '\'' +
                ", columne='" + columne + '\'' +
                ", cause='" + cause + '\'' +
                ", job_execution_id=" + job_execution_id +
                '}';
    }
}
