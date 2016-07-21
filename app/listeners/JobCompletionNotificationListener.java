package listeners;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;


import javassist.CannotCompileException;
import javassist.NotFoundException;
import model.App;
import model.Person;
import model.ReaderGenerique;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import running.Global;
import util.test;

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
			ApplicationContext context = Global.getApplicationContext();
			ReaderGenerique readerGenerique = context.getBean("readerGenerique",ReaderGenerique.class);
			StringBuffer cols = null;
			int i=0;
			for (Map.Entry<String,String> prop :readerGenerique.getColumnsTable().entrySet()
				 ) {
				if(i==0) {
					cols = new StringBuffer(prop.getKey());
					i++;
				}else {
					cols.append(", " + prop.getKey());
				}
			}

			final String select = "SELECT "+cols.toString()+" From "+readerGenerique.getTable();

			List<Object> results = jdbcTemplate.query(select, new RowMapper<Object>() {
				@Override
				public Object mapRow(ResultSet rs, int row) throws SQLException {
					try {
						Object o = App.buildCSVClassName(readerGenerique.getProperties(),"listeners").newInstance();
						Field c;
						int i=1;
						for (Field f : o.getClass().getDeclaredFields()
								) {
							c = o.getClass().getDeclaredField(f.getName());
							switch (f.getType().getSimpleName()) {
								case "String":
									c.setAccessible(true);
									c.set(o, rs.getString(i));
									i++;
									break;
								case "Integer":
									c.setAccessible(true);
									c.set(o, rs.getInt(i));
									i++;
									break;
								case "Date":
									c.setAccessible(true);
									c.set(o, rs.getDate(i));
									i++;
									break;
							}
						}
						System.out.println(App.reflectToString(o));
						return o;

					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (CannotCompileException e) {
						e.printStackTrace();
					} catch (NotFoundException e) {
						e.printStackTrace();
					} catch (NoSuchFieldException e) {
						e.printStackTrace();
					} catch (InstantiationException e) {
						e.printStackTrace();
					}
					return null;
				}
				});



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
