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
        System.out.println(" -----------------------------Demarage application-------------------------");
    }

    public Result csvHeader() throws IOException {
        BatchJobService batchJobService = new BatchJobServiceImpl();
        Form<Reader> form = Form.form(Reader.class).bindFromRequest();
        System.out.println(form.get());
        if(Reader.getReaderUser(session("email")) != null){
            Reader.getReaderUser(session("email")).delete();
        }
        Reader reader = new Reader();
        reader.filePath = form.get().filePath;
        reader.separator = form.get().separator;
        reader.nbLineToSkip = form.get().nbLineToSkip;
        reader.emailUser = session("email");
        String[] s = batchJobService.firstLineCsvFile(new File(reader.filePath),reader.separator) ;
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
        reader.save();
        return ok(Json.toJson(s));
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
        DynamicForm form = Form.form().bindFromRequest();
        JobCompletionNotificationListener jobCompletionNotificationListener = (JobCompletionNotificationListener) context.getBean("listener");
        jobCompletionNotificationListener.setUser(User.find.byId(session("email")));
        Reader reader = Reader.getReaderUser(session("email"));
        List<Attribute> attributes = new ArrayList<>();
        String classe = form.field("tableName").value();
        reader.classeName = classe;
        reader.update();
        int i= 0;
        while(form.field("cols["+i+"]").value()!= null){
            Attribute attribute = new Attribute();
            if(form.field("pk["+i+"]").value() !=null) {
                if (form.field("pk[" + i + "]").value().equals("primaryKey")) {
                    attribute.setPko(true);
                }
            }
            attribute.setId(reader.id);
            attribute.setNameo(form.field("cols["+i+"]").value());
            attribute.setType(form.field("type["+i+"]").value());
            attribute.setSizeo(form.field("size["+i+"]").value());
            if(form.field("nonNull["+i+"]").value() !=null) {
                if (form.field("nonNull[" + i + "]").value().equals("notNull")) {
                    attribute.setNonNull(true);
                }
            }
            attribute.setDefautlVal(form.field("defaultVal["+i+"]").value());
            attribute.setCommentaire(form.field("commentaire["+i+"]").value());
            attribute.setClasse(form.field("tableName").value());
            attributes.add(attribute);
            i++;
        }
        BatchJobDao batchJobDao = (BatchJobDao) context.getBean("batchJobDao");
        BatchJobService batchJobService = new BatchJobServiceImpl(reader,classe,batchJobDao);
        Map<String,String> columnTable =   batchJobService.columnsWithTypeAndSize(attributes);
        batchJobService.typeAttributes(attributes);
        String dropeTable = form.field("dropeTable").value();
        if (dropeTable.equals("true")) {
            batchJobDao.dropTable(classe);
        }
        Boolean create = batchJobDao.createTableOracle(classe, columnTable,attributes);
        if (create) {
            Resume resume = batchJobService.doJob(reader,classe);
            return ok(Json.toJson(resume));
        }else{
            return ok("errors");
        }
    }

    public Result csvNoHead(){
        Form<Reader> form = Form.form(Reader.class).bindFromRequest();
        System.out.println(form.get());
        if (form.hasErrors()) {
            // Don't call formData.get() when there are errors, pass 'null' to helpers instead.
            flash("error", "Please correct errors above.");
            return badRequest(index.render(batch.model.User.find.byId(request().username())));
        }
        if(Reader.getReaderUser(session("email")) != null){
            Reader.getReaderUser(session("email")).delete();
        }
        Reader reader = new Reader();
        reader.filePath = form.get().filePath;
        reader.separator = form.get().separator;
        reader.nbLineToSkip = form.get().nbLineToSkip;
        reader.emailUser = session("email");
        reader.cols = form.get().cols;

        StringBuffer cols = new StringBuffer();
        int i=0;
        for (String col: reader.cols
             ) {
            if(i==0){
                cols.append(col);
            }else{
                cols.append(","+col);
            }
            i++;
        }
        reader.columns = cols.toString();
        reader.save();
        System.out.println("cols "+ cols);
        return ok(Json.toJson(reader.cols));
    }

    public Result nbColCsvNoHead(){
        BatchJobService batchJobService = new BatchJobServiceImpl();
        DynamicForm form = Form.form().bindFromRequest();
        System.out.println(form.data());
        String path = form.field("filePath").value();
        String separator = form.field("separator").value();
        System.out.println("sep "+ separator);
            String[] s = batchJobService.firstLineCsvFile(new File(path),separator);
            ObjectNode result = play.libs.Json.newObject();
            for (String ss:s){
                System.out.println("s"+ss);
            }
            result.put("size",s.length);
            return ok(result);

    }


    public Result colsXml() throws IOException {
        BatchJobService batchJobService = new BatchJobServiceImpl();
        Form<Reader> form = Form.form(Reader.class).bindFromRequest();
        Reader reader = Global.getApplicationContext().getBean("reader",Reader.class);
        if(Reader.getReaderUser(session("email")) != null){
            Reader.getReaderUser(session("email")).delete();
        }
        Reader reader1 = new Reader();
        reader1.filePath = form.get().filePath;
        reader1.emailUser = session("email");
        String[] s = batchJobService.getElementAndAttributesFileXml(new File(reader1.filePath));
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
        reader1.columns = cols.toString();

        reader1.save();
        reader = reader1;
        return ok(Json.toJson(s));
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
        System.out.println("metada table" + table[0]);
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
    public Result param1() {
        return ok(parameter1.render(batch.model.User.find.byId(request().username())));
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
            return  ok(lockscreen.render(User.find.byId(session("email"))));
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
            e.find.where().eq("classe",id).delete();
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

    public Result csvExistHeader(){
        ApplicationContext context = Global.getApplicationContext();
        Form<Reader> form = Form.form(Reader.class).bindFromRequest();
        BatchJobDao batchJobDao = (BatchJobDao) context.getBean("batchJobDao");

        Reader reader1 = context.getBean("reader",Reader.class);
        if(Reader.getReaderUser(session("email")) != null){
            Reader.getReaderUser(session("email")).delete();
        }
        Reader reader = new Reader();
        reader.fragmentRootName = form.get().fragmentRootName;
        reader.filePath = form.get().filePath;
        reader.separator = form.get().separator;
        reader.nbLineToSkip = form.get().nbLineToSkip;
        Classe classe = Classe.find.byId(reader.fragmentRootName);
        System.out.println ("Reader BY CLASSe " +  classe);
        reader.columns = classe.columns;
        List<Attribute> attributes = Attribute.findInvolving(reader.fragmentRootName);
        reader.save();
        BatchJobService batchJobService = new BatchJobServiceImpl(reader,reader.fragmentRootName,batchJobDao);
        Map<String,Class<?>> properties = batchJobService.typeAttributes(Attribute.findInvolving(classe.className));
        JobParameters param = new JobParametersBuilder()
                .addString("input.file.name", reader.filePath)
                .addString("separator",reader.separator)
                .addString("columns",reader.columns)
                .addString("nbLineToSkip",reader.nbLineToSkip+"")
                .addString("cData",batchJobDao.getCdata())
                .addLong("time", System.currentTimeMillis()).toJobParameters();

        JobLauncher jobLauncher = (JobLauncher) context.getBean("jobLauncher");
        try {
            if (batchJobService.getExtension(reader.filePath).equals("csv")) {
                Job job = (Job) context.getBean("csvJob");
                JobExecution jobExecution = jobLauncher.run(job, param);
                if (jobExecution.getStatus().equals(BatchStatus.COMPLETED)) {
                    List<InputError> inputErros = InputError.find.where().eq("job_execution_id",jobExecution.getId()).findList();
                    Resume resume = new Resume();
                    System.out.println("job ID " + jobExecution.getId());
                    System.out.println("Batch "+ BatchStepExecution.find.byId(jobExecution.getId()));
                    resume.setBatchStepExecution(BatchStepExecution.findByJobExecID(jobExecution.getId()));
                    resume.setInputError(inputErros);
                    Generator c = context.getBean("generator", Generator.class);
                    c.setClassGenerate(null);
                    return ok(Json.toJson(resume));
                }else{
                    //batchJobDao.dropTable(reader.fragmentRootName);
                    //Classe.dropClasseAndAttribute(reader.fragmentRootName);
                }
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
        return null;
    }


    public Result existXml(){
        ApplicationContext context = Global.getApplicationContext();
        Form<Reader> form = Form.form(Reader.class).bindFromRequest();
        BatchJobDao batchJobDao = (BatchJobDao) context.getBean("batchJobDao");
        Reader reader1 = context.getBean("reader",Reader.class);
        if(Reader.getReaderUser(session("email")) != null){
            Reader.getReaderUser(session("email")).delete();
        }
        Reader reader = new Reader();
        reader.fragmentRootName = form.get().fragmentRootName;
        reader.filePath = form.get().filePath;
        Classe classe = Classe.find.byId(reader.fragmentRootName);
        System.out.println ("Reader BY CLASSe " +  classe);
        reader.columns = classe.columns;
        System.out.println("reader columns " +  reader.columns);
        System.out.println("Attribute");
        reader.save();
        BatchJobService batchJobService = new BatchJobServiceImpl(reader,reader.fragmentRootName,batchJobDao);
        batchJobService.getElementAndAttributesFileXml(new File(reader.filePath));
        batchJobService.typeAttributes(Attribute.findInvolving(classe.className));
        JobParameters param = new JobParametersBuilder()
                .addString("input.file.name", reader.filePath)
                .addString("cData",classe.cData)
                .addLong("time", System.currentTimeMillis()).toJobParameters();
        JobLauncher jobLauncher = (JobLauncher) context.getBean("jobLauncher");
        try {
            Job job = (Job) context.getBean("importXML");
            JobExecution jobExecution = jobLauncher.run(job, param);
            JobCompletionNotificationListener listener = context.getBean("listener", JobCompletionNotificationListener.class);
            if (jobExecution.getStatus().equals(BatchStatus.COMPLETED)) {
                List<InputError> inputErros = InputError.find.where().eq("job_execution_id",jobExecution.getId()).findList();
                Resume resume = new Resume();
                System.out.println("job ID " + jobExecution.getId());
                System.out.println("Batch "+ BatchStepExecution.find.byId(jobExecution.getId()));
                resume.setBatchStepExecution(BatchStepExecution.findByJobExecID(jobExecution.getId()));
                resume.setInputError(inputErros);
                Generator c = context.getBean("generator", Generator.class);
                c.setClassGenerate(null);
                return ok(Json.toJson(resume));
            }else{
               // batchJobDao.dropTable(reader.fragmentRootName);
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
    return null;

    }

}