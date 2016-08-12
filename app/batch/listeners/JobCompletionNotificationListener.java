package batch.listeners;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;


import batch.util.Generator;
import batch.model.ReaderGenerique;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import running.Global;

public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

	//private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

	private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);
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


	/*public Long getProgress() {

		double jobComplete = (Double) jobE.
				getExecutionContext().
				get("jobComplete");
		double reads = 0;
		for (StepExecution step : jobE.getStepExecutions()) {
			reads = reads + step.getReadCount();
		}
		return Math.round(reads / jobComplete * 100);
	}*/

	@Override
	public void afterJob(JobExecution jobExecution) {
		ApplicationContext context = Global.getApplicationContext();
		stopTime = new DateTime();
		ReaderGenerique readerGenerique = context.getBean("readerGenerique",ReaderGenerique.class);
		log.trace("Loading the data in Process");
		if(jobExecution.getStatus() == BatchStatus.COMPLETED) {

			//log.info("!!! JOB FINISHED! Time to verify the results");
			System.out.println("ExamResult Job stops at :"+stopTime);
			System.out.println("Total time take in millis :"+getTimeInMillis(startTime , stopTime));
			readerGenerique.setDateTime(getTimeInMillis(startTime , stopTime));
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
							Object o=new Object(); //Object o = Generator.buildCSVClassNamexml(readerGenerique.getProperties(),readerGenerique.getTable(),readerGenerique.getTypeXml()).newInstance();
						Class<?> act = null;
						String nameClasse;
						  if(readerGenerique.getExt().equals("csv")){
							   nameClasse = "app.batch.generate."+readerGenerique.getTable()+"csv$"+(Generator.getCounter2()-1);
						  }else{
							  nameClasse = "app.batch.generate."+readerGenerique.getFragmentRootElementName()+"xml$"+(Generator.getCounter2()-1);
						  }
							act = Class.forName(nameClasse);
							o = act.newInstance();
						Field c;
						int i=1;
						for (Field f : o.getClass().getDeclaredFields()) {
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
								case "Long":
									c.setAccessible(true);
									c.set(o, rs.getLong(i));
									i++;
								case "Float":
									c.setAccessible(true);
									c.set(o, rs.getFloat(i));
									i++;
								case "Double":
									c.setAccessible(true);
									c.set(o, rs.getDouble(i));
									i++;
								case "Date":
									c.setAccessible(true);
									c.set(o, rs.getDate(i));
									i++;
									break;
							}
						}
						//System.out.println(Generator.reflectToString(o));
						log.debug("Succes writing {} Object", Generator.reflectToString(o));
						return o;
					} catch (ClassNotFoundException e) {
						e.printStackTrace();

					} catch (IllegalAccessException e) {
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
			int i =0;
		for(Throwable th : exceptionList){
			readerGenerique.getErrors().put("exception"+i,th.getLocalizedMessage());
			System.err.println("exceptionsJOBLISTENER :" +th.getLocalizedMessage());
			System.err.println("Message :" +th.getMessage());
		}
	}
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
}
