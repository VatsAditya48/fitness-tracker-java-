package dao;

import db.DBHelper;
import models.Workout;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * WorkoutDAO - CRUD for Workout records.
 */
public class WorkoutDAO implements GenericRepository<Workout> {

    @Override
    public Workout create(Workout w) throws SQLException {
        String sql = "INSERT INTO workouts (user_id,type,duration_minutes,calories_burned,workout_date) VALUES (?,?,?,?,?)";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, w.getUserId());
            ps.setString(2, w.getType());
            ps.setInt(3, w.getDurationMinutes());
            ps.setInt(4, w.getCaloriesBurned());
            ps.setDate(5, Date.valueOf(w.getWorkoutDate()));
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) w.setWorkoutId(keys.getInt(1));
            }
            return w;
        }
    }

    @Override
    public Workout findById(int id) throws SQLException {
        String sql = "SELECT * FROM workouts WHERE workout_id=?";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRowToWorkout(rs);
            }
        }
        return null;
    }

    public List<Workout> findByUserId(int userId) throws SQLException {
        String sql = "SELECT * FROM workouts WHERE user_id=? ORDER BY workout_date DESC";
        List<Workout> list = new ArrayList<>();
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRowToWorkout(rs));
            }
        }
        return list;
    }

    @Override
    public List<Workout> findAll() throws SQLException {
        String sql = "SELECT * FROM workouts";
        List<Workout> list = new ArrayList<>();
        try (Connection conn = DBHelper.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapRowToWorkout(rs));
        }
        return list;
    }

    @Override
    public boolean update(Workout w) throws SQLException {
        String sql = "UPDATE workouts SET type=?,duration_minutes=?,calories_burned=?,workout_date=? WHERE workout_id=?";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, w.getType());
            ps.setInt(2, w.getDurationMinutes());
            ps.setInt(3, w.getCaloriesBurned());
            ps.setDate(4, Date.valueOf(w.getWorkoutDate()));
            ps.setInt(5, w.getWorkoutId());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM workouts WHERE workout_id=?";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private Workout mapRowToWorkout(ResultSet rs) throws SQLException {
        Workout w = new Workout();
        w.setWorkoutId(rs.getInt("workout_id"));
        w.setUserId(rs.getInt("user_id"));
        w.setType(rs.getString("type"));
        w.setDurationMinutes(rs.getInt("duration_minutes"));
        w.setCaloriesBurned(rs.getInt("calories_burned"));
        Date d = rs.getDate("workout_date");
        if (d != null) w.setWorkoutDate(d.toLocalDate());
        return w;
    }
}