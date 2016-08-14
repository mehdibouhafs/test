package batch.model;

import com.avaje.ebean.Model;
import org.hibernate.validator.constraints.Email;
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

    public static Finder<String, User> find = new Finder<String,User>(User.class);

    public static List<User> findAll(){
        return find.all();
    }
    /*public static List<User> findById(){
        return User.find.orderBy("id").findList();
    }*/

    public static User authenticate(String email, String password) {
        return find.where().eq("email", email).eq("password", password).findUnique();
    }
}
