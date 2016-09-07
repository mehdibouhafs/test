package batch.listeners;

import java.util.List;


import batch.model.Reader;
import batch.model.Resume;
import batch.model.User;
import batch.policy.FileVerificationSkipper;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.batch.item.file.transform.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import running.Global;

public class JobCompletionNotificationListener extends JobExecutionListenerSupport {
	//private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

	private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);
	private final JdbcTemplate jdbcTemplate;
	private User user;
	private Reader reader;
	private Resume resume;

	@Autowired
	public JobCompletionNotificationListener(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}


	private DateTime startTime, stopTime;

	@Override
	public void beforeJob(JobExecution jobExecution) {
		startTime = new DateTime();
		System.out.println("ExamResult Job starts at :"+startTime);
		ApplicationContext context = Global.getApplicationContext();
		FileVerificationSkipper fileVerificationSkipper = (FileVerificationSkipper) context.getBean("FileVerificationSkipper");
		fileVerificationSkipper.setJob_execution_id(jobExecution.getJobId());
		fileVerificationSkipper.setReaderId(reader.id);
	}
	@Override
	public void afterJob(JobExecution jobExecution) {
		ApplicationContext context = Global.getApplicationContext();
		User user = this.user;
		stopTime = new DateTime();
		log.trace("Loading the data in Process");
		if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
			user.job_completed = user.job_completed + 1L;
			//log.info("!!! JOB FINISHED! Time to verify the results");
			System.out.println("ExamResult Job stops at :"+stopTime);
			System.out.println("Total time take in millis :"+getTimeInMillis(startTime , stopTime));
		}else if(jobExecution.getStatus() == BatchStatus.FAILED){
			user.job_failed = user.job_failed + 1L;
		System.out.println("ExamResult job failed with following exceptions ");
		List<Throwable> exceptionList = jobExecution.getAllFailureExceptions();
			int i =0;
		if(exceptionList.size()>0){
			    this.resume = new Resume();
				this.resume.setError("Erreur parsing du fichier on vous suggère de choisir le bon séparateur pour ce fichier et relancer votre batch ! ");
			}

	}else if(jobExecution.getStatus() == BatchStatus.ABANDONED){
			user.job_abondonned = user.job_abondonned + 1L;
		}
		user.total_jobs = user.job_completed + user.job_failed + user.job_abondonned;
		user.update();
	}


	public DateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(DateTime startTime) {
		this.startTime = startTime;
	}

	public DateTime getStopTime() {
		return stopTime;
	}

	public void setStopTime(DateTime stopTime) {
		this.stopTime = stopTime;
	}

	private long getTimeInMillis(DateTime start, DateTime stop){
		return stop.getMillis() - start.getMillis();
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

	public Resume getResume() {
		return resume;
	}

	public void setResume(Resume resume) {
		this.resume = resume;
	}
}
