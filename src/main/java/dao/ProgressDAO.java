package dao;

import db.DBHelper;
import models.Progress;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * ProgressDAO - CRUD for daily progress.
 */
public class ProgressDAO implements GenericRepository<Progress> {

    @Override
    public Progress create(Progress p) throws SQLException {
        String sql = "INSERT INTO progress (user_id,day,steps,sleep_hours,notes) VALUES (?,?,?,?,?)";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, p.getUserId());
            ps.setDate(2, Date.valueOf(p.getDay()));
            ps.setObject(3, p.getSteps(), Types.INTEGER);
            ps.setObject(4, p.getSleepHours(), Types.DOUBLE);
            ps.setString(5, p.getNotes());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) p.setProgressId(keys.getInt(1));
            }
            return p;
        }
    }

    @Override
    public Progress findById(int id) throws SQLException {
        String sql = "SELECT * FROM progress WHERE progress_id=?";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRowToProgress(rs);
            }
        }
        return null;
    }

    public List<Progress> findByUserId(int userId) throws SQLException {
        String sql = "SELECT * FROM progress WHERE user_id=? ORDER BY day DESC";
        List<Progress> list = new ArrayList<>();
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRowToProgress(rs));
            }
        }
        return list;
    }

    @Override
    public List<Progress> findAll() throws SQLException {
        String sql = "SELECT * FROM progress";
        List<Progress> list = new ArrayList<>();
        try (Connection conn = DBHelper.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapRowToProgress(rs));
        }
        return list;
    }

    @Override
    public boolean update(Progress p) throws SQLException {
        String sql = "UPDATE progress SET day=?,steps=?,sleep_hours=?,notes=? WHERE progress_id=?";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(p.getDay()));
            ps.setObject(2, p.getSteps(), Types.INTEGER);
            ps.setObject(3, p.getSleepHours(), Types.DOUBLE);
            ps.setString(4, p.getNotes());
            ps.setInt(5, p.getProgressId());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM progress WHERE progress_id=?";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private Progress mapRowToProgress(ResultSet rs) throws SQLException {
        Progress p = new Progress();
        p.setProgressId(rs.getInt("progress_id"));
        p.setUserId(rs.getInt("user_id"));
        Date d = rs.getDate("day");
        if (d != null) p.setDay(d.toLocalDate());
        int s = rs.getInt("steps"); if (!rs.wasNull()) p.setSteps(s);
        double sh = rs.getDouble("sleep_hours"); if (!rs.wasNull()) p.setSleepHours(sh);
        p.setNotes(rs.getString("notes"));
        return p;
    }
}
