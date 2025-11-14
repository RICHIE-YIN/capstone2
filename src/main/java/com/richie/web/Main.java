package com.richie.web;

import com.richie.util.OrderValidator;
import com.richie.util.ReceiptFileManager;
import io.javalin.Javalin;
import com.richie.dao.*;
import com.richie.model.*;
import io.javalin.http.staticfiles.Location;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Main {

    private static ToppingDAO toppingDAO = new ToppingDAO();
    private static SidesDAO sidesDAO = new SidesDAO();
    private static DrinksDAO drinksDAO = new DrinksDAO();
    private static ExtraDAO extraDAO = new ExtraDAO();
    private static OrderDAO orderDAO = new OrderDAO();

    public static void main(String[] args) {
        int port = getPort();

        Javalin app = Javalin.create(config -> {
            config.staticFiles.add(staticFiles -> {
                staticFiles.hostedPath = "/";       // served from root
                staticFiles.directory = "public";   // folder *next to* pom.xml
                staticFiles.location = Location.EXTERNAL;
            });
        }).start(port);
        app.get("/", ctx -> ctx.redirect("/index.html"));


        app.get("/api/health", ctx -> ctx.json(Map.of("status", "healthy")));

        app.get("/api/toppings", ctx -> {
            try {
                ArrayList<Topping> toppings = toppingDAO.getAllToppings();
                ctx.json(toppings);
            } catch (Exception e) {
                ctx.status(400).json(Map.of("error", "failed to load toppings"));
                throw new RuntimeException(e);
            }
        });

        app.get("/api/sides", ctx -> {
            try {
                ArrayList<Sides> sides = sidesDAO.getAllSides();
                ctx.json(sides);
            } catch (Exception e) {
                ctx.status(400).json(Map.of("error", "failed to load sides"));
                throw new RuntimeException(e);
            }
        });

        app.get("/api/drinks", ctx -> {
            try {
                ArrayList<Drink> drinks = drinksDAO.getAllDrinks();
                ctx.json(drinks);
            } catch (Exception e) {
                ctx.status(400).json(Map.of("error", "failed to load drinks"));
                throw new RuntimeException(e);
            }
        });

        app.get("/api/signature-bowls", ctx -> {
            ArrayList<Map<String, Object>> signature = new ArrayList<>();

            signature.add(Map.of(
                    "name", "Richie Special",
                    "base", "White Rice",
                    "size", "L",
                    "toppings", new String[]{
                            "Spicy Tuna", "Avocado", "Seaweed Salad", "Cucumber",
                            "Green Onion", "Masago", "Spicy Mayo", "Eel Sauce",
                            "Furikake", "Crispy Onions", "Tempura Flakes"
                    },
                    "description", "Richie’s go-to overloaded bowl with max crunch and spice. Signature 10% off."
            ));

            signature.add(Map.of(
                    "name", "Hawaiian Classic",
                    "base", "White Rice",
                    "size", "M",
                    "toppings", new String[]{
                            "Salmon", "Crab Mix", "Avocado", "Cucumber",
                            "Mango", "Green Onion", "Sesame Oil", "Ponzu Sauce", "Furikake"
                    },
                    "description", "OG island vibes with salmon, crab mix, and mango."
            ));

            signature.add(Map.of(
                    "name", "Spicy Volcano",
                    "base", "Brown Rice",
                    "size", "M",
                    "toppings", new String[]{
                            "Spicy Salmon", "Shrimp", "Jalapeño", "Masago",
                            "Green Onion", "Sriracha", "Spicy Mayo", "Tempura Flakes"
                    },
                    "description", "Heat + texture. For the spice demons."
            ));

            signature.add(Map.of(
                    "name", "Veggie Zen",
                    "base", "Mixed Greens",
                    "size", "M",
                    "toppings", new String[]{
                            "Tofu", "Avocado", "Cucumber", "Seaweed Salad",
                            "Pickled Ginger", "Sesame Oil", "Ponzu Sauce",
                            "Furikake", "Crispy Onions"
                    },
                    "description", "Fully veggie, still fire. Light but satisfying."
            ));

            ctx.json(signature);
        });

        app.post("/api/orders", ctx -> {
            try {
                OrderRequest request = ctx.bodyAsClass(OrderRequest.class);

                if (request.customerName == null || request.customerName.trim().isEmpty()) {
                    ctx.status(400).json(Map.of("error", "please enter customer name"));
                    return;
                }

                Order order = new Order(request.customerName);

                // bowls (signature OR custom)
                for (BowlRequest bowlReq : request.bowls) {
                    PokeBowl pokeBowl;

                    if (bowlReq.signature) {
                        // signature bowl
                        SignaturePokeBowl sig = buildSignatureBowl(bowlReq.name);
                        if (sig == null) {
                            ctx.status(400).json(Map.of(
                                    "success", false,
                                    "error", "Unknown signature bowl: " + bowlReq.name
                            ));
                            return;
                        }
                        pokeBowl = sig;
                    } else {
                        // custom bowl
                        pokeBowl = new PokeBowl(
                                bowlReq.name,
                                bowlReq.base,
                                bowlReq.size
                        );

                        // custom toppings
                        addToppings(pokeBowl, bowlReq.toppings.toArray(new String[0]));
                    }

                    // extras
                    for (ExtraRequest extraReq : bowlReq.extras) {
                        Topping topping = toppingDAO.getToppingByName(extraReq.toppingName);
                        if (topping != null) {
                            Extra extra = new Extra(topping);
                            pokeBowl.addExtra(extra);
                            System.out.println("Extra added: " + extra.getName());
                        } else {
                            System.out.println("topping not found for extra: " + extraReq.toppingName);
                        }
                    }

                    order.addItem(pokeBowl);
                }

                // drinks
                for (DrinkRequest drinkRequest : request.drinks) {
                    Drink drink = new Drink(drinkRequest.size, drinkRequest.flavor);
                    order.addItem(drink);
                }

                // sides
                for (SideRequest sideRequest : request.sides) {
                    Sides side = new Sides(sideRequest.type);
                    order.addItem(side);
                }

                // validate
                if (!OrderValidator.isValid(order)) {
                    ctx.status(400).json(Map.of(
                            "success", false,
                            "error", "Invalid order. Must include at least a bowl, drink, or side."
                    ));
                    return;
                }

                int orderId = orderDAO.saveOrder(order);
                if (orderId > 0) {

                    String receiptFileName = ReceiptFileManager.saveReceipt(order);

                    Map<String, Object> resp = new HashMap<>();
                    resp.put("success", true);
                    resp.put("orderId", orderId);
                    resp.put("orderNumber", order.getOrderNumber());
                    resp.put("customerName", order.getName());
                    resp.put("subtotal", order.getSubtotal());
                    resp.put("tax", order.getTax());
                    resp.put("total", order.getTotal());
                    resp.put("receipt", formatReceipt(order));
                    resp.put("receiptFileName", receiptFileName);

                    ctx.status(201).json(resp);
                } else {
                    ctx.status(500).json(Map.of("error", "failed to save"));
                }

            } catch (Exception e) {
                e.printStackTrace();
                ctx.status(500).json(Map.of("error", e.getMessage()));
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
        public boolean signature;
        public ArrayList<String> toppings = new ArrayList<>();
        public ArrayList<ExtraRequest> extras = new ArrayList<>();
    }

    public static class ExtraRequest {
        public String toppingName;
    }

    public static class DrinkRequest {
        public String size;
        public String flavor;
    }

    public static class SideRequest {
        public String type;
    }

    private static String formatReceipt(Order order) {
        StringBuilder receipt = new StringBuilder();

        receipt.append("======================================\n");
        receipt.append("         THE POKE SPOT RECEIPT        \n");
        receipt.append("======================================\n");
        receipt.append(String.format("Order #: %s\n", order.getOrderNumber()));
        receipt.append(String.format("Customer: %s\n", order.getName()));
        receipt.append("======================================\n\n");

        for (Product p : order.getProducts()) {
            receipt.append(String.format("Item: %s\nPrice: $%.2f\n", p.getName(), p.getPrice()));

            if (p instanceof PokeBowl) {
                PokeBowl pokeBowl = (PokeBowl) p;
                receipt.append(String.format(" Base: %s | Size: %s\n", pokeBowl.getBase(), pokeBowl.getSize()));

                if (!pokeBowl.getToppings().isEmpty()) {
                    receipt.append("  Toppings:\n");
                    for (Topping t : pokeBowl.getToppings()) {
                        receipt.append(String.format("   - %s\n", t.getName()));
                    }
                }

                if (pokeBowl.hasExtras()) {
                    receipt.append("  Extras:\n");
                    for (Extra e : pokeBowl.getExtras()) {
                        receipt.append(String.format("   - %s: +$%.2f\n", e.getName(), e.getUpcharge()));
                    }
                }

            } else if (p instanceof Drink) {
                Drink d = (Drink) p;
                receipt.append(String.format(" Size: %s | Flavor: %s\n", d.getSize(), d.getFlavor()));

            } else if (p instanceof Sides) {
                Sides s = (Sides) p;
                receipt.append(String.format(" Type: %s\n", s.getType()));
            }

            receipt.append("\n");
        }

        receipt.append("======================================\n");
        receipt.append(String.format("Subtotal:                 $%.2f\n", order.getSubtotal()));
        receipt.append(String.format("Tax (7%%):                 $%.2f\n", order.getTax()));
        receipt.append("--------------------------------------\n");
        receipt.append(String.format("TOTAL:                    $%.2f\n", order.getTotal()));
        receipt.append("======================================\n");

        return receipt.toString();
    }

    private static SignaturePokeBowl buildSignatureBowl(String requestedName) {
        if (requestedName == null) {
            return null;
        }

        String key = requestedName.trim().toLowerCase();
        SignaturePokeBowl bowl;

        switch (key) {
            case "richie special":
                bowl = new SignaturePokeBowl("Richie Special", "White Rice", "L", "Richie Special");
                addToppings(bowl,
                        "Spicy Tuna",
                        "Avocado",
                        "Seaweed Salad",
                        "Cucumber",
                        "Green Onion",
                        "Masago",
                        "Spicy Mayo",
                        "Eel Sauce",
                        "Furikake",
                        "Crispy Onions",
                        "Tempura Flakes"
                );
                break;

            case "hawaiian classic":
                bowl = new SignaturePokeBowl("Hawaiian Classic", "White Rice", "M", "Hawaiian Classic");
                addToppings(bowl,
                        "Salmon",
                        "Crab Mix",
                        "Avocado",
                        "Cucumber",
                        "Mango",
                        "Green Onion",
                        "Sesame Oil",
                        "Ponzu Sauce",
                        "Furikake"
                );
                break;

            case "spicy volcano":
                bowl = new SignaturePokeBowl("Spicy Volcano", "Brown Rice", "M", "Spicy Volcano");
                addToppings(bowl,
                        "Spicy Salmon",
                        "Shrimp",
                        "Jalapeño",
                        "Masago",
                        "Green Onion",
                        "Sriracha",
                        "Spicy Mayo",
                        "Tempura Flakes"
                );
                break;

            case "veggie zen":
                bowl = new SignaturePokeBowl("Veggie Zen", "Mixed Greens", "M", "Veggie Zen");
                addToppings(bowl,
                        "Tofu",
                        "Avocado",
                        "Cucumber",
                        "Seaweed Salad",
                        "Pickled Ginger",
                        "Sesame Oil",
                        "Ponzu Sauce",
                        "Furikake",
                        "Crispy Onions"
                );
                break;

            default:
                return null;
        }

        System.out.println("Built signature bowl: " + bowl.getName());
        return bowl;
    }

    // Generic helper so it works for both PokeBowl & SignaturePokeBowl
    private static void addToppings(PokeBowl bowl, String... names) {
        for (String name : names) {
            Topping t = toppingDAO.getToppingByName(name);
            if (t != null) {
                bowl.addTopping(t);
            } else {
                System.out.println("topping not found in db: " + name);
            }
        }
    }
}
