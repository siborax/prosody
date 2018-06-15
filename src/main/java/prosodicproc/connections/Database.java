package prosodicproc.connections;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
//import org.apache.tomcat.util.http.fileupload.IOUtils;

public class Database {
    private final String url = "jdbc:postgresql://localhost:5432/postgres";

    //private final String user = "postgres";
   // private final String password = "sibora";

    public  Connection connect() {
        Properties props = new Properties();
        props.setProperty("user","postgres");
        props.setProperty("password","sibora");
        //props.setProperty("ssl","true");
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, props);
            System.out.println("Connected to the PostgreSQL server successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
     return conn;
    }



}
