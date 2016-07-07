package controllers;
import model.*;
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
import play.Routes;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import running.Global;
import views.html.index;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.BatchUpdateException;
import java.util.*;


/**
 * Created by MBS on 27/06/2016.
 */
public class UploadController extends Controller {


    public UploadController() {

    }


    public Result upload() throws IOException, JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {


        System.out.println("UPLOAD --------------£££££££££££££");

        Http.MultipartFormData<File> body = request().body().asMultipartFormData();
        Http.MultipartFormData.FilePart<File> picture = body.getFile("picture");

        if (body == null) {
            return badRequest("Invalid request, required is POST with enctype=multipart/form-data.");
        }

        if (picture == null) {
            return badRequest("Invalid request, no file has been sent.");
        }

        if (picture != null) {
            System.out.println("NOT NULL");
            File f = new File(System.getProperty("user.home") +"/app/uploads/");
            if(!f.exists()) {
                try {
                    f.mkdir();
                } catch (SecurityException se) {
                    se.printStackTrace();
                }
            }

            String fileName = picture.getFilename();
            System.out.println("FILE NAME = "+fileName);
            String contentType = picture.getContentType();
            File file = picture.getFile();

            UploadResult uploadResult = new UploadResult();
            uploadResult.setName(fileName);
            uploadResult.setType(contentType);
            uploadResult.setFile(file);


            System.out.println(f.getPath());
            File destination = new File(System.getProperty("user.home") +"/app/uploads/",fileName);
            uploadResult.setUrl(destination.getPath());
            uploadResult.save();
            if(destination.exists()){
                destination.delete();
            }
            if(!destination.exists()) {
                FileUtils.moveFile(file, destination);
            }
            if(session("idFile")!= null){
                session().remove("idFile");
            }else {

                session("idFile", uploadResult.getId() + "");
            }
            ApplicationContext context = Global.getApplicationContext();
            Rows rows = firstLine(new File(uploadResult.getUrl()));

            /*ReaderGenerique readerGenerique = context.getBean("readerGenerique",ReaderGenerique.class);
            readerGenerique.setFirstLine(rows);

            String dateParam = new Date().toString();
            System.out.printf("-----------------------------" + destination.getPath() + "-------------------------------------------");
            JobParameters param = new JobParametersBuilder()
                    .addString("input.file.name", destination.getPath())
                    .addString("date", dateParam).toJobParameters();

            JobLauncher jobLauncher = (JobLauncher) context.getBean("jobLauncher");
            String ext = getExtension(destination.getPath());

            Job job = null;

            if (ext.equals("csv")) {
                job = (Job) context.getBean("importUserJob");
                jobLauncher.run(job, param);
                return ok(views.html.parameter.render(uploadResult,rows));
            }

            if (ext.equals("xml")) {
                job = (Job) context.getBean("importXML");
                jobLauncher.run(job, param);
                return ok(views.html.parameter.render(uploadResult,null));
            }
            //destination.delete();*/
            return ok("ok");
            //return ok(views.html.parameter.render(uploadResult,rows));

        } else {
            System.out.println(" NULL");
            flash("error", "Missing file");
            return badRequest();
        }
    }


    public Rows firstLine (File f) throws IOException {
        //List<String> result = new ArrayList<>(); // !!!

        Rows rows = new Rows();
        //LinkedHashMap<String,Row> columns = new LinkedHashMap<>();

        List<Row> row = new ArrayList<>();

        FileReader fr = new FileReader(f);
        BufferedReader br = new BufferedReader(fr);

        /*for (String line = br.readLine(); line != null; line = br.readLine()) {
            result.add(line);
        }
        br.close();
        fr.close();*/

        String line = br.readLine();
        String[] cols =  line.split(",");
        for (String col: cols
             ) {

            Row w = new Row();
            //w.setSelected(false);
            //w.setType(new Object());
            //w.setSize(0);
            //w.setName(col);
            w.setName(new Columns(col));
            row.add(w);
        }

        rows.setRow(row);
        return rows;
    }


   /* public Result upload() {

        Http.MultipartFormData<File> body = request().body().asMultipartFormData();
        Http.MultipartFormData.FilePart<File> picture = body.getFile("picture");

        if (body == null) {
            return badRequest("Invalid request, required is POST with enctype=multipart/form-data.");
        }
        if (picture == null) {
            return badRequest("Invalid request, no file has been sent.");
        }
        if (picture != null) {
            File f = new File(System.getProperty("user.home") + "/app/uploads/");
            if (!f.exists()) {
                try {
                    f.mkdir();
                } catch (SecurityException se) {
                    se.printStackTrace();
                }
            }
            try {
                UploadResult uploadResult = new UploadResult();
                uploadResult.save();
                uploadResult.setType(picture.getContentType());
                File destination = new File(System.getProperty("user.home") + "/app/uploads/",uploadResult.getName());
                FileUtils.moveFile(picture.getFile(), destination);
                uploadResult.setUrl(destination.getPath());
                uploadResult.setFile(destination);
                uploadResult.setSize(FileUtils.sizeOf(destination));
                uploadResult.update();
                session("idFile", ""+uploadResult.getId());
                System.out.println("id========="+uploadResult.getId());
                ApplicationContext context = Global.getApplicationContext();
                String dateParam = new Date().toString();
                System.out.printf("-----------------------------" + uploadResult.getFile().getPath() + "-------------------------------------------");
                JobParameters param = new JobParametersBuilder()
                        .addString("input.file.name", uploadResult.getFile().getPath())
                        .addString("date", dateParam).toJobParameters();

                JobLauncher jobLauncher = (JobLauncher) context.getBean("jobLauncher");
                String ext = getExtension(uploadResult.getFile().getPath());

                Job job = null;

                if (ext.equals("csv")) {
                    job = (Job) context.getBean("importUserJob");
                    jobLauncher.run(job, param);
                }

                if (ext.equals("xml")) {
                    job = (Job) context.getBean("importXML");
                    jobLauncher.run(job, param);
                }

                destination.delete();

                return ok(uploadResult.getId()+"");

            } catch (FileExistsException e) {
                e.printStackTrace();

            } catch (IOException e) {
                e.printStackTrace();

            } catch (JobInstanceAlreadyCompleteException e) {
                e.printStackTrace();

            } catch (JobExecutionAlreadyRunningException e) {
                e.printStackTrace();

            } catch (JobParametersInvalidException e) {
                e.printStackTrace();

            } catch (JobRestartException e) {
                e.printStackTrace();

            } catch (Exception e) {
                e.printStackTrace();

            }
        }
        return ok("File NULL");
    } */



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
