package batch.model;

import com.avaje.ebean.Model;
import org.hibernate.validator.constraints.Email;
import org.mindrot.jbcrypt.BCrypt;
import play.data.format.Formats;
import play.data.validation.Constraints;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;


/**
 * Created by MBS on 14/08/2016.
 */

@Entity
@Table(name = "A_MBS3_USER")
public class User extends Model {

    @Id
    @Constraints.Required
    @Formats.NonEmpty
    @Email
    public String email;

    @Constraints.Required
    @Formats.NonEmpty
    public String first_name;

    @Constraints.Required
    @Formats.NonEmpty
    public String last_name;


    public String imagePath;

    @Constraints.Required
    @Constraints.MinLength(6)
    @Constraints.MaxLength(20)
    public String password;


    public Long job_completed;

    public Long job_failed;

    public Long job_abondonned;

    public Long total_jobs;

    public static Finder<String, User> find = new Finder<String,User>(User.class);


    public static User create(String email,String password,String first_name,String last_name,String imagePath) {
        User user = new User();
        user.email = email;
        user.last_name=last_name;
        user.imagePath=imagePath;
        user.first_name=first_name;
        user.password = BCrypt.hashpw(password, BCrypt.gensalt());
        user.job_abondonned = 0L;
        user.job_completed = 0L;
        user.job_failed = 0L;
        user.total_jobs = 0L;
        user.save();
        return user;
    }

    public static User createMobile(String email,String password,String first_name,String last_name) {
        User user = new User();
        user.email = email;
        user.last_name=last_name;
        user.first_name=first_name;
        user.password = BCrypt.hashpw(password, BCrypt.gensalt());
        user.imagePath = "assets/template/dist/img/avatar.png";
        user.job_abondonned = 0L;
        user.job_completed = 0L;
        user.job_failed = 0L;
        user.total_jobs = 0L;
        try {
            user.save();
            return user;
        }catch (Exception e){
            return null;
        }

    }




    public static List<User> findAll(){
        return find.all();
    }
    /*public static List<User> findById(){
        return User.find.orderBy("id").findList();
    }*/

    public static User authenticate(String email, String password) {
        //return find.where().eq("email", email).eq("password", password).findUnique();
        User user = User.find.where().eq("email", email).findUnique();
        if (user != null && BCrypt.checkpw(password, user.password)) {
            return user;
        } else {
            return null;
        }
    }

    public String validate() {
        if(User.find.byId(email)!=null){
            return  "Un utilisateur est déjà inscrit avec cet email !";
        }
        return null;
    }



    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", imagePath='" + imagePath + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
