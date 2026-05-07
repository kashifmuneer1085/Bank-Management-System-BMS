package bank.ui;

import bank.model.Customer;
import bank.service.BankService;
import bank.util.UIHelper;

import javax.swing.*;
import java.awt.*;

/**
 * Main dashboard shown after login.
 * Provides access to all customer operations.
 */
public class DashboardFrame extends JFrame {

    private final BankService service;
    private       Customer    customer;   // mutable — updated after each operation

    public DashboardFrame(BankService service, Customer customer) {
        this.service  = service;
        this.customer = customer;
        setTitle("Dashboard — " + customer.getName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        buildUI();
        pack();
        setMinimumSize(new Dimension(440, 520));
        UIHelper.centerOnScreen(this);
    }

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout(0, 0));
        root.setBackground(UIHelper.BG);

        // ── Top bar ──
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(UIHelper.PRIMARY_DARK);
        topBar.setBorder(BorderFactory.createEmptyBorder(14, 20, 14, 20));

        JLabel greet = new JLabel("👤  " + customer.getName());
        greet.setFont(new Font("Segoe UI", Font.BOLD, 16));
        greet.setForeground(UIHelper.ACCENT);

        JLabel idLbl = new JLabel("ID: " + customer.getId() + "   |   Acc. Balance: Rs " + formatBalance());
        idLbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        idLbl.setForeground(Color.WHITE);

        topBar.add(greet, BorderLayout.NORTH);
        topBar.add(idLbl, BorderLayout.SOUTH);

        // ── Buttons grid ──
        JPanel grid = new JPanel(new GridLayout(4, 2, 12, 12));
        grid.setBackground(UIHelper.BG);
        grid.setBorder(BorderFactory.createEmptyBorder(30, 36, 20, 36));

        JButton depositBtn  = menuButton("💰  Deposit",        new Color(0, 120, 60));
        JButton withdrawBtn = menuButton("💸  Withdraw",       new Color(170, 50, 0));
        JButton balanceBtn  = menuButton("📊  Check Balance",  UIHelper.PRIMARY);
        JButton showBtn     = menuButton("📋  My Details",     UIHelper.PRIMARY);
        JButton addressBtn  = menuButton("🏠  Update Address", new Color(80, 0, 140));
        JButton passBtn     = menuButton("🔒  Change Password",new Color(80, 0, 140));
        JButton deleteBtn   = menuButton("🗑️  Delete Account", new Color(160, 0, 0));
        JButton logoutBtn   = menuButton("⬅  Logout",          Color.DARK_GRAY);

        grid.add(depositBtn); grid.add(withdrawBtn);
        grid.add(balanceBtn); grid.add(showBtn);
        grid.add(addressBtn); grid.add(passBtn);
        grid.add(deleteBtn);  grid.add(logoutBtn);

        root.add(topBar, BorderLayout.NORTH);
        root.add(grid,   BorderLayout.CENTER);
        setContentPane(root);

        // ── Actions ──
        depositBtn.addActionListener(e  -> doDeposit());
        withdrawBtn.addActionListener(e -> doWithdraw());
        balanceBtn.addActionListener(e  -> doCheckBalance());
        showBtn.addActionListener(e     -> doShowDetails());
        addressBtn.addActionListener(e  -> doUpdateAddress());
        passBtn.addActionListener(e     -> doChangePassword());
        deleteBtn.addActionListener(e   -> doDeleteAccount());
        logoutBtn.addActionListener(e   -> doLogout());
    }

    // ── Operations ─────────────────────────────────────────────────────────────

    private void doDeposit() {
        String input = JOptionPane.showInputDialog(this, "Enter deposit amount (Rs):", "Deposit", JOptionPane.PLAIN_MESSAGE);
        if (input == null) return;
        try {
            double amount     = UIHelper.parseAmount(input, "Amount");
            double newBalance = service.deposit(customer, amount);
            UIHelper.showSuccess(this, "Deposit successful!\nNew Balance: Rs " + (int) newBalance);
            refreshTopBar();
        } catch (IllegalArgumentException | BankService.ServiceException ex) {
            UIHelper.showError(this, ex.getMessage());
        }
    }

    private void doWithdraw() {
        String input = JOptionPane.showInputDialog(this, "Enter withdrawal amount (Rs):", "Withdraw", JOptionPane.PLAIN_MESSAGE);
        if (input == null) return;
        try {
            double amount     = UIHelper.parseAmount(input, "Amount");
            double newBalance = service.withdraw(customer, amount);
            UIHelper.showSuccess(this, "Withdrawal successful!\nRemaining Balance: Rs " + (int) newBalance);
            refreshTopBar();
        } catch (IllegalArgumentException | BankService.ServiceException ex) {
            UIHelper.showError(this, ex.getMessage());
        }
    }

    private void doCheckBalance() {
        UIHelper.showSuccess(this, "Current Balance: Rs " + (int) customer.getBalance());
    }

    private void doShowDetails() {
        String msg = "── Account Details ─────────────────\n" +
                     "Customer ID   : " + customer.getId()       + "\n" +
                     "Name          : " + customer.getName()     + "\n" +
                     "Phone         : " + customer.getPhone()    + "\n" +
                     "CNIC          : " + customer.getCnic()     + "\n" +
                     "Address       : " + customer.getAddress()  + "\n" +
                     "Balance       : Rs " + (int) customer.getBalance();
        JOptionPane.showMessageDialog(this, msg, "My Details", JOptionPane.INFORMATION_MESSAGE);
    }

    private void doUpdateAddress() {
        String input = JOptionPane.showInputDialog(this, "Enter new address:", "Update Address", JOptionPane.PLAIN_MESSAGE);
        if (input == null) return;
        try {
            service.updateAddress(customer, input);
            UIHelper.showSuccess(this, "Address updated successfully!");
        } catch (BankService.ServiceException ex) {
            UIHelper.showError(this, ex.getMessage());
        }
    }

    private void doChangePassword() {
        JPasswordField pf1 = new JPasswordField();
        JPasswordField pf2 = new JPasswordField();
        Object[] msg = { "New Password:", pf1, "Confirm Password:", pf2 };
        int opt = JOptionPane.showConfirmDialog(this, msg, "Change Password", JOptionPane.OK_CANCEL_OPTION);
        if (opt != JOptionPane.OK_OPTION) return;
        String p1 = new String(pf1.getPassword());
        String p2 = new String(pf2.getPassword());
        if (!p1.equals(p2)) { UIHelper.showError(this, "Passwords do not match."); return; }
        try {
            service.updatePassword(customer, p1);
            UIHelper.showSuccess(this, "Password changed successfully!");
        } catch (BankService.ServiceException ex) {
            UIHelper.showError(this, ex.getMessage());
        }
    }

    private void doDeleteAccount() {
        if (!UIHelper.confirm(this, "Are you sure you want to permanently delete your account?\nThis cannot be undone."))
            return;
        try {
            service.deleteAccount(customer);
            UIHelper.showSuccess(this, "Account deleted successfully. Goodbye!");
            doLogout();
        } catch (BankService.ServiceException ex) {
            UIHelper.showError(this, ex.getMessage());
        }
    }

    private void doLogout() {
        new WelcomeFrame(service).setVisible(true);
        dispose();
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    private String formatBalance() {
        return String.format("%,.0f", customer.getBalance());
    }

    /** Rebuild the top bar after a balance change. */
    private void refreshTopBar() {
        // Simplest: rebuild the whole UI
        getContentPane().removeAll();
        buildUI();
        revalidate();
        repaint();
    }

    private JButton menuButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 10));
        return btn;
    }
}
