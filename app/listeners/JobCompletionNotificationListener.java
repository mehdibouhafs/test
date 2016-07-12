package listeners;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


import model.Person;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

	//private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

	private final JdbcTemplate jdbcTemplate;


	@Autowired
	public JobCompletionNotificationListener(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}


	private DateTime startTime, stopTime;

	@Override
	public void beforeJob(JobExecution jobExecution) {
		startTime = new DateTime();
		System.out.println("ExamResult Job starts at :"+startTime);
	}

	@Override
	public void afterJob(JobExecution jobExecution) {

		stopTime = new DateTime();

		if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
			//log.info("!!! JOB FINISHED! Time to verify the results");
			System.out.println("ExamResult Job stops at :"+stopTime);
			System.out.println("Total time take in millis :"+getTimeInMillis(startTime , stopTime));

			List<Person> results = jdbcTemplate.query("SELECT person_id, first_name, last_name, date FROM people", new RowMapper<Person>() {
				@Override
				public Person mapRow(ResultSet rs, int row) throws SQLException {
                    Person p = new Person();
                    p.setId(rs.getInt(1));
                    p.setFirstName(rs.getString(2));p.setLastName(rs.getString(3));
                    p.setDate(rs.getDate(4));
					return p;
				}
			});

			for (Person person : results) {
				//log.info("Found <" + person + "> in the database.");
				System.out.println(person);
			}

		}else if(jobExecution.getStatus() == BatchStatus.FAILED){
		System.out.println("ExamResult job failed with following exceptions ");
		List<Throwable> exceptionList = jobExecution.getAllFailureExceptions();
		for(Throwable th : exceptionList){
			System.err.println("exception :" +th.getLocalizedMessage());
		}
	}
	}
	private long getTimeInMillis(DateTime start, DateTime stop){
		return stop.getMillis() - start.getMillis();
	}
}
