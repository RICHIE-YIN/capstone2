package com.richie.ui;

import com.richie.model.*;
import com.richie.util.ReceiptFileManager;

import java.util.ArrayList;
import java.util.Scanner;

public class CLI {

    public static void main(String[] args) {
        mainScreen();
    }

    static Scanner scanner = new Scanner(System.in);
    static ArrayList<Order> orders = new ArrayList<>();

    public static void mainScreen() {
        boolean picking = true;
        while(picking) {
            System.out.println("Welcome to Funkin' Poke!");
            System.out.println("Press 1 to order | Press 2 to quit or when you're finished");
            String answer = scanner.nextLine();

            if(answer.equalsIgnoreCase("1")) {
                System.out.println("What is the name for your order?");
                String orderName = scanner.nextLine();
                menuScreen(orderName);
            } else {
                picking = false;
                for(Order o : orders) {
                    ReceiptFileManager.previewReceipt(o);
                    ReceiptFileManager.saveReceipt(o);
                }
            }

        }
    }

    public static void menuScreen(String orderName) {
        Order order = new Order(orderName);
        boolean picking = true;

        while(picking) {
            System.out.println("What would you like to order?");
            System.out.println("Press 1 for Entree | Press 2 for Sides | Press 3 for Drinks | Press 0 if done");
            String answer = scanner.nextLine();
            if(answer.equalsIgnoreCase("1")) {
                PokeBowl pokeBowl = entreeScreen();
                order.addItem(pokeBowl);
            } else if (answer.equalsIgnoreCase("2")) {
                ArrayList<Sides> side = sidesScreen();
                for(Sides s : side) {
                    order.addItem(s);
                }
            } else if (answer.equalsIgnoreCase("3")) {
                ArrayList<Drink> drinks = drinksScreen();
                for(Drink d : drinks) {
                    order.addItem(d);
                }
            } else if (answer.equalsIgnoreCase("0")) {
                picking = false;
            }
        }
        orders.add(order);
    }

    public static PokeBowl entreeScreen() {
        ArrayList<Topping> pokeBowlToppings = new ArrayList<>();
        ArrayList<String> pokeBowlExtras = new ArrayList<>();

        System.out.println("Name your poke bowl:");
        String name = scanner.nextLine();
        System.out.println("What would you like for your base:");
        String base = scanner.nextLine();
        System.out.println("What size would you like? | S, M, or L");
        String sizeAnswer = scanner.nextLine();
        if(!(sizeAnswer.equalsIgnoreCase("s") || sizeAnswer.equalsIgnoreCase("m") || sizeAnswer.equalsIgnoreCase("l"))) {
            System.out.println("Invalid size, defaulting to Medium.");
            sizeAnswer = "M";
        }
        System.out.println("Would you like to add toppings?");
        String toppingsAnswer = scanner.nextLine();
        if(toppingsAnswer.equalsIgnoreCase("yes")) {
            pokeBowlToppings = toppingsScreen();
        }
        System.out.println("Would you like to add extras?");
        String extrasAnswer = scanner.nextLine();
        if(extrasAnswer.equalsIgnoreCase("yes")) {
            pokeBowlExtras = extrasScreen();
        }
        PokeBowl pokeBowl = new PokeBowl(name, base, sizeAnswer.toUpperCase());
        if(!pokeBowlToppings.isEmpty()) {
            for(Topping t : pokeBowlToppings) {
                pokeBowl.addTopping(t);
            }
        }
        if(!pokeBowlExtras.isEmpty()) {
            for(String s : pokeBowlExtras) {
                pokeBowl.addExtra(s);
            }
        }

        return pokeBowl;
    }

    public static ArrayList<Topping> toppingsScreen() {
        ArrayList<Topping> list = new ArrayList<>();
        ArrayList<Topping> toppingChoice = new ArrayList<>();
        list.add(new Topping("Salmon", 2.00, true));
        list.add(new Topping("Tuna", 2.25, true));
        list.add(new Topping("Spicy Tuna", 2.50, true));
        list.add(new Topping("Spicy Salmon", 2.50, true));
        list.add(new Topping("Shrimp", 1.75, true));
        list.add(new Topping("Tofu", 1.00, true));
        list.add(new Topping("Crab Mix", 1.25, true));
        list.add(new Topping("Avocado", 1.00, true));
        list.add(new Topping("Seaweed Salad", 0.75, false));
        list.add(new Topping("Cucumber", 0.25, false));
        list.add(new Topping("Mango", 0.50, false));
        list.add(new Topping("Green Onion", 0.25, false));
        list.add(new Topping("Masago", 0.75, false));
        list.add(new Topping("Pickled Ginger", 0.25, false));
        list.add(new Topping("Jalapeño", 0.25, false));
        list.add(new Topping("Nori", 0.10, false));
        list.add(new Topping("Spicy Mayo", 0.50, false));
        list.add(new Topping("Eel Sauce", 0.50, false));
        list.add(new Topping("Ponzu Sauce", 0.50, false));
        list.add(new Topping("Sesame Oil", 0.25, false));
        list.add(new Topping("Yuzu Dressing", 0.50, false));
        list.add(new Topping("Wasabi Aioli", 0.75, false));
        list.add(new Topping("Sriracha", 0.25, false));
        list.add(new Topping("Furikake", 0.25, false));
        list.add(new Topping("Crispy Onions", 0.50, false));
        list.add(new Topping("Tempura Flakes", 0.50, false));
        System.out.println("Topping choices:");
        for(Topping t : list) {
            System.out.printf("%s, price: %.2f\n", t.getName(), t.getPrice());
        }
        boolean picking = true;
        while(picking) {
            System.out.println("Please enter topping name to add | Enter 'done' when done.");
            String toppingChoiceAnswer = scanner.nextLine();
            if(toppingChoiceAnswer.equalsIgnoreCase("done")) {
                break;
            } else {
                boolean found = false;
                for(Topping t : list) {
                    if(toppingChoiceAnswer.equalsIgnoreCase(t.getName())) {
                        toppingChoice.add(t);
                        found = true;
                        break;
                    }
                }
                if(found == false) {
                    System.out.println("not found, please try again");
                }
            }
        }
        System.out.println("Here are your chosen toppings:");
        for(Topping t : toppingChoice) {
            System.out.println(t.getName());
        }
        return toppingChoice;
    }

    public static ArrayList<String> extrasScreen() {
        ArrayList<String> addedExtras = new ArrayList<>();
        boolean picking = true;
        while(picking) {
            System.out.println("Add in your extras here: | Enter 'done' when done");
            String ans = scanner.nextLine();
            if(ans.equalsIgnoreCase("done")) {
                break;
            } else {
                addedExtras.add(ans);
            }
        }
        return addedExtras;
    }

    public static ArrayList<Sides> sidesScreen() {
        ArrayList<Sides> list = new ArrayList<>();
        ArrayList<Sides> sidesChoice = new ArrayList<>();

        list.add(new Sides("Edamame"));
        list.add(new Sides("Seaweed Salad"));
        list.add(new Sides("Miso Soup"));
        list.add(new Sides("Cucumber Salad"));
        list.add(new Sides("Kimchi"));
        list.add(new Sides("Crab Salad"));
        list.add(new Sides("Spicy Tuna Mix"));
        list.add(new Sides("Spicy Crab Mix"));
        list.add(new Sides("Salmon Poke Scoop"));
        list.add(new Sides("Tuna Poke Scoop"));
        list.add(new Sides("Gyoza"));
        list.add(new Sides("Takoyaki"));
        list.add(new Sides("Spring Rolls"));
        list.add(new Sides("Crispy Tofu Bites"));
        list.add(new Sides("Tamago Slices"));
        list.add(new Sides("Mini Rice Bowl"));
        list.add(new Sides("Imitation Crab"));
        list.add(new Sides("Mango Slices"));
        list.add(new Sides("Pineapple Cubes"));
        list.add(new Sides("Wasabi Peas"));

        System.out.println("Sides choices:");
        for(Sides s : list) {
            System.out.printf("%s, price: %.2f\n", s.getName(), s.getPrice());
        }
        boolean picking = true;
        while(picking) {
            System.out.println("What sides would you like? | Type 'done' when finished");
            String sidesAnswer = scanner.nextLine();
            if(sidesAnswer.equalsIgnoreCase("done")) {
                break;
            } else {
                boolean found = false;
                for(Sides s : list) {
                    if(sidesAnswer.equalsIgnoreCase(s.getName())) {
                        sidesChoice.add(s);
                        found = true;
                        break;
                    }
                }
                if(found == false) {
                    System.out.println("Side not found, try again.");
                }
            }
        }
        if(!sidesChoice.isEmpty()) {
            System.out.println("Here are your side choices:");
            for(Sides s : sidesChoice) {
                System.out.println(s.getName());
            }
        }
        return sidesChoice;
    }

    public static ArrayList<Drink> drinksScreen() {
        ArrayList<Drink> list = new ArrayList<>();
        ArrayList<Drink> drinksChoice = new ArrayList<>();
        list.add(new Drink("N/A", "Green Tea"));
        list.add(new Drink("N/A", "Mango Iced Tea"));
        list.add(new Drink("N/A", "Lychee Lemonade"));
        list.add(new Drink("N/A", "Yuzu Soda"));
        list.add(new Drink("N/A", "Coconut Water"));
        list.add(new Drink("N/A", "Passion Fruit Juice"));
        list.add(new Drink("N/A", "Matcha Milk Tea"));
        list.add(new Drink("N/A", "Thai Iced Tea"));
        list.add(new Drink("N/A", "Pineapple Refresher"));
        list.add(new Drink("N/A", "Watermelon Refresher"));
        list.add(new Drink("N/A", "Strawberry Lemonade"));
        list.add(new Drink("N/A", "Peach Green Tea"));
        list.add(new Drink("N/A", "Honeydew Juice"));
        list.add(new Drink("N/A", "Brown Sugar Milk Tea"));
        list.add(new Drink("N/A", "Taro Milk Tea"));
        list.add(new Drink("N/A", "Black Milk Tea"));
        list.add(new Drink("N/A", "Jasmine Milk Tea"));
        list.add(new Drink("N/A", "Rose Milk Tea"));
        list.add(new Drink("N/A", "Lavender Lemonade"));
        list.add(new Drink("N/A", "Sparkling Lychee Water"));

        System.out.println("List of flavors:");
        for(Drink d : list) {
            System.out.println(d.getFlavor());
        }

        boolean picking = true;
        while(picking) {
            System.out.println("What flavor would you like? | type 'done' when finished");
            String flavorAnswer = scanner.nextLine();
            if(flavorAnswer.equalsIgnoreCase("done")) {
                break;
            } else {
                boolean found = false;
                for(Drink d : list) {
                    if(flavorAnswer.equalsIgnoreCase(d.getFlavor())) {
                        System.out.println("What size would you like? | S, M, or L");
                        String sizeAnswer = scanner.nextLine();
                        if(sizeAnswer.equalsIgnoreCase("s") || sizeAnswer.equalsIgnoreCase("m") || sizeAnswer.equalsIgnoreCase("l")) {
                            Drink drink = new Drink(sizeAnswer.toUpperCase(), flavorAnswer);
                            drinksChoice.add(drink);
                        } else {
                            System.out.println("Invalid size, please try again");
                        }
                        found = true;
                    }
                }
                if(found == false) {
                    System.out.println("Flavor not found, please try again.");
                }
            }
        }
        if(!drinksChoice.isEmpty()) {
            System.out.println("Your drink choice and size:");
            for(Drink d : drinksChoice) {
                System.out.printf("flavor: %s size: %s\n", d.getFlavor(), d.getSize());
            }
        }
        return drinksChoice;
    }
}