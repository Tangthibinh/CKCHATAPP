package Server;

import java.sql.Connection;
import java.sql.DriverManager;

public class Config {

    public static String DB_URL = "jdbc:sqlserver://localhost:3306;"
                        + "database=chat;"
                        + "user=sa;"
                        + "password=123456789;"
                        + "encrypt=true;"
                        + "trustServerCertificate=true;"
                        + "loginTimeout=30;";

    public Config() {
        Connection conn = null;
        String gettext = "connect.." ;
        try {
            System.out.println(gettext);
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");  
            conn = DriverManager.getConnection(DB_URL);
            gettext = "connect successfully!";
            System.out.println(gettext);
        } catch (Exception ex) {
            gettext = "connect failure!";
            System.out.println(gettext);
            ex.printStackTrace();
        }

    }
    
}
