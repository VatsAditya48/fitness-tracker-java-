import ui.MainFrame;

import javax.swing.*;

/**
 * App - entry point.
 */
public class App {
    public static void main(String[] args) {
        // Use system look and feel for modern appearance
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
