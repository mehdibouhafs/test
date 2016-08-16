package batch.model;


/**
 * Created by MBS on 13/08/2016.
 */
public class Login {

    public String email;
    public String password;
    public boolean rememberMe = false;

    public String validate() {
        if (User.authenticate(email, password) == null) {
            return "Invalid user or password";
        }
        return null;
    }

    @Override
    public String toString() {
        return "Login{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", rememberMe=" + rememberMe +
                '}';
    }
}
