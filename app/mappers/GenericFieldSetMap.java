package mappers;

import model.App;
import model.Person;
import model.ReaderGenerique;
import org.joda.time.LocalDate;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.BindException;
import running.Global;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created by MBS on 30/06/2016.
 */
public class GenericFieldSetMap implements FieldSetMapper<Object> {

    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    public Class<?> mapFieldSet(FieldSet fieldSet) throws BindException {
        ApplicationContext context = Global.getApplicationContext();
        ReaderGenerique readerGenerique = context.getBean("readerGenerique",ReaderGenerique.class);
        Object generator = context.getBean("generator",readerGenerique);
        App app = new App();
        //return app.SettersBean(readerGenerique.getProperties(),generator,fieldSet);
          /*  Person person = new Person();
        person.setFirstName(fieldSet.readString(1));
        person.setLastName(fieldSet.readString(2));
        //default format yyyy-MM-dd
        //fieldSet.readDate(4);
        String date = fieldSet.readString(3);
        try {
            person.setDate(fieldSet.readDate(4));
            System.out.println(person);
            return person;
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        return  null;
    }
}
