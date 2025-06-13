import javax.swing.*;
import java.awt.*;

public class LoginForm extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private MainMenu mainMenu;

    public LoginForm(MainMenu mainMenu) {
        this.mainMenu = mainMenu;

        setTitle("🔐 Login - Quran Journey");
        setSize(400, 250);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        setLocation(mainMenu.getLocation());  // same location as main menu

        // Input fields panel
        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));

        inputPanel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        inputPanel.add(usernameField);

        inputPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        inputPanel.add(passwordField);

        add(inputPanel, BorderLayout.CENTER);

        // Buttons panel
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        JButton loginBtn = new JButton("Login");
        loginBtn.setBackground(new Color(44, 130, 108));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFocusPainted(false);
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setBackground(new Color(192, 57, 43));
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.setFocusPainted(false);
        cancelBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));

        btnPanel.add(loginBtn);
        btnPanel.add(cancelBtn);

        add(btnPanel, BorderLayout.SOUTH);

        // Action listeners
        loginBtn.addActionListener(e -> loginUser());

        cancelBtn.addActionListener(e -> {
            this.dispose();
            mainMenu.setLocation(this.getLocation());
            mainMenu.setVisible(true);
        });

        setVisible(true);
    }

    private void loginUser() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "❗ Please enter both username and password.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean success = UserDAO.loginUser(username, password);
        if (success) {
            JOptionPane.showMessageDialog(this, "✅ Login successful! Welcome " + username, "Success", JOptionPane.INFORMATION_MESSAGE);
            this.dispose();
            mainMenu.setLocation(this.getLocation());
            mainMenu.setLoggedInUser(username);
            mainMenu.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "❌ Invalid username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }
}

