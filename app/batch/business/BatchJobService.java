package batch.business;

import batch.model.Attribute;
import batch.model.Reader;
import batch.model.Resume;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.mvc.Http;
import play.mvc.Result;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Created by MBS on 08/08/2016.
 */
public interface BatchJobService {

    public Resume doJob(Reader reader);
    public List<String> firstLineCsvFile(File f, String delimiter);
    public String[] getElementAndAttributesFileXml(Reader reader);
    public Map<String, Class<?>> typeAttributes(List<Attribute> attributes,Reader reader);
    public Map<String,String > dataTable(String table);
    public String getExtension(String fileName);
}
