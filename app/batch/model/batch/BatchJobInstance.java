package batch.model.batch;

import com.avaje.ebean.Model;
import org.springframework.batch.core.JobInstance;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by MBS on 16/08/2016.
 */
@Entity
@Table(name="BATCH_JOB_INSTANCE")
public class BatchJobInstance extends Model{

    @Id
    public Long job_instance_id;
    public Long version;
    public String job_name;
    public String job_key;


    public static Model.Finder<Long,BatchJobInstance> find = new Model.Finder(Long.class, BatchJobInstance.class);




}
