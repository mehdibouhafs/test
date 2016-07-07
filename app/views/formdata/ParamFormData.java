package views.formdata;

import model.Columns;
import model.Row;
import model.Type;
import play.data.validation.ValidationError;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MBS on 07/07/2016.
 */
public class ParamFormData {

    public String tableName;

    public String name;

    public String type;

    public String bools;


    public ParamFormData() {
    }

    public ParamFormData(Columns name, Type type, boolean bools) {
        //this.tableName = tableName;
        this.name = name.getName();
        this.type = type.getName();

        this.bools = bools ? "true" : "false";

    }

    public ParamFormData(String tableName, String name, String type, String bools) {
        this.tableName = tableName;
        this.name = name;
        this.type = type;
        this.bools = bools;
    }

    /**
     * Validates Form<StudentFormData>.
     * Called automatically in the controller by bindFromRequest().
     *
     * Validation checks include:
     * <ul>
     * <li> Name must be non-empty.
     * <li> types is required and must exist in database.
     * <li> zise optional.
     * <li> add required.
     * </ul>
     *
     * @return Null if valid, or a List[ValidationError] if problems found.
     */
   /* public List<ValidationError> validate() {

        List<ValidationError> errors = new ArrayList<>();

        if(tableName == null){
            errors.add(new ValidationError("Table Name", "No Table name was given."));
        }

        if (name == null) {
            errors.add(new ValidationError("name", "No Column Name was given."));
        }

        // GPA is required and must exist in database.
        if (type == null) {
            errors.add(new ValidationError("type", "No type was given."));
        } else if (Type.findType(type) == null) {
            errors.add(new ValidationError("type", "Invalid TYPE: " + type + "."));
        }

        if(errors.size() > 0)
            return errors;

        return null;
    }*/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBools() {
        return bools;
    }

    public void setBools(String bools) {
        this.bools = bools;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public String toString() {
        return "ParamFormData{" +
                "tableName='" + tableName + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", bools='" + bools + '\'' +
                '}';
    }
}
