
import java.sql.Connection;

public class DatabasaeConnectionTest {
    public static void main(String[] args) {
        Connection conn = DatabaseConnection.getConnection();
        if (conn != null) {
            System.out.println("🎉 SQL Server connected successfully!");
        } else {
            System.out.println("❌ Connection failed.");
        }
    }
}
