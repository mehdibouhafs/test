package batch.model;

import com.avaje.ebean.Model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by MBS on 05/07/2016.
 */


public class ReaderGenerique extends Model {

    private int id;

    private String ext;

    private String[] columns;

    private String separator;

    private int lineToSkip;

    private String filePath;

    private String table;

    private Map<String,String> errors = new LinkedHashMap<>();

    private Class<?> classs = Object.class;

    public Map<String,String> columnsTable;

    public  String cData ;


    private String classXml;

    private String fragmentRootElementName;


    private Long dateTime;


    private Map<String, Class<?>> properties;


    private List<String> attributes;


    private List<String> elements;

    public ReaderGenerique() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getcData() {
        return cData;
    }

    public void setcData(String cData) {
        this.cData = cData;
    }

    public String[] getColumns() {
        return columns;
    }

    public void setColumns(String[] columns) {
        this.columns = columns;
    }

    public int getLineToSkip() {
        return lineToSkip;
    }

    public void setLineToSkip(int lineToSkip) {
        this.lineToSkip = lineToSkip;
    }

    public Class<?> getClasss() {
        return classs;
    }

    public void setClasss(Class<?> classs) {
        classs = classs;
    }

    public Map<String, Class<?>> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Class<?>> properties) {
        this.properties = properties;
    }

    public Map<String, String> getColumnsTable() {
        return columnsTable;
    }

    public void setColumnsTable(Map<String, String> columnsTable) {
        this.columnsTable = columnsTable;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public Long getDateTime() {
        return dateTime;
    }

    public void setDateTime(Long dateTime) {
        this.dateTime = dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getSeparator() {
        return separator;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }

    public String getFragmentRootElementName() {
        return fragmentRootElementName;
    }

    public void setFragmentRootElementName(String fragmentRootElementName) {
        this.fragmentRootElementName = fragmentRootElementName;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getClassXml() {
        return classXml;
    }

    public void setClassXml(String classXml) {
        this.classXml = classXml;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }


    public List<String> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<String> attributes) {
        this.attributes = attributes;
    }

    public List<String> getElements() {
        return elements;
    }

    public void setElements(List<String> elements) {
        this.elements = elements;
    }
}
