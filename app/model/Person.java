package model;







import model.adapter.LocalDateAdapter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Date;


@XmlRootElement(name = "Person")
public class Person {
    private int id;
    private String lastName;
    private String firstName;
    private Date date;

    public Person() {
    }


    public Person(int id, String firstName, String lastName, Date date) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.date = date;
    }


    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    @XmlElement(name = "firstName")
    public String getFirstName() {
        return firstName;
    }

    @XmlElement(name = "lastName")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @XmlElement(name = "date")
    @XmlJavaTypeAdapter(type = LocalDateAdapter.class, value = model.adapter.LocalDateAdapter.class)
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @XmlElement(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", date=" + date +
                '}';
    }
}
