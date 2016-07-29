package views.formdata;

/**
 * Created by MBS on 29/07/2016.
 */
public class ParamFormData02 {

    public String fragmentRootElementName;

    public String xml;


    public ParamFormData02() {
    }

    public String getFragmentRootElementName() {
        return fragmentRootElementName;
    }

    public void setFragmentRootElementName(String fragmentRootElementName) {
        this.fragmentRootElementName = fragmentRootElementName;
    }

    public String getXml() {
        return xml;
    }

    public void setXml(String xml) {
        this.xml = xml;
    }

    @Override
    public String toString() {
        return "ParamFormData02{" +
                "fragmentRootElementName='" + fragmentRootElementName + '\'' +
                ", xml='" + xml + '\'' +
                '}';
    }
}
