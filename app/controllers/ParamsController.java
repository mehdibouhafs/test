package controllers;


import model.Row;
import model.Rows;
import model.UploadResult;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

import java.io.*;
import java.util.*;


/**
 * Created by MBS on 04/07/2016.
 */
public class ParamsController extends Controller {



    public Result addParameters() throws IOException {
        Rows rows = Form.form(Rows.class).bindFromRequest().get();

        return ok(rows.toString());
}
}
