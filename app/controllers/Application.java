package controllers;
import batch.util.App;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import batch.dao.ObjectDao;
import batch.dao.ObjectDaoJdbc;
import javassist.CannotCompileException;
import javassist.NotFoundException;
import batch.listeners.JobCompletionNotificationListener;
import batch.model.Attribute;
import batch.model.ReaderGenerique;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import play.data.Form;
import play.libs.Json;
import play.mvc.*;
import play.mvc.Controller;
import running.Global;
import batch.util.ReadXMLFile2;
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
        ApplicationContext context = Global.getApplicationContext();
        ReaderGenerique readerGenerique = context.getBean("readerGenerique", ReaderGenerique.class);
        Form<ParamFormData2> formData = Form.form(ParamFormData2.class).bindFromRequest();
        String dateParam = new Date().toString();
        List<String> ss = null;
        String type = formData.get().getTypeXML();
        readerGenerique.setTypeXml(type);
        if(type.equals("type3") || type.equals("type2")){
            String[] cols = firstLine1(new File(readerGenerique.getFilePath()), type);
            ss = Arrays.asList(cols);
            readerGenerique.setColumns(cols);
        }else{
            ss = formData.get().getCols();
        }
        //elements = new ArrayList<>();
        //attributtes = new ArrayList<>();

        /*if (type.equals("type3")) {
            for (int i = 0; i < ss.size(); i++) {
                try {
                    String[] element = request().body().asFormUrlEncoded().get("elements[" + i + "]");
                    if (element[0].equals("elements[" + i + "]")) {
                        elements.add(ss.get(i));
                    }
                } catch (Exception e) {
                    String[] attribute = request().body().asFormUrlEncoded().get("attributes[" + i + "]");
                    if (attribute[0].equals("attributes[" + i + "]")) {
                        attributtes.add(ss.get(i));
                    }

                    readerGenerique.getErrors().put("uploadException", e.getMessage());
                    System.out.println(e.getMessage());
                }
            }
        }*/

        System.out.println("Element = " + readerGenerique.getElements());
        System.out.println("Attributes = " + readerGenerique.getAttributes());

        ObjectNode result;
        //JsonArrayBuilder jsa =  Json.createArrayBuilder();
        ArrayNode resuls = Json.newArray();
        int i = 0;
        for (String s : ss) {
            result = Json.newObject();
            result.put("id", i + "");
            result.put("name", s);
            i++;
            resuls.add(result);
        }
        return ok(Json.toJson(resuls));
    }

    public Result getTypes() throws NoSuchMethodException, IllegalAccessException, ClassNotFoundException, InstantiationException, CannotCompileException, NotFoundException {
        Attribute attribute;
        List<Attribute> attributes = new ArrayList<>();
        Form<ParamFormData1> formData = Form.form(ParamFormData1.class).bindFromRequest();
        String typeXml = formData.get().getTypeXML();
        List<String> ss = formData.get().getCols();
        List<Integer> ids = formData.get().getId();
        System.out.println("id :" + ids);
        System.out.println("COls Selected:" + ss);

        ApplicationContext context = Global.getApplicationContext();
        ReaderGenerique readerGenerique = context.getBean("readerGenerique", ReaderGenerique.class);
        Map<String, Class<?>> properties = new LinkedHashMap<>();
        StringBuffer typeSizes;
        final Map<String, String> columnsTable = new LinkedHashMap<>();
        for (int i = 0; i < ss.size(); i++) {
            try {
                attribute = new Attribute();
                String type = formData.get().getType().get(ids.get(i));
                //String typeSize = type + "(" + formData.get().getSize().get(i) + ")," + formData.get().getPrimaryKey().get(ids.get(i)) + ",";
                String typeSize = type + "-"+formData.get().getSize().get(i)+"-";////hey
                typeSizes = new StringBuffer(typeSize);
                String size = formData.get().getSize().get(ids.get(i));
                String primarykey = formData.get().getPrimaryKey().get(ids.get(i));
                System.out.println("KEY" + ids.get(i));
                try {
                    // boolean autoIncrement = formData.get().getAutoIncrement().get(col.getKey());
                    String[] autoIncrement = request().body().asFormUrlEncoded().get("autoIncrement[" + ids.get(i) + "]");
                    if (autoIncrement[0].equals("autoIncrement")) {
                        System.out.println("FDP TRUE" + i + "val = " + ss.get(i));
                        String s = "AUTO_INCREMENT";
                        typeSizes.append(s);
                        attribute.setAutoIncrement(true);
                    } else {
                        System.out.println("FDP false" + i + "val = " + ss.get(i));
                    }
                } catch (Exception e) {
                    System.out.println("Exception try" + i + "val = " + ss.get(i));
                    String s = "walou";
                    attribute.setAutoIncrement(false);
                    typeSizes.append(s);
                }
                attribute.setType(type);
                Class o;
                switch (type) {
                    case "INT":
                        o = Integer.class;
                        break;
                    case "TINYINT":
                        o = Integer.class;
                        break;
                    case "SMALLINT":
                        o = Integer.class;
                        break;
                    case "BLOB":
                        o = Blob.class;
                        break;
                    case "DOUBLE":
                        o = Double.class;
                        break;
                    case "DECIMAL":
                        o = Double.class;
                        break;
                    case "REAL":
                        o = Float.class;
                        break;
                    case "BIT":
                        o = Byte.class;
                        break;
                    case "BOOLEAN":
                        o = Boolean.class;
                        break;
                    case "MEDIUMINT":
                        o = Integer.class;
                        break;
                    case "BIGINT":
                        o = Integer.class;
                        break;
                    case "VARCHAR":
                        o = String.class;
                        break;
                    case "TEXT":
                        o = String.class;
                        break;
                    case "DATETIME":
                        o = Date.class;
                        break;
                    case "DATE":
                        o = Date.class;
                        break;
                    default:
                        o = null;
                        break;
                }
                properties.put(ss.get(i), o);
                columnsTable.put(ss.get(i), typeSizes.toString());
                attribute.setId(ids.get(i));
                attribute.setSize(size);
                attribute.setName(ss.get(i));
                attribute.setPrimaryKey(primarykey);
                attributes.add(attribute);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        for (String s : readerGenerique.getColumns()) {
            Class<?> val = properties.get(s);
            if (val == null) {
                properties.put(s, Object.class);
            }
        }
        readerGenerique.setColumnsTable(columnsTable);
        readerGenerique.setTable(formData.get().getTableName());
        readerGenerique.setProperties(properties);
        App c = context.getBean("app", App.class);
        c.setProperties(properties);
        Class<?> classNew = null;
        classNew = c.generator(formData.get().getTableName(), typeXml);
        System.out.println("Classe New " + classNew);
        //c.setClassGenerate(classNew);
        readerGenerique.setClassGenerate(classNew);
        String sp = classNew.toString();
        String[] a = sp.split(" ");
        System.out.println("classe XML " + a[1]);
        readerGenerique.setClassXml(a[1]);
        Object c1 = context.getBean("firstBe");
        System.out.println("classsssssssssssssssssssss   " + c1.getClass() +"reader clss "+readerGenerique.getClassGenerate());
        ObjectNode result;
        //JsonArrayBuilder jsa =  Json.createArrayBuilder();
        ArrayNode resuls = play.libs.Json.newArray();
        System.out.println("Attributes" + attributes);
        for (Attribute s : attributes
                ) {
            result = new play.libs.Json().newObject();
            result.put("id", s.getId());
            result.put("name", s.getName());
            result.put("type", s.getType());
            result.put("size", s.getSize());
            result.put("primaryKey", s.getPrimaryKey());
            if (s.isAutoIncrement() == true) {
                result.put("autoIncrement", "true");
            } else {
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
        String filePath = formData.get().getFilePath();
        readerGenerique.setFilePath(filePath);
        String extFile = getExtension(readerGenerique.getFilePath());
        readerGenerique.setExt(extFile);
        if (formData.hasErrors()) {
            // Don't call formData.get() when there are errors, pass 'null' to helpers instead.
            flash("error", "Please correct errors above.");
            return badRequest(index.render());
        }
        return ok("path" + readerGenerique.getFilePath());
    }


    public Result cols() throws IOException {
        String[] cols;
        ApplicationContext context = Global.getApplicationContext();
        ReaderGenerique readerGenerique = context.getBean("readerGenerique", ReaderGenerique.class);
        Form<ParamformData01> formData01 = Form.form(ParamformData01.class).bindFromRequest();
        File destination = new File(readerGenerique.getFilePath());
        /*if (formData01.hasErrors()) {
            // Don't call formData.get() when there are errors, pass 'null' to helpers instead.
            flash("error", "Please correct errors above.");
            return badRequest(index.render(null,
                    null,
                    null,
                    null,
                    null,
                    null
            ));
        }*/
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
        for (String s1 : cols) {
            result = play.libs.Json.newObject();
            result.put("id", String.valueOf(i));
            result.put("name", s1);
            i++;
            resuls.add(result);
        }

        return ok(Json.toJson(resuls));
    }

    public Result colsxml() throws IOException {
        String[] cols;
        ApplicationContext context = Global.getApplicationContext();
        ReaderGenerique readerGenerique = context.getBean("readerGenerique", ReaderGenerique.class);
        Form<ParamFormData02> formData = Form.form(ParamFormData02.class).bindFromRequest();
        File destination = new File(readerGenerique.getFilePath());
        if (formData.hasErrors()) {
            // Don't call formData.get() when there are errors, pass 'null' to helpers instead.
            flash("error", "Please correct errors above.");
            return badRequest(index.render(
            ));
        }
        System.out.println("xml File cols");
        String typeXML = formData.get().getXml().get(0);
        System.out.println(typeXML);
        cols = firstLine1(destination, typeXML);
        readerGenerique.setColumns(cols);
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
        return ok(Json.toJson(resuls));
    }

    public String[] firstLine(File f, String delimiter) throws IOException {
        //String result = new ArrayList<>(); // !!!
        //Rows rows = new Rows();
        //LinkedHashMap<String,Row> columns = new LinkedHashMap<>();
        //List<Row> row = new ArrayList<>();
        FileReader fr = new FileReader(f);
        BufferedReader br = new BufferedReader(fr);
        String quotes = "\"";
        if (delimiter != null) {
            String line0 = br.readLine().replaceAll(quotes, "");
            String[] line = line0.split(delimiter);
            //System.out.println("Delimiter not null"+br.readLine());
            br.close();
            return line;
        } else {
            String[] s = br.readLine().split(",");
            br.close();
            return s;
        }
        //System.out.println(line);
        //rows.setRow(row);
    }

    public String[] firstLine1(File f, String typeXml) {
        ApplicationContext context = Global.getApplicationContext();
        ReaderGenerique readerGenerique = context.getBean("readerGenerique", ReaderGenerique.class);
        DocumentBuilder dBuilder = null;
        try {
            dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = dBuilder.parse(f);
            if (doc.hasChildNodes()) {
                ReadXMLFile2 readXMLFile2 = new ReadXMLFile2();
                readXMLFile2.printNote(doc.getChildNodes());
                if (typeXml.equals("type1")) {
                    String[] ss = new String[readXMLFile2.getS().size() - 2];
                    int j = 0;
                    for (int i = 2; i < readXMLFile2.getS().size(); i++) {
                        ss[j] = readXMLFile2.getS().get(i);
                        j++;
                    }
                    return ss;
                } else {
                    if (typeXml.equals("type2")) {
                        String[] att = new String[readXMLFile2.getAtt().size()];
                        for (int i = 0; i < readXMLFile2.getAtt().size(); i++) {
                            att[i] = readXMLFile2.getAtt().get(i);
                        }
                        return att;
                    } else if (typeXml.equals("type3")) {
                        String[] ss = new String[readXMLFile2.getS().size() - 2];
                        int j = 0;
                        readerGenerique.setFragmentRootElementName(readXMLFile2.getS().get(1));
                        for (int i = 2; i < readXMLFile2.getS().size(); i++) {
                            ss[j] = readXMLFile2.getS().get(i);
                            j++;
                        }
                        readerGenerique.setElements(Arrays.asList(ss));
                        String[] att = new String[readXMLFile2.getAtt().size()];
                        for (int i = 0; i < readXMLFile2.getAtt().size(); i++) {
                            att[i] = readXMLFile2.getAtt().get(i);
                        }
                        readerGenerique.setAttributes(Arrays.asList(att));
                        String[] both = (String[]) ArrayUtils.addAll(ss, att);
                        return both;
                    }
                }
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            readerGenerique.getErrors().put("colsXmlParserException", e.getMessage());
        } catch (SAXException e) {
            e.printStackTrace();
            readerGenerique.getErrors().put("colsXmlSAXException", e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            readerGenerique.getErrors().put("colsXmlIOException", e.getMessage());
        }

        return null;

    }

    public Result delete(int id) {
        //colsSelectedMap.remove(id);
        //attributtes.remove(id);
        return ok("removed");
    }


    public Result metadata() {
        ApplicationContext context = Global.getApplicationContext();
        ObjectDao objectDao = context.getBean("ObjectDao", ObjectDaoJdbc.class);
        String[] table = request().body().asFormUrlEncoded().get("tableName");
        System.out.println("metada table" + table[0]);
        Map<String, String> metadats = objectDao.dataTable(table[0]);
        ObjectNode result;
        //JsonArrayBuilder jsa =  Json.createArrayBuilder();
        ArrayNode resuls = play.libs.Json.newArray();
        //Object o;
        for (Map.Entry<String, String> metada : metadats.entrySet()) {
            result = play.libs.Json.newObject();
            result.put("col", metada.getKey());
            result.put("type", metada.getValue());
            resuls.add(result);
        }

        return ok(Json.toJson(resuls));
    }

    public Result validate() {
        ApplicationContext context = Global.getApplicationContext();
        ReaderGenerique readerGenerique = context.getBean("readerGenerique", ReaderGenerique.class);
        ObjectDao objectDao = context.getBean("ObjectDao", ObjectDaoJdbc.class);
        Form<ParamFormData3> formData = Form.form(ParamFormData3.class).bindFromRequest();
        String dropeTable = formData.get().getDropeTable();
        if (dropeTable.equals("true")) {
            System.out.println("****************************************");

            System.out.println("****************************************");
            objectDao.dropTable(readerGenerique.getTable());
        }
        //Boolean create = objectDao.createTable(readerGenerique.getTable(), readerGenerique.getColumnsTable());
        Boolean create = objectDao.createTableOracle(readerGenerique.getTable(), readerGenerique.getColumnsTable());
        System.out.println("Validate");
        ObjectNode resultEchec = play.libs.Json.newObject();
        if (create) {
            File destination = new File(readerGenerique.getFilePath());
            JobParameters param = new JobParametersBuilder()
                    .addString("input.file.name", destination.getPath())
                    .addLong("time", System.currentTimeMillis()).toJobParameters();
            JobLauncher jobLauncher = (JobLauncher) context.getBean("jobLauncher");

            if (readerGenerique.getExt().equals("csv")) {
                Job job = (Job) context.getBean("importUserJob");
                try {
                    JobExecution jobExecution = jobLauncher.run(job, param);
                    JobCompletionNotificationListener listener = context.getBean("listener", JobCompletionNotificationListener.class);
                    if (jobExecution.getStatus().equals(BatchStatus.COMPLETED)) {
                        App c = context.getBean("app", App.class);
                        c.setClassGenerate(null);
                        Object c1 = context.getBean("firstBe");
                        System.out.println(c1.getClass());

                        ObjectNode result = play.libs.Json.newObject();

                        Double time = readerGenerique.getDateTime() / 1000.0;
                        System.out.println("time in seconde" + time + "time in mili" + readerGenerique.getDateTime());
                        time = (double) Math.round(time * 100);
                        time = time/100;
                        result.put("time", time);
                        return ok(Json.toJson(result));
                    }
                } catch (FlatFileParseException e) {
                    System.out.println("CATCH IT ");
                    e.printStackTrace();

                } catch (JobExecutionAlreadyRunningException e) {
                    e.printStackTrace();
                } catch (JobRestartException e) {
                    e.printStackTrace();

                } catch (JobInstanceAlreadyCompleteException e) {
                    e.printStackTrace();

                } catch (JobParametersInvalidException e) {
                    e.printStackTrace();

                }
            } else if (readerGenerique.getExt().equals("xml")) {
                System.out.println("XML JOB");
                Job job = (Job) context.getBean("importXML");
                try {
                    JobExecution jobExecution = jobLauncher.run(job, param);
                    JobCompletionNotificationListener listener = context.getBean("listener", JobCompletionNotificationListener.class);
                    if (jobExecution.getStatus().equals(BatchStatus.COMPLETED)) {
                        App c = context.getBean("app", App.class);
                        c.setClassGenerate(null);
                        ObjectNode result = play.libs.Json.newObject();
                        Double time = readerGenerique.getDateTime() / 1000.0;
                        System.out.println("time in seconde" + time + "time in mili" + readerGenerique.getDateTime());
                        result.put("time", time);
                        return ok(Json.toJson(result));
                    }
                } catch (JobExecutionAlreadyRunningException e) {
                    e.printStackTrace();
                    resultEchec.put("time", e.getMessage());


                } catch (JobRestartException e) {
                    e.printStackTrace();
                    resultEchec.put("time", e.getMessage());


                } catch (JobInstanceAlreadyCompleteException e) {
                    resultEchec.put("time", e.getMessage());


                } catch (JobParametersInvalidException e) {
                    e.printStackTrace();

                } catch (FlatFileParseException e) {
                    System.out.println("CATCH IT " + e.getMessage());

                }
            }

        } else {
            readerGenerique.getErrors().put("SQLError", "table not created sql error");
            return ok(Json.toJson(getErrors()));
        }
        //readerGenerique.getErrors().put("imcompleted","Job not completed");
        return ok(Json.toJson(getErrors()));
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
        String[] seps = {";", ",", ".", "|", ":", " "};
        List<String> sep = new ArrayList<>(Arrays.asList(seps));
        ParamFormData paramData = new ParamFormData();
        Form<ParamFormData> formData = Form.form(ParamFormData.class).fill(paramData);
        return ok(index.render());
    }


    public String getExtension(String fileName) {
        String extension = "";

        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            extension = fileName.substring(i + 1);
        }
        return extension;
    }

    public ArrayNode getErrors() {
        ApplicationContext context = Global.getApplicationContext();
        ReaderGenerique readerGenerique = context.getBean("readerGenerique", ReaderGenerique.class);
        ObjectNode result;
        ArrayNode resuls = play.libs.Json.newArray();
        for (Map.Entry<String, String> error : readerGenerique.getErrors().entrySet()) {
            result = play.libs.Json.newObject();
            result.put("erreur", error.getKey());
            result.put("value", error.getValue());
            resuls.add(result);
        }
        return resuls;
    }


}