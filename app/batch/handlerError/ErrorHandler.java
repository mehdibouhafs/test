package batch.handlerError;

import batch.model.User;
import batch.security.Secured;
import play.http.HttpErrorHandler;
import play.mvc.*;
import play.mvc.Http.*;
import play.libs.F.*;
import views.html.error;


public class ErrorHandler extends Controller implements HttpErrorHandler {
    public Promise<Result> onClientError(RequestHeader request, int statusCode, String message) {

        User user = User.find.byId(session("email"));
        return Promise.<Result>pure(
                Results.badRequest(error.render(message,user))
        );
    }

    public Promise<Result> onServerError(RequestHeader request, Throwable exception) {
        User user = User.find.byId(session("email"));
        return Promise.<Result>pure(
                Results.badRequest(error.render("Error Serveur "+exception.getMessage(),user))
        );
    }
}