package processor;

import model.Person;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by MBS on 29/06/2016.
 */
public class FieldSetItemPreparedStatementSetter implements ItemPreparedStatementSetter<Person> {


    @Override
    public void setValues(Person item, PreparedStatement ps) throws SQLException {
        System.out.println(item);

        ps.setString(1,item.getFirstName());
        ps.setString(2,item.getLastName());
        ps.setDate(3, new java.sql.Date(item.getDate().getTime()));
    }
}
