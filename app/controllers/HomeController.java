package controllers;

import com.avaje.ebean.Model;
import model.Client;
import model.UploadResult;
import play.data.Form;
import play.mvc.*;

import views.html.*;

import java.util.List;

import static play.libs.Json.toJson;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    public Result index() {
        return ok(index.render(1993));
    }


    public Result addClient() {
        Client client = Form.form(Client.class).bindFromRequest().get();
        client.save();
        return redirect(routes.HomeController.index());

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
