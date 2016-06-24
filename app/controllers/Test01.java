package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

/**
 * Created by MBS on 24/06/2016.
 */
public class Test01 extends Controller {

    Test01(){

    }

    public Result test(){
        return ok();
    }
}
