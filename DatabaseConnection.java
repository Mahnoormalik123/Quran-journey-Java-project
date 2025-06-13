import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    public static Connection getConnection() {
        try {
            String url = "jdbc:sqlserver://MAHNOOR-MALIKDESKTOP:1433;databaseName=QuranJourneyDB;encrypt=true;trustServerCertificate=true;";
            String username = "quranuser";
            String password = "573549";

            Connection conn = DriverManager.getConnection(url, username, password);
            System.out.println("✅ Connection successful!");
            return conn;

        } catch (SQLException e) {
            System.out.println("❌ Connection failed.");
            e.printStackTrace();
            return null;
        }
    }
}

