package com.scc.dao;

import com.scc.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao {

    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT id, student_name, building, room_no, monthly_rent FROM allocation";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                users.add(new User(
                        rs.getInt("id"),
                        rs.getString("student_name"),
                        rs.getString("building"),
                        rs.getString("room_no"),
                        rs.getDouble("monthly_rent")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching allocations: " + e.getMessage());
        }
        return users;
    }

    public int insert(User user) {
        String sql = "INSERT INTO allocation (student_name, building, room_no, monthly_rent) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, user.studentName());
            pstmt.setString(2, user.building());
            pstmt.setString(3, user.roomNo());
            pstmt.setDouble(4, user.monthlyRent());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1); // Returns the newly created auto-increment ID
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error inserting allocation: " + e.getMessage());
        }
        return -1; // Returns -1 on failure
    }
}