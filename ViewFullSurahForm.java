import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;
import java.util.*;

public class ViewFullSurahForm extends Frame {
    JComboBox<String> surahDropdown;
    TextArea displayArea;
    Button searchBtn, zoomInBtn, zoomOutBtn, nightModeBtn;
    TextField searchField;
    Button backButton;
    Label totalAyahsLbl, wordCountLbl, sajdahLbl, rukoLbl, juzLbl, manzilLbl;

    private MainMenu mainMenu;
    private int fontSize = 18;
    private boolean isNightMode = false;

    public ViewFullSurahForm() {
        this(null);
    }

    public ViewFullSurahForm(MainMenu mainMenu) {
        this.mainMenu = mainMenu;
        setTitle("📖 View Full Surah & Translation");
        setLayout(new BorderLayout(10, 10));

        Panel topPanel = new Panel(new FlowLayout());

        surahDropdown = new JComboBox<>();
        loadSurahList();
        topPanel.add(new Label("Select Surah:"));
        topPanel.add(surahDropdown);

        searchField = new TextField(15);
        topPanel.add(new Label("Search Keyword/Ayah No:"));
        topPanel.add(searchField);

        searchBtn = new Button("Search");
        topPanel.add(searchBtn);

        zoomInBtn = new Button("🔍 Zoom In");
        zoomOutBtn = new Button("🔎 Zoom Out");
        nightModeBtn = new Button("🌙 Night Mode");
        topPanel.add(zoomInBtn);
        topPanel.add(zoomOutBtn);
        topPanel.add(nightModeBtn);

        backButton = new Button("⬅ Back");
        topPanel.add(backButton);

        add(topPanel, BorderLayout.NORTH);

        displayArea = new TextArea();
        displayArea.setFont(new Font("Scheherazade New", Font.PLAIN, fontSize));
        displayArea.setEditable(false);
        add(displayArea, BorderLayout.CENTER);

        Panel statsPanel = new Panel(new GridLayout(2, 3));
        totalAyahsLbl = new Label("Total Ayahs: ");
        wordCountLbl = new Label("Word Count: ");
        sajdahLbl = new Label("Sajdah Ayahs: ");
        rukoLbl = new Label("Rukus: ");
        juzLbl = new Label("Juz: ");
        manzilLbl = new Label("Manzils: ");

        statsPanel.add(totalAyahsLbl);
        statsPanel.add(wordCountLbl);
        statsPanel.add(sajdahLbl);
        statsPanel.add(rukoLbl);
        statsPanel.add(juzLbl);
        statsPanel.add(manzilLbl);

        add(statsPanel, BorderLayout.SOUTH);

        // Listeners
        surahDropdown.addActionListener(e -> loadFullSurahText());
        searchBtn.addActionListener(e -> searchInSurah());
        zoomInBtn.addActionListener(e -> adjustFontSize(true));
        zoomOutBtn.addActionListener(e -> adjustFontSize(false));
        nightModeBtn.addActionListener(e -> toggleNightMode());

        backButton.addActionListener(e -> {
            if (mainMenu != null) mainMenu.setVisible(true);
            else new MainMenu();
            dispose();
        });

        setSize(900, 700);
        setLocationRelativeTo(null);
        setVisible(true);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                dispose();
            }
        });
    }

    private void adjustFontSize(boolean zoomIn) {
        fontSize += zoomIn ? 2 : -2;
        if (fontSize < 12) fontSize = 12;
        displayArea.setFont(new Font("Scheherazade New", Font.PLAIN, fontSize));
    }

    private void toggleNightMode() {
        isNightMode = !isNightMode;
        Color bg = isNightMode ? Color.DARK_GRAY : Color.WHITE;
        Color fg = isNightMode ? Color.WHITE : Color.BLACK;
        displayArea.setBackground(bg);
        displayArea.setForeground(fg);
    }

    private void loadSurahList() {
        String sql = "SELECT SurahNameAr FROM Surah ORDER BY SurahID";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            surahDropdown.removeAllItems();
            while (rs.next()) {
                surahDropdown.addItem(rs.getString("SurahNameAr"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading Surah list: " + e.getMessage());
        }
    }

    private void loadFullSurahText() {
        String selectedSurah = (String) surahDropdown.getSelectedItem();
        if (selectedSurah == null) return;

        String sqlSurahID = "SELECT SurahID, PlaceOfRevelation FROM Surah WHERE SurahNameAr = ?";
        String sqlAyahs = "SELECT AyahNoSurah, ArabicText, EnglishText, RukoNo, JuzNo, ManzilNo, Sajdah, WordCount FROM Ayah WHERE SurahID = ? ORDER BY AyahNoSurah";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement psSurah = conn.prepareStatement(sqlSurahID)) {

            psSurah.setString(1, selectedSurah);
            ResultSet rsSurah = psSurah.executeQuery();

            int surahId = -1;
            String revelation = "";
            if (rsSurah.next()) {
                surahId = rsSurah.getInt("SurahID");
                revelation = rsSurah.getString("PlaceOfRevelation");
            }

            if (surahId == -1) return;

            PreparedStatement psAyahs = conn.prepareStatement(sqlAyahs);
            psAyahs.setInt(1, surahId);
            ResultSet rsAyahs = psAyahs.executeQuery();

            int totalAyahs = 0, totalWords = 0, sajdahCount = 0;
            Set<Integer> rukus = new HashSet<>();
            Set<Integer> juz = new HashSet<>();
            Set<Integer> manzils = new HashSet<>();

            StringBuilder sb = new StringBuilder();
            sb.append("Surah: ").append(selectedSurah).append("  (").append(revelation).append(")\n\n");

            while (rsAyahs.next()) {
                int ayahNo = rsAyahs.getInt("AyahNoSurah");
                String arabic = rsAyahs.getString("ArabicText");
                String english = rsAyahs.getString("EnglishText");
                int ruko = rsAyahs.getInt("RukoNo");
                int juzNo = rsAyahs.getInt("JuzNo");
                int manzil = rsAyahs.getInt("ManzilNo");
                int wordCount = rsAyahs.getInt("WordCount");
                int sajdah = rsAyahs.getInt("Sajdah");

                totalAyahs++;
                totalWords += wordCount;
                if (sajdah == 1) sajdahCount++;
                rukus.add(ruko);
                juz.add(juzNo);
                manzils.add(manzil);

                sb.append(ayahNo).append(". ").append(arabic).append("\n");
                sb.append("    ").append(english).append("\n");
                if (sajdah == 1) sb.append("    🔴 Sajdah Ayah\n");
                sb.append("\n");
            }

            displayArea.setText(sb.toString());
            totalAyahsLbl.setText("Total Ayahs: " + totalAyahs);
            wordCountLbl.setText("Word Count: " + totalWords);
            sajdahLbl.setText("Sajdah Ayahs: " + sajdahCount);
            rukoLbl.setText("Rukus: " + rukus.size());
            juzLbl.setText("Juz: " + juz.size());
            manzilLbl.setText("Manzils: " + manzils.size());

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading Surah: " + e.getMessage());
        }
    }

    private void searchInSurah() {
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a keyword or Ayah number to search.");
            return;
        }

        String selectedSurah = (String) surahDropdown.getSelectedItem();
        if (selectedSurah == null) return;

        boolean isNumber = false;
        int ayahNo = -1;
        try {
            ayahNo = Integer.parseInt(keyword);
            isNumber = true;
        } catch (NumberFormatException ignored) {}

        String sql = "SELECT AyahNoSurah, ArabicText, EnglishText FROM Ayah " +
                "WHERE SurahID = (SELECT SurahID FROM Surah WHERE SurahNameAr = ?) " +
                "AND (ArabicText LIKE ? OR EnglishText LIKE ?" + (isNumber ? " OR AyahNoSurah = ?" : "") + ") " +
                "ORDER BY AyahNoSurah";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, selectedSurah);
            ps.setString(2, "%" + keyword + "%");
            ps.setString(3, "%" + keyword + "%");
            if (isNumber) ps.setInt(4, ayahNo);

            ResultSet rs = ps.executeQuery();

            StringBuilder sb = new StringBuilder();
            sb.append("Search results for '").append(keyword).append("' in Surah ").append(selectedSurah).append(":\n\n");

            boolean found = false;
            while (rs.next()) {
                found = true;
                int ayahNoRes = rs.getInt("AyahNoSurah");
                String arabic = rs.getString("ArabicText");
                String english = rs.getString("EnglishText");

                sb.append(ayahNoRes).append(". ").append(arabic).append("\n");
                sb.append("    ").append(english).append("\n\n");
            }

            if (!found) {
                sb.append("No matches found.");
            }

            displayArea.setText(sb.toString());

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Search error: " + e.getMessage());
        }
    }
}




