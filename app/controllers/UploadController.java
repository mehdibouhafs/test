package controllers;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import javassist.CannotCompileException;
import javassist.NotFoundException;
import model.*;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.context.ApplicationContext;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import running.Global;
import views.formdata.ParamFormData;
import views.formdata.ParamFormData1;
import views.formdata.ParamFormData2;
import views.html.index;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


/**
 * Created by MBS on 27/06/2016.
 */
public class UploadController extends Controller {

    public String[] cols;

    public String[] colsSelected;

    public UploadController() {

    }

    public Result upload() throws IOException {
            String cheminMac = "/Users/bouhafs/Documents/sample-data.csv";
            String cheminWin = "C:/Users/MBS/Desktop/complete/src/main/resources/sample-data.csv";
            File destination = new File(cheminWin);
                Form<ParamFormData2> formData = Form.form(ParamFormData2.class).bindFromRequest();
                ApplicationContext context = Global.getApplicationContext();

                 List<String> list = new ArrayList<String>(Arrays.asList(cols));
                    String dateParam = new Date().toString();
            List<String> ss = formData.get().getCols();
                    //if (ext.equals("csv")) {

            ObjectNode result;
            //JsonArrayBuilder jsa =  Json.createArrayBuilder();
        for (String s:ss
             ) {
            System.out.println(s);

        }
            ArrayNode resuls = Json.newArray();
            colsSelected = new String[ss.size()];
            int i=0;
            for (String s:ss) {
                result = Json.newObject();
                result.put("id",i);
                result.put("name",s);
                colsSelected[i]=s;
                i++;
                resuls.add(result);

                             }
                        return ok(resuls);
    }

    public Result getTypes() throws NoSuchMethodException, IllegalAccessException, ClassNotFoundException, InstantiationException, CannotCompileException, NotFoundException {
        Attribute attribute ;
        List<Attribute> attributes = new ArrayList<>();
        Form<ParamFormData1> formData = Form.form(ParamFormData1.class).bindFromRequest();
        ApplicationContext context = Global.getApplicationContext();
        for (String s:colsSelected
             ) {
            System.out.println("Colselected"+s);
        }
        final Map<String, Class<?>> properties =
                new HashMap<String, Class<?>>();
        String tableName = formData.get().getTableName();
        for(int i = 0 ; i<colsSelected.length;i++) {
            attribute = new Attribute();
            String type = formData.get().getType().get(i);
            attribute.setType(type);
            Object o;
            switch(type){
                case "INT":
                    int c=0;
                    o = c;
                    break;
                case "VARCHAR":
                    String c1="";
                    o = c1;
                    break;
                case "DATE":
                    Date d = new Date();
                    o = d;
                    break;
                default:
                    String k="";
                    o = k;
                    break;
            }
            properties.put(cols[i],o.getClass());
            //String colCap = cols[i].substring(0, 1).toUpperCase() + cols[i].substring(1);
           /* beanGenerator.getClass().getMethod("set"+colCap,o.getClass());
            System.out.println("set"+colCap);
            beanGenerator.getClass().getMethod("get"+colCap);*/
            String size = formData.get().getSize().get(i);
            attribute.setSize(size);
            attribute.setName(cols[i]);
            attributes.add(attribute);
        }
        System.out.println(properties);
        Generator c = context.getBean("generator",Generator.class);
        c.setProperties(properties);
        c.generator();
        Object c1 = context.getBean("firstBe");

        System.out.println("GeneratorClass"+c.getClassGenerate());
        System.out.println("NEW CLASSE : " + c1.getClass());

        ObjectNode result;
        //JsonArrayBuilder jsa =  Json.createArrayBuilder();
        ArrayNode resuls = play.libs.Json.newArray();

        int i = 0;
        for (Attribute s:attributes
             ) {

            result = new play.libs.Json().newObject();
            result.put("id",i);
            result.put("name",s.getName());
            result.put("size",s.getSize());
            result.put("type",s.getType());
            result.put("size",s.getSize());

            i++;
            resuls.add(result);
        }
        return  ok(resuls);
    }

    public Result cols() throws IOException {
        ApplicationContext context = Global.getApplicationContext();
        String cheminMac = "/Users/bouhafs/Documents/sample-data.csv";
        String cheminWin = "C:/Users/MBS/Desktop/complete/src/main/resources/sample-data.csv";
        File destination = new File(cheminWin);
        Form<ParamFormData> formData = Form.form(ParamFormData.class).bindFromRequest();
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
        }
            if (formData.get().getSeparator() != null) {
                cols = firstLine(destination, formData.get().getSeparator());
            } else {
                cols = firstLine(destination, null);
            }
            int nbLineToEscape = formData.get().getNumberLine();
            ReaderGenerique readerGenerique = context.getBean("readerGenerique", ReaderGenerique.class);
            readerGenerique.setColumns(cols);
            readerGenerique.setLineToSkip(nbLineToEscape);

            ObjectNode result;
            //JsonArrayBuilder jsa =  Json.createArrayBuilder();
            ArrayNode resuls = play.libs.Json.newArray();
            Object o;
            int i = 0;
            for (String s1 : cols) {
                result = play.libs.Json.newObject();
                result.put("id", i);
                result.put("name", s1);
                i++;
                resuls.add(result);
            }
            return ok(resuls);
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


    public Result validate(){
        ApplicationContext context = Global.getApplicationContext();
        String cheminMac = "/Users/bouhafs/Documents/sample-data.csv";
        String cheminWin = "C:/Users/MBS/Desktop/complete/src/main/resources/sample-data.csv";
        File destination = new File(cheminWin);
        JobParameters param = new JobParametersBuilder()
                .addString("input.file.name", destination.getPath())
                .addLong("time",System.currentTimeMillis()).toJobParameters();
        JobLauncher jobLauncher = (JobLauncher) context.getBean("jobLauncher");
        String ext = getExtension(destination.getPath());
        Job job = (Job) context.getBean("importUserJob");
        try {
            jobLauncher.run(job, param);
        } catch (JobExecutionAlreadyRunningException e) {
            e.printStackTrace();
        } catch (JobRestartException e) {
            e.printStackTrace();
        } catch (JobInstanceAlreadyCompleteException e) {
            e.printStackTrace();
        } catch (JobParametersInvalidException e) {
            e.printStackTrace();
        }
        return ok("valider");
    }

    public List<Integer> getPositons(List<String> s){
        List<Integer> poss = new ArrayList<>();
        for(int i=0;i<s.size();i++){
            if(s.get(i)!=null){
                poss.add(i);
            }
        }
        return  poss;
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
                null,
                null,
                sep
        ));
    }


    public String getExtension(String fileName) {
        String extension = "";

        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            extension = fileName.substring(i + 1);
        }
        return  extension;
    }

    public String[] getCols() {
        return cols;
    }

    public void setCols(String[] cols) {
        this.cols = cols;
    }

    public String[] getColsSelected() {
        return colsSelected;
    }

    public void setColsSelected(String[] colsSelected) {
        this.colsSelected = colsSelected;
    }
}
