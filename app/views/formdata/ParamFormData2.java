package views.formdata;

import java.util.List;

/**
 * Created by MBS on 15/07/2016.
 */
public class ParamFormData2 {

    public List<String> cols;

    public String typeXML;

    public ParamFormData2() {
    }

    public List<String> getCols() {
        return cols;
    }

    public void setCols(List<String> cols) {
        this.cols = cols;
    }

    public String getTypeXML() {
        return typeXML;
    }

    public void setTypeXML(String typeXML) {
        this.typeXML = typeXML;
    }

    @Override
    public String toString() {
        return "ParamFormData2{" +
                "cols=" + cols +
                ", typeXML='" + typeXML + '\'' +
                '}';
    }
}
