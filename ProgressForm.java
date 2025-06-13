import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.List;

public class ProgressForm extends Frame {
    Choice surahDropdown;
    TextField ayahField;
    int userId;

    public ProgressForm(int userId) {
        this.userId = userId;
        setLayout(new GridLayout(4, 2));
        setTitle("Enter Progress");

        add(new Label("Select Surah:"));
        surahDropdown = new Choice();
        List<String> surahList = SurahDAO.getSurahList();
        for (String surah : surahList) {
            surahDropdown.add(surah);
        }
        add(surahDropdown);

        add(new Label("Ayah Number:"));
        ayahField = new TextField();
        add(ayahField);

        Button saveBtn = new Button("Save Progress");
        add(saveBtn);

        saveBtn.addActionListener(e -> {
            String selectedSurah = surahDropdown.getSelectedItem();
            int surahId = SurahDAO.getSurahIDByName(selectedSurah);
            int ayahNo = Integer.parseInt(ayahField.getText());

            boolean success = ProgressDAO.saveProgress(userId, surahId, ayahNo);
            if (success) {
                JOptionPane.showMessageDialog(this, "✅ Progress saved!");
            } else {
                JOptionPane.showMessageDialog(this, "❌ Failed to save progress.");
            }
        });

        setSize(350, 200);
        setVisible(true);
    }
}


