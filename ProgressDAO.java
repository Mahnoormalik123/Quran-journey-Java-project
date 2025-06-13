import java.sql.*;

public class ProgressDAO {
    public static boolean saveProgress(int userId, int surahId, int ayahNo) {
        String sql = "INSERT INTO Progress (UserID, SurahName, AyahNo) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, surahId);
            stmt.setInt(3, ayahNo);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}

