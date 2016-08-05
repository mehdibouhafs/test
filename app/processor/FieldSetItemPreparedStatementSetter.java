package processor;

import model.App;
import model.Generator;
import model.ReaderGenerique;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;
import org.springframework.context.ApplicationContext;
import running.Global;

import java.lang.reflect.Field;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by MBS on 29/06/2016.
 */
public class FieldSetItemPreparedStatementSetter implements ItemPreparedStatementSetter<Object> {


    @Override
    public void setValues(Object item, PreparedStatement ps) throws SQLException {

        ApplicationContext context = Global.getApplicationContext();
        ReaderGenerique readerGenerique = context.getBean("readerGenerique", ReaderGenerique.class);
        try {
            System.out.println("FIELDSET"+ App.reflectToString(item));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        Generator generator = (Generator) context.getBean("generator");
        int i=1;
        for (Map.Entry<String, Class<?>> entry : generator.getProperties().entrySet()) {
            try {
                System.out.println("keyyyyyyyyyyyyyyyyyyyyyyyy = "+entry.getKey()+ "val = "+entry.getValue().getSimpleName());
                Field f = item.getClass().getDeclaredField(entry.getKey());
                f.setAccessible(true);
                System.out.println("IITEM " +item +" getItem " +f.get(item));
                switch (entry.getValue().getSimpleName()){
                    case "Integer":
                        System.out.println(i+"Integer :"+(Integer.parseInt(f.get(item).toString())));
                        ps.setInt(i,(Integer.parseInt(f.get(item).toString())));
                        i++;
                        break;
                    case "Boolean":
                        ps.setBoolean(i,(Boolean.parseBoolean(f.get(item).toString())));
                        i++;
                        break;
                    case "Double":
                        ps.setDouble(i,Double.parseDouble(f.get(item).toString()));
                        i++;
                        break;
                    case "Float":
                        ps.setFloat(i,Float.parseFloat(f.get(item).toString()));
                        i++;
                        break;
                    case "Long":
                        ps.setLong(i,Long.parseLong(f.get(item).toString()));
                        i++;
                        break;
                    case "String":
                        System.out.println(i+"String :"+ String.valueOf(f.get(item)));
                            ps.setString(i, f.get(item)+"");
                        i++;
                        break;
                    case "Date":
                        System.out.println("Date");
                        //System.out.println("Date"+i+":"+((java.util.Date)(f.get(item))).getTime());
                       /* final String NEW_FORMAT = "dd/MM/yyyy";
                        SimpleDateFormat sdf = new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
                        java.util.Date date = null;
                        try {
                            date = sdf.parse((f.get(item)).toString());
                            sdf.applyPattern(NEW_FORMAT);
                            String s = sdf.format(date);
                            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                            java.util.Date date1 = formatter.parse(s);
                            System.out.println("Date :"+ date1.toString());*/
                        try {
                            ps.setDate(i,  new Date(((java.util.Date)(f.get(item))).getTime()));
                            i++;
                        } catch (IllegalAccessException e) {
                            readerGenerique.getErrors().put("casting Date",e.getMessage());
                            e.printStackTrace();
                        }
                        break;
                    default:
                        System.out.println("DEFAULT");
                        break;
                }

            } catch (NoSuchFieldException e) {
                readerGenerique.getErrors().put("NoSuchFiledException",e.getMessage());
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                readerGenerique.getErrors().put("IllegalAccessException",e.getMessage());
                e.printStackTrace();
            }

        }
       /* try {
            ps.setString(1, App.getField(entry.getKey(),item));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }


        /*try {
            //ps.setDate(3, new Date(item.getClass().getMethod("getDate")));

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        System.out.println("ItemPREPARED"+item.getClass().getMethod("getFirstName").toString());*/
    }
}

