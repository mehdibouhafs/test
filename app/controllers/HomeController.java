package controllers;
import com.avaje.ebean.Model;
import model.*;
import play.data.Form;
import play.mvc.*;
import views.formdata.ParamFormData;
import views.html.*;
import java.util.List;
import static play.libs.Json.toJson;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {
    public HomeController() {
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
        return ok(index.render(1993));
    }

    public Result postIndex() {
        // Get the submitted form data from the request object, and run validation.
        Form<ParamFormData> formData = Form.form(ParamFormData.class).bindFromRequest();
        //system.out.println(formData.get().toString());
        if (formData.hasErrors()) {
            System.out.println("ERROR POST");
            // Don't call formData.get() when there are errors, pass 'null' to helpers instead.
            flash("error", "Please correct errors above.");
            return badRequest( parameter.render(formData,
                    Columns.makeColumnMap(null),
                    Type.makeTypeMap(null)

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
            return ok(parameter.render(formData,
                    Columns.makeColumnMap(formData.get()),
                    Type.makeTypeMap(formData.get())

            ));
        }
    }

    public Result getIndex(long id) {
        ParamFormData paramData = (id == 0) ? new ParamFormData() : model.Row.makeRowFormData(id);
        Form<ParamFormData> formData = Form.form(ParamFormData.class).fill(paramData);
        return ok(parameter.render(
                formData,
                Columns.makeColumnMap(paramData),
                Type.makeTypeMap(paramData)
        ));
    }


    public Result addClient() {
        Client client = Form.form(Client.class).bindFromRequest().get();
        client.save();
        return redirect(routes.HomeController.getIndex(client.getId()));

    }

    public Result getClients(){
        List<Client> clients = new Model.Finder(String.class,Client.class).all();
        return  ok(toJson(clients));
    }


    public Result getFiles(){
        List<UploadResult> uploadResults = new Model.Finder(String.class,UploadResult.class).all();
        return  ok(toJson(uploadResults));
    }

}
