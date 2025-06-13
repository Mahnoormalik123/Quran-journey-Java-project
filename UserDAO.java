import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    public static boolean registerUser(String username, String email, String password) {
        String sql = "INSERT INTO Users (UserName, Email, Password, DateCreated) VALUES (?, ?, ?, GETDATE())";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, email);
            stmt.setString(3, password);

            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.out.println("❌ registerUser error: " + e.getMessage());
            return false;
        }
    }

    public static boolean loginUser(String username, String password) {
        String sql = "SELECT * FROM Users WHERE UserName = ? AND Password = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println("❌ loginUser error: " + e.getMessage());
            return false;
        }
    }

    public static int getUserIdByUsername(String username) {
        String sql = "SELECT UserID FROM Users WHERE UserName = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("UserID");
            }
        } catch (SQLException e) {
            System.out.println("❌ getUserIdByUsername error: " + e.getMessage());
        }
        return -1;
    }

    public static boolean saveProgress(String username, String surah, int ayahNo) {
        int userId = getUserIdByUsername(username);
        if (userId == -1) return false;

        String sql = "INSERT INTO Progress (UserID, SurahName, AyahNo) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setString(2, surah);
            stmt.setInt(3, ayahNo);

            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.out.println("❌ saveProgress error: " + e.getMessage());
            return false;
        }
    }

    public static List<ProgressRecord> getProgressForUser(String username) {
        List<ProgressRecord> list = new ArrayList<>();
        int userId = getUserIdByUsername(username);
        if (userId == -1) return list;

        String sql = "SELECT SurahName, AyahNo FROM Progress WHERE UserID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String surah = rs.getString("SurahName");
                int ayah = rs.getInt("AyahNo");
                list.add(new ProgressRecord(surah, ayah));
            }
        } catch (SQLException e) {
            System.out.println("❌ getProgressForUser error: " + e.getMessage());
        }
        return list;
    }

    public static class ProgressRecord {
        private String surahName;
        private int ayahNo;

        public ProgressRecord(String surahName, int ayahNo) {
            this.surahName = surahName;
            this.ayahNo = ayahNo;
        }

        @Override
        public String toString() {
            return "Surah: " + surahName + " | Ayah: " + ayahNo;
        }
    }
}

