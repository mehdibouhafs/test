package controllers;
import com.google.inject.Inject;
import model.Client;
import model.Columns;
import model.Separator;
import model.Type;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Qualifier;
import play.data.Form;
import play.mvc.*;
import play.mvc.Controller;
import views.formdata.ParamFormData;


/**
 * Created by MBS on 27/06/2016.
 */

@Component("application")
public class Application extends Controller {


    public Application(){
        System.out.println(" -----------------------------Demarage application Construct------------------by Mehdi Bouhafs Encadre by - MIMO");
    }

    public Result home() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        //ApplicationContext context = new ClassPathXmlApplicationContext("context.xml");
        //Application app = (Application) context.getBean("application");
        //String s = app.encadrant.getName();
        //encadrant.setName("ok");
        //this.encadrant = Global.getBean(encadrant.getClass());

        ParamFormData paramData =  new ParamFormData();
        Form<ParamFormData> formData = Form.form(ParamFormData.class).fill(paramData);


        return ok(views.html.upload.render());
    }






}
