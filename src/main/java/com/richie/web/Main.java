package com.richie.web;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import com.richie.dao.*;
import com.richie.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Main {

    private static ToppingDAO toppingDAO = new ToppingDAO();
    private static OrderDAO orderDAO = new OrderDAO();

    public static void main(String[] args) {
        int port = getPort();

        Javalin app = Javalin.create().start(port);
        System.out.println("Server running on port: " + port);

        app.get("/", ctx -> {
            ctx.result("Funkin Poke API. Go to /api/toppings");
        });

        app.get("/api/health", ctx -> {
            ctx.json(Map.of("status", "healthy"));
        });

        //get all toppings
        app.get("/api/toppings", ctx -> {
            try{
                ArrayList<Topping> toppings = toppingDAO.getAllToppings();
                ctx.json(toppings);
            } catch (Exception e) {
                ctx.status(400);
                ctx.json(Map.of("error", "failed to load toppings"));
                throw new RuntimeException(e);
            }
        });

        //order
        app.post("/api/orders", ctx -> {
            try{
                OrderRequest request = ctx.bodyAsClass(OrderRequest.class);

                if(request.customerName == null || request.customerName.trim().isEmpty()) {
                    ctx.status(400);
                    ctx.json(Map.of("error", "please enter customer name"));
                    return;
                }

                Order order = new Order(request.customerName);

                //add bowl
                for(BowlRequest bowlReq : request.bowls) {
                    PokeBowl pokeBowl = new PokeBowl(
                            bowlReq.name,
                            bowlReq.base,
                            bowlReq.size
                    );

                    //add toppings to bowl
                    for(String toppingName : bowlReq.toppings) {
                        Topping topping = toppingDAO.getToppingByName(toppingName);
                        if(topping != null) {
                            pokeBowl.addTopping(topping);
                        } else {
                            System.out.println("topping not found");
                        }
                    }
                    order.addItem(pokeBowl);
                }

                //add drink
                for(DrinkRequest drinkRequest : request.drinks) {
                    Drink drink = new Drink(drinkRequest.size, drinkRequest.flavor);
                    order.addItem(drink);
                }

                //add side
                for(SideRequest sideRequest : request.sides) {
                    Sides side = new Sides(sideRequest.type);
                    order.addItem(side);
                }

                //save to db
                int orderId = orderDAO.saveOrder(order);
                if(orderId > 0) {
                    Map<String, Object> resp = new HashMap<>();
                    resp.put("Success", true);
                    resp.put("orderId", orderId);
                    resp.put("customerName", order.getName());
                    resp.put("subtotal", order.getSubtotal());
                    resp.put("tax", order.getTax());
                    resp.put("total", order.getTotal());

                    ctx.status(201); //created status
                    ctx.json(resp);
                } else {
                    ctx.status(500);
                    ctx.json(Map.of("error", "failed to save"));
                }
            } catch (Exception e) {
                e.printStackTrace();
                ctx.status(500);
                ctx.json(Map.of("error", e.getMessage()));
                throw new RuntimeException(e);
            }
        });
    }

    private static int getPort() {
        String port = System.getenv("PORT");
        return port != null ? Integer.parseInt(port) : 8080;
    }

    public static class OrderRequest {
        public String customerName;
        public ArrayList<BowlRequest> bowls = new ArrayList<>();
        public ArrayList<DrinkRequest> drinks = new ArrayList<>();
        public ArrayList<SideRequest> sides = new ArrayList<>();
    }

    public static class BowlRequest {
        public String name;
        public String base;
        public String size;
        public ArrayList<String> toppings = new ArrayList<>();
    }

    public static class DrinkRequest {
        public String size;
        public String flavor;
    }

    public static class SideRequest {
        public String type;
    }
}