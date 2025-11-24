package ui;

import dao.UserDAO;
import dao.WorkoutDAO;
import dao.ProgressDAO;
import models.User;
import models.Workout;
import models.Progress;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * MainFrame - Swing GUI with Login, Dashboard & Workout Logging.
 * Simple, modular panels inside a frame.
 */
public class MainFrame extends JFrame {

    private CardLayout cardLayout = new CardLayout();
    private JPanel rootPanel = new JPanel(cardLayout);

    private UserDAO userDAO = new UserDAO();
    private WorkoutDAO workoutDAO = new WorkoutDAO();
    private ProgressDAO progressDAO = new ProgressDAO();

    private User currentUser;

    public MainFrame() {
        setTitle("Fitness Tracker - Review 1");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        rootPanel.add(new LoginPanel(), "login");
        rootPanel.add(new DashboardPanel(), "dashboard");
        rootPanel.add(new WorkoutLogPanel(), "workout");

        add(rootPanel);
        cardLayout.show(rootPanel, "login");

        startAutoSaveThread();
    }

    // Background autosave / sync thread
    private void startAutoSaveThread() {
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            if (currentUser != null) {
                try {
                    // Example auto-sync: fetch latest workouts (non-blocking)
                    List<Workout> list = workoutDAO.findByUserId(currentUser.getId());
                    // In a more complex app, update UI via SwingUtilities.invokeLater(...)
                    System.out.println("Auto-sync: fetched " + list.size() + " workouts for user " + currentUser.getEmail());
                } catch (SQLException ex) {
                    System.err.println("Auto-sync failed: " + ex.getMessage());
                }
            }
        }, 10, 20, TimeUnit.SECONDS); // initial delay, period
    }

    // ---------- Panels ----------

    private class LoginPanel extends JPanel {
        private JTextField emailField = new JTextField(25);
        private JTextField nameField = new JTextField(25);

        public LoginPanel() {
            setLayout(new BorderLayout());
            setBackground(new Color(30,30,30));
            JPanel box = new JPanel(new GridBagLayout());
            box.setOpaque(false);
            box.setBorder(new EmptyBorder(40,40,40,40));

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(8,8,8,8);
            gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
            box.add(new JLabel("Email:"), gbc);
            gbc.gridx = 1; box.add(emailField, gbc);

            gbc.gridy++;
            gbc.gridx = 0; box.add(new JLabel("Name (new user):"), gbc);
            gbc.gridx = 1; box.add(nameField, gbc);

            gbc.gridy++;
            gbc.gridx = 1;
            JButton loginBtn = new JButton("Login / Register");
            loginBtn.addActionListener(e -> onLogin());
            box.add(loginBtn, gbc);

            add(Box.createVerticalStrut(80), BorderLayout.NORTH);
            add(box, BorderLayout.CENTER);
        }

        private void onLogin() {
            String email = emailField.getText().trim();
            String name = nameField.getText().trim();
            if (email.isEmpty()) { JOptionPane.showMessageDialog(this, "Enter email"); return; }

            SwingUtilities.invokeLater(() -> {
                try {
                    User u = userDAO.findByEmail(email);
                    if (u == null) {
                        if (name.isEmpty()) { JOptionPane.showMessageDialog(this, "Enter name for new user"); return; }
                        u = new User(name, email);
                        userDAO.create(u);
                        JOptionPane.showMessageDialog(this, "New user created: " + u.getName());
                    } else {
                        JOptionPane.showMessageDialog(this, "Welcome back, " + u.getName());
                    }
                    currentUser = u;
                    cardLayout.show(rootPanel, "dashboard");
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "DB Error: " + ex.getMessage());
                }
            });
        }
    }

    private class DashboardPanel extends JPanel {
        private JLabel welcomeLabel = new JLabel();

        public DashboardPanel() {
            setLayout(new BorderLayout());
            JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
            top.setBorder(new EmptyBorder(10,10,10,10));
            top.add(welcomeLabel);

            JButton logBtn = new JButton("Log Workout");
            logBtn.addActionListener(e -> cardLayout.show(rootPanel, "workout"));
            top.add(logBtn);

            JButton refresh = new JButton("Refresh");
            refresh.addActionListener((ActionEvent e) -> loadDashboard());
            top.add(refresh);

            add(top, BorderLayout.NORTH);

            // center placeholder
            add(new JLabel("<html><center>Dashboard charts will appear here (placeholder).</center></html>", SwingConstants.CENTER), BorderLayout.CENTER);
        }

        // load user-specific data
        private void loadDashboard() {
            if (currentUser == null) return;
            welcomeLabel.setText("Logged in: " + currentUser.getName() + " (" + currentUser.getEmail() + ")");
            SwingUtilities.invokeLater(() -> {
                try {
                    // load workouts and progress
                    List<Workout> w = workoutDAO.findByUserId(currentUser.getId());
                    List<Progress> p = progressDAO.findByUserId(currentUser.getId());
                    System.out.println("Dashboard load: " + w.size() + " workouts, " + p.size() + " progress rows.");
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Error loading dashboard: " + ex.getMessage());
                }
            });
        }
    }

    private class WorkoutLogPanel extends JPanel {
        private JComboBox<String> typeCombo = new JComboBox<>(new String[]{"Cardio","Strength","Flexibility"});
        private JTextField durationField = new JTextField(6);
        private JTextField caloriesField = new JTextField(6);
        private JButton saveBtn = new JButton("Save");

        public WorkoutLogPanel() {
            setLayout(new FlowLayout(FlowLayout.LEFT));
            setBorder(new EmptyBorder(20,20,20,20));
            add(new JLabel("Type:")); add(typeCombo);
            add(new JLabel("Duration (min):")); add(durationField);
            add(new JLabel("Calories:")); add(caloriesField);
            add(saveBtn);

            saveBtn.addActionListener(e -> onSave());
            JButton back = new JButton("Back");
            back.addActionListener(e -> cardLayout.show(rootPanel, "dashboard"));
            add(back);
        }

        private void onSave() {
            if (currentUser == null) { JOptionPane.showMessageDialog(this, "No user logged in"); return; }
            try {
                Workout w = new Workout();
                w.setUserId(currentUser.getId());
                w.setType((String) typeCombo.getSelectedItem());
                w.setDurationMinutes(Integer.parseInt(durationField.getText().trim()));
                w.setCaloriesBurned(Integer.parseInt(caloriesField.getText().trim()));
                w.setWorkoutDate(LocalDate.now());
                workoutDAO.create(w);
                JOptionPane.showMessageDialog(this, "Workout saved");
            } catch (NumberFormatException nf) {
                JOptionPane.showMessageDialog(this, "Enter numeric values for duration/calories");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "DB Error: " + ex.getMessage());
            }
        }
    }
}
