import java.io.*;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;


public class FitnessSystem {
    // Console Test (Main Method) to demonstrate non-web features
    public static void main(String[] args) {
        System.out.println("=== Console Test Started ===");
        try {
            // 1. Test OOP & Collections
            List<FitnessActivity> activities = new ArrayList<>();
            activities.add(new Running(101, 30, 5.0, "2025-12-01"));
            activities.add(new Cycling(101, 60, 20.0, "2025-12-02"));

            // 2. Test Generics (Added Custom Generic Class)
            GoalValidator<Double> calorieGoal = new GoalValidator<>(500.0);
            double totalBurned = activities.get(0).calculateCaloriesBurned() + activities.get(1).calculateCaloriesBurned();
            System.out.println("Goal Met? " + calorieGoal.isMet(totalBurned));

            // 3. Test Multithreading
            AnalyticsProcessor processor = new AnalyticsProcessor(activities);
            Thread bgThread = new Thread(processor);
            bgThread.start();
            bgThread.join(); 

            System.out.println("Console Test Completed.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

// ================= 1. CUSTOM EXCEPTIONS =================

class DatabaseException extends Exception {
    public DatabaseException(String message) { super(message); }
}

class InvalidActivityException extends Exception {
    public InvalidActivityException(String message) { super(message); }
}

// ================= 2. GENERICS (Requirement: 6 Marks) =================

// Custom Generic Class to validate any numeric goal (Integer, Double, Float)
class GoalValidator<T extends Number> {
    private T target;

    public GoalValidator(T target) {
        this.target = target;
    }

    public boolean isMet(T value) {
        return value.doubleValue() >= target.doubleValue();
    }
}

// ================= 3. OOP MODELS (Requirement: 10 Marks) =================

interface Activity {
    double calculateCaloriesBurned();
    String getActivityType();
}

abstract class FitnessActivity implements Activity {
    protected int userId;
    protected String activityName;
    protected int durationMinutes;
    protected String date;

    public FitnessActivity(int userId, String activityName, int durationMinutes, String date) {
        this.userId = userId;
        this.activityName = activityName;
        this.durationMinutes = durationMinutes;
        this.date = date;
    }
    
    // Getters
    public int getUserId() { return userId; }
    public int getDurationMinutes() { return durationMinutes; }
    public String getDate() { return date; }
}

class Running extends FitnessActivity {
    private double distanceKm;
    
    public Running(int userId, int durationMinutes, double distanceKm, String date) {
        super(userId, "Running", durationMinutes, date);
        this.distanceKm = distanceKm;
    }

    @Override
    public double calculateCaloriesBurned() {
        return distanceKm * 60; // 60 calories per km
    }

    @Override
    public String getActivityType() { return "RUNNING"; }
}

class Cycling extends FitnessActivity {
    private double distanceKm;
    
    public Cycling(int userId, int durationMinutes, double distanceKm, String date) {
        super(userId, "Cycling", durationMinutes, date);
        this.distanceKm = distanceKm;
    }

    @Override
    public double calculateCaloriesBurned() {
        return distanceKm * 40; // 40 calories per km
    }

    @Override
    public String getActivityType() { return "CYCLING"; }
}

// ================= 4. MULTITHREADING (Requirement: 4 Marks) =================

class AnalyticsProcessor implements Runnable {
    private List<FitnessActivity> data;

    public AnalyticsProcessor(List<FitnessActivity> data) {
        this.data = data;
    }

    @Override
    public void run() {
        // Synchronization Block
        synchronized(System.out) {
            System.out.println("[Thread]: Processing analytics for " + data.size() + " activities...");
            try {
                Thread.sleep(500); // Simulate delay
                double total = 0;
                for(FitnessActivity a : data) total += a.calculateCaloriesBurned();
                System.out.println("[Thread]: Total Calories Burned: " + total);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}

// ================= 5. DATABASE & JDBC (Requirement: 13 Marks) =================

class DBConnection {
    // Update these credentials for your PC
    private static final String URL = "jdbc:mysql://localhost:3306/fitness_db";
    private static final String USER = "root";
    private static final String PASS = "password"; 

    public static Connection getConnection() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(URL, USER, PASS);
    }
}

class ActivityDAO {
    public void save(FitnessActivity a) throws DatabaseException {
        String sql = "INSERT INTO activities (user_id, type, duration, calories, date_logged) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, a.getUserId());
            ps.setString(2, a.getActivityType());
            ps.setInt(3, a.getDurationMinutes());
            ps.setDouble(4, a.calculateCaloriesBurned());
            ps.setString(5, a.getDate());
            ps.executeUpdate();
        } catch (Exception e) {
            throw new DatabaseException("Save Error: " + e.getMessage());
        }
    }

    public List<FitnessActivity> getAll(int userId) throws DatabaseException {
        List<FitnessActivity> list = new ArrayList<>();
        String sql = "SELECT * FROM activities WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                String type = rs.getString("type");
                int dur = rs.getInt("duration");
                String date = rs.getString("date_logged");
                if("RUNNING".equalsIgnoreCase(type)) 
                    list.add(new Running(userId, dur, 0, date));
                else 
                    list.add(new Cycling(userId, dur, 0, date));
            }
        } catch (Exception e) {
            throw new DatabaseException("Load Error: " + e.getMessage());
        }
        return list;
    }
}

// ================= 6. SERVLET (Requirement: 10 Marks) =================

@WebServlet("/fitness")
class FitnessServlet extends HttpServlet {
    private ActivityDAO dao = new ActivityDAO();

    // GET: Show Form and History
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        out.println("<html><body><h2>Fitness Tracker</h2>");
        
        // Form
        out.println("<form method='POST'>");
        out.println("Type: <select name='type'><option>Running</option><option>Cycling</option></select><br>");
        out.println("Duration (mins): <input type='number' name='dur' required><br>");
        out.println("Distance (km): <input type='text' name='dist' required><br>");
        out.println("<button type='submit'>Save</button></form>");
        
        // Table
        out.println("<hr><h3>History</h3><table border='1'><tr><th>Type</th><th>Cals</th></tr>");
        try {
            List<FitnessActivity> history = dao.getAll(101); // Hardcoded user 101
            for(FitnessActivity a : history) {
                out.println("<tr><td>" + a.getActivityType() + "</td><td>" + a.calculateCaloriesBurned() + "</td></tr>");
            }
        } catch (Exception e) { out.println("Error loading data: " + e.getMessage()); }
        out.println("</table></body></html>");
    }

    // POST: Save Data
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String type = req.getParameter("type");
            int dur = Integer.parseInt(req.getParameter("dur"));
            double dist = Double.parseDouble(req.getParameter("dist"));
            
            FitnessActivity act = type.equals("Running") ? 
                new Running(101, dur, dist, "2025-12-19") : new Cycling(101, dur, dist, "2025-12-19");
                
            dao.save(act);
            
            // Background Thread Analysis
            new Thread(new AnalyticsProcessor(Collections.singletonList(act))).start();
            
            resp.sendRedirect("fitness");
        } catch (Exception e) {
            resp.getWriter().println("Error: " + e.getMessage());
        }
    }
}
