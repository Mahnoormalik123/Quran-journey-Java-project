import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class ViewProgressForm extends JFrame {

    private MainMenu mainMenu;
    private String loggedInUser;

    private DefaultListModel<String> listModel;
    private JList<String> progressList;

    public ViewProgressForm(MainMenu mainMenu, String username) {
        this.mainMenu = mainMenu;
        this.loggedInUser = username;

        setTitle("📖 View Progress - User: " + username);
        setSize(450, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Set modern background color
        getContentPane().setBackground(new Color(245, 245, 250));
        setLayout(new BorderLayout(15, 15));

        // Header label with shadow
        JLabel headerLabel = new JLabel("Your Quran Progress");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        headerLabel.setForeground(new Color(30, 30, 60));
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(headerLabel, BorderLayout.NORTH);

        // List model and JList
        listModel = new DefaultListModel<>();
        progressList = new JList<>(listModel);

        // Custom cell renderer for hover effect & padding
        progressList.setCellRenderer(new DefaultListCellRenderer() {
            Color hoverBackground = new Color(200, 230, 250);
            int hoveredIndex = -1;

            {
                progressList.addMouseMotionListener(new MouseMotionAdapter() {
                    public void mouseMoved(MouseEvent e) {
                        int index = progressList.locationToIndex(e.getPoint());
                        if (index != hoveredIndex) {
                            hoveredIndex = index;
                            progressList.repaint();
                        }
                    }
                });
                progressList.addMouseListener(new MouseAdapter() {
                    public void mouseExited(MouseEvent e) {
                        hoveredIndex = -1;
                        progressList.repaint();
                    }
                });
            }

            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
                if (index == hoveredIndex && !isSelected) {
                    label.setBackground(hoverBackground);
                    label.setForeground(new Color(10, 50, 100));
                    label.setFont(label.getFont().deriveFont(Font.BOLD));
                    label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                }
                return label;
            }
        });

        JScrollPane scrollPane = new JScrollPane(progressList);
        scrollPane.setBorder(new CompoundBorder(
                new LineBorder(new Color(100, 150, 200), 2, true),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        add(scrollPane, BorderLayout.CENTER);

        // Back button with hover effect
        JButton backBtn = new JButton("🔙 Back");
        backBtn.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        backBtn.setBackground(new Color(100, 150, 220));
        backBtn.setForeground(Color.white);
        backBtn.setFocusPainted(false);
        backBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        backBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        backBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                backBtn.setBackground(new Color(70, 110, 180));
            }
            public void mouseExited(MouseEvent e) {
                backBtn.setBackground(new Color(100, 150, 220));
            }
        });

        backBtn.addActionListener(e -> {
            this.dispose();
            mainMenu.setVisible(true);
        });

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(245, 245, 250));
        bottomPanel.add(backBtn);
        add(bottomPanel, BorderLayout.SOUTH);

        loadUserProgress();

        setVisible(true);
    }

    private void loadUserProgress() {
        listModel.clear();
        List<UserDAO.ProgressRecord> progress = UserDAO.getProgressForUser(loggedInUser);
        if (progress.isEmpty()) {
            listModel.addElement("No progress found.");
        } else {
            for (UserDAO.ProgressRecord pr : progress) {
                listModel.addElement(pr.toString());
            }
        }
    }
}






