package controllers;
import akka.dispatch.Foreach;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dao.ObjectDao;
import dao.ObjectDaoJdbc;
import javassist.CannotCompileException;
import javassist.NotFoundException;
import listeners.JobCompletionNotificationListener;
import model.Attribute;
import model.Generator;
import model.ReaderGenerique;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import play.data.Form;
import play.libs.Json;
import play.mvc.*;
import play.mvc.Controller;
import running.Global;
import util.ReadXMLFile2;
import views.formdata.*;
import views.html.index;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Blob;
import java.util.*;


/**
 * Created by MBS on 27/06/2016.
 */

@Component("application")
public class Application extends Controller {

    private String[] cols;

    private Map<Integer, String> colsSelectedMap;


    private List<Attribute> attributes;

    private Map<String, Class<?>> properties;

    private String extFile;

    //String cheminMac = "/Users/bouhafs/Documents/sample-data.csv";
    //String cheminWin = "C:/Users/MBS/Desktop/complete/src/main/resources/sample-data.csv";
    private String filePath;

    public Application() {
        System.out.println(" -----------------------------Demarage application Construct------------------by Mehdi Bouhafs Encadre by - MIMO");
    }

    public Result home() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        //ApplicationContext context = new ClassPathXmlApplicationContext("context.xml");
        //Application app = (Application) context.getBean("application");
        //String s = app.encadrant.getName();
        //encadrant.setName("ok");
        //this.encadrant = Global.getBean(encadrant.getClass());

        ParamFormData paramData = new ParamFormData();
        Form<ParamFormData> formData = Form.form(ParamFormData.class).fill(paramData);


        return ok("ApplicationController");
    }

    public Result upload() throws IOException {
        Form<ParamFormData2> formData = Form.form(ParamFormData2.class).bindFromRequest();
        ApplicationContext context = Global.getApplicationContext();
        List<String> list = new ArrayList<String>(Arrays.asList(getCols()));
        String dateParam = new Date().toString();
        List<String> ss = formData.get().getCols();
        ObjectNode result;
        //JsonArrayBuilder jsa =  Json.createArrayBuilder();
        colsSelectedMap = new HashMap<>();
        ArrayNode resuls = Json.newArray();
        int i = 0;
        for (String s : ss) {
            result = Json.newObject();
            result.put("id", i + "");
            result.put("name", s);
            colsSelectedMap.put(i, s);
            i++;
            resuls.add(result);
        }
        return ok(Json.toJson(resuls));
    }

    public Result getTypes() throws NoSuchMethodException, IllegalAccessException, ClassNotFoundException, InstantiationException, CannotCompileException, NotFoundException {
        Attribute attribute;
        attributes = new ArrayList<>();
        Form<ParamFormData1> formData = Form.form(ParamFormData1.class).bindFromRequest();
        System.out.println(formData.get().toString());
        //System.out.println(formData.toString());
       /* if (formData.hasErrors()) {
            // Don't call formData.get() when there are errors, pass 'null' to helpers instead.
            flash("error", "Please correct errors above.");
            return badRequest(index.render(null, formData, null,
                    null,
                    null,
                    null
            ));
        }*/
        ApplicationContext context = Global.getApplicationContext();
        ReaderGenerique readerGenerique = context.getBean("readerGenerique", ReaderGenerique.class);
        properties = new LinkedHashMap<>();
        StringBuffer typeSizes;
        final Map<String, String> columnsTable = new LinkedHashMap<>();
        for (Map.Entry<Integer, String> col : colsSelectedMap.entrySet()) {
            attribute = new Attribute();
            String type = formData.get().getType().get(col.getKey());
            String typeSize = type + "(" + formData.get().getSize().get(col.getKey()) + "),"+formData.get().getPrimaryKey().get(col.getKey())+",";
            typeSizes = new StringBuffer(typeSize);
            String size = formData.get().getSize().get(col.getKey());
            String primarykey = formData.get().getPrimaryKey().get(col.getKey());
            System.out.println("KEY"+col.getKey());


            try {
               // boolean autoIncrement = formData.get().getAutoIncrement().get(col.getKey());
                String[] autoIncrement = request().body().asFormUrlEncoded().get("autoIncrement["+col.getKey()+"]");
                if(autoIncrement[0].equals("autoIncrement")){
                    System.out.println("FDP TRUE" + col.getKey() +"val = " +col.getValue());
                    String s = "AUTO_INCREMENT";
                    typeSizes.append(s);
                    attribute.setAutoIncrement(true);
                }else{
                    System.out.println("FDP false" + col.getKey() +"val = " +col.getValue());
                }
            }catch (Exception e){
                System.out.println("Exception try" + col.getKey() +"val = " +col.getValue());
                String s = "walou";
                attribute.setAutoIncrement(false);
                typeSizes.append(s);
            }
            attribute.setType(type);

            Object o;
            switch (type) {
                case "INT":
                    int c = 0;
                    o = c;
                    break;
                case "TINYINT":
                    int c00 = 0;
                    o = c00;
                    break;
                case "SMALLINT":
                    int c01 = 0;
                    o = c01;
                    break;
                case "BLOB":
                    Blob c01782 = null;
                    o = c01782;
                    break;
                case "DOUBLE":
                    Double c001 = 0.0;
                    o = c001;
                    break;
                case "DECIMAL":
                    Double c0019 = 0.0;
                    o = c0019;
                    break;
                case "REAL":
                    float p = 2;
                    o = p;
                    break;
                case "BIT":
                    Byte p1 = 2;
                    o = p1;
                    break;
                case "BOOLEAN":
                    Boolean b = true;
                    o = b;
                    break;
                case "MEDIUMINT":
                    int c02 = 0;
                    o = c02;
                    break;
                case "BIGINT":
                    int c03 = 0;
                    o = c03;
                    break;
                case "VARCHAR":
                    String c1 = "";
                    o = c1;
                    break;
                case "TEXT":
                    String c10 = "";
                    o = c10;
                    break;
                case "DATETIME":
                    //DateTime c11= new DateTime();
                    Date c11 = new Date();
                    o = c11;
                    break;
                case "DATE":
                    Date d = new Date();
                    o = d;
                    break;
                default:
                    o = null;
                    break;
            }
            properties.put(col.getValue(), o.getClass());
            columnsTable.put(col.getValue(), typeSizes.toString());
            //String colCap = cols[i].substring(0, 1).toUpperCase() + cols[i].substring(1);
           /* beanGenerator.getClass().getMethod("set"+colCap,o.getClass());
            System.out.println("set"+colCap);
            beanGenerator.getClass().getMethod("get"+colCap);*/
            attribute.setId(col.getKey());
            attribute.setSize(size);
            attribute.setName(col.getValue());
            attribute.setPrimaryKey(primarykey);
            attributes.add(attribute);
        }
        for (String s : cols) {
            Class<?> val = properties.get(s);
            if (val == null) {
                properties.put(s, Object.class);
            }
        }
        readerGenerique.setColumnsTable(columnsTable);
        readerGenerique.setTable(formData.get().getTableName());
        readerGenerique.setProperties(properties);
        Generator c = context.getBean("generator", Generator.class);
        c.setProperties(properties);
        Class<?> classNew = c.generator(formData.get().getTableName());
        System.out.println("Classe New "+classNew);
        c.setClassGenerate(classNew);
        Object c1 = context.getBean("firstBe");
        String sp = classNew.toString();
        String[] a= sp.split(" ");
        System.out.println("A1"+a[1]);
        readerGenerique.setClassXml(a[1]);
        System.out.println("classsssssssssssssssssssss   "+c1.getClass());
        ObjectNode result;
        //JsonArrayBuilder jsa =  Json.createArrayBuilder();
        ArrayNode resuls = play.libs.Json.newArray();
        System.out.println("Attributes"+attributes);
        for (Attribute s : attributes
                ) {
            result = new play.libs.Json().newObject();
            result.put("id", s.getId());
            result.put("name", s.getName());
            result.put("type", s.getType());
            result.put("size", s.getSize());
            result.put("primaryKey",s.getPrimaryKey());
            if(s.isAutoIncrement() == true) {
                result.put("autoIncrement", "true");
            }else{
                result.put("autoIncrement", "false");
            }
            resuls.add(result);
        }
        return ok(Json.toJson(resuls));
    }

    public Result path() throws IOException {
        ApplicationContext context = Global.getApplicationContext();
        ReaderGenerique readerGenerique = context.getBean("readerGenerique", ReaderGenerique.class);
        Form<ParamFormData> formData = Form.form(ParamFormData.class).bindFromRequest();
        this.filePath = formData.get().getFilePath();
        readerGenerique.setFilePath(this.filePath);
        this.extFile = getExtension(filePath);
        readerGenerique.setExt(extFile);
        if (formData.hasErrors()) {
            // Don't call formData.get() when there are errors, pass 'null' to helpers instead.
            flash("error", "Please correct errors above.");
            return badRequest(index.render(formData,
                    null,
                    null,
                    null,
                    null,
                    null
            ));
        }
        return ok("path"+filePath);

    }


    public Result cols() throws IOException {
        ApplicationContext context = Global.getApplicationContext();
        ReaderGenerique readerGenerique = context.getBean("readerGenerique", ReaderGenerique.class);
        Form<ParamformData01> formData01 = Form.form(ParamformData01.class).bindFromRequest();
        File destination = new File(this.filePath);
        if (formData01.hasErrors()) {
            // Don't call formData.get() when there are errors, pass 'null' to helpers instead.
            flash("error", "Please correct errors above.");
            return badRequest(index.render(null,
                    null,
                    null,
                    null,
                    null,
                    null
            ));
        }
            System.out.println("csv File cols");
            if (formData01.get().getSeparator() != null) {
                cols = firstLine(destination, formData01.get().getSeparator());
            } else {
                cols = firstLine(destination, null);
            }
            int nbLineToEscape = formData01.get().getNumberLine();
            readerGenerique.setLineToSkip(nbLineToEscape);
            readerGenerique.setSeparator(formData01.get().getSeparator());
            readerGenerique.setColumns(cols);
        ObjectNode result;
            //JsonArrayBuilder jsa =  Json.createArrayBuilder();
            ArrayNode resuls = play.libs.Json.newArray();
            //Object o;
            int i = 0;
            for(String s1 :cols)
            {
                result = play.libs.Json.newObject();
                result.put("id", String.valueOf(i));
                result.put("name", s1);
                i++;
                resuls.add(result);
            }
            return ok(Json.toJson(resuls));
}

    public Result colsxml() throws IOException {
        ApplicationContext context = Global.getApplicationContext();
        ReaderGenerique readerGenerique = context.getBean("readerGenerique", ReaderGenerique.class);
        Form<ParamFormData02> formData = Form.form(ParamFormData02.class).bindFromRequest();
        File destination = new File(this.filePath);
        if (formData.hasErrors()) {
            // Don't call formData.get() when there are errors, pass 'null' to helpers instead.
            flash("error", "Please correct errors above.");
            return badRequest(index.render(null,
                    null,
                    null,
                    null,
                    null,
                    null
            ));
        }
            System.out.println("xml File cols");
            String typeXML = formData.get().getXml();
            System.out.println(typeXML);
            cols = firstLine1(destination);
        ObjectNode result;
        //JsonArrayBuilder jsa =  Json.createArrayBuilder();
        ArrayNode resuls = play.libs.Json.newArray();
        //Object o;
        int i = 0;
        for(String s1 :cols)
        {
            result = play.libs.Json.newObject();
            result.put("id", String.valueOf(i));
            result.put("name", s1);
            i++;
            resuls.add(result);
        }
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
            //System.out.println("Delimiter not null"+br.readLine());
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

    public String[] firstLine1(File f){
        DocumentBuilder dBuilder = null;
        try {
            dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = dBuilder.parse(f);
            if (doc.hasChildNodes()) {
                ReadXMLFile2 readXMLFile2 = new ReadXMLFile2();

                readXMLFile2.printNote(doc.getChildNodes());
                String[] ss = new String[readXMLFile2.getS().size()-2];
                int j=0;
                for (int i=2;i<readXMLFile2.getS().size();i++) {
                    ss[j]=readXMLFile2.getS().get(i);
                    j++;
                }
                return ss;
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

    public Result delete(int id){
        colsSelectedMap.remove(id);
        return ok("removed");
    }

    public Result validate() {
        ApplicationContext context = Global.getApplicationContext();
        ReaderGenerique readerGenerique = context.getBean("readerGenerique", ReaderGenerique.class);
        ObjectDao objectDao = context.getBean("ObjectDao", ObjectDaoJdbc.class);
        Boolean create = objectDao.createTable(readerGenerique.getTable(), readerGenerique.getColumnsTable());
        System.out.println("Validate");
        if (create) {
            File destination = new File(filePath);
            JobParameters param = new JobParametersBuilder()
                    .addString("input.file.name", destination.getPath())
                    .addLong("time", System.currentTimeMillis()).toJobParameters();
            JobLauncher jobLauncher = (JobLauncher) context.getBean("jobLauncher");
            if(extFile.equals("csv")) {
                System.out.println("CSV JOB");
                Job job = (Job) context.getBean("importUserJob");
                try {
                    JobExecution jobExecution = jobLauncher.run(job, param);
                    JobCompletionNotificationListener listener = context.getBean("listener", JobCompletionNotificationListener.class);
                    if (jobExecution.getStatus().equals(BatchStatus.COMPLETED)) {
                        Generator c = context.getBean("generator", Generator.class);
                        c.setClassGenerate(null);
                        Object c1 = context.getBean("firstBe");
                        System.out.println(c1.getClass());

                        ObjectNode result = play.libs.Json.newObject();
                        result.put("time", readerGenerique.getDateTime());
                        return ok(Json.toJson(result));
                    }
                } catch (JobExecutionAlreadyRunningException e) {
                    e.printStackTrace();
                } catch (JobRestartException e) {
                    e.printStackTrace();
                } catch (JobInstanceAlreadyCompleteException e) {
                    e.printStackTrace();
                } catch (JobParametersInvalidException e) {
                    e.printStackTrace();
                }
            }else if(extFile.equals("xml")){
                System.out.println("XML JOB");
                Job job = (Job) context.getBean("importXML");
                try {
                    JobExecution jobExecution = jobLauncher.run(job, param);
                    JobCompletionNotificationListener listener = context.getBean("listener", JobCompletionNotificationListener.class);
                    if (jobExecution.getStatus().equals(BatchStatus.COMPLETED)) {
                        Generator c = context.getBean("generator", Generator.class);
                        c.setClassGenerate(null);
                        ObjectNode result = play.libs.Json.newObject();
                        result.put("time", readerGenerique.getDateTime());
                        return ok(Json.toJson(result));
                    }
                } catch (JobExecutionAlreadyRunningException e) {
                    e.printStackTrace();
                } catch (JobRestartException e) {
                    e.printStackTrace();
                } catch (JobInstanceAlreadyCompleteException e) {
                    e.printStackTrace();
                } catch (JobParametersInvalidException e) {
                    e.printStackTrace();
                }
            }

        } else {
            return ok("Table Not Created");
        }
        return ok("Job not completed");
    }

    /*public List<Integer> getPositons(List<String> s){
        List<Integer> poss = new ArrayList<>();
        for(int i=0;i<s.size();i++){
            if(s.get(i)!=null){
                poss.add(i);
            }
        }
        return  poss;
    }*/



    public Result index() {
        String[] seps = {";", ",", ".", "|", ":"," "};
        List<String> sep = new ArrayList<>(Arrays.asList(seps));
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
        System.out.println(filePath);
        this.filePath = filePath;
    }

    public String getExtFile() {
        return extFile;
    }

    public void setExtFile(String extFile) {
        this.extFile = extFile;
    }
}
