package running;

import java.util.Date;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
	public static void main(String[] args) throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
		String springConfig = "JobConfig/jobEncadrant.xml";

		ApplicationContext context = new ClassPathXmlApplicationContext(springConfig);
		Job job = (Job) context.getBean("TestJob");
		String dateParam = new Date().toString();
		JobParameters param = new JobParametersBuilder().addString("date", dateParam).toJobParameters();
		JobLauncher jobLauncher = (JobLauncher) context.getBean("jobLauncher"); 
		jobLauncher.run(job, param);
	}
}
