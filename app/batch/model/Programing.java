package batch.model;

import batch.business.BatchJobService;
import batch.business.BatchJobServiceImpl;
import com.avaje.ebean.Model;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Created by MBS on 31/08/2016.
 */

@Entity
@Table(name="A_MBS_Programing")
public class Programing extends Model{

    @Id
    public Long id;
    @Transient
    private TaskScheduler scheduler;
    @Transient
    private Reader reader;

    private Long readerId;

    private Date datee;

    public Programing() {
    }

    public Programing(TaskScheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Transient
    Runnable exampleRunnable = new Runnable(){
        @Override
        public void run() {
            System.out.println("  RUN ");
            BatchJobService service = new BatchJobServiceImpl();
            service.doJob(reader);
            System.out.println("Done");
        }
    };

    @Async
    public void executeTaskT() {
        ScheduledExecutorService localExecutor = Executors.newSingleThreadScheduledExecutor();
        scheduler = new ConcurrentTaskScheduler(localExecutor);
        scheduler.schedule(exampleRunnable, datee);
    }


    public TaskScheduler getScheduler() {
        return scheduler;
    }

    public void setScheduler(TaskScheduler scheduler) {
        this.scheduler = scheduler;
    }

    public Reader getReader() {
        return reader;
    }

    public void setReader(Reader reader) {
        this.reader = reader;
        this.readerId = reader.id;
    }



    public Date getDate() {
        return datee;
    }

    public void setDate(Date date) {
        this.datee = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getReaderId() {
        return readerId;
    }

    public void setReaderId(Long readerId) {
        this.readerId = readerId;
    }

    public Date getDatee() {
        return datee;
    }

    public void setDatee(Date datee) {
        this.datee = datee;
    }
}
