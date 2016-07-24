package controllers;
import com.avaje.ebean.Model;
import model.*;
import org.springframework.context.ApplicationContext;
import play.data.Form;
import play.mvc.*;
import running.Global;
import views.formdata.ParamFormData;
import views.formdata.ParamFormData1;
import views.html.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static play.libs.Json.toJson;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {

    ApplicationContext context;

    public HomeController() {
        context = Global.getApplicationContext();
    }

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     *
     *
     */
    public Result index() {
        String[] types = {";",",",".","|",":"};
        List<String> type = new ArrayList<>(Arrays.asList(types));

        String[] seps = {";", ",", ".", "|", ":"};
        List<String> sep = new ArrayList<>(Arrays.asList(seps));

        ParamFormData paramData =  new ParamFormData();
        ParamFormData1 paramData1 = new ParamFormData1();
        Form<ParamFormData> formData = Form.form(ParamFormData.class).fill(paramData);
        Form<ParamFormData1> formData1 = Form.form(ParamFormData1.class);
        return ok(index.render(formData,null,null,
                null,
                null,
                null
        ));
    }

    public Result postIndex() {
        String[] types = {";", ",", ".", "|", ":"};
        List<String> type = new ArrayList<>(Arrays.asList(types));
        // Get the submitted form data from the request object, and run validation.
        Form<ParamFormData> formData = Form.form(ParamFormData.class).bindFromRequest();
        //system.out.println(formData.get().toString());
        /*if (formData.hasErrors()) {
            System.out.println("ERROR POST");
            // Don't call formData.get() when there are errors, pass 'null' to helpers instead.
            flash("error", "Please correct errors above.");
            Columns columns = context.getBean("columns",Columns.class);
            return badRequest( parameter.render(formData,
                    columns.makeColumnMap(null),
                    Type.makeTypeMap(null),
                    Separator.makeSeparatorMap(null)

            ));
        }
        else {
            // Convert the formData into a Student model instance.
            System.out.println("NO ERROR POST");
            //String tableName = formData.get().getTableName();
            List<Row> rows1 = formData.get().getRows();

            //System.out.println(tableName);
            for (Row r: rows1
                 ) {
                System.out.println(r);

            }
            List<Row> rows = Row.makeInstance(formData.get()); //maket
            System.out.println(rows);
            flash("success", "Column instance created/edited: " + rows);
            Columns columns = context.getBean("columns",Columns.class);
            return ok(parameter.render(formData,
                    columns.makeColumnMap(formData.get()),
                    Type.makeTypeMap(formData.get()),
                    Separator.makeSeparatorMap(formData.get())
            ));
        }
    }*/
        return null;
    }

    /*public Result getIndex(long id) {
        ParamFormData paramData = (id == 0) ? new ParamFormData() : model.Row.makeRowFormData(id);
        Form<ParamFormData> formData = Form.form(ParamFormData.class).fill(paramData);
        Columns columns = context.getBean("columns",Columns.class);
        return ok(parameter.render(
                formData,
                columns.makeColumnMap(paramData),
                Type.makeTypeMap(paramData),
                Separator.makeSeparatorMap(paramData)
        ));
    }*/

    /*public Result addClient() {
        Client client = Form.form(Client.class).bindFromRequest().get();
        client.save();
        return redirect(routes.HomeController.getIndex(client.getId()));
    }

    public Result getClients(){
        List<Client> clients = new Model.Finder(String.class,Client.class).all();
        return  ok(toJson(clients));
    }*/



    public ApplicationContext getContext() {
        return context;
    }

    public void setContext(ApplicationContext context) {
        this.context = context;
    }
}
