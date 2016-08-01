package views.formdata;

import java.util.List;

/**
 * Created by MBS on 29/07/2016.
 */
public class ParamFormData02 {

    public List<String> xml;


    public ParamFormData02() {
    }

    public List<String> getXml() {
        return xml;
    }

    public void setXml(List<String> xml) {
        this.xml = xml;
    }

    @Override
    public String toString() {
        return "ParamFormData02{" +
                ", xml='" + xml + '\'' +
                '}';
    }
}
