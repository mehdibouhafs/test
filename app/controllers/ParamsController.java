package controllers;


import model.UploadResult;
import play.mvc.Controller;
import play.mvc.Result;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by MBS on 04/07/2016.
 */
public class ParamsController extends Controller {

    public Result parameters() throws IOException {
        String id = session("idFile");
        UploadResult uploadResult = UploadResult.find.byId(Integer.parseInt(id));


        return ok("ok");
    }



}
