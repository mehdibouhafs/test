package batch.business;

import batch.dao.ObjectDao;
import batch.dao.ObjectDaoJdbc;
import batch.listeners.JobCompletionNotificationListener;
import batch.model.Attribute;
import batch.model.ReaderGenerique;
import batch.util.App;
import batch.util.ReadXMLFile2;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import javassist.CannotCompileException;
import javassist.NotFoundException;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.context.ApplicationContext;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import play.data.Form;
import play.libs.Json;
import play.mvc.Http;
import running.Global;
import views.formdata.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.sql.Blob;
import java.util.*;

/**
 * Created by MBS on 08/08/2016.
 */


public class BatchServiceImpl  {

    public BatchServiceImpl() {
    }


}
