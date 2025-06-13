import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class LeaderboardForm extends JFrame {

    private MainMenu mainMenu;
    private DefaultListModel<String> listModel;
    private JList<String> leaderboardList;

    public LeaderboardForm(MainMenu mainMenu) {
        this.mainMenu = mainMenu;

        setTitle("🏆 Leaderboard - Top Users by Progress");
        setSize(450, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Title label with emoji and nice font
        JLabel titleLabel = new JLabel("🏆 Top Readers Leaderboard", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 20));
        add(titleLabel, BorderLayout.NORTH);

        // List and scroll pane
        listModel = new DefaultListModel<>();
        leaderboardList = new JList<>(listModel);
        leaderboardList.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));  // Emoji-support font
        leaderboardList.setFixedCellHeight(30);
        JScrollPane scrollPane = new JScrollPane(leaderboardList);
        add(scrollPane, BorderLayout.CENTER);

        // Back button
        JButton backBtn = new JButton("Back to Main Menu");
        backBtn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        backBtn.addActionListener(e -> {
            this.dispose();
            mainMenu.setVisible(true);
        });
        add(backBtn, BorderLayout.SOUTH);

        // Load leaderboard data
        loadLeaderboard();

        setVisible(true);
    }

    private void loadLeaderboard() {
        listModel.clear();

        String sql = "SELECT U.UserName, COUNT(P.AyahNo) AS ProgressCount " +
                "FROM Users U LEFT JOIN Progress P ON U.UserID = P.UserID " +
                "GROUP BY U.UserName ORDER BY ProgressCount DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            int rank = 1;
            while (rs.next()) {
                String user = rs.getString("UserName");
                int count = rs.getInt("ProgressCount");

                String medal;
                switch (rank) {
                    case 1 -> medal = "🥇";
                    case 2 -> medal = "🥈";
                    case 3 -> medal = "🥉";
                    default -> medal = "🏅";
                }

                String entry = String.format("%s  %d. %s - %d ayahs read", medal, rank, user, count);
                listModel.addElement(entry);
                rank++;
            }

            if (rank == 1) {
                listModel.addElement("No data found.");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading leaderboard: " + e.getMessage());
        }
    }
}

