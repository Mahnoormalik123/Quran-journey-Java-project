import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class RegisterationForm extends JFrame {

    private JTextField usernameField, emailField;
    private JPasswordField passwordField;
    private MainMenu mainMenu;  // reference to main menu to go back

    public RegisterationForm(MainMenu mainMenu) {
        this.mainMenu = mainMenu;

        setTitle("📝 Register - Quran Journey");
        setSize(400, 300);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        setLocation(mainMenu.getLocation());  // open at same location as MainMenu

        // Panel for input fields
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));

        inputPanel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        inputPanel.add(usernameField);

        inputPanel.add(new JLabel("Email:"));
        emailField = new JTextField();
        inputPanel.add(emailField);

        inputPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        inputPanel.add(passwordField);

        add(inputPanel, BorderLayout.CENTER);

        // Panel for buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        JButton registerBtn = new JButton("Register");
        registerBtn.setBackground(new Color(44, 130, 108));
        registerBtn.setForeground(Color.WHITE);
        registerBtn.setFocusPainted(false);
        registerBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JButton backBtn = new JButton("🔙 Back");
        backBtn.setBackground(new Color(192, 57, 43));
        backBtn.setForeground(Color.WHITE);
        backBtn.setFocusPainted(false);
        backBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));

        btnPanel.add(registerBtn);
        btnPanel.add(backBtn);

        add(btnPanel, BorderLayout.SOUTH);

        // Register button logic
        registerBtn.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "❗ Please fill all fields.", "Input Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            boolean success = UserDAO.registerUser(username, email, password);
            if (success) {
                JOptionPane.showMessageDialog(this, "✅ Registration successful!", "Success", JOptionPane.INFORMATION_MESSAGE);

                // Save session info
                Session.loggedInUsername = username;
                Session.loggedInUserId = UserDAO.getUserIdByUsername(username);

                // Close registration form and show main menu again
                this.dispose();
                mainMenu.setLocation(this.getLocation());
                mainMenu.setLoggedInUser(username);
                mainMenu.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "❌ Registration failed. Try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Back button logic
        backBtn.addActionListener(e -> {
            this.dispose();
            mainMenu.setLocation(this.getLocation());
            mainMenu.setVisible(true);
        });

        setVisible(true);
    }
}




