package mappers;

import model.Person;
import org.joda.time.LocalDate;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by MBS on 30/06/2016.
 */
public class PersonFieldSetMap implements FieldSetMapper<Person> {

    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    public Person mapFieldSet(FieldSet fieldSet) throws BindException {
        Person person = new Person();
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
        }
        return null;
    }
}
