package batch.model;

import batch.model.batch.BatchJobExecution;
import batch.model.batch.BatchStepExecution;
import com.avaje.ebean.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MBS on 18/08/2016.
 */
public class Resume extends Model {

    private List<InputError> inputError;
    private BatchStepExecution batchStepExecution;
    private Double time;
    private User user;



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


    public Double getTime() {
        return time;
    }

    public void setTime(Double time) {
        this.time = time;
    }
}
