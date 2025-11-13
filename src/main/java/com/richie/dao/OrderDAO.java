package com.richie.dao;

import com.richie.model.*;

import java.sql.*;
import java.util.ArrayList;

public class OrderDAO {
    private static ToppingDAO toppingDAO = new ToppingDAO();

    public static void main(String[] args) {
        System.out.println("Testing order");
        OrderDAO orderDAO = new OrderDAO();

        //test order
        Order order = new Order("Test Order");

        //add poke bowl
        PokeBowl pokeBowl = new PokeBowl("test bowl", "white rice", "l");

        // Add regular toppings
        Topping spicyTuna = toppingDAO.getToppingByName("Spicy Tuna");
        pokeBowl.addTopping(spicyTuna);
        pokeBowl.addTopping(toppingDAO.getToppingByName("Spicy Mayo"));
        pokeBowl.addTopping(toppingDAO.getToppingByName("Eel Sauce"));

        // Add an extra (double spicy tuna)
        Extra extraSpicyTuna = new Extra(spicyTuna);
        pokeBowl.addExtra(extraSpicyTuna);

        order.addItem(pokeBowl);

        //add drink
        Drink drink = new Drink("M", "Lavender Lemonade");
        order.addItem(drink);

        //add side
        Sides side = new Sides("Takoyaki");
        order.addItem(side);

        System.out.println("Saving order");
        int orderId = orderDAO.saveOrder(order);

        if(orderId > 0) {
            System.out.println("\nSuccess! Order ID: " + orderId);
            System.out.println("Total: $" + order.getTotal());
        } else {
            System.out.println("\nFailed");
        }
    }


    public int saveOrder(Order order) {
        String sqlOrderCommand = "INSERT INTO orders (customer_name, subtotal, tax, total) VALUES (?, ?, ?, ?) RETURNING id";
        String sqlItemCommand = "INSERT INTO order_items (order_id, item_type, item_name, base, size, flavor, side_type, price) VALUES (?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";
        String sqlToppingCommand = "INSERT INTO order_item_toppings (order_item_id, topping_id, price) VALUES (?, ?, ?)";
        String sqlExtraCommand = "INSERT INTO order_item_extras (order_item_id, topping_id, upcharge) VALUES (?, ?, ?)";

        Connection conn = null;

        try{
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            int orderId;

            //save order
            try(PreparedStatement preparedStatement = conn.prepareStatement(sqlOrderCommand)){
                preparedStatement.setString(1, order.getName());
                preparedStatement.setDouble(2, order.getSubtotal());
                preparedStatement.setDouble(3, order.getTax());
                preparedStatement.setDouble(4, order.getTotal());

                ResultSet rs = preparedStatement.executeQuery();
                rs.next();
                orderId = rs.getInt("id");
                System.out.println("Order saved: ID = " + orderId);
            }

            //save items
            for(Product product : order.getItems()) {
                int itemId;

                try(PreparedStatement preparedStatement = conn.prepareStatement(sqlItemCommand)){
                    preparedStatement.setInt(1, orderId);

                    //set fields for product type
                    if(product instanceof PokeBowl) {
                        PokeBowl pokeBowl = (PokeBowl) product;
                        preparedStatement.setString(2, "poke bowl");
                        preparedStatement.setString(3, pokeBowl.getName());
                        preparedStatement.setString(4, pokeBowl.getBase());
                        preparedStatement.setString(5, pokeBowl.getSize());
                        preparedStatement.setNull(6, Types.VARCHAR);
                        preparedStatement.setNull(7, Types.VARCHAR);
                    } else if (product instanceof Drink) {
                        Drink drink = (Drink) product;
                        preparedStatement.setString(2, "drink");
                        preparedStatement.setString(3, drink.getFlavor());
                        preparedStatement.setNull(4, Types.VARCHAR);
                        preparedStatement.setString(5, drink.getSize());
                        preparedStatement.setString(6, drink.getFlavor());
                        preparedStatement.setNull(7, Types.VARCHAR);
                    } else if (product instanceof Sides) {
                        Sides side = (Sides) product;
                        preparedStatement.setString(2, "side");
                        preparedStatement.setString(3, side.getType());
                        preparedStatement.setNull(4, Types.VARCHAR);
                        preparedStatement.setNull(5, Types.VARCHAR);
                        preparedStatement.setNull(6, Types.VARCHAR);
                        preparedStatement.setString(7, side.getType());
                    }

                    preparedStatement.setDouble(8, product.getPrice());

                    ResultSet rs = preparedStatement.executeQuery();
                    rs.next();
                    itemId = rs.getInt("id");
                    System.out.println("Item saved: Id = " + itemId);
                }

                //save toppings and extras if bowl
                if(product instanceof PokeBowl) {
                    PokeBowl pokeBowl = (PokeBowl) product;

                    // Save toppings
                    try(PreparedStatement preparedStatement = conn.prepareStatement(sqlToppingCommand)) {
                        for(Topping t : pokeBowl.getToppings()) {
                            int toppingId = toppingDAO.getToppingId(conn, t.getName());
                            preparedStatement.setInt(1, itemId);
                            preparedStatement.setInt(2, toppingId);
                            preparedStatement.setDouble(3, t.getPrice());
                            preparedStatement.executeUpdate();
                            System.out.println("Topping added: " + t.getName());
                        }
                    }

                    // Save extras
                    if (pokeBowl.hasExtras()) {
                        try(PreparedStatement preparedStatement = conn.prepareStatement(sqlExtraCommand)) {
                            for(Extra e : pokeBowl.getExtras()) {
                                // Get the topping ID from the extra's topping
                                int toppingId = toppingDAO.getToppingId(conn, e.getTopping().getName());
                                preparedStatement.setInt(1, itemId);
                                preparedStatement.setInt(2, toppingId);
                                preparedStatement.setDouble(3, e.getUpcharge());
                                preparedStatement.executeUpdate();
                                System.out.println("Extra added: " + e.getName() + " (+$" + e.getUpcharge() + ")");
                            }
                        }
                    }
                }
            }

            conn.commit();
            System.out.println("Transaction commited");
            return orderId;

        } catch (SQLException e) {
            System.out.println("Error saving order");
            e.printStackTrace();
            if(conn != null) {
                try{
                    conn.rollback();
                    System.out.println("Transaction rolled back");
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
            return -1;
        } finally {
            if(conn != null) {
                try{
                    conn.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}