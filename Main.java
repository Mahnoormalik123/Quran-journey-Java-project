public class Main {
    public static void main(String[] args) {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            System.out.println("✅ Driver loaded successfully!");
        } catch (ClassNotFoundException e) {
            System.out.println("❌ Driver NOT found!");
        }
    }
}
