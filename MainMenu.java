import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainMenu extends JFrame {

    private String loggedInUser = null;

    private JButton btnRegister, btnLogin, btnEnterProgress, btnViewProgress, btnLeaderboard, btnViewFullSurah, btnLogout, btnExit;
    private JLabel titleLabel;

    public MainMenu() {
        setTitle("Quran Journey - Main Menu");
        setSize(460, 570);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // Background Panel with solid deep blue
        JPanel bgPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(10, 40, 90));  // solid deep blue
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        bgPanel.setLayout(new BorderLayout());
        setContentPane(bgPanel);

        // Simple clear title
        titleLabel = new JLabel("Quran Journey", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Georgia", Font.BOLD, 38));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 10, 20, 10));
        bgPanel.add(titleLabel, BorderLayout.NORTH);

        // Button Panel
        JPanel buttonPanel = new JPanel(new GridLayout(8, 1, 15, 15));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 70, 40, 70));
        buttonPanel.setOpaque(false);

        btnRegister = create3DButton("Register");
        btnLogin = create3DButton("Login");
        btnEnterProgress = create3DButton("Enter Progress");
        btnViewProgress = create3DButton("View Progress");
        btnLeaderboard = create3DButton("Leaderboard");
        btnViewFullSurah = create3DButton("View Full Surah");
        btnLogout = create3DButton("Logout");
        btnExit = create3DButton("Exit");

        buttonPanel.add(btnRegister);
        buttonPanel.add(btnLogin);
        buttonPanel.add(btnEnterProgress);
        buttonPanel.add(btnViewProgress);
        buttonPanel.add(btnLeaderboard);
        buttonPanel.add(btnViewFullSurah);
        buttonPanel.add(btnLogout);
        buttonPanel.add(btnExit);

        bgPanel.add(buttonPanel, BorderLayout.CENTER);

        setUserButtonsEnabled(false);
        btnLogout.setEnabled(false);

        // Button actions
        btnRegister.addActionListener(e -> {
            this.setVisible(false);
            new RegisterationForm(this);
        });

        btnLogin.addActionListener(e -> {
            this.setVisible(false);
            new LoginForm(this);
        });


        btnEnterProgress.addActionListener(e -> {
            if (loggedInUser != null) {
                this.setVisible(false);
                new ProgressEntryForm(this, loggedInUser);
            } else {
                JOptionPane.showMessageDialog(this, "Please login first.");
            }
        });


        btnViewProgress.addActionListener(e -> {
            if (loggedInUser != null) {
                this.setVisible(false);
                new ViewProgressForm(this, loggedInUser);
            } else {
                JOptionPane.showMessageDialog(this, "Please login first.");
            }
        });


        btnLeaderboard.addActionListener(e -> {
            if (loggedInUser != null) {
                this.setVisible(false);
                new LeaderboardForm(this);
            } else {
                JOptionPane.showMessageDialog(this, "Please login first.");
            }
        });

        btnViewFullSurah.addActionListener(e -> {
            this.setVisible(false);
            new ViewFullSurahForm(this);
        });


        btnLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                loggedInUser = null;
                setUserButtonsEnabled(false);
                btnLogout.setEnabled(false);
                btnLogin.setEnabled(true);
                setTitle("Quran Journey - Main Menu");
                JOptionPane.showMessageDialog(this, "Logged out successfully.");
            }
        });

        btnExit.addActionListener(e -> System.exit(0));

        setVisible(true);
    }

    private JButton create3DButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 20));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(30, 70, 130)); // medium blue gradient base
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));
        button.setOpaque(true);

        // Shadow layer: We'll simulate with a drop shadow effect using layered borders on hover
        // Use MouseListener to handle hover & press for 3D effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                // Brighter background + shadow border
                button.setBackground(new Color(60, 110, 190));
                button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(0, 0, 0, 150), 3, true),
                        BorderFactory.createEmptyBorder(12, 25, 12, 25)));
                button.setFont(button.getFont().deriveFont(Font.BOLD, 22f));
                button.setLocation(button.getX(), button.getY() - 2);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // Return to normal
                button.setBackground(new Color(30, 70, 130));
                button.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));
                button.setFont(button.getFont().deriveFont(Font.BOLD, 20f));
                button.setLocation(button.getX(), button.getY() + 2);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                // Darker background + pressed effect
                button.setBackground(new Color(15, 50, 100));
                button.setFont(button.getFont().deriveFont(Font.BOLD, 20f));
                button.setLocation(button.getX(), button.getY() + 2);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                button.setBackground(new Color(60, 110, 190));
                button.setFont(button.getFont().deriveFont(Font.BOLD, 22f));
                button.setLocation(button.getX(), button.getY() - 2);
            }
        });

        return button;
    }

    public void setLoggedInUser(String username) {
        this.loggedInUser = username;
        setUserButtonsEnabled(true);
        btnLogout.setEnabled(true);
        btnLogin.setEnabled(false);
        setTitle("Quran Journey - Logged in as: " + username);
        this.setVisible(true);
    }

    private void setUserButtonsEnabled(boolean enabled) {
        btnEnterProgress.setEnabled(enabled);
        btnViewProgress.setEnabled(enabled);
        btnLeaderboard.setEnabled(enabled);
        btnViewFullSurah.setEnabled(enabled);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainMenu());
    }
}
