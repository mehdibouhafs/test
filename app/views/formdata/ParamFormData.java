package views.formdata;

/**
 * Created by MBS on 07/07/2016.
 */
public class ParamFormData {

    public String filePath;





    public ParamFormData() {
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
        //ApplicationContext context = Global.getApplicationContext();
       // ReaderGenerique readerGenerique = context.getBean("readerGenerique",ReaderGenerique.class);
        List<ValidationError> errors = new ArrayList<>();
          /*  File destination = new File(readerGenerique.getFilePath());
            FileReader fr = null;
            String s = "";
            try {
                fr = new FileReader(destination);
                BufferedReader br = new BufferedReader(fr);
                s = br.readLine();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }*/
           /* if (separator.equals("")) {
                errors.add(new ValidationError("separator", "No Separator was given."));
            }
          /*  if (s.contains(separator) == false) {
                errors.add(new ValidationError("separator", "Veuillez choisir le bon séparateur."));
            }*/

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

       /*if(errors.size() > 0) {
            System.out.println(errors);
            return errors;
        }

        return null;
    }*/


    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }




}
