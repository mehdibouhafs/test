package batch.model.batch;

import com.avaje.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by MBS on 16/08/2016.
 */
@Entity
@Table(name="BATCH_JOB_EXECUTION_PARAMS")
public class BatchExecutionParam extends Model {
   @Id
    private  Long job_execution_id;
    private String type_cd;
    private String key_name;
    private String string_val;
    private Date date_val;
    private Long long_val;
    private Double double_val;
    private char identifying;

    public static Model.Finder<Long,BatchExecutionParam> find = new Model.Finder(Long.class, BatchExecutionParam.class);

    public BatchExecutionParam() {
    }


    public char getIdentifying() {
        return identifying;
    }

    public void setIdentifying(char identifying) {
        this.identifying = identifying;
    }

    public String getType_cd() {
        return type_cd;
    }

    public void setType_cd(String type_cd) {
        this.type_cd = type_cd;
    }

    public String getKey_name() {
        return key_name;
    }

    public void setKey_name(String key_name) {
        this.key_name = key_name;
    }

    public String getString_val() {
        return string_val;
    }

    public void setString_val(String string_val) {
        this.string_val = string_val;
    }

    public Date getDate_val() {
        return date_val;
    }

    public void setDate_val(Date date_val) {
        this.date_val = date_val;
    }

    public Long getLong_val() {
        return long_val;
    }

    public void setLong_val(Long long_val) {
        this.long_val = long_val;
    }

    public Double getDouble_val() {
        return double_val;
    }

    public void setDouble_val(Double double_val) {
        this.double_val = double_val;
    }

    public Long getJob_execution_id() {
        return job_execution_id;
    }

    public void setJob_execution_id(Long job_execution_id) {
        this.job_execution_id = job_execution_id;
    }

    @Override
    public String toString() {
        return "BatchExecutionParam{" +
                "job_execution_id=" + job_execution_id +
                ", type_cd='" + type_cd + '\'' +
                ", key_name='" + key_name + '\'' +
                ", string_val='" + string_val + '\'' +
                ", date_val=" + date_val +
                ", long_val=" + long_val +
                ", double_val=" + double_val +
                ", identifying=" + identifying +
                '}';
    }
}
