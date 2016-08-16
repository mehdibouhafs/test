package views.formdata;


/**
 * Created by MBS on 14/08/2016.
 */
public class EditPassword {

    public String exPassword;

    public String password;

    public String comfirmedPassword;


    public EditPassword() {
    }

    public String validate() {
        if (!password.equals(comfirmedPassword)) {
            return "PAssword non correspondant";
        }
        return null;
    }


    public String getComfirmedPassword() {
        return comfirmedPassword;
    }

    public void setComfirmedPassword(String comfirmedPassword) {
        this.comfirmedPassword = comfirmedPassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getExPassword() {
        return exPassword;
    }

    public void setExPassword(String exPassword) {
        this.exPassword = exPassword;
    }
}
