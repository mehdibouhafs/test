package model.adapters;



import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.Date;

public class LocalDateAdapter extends XmlAdapter<String, Date>{

	public Date unmarshal(String v) throws Exception {
		return new Date(v);
	}

	public String marshal(Date v) throws Exception {
		return v.toString();
	}

}