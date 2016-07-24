package controllers;
import com.fasterxml.jackson.databind.annotation.JsonAppend;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dao.ObjectDao;
import dao.ObjectDaoJdbc;
import javassist.CannotCompileException;
import javassist.NotFoundException;
import model.*;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
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
import java.lang.reflect.Field;
import java.util.*;


/**
 * Created by MBS on 27/06/2016.
 */
public class UploadController extends Controller {

    private String[] cols;

    private Map<Integer,String> colsSelectedMap;


    private List<Attribute> attributes;

    private Map<String, Class<?>> properties;

    //String cheminMac = "/Users/bouhafs/Documents/sample-data.csv";
    //String cheminWin = "C:/Users/MBS/Desktop/complete/src/main/resources/sample-data.csv";
    private String filePath;

    public UploadController() {

    }
    public Result upload() throws IOException {

            //File destination = new File(cheminMac);
                Form<ParamFormData2> formData = Form.form(ParamFormData2.class).bindFromRequest();
                ApplicationContext context = Global.getApplicationContext();

                 List<String> list = new ArrayList<String>(Arrays.asList(getCols()));
                    String dateParam = new Date().toString();
            List<String> ss = formData.get().getCols();
                    //if (ext.equals("csv")) {

            ObjectNode result;
            //JsonArrayBuilder jsa =  Json.createArrayBuilder();
        colsSelectedMap = new HashMap<>();
            ArrayNode resuls = Json.newArray();
            int i=0;
            for (String s:ss) {
                result = Json.newObject();
                result.put("id",i+"");
                result.put("name",s);
                colsSelectedMap.put(i,s);
                i++;
                resuls.add(result);

                             }
                        return ok(Json.toJson(resuls));
    }

    public Result getTypes() throws NoSuchMethodException, IllegalAccessException, ClassNotFoundException, InstantiationException, CannotCompileException, NotFoundException {
        Attribute attribute ;
        attributes = new ArrayList<>();
        Form<ParamFormData1> formData = Form.form(ParamFormData1.class).bindFromRequest();
        //System.out.println(formData.toString());
        if (formData.hasErrors()) {
            System.out.println("ERROR POST");
            // Don't call formData.get() when there are errors, pass 'null' to helpers instead.
            flash("error", "Please correct errors above.");
            return badRequest(index.render(null,formData,null,
                    null,
                    null,
                    null
            ));
        }
        ApplicationContext context = Global.getApplicationContext();
        ReaderGenerique readerGenerique = context.getBean("readerGenerique",ReaderGenerique.class);

        properties = new LinkedHashMap<>();
        final Map<String,String> columnsTable = new LinkedHashMap<>();

        String tableName =  formData.get().getTable();
        System.out.println(formData.get().toString());
        System.out.println("TABLE NAme " + tableName);
        System.out.println(colsSelectedMap);
        for (Map.Entry<Integer,String> col : colsSelectedMap.entrySet()){
            attribute = new Attribute();
            String type = formData.get().getType().get(col.getKey());
            String typeSize = type +"(" + formData.get().getSize().get(col.getKey())+")";
            String size = formData.get().getSize().get(col.getKey());
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
                case "DATETIME":
                    Date d = new Date();
                    o = d;
                    break;
                default:
                    o = null;
                    break;
            }
            System.out.println("COlsid"+col.getKey()+"val : "+col.getValue());
            System.out.println("typeSize"+typeSize);
            properties.put(col.getValue(),o.getClass());


            columnsTable.put(col.getValue(),typeSize);
            //String colCap = cols[i].substring(0, 1).toUpperCase() + cols[i].substring(1);
           /* beanGenerator.getClass().getMethod("set"+colCap,o.getClass());
            System.out.println("set"+colCap);
            beanGenerator.getClass().getMethod("get"+colCap);*/
            attribute.setId(col.getKey());
            attribute.setSize(size);
            attribute.setName(col.getValue());
            attributes.add(attribute);
        }
        for (String s:cols){
            Class<?> val = properties.get(s);
            if(val == null){
                properties.put(s,Object.class);
            }
        }
        System.out.println("ColumnTable"+columnsTable);
        readerGenerique.setColumnsTable(columnsTable);
        readerGenerique.setTable(tableName);
        readerGenerique.setProperties(properties);
        Generator c = context.getBean("generator",Generator.class);
        c.setProperties(properties);
        c.setClassGenerate(c.generator());
        Object c1 = context.getBean("firstBe");

        System.out.println("GeneratorClass"+c.getClassGenerate());
        System.out.println("NEW CLASSE : " + c1.getClass());

        ObjectNode result;
        //JsonArrayBuilder jsa =  Json.createArrayBuilder();
        ArrayNode resuls = play.libs.Json.newArray();

        for (Attribute s:attributes
             ) {
            result = new play.libs.Json().newObject();
            result.put("id",s.getId());
            result.put("name",s.getName());
            result.put("type",s.getType());
            result.put("size",s.getSize());
            resuls.add(result);
        }
        return  ok(Json.toJson(resuls));
    }

    public Result cols() throws IOException {
        ApplicationContext context = Global.getApplicationContext();
        Form<ParamFormData> formData = Form.form(ParamFormData.class).bindFromRequest();
        this.filePath = formData.get().getFilePath();
        System.out.println("FilePath = "+filePath);
        String s = filePath;
        File destination = new File(filePath);
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
                    null,
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
            readerGenerique.setFilePath(filePath);
            ObjectNode result;
            //JsonArrayBuilder jsa =  Json.createArrayBuilder();
            ArrayNode resuls = play.libs.Json.newArray();
            //Object o;
            int i = 0;
            for (String s1 : cols) {
                result = play.libs.Json.newObject();
                result.put("id", String.valueOf(i));
                result.put("name", s1);
                i++;
                resuls.add(result);
            }
        System.out.println(Json.toJson(resuls));
            return ok(Json.toJson(resuls));
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

    public Result delete(int id){
        colsSelectedMap.remove(id);
        return ok("removed");
    }

    public Result validate(){
        ApplicationContext context = Global.getApplicationContext();
        ReaderGenerique readerGenerique = context.getBean("readerGenerique",ReaderGenerique.class);
        ObjectDao objectDao = context.getBean("ObjectDao",ObjectDaoJdbc.class);
        Boolean create = objectDao.createTable(readerGenerique.getTable(),readerGenerique.getColumnsTable());
        System.out.println("Validate");
        if(create) {
            File destination = new File(filePath);
            JobParameters param = new JobParametersBuilder()
                    .addString("input.file.name", destination.getPath())
                    .addLong("time", System.currentTimeMillis()).toJobParameters();
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
        }else {
            return ok("Table Not Create");
        }
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
        return ok(index.render(formData,null,null,
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

    public Map<Integer, String> getColsSelectedMap() {
        return colsSelectedMap;
    }

    public void setColsSelectedMap(Map<Integer, String> colsSelectedMap) {
        this.colsSelectedMap = colsSelectedMap;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }

    public Map<String, Class<?>> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Class<?>> properties) {
        this.properties = properties;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
