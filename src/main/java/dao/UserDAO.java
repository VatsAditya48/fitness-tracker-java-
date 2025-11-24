package dao;

import db.DBHelper;
import models.User;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * UserDAO - implements GenericRepository for User.
 * Uses PreparedStatement and handles SQLExceptions.
 */
public class UserDAO implements GenericRepository<User> {

    @Override
    public User create(User u) throws SQLException {
        String sql = "INSERT INTO users (name,email,age,height_cm,weight_kg,membership_type) VALUES (?,?,?,?,?,?)";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, u.getName());
            ps.setString(2, u.getEmail());
            ps.setObject(3, u.getAge(), Types.INTEGER);
            ps.setObject(4, u.getHeightCm(), Types.DOUBLE);
            ps.setObject(5, u.getWeightKg(), Types.DOUBLE);
            ps.setString(6, u.getMembershipType());
            int affected = ps.executeUpdate();
            if (affected == 0) throw new SQLException("Creating user failed, no rows affected.");
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) u.setId(keys.getInt(1));
            }
            return u;
        }
    }

    @Override
    public User findById(int id) throws SQLException {
        String sql = "SELECT * FROM users WHERE id=?";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRowToUser(rs);
            }
        }
        return null;
    }

    public User findByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM users WHERE email=?";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRowToUser(rs);
            }
        }
        return null;
    }

    @Override
    public List<User> findAll() throws SQLException {
        String sql = "SELECT * FROM users";
        List<User> list = new ArrayList<>();
        try (Connection conn = DBHelper.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapRowToUser(rs));
        }
        return list;
    }

    @Override
    public boolean update(User u) throws SQLException {
        String sql = "UPDATE users SET name=?,email=?,age=?,height_cm=?,weight_kg=?,membership_type=? WHERE id=?";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, u.getName());
            ps.setString(2, u.getEmail());
            ps.setObject(3, u.getAge(), Types.INTEGER);
            ps.setObject(4, u.getHeightCm(), Types.DOUBLE);
            ps.setObject(5, u.getWeightKg(), Types.DOUBLE);
            ps.setString(6, u.getMembershipType());
            ps.setInt(7, u.getId());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM users WHERE id=?";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private User mapRowToUser(ResultSet rs) throws SQLException {
        User u = new User();
        u.setId(rs.getInt("id"));
        u.setName(rs.getString("name"));
        u.setEmail(rs.getString("email"));
        int age = rs.getInt("age");
        if (!rs.wasNull()) u.setAge(age);
        double h = rs.getDouble("height_cm");
        if (!rs.wasNull()) u.setHeightCm(h);
        double w = rs.getDouble("weight_kg");
        if (!rs.wasNull()) u.setWeightKg(w);
        u.setMembershipType(rs.getString("membership_type"));
        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) u.setCreatedAt(ts.toLocalDateTime());
        return u;
    }
}