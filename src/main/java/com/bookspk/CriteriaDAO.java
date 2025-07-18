package com.bookspk;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CriteriaDAO {
    public List<Criteria> getAllCriteria() {
        List<Criteria> list = new ArrayList<>();
        String sql = "SELECT * FROM criteria ORDER BY id";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Criteria c = new Criteria(
                    rs.getInt("id"),
                    rs.getString("code"),
                    rs.getString("name"),
                    rs.getFloat("bobot")
                );
                list.add(c);
            }
        } catch (SQLException e) {
            System.err.println("Error getAllCriteria: " + e.getMessage());
        }
        return list;
    }

    public boolean addCriteria(Criteria c) {
        String sql = "INSERT INTO criteria (code, name, bobot) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getCode());
            ps.setString(2, c.getName());
            ps.setFloat(3, c.getBobot());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error addCriteria: " + e.getMessage());
            return false;
        }
    }

    public boolean updateCriteria(Criteria c) {
        String sql = "UPDATE criteria SET code=?, name=?, bobot=? WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getCode());
            ps.setString(2, c.getName());
            ps.setFloat(3, c.getBobot());
            ps.setInt(4, c.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updateCriteria: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteCriteria(int id) {
        String sql = "DELETE FROM criteria WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleteCriteria: " + e.getMessage());
            return false;
        }
    }
} 