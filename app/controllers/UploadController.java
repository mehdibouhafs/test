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

        File f = new File("/home/app/uploads/");
        if(!f.exists()) {
            try {
                f.mkdir();
            } catch (SecurityException se) {
                System.out.println("SSSSSSSSSSSSSSSSSSSSSSSSSSSSS");
                se.printStackTrace();
            }
        }
        try {
            File destination = new File("/home/app/uploads/", body.getFile("file").getFilename());
            System.out.println("---------------------------------DEBUT TRY UPLOAD---------------------------------");
            FileUtils.moveFile(body.getFile("file").getFile(), destination);
            System.out.println("FILE UPLOAD : "+destination.getPath());
            ApplicationContext context = Global.getApplicationContext();
            String dateParam = new Date().toString();
            System.out.printf("-----------------------------"+destination.getPath()+"-------------------------------------------");
            JobParameters param = new JobParametersBuilder()
                    .addString("input.file.name", destination.getPath())
                    .addString("date", dateParam).toJobParameters();

            JobLauncher jobLauncher = (JobLauncher) context.getBean("jobLauncher");
            String ext = getExtension(destination.getPath());
            System.out.println("EXTENSIONS : "+ext);
            Job job = null;
            if(ext.equals("csv")) {
               job = (Job) context.getBean("importUserJob");
            }
            if(ext.equals("xml")){
                job = (Job) context.getBean("importXML");
            }
            jobLauncher.run(job, param);
            destination.delete();
            //System.out.println("ok");
            return redirect("/home");

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




    public Result upload3() {
        Http.MultipartFormData<File> body = request().body().asMultipartFormData();
        Http.MultipartFormData.FilePart<File> picture = body.getFile("picture");
        if (picture != null) {
            String fileName = picture.getFilename();
            String contentType = picture.getContentType();
            File file = picture.getFile();
            return ok("File uploaded");
        } else {
            flash("error", "Missing file");
            return badRequest();
        }
    }
}
