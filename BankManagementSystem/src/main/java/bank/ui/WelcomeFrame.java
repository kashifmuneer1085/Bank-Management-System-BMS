package bank.ui;

import bank.service.BankService;
import bank.service.CustomerRepository;
import bank.util.UIHelper;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Path;

/**
 * Entry point window — shown first when the app launches.
 */
public class WelcomeFrame extends JFrame {

    public WelcomeFrame(BankService service) {
        setTitle("Welcome — SecureBank");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(UIHelper.BG);

        // ── Header ──
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(UIHelper.PRIMARY_DARK);
        header.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        JLabel bankName = new JLabel("🏦  SecureBank", JLabel.CENTER);
        bankName.setFont(new Font("Segoe UI", Font.BOLD, 28));
        bankName.setForeground(UIHelper.ACCENT);

        JLabel tagline = new JLabel("Your trusted banking partner", JLabel.CENTER);
        tagline.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        tagline.setForeground(Color.WHITE);

        header.add(bankName,  BorderLayout.CENTER);
        header.add(tagline,   BorderLayout.SOUTH);

        // ── Buttons ──
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setBackground(UIHelper.BG);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.weightx = 1;

        JButton loginBtn  = UIHelper.primaryButton("🔑  Existing Customer – Login");
        JButton signupBtn = UIHelper.primaryButton("➕  New Customer – Open Account");
        JButton exitBtn   = UIHelper.secondaryButton("✕  Exit");

        loginBtn.setPreferredSize(new Dimension(280, 45));
        signupBtn.setPreferredSize(new Dimension(280, 45));
        exitBtn.setPreferredSize(new Dimension(280, 40));

        gbc.gridy = 0; buttonPanel.add(loginBtn,  gbc);
        gbc.gridy = 1; buttonPanel.add(signupBtn, gbc);
        gbc.gridy = 2; buttonPanel.add(exitBtn,   gbc);

        // ── Footer ──
        JLabel footer = new JLabel("© 2025 SecureBank. All rights reserved.", JLabel.CENTER);
        footer.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        footer.setForeground(Color.GRAY);
        footer.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));

        main.add(header,      BorderLayout.NORTH);
        main.add(buttonPanel, BorderLayout.CENTER);
        main.add(footer,      BorderLayout.SOUTH);

        setContentPane(main);
        pack();
        setMinimumSize(new Dimension(400, 350));
        UIHelper.centerOnScreen(this);

        // ── Actions ──
        loginBtn.addActionListener(e -> {
            LoginFrame login = new LoginFrame(service);
            login.setVisible(true);
            dispose();
        });

        signupBtn.addActionListener(e -> {
            OpenAccountFrame open = new OpenAccountFrame(service, this);
            open.setVisible(true);
        });

        exitBtn.addActionListener(e -> System.exit(0));
    }
}
