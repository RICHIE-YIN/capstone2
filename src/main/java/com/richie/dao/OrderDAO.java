package com.richie.dao;

import com.richie.model.*;
import com.richie.util.OrderValidator;

import java.sql.*;
import java.util.ArrayList;

public class OrderDAO {
    private static ToppingDAO toppingDAO = new ToppingDAO();

    public int saveOrder(Order order) {
        if (!OrderValidator.isValid(order)) {
            System.out.println("Order is invalid. You need to at least order a drink or side.");
            return -1;
        }

        String sqlOrderCommand =
                "INSERT INTO orders (customer_name, order_number, subtotal, tax, total) " +
                        "VALUES (?, ?, ?, ?, ?) RETURNING id";
        String sqlItemCommand =
                "INSERT INTO order_items (order_id, item_type, item_name, base, size, flavor, side_type, price) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";
        String sqlToppingCommand =
                "INSERT INTO order_item_toppings (order_item_id, topping_id, price) VALUES (?, ?, ?)";
        String sqlExtraCommand =
                "INSERT INTO order_item_extras (order_item_id, topping_id, upcharge) VALUES (?, ?, ?)";

        Connection conn = null;

        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            int orderId;
            int tempId = getNextOrderNumber();
            String orderNumber = String.format("F%04d", tempId);

            // Save order
            try (PreparedStatement preparedStatement = conn.prepareStatement(sqlOrderCommand)) {
                preparedStatement.setString(1, order.getName());
                preparedStatement.setString(2, orderNumber);
                preparedStatement.setDouble(3, order.getSubtotal());
                preparedStatement.setDouble(4, order.getTax());
                preparedStatement.setDouble(5, order.getTotal());

                ResultSet rs = preparedStatement.executeQuery();
                rs.next();
                orderId = rs.getInt("id");
                order.setId(orderId);
                order.setOrderNumber(orderNumber);
                System.out.println("Order saved: ID=" + orderId + " | Number=" + orderNumber);
            }

            // Save items
            for (Product product : order.getItems()) {
                int itemId;

                try (PreparedStatement preparedStatement = conn.prepareStatement(sqlItemCommand)) {
                    preparedStatement.setInt(1, orderId);

                    if (product instanceof PokeBowl) {
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

                if (product instanceof PokeBowl) {
                    PokeBowl pokeBowl = (PokeBowl) product;

                    // Toppings
                    try (PreparedStatement preparedStatement = conn.prepareStatement(sqlToppingCommand)) {
                        for (Topping t : pokeBowl.getToppings()) {
                            int toppingId = toppingDAO.getToppingId(conn, t.getName());
                            preparedStatement.setInt(1, itemId);
                            preparedStatement.setInt(2, toppingId);
                            preparedStatement.setDouble(3, t.getPrice());
                            preparedStatement.executeUpdate();
                            System.out.println("Topping added: " + t.getName());
                        }
                    }

                    // Extras
                    if (pokeBowl.hasExtras()) {
                        try (PreparedStatement preparedStatement = conn.prepareStatement(sqlExtraCommand)) {
                            for (Extra e : pokeBowl.getExtras()) {
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
            System.out.println("Transaction committed");
            return orderId;

        } catch (SQLException e) {
            System.out.println("Error saving order");
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback();
                    System.out.println("Transaction rolled back");
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
            return -1;
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private int getNextOrderNumber() {
        String sql = "SELECT COALESCE(MAX(id), 0) + 1 FROM orders";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            System.out.println("Error getting next order number: " + e.getMessage());
        }

        return 1;
    }
}
