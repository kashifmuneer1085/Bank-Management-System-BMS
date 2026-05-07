package bank.ui;

import bank.service.BankService;
import bank.util.UIHelper;

import javax.swing.*;
import java.awt.*;

/**
 * Form to open a new bank account.
 */
public class OpenAccountFrame extends JFrame {

    private final BankService service;
    private final JFrame parent;

    private JTextField nameField, phoneField, cnicField, passwordField, addressField, balanceField;

    public OpenAccountFrame(BankService service, JFrame parent) {
        this.service = service;
        this.parent  = parent;
        setTitle("Open New Account — SecureBank");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        buildUI();
        pack();
        setMinimumSize(new Dimension(420, 460));
        UIHelper.centerOnScreen(this);
    }

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UIHelper.BG);

        // Header
        JPanel header = new JPanel();
        header.setBackground(UIHelper.PRIMARY);
        header.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));
        JLabel title = new JLabel("Open New Account");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(Color.WHITE);
        header.add(title);

        // Form
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(UIHelper.BG);
        form.setBorder(BorderFactory.createEmptyBorder(24, 40, 16, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(7, 6, 7, 6);
        gbc.fill   = GridBagConstraints.HORIZONTAL;

        nameField     = new JTextField(18);
        phoneField    = new JTextField(18);
        cnicField     = new JTextField(18);
        passwordField = new JTextField(18);
        addressField  = new JTextField(18);
        balanceField  = new JTextField(18);

        String[][] rows = {
            {"Full Name:",      "name"},
            {"Phone Number:",   "phone"},
            {"CNIC Number:",    "cnic"},
            {"Password:",       "password"},
            {"Address:",        "address"},
            {"Initial Balance:","balance"},
        };
        JTextField[] fields = { nameField, phoneField, cnicField, passwordField, addressField, balanceField };

        for (int i = 0; i < rows.length; i++) {
            gbc.gridwidth = 1; gbc.gridx = 0; gbc.gridy = i; gbc.weightx = 0;
            JLabel lbl = new JLabel(rows[i][0]);
            lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            form.add(lbl, gbc);
            gbc.gridx = 1; gbc.weightx = 1;
            form.add(fields[i], gbc);
        }

        // Buttons
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        btnRow.setBackground(UIHelper.BG);
        JButton createBtn = UIHelper.primaryButton("Create Account");
        JButton clearBtn  = UIHelper.secondaryButton("Clear");
        JButton backBtn   = UIHelper.secondaryButton("← Back");
        createBtn.setPreferredSize(new Dimension(140, 36));
        clearBtn.setPreferredSize(new Dimension(90, 36));
        backBtn.setPreferredSize(new Dimension(90, 36));
        btnRow.add(createBtn);
        btnRow.add(clearBtn);
        btnRow.add(backBtn);

        gbc.gridx = 0; gbc.gridy = rows.length; gbc.gridwidth = 2;
        gbc.insets = new Insets(18, 6, 8, 6);
        form.add(btnRow, gbc);

        root.add(header, BorderLayout.NORTH);
        root.add(form,   BorderLayout.CENTER);
        setContentPane(root);

        // ── Actions ──
        createBtn.addActionListener(e -> doCreate());
        clearBtn.addActionListener(e  -> clearFields());
        backBtn.addActionListener(e   -> dispose());
    }

    private void doCreate() {
        try {
            double balance = UIHelper.parseAmount(balanceField.getText(), "Initial Balance");
            int id = service.openAccount(
                    nameField.getText(), phoneField.getText(), cnicField.getText(),
                    passwordField.getText(), addressField.getText(), balance);
            UIHelper.showSuccess(this,
                    "Account created successfully!\nYour Customer ID is: " + id +
                    "\nPlease save your ID — you need it to log in.");
            clearFields();
        } catch (IllegalArgumentException ex) {
            UIHelper.showError(this, ex.getMessage());
        } catch (BankService.ServiceException ex) {
            UIHelper.showError(this, ex.getMessage());
        }
    }

    private void clearFields() {
        nameField.setText(""); phoneField.setText(""); cnicField.setText("");
        passwordField.setText(""); addressField.setText(""); balanceField.setText("");
        nameField.requestFocus();
    }
}
