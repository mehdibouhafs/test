package processor;

import com.avaje.ebean.Model;
import model.App;
import model.Generator;
import model.Person;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;
import org.springframework.context.ApplicationContext;
import running.Global;

import java.lang.reflect.Field;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Map;

/**
 * Created by MBS on 29/06/2016.
 */
public class FieldSetItemPreparedStatementSetter implements ItemPreparedStatementSetter<Object> {


    @Override
    public void setValues(Object item, PreparedStatement ps) throws SQLException {
        System.out.println(item);
        ApplicationContext context = Global.getApplicationContext();
        Generator generator = (Generator) context.getBean("generator");
        int i=1;
        for (Map.Entry<String, Class<?>> entry : generator.getProperties().entrySet()) {
            try {
                Field f = item.getClass().getDeclaredField(entry.getKey());
                f.setAccessible(true);
                switch (entry.getValue().getSimpleName()){
                    case "Integer":
                        System.out.println(i+"Integer :"+(Integer.parseInt(f.get(item).toString())));
                        //ps.setInt(i,(Integer.parseInt(f.get(item).toString())));
                        i++;
                        break;
                    case "String":
                        System.out.println(i+"String :"+ String.valueOf(f.get(item)));
                        if(entry.getKey().equals("firstName")) {
                            ps.setString(1, f.get(item)+"");
                        }else{
                            ps.setString(2, f.get(item)+"");
                        }
                        break;
                    case "Date":
                        System.out.println("Date"+i);
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
                            ps.setDate(3,  new Date(((java.util.Date)(f.get(item))).getTime()));
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                        break;
                    default:
                        break;
                }
                i++;
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
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

