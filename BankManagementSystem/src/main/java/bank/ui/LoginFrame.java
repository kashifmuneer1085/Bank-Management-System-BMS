package bank.ui;

import bank.model.Customer;
import bank.service.BankService;
import bank.util.UIHelper;

import javax.swing.*;
import java.awt.*;

/**
 * Login dialog for existing customers.
 */
public class LoginFrame extends JFrame {

    private final BankService service;

    public LoginFrame(BankService service) {
        this.service = service;
        setTitle("Login — SecureBank");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        buildUI();
        pack();
        setMinimumSize(new Dimension(380, 300));
        UIHelper.centerOnScreen(this);
    }

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout(0, 0));
        root.setBackground(UIHelper.BG);

        // Header
        JPanel header = new JPanel();
        header.setBackground(UIHelper.PRIMARY);
        header.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));
        JLabel title = new JLabel("Customer Login");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(Color.WHITE);
        header.add(title);

        // Form
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(UIHelper.BG);
        form.setBorder(BorderFactory.createEmptyBorder(30, 40, 20, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 6, 8, 6);
        gbc.fill   = GridBagConstraints.HORIZONTAL;

        JTextField   idField   = new JTextField(15);
        JPasswordField passField = new JPasswordField(15);

        addFormRow(form, gbc, 0, "Customer ID:", idField);
        addFormRow(form, gbc, 1, "Password:",    passField);

        // Buttons
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        btnRow.setBackground(UIHelper.BG);
        JButton loginBtn = UIHelper.primaryButton("Login");
        JButton backBtn  = UIHelper.secondaryButton("← Back");
        loginBtn.setPreferredSize(new Dimension(110, 36));
        backBtn.setPreferredSize(new Dimension(110, 36));
        btnRow.add(loginBtn);
        btnRow.add(backBtn);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        gbc.insets = new Insets(18, 6, 8, 6);
        form.add(btnRow, gbc);

        root.add(header, BorderLayout.NORTH);
        root.add(form,   BorderLayout.CENTER);
        setContentPane(root);

        // ── Actions ──
        loginBtn.addActionListener(e -> doLogin(idField, passField));
        passField.addActionListener(e -> doLogin(idField, passField));  // Enter key
        backBtn.addActionListener(e -> {
            new WelcomeFrame(service).setVisible(true);
            dispose();
        });
    }

    private void doLogin(JTextField idField, JPasswordField passField) {
        String id   = idField.getText().trim();
        String pass = new String(passField.getPassword()).trim();

        if (id.isEmpty() || pass.isEmpty()) {
            UIHelper.showError(this, "Please enter both ID and Password.");
            return;
        }

        try {
            Customer customer = service.login(id, pass);
            UIHelper.showSuccess(this, "Welcome back, " + customer.getName() + "!");
            new DashboardFrame(service, customer).setVisible(true);
            dispose();
        } catch (BankService.AuthException ex) {
            UIHelper.showError(this, ex.getMessage());
            passField.setText("");
            passField.requestFocus();
        }
    }

    private void addFormRow(JPanel panel, GridBagConstraints gbc, int row, String label, JComponent field) {
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = row;
        gbc.weightx = 0;
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panel.add(lbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        panel.add(field, gbc);
    }
}
