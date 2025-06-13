import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class ProgressEntryForm extends JFrame {

    private JComboBox<String> surahDropdown;
    private JTextField ayahField;
    private JButton saveBtn, backBtn;
    private MainMenu mainMenu;
    private String username;

    public ProgressEntryForm(MainMenu menu, String username) {
        this.mainMenu = menu;
        this.username = username;

        setTitle("✏️ Enter Quran Progress - Quran Journey");
        setSize(420, 280);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        setLocation(menu.getLocation()); // Same location as MainMenu

        // Panel for inputs
        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 15, 15));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 10, 40));

        inputPanel.add(new JLabel("📜 Surah Name (Arabic):"));
        surahDropdown = new JComboBox<>();
        inputPanel.add(surahDropdown);

        inputPanel.add(new JLabel("📖 Ayah Number:"));
        ayahField = new JTextField();
        inputPanel.add(ayahField);

        add(inputPanel, BorderLayout.CENTER);

        // Buttons panel
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 15));

        saveBtn = new JButton("💾 Save Progress");
        saveBtn.setBackground(new Color(44, 130, 108));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setFocusPainted(false);
        saveBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));

        backBtn = new JButton("🔙 Back");
        backBtn.setBackground(new Color(192, 57, 43));
        backBtn.setForeground(Color.WHITE);
        backBtn.setFocusPainted(false);
        backBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));

        btnPanel.add(saveBtn);
        btnPanel.add(backBtn);

        add(btnPanel, BorderLayout.SOUTH);

        loadSurahNames();

        saveBtn.addActionListener(e -> saveProgress());
        backBtn.addActionListener(e -> {
            this.dispose();
            mainMenu.setLocation(this.getLocation());
            mainMenu.setVisible(true);
        });

        setVisible(true);
    }

    private void loadSurahNames() {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT SurahNameAr FROM Surah ORDER BY SurahID");
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                surahDropdown.addItem(rs.getString("SurahNameAr"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "❌ Could not load Surah names.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveProgress() {
        String selectedSurah = (String) surahDropdown.getSelectedItem();
        String ayahText = ayahField.getText().trim();

        if (selectedSurah == null || ayahText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠ Please fill all fields.", "Input Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int ayahNo;
        try {
            ayahNo = Integer.parseInt(ayahText);
            if (ayahNo <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "⚠ Please enter a valid positive number for Ayah.", "Input Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO Progress (UserID, SurahName, AyahNo) VALUES (?, ?, ?)")) {

            int userId = UserDAO.getUserIdByUsername(username);

            stmt.setInt(1, userId);
            stmt.setString(2, selectedSurah);
            stmt.setInt(3, ayahNo);

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, "✅ Progress saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                ayahField.setText("");  // Clear after save
            } else {
                JOptionPane.showMessageDialog(this, "❌ Failed to save progress.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "❌ Failed to save progress.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}




