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

	@Override
	public boolean shouldSkip(Throwable exception, int skipCount) throws SkipLimitExceededException {
		if (exception instanceof FileNotFoundException) {
			return false;
		} else if (exception instanceof FlatFileParseException && skipCount <= 10) {
			FlatFileParseException ffpe = (FlatFileParseException) exception;
			StringBuilder errorMessage = new StringBuilder();
			errorMessage.append("An error occured while processing the " + ffpe.getLineNumber()
					+ " line of the file. Below was the faulty " + "input.\n");
			errorMessage.append(ffpe.getInput() + "\n");
			InputError inputError  = new InputError();
			inputError.date = new Date();
			inputError.lineNumber=ffpe.getLineNumber();
			inputError.line = ffpe.getInput();
			inputError.save();
			logger.error("{}", errorMessage.toString());
			return true;
		} /*else {
			if (exception instanceof ClassCastException && skipCount <= 10) {
				ClassCastException cce = (ClassCastException) exception;
				StringBuilder errorMessage = new StringBuilder();
				errorMessage.append("error parseException");
				//errorMessage.append(ffpe.getInput() + "\n");
				logger.error("{}", errorMessage.toString());
				return true;
			} */else {
				return false;
			}

	}

}
