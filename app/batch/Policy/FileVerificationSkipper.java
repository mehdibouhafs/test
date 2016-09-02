package batch.policy;

import java.io.FileNotFoundException;
import java.util.Date;

import batch.model.InputError;
import batch.model.Reader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.context.ApplicationContext;
import running.Global;


/**
 * Created by MBS on 05/08/2016.
 */
public class FileVerificationSkipper implements SkipPolicy {

	private static final Logger logger = LoggerFactory.getLogger("badRecordLogger");
	private Long job_execution_id;
	private Long readerId;

	public FileVerificationSkipper() {
	}

	@Override
	public boolean shouldSkip(Throwable exception, int skipCount) throws SkipLimitExceededException {
		ApplicationContext context = Global.getApplicationContext();
		InputError inputError = new InputError();
		if (exception instanceof FileNotFoundException) {
			return false;
		} else if (exception instanceof FlatFileParseException && skipCount <= 1000000) {
			FlatFileParseException ffpe = (FlatFileParseException) exception;
			StringBuilder errorMessage = new StringBuilder();
			errorMessage.append("An error occured while processing the " + ffpe.getLineNumber()
					+ " line of the file. Below was the faulty " + "input.\n");
			errorMessage.append(ffpe.getInput() + "\n");
			inputError.job_execution_id = job_execution_id;
			inputError.lineNumber = ffpe.getLineNumber();
			inputError.line = ffpe.getInput();
			inputError.filee = Reader.find.byId(readerId).filePath;
			//System.out.println("mehdi " + exception.getCause());
			String columne = exception.getCause().toString().split("property")[2].split(";")[0].split("'")[1];
			inputError.columne = columne;
			//String cause = exception.getCause().toString().split("default message")[2].split(";")[0];

			System.out.println("job id " + job_execution_id);
			Reader reader = Reader.find.byId(readerId);
			System.out.println("reader Skip policy" +  reader);
			String[] header = reader.columns.split(",");

			System.out.println("header 2 " + header[2]);
			int i=0;
			int pos=0;
			for(String ss : header){
				if(ss.equals(columne)){
					pos = i;
					break;
				}
				i++;
			}
			System.out.println("pos " + pos);
			String[] errors = ffpe.getInput().split(reader.separator);
			String error = errors[pos];
			String cause = "Failed to convert " + error+ " to "+exception.getCause().toString().split("default message")[2].split(";")[0].split("to")[2];
			inputError.cause = cause;
			System.out.println(inputError);
			inputError.save();
			return true;
		} else {
			if (exception instanceof ClassCastException && skipCount <= 1000000) {
				ClassCastException cce = (ClassCastException) exception;
				FlatFileParseException ffpe = (FlatFileParseException) exception;
				StringBuilder errorMessage = new StringBuilder();
				errorMessage.append("error parseException");
				//errorMessage.append(ffpe.getInput() + "\n");
				logger.error("{}", errorMessage.toString());
				System.out.println(exception);
				inputError.job_execution_id = job_execution_id;
				inputError.lineNumber = ffpe.getLineNumber();
				inputError.line = ffpe.getInput();
				inputError.filee = Reader.find.byId(readerId).filePath;
				System.out.println(inputError);
				String columne = exception.getCause().toString().split("property")[2].split(";")[0].split("'")[1];
				inputError.columne = columne;
				String cause = exception.getCause().toString().split("default message")[2].split(";")[0];
				inputError.cause = cause;
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

	public Long getReaderId() {
		return readerId;
	}

	public void setReaderId(Long readerId) {
		this.readerId = readerId;
	}
}
