package views.formdata;

import play.data.validation.ValidationError;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MBS on 13/07/2016.
 */
public class ParamFormData1 {


    public String typeXML;
    public String tableName;
    public List<Integer> id;
    public List<String> cols;
    public List<String> type;
    public List<String> size;
    public List<String> defaultVal;
    public List<String> commentaire;
    //public List<String> columns;

    //public String filePath;



    public List<String> getSize() {
        return size;
    }

    public void setSize(List<String> size) {
        this.size = size;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<String> getType() {
        return type;
    }

    public void setType(List<String> type) {
        this.type = type;
    }

    public List<String> getDefaultVal() {
        return defaultVal;
    }

    public void setDefaultVal(List<String> defaultVal) {
        this.defaultVal = defaultVal;
    }

    public String getTypeXML() {
        return typeXML;
    }

    public void setTypeXML(String typeXML) {
        this.typeXML = typeXML;
    }

    public List<Integer> getId() {
        return id;
    }

    public void setId(List<Integer> id) {
        this.id = id;
    }


    public List<String> getCols() {
        return cols;
    }

    public void setCols(List<String> cols) {
        this.cols = cols;
    }

    public List<String> getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(List<String> commentaire) {
        this.commentaire = commentaire;
    }

    @Override
    public String toString() {
        return "ParamFormData1{" +
                "typeXML='" + typeXML + '\'' +
                ", tableName='" + tableName + '\'' +
                ", id=" + id +
                ", cols=" + cols +
                ", type=" + type +
                ", size=" + size +
                ", defaultVal=" + defaultVal +
                ", commentaire=" + commentaire +
                '}';
    }
}
