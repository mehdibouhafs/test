package controllers;
import batch.business.BatchJobService;
import batch.business.BatchJobServiceImpl;
import batch.dao.BatchJobDao;
import batch.util.Generator;
import batch.model.*;
import batch.model.Reader;
import batch.model.batch.BatchExecutionParam;
import batch.model.batch.BatchJobExecution;
import batch.model.batch.BatchStepExecution;
import batch.security.Secured;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import javassist.*;
import batch.listeners.JobCompletionNotificationListener;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.batch.core.*;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.context.ApplicationContext;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.*;
import play.mvc.Controller;
import running.Global;
import views.formdata.*;
import views.html.*;

import java.io.*;
import java.util.*;

/**
 * Created by MBS on 27/06/2016.
 */

public class Application extends Controller {

    public Application() {
    }

    public Result csvHeader() throws IOException {
        BatchJobService batchJobService = new BatchJobServiceImpl();
        DynamicForm form = Form.form().bindFromRequest();
        Reader reader = new Reader();
        reader.filePath = form.field("filePath").value();
        reader.separator =  form.field("separator").value();
        reader.nbLineToSkip =  Integer.parseInt(form.field("nbLineToSkip").value());
        reader.emailUser = session("email");
        String entete = form.field("entete").value();
        StringBuffer cols = new StringBuffer();
        List<String> s;
        if(entete != null){
            s = batchJobService.firstLineCsvFile(new File(reader.filePath),reader.separator) ;
            System.out.println("on s" + s);
        }else{
            int i = 0;
            List<String> colss = new ArrayList<>();
            while(form.field("cols["+i+"]").value()!=null){
                colss.add(form.field("cols["+i+"]").value());
                i++;
            }
            s = colss;
        }
        int i = 0;
        for (String col : s ) {
            if(i == 0) {
                cols.append(col);
                i++;
            }else{
                cols.append(","+col);
            }
        }
        reader.columns = cols.toString();
        reader.save();
        return ok(Json.toJson(s));
    }

    public Result csvExistConfig(){
        BatchJobService batchJobService = new BatchJobServiceImpl();
        DynamicForm form = Form.form().bindFromRequest();
        String classe = form.field("classe").value();
        Reader reader = new Reader();
        reader.filePath = form.field("filePath").value();
        reader.separator =  form.field("separator").value();
        reader.nbLineToSkip =  Integer.parseInt(form.field("nbLineToSkip").value());
        reader.emailUser = session("email");
        reader.tableName = form.field("tableName").value();
        reader.classeName = form.field("tableName").value();
        String entete = form.field("entete").value();
        StringBuffer cols = new StringBuffer();
        List<String> s;
        if(entete != null){
            System.out.println("on");
            s = batchJobService.firstLineCsvFile(new File(reader.filePath),reader.separator) ;
        }else{
            int i = 0;
            List<String> colss = new ArrayList<>();
            while(form.field("cols["+i+"]").value()!=null){
                colss.add(form.field("cols["+i+"]").value());
                i++;
            }
            s = colss;
        }
        int i = 0;
        for (String col : s ) {
            if(i == 0) {
                cols.append(col);
                i++;
            }else{
                cols.append(","+col);
            }
        }
        Attribute newAttribute;
        int ii=0;
        for(Attribute attribute : Attribute.findInvolving(classe)){
            newAttribute = new Attribute();
            newAttribute.setType(attribute.getType());
            newAttribute.setSizeo(attribute.getSizeo());
            newAttribute.setNonNull(attribute.isNonNull());
            newAttribute.setCommentaires(attribute.getCommentaires());
            newAttribute.setClasse(form.field("tableName").value());
            newAttribute.setNameo(s.get(ii));
            newAttribute.save();
            ii++;
        }
        Classe c = new Classe();
        c.className = form.field("tableName").value();
        c.user_email = session("email");
        c.save();
        reader.columns = cols.toString();
        reader.tableName=form.field("tableName").value();
        reader.save();
        return  ok(Json.toJson(Attribute.findInvolving(form.field("tableName").value())));
    }



    public Result colsXml() throws IOException {
        BatchJobService batchJobService = new BatchJobServiceImpl();
        Form<Reader> form = Form.form(Reader.class).bindFromRequest();
        Reader reader = new Reader();
        reader.filePath = form.get().filePath;
        reader.separator="";
        reader.emailUser = session("email");
        reader.save();
        String[] s = batchJobService.getElementAndAttributesFileXml(reader);
        StringBuffer cols = new StringBuffer();
        int i = 0;
        for (String col : s ) {
            if(i == 0) {
                cols.append(col);
                i++;
            }else{
                cols.append(","+col);
            }
        }
        reader.columns = cols.toString();
        reader.update();
        return ok(Json.toJson(s));
    }



    public Result xmlExistConfig(){
        BatchJobService batchJobService = new BatchJobServiceImpl();
        DynamicForm form = Form.form().bindFromRequest();
        String classe = form.field("classe").value();
        Reader reader = new Reader();
        reader.separator="";
        reader.filePath = form.field("filePath").value();
        reader.emailUser = session("email");
        String[] s = batchJobService.getElementAndAttributesFileXml(reader);
        StringBuffer cols = new StringBuffer();
        int i = 0;
        for (String col : s ) {
            if(i == 0) {
                cols.append(col);
                i++;
            }else{
                cols.append(","+col);
            }
        }
        reader.columns = cols.toString();
        reader.update();
        Attribute newAttribute;
        int ii=0;
        for(Attribute attribute : Attribute.findInvolving(classe)){
            newAttribute = new Attribute();
            newAttribute.setType(attribute.getType());
            newAttribute.setSizeo(attribute.getSizeo());
            newAttribute.setNonNull(attribute.isNonNull());
            newAttribute.setCommentaires(attribute.getCommentaires());
            newAttribute.setClasse(form.field("tableName").value());
            newAttribute.setNameo(Arrays.asList(s).get(ii));
            newAttribute.save();
            ii++;
        }
        Classe c = new Classe();
        c.className = reader.tableName;
        c.user_email = session("email");
        c.update();
        reader.columns = cols.toString();
        reader.save();
        return  ok(Json.toJson(Attribute.findInvolving(classe)));
    }


    public Result csvHeaderColsSelected(){
        Reader reader = Reader.getReaderUser(session("email"));
        DynamicForm dynamicForm = Form.form().bindFromRequest();
        Collection<String> columns =  dynamicForm.data().values();
        List<String> list = new ArrayList<>(columns);
        Collections.reverse(list);
        return  ok(Json.toJson(list));
    }


    public Result addAttributeReader(){
        Form<Reader> form = Form.form(Reader.class).bindFromRequest();
        return  ok(Json.toJson(form.get().cols));
    }


    public Result getTypes() throws NoSuchMethodException, IllegalAccessException, ClassNotFoundException, InstantiationException, CannotCompileException, NotFoundException {
        ApplicationContext context = Global.getApplicationContext();
        Map<String,String> columnTable =  new LinkedHashMap<>();
        DynamicForm form = Form.form().bindFromRequest();
        JobCompletionNotificationListener jobCompletionNotificationListener = (JobCompletionNotificationListener) context.getBean("listener");
        jobCompletionNotificationListener.setUser(User.find.byId(session("email")));
        Reader reader = Reader.find.byId(Reader.maxId());
        List<Attribute> attributes = new ArrayList<>();
        String classe = form.field("tableName").value();
        reader.classeName = classe;
        Reader readerDelete = Reader.getbyClasse(reader.classeName);
        if(readerDelete != null){
            readerDelete.delete();
        }
        int i= 0;
        Attribute attribute;
        while(form.field("cols["+i+"]").value()!= null){
            attribute = new Attribute();
            if(form.field("pk["+i+"]").value() !=null) {
                if (form.field("pk[" + i + "]").value().equals("primaryKey")) {
                    attribute.setPko(true);
                }
            }
            attribute.setNameo(form.field("cols["+i+"]").value());
            attribute.setType(form.field("type["+i+"]").value());
            attribute.setSizeo(form.field("size["+i+"]").value());
            if(form.field("nonNull["+i+"]").value() !=null) {
                if (form.field("nonNull[" + i + "]").value().equals("notNull")) {
                    attribute.setNonNull(true);
                }
            }
               attribute.setDefaut(form.field("defaultVal[" + i + "]").value());
                attribute.setCommentaires(form.field("commentaire[" + i + "]").value());
            attribute.setClasse(form.field("tableName").value());
            attributes.add(attribute);
            i++;
        }
        BatchJobDao batchJobDao = (BatchJobDao) context.getBean("batchJobDao");
        BatchJobService batchJobService = new BatchJobServiceImpl(reader,batchJobDao);
        batchJobService.typeAttributes(attributes,reader);
        String dropeTable = form.field("dropeTable").value();
        if (dropeTable.equals("true")) {
            batchJobDao.dropTable(classe);
        }
        Boolean create = batchJobDao.createTableOracle(reader,attributes);
        if(create) {
            return ok(Json.toJson(attributes));
        }else{
            return null;
        }
    }

    public Result runjob(){
        DynamicForm form = Form.form().bindFromRequest();
        Long id = Long.valueOf(form.field("id").value());
        Reader reader = Reader.find.byId(id);
        reader.executed_by = session("email");
        reader.dateLancement = new Date();
        reader.update();
        BatchJobService service = new BatchJobServiceImpl();
        Resume resume =  service.doJob(reader);
        ApplicationContext context = Global.getApplicationContext();
        JobCompletionNotificationListener listener = context.getBean("listener", JobCompletionNotificationListener.class);
        if(resume==null){
            resume = listener.getResume();
        }
        return ok(Json.toJson(resume));
    }


    public Result programJob(){
        DynamicForm form  = Form.form().bindFromRequest();
        String classe = form.field("classe").value();
        Long date = Long.parseLong(form.field("date").value());
        System.out.println(date);
        ApplicationContext context = Global.getApplicationContext();
        Programing programing = (Programing) context.getBean("programming");
        Reader reader = Reader.getbyClasse(classe);
        reader.executed = false;
        reader.resultat = false;
        reader.executed_by = session("email");
        reader.dateLancement = new Date(date);
        reader.nbLinesFailed = 0L;
        reader.nbLinesSuccess = 0L;
        reader.update();
        programing.setReader(reader);
        programing.setDate(new Date(date));
        programing.executeTaskT();
        System.out.println("Classe = "+ classe );
        return ok("ok");
    }

    public Result editReader() {
        DynamicForm form = Form.form().bindFromRequest();
        System.out.println("form " + form);
        Long id = Long.parseLong(form.field("id").value());
        System.out.println("id = " + id);
        String file = form.field("file").value();
        String separator = "";
        if(form.field("separator").value() != null) {
             separator = form.field("separator").value();
        }
        System.out.println("separatorr " + separator);
        String sep1="";
            switch (separator) {
                case "1":
                    sep1 = ";";
                    break;
                case "2":
                    sep1 = ",";
                    break;
                case "3":
                    sep1 = ":";
                    break;
                case "4":
                    sep1 = "|";
                    break;
                default:
                    sep1 = "";
                    break;
            }

        Reader reader = Reader.find.byId(id);
        System.out.println( "readeres "+reader);
        BatchJobService batchJobService = new BatchJobServiceImpl();
        if (batchJobService.getExtension(reader.filePath).equals("csv")) {
            if(reader.separator!=null) {
                System.out.println("TEST 1");
                if (reader.separator.equals(sep1) && reader.filePath.equals(file)) {
                    System.out.println("TEST 2");
                    return ok("memememe");
                }
            }
        }
                reader.jobId = null;
                reader.filePath = file;
                reader.separator = sep1;
                reader.dateLancement = null;
                reader.dateCreation = new Date();
                reader.executed = false;
                reader.executed_by = null;
                reader.emailUser = session("email");
                reader.resultat = false;
                reader.nbLinesSuccess = 0L;
                reader.nbLinesFailed = 0L;
                reader.update();
                return ok("ok");
    }





    public Result nbColCsvNoHead(){
        BatchJobService batchJobService = new BatchJobServiceImpl();
        DynamicForm form = Form.form().bindFromRequest();
        String path = form.field("filePath").value();
        String separator = form.field("separator").value();
            List<String> s = batchJobService.firstLineCsvFile(new File(path),separator);
            ObjectNode result = play.libs.Json.newObject();
            result.put("size",s.size());
            return ok(result);
    }





    public Result delete(int id) {
        //colsSelectedMap.remove(id);
        //attributtes.remove(id);
        return ok("removed");
    }


    public Result metadata() {
        ApplicationContext context = Global.getApplicationContext();
        BatchJobDao batchJobDao = (BatchJobDao) context.getBean("batchJobDao");
        BatchJobService batchJobService = new BatchJobServiceImpl(batchJobDao);
        String[] table = request().body().asFormUrlEncoded().get("tableName");
        Map<String, String> metadats = batchJobService.dataTable(table[0]);
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


    @Security.Authenticated(Secured.class)
    public Result index() {
        return ok(index.render(batch.model.User.find.byId(request().username())));
    }
    @Security.Authenticated(Secured.class)
    public Result param() {
        return ok(parameter.render(batch.model.User.find.byId(request().username())));
    }

    @Security.Authenticated(Secured.class)
    public Result param2() {
        return ok(parameter2.render(batch.model.User.find.byId(request().username())));
    }

    @Security.Authenticated(Secured.class)
    public Result param3() {
        return ok(parameter3.render(batch.model.User.find.byId(request().username())));
    }

    @Security.Authenticated(Secured.class)
    public Result param4() {
        return ok(parameter4.render(batch.model.User.find.byId(request().username())));
    }

    public Result logout() {
        session().clear();
        response().discardCookie("rememberme");
        flash("success", "You've been logged out");
        return redirect(routes.Application.login());
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
        Form<EditPassword> form = Form.form(EditPassword.class).bindFromRequest();
        if (form.hasErrors()) {
            return badRequest(editPassword.render(form,user));
        }else{
            if(BCrypt.checkpw(form.get().exPassword, user.password)){
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
        List<Reader> readers = Reader.getByUser(session("email"));
        return ok(Json.toJson(readers));
    }


    public Result allMyJobCompleted(){
        List<Reader> readers = Reader.getByUserAndCompleted(session("email"));
        return ok(Json.toJson(readers));
    }


    public Result allMyJobFailed(){
        List<Reader> readers = Reader.getByUserAndNotCompleted(session("email"));
        return ok(Json.toJson(readers));
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
        if (loginForm.hasErrors()) {
            return badRequest(login.render(loginForm));
        } else {
            batch.model.User user = batch.model.User.authenticate(loginForm.get().email,loginForm.get().password);
            if(user!=null){
            session().clear();
            session("email", loginForm.get().email);
                if(loginForm.get().rememberMe == true) {
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


    public Result authenticate2() {
        Form<Login> loginForm = Form.form(Login.class).bindFromRequest();
        if (loginForm.hasErrors()) {
            return badRequest(login.render(loginForm));
        } else {
            batch.model.User user = batch.model.User.authenticate(loginForm.get().email,loginForm.get().password);
            if(user!=null){
                return ok(Json.toJson(user));
            }  else{
                return null;
            }
        }
    }


    public Result lockScreen(){
        batch.model.User user = batch.model.User.find.byId(session(("email")));
        return  ok(lockscreen.render(user));
    }



    public Result delockScreen(){
        String[] password = request().body().asFormUrlEncoded().get("password");
        batch.model.User user = batch.model.User.authenticate(session(("email")),password[0]);
        if(user != null){
            session("email",user.email);
            response().setCookie("rememberme", play.api.libs.Crypto.crypto().sign(user.email) + "-" + user.email, 60*60*24*360);
            return ok(index.render(user));
        }else{
            return  ok(lockscreen.render(User.find.byId(session("email"))));
        }
    }



    public Result getReaders(){
        for (Reader r: Reader.find.all()) {
            if(r.classeName == null){
                r.delete();
            }
        }
        if(Reader.find.where()!=null){
        return ok(Json.toJson(Reader.find.where().orderBy("DATE_CREATION DESC").findList()));
        }else{
            return ok(Json.toJson(new Reader()));
        }
    }

    public Result getReader(){
        DynamicForm form = Form.form().bindFromRequest();
        Long id = Long.parseLong(form.field("id").value());
        return ok(Json.toJson(Reader.find.byId(id)));
    }



    public Result getAttributes(String  id){
        return  ok(Json.toJson(Attribute.findInvolvingNotEqual(id)));
    }

    public Result getResume(String id){
        Resume resume = new Resume();
        Reader reader = Reader.getbyClasse(id);
        resume.setReader(reader);
        BatchStepExecution stepExecution = BatchStepExecution.findByJobExecID(reader.jobId);
        resume.setBatchStepExecution(stepExecution);
        List<Attribute> attributes = Attribute.findInvolvingNotEqual(id);
        resume.setAttributes(attributes);
        List<InputError> inputErrors = InputError.findByJobExecutionId(reader.jobId);
        resume.setInputError(inputErrors);
        if(session("email")!=null) {
            User user = User.find.byId(session("email"));
            resume.setUser(user);
        }

        return ok(Json.toJson(resume));
    }


    public Result deleteReader(Long id){
        Reader r = Reader.find.byId(id);
        Classe.find.byId(r.classeName).delete();
        Attribute.dropInvolving(r.classeName);
        r.delete();
        return ok("deleted");
    }



    public Result register(){
        Form form = Form.form(batch.model.User.class);
        return ok(register.render(form));
    }








    public Result addUser(){
        Http.MultipartFormData<File> body = request().body().asMultipartFormData();
        Http.MultipartFormData.FilePart<File> picture = body.getFile("picture");
        Form<User> form = Form.form(batch.model.User.class).bindFromRequest();
        if (form.hasErrors()) {
            return badRequest(register.render(form));
        }else {
            batch.model.User user = Form.form(batch.model.User.class).bindFromRequest().get();
            if (picture != null) {
                String fileName = picture.getFilename();
                String contentType = picture.getContentType();
                File file = picture.getFile();
                OutputStream out = null;
                InputStream filecontent = null;
                try {
                    out = new FileOutputStream(new File("public/template/dist/img/" + fileName));
                    filecontent = new FileInputStream(file);
                    int read = 0;
                    final byte[] bytes = new byte[1024];

                    while ((read = filecontent.read(bytes)) != -1) {
                        out.write(bytes, 0, read);
                    }
                    batch.model.User user1 = batch.model.User.create(user.email, user.password, user.first_name, user.last_name, "assets/template/dist/img/" + fileName);

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
            } else {
                flash("error", "Missing file");
                return badRequest();
            }
            return redirect(routes.Application.login());
        }
    }

    public Result addUserMobile(){
            DynamicForm form = Form.form().bindFromRequest();
            String nom = form.field("nom").value();
            String prenom = form.field("prenom").value();
            String email = form.field("email").value();
            String password = form.field("password").value();
            String password1 = form.field("password1").value();
             if(!password.equals(password1)){
            return ok(Json.toJson("Password not the same"));
            }
            batch.model.User user1 = batch.model.User.createMobile(email, password, prenom, nom);
            if(user1!=null){
                return ok(Json.toJson(user1));
            }else {
                return ok(Json.toJson("false"));
            }

    }





    public Result viewedClasses(){
        List<Classe> classe = Classe.find.where().eq("viewed",0).findList();
        return ok(Json.toJson(classe));
    }




    public Result viewedClasse(){
        List<Classe> classes = Classe.find.where().eq("viewed",0).findList();
        for (Classe classe : classes){
            classe.viewed = 1;
            classe.update();
        }
        return ok("ok");
    }

    public Result getReadersdXml(){
        return  ok(Json.toJson(Reader.getReaderXMl()));
    }

    public Result getReadersCsv(){
        return ok(Json.toJson(Reader.getReaderCSV()));
    }







}

