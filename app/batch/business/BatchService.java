package batch.business;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.mvc.Http;
import play.mvc.Result;

import java.io.File;

/**
 * Created by MBS on 08/08/2016.
 */
public interface BatchService {
    public ArrayNode getType(Http.Request request);
    public ObjectNode validate();
    public ArrayNode upload(Http.Request request);
    public ArrayNode cols();
    public ArrayNode colsxml();
    public String path();
    public String[] firstLine (File f, String delimiter);
    public String[] firstLine1(File f,String typeXml);
    public String getExtension(String fileName);
    public ArrayNode metadata(String[] table);
    public ArrayNode getErrors();

}
