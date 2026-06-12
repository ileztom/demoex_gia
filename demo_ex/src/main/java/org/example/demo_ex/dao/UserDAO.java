package org.example.demo_ex.dao;

import org.example.demo_ex.model.User;

import java.sql.*;

public class UserDAO {

    public User authorize(String login, String password) {
        String sql = "SELECT * FROM users WHERE Login=? AND Password=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, login);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getInt("UserID"),
                        rs.getString("Login"),
                        rs.getString("Password"),
                        rs.getString("Role"),
                        rs.getString("FullName")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
