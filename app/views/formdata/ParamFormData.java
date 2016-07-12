package views.formdata;

import model.Columns;
import model.Row;
import model.Type;
import play.data.validation.ValidationError;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * Created by MBS on 07/07/2016.
 */
public class ParamFormData {

    public String separator;

    public int numberLine;

    public String tableName;

    public List<Row> rows;

    public ParamFormData() {
    }

    public int getNumberLine() {
        return numberLine;
    }

    public void setNumberLine(int numberLine) {
        this.numberLine = numberLine;
    }

    public ParamFormData(List<Row> rows) {
        this.rows = rows;
    }

    public ParamFormData(String tableName, List<Row> rows) {
        this.tableName = tableName;
        this.rows = rows;
    }

    public List<Row> getRows() {
        return rows;
    }
    public void setRows(List<Row> rows) {
        this.rows = rows;
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
    public List<ValidationError> validate() {

        List<ValidationError> errors = new ArrayList<>();

        if(separator == null){
            errors.add(new ValidationError("separator", "No Separator was given."));
        }

        /*if(tableName == null || tableName.equals("")){
            errors.add(new ValidationError("tableName", "No table name was given."));
        }*/
       /* for(int i=0;i<rows.size();i++) {
            //System.out.println("rows["+i+"].name="+rows.get(i).name);
            if (rows.get(i).name.equals("")) {
                errors.add(new ValidationError("rows["+i+"]", "No column name was given for the row number ("+i+")"));
            }
            if(rows.get(i).type.equals("")){
                errors.add(new ValidationError("rows["+i+"]", "No type was given for the row number ("+i+")"));
            }
        }
*/
        if(errors.size() > 0) {
            System.out.println(errors);
            return errors;
        }

        return null;
    }

    public String getSeparator() {
        return separator;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
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
                ", rows=" + rows +
                '}';
    }
}
