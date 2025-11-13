package com.richie.dao;

import com.richie.model.Topping;

import java.sql.*;
import java.util.ArrayList;

public class ToppingDAO {

    public static void main(String[] args) {
        ToppingDAO dao = new ToppingDAO();

        System.out.println("Test 1: Get All Toppings");
        ArrayList<Topping> all = dao.getAllToppings();
        System.out.println("Total toppings: " + all.size());

        System.out.println("\nTest 2: Get Salmon");
        Topping salmon = dao.getToppingByName("Salmon");
        if(salmon != null) {
            System.out.println(salmon.getName() + " costs: " + salmon.getPrice());
        }
    }


    public ArrayList<Topping> getAllToppings() {
        ArrayList<Topping> toppings = new ArrayList<>();
        String sqlCommand = "SELECT name, base_price, is_premium FROM toppings ORDER BY name";

        try(Connection conn = DatabaseConnection.getConnection()){
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sqlCommand);

            while(rs.next()) {
                Topping topping = new Topping(
                        rs.getString("name"),
                        rs.getDouble("base_price"),
                        rs.getBoolean("is_premium")
                );
                toppings.add(topping);
            }
        } catch (SQLException e) {
            System.out.println("Error loading toppings");
            throw new RuntimeException(e);
        }
        return toppings;
    }

    public Topping getToppingByName(String name) {
        String sqlCommand = "SELECT name, base_price, is_premium FROM toppings WHERE name = ?";

        try(Connection conn = DatabaseConnection.getConnection()){
            //used preparedstatement to prevent sql injections
            PreparedStatement preparedStatement = conn.prepareStatement(sqlCommand);

            preparedStatement.setString(1, name);

            ResultSet rs = preparedStatement.executeQuery();

            if(rs.next()) {
                return new Topping(
                        rs.getString("name"),
                        rs.getDouble("base_price"),
                        rs.getBoolean("is_premium")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    //note to self: reason why we're putting Connection in parameters is so that we can work in the same transaction
    public int getToppingId(Connection conn, String name) throws SQLException {
        String sqlCommand = "SELECT id FROM toppings WHERE name = ?";

        try(PreparedStatement preparedStatement = conn.prepareStatement(sqlCommand)){
            preparedStatement.setString(1, name);
            ResultSet rs = preparedStatement.executeQuery();

            if(rs.next()) {
                return rs.getInt(("id"));
            }
        } catch (SQLException e) {
        throw new RuntimeException(e);
        }
        throw new SQLException("Topping not found: " + name);
    }
}
