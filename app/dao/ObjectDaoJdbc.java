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
            if (columnsTable.size() <= 0 || table.equals("")) {
                System.out.println("Columns < 0");
                return false;
            }

        for (Map.Entry<String,String> entry : columnsTable.entrySet()) {
            if (i == 0) {
                query = new StringBuffer("CREATE TABLE IF NOT EXISTS " + table + " ("
                        + entry.getKey() + " " + entry.getValue());
                query2 = new StringBuffer("INSERT INTO "+ table+ " ("
                        + entry.getKey() );
                query3 = new StringBuffer("(?");
                i++;
            } else {
                query.append(", " + entry.getKey() + " " + entry.getValue());
                query2.append(", "+entry.getKey());
                query3.append(",?");

            }
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
