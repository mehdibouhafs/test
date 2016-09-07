package batch.model;

import batch.model.batch.BatchJobExecution;
import batch.model.batch.BatchStepExecution;
import com.avaje.ebean.Model;

import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by MBS on 18/08/2016.
 */
public class Resume extends Model {

    private List<InputError> inputError;
    private BatchStepExecution batchStepExecution;
    private User user;
    private Reader reader;
    private List<Attribute> attributes;
    @Transient
   private String error;

    public Resume() {
    }



    public BatchStepExecution getBatchStepExecution() {
        return batchStepExecution;
    }

    public void setBatchStepExecution(BatchStepExecution batchStepExecution) {
        this.batchStepExecution = batchStepExecution;
    }

    public List<InputError> getInputError() {
        return inputError;
    }

    public void setInputError(List<InputError> inputError) {
        this.inputError = inputError;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Reader getReader() {
        return reader;
    }

    public void setReader(Reader reader) {
        this.reader = reader;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
