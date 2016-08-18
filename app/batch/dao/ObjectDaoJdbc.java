package batch.dao;

import batch.model.batch.BatchStepExecution;
import batch.model.ReaderGenerique;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DataSourceUtils;
import running.Global;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by MBS on 20/07/2016.
 */
public class ObjectDaoJdbc extends JdbcTemplate implements ObjectDao{

    public ObjectDaoJdbc() {

    }

    @Override
    public boolean createTableOracle(String name, Map<String, String> columnsTable) {
        ApplicationContext context = Global.getApplicationContext();
        ReaderGenerique readerGenerique = context.getBean("readerGenerique",ReaderGenerique.class);
        int i=0;
        StringBuffer query = new StringBuffer();
        StringBuffer query2 = new StringBuffer();
        StringBuffer query3 = new StringBuffer();
        StringBuffer primaryKey = new StringBuffer();
        String commentaire;

        List<String> comments = new ArrayList<>();

        boolean b = false;


        if (columnsTable.size() <= 0 || name.equals("")) {
            System.out.println("Columns < 0");
            return false;
        }
        for (Map.Entry<String,String> entry : columnsTable.entrySet()) {
            String[] typeSize = entry.getValue().split("-");

            if(!typeSize[5].equals("")){
                commentaire="COMMENT ON COLUMN "+name+"."+entry.getKey()+" IS '"+typeSize[5]+"'";
                comments.add(commentaire);
            }
            if(typeSize[3].equals("PrimaryKey") && b == true){
                primaryKey.append(","+entry.getKey());
            }
            if(b==false){
                if(typeSize[3].equals("PrimaryKey")){
                    primaryKey = new StringBuffer(", CONSTRAINT "+name+"_PK PRIMARY KEY (" + entry.getKey());
                    b = true;
                }
            }
            if (i == 0) {
                System.out.println(typeSize[0]);
                if(typeSize[1].equals("")){
                    System.out.println("Containe NUmber or Float");
                    query = new StringBuffer("CREATE TABLE " + name + " ("
                            + entry.getKey() + " " + typeSize[0] +typeSize[4]+typeSize[2]);
                }else{
                    if(typeSize[1].equals("DATE")){
                        query = new StringBuffer("CREATE TABLE " + name + " ("
                                + entry.getKey() + " " + typeSize[0]+typeSize[4]+typeSize[2]);
                    }else {
                        query = new StringBuffer("CREATE TABLE " + name + " ("
                                + entry.getKey() + " " + typeSize[0] + "(" + typeSize[1] + ")" +typeSize[4]+ typeSize[2]);
                    }
                }
                query2 = new StringBuffer("INSERT INTO "+ name+ " ("
                        + entry.getKey() );
                query3 = new StringBuffer("(?");
                i++;
            } else {
                if(typeSize[1].equals("")){
                    query.append(", " + entry.getKey() + " " + typeSize[0] +typeSize[4]+typeSize[2]);
                }else{
                    if(typeSize[1].equals("DATE")){
                        query.append(", " + entry.getKey()+typeSize[2]+typeSize[3]);
                    }else {
                        query.append(", " + entry.getKey() + " " + typeSize[0] + "(" + typeSize[1] + ")"+typeSize[4]+typeSize[2]);
                    }
                }
                query2.append(", "+entry.getKey());
                query3.append(",?");
            }
        }
        if(b == true){
            primaryKey.append(") ENABLE ");
            query.append(primaryKey);
        }
        query.append(")");
        query2.append(") VALUES ");
        query3.append(")");

        try {
            System.out.println("Query"+query.toString());
            System.out.println("Query"+query2.toString());
            System.out.println("Query"+query3.toString());
            String cData = (query2.append(query3)).toString();
            execute(query.toString());
            for (String s : comments) {
                System.out.println("com "+s);
                execute(s);
            }
            readerGenerique.setcData(cData);
            return true;
        }catch (Exception e){
            System.out.println("CALS");
            e.printStackTrace();
            return false;
        }

    }


    public boolean dropTable(String table) {
            String q = "DROP TABLE " + table;
            try {
                execute(q);
                return true;
            } catch (Exception e) {
                System.out.println("Erreur : " + e.getMessage());
                return false;
            }
        }

   public Map<String,String > dataTable(String table) {
       Map<String,String > map = new LinkedHashMap<>();
       String query = "SELECT * FROM " + table;
       Connection con = DataSourceUtils.getConnection(getDataSource()); // your datasource
       Statement s = null;
       ResultSet rs = null;
       ResultSetMetaData rsmd = null;
       ResultSet rstab = null;
       try {
           DatabaseMetaData dbm = con.getMetaData();
           rstab = dbm.getTables(null,null,table,null);
           if(rstab.next()) {
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
           }else{
               map.put("existe","notExiste");
           }
       } catch (SQLException e) {
           e.printStackTrace();
       }finally
       {
           //Closing The DB Resources
           //Closing the ResultSet object
           try
           {
               if(rstab != null){
                   rstab.close();
                   rstab=null;
               }
               if(rs!=null)
               {
                   rs.close();
                   rs=null;
               }
               if(s!=null)
               {
                   s.close();
                   s=null;
               }
               if(con!=null)
               {
                   con.close();
                   con=null;
               }
           }
           catch (SQLException e)
           {
               e.printStackTrace();
           }

       }
       return map;
   }

    public List<BatchStepExecution> selectAllDetail() {
        /*List<BatchStepExecution> batchStepExecutions = query("select version,step_name form Batch_step_exection", new RowMapper<BatchStepExecution>() {
            @Override
            public BatchStepExecution mapRow(ResultSet rs, int rowNum) throws SQLException {
                BatchStepExecution batchStepExecution = new BatchStepExecution();
                batchStepExecution.setVersion(rs.getLong("version"));
                batchStepExecution.setStep_name(rs.getString("step_name"));
                return batchStepExecution;
            }
        });*/
        return null;//batchStepExecutions;
    }

   }
