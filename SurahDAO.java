import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SurahDAO {
    public static List<String> getSurahList() {
        List<String> list = new ArrayList<>();
        String sql = "SELECT SurahNameAr FROM Surah";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(rs.getString("SurahNameAr"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static int getSurahIDByName(String name) {
        String sql = "SELECT SurahID FROM Surah WHERE SurahNameAr = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("SurahID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}




