package batch.model.batch;

import com.avaje.ebean.Model;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by MBS on 16/08/2016.
 */
@Entity
@Table(name="BATCH_JOB_EXECUTION")
public class BatchJobExecution extends Model{

    @Id
    public Long job_execution_id;
    public Long version;
    public Long job_instance_id;
    public Date create_time;
    public Date start_time;
    public Date end_time;
    public String status;
    public String exit_code;
    public String exit_message;
    public Date last_updated;
    public String job_configuration_location;

    public static Model.Finder<Long,BatchJobExecution> find = new Model.Finder(Long.class, BatchJobExecution.class);


    @Override
    public String toString() {
        return "JobExecution{" +
                "job_execution_id=" + job_execution_id +
                ", version=" + version +
                ", jobInstance=" + job_instance_id +
                ", create_time=" + create_time +
                ", start_time=" + start_time +
                ", end_time=" + end_time +
                ", status='" + status + '\'' +
                ", exit_code='" + exit_code + '\'' +
                ", exit_message='" + exit_message + '\'' +
                ", last_updated=" + last_updated +
                ", job_configuration_location='" + job_configuration_location + '\'' +
                '}';
    }
}
