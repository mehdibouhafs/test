package batch.policy;

import java.io.FileNotFoundException;
import java.util.Date;

import batch.model.InputError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.item.file.FlatFileParseException;


/**
 * Created by MBS on 05/08/2016.
 */
public class FileVerificationSkipper implements SkipPolicy {

	private static final Logger logger = LoggerFactory.getLogger("badRecordLogger");
	private Long job_execution_id;

	public FileVerificationSkipper() {
	}

	@Override
	public boolean shouldSkip(Throwable exception, int skipCount) throws SkipLimitExceededException {
		if (exception instanceof FileNotFoundException) {
			return false;
		} else if (exception instanceof FlatFileParseException && skipCount <= 1000000) {
			FlatFileParseException ffpe = (FlatFileParseException) exception;
			StringBuilder errorMessage = new StringBuilder();
			errorMessage.append("An error occured while processing the " + ffpe.getLineNumber()
					+ " line of the file. Below was the faulty " + "input.\n");
			errorMessage.append(ffpe.getInput() + "\n");
			InputError inputError = new InputError();
			inputError.job_execution_id = job_execution_id;
			inputError.lineNumber = ffpe.getLineNumber();
			inputError.line = ffpe.getInput();
			inputError.messages = ffpe.getMessage();
			inputError.save();
			logger.error("{}", errorMessage.toString());
			return true;
		} else {
			if (exception instanceof ClassCastException && skipCount <= 1000000) {
				ClassCastException cce = (ClassCastException) exception;
				FlatFileParseException ffpe = (FlatFileParseException) exception;
				StringBuilder errorMessage = new StringBuilder();
				errorMessage.append("error parseException");
				//errorMessage.append(ffpe.getInput() + "\n");
				logger.error("{}", errorMessage.toString());
				InputError inputError = new InputError();
				inputError.job_execution_id = job_execution_id;
				inputError.lineNumber = ffpe.getLineNumber();
				inputError.line = ffpe.getInput();
				inputError.messages = exception.getCause().getMessage();
				inputError.save();
				return true;
			} else {
				return false;
			}
		}
	}

	public Long getJob_execution_id() {
		return job_execution_id;
	}

	public void setJob_execution_id(Long job_execution_id) {
		this.job_execution_id = job_execution_id;
	}
}
