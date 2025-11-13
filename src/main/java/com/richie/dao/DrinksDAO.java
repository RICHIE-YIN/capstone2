package com.richie.dao;

import com.richie.model.Drink;
import java.sql.*;
import java.util.ArrayList;

public class DrinksDAO {

    public ArrayList<Drink> getAllDrinks() {
        ArrayList<Drink> drinks = new ArrayList<>();
        String sqlCommand = "SELECT name FROM drinks ORDER BY name";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sqlCommand)) {

            while (rs.next()) {
                // Create Drink with N/A size as placeholder
                Drink d = new Drink("N/A", rs.getString("name"));
                drinks.add(d);
            }

        } catch (SQLException e) {
            System.out.println("error loading drinks");
            e.printStackTrace();
        }

        return drinks;
    }

    public Drink getDrinkByFlavor(String flavor, String size) {
        String sqlCommand = "SELECT name FROM drinks WHERE name = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sqlCommand)) {

            preparedStatement.setString(1, flavor);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                return new Drink(size, rs.getString("name"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public int getDrinkId(Connection conn, String flavor) throws SQLException {
        String sqlCommand = "SELECT id FROM drinks WHERE name = ?";

        try (PreparedStatement preparedStatement = conn.prepareStatement(sqlCommand)) {
            preparedStatement.setString(1, flavor);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                return rs.getInt("id");
            }

            throw new SQLException("Drink not found: " + flavor);
        }
    }
}