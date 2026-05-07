package bank.util;

import javax.swing.*;
import java.awt.*;

/**
 * Shared Swing helpers to keep UI classes DRY.
 */
public final class UIHelper {

    // Bank colour palette
    public static final Color PRIMARY     = new Color(0, 82, 155);   // deep bank blue
    public static final Color PRIMARY_DARK = new Color(0, 55, 110);
    public static final Color ACCENT      = new Color(255, 180, 0);  // gold accent
    public static final Color BG          = new Color(240, 245, 255);
    public static final Color TEXT_LIGHT  = Color.WHITE;
    public static final Color ERROR_RED   = new Color(200, 30, 30);
    public static final Color SUCCESS_GREEN = new Color(30, 140, 30);

    private UIHelper() {}

    /** Apply FlatLaf / Nimbus look and feel, fall back gracefully. */
    public static void setupLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}
        // Global font bump
        setUIFont(new javax.swing.plaf.FontUIResource("Segoe UI", Font.PLAIN, 13));
    }

    private static void setUIFont(javax.swing.plaf.FontUIResource f) {
        java.util.Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof javax.swing.plaf.FontUIResource)
                UIManager.put(key, f);
        }
    }

    /** Create a styled primary action button. */
    public static JButton primaryButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(PRIMARY);
        btn.setForeground(TEXT_LIGHT);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        return btn;
    }

    /** Create a styled secondary (outline-style) button. */
    public static JButton secondaryButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(Color.WHITE);
        btn.setForeground(PRIMARY);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        return btn;
    }

    /** Create a titled panel with a consistent blue-bar header. */
    public static JPanel titledPanel(String title) {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(BG);

        JLabel header = new JLabel("  " + title, JLabel.LEFT);
        header.setFont(new Font("Segoe UI", Font.BOLD, 16));
        header.setForeground(TEXT_LIGHT);
        header.setBackground(PRIMARY);
        header.setOpaque(true);
        header.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));

        wrapper.add(header, BorderLayout.NORTH);
        return wrapper;
    }

    /** Show a styled success message dialog. */
    public static void showSuccess(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Success",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /** Show a styled error message dialog. */
    public static void showError(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    /** Ask a yes/no confirmation. Returns true if user clicks Yes. */
    public static boolean confirm(Component parent, String message) {
        return JOptionPane.showConfirmDialog(parent, message, "Confirm",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE)
                == JOptionPane.YES_OPTION;
    }

    /** Center a window on screen. */
    public static void centerOnScreen(Window window) {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        window.setLocation(
            (screen.width  - window.getWidth())  / 2,
            (screen.height - window.getHeight()) / 2
        );
    }

    /** Parse a double from a text field, throw IllegalArgumentException with a friendly message. */
    public static double parseAmount(String text, String fieldName) {
        try {
            double v = Double.parseDouble(text.trim());
            if (v <= 0) throw new NumberFormatException();
            return v;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(fieldName + " must be a positive number.");
        }
    }
}
