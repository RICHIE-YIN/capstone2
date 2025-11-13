package com.richie.dao;

import com.richie.model.Sides;
import java.sql.*;
import java.util.ArrayList;

public class SidesDAO {

    public ArrayList<Sides> getAllSides() {
        ArrayList<Sides> sides = new ArrayList<>();
        String sqlCommand = "SELECT name FROM sides ORDER BY name";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sqlCommand)) {

            while (rs.next()) {
                Sides s = new Sides(rs.getString("name"));
                sides.add(s);
            }

        } catch (SQLException e) {
            System.out.println("error loading sides");
            e.printStackTrace();
        }

        return sides;
    }

    public Sides getSideByName(String name) {
        String sqlCommand = "SELECT name FROM sides WHERE name = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sqlCommand)) {

            preparedStatement.setString(1, name);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                return new Sides(rs.getString("name"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public int getSideId(Connection conn, String name) throws SQLException {
        String sqlCommand = "SELECT id FROM sides WHERE name = ?";

        try (PreparedStatement preparedStatement = conn.prepareStatement(sqlCommand)) {
            preparedStatement.setString(1, name);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                return rs.getInt("id");
            }

            throw new SQLException("Side not found: " + name);
        }
    }
}