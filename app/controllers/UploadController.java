package controllers;
import org.apache.commons.io.FileExistsException;
import org.apache.commons.io.FileUtils;
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
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import running.Global;

import java.io.File;
import java.io.IOException;
import java.sql.BatchUpdateException;
import java.util.Date;


/**
 * Created by MBS on 27/06/2016.
 */
public class UploadController extends Controller {

    public UploadController() {
    }

    public Result test(){
        //return render("/persons")
        return ok(views.html.test.render());
    }

    public Result upload() {
        System.out.println("---------------------------------DEBUT UPLOAD---------------------------------");
        Http.MultipartFormData<File> body = request().body().asMultipartFormData();
        if (body == null) {
            return badRequest("Invalid request, required is POST with enctype=multipart/form-data.");
        }

        Http.MultipartFormData.FilePart filePart = body.getFile("file");
        if (filePart == null) {
            return badRequest("Invalid request, no file has been sent.");
        }

        // getContentType can return null, so we check the other way around to prevent null exception
       /* if (!"application/vnd.ms-excel".equalsIgnoreCase(filePart.getContentType())) {
            return badRequest("Invalid request, only CSVs are allowed.");
        }*/
        boolean c = createDirectory();
        File destination = new File("/home/app/uploads/", body.getFile("file").getFilename());
        if(destination.exists()){
            destination.delete();
        }
        try {
            System.out.println("---------------------------------DEBUT TRY UPLOAD---------------------------------");
            FileUtils.moveFile(body.getFile("file").getFile(), destination);
            ApplicationContext context = Global.getApplicationContext();
            String dateParam = new Date().toString();
            System.out.printf("-----------------------------"+destination.getPath()+"-------------------------------------------");
            JobParameters param = new JobParametersBuilder()
                    .addString("input.file.name", destination.getPath())
                    .addString("date", dateParam).toJobParameters();

            JobLauncher jobLauncher = (JobLauncher) context.getBean("jobLauncher");
            String ext = getExtension(destination.getPath());
            Job job = null;
            if(ext.equals("csv")) {
               job = (Job) context.getBean("importUserJob");
            }
            if(ext.equals("xml")){
                job = (Job) context.getBean("importXML");
            }
            jobLauncher.run(job, param);
            destination.delete();
            System.out.println("ok");
            return ok(views.html.test.render());

        } catch (FileExistsException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            flash("File not UPLOAD");
            return ok("not upload");
        } catch (JobInstanceAlreadyCompleteException e) {
            e.printStackTrace();
        } catch (JobExecutionAlreadyRunningException e) {
            e.printStackTrace();
        } catch (JobParametersInvalidException e) {
            e.printStackTrace();
        } catch (JobRestartException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
        return ok("False");
    }



    public Result upload2() {
        File file = request().body().asRaw().asFile();
        return ok("File uploaded");
    }
    public String getExtension(String fileName) {
        String extension = "";

        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            extension = fileName.substring(i + 1);
        }
        return  extension;
    }

    public boolean createDirectory(){
        File theDir = new File("/home/app/uploads/");
        boolean result = false;

// if the directory does not exist, create it
        if (!theDir.exists()) {
             result = false;
            try{
                theDir.mkdir();
                result = true;
            }
            catch(SecurityException se){
                se.printStackTrace();
            }
            if(result) {
                System.out.println("DIR created");
                result = true;
            }
        }
        return result;
    }
}
