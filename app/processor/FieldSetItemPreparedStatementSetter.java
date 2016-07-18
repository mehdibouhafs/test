package processor;

import model.Person;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;
import org.springframework.context.ApplicationContext;
import running.Global;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by MBS on 29/06/2016.
 */
public class FieldSetItemPreparedStatementSetter implements ItemPreparedStatementSetter<Object> {


    @Override
    public void setValues(Object item, PreparedStatement ps) throws SQLException {
        System.out.println(item);
        ApplicationContext context = Global.getApplicationContext();
        item = context.getBean("generator");
        try {
            ps.setString(1, item.getClass().getMethod("getFirstName")+"");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        try {
            ps.setString(2,item.getClass().getMethod("getLastName")+"");
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
