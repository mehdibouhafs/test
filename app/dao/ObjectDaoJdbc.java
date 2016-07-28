package dao;

import model.ReaderGenerique;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import running.Global;

import java.util.List;
import java.util.Map;

/**
 * Created by MBS on 20/07/2016.
 */
public class ObjectDaoJdbc extends JdbcTemplate implements ObjectDao{

    public ObjectDaoJdbc() {
    }

    @Override
    public boolean createTable(String table, Map<String, String> columnsTable) {
        ApplicationContext context = Global.getApplicationContext();
        ReaderGenerique readerGenerique = context.getBean("readerGenerique",ReaderGenerique.class);
        int i=0;
        StringBuffer query = new StringBuffer();
        StringBuffer query2 = new StringBuffer();
        StringBuffer query3 = new StringBuffer();
        StringBuffer queryPrimary = new StringBuffer();
        StringBuffer unique = new StringBuffer();
        StringBuffer index = new StringBuffer();
        StringBuffer fullText = new StringBuffer();
        StringBuffer spatial = new StringBuffer();
        int j = 0;
        int k = 0;
        int p = 0;
        int o = 0;
        int m = 0;
            if (columnsTable.size() <= 0 || table.equals("")) {
                System.out.println("Columns < 0");
                return false;
            }

        for (Map.Entry<String,String> entry : columnsTable.entrySet()) {
            String[] typeSize = entry.getValue().split(",");

            if(j==0 && typeSize[1].equals("PRIMARY")){
                queryPrimary = new StringBuffer(",PRIMARY KEY ("+entry.getKey());
                j++;
            }else{
                if(j==1 && typeSize[1].equals("PRIMARY")){
                    queryPrimary.append(","+entry.getKey());
                }
            }
            if(k==0 && typeSize[1].equals("INDEX")){
                index = new StringBuffer(",INDEX KEY ("+entry.getKey());
                k++;
            }else{
                if(k==1 && typeSize[1].equals("INDEX")){
                    index.append(","+entry.getKey());
                }
            }
            if(p==0 && typeSize[1].equals("UNIQUE")){
                unique = new StringBuffer(",UNIQUE KEY("+entry.getKey());
                p++;
            }else{
                if(p==1 && typeSize[1].equals("UNIQUE")){
                    unique.append(","+entry.getKey());
                }
            }
            if(o==0 && typeSize[1].equals("FULLTEXT")){
                fullText =  new StringBuffer(",FULLTEXT KEY ("+entry.getKey());
                o++;
            }else{
                if(o==1 && typeSize[1].equals("FULLTEXT")){
                    fullText.append(","+entry.getKey());
                }
            }
            if(m==0 && typeSize[1].equals("SPATIAL")){
                spatial =  new StringBuffer(",SPATIAL KEY ("+entry.getKey());
                m++;
            }else{
                if(m==1 && typeSize[1].equals("SPATIAL")){
                    spatial.append(","+entry.getKey());
                }
            }
            if (i == 0) {
                if(typeSize[0].equals("INTEGER") || typeSize[0].equals("SMALLINT")|| typeSize[0].equals("BIGINT")|| typeSize[0].equals("BIGINT")|| typeSize[0].equals("DOUBLE")) {
                    query = new StringBuffer("CREATE TABLE IF NOT EXISTS " + table + " ("
                            + entry.getKey() + " " + typeSize[0] + " " + typeSize[2]);
                }else {
                    query = new StringBuffer("CREATE TABLE IF NOT EXISTS " + table + " ("
                            + entry.getKey() + " " + typeSize[0]);
                }
                query2 = new StringBuffer("INSERT INTO "+ table+ " ("
                        + entry.getKey() );
                query3 = new StringBuffer("(?");
                i++;
            } else {
                if(typeSize[2].equals("AUTO_INCREMENT") && (typeSize[0].equals("INTEGER") || typeSize[0].equals("SMALLINT")|| typeSize[0].equals("BIGINT")|| typeSize[0].equals("BIGINT")|| typeSize[0].equals("DOUBLE"))){
                    query.append(", " + entry.getKey() + " " + typeSize[0]+" "+typeSize[2]);
                }else{
                    query.append(", " + entry.getKey() + " " + typeSize[0]);
                }
                query2.append(", "+entry.getKey());
                query3.append(",?");

            }
        }
        if(!queryPrimary.toString().equals("")){
            queryPrimary.append(")");
            query.append(queryPrimary);
        }
        if(!unique.toString().equals("")){
            unique.append(")");
            query.append(unique);
        }
        if(!index.toString().equals("")){
            index.append(")");
            query.append(index);
        }
        if(!fullText.toString().equals("")){
            fullText.append(")");
            query.append(fullText);
        }
        if(!spatial.toString().equals("")){
            spatial.append(")");
            query.append(spatial);
        }
        query.append(")");

        query2.append(") VALUES ");
        query3.append(")");
        try {
            System.out.println("Query"+query.toString());
            System.out.println("Query"+query2.toString());
            System.out.println("Query"+query3.toString());
            System.out.println("QueryPrimary : "+queryPrimary.toString());
            System.out.println("unique : "+unique.toString());
            System.out.println("index : "+index.toString());
            System.out.println("fullText : "+fullText.toString());
            System.out.println("spatial : "+spatial.toString());
            String cData = (query2.append(query3)).toString();
            execute(query.toString());
            readerGenerique.setcData(cData);
            System.out.println("ture");
            return true;
        }catch (Exception e){
            System.out.println("CALS");
            return false;
        }
        }

        public boolean dropTable(String table) {
            String q = "DROP TABLE " + table;
            try {
               execute(q);
                return  true;
            } catch (Exception e) {
                System.out.println("Erreur : " + e.getMessage());
                return false;
            }
        }


}
