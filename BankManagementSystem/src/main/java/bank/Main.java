package bank;

import bank.service.BankService;
import bank.service.CustomerRepository;
import bank.ui.WelcomeFrame;
import bank.util.UIHelper;

import javax.swing.*;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Application entry point.
 * Run this class from IntelliJ.
 */
public class Main {

    public static void main(String[] args) {
        // Resolve data file relative to project root (works in IntelliJ)
        Path dataFile = Paths.get("data", "customers.csv");

        CustomerRepository repo    = new CustomerRepository(dataFile);
        BankService        service = new BankService(repo);

        UIHelper.setupLookAndFeel();

        SwingUtilities.invokeLater(() -> {
            WelcomeFrame welcome = new WelcomeFrame(service);
            welcome.setVisible(true);
        });
    }
}
