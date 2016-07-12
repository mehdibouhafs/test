package processor;

import model.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class PersonItemProcessor implements ItemProcessor<Person, Person> {

    private static final Logger log = LoggerFactory.getLogger(PersonItemProcessor.class);

    private static final String GET_PERSON = "select * from People where person_id = ?";


    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Person process(final Person person) throws Exception {

        List<Person> productList = jdbcTemplate.query(GET_PERSON, new Object[] {person.getId()}, new RowMapper<Person>() {

            @Override
            public Person mapRow(ResultSet rs, int rowNum) throws SQLException {
                Person p = new Person();
                p.setId(rs.getInt(1));
                p.setFirstName(rs.getString(2));
                p.setLastName(rs.getString(3));
                p.setDate(rs.getDate(4));
                System.out.println("ProcessITEM"+p);
                return p;
            }
        });
            /*final String firstName = person.getFirstName().toUpperCase();
        final int id = person.getId();
        final String lastName = person.getLastName().toUpperCase();
        ;
           /* if(productList.contains(transformedPerson)) {
                jdbcTemplate.update( UPDATE_PERSON,transformedPerson.getId(),transformedPerson.getFirstName(),transformedPerson.getLastName() );
            }
        else {
                jdbcTemplate.update(INSERT_PERSON, transformedPerson.getId(), transformedPerson.getFirstName(), transformedPerson.getLastName());
            }*/
        if(find(productList,person)){
            return null;
        }
        else
            System.out.println("TRUE");
            return person;
    }

    public boolean find(List<Person> ps,Person p){
        for (Person c:ps
             ) {
            if(c.getLastName().equals(p.getLastName()) && p.getFirstName().equals(p.getFirstName())){
                return  true;
            }
        }
        return  false;
    }

}
