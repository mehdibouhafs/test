package controllers;

import model.UploadResult;
import play.mvc.Controller;
import play.mvc.Result;
import scala.Int;

/**
 * Created by MBS on 04/07/2016.
 */
public class ParamsController extends Controller {

    public Result parameters(int id) {
        System.out.println("id ======"+id);
        UploadResult uploadResult = UploadResult.find.byId(id);

        return ok("params"+uploadResult.getName() + "id" + id);
    }
}
