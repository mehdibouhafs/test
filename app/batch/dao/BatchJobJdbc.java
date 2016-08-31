package batch.dao;

import batch.business.BatchJobService;
import batch.business.BatchJobServiceImpl;
import batch.model.Attribute;
import batch.model.Classe;
import batch.model.Reader;
import com.avaje.ebean.annotation.Transactional;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;
import running.Global;

import java.sql.*;
import java.util.*;
import java.util.Date;

/**
 * Created by MBS on 16/08/2016.
 */

public class BatchJobJdbc extends JdbcTemplate implements BatchJobDao {

    public BatchJobJdbc() {

    }

    public Map<String, String> generatedColumnsTable(List<Attribute> attributes) {
        Map<String, String> columnsTable = new LinkedHashMap<>();
        StringBuffer query;
        for (Attribute attribute : attributes){
            if(!attribute.getType().equals("object")) {
                query = new StringBuffer();
                if (attribute.getSizeo() != null && !attribute.getSizeo().equals("")) {
                    //columnsTable.put(attribute.getNameo(), attribute.getType() + "-" + attribute.getSizeo());
                    query.append(attribute.getType() + "-" + attribute.getSizeo());
                } else {
                    columnsTable.put(attribute.getNameo(), attribute.getType() + "-nul");
                    query.append(attribute.getType() + "-nul");
                }
                if (attribute.isPko()) {
                    query.append("-pk");
                } else {
                    query.append("-nul");
                }
                if (attribute.getDefaut() != null && !attribute.getDefaut().equals("")) {
                    query.append("-" + attribute.getDefaut());
                } else {
                    query.append("-nul");
                }
                if (attribute.isNonNull()) {
                    query.append("-nonNull");
                } else {
                    query.append("-nul");
                }
                if (attribute.getCommentaires() != null && !attribute.getCommentaires().equals("")) {
                    query.append("-" + attribute.getCommentaires());
                } else {
                    query.append("-nul");
                }

                columnsTable.put(attribute.getNameo(), query.toString());
            }

        }
        System.out.println ("cols gereatedColumns"+ columnsTable);
        return columnsTable;
    }

    @Override
    public boolean createTableOracle(Reader reader,List<Attribute> attributes) {
            int i = 0;
            StringBuffer query = new StringBuffer();
            StringBuffer query2 = new StringBuffer();
            StringBuffer query3 = new StringBuffer();
            StringBuffer primaryKey = new StringBuffer();
            String commentaire;
            boolean b = false;
            if (attributes.size() <= 0 || reader.classeName.equals("")) {
                System.out.println("Columns < 0");
                return false;
            }
        Map<String,String> columnTable = generatedColumnsTable(attributes);
        System.out.println("columnTable  entryset createtable  " + columnTable);
            for (Map.Entry<String, String> entry : columnTable.entrySet()) {
                String[] typeSize = entry.getValue().split("-");
                if(b==false){
                    if(typeSize[2].equals("pk")){
                        primaryKey = new StringBuffer(", CONSTRAINT "+reader.classeName+"_PK PRIMARY KEY (" + entry.getKey());
                        b = true;
                    }
                }
                if (i == 0) {
                    System.out.println(typeSize[0]);
                    if (typeSize[1].equals("nul")) {
                        query = new StringBuffer("CREATE TABLE " + reader.classeName + " ("
                                + entry.getKey() + " " + typeSize[0]);
                    } else {
                        query = new StringBuffer("CREATE TABLE " + reader.classeName + " ("
                                + entry.getKey() + " " + typeSize[0] + "(" + typeSize[1] + ")");
                    }
                    query2 = new StringBuffer("INSERT INTO " + reader.classeName + " ("
                            + entry.getKey());
                    query3 = new StringBuffer("(?");

                    if(!typeSize[3].equals("nul")){
                        query.append(" DEFAULT "+typeSize[3]);
                    }
                    if(!typeSize[4].equals("nul")){
                        query.append(" NOT NULL");
                    }
                    i++;
                } else {
                    if (typeSize[1].equals("nul")) {
                        query.append(", " + entry.getKey() + " " + typeSize[0]);
                    } else {
                        query.append(", " + entry.getKey() + " " + typeSize[0] + "(" + typeSize[1] + ")" );
                    }
                    if(!typeSize[3].equals("nul")){
                        query.append(" DEFAULT "+typeSize[3]);
                    }
                    if(!typeSize[4].equals("nul")){
                        query.append(" NOT NULL");
                    }


                    query2.append(", " + entry.getKey());
                    query3.append(",?");
                }
            }
            if (b == true) {
                primaryKey.append(") ENABLE ");
                query.append(primaryKey);
            }
            query.append(")");
            query2.append(") VALUES ");
            query3.append(")");

            System.out.println("Query 1 : " + query.toString());
            System.out.println("Query2 :" + query2.toString());
            System.out.println("Query 3: " + query3.toString());
            reader.dateCreation = new Date();
            reader.update();
            //execute(query.toString());
            return true;
    }


    public String buildCdata(Reader reader,List<Attribute> attributes) {
        int i = 0;
        StringBuffer query = new StringBuffer();
        StringBuffer query2 = new StringBuffer();
        StringBuffer columns = new StringBuffer();
        StringBuffer query3 = new StringBuffer();
        StringBuffer primaryKey = new StringBuffer();
        boolean b = false;
        Map<String,String> columnTable = generatedColumnsTable(attributes);
        System.out.println("columnTable  entryset createtable  " + columnTable);
        for (Map.Entry<String, String> entry : columnTable.entrySet()) {
            String[] typeSize = entry.getValue().split("-");
            if(b==false){
                if(typeSize[2].equals("pk")){
                    primaryKey = new StringBuffer(", CONSTRAINT "+reader.classeName+"_PK PRIMARY KEY (" + entry.getKey());
                    b = true;
                }
            }
            if (i == 0) {
                System.out.println(typeSize[0]);
                if (typeSize[1].equals("nul")) {
                    query = new StringBuffer("CREATE TABLE " + reader.classeName + " ("
                            + entry.getKey() + " " + typeSize[0]);
                } else {
                    query = new StringBuffer("CREATE TABLE " + reader.classeName + " ("
                            + entry.getKey() + " " + typeSize[0] + "(" + typeSize[1] + ")");
                }
                query2 = new StringBuffer("INSERT INTO " + reader.classeName + " ("
                        + entry.getKey());
                query3 = new StringBuffer("(?");

                if(!typeSize[3].equals("nul")){
                    query.append(" DEFAULT '"+typeSize[3]+"'");
                }
                if(!typeSize[4].equals("nul")){
                    query.append(" NOT NULL");
                }
                i++;
            } else {
                if (typeSize[1].equals("nul")) {
                    query.append(", " + entry.getKey() + " " + typeSize[0]);
                } else {
                    query.append(", " + entry.getKey() + " " + typeSize[0] + "(" + typeSize[1] + ")" );
                }
                if(!typeSize[3].equals("nul")){
                    query.append(" DEFAULT '"+typeSize[3]+"'");
                }
                if(!typeSize[4].equals("nul")){
                    query.append(" NOT NULL");
                }

                query2.append(", " + entry.getKey());
                query3.append(",?");
            }
        }
        if (b == true) {
            primaryKey.append(") ENABLE ");
            query.append(primaryKey);
        }
        query.append(")");
        query2.append(") VALUES ");
        query3.append(")");
        System.out.println("Query 1 : " + query.toString());
        System.out.println("Query2 :" + query2.toString());
        System.out.println("Query 3: " + query3.toString());
        String cData = (query2.append(query3)).toString();
        return cData +";"+query.toString()+";"+columns.toString();
    }

    public List<String> getCommentaires(String table,List<Attribute> attributes){
        String commentaire;
        List<String> comments = new ArrayList<>();
        Map<String,String> columnTable = generatedColumnsTable(attributes);
        for (Map.Entry<String,String> entry : columnTable.entrySet()) {
            String[] typeSize = entry.getValue().split("-");
            if (typeSize[5] != null) {
                if (!typeSize[5].equals("")) {
                    commentaire = "COMMENT ON COLUMN " + table + "." + entry.getKey() + " IS '" + typeSize[5] + "'";
                    comments.add(commentaire);
                }
            }
        }
        System.out.println("yeaaaaaaaaaaah");
        return  comments;

    }


    public boolean dropTable(String table) {
        String q = "DROP TABLE " + table;
        try {
            execute(q);
            return true;
        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage());
            e.printStackTrace();
            return  false;
        }
    }

    public Map<String, String> dataTable(String table) {
        Map<String, String> map = new LinkedHashMap<>();
        String query = "SELECT * FROM " + table;
        Connection con = DataSourceUtils.getConnection(getDataSource()); // your datasource
        Statement s = null;
        ResultSet rs = null;
        ResultSetMetaData rsmd = null;
        ResultSet rstab = null;
        try {
            DatabaseMetaData dbm = con.getMetaData();
            rstab = dbm.getTables(null, null, table, null);
            if (rstab.next()) {
                s = con.createStatement();
                rs = s.executeQuery(query); // your query
                rsmd = rs.getMetaData();
                int colCount = rsmd.getColumnCount();
                System.out.println("Number Of Columns : " + colCount);
                for (int i = 1; i <= colCount; i++) {
                    //getting column name of index 'i'
                    String colName = rsmd.getColumnName(i);
                    //getting column's data type of index 'i'
                    String colType = rsmd.getColumnTypeName(i);
                    map.put(colName, colType);
                }
            } else {
                map.put("existe", "notExiste");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            //Closing The DB Resources
            //Closing the ResultSet object
            try {
                if (rstab != null) {
                    rstab.close();
                    rstab = null;
                }
                if (rs != null) {
                    rs.close();
                    rs = null;
                }
                if (s != null) {
                    s.close();
                    s = null;
                }
                if (con != null) {
                    con.close();
                    con = null;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        return map;
    }


    @Override
    public Boolean executer(String query) {
        try {
            execute(query);
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
}




