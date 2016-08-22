package batch.model.batch;

import com.avaje.ebean.Model;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by MBS on 16/08/2016.
 */

@Entity
@Table(name = "BATCH_STEP_EXECUTION")
public class BatchStepExecution extends Model{
    @Id
    private Long step_execution_id;
    private Long version;
    private String step_name;
    private String exit_message;
    private Long job_execution_id;
    private Date start_time;
    private Date end_time;
    private String status;
    private Long commit_count;
    private Long read_count;
    private Long filter_count;
    private Long write_count;
    private Long read_skip_count;
    private Long write_skip_count;
    private Long process_skip_count;
    private Long rollback_count;
    private String exit_code;
    private Date last_updated;


    public static BatchStepExecution findByJobExecID(Long job_instance_id){
        return  find.where().eq("job_execution_id",job_instance_id).findUnique();
    }




    public static Model.Finder<Long,BatchStepExecution> find = new Model.Finder(Long.class, BatchStepExecution.class);


    public BatchStepExecution() {
    }

    public Long getStep_execution_id() {
        return step_execution_id;
    }

    public void setStep_execution_id(Long step_execution_id) {
        this.step_execution_id = step_execution_id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getStep_name() {
        return step_name;
    }

    public void setStep_name(String step_name) {
        this.step_name = step_name;
    }

    public Long getJob_execution_id() {
        return job_execution_id;
    }

    public void setJob_execution_id(Long job_execution_id) {
        this.job_execution_id = job_execution_id;
    }

    public Date getStart_time() {
        return start_time;
    }

    public void setStart_time(Date start_time) {
        this.start_time = start_time;
    }

    public Date getEnd_time() {
        return end_time;
    }

    public void setEnd_time(Date end_time) {
        this.end_time = end_time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getCommit_count() {
        return commit_count;
    }

    public void setCommit_count(Long commit_count) {
        this.commit_count = commit_count;
    }

    public Long getRead_count() {
        return read_count;
    }

    public void setRead_count(Long read_count) {
        this.read_count = read_count;
    }

    public Long getFilter_count() {
        return filter_count;
    }

    public void setFilter_count(Long filter_count) {
        this.filter_count = filter_count;
    }

    public Long getWrite_count() {
        return write_count;
    }

    public void setWrite_count(Long write_count) {
        this.write_count = write_count;
    }

    public Long getRead_skip_count() {
        return read_skip_count;
    }

    public void setRead_skip_count(Long read_skip_count) {
        this.read_skip_count = read_skip_count;
    }

    public Long getWrite_skip_count() {
        return write_skip_count;
    }

    public void setWrite_skip_count(Long write_skip_count) {
        this.write_skip_count = write_skip_count;
    }

    public Long getProcess_skip_count() {
        return process_skip_count;
    }

    public void setProcess_skip_count(Long process_skip_count) {
        this.process_skip_count = process_skip_count;
    }

    public Long getRollback_count() {
        return rollback_count;
    }

    public void setRollback_count(Long rollback_count) {
        this.rollback_count = rollback_count;
    }

    public String getExit_code() {
        return exit_code;
    }

    public void setExit_code(String exit_code) {
        this.exit_code = exit_code;
    }

    public Date getLast_updated() {
        return last_updated;
    }

    public void setLast_updated(Date last_updated) {
        this.last_updated = last_updated;
    }

    public String getExit_message() {
        return exit_message;
    }

    public void setExit_message(String exit_message) {
        this.exit_message = exit_message;
    }

    @Override
    public String toString() {
        return "BatchExecution{" +
                "step_execution_id=" + step_execution_id +
                ", version=" + version +
                ", step_name='" + step_name + '\'' +
                ", job_execution_id=" + job_execution_id +
                ", start_time=" + start_time +
                ", end_time=" + end_time +
                ", statuts='" + status + '\'' +
                ", commit_count=" + commit_count +
                ", read_count=" + read_count +
                ", filter_count=" + filter_count +
                ", write_count=" + write_count +
                ", read_skip_count=" + read_skip_count +
                ", write_skip_count=" + write_skip_count +
                ", process_skip_count=" + process_skip_count +
                ", rollback_count=" + rollback_count +
                ", exit_code='" + exit_code + '\'' +
                ", last_updated=" + last_updated +
                '}';
    }
}
