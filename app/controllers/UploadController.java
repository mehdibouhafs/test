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
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import play.Routes;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import running.Global;
import views.formdata.ParamFormData;
import views.html.index;
import views.html.parameter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Array;
import java.sql.BatchUpdateException;
import java.util.*;


/**
 * Created by MBS on 27/06/2016.
 */
public class UploadController extends Controller {


    public UploadController() {

    }


    public Result upload() throws IOException, JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
            File destination = new File("/Users/bouhafs/Documents/sample-data.csv");

                Form<ParamFormData> formData = Form.form(ParamFormData.class).bindFromRequest();
                ApplicationContext context = Global.getApplicationContext();

        String[] types = {"INTEGER", "STRING", "CHAR", "DOUBLE", "FLOAT", "DATE"};
        List<String> type = new ArrayList<String>(Arrays.asList(types));
        String[] seps = {";", ",", ".", "|", ":"};
        List<String> sep = new ArrayList<>(Arrays.asList(seps));
        if (formData.hasErrors()) {
            System.out.println("ERROR POST");
            // Don't call formData.get() when there are errors, pass 'null' to helpers instead.
            flash("error", "Please correct errors above.");
            return badRequest(index.render(formData,
                    null,
                    type,
                    sep
            ));
        }else {
                String[] cols;
                if(formData.get().getSeparator()!=null) {
                    cols = firstLine(destination, formData.get().getSeparator());
                }else{
                    cols=firstLine(destination,null);
                }
                 List<String> list = new ArrayList<String>(Arrays.asList(cols));
                    int nbLineToEscape = formData.get().getNumberLine();
                    ReaderGenerique readerGenerique = context.getBean("readerGenerique", ReaderGenerique.class);
                    readerGenerique.setColumns(cols);
                    readerGenerique.setLineToSkip(nbLineToEscape);



                    String dateParam = new Date().toString();
                    JobParameters param = new JobParametersBuilder()
                            .addString("input.file.name", destination.getPath())
                            .addString("date", dateParam).toJobParameters();
                    JobLauncher jobLauncher = (JobLauncher) context.getBean("jobLauncher");
                    String ext = getExtension(destination.getPath());
                    Job job = (Job) context.getBean("importUserJob");
                    try {
                        jobLauncher.run(job, param);
                    } catch (FlatFileParseException e) {
                        System.out.println(e.getMessage());
                    }
                    if (ext.equals("csv")) {
                        return ok(views.html.index.render(formData, list, type,sep));
                        //return ok("ok");
                    } else {
                        System.out.println("NULLL");
                    }
                }

            /*if (ext.equals("xml")) {
                System.out.println("XML");
                job = (Job) context.getBean("importXML");
                jobLauncher.run(job, param);
                return ok(views.html.parameter.render(formData,columns.makeColumnMap(paramData),Type.makeTypeMap(paramData),Separator.makeSeparatorMap(paramData)));
            }
            //destination.delete();*/
            return ok("ok");
            //return ok(views.html.parameter.render(uploadResult,rows));

    }

    public Result index() {
        String[] types = {";",",",".","|",":"};
        List<String> type = new ArrayList<>(Arrays.asList(types));

        String[] seps = {";", ",", ".", "|", ":"};
        List<String> sep = new ArrayList<>(Arrays.asList(seps));

       List<String> cols = new ArrayList<>();


        ParamFormData paramData =  new ParamFormData();
        Form<ParamFormData> formData = Form.form(ParamFormData.class).fill(paramData);
        return ok(index.render(formData,
                cols,
                type,
                sep
        ));
    }


    public String[] firstLine (File f,String delimiter) throws IOException {
        //String result = new ArrayList<>(); // !!!
        //Rows rows = new Rows();
        //LinkedHashMap<String,Row> columns = new LinkedHashMap<>();
        //List<Row> row = new ArrayList<>();
        FileReader fr = new FileReader(f);
        BufferedReader br = new BufferedReader(fr);
        String quotes ="\"";
        if(delimiter!=null) {
            String line0 = br.readLine().replaceAll(quotes, "");
            String[] line = line0.split(delimiter);
            System.out.println("Delimiter not null"+br.readLine());
            br.close();
            return line;
        }
        else {
            String[] s = br.readLine().split(",");
            br.close();
            return s ;
        }
        //System.out.println(line);
        //rows.setRow(row);

    }


    public String getExtension(String fileName) {
        String extension = "";

        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            extension = fileName.substring(i + 1);
        }
        return  extension;
    }



}
