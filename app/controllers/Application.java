package controllers;
import batch.dao.BatchJobDao;
import batch.model.*;
import batch.model.batch.BatchExecutionParam;
import batch.model.batch.BatchJobExecution;
import batch.model.batch.BatchStepExecution;
import batch.security.Secured;
import batch.util.Generator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import batch.dao.ObjectDao;
import batch.dao.ObjectDaoJdbc;
import javassist.CannotCompileException;
import javassist.NotFoundException;
import batch.listeners.JobCompletionNotificationListener;
import org.apache.commons.lang3.ArrayUtils;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.batch.core.*;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import play.data.Form;
import play.libs.Json;
import play.mvc.*;
import play.mvc.Controller;
import running.Global;
import batch.util.ReadXMLFile2;
import scala.util.parsing.json.JSONObject;
import views.formdata.*;
import views.html.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.sql.Blob;
import java.util.*;


/**
 * Created by MBS on 27/06/2016.
 */

@Component("application")
public class Application extends Controller {

    private ReaderGenerique readerGenerique;
    private ApplicationContext context;
    public Application() {
        context = Global.getApplicationContext();
        System.out.println(" -----------------------------Demarage application Construct------------------by Mehdi Bouhafs Encadre by - MIMO");

    }

    public Result home() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        ParamFormData paramData = new ParamFormData();
        Form<ParamFormData> formData = Form.form(ParamFormData.class).fill(paramData);
        return ok("ApplicationController");
    }

    public Result upload() throws IOException {
        readerGenerique = context.getBean("readerGenerique", ReaderGenerique.class);
        Form<ParamFormData2> formData = Form.form(ParamFormData2.class).bindFromRequest();
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
        System.out.println("Element = " + readerGenerique.getElements());
        System.out.println("Attributes = " + readerGenerique.getAttributes());
        ObjectNode result;
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
        List<String> comments = formData.get().getCommentaire();
        List<String> valDefautl = formData.get().getDefaultVal();
        System.out.println("val"+valDefautl);
        System.out.println("comments"+comments);
        System.out.println("id :" + ids);
        System.out.println("COls Selected:" + ss);
        ApplicationContext context = Global.getApplicationContext();
        ReaderGenerique readerGenerique = context.getBean("readerGenerique", ReaderGenerique.class);
        Map<String, Class<?>> properties = new LinkedHashMap<>();
        StringBuffer typeSizes;
        Classe classe = new Classe();
        classe.className=formData.get().getTableName();
        classe.user_email = session("email");
         classe.save();
        System.out.println("/////////////////////////////SAVE CLASSE DATABASE");
        final Map<String, String> columnsTable = new LinkedHashMap<>();
        Long j = 0L;
        for (int i = 0; i < ss.size(); i++) {
            try {
                attribute = new Attribute();
                String type = formData.get().getType().get(ids.get(i));
                //String typeSize = type + "(" + formData.get().getSize().get(i) + ")," + formData.get().getPrimaryKey().get(ids.get(i)) + ",";
                String typeSize = type + "-"+formData.get().getSize().get(i);////hey
                typeSizes = new StringBuffer(typeSize);
                String size = formData.get().getSize().get(ids.get(i));
                try{
                    String[] notNull = request().body().asFormUrlEncoded().get("nonNull[" + ids.get(i) + "]");
                    if (notNull[0].equals("notNull")) {
                        System.out.println("NotNULL" + i + "val = " + ss.get(i));
                        String s = "- NOT NULL";
                        typeSizes.append(s);
                        attribute.nonNull = true;
                    }
                }catch (Exception e){
                    attribute.nonNull = false;
                    typeSizes.append("-");
                }
                try {
                    // boolean autoIncrement = formData.get().getAutoIncrement().get(col.getKey());
                    String[] primarayKey = request().body().asFormUrlEncoded().get("pk[" + ids.get(i) + "]");
                    if (primarayKey[0].equals("primaryKey")) {
                        System.out.println("Primary key" + i + "val = " + ss.get(i));
                        String s = "-PrimaryKey";
                        typeSizes.append(s);
                        attribute.pko = true;
                    }
                } catch (Exception e) {
                    attribute.pko = false;
                    typeSizes.append("-");
                }

                attribute.type = type;
                Class o;

                switch (type) {
                    case "NUMBER":
                        o = Integer.class;
                        break;
                    case "CHAR":
                        o = Character.class;
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
                    case "VARCHAR2":
                        o = String.class;
                        break;
                    case "VARCHAR":
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
                typeSizes.append("- DEFAULT '"+valDefautl.get(i)+"'");
                typeSizes.append("- "+comments.get(i));
                System.out.println("typeSize for "+ss.get(i)+" typeSizes "+typeSizes.toString());
                columnsTable.put(ss.get(i), typeSizes.toString());
                attribute.sizeo = size;
                attribute.nameo = ss.get(i);
                attribute.commentaire = comments.get(i);
                attribute.defautlVal = valDefautl.get(i);
                attribute.classe = classe;
                attributes.add(attribute);
                attribute.save();
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
        Generator c = context.getBean("generator", Generator.class);
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
            result.put("pk",s.isPko());
            result.put("name", s.getNameo());
            result.put("type", s.getType());
            result.put("size", s.getSizeo());
            result.put("notNull",s.isNonNull());
            result.put("defautlVal",s.getDefautlVal());
            result.put("comment",s.getCommentaire());

            /*result.put("primaryKey", s.getPrimaryKey());
            if (s.isAutoIncrement() == true) {
                result.put("autoIncrement", "true");
            } else {
                result.put("autoIncrement", "false");
            }*/
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
            return badRequest(index.render(batch.model.User.find.byId(request().username())));
        }
        return ok("path" + readerGenerique.getFilePath());
    }



    public Result cols001(){
        ApplicationContext context = Global.getApplicationContext();
        ReaderGenerique readerGenerique = context.getBean("readerGenerique", ReaderGenerique.class);
        Form<ParamFormData001> formData001 = Form.form(ParamFormData001.class).bindFromRequest();
        String[] separator = request().body().asFormUrlEncoded().get("separator");
        List<String> cols = formData001.get().getCol();
        System.out.println("cols "+ cols);
        File destination = new File(readerGenerique.getFilePath());
        readerGenerique.setLineToSkip(0);
        readerGenerique.setSeparator(separator[0]);
        String[] cols1 = new String[cols.size()];
        ObjectNode result;
        ArrayNode resuls = play.libs.Json.newArray();
        int i=0;
        for (String s:cols) {
            cols1[i] = s;
            result = play.libs.Json.newObject();
            result.put("id", String.valueOf(i));
            result.put("name", s);
            i++;
            resuls.add(result);
        }
        readerGenerique.setColumns(cols1);
        return ok(Json.toJson(resuls));
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
            return badRequest(index.render(batch.model.User.find.byId(request().username())
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


    public Result testJson(Long id){
        List<InputError> inputErros = InputError.find.where().eq("job_execution_id",id).findList();
        Resume resume = new Resume();
        //resume.getBatchJobExecution().add(BatchJobExecution.find.byId(jobExecution.getId()));
        resume.setBatchStepExecution(BatchStepExecution.find.byId(id));
        if(inputErros.size()>0){
            resume.setInputError(inputErros);
        }
        return  ok(Json.toJson(resume));
    }

    @Transactional
    @Security.Authenticated(Secured.class)
    public Result validate() {
        batch.model.User user = batch.model.User.find.byId(request().username());
        ApplicationContext context = Global.getApplicationContext();
        ReaderGenerique readerGenerique = context.getBean("readerGenerique", ReaderGenerique.class);
        JobCompletionNotificationListener jobCompletionNotificationListener = (JobCompletionNotificationListener) context.getBean("listener");
        jobCompletionNotificationListener.setUser(user);
        ObjectDao objectDao = context.getBean("ObjectDao", ObjectDaoJdbc.class);
        Form<ParamFormData3> formData = Form.form(ParamFormData3.class).bindFromRequest();
        String dropeTable = formData.get().getDropeTable();
        if (dropeTable.equals("true")) {
            System.out.println("****************************************");
            objectDao.dropTable(readerGenerique.getTable());
            System.out.println("****************************************");
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
                Job job = (Job) context.getBean("csvJob");
                try {
                    JobExecution jobExecution = jobLauncher.run(job, param);
                    if (jobExecution.getStatus().equals(BatchStatus.COMPLETED)) {
                        List<InputError> inputErros = InputError.find.where().eq("job_execution_id",jobExecution.getJobId()).findList();
                        Resume resume = new Resume();
                        //resume.getBatchJobExecution().add(BatchJobExecution.find.byId(jobExecution.getId()));
                        //resume.getBatchStepExecution().add(BatchStepExecution.find.byId(jobExecution.getId()));
                        resume.setBatchStepExecution(BatchStepExecution.find.byId(jobExecution.getId()));
                        resume.setInputError(inputErros);
                        Generator c = context.getBean("generator", Generator.class);
                        c.setClassGenerate(null);
                        Double time = readerGenerique.getDateTime() / 1000.0;
                        time = (double) Math.round(time * 100);
                        time = time/100;
                        resume.setTime(time);
                        return ok(Json.toJson(resume));
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
                        JsonNode jsonObject = Json.toJson(InputError.find.where().eq("job_execution_id",jobExecution.getJobId()).findList());
                        JsonNode jsonObject1 = Json.toJson(jobExecution);
                        ArrayNode arrayNode = play.libs.Json.newArray();
                        arrayNode.add(jsonObject);
                        arrayNode.add(jsonObject1);
                        System.out.println("eeeeee "+InputError.find.where().eq("job_execution_id",jobExecution.getJobId()).findList());
                        InputError.find.where().eq("job_execution_id",jobExecution.getJobId()).findList();
                        Generator c = context.getBean("generator", Generator.class);
                        c.setClassGenerate(null);
                        ObjectNode result = play.libs.Json.newObject();
                        Double time = readerGenerique.getDateTime() / 1000.0;
                        System.out.println("time in seconde" + time + "time in mili" + readerGenerique.getDateTime());
                        result.put("time", time);
                        arrayNode.add(result);
                        return ok(Json.toJson(arrayNode));
                    }
                } catch (JobExecutionAlreadyRunningException e) {
                    e.printStackTrace();
                    resultEchec.put("time", e.getMessage());


                } catch (JobRestartException e) {
                    e.printStackTrace();
                    resultEchec.put("time", e.getMessage());


                } catch (JobInstanceAlreadyCompleteException e) {
                    resultEchec.put("time", e.getMessage());
                    e.printStackTrace();

                } catch (JobParametersInvalidException e) {
                    e.printStackTrace();

                } catch (FlatFileParseException e) {
                    System.out.println("CATCH IT " + e.getMessage());
                    e.printStackTrace();
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
    @Security.Authenticated(Secured.class)
    public Result index() {
        String[] seps = {";", ",", ".", "|", ":", " "};
        List<String> sep = new ArrayList<>(Arrays.asList(seps));
        ParamFormData paramData = new ParamFormData();
        Form<ParamFormData> formData = Form.form(ParamFormData.class).fill(paramData);

        return ok(index.render(batch.model.User.find.byId(request().username())));
    }
    @Security.Authenticated(Secured.class)
    public Result param() {
        return ok(parameter.render(batch.model.User.find.byId(request().username())));
    }

    public Result logout() {
        session().clear();
        response().discardCookie("rememberme");
        flash("success", "You've been logged out");
        return redirect(routes.Application.login()
        );
    }


    @Security.Authenticated(Secured.class)
    public Result edit(){
        batch.model.User user = batch.model.User.find.byId(request().username());
        return ok(edit.render(user));
    }


    @Security.Authenticated(Secured.class)
    public Result editPass(){
        batch.model.User user = batch.model.User.find.byId(request().username());

        EditPassword paramData = new EditPassword();
        Form<EditPassword> form = Form.form(EditPassword.class).fill(paramData);

        return ok(editPassword.render(form,user));
    }

    @Security.Authenticated(Secured.class)
    public Result editPassword(){
        batch.model.User user = batch.model.User.find.byId(request().username());
        System.out.println("User "+ user);
        Form<EditPassword> form = Form.form(EditPassword.class).bindFromRequest();
        if (form.hasErrors()) {
            return badRequest(editPassword.render(form,user));
        }else{
            if(BCrypt.checkpw(form.get().exPassword, user.password)){
                System.out.println("Her");
                user.password = BCrypt.hashpw(form.get().password, BCrypt.gensalt());
                user.update();
                return ok(editPassword.render(form,user));
        }else{
                return badRequest(editPassword.render(form,user));
            }
        }
    }

    @Security.Authenticated(Secured.class)
    public Result editUser(){
        batch.model.User user = batch.model.User.find.byId(request().username());
        Form<EditProfile> loginForm = Form.form(EditProfile.class).bindFromRequest();
        user.first_name = loginForm.get().first_name;
        user.last_name = loginForm.get().last_name;
        Http.MultipartFormData<File> body = request().body().asMultipartFormData();
        Http.MultipartFormData.FilePart<File> picture = body.getFile("picture");

        if (picture != null) {
            String fileName = picture.getFilename();
            String contentType = picture.getContentType();
            File file = picture.getFile();
            OutputStream out = null;
            InputStream filecontent = null;
            try {
                out = new FileOutputStream(new File("public/template/dist/img/"+fileName));
                filecontent = new FileInputStream(file);
                int read = 0;
                final byte[] bytes = new byte[1024];

                while ((read = filecontent.read(bytes)) != -1) {
                    out.write(bytes, 0, read);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            user.imagePath = "assets/template/dist/img/"+fileName;
            user.update();
            return ok(edit.render(user));
    }else{
            user.update();
            return ok(edit.render(user));
        }
    }

    public Result allMyJobs(){
        List<BatchExecutionParam> batchExecutionParams = BatchExecutionParam.find.where().eq("STRING_VAL",session("email")).findList();
        List<BatchJobExecution> batchJobExecutions = new ArrayList<>();
        for (BatchExecutionParam batchExecutionParam : batchExecutionParams){
            BatchJobExecution batchJobExecution =  BatchJobExecution.find.byId(batchExecutionParam.getJob_execution_id());
            batchJobExecutions.add(batchJobExecution);
        }
        return ok(Json.toJson(batchJobExecutions));
    }

    public Result allMyJobCompleted(){
        List<BatchExecutionParam> batchExecutionParams = BatchExecutionParam.find.where().eq("STRING_VAL",session("email")).findList();
        List<BatchJobExecution> batchJobExecutionsStatusCompleted = new ArrayList<>();
        for (BatchExecutionParam batchExecutionParam : batchExecutionParams){
            BatchJobExecution batchJobExecution =  BatchJobExecution.find.byId(batchExecutionParam.getJob_execution_id());
            if(batchJobExecution.status.equals("COMPLETED")) {
                batchJobExecutionsStatusCompleted.add(batchJobExecution);
            }
        }
        return ok(Json.toJson(batchJobExecutionsStatusCompleted));
    }


    public Result allMyJobFailed(){
        List<BatchExecutionParam> batchExecutionParams = BatchExecutionParam.find.where().eq("STRING_VAL",session("email")).findList();
        List<BatchJobExecution> batchJobExecutionsStatusFailed = new ArrayList<>();
        for (BatchExecutionParam batchExecutionParam : batchExecutionParams){
            BatchJobExecution batchJobExecution =  BatchJobExecution.find.byId(batchExecutionParam.getJob_execution_id());
            if(batchJobExecution.status.equals("FAILED")) {
                batchJobExecutionsStatusFailed.add(batchJobExecution);
            }
        }
        return ok(Json.toJson(batchJobExecutionsStatusFailed));
    }

    public Result allMyJobAbondonned(){
        List<BatchExecutionParam> batchExecutionParams = BatchExecutionParam.find.where().eq("STRING_VAL",session("email")).findList();
        List<BatchJobExecution> batchJobExecutionsStatusAbondonned = new ArrayList<>();
        for (BatchExecutionParam batchExecutionParam : batchExecutionParams){
            BatchJobExecution batchJobExecution =  BatchJobExecution.find.byId(batchExecutionParam.getJob_execution_id());
            if(batchJobExecution.status.equals("ABONDONNED")) {
                batchJobExecutionsStatusAbondonned.add(batchJobExecution);
            }
        }
        return ok(Json.toJson(batchJobExecutionsStatusAbondonned));
    }

    public Result login() {
        Form form = Form.form(Login.class);
        Http.Cookie remember = request().cookie("rememberme");
        ApplicationContext context = Global.getApplicationContext();
        BatchJobDao batchJobDao = (BatchJobDao) context.getBean("batchJobDao");
        if(remember != null) {
            int firstIndex = remember.value().indexOf("-");
            //System.out.println("BACHAR " +batchJobDao.selectAllStepExectuion());
            //System.out.println("BATCHPAr " + );
                String sign = remember.value().substring(0, firstIndex);
                String restOfCookie = remember.value().substring(firstIndex + 1);
                String username = restOfCookie;
                if(play.api.libs.Crypto.crypto().sign(restOfCookie).equals(sign)) {
                    session("email", username);
                    batch.model.User user = batch.model.User.find.byId(username);
                    return ok(index.render(user));
            }
        }
        return ok(login.render(form));
    }

    public Result authenticate() {
        Form<Login> loginForm = Form.form(Login.class).bindFromRequest();
        System.out.println(loginForm);
        System.out.println(loginForm);
        if (loginForm.hasErrors()) {
            return badRequest(login.render(loginForm));
        } else {
            batch.model.User user = batch.model.User.authenticate(loginForm.get().email,loginForm.get().password);
            if(user!=null){
            session().clear();
            session("email", loginForm.get().email);
                if(loginForm.get().rememberMe == true) {
                    System.out.println("REmember me check cookie");
                    response().setCookie("rememberme", play.api.libs.Crypto.crypto().sign(loginForm.get().email) + "-" + loginForm.get().email, 60*60*24*360);
                }
            return redirect(
                    routes.Application.index()
            );
            }  else{
                return badRequest(login.render(loginForm));
            }
        }
    }

    public Result lockScreen(){
        batch.model.User user = batch.model.User.find.byId(session(("email")));
        return  ok(lockscreen.render(user));
    }

    public Result delockScreen(){
        System.out.println("delock");
        String[] password = request().body().asFormUrlEncoded().get("password");
        System.out.println("password"+password[0]);
        batch.model.User user = batch.model.User.authenticate(session(("email")),password[0]);
        if(user != null){
            session("email",user.email);
            response().setCookie("rememberme", play.api.libs.Crypto.crypto().sign(user.email) + "-" + user.email, 60*60*24*360);
            return ok(index.render(user));
        }else{
            return  ok(index.render(new User()));
        }
    }

    public Result getClasses(){
        if(Classe.find.all()!=null){
        return ok(Json.toJson(Classe.find.all()));
        }else{
            return ok(Json.toJson(new Classe()));
        }
    }

    public Result getAttributes(String  id){
        System.out.println("id " + id);
        System.out.println("attribute "+Attribute.findInvolving(id));
        return  ok(Json.toJson(Attribute.findInvolving(id)));
    }

    public Result deleteClasse(String id){
        List<Attribute> attributes =  Attribute.findInvolving(id);
        for (Attribute e : attributes){
            e.delete();
        }
        Classe.find.byId(id).delete();
        return ok("deleted");
    }

    public Result register(){
        Form form = Form.form(batch.model.User.class);
        return ok(register.render(form));
    }

    public Result addUser(){
        Http.MultipartFormData<File> body = request().body().asMultipartFormData();
        Http.MultipartFormData.FilePart<File> picture = body.getFile("picture");
        batch.model.User user = Form.form(batch.model.User.class).bindFromRequest().get();
        if (picture != null) {
            String fileName = picture.getFilename();
            String contentType = picture.getContentType();
            File file = picture.getFile();
            OutputStream out = null;
            InputStream filecontent = null;
            try {
                out = new FileOutputStream(new File("public/template/dist/img/"+fileName));
                filecontent = new FileInputStream(file);
                int read = 0;
                final byte[] bytes = new byte[1024];

                while ((read = filecontent.read(bytes)) != -1) {
                    out.write(bytes, 0, read);
                }
                batch.model.User user1 = batch.model.User.create(user.email,user.password,user.first_name,user.last_name,"assets/template/dist/img/"+fileName);
                user1.save();
        } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (filecontent != null) {
                    try {
                        filecontent.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
            else {
            flash("error", "Missing file");
            return badRequest();
        }
        return redirect(routes.Application.login());
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