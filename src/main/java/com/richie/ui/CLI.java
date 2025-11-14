package com.richie.ui;

import com.richie.model.*;
import com.richie.util.OrderValidator;
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
        while (picking) {
            System.out.println("Welcome to The Poke Spot!");
            System.out.println("Press 1 to order | Press 2 to quit or when you're finished");
            String answer = scanner.nextLine();

            if (answer.equalsIgnoreCase("1")) {
                System.out.println("What is the name for your order?");
                String orderName = scanner.nextLine();
                menuScreen(orderName);
            } else {
                picking = false;
                for (Order o : orders) {
                    ReceiptFileManager.previewReceipt(o);
                    ReceiptFileManager.saveReceipt(o);
                }
            }
        }
    }

    public static void menuScreen(String orderName) {
        Order order = new Order(orderName);
        boolean picking = true;

        while (picking) {
            System.out.println("\nWhat would you like to order?");
            System.out.println("1 = Entree");
            System.out.println("2 = Sides");
            System.out.println("3 = Drinks");
            System.out.println("0 = Finish Order");
            System.out.println("9 = Cancel Order");

            String answer = scanner.nextLine().trim();

            switch (answer) {
                case "1":
                    PokeBowl pokeBowl = entreeScreen();
                    order.addItem(pokeBowl);
                    break;

                case "2":
                    for (Sides s : sidesScreen()) {
                        order.addItem(s);
                    }
                    break;

                case "3":
                    for (Drink d : drinksScreen()) {
                        order.addItem(d);
                    }
                    break;

                case "0":
                    picking = false;
                    break;

                case "9":
                    System.out.println("\norder cancelled. returning to main menu");
                    return;
            }
        }

        if (!OrderValidator.isValid(order)) {
            System.out.println("\ninvalid order. must order at least a bowl, drink, or side.");
            System.out.println("Order not saved.\n");
            return;
        }

        orders.add(order);
        System.out.println("\norder saved for: " + orderName + "\n");
    }


    public static PokeBowl entreeScreen() {
        System.out.println("What kind of bowl would you like?");
        System.out.println("press 1 for Signature Bowl (10% off) | press 2 for Custom Bowl");
        String bowlTypeAnswer = scanner.nextLine();

        if (bowlTypeAnswer.equalsIgnoreCase("1")) {
            return signatureBowlScreen();
        }

        ArrayList<Topping> pokeBowlToppings = new ArrayList<>();
        ArrayList<Extra> pokeBowlExtras = new ArrayList<>();

        System.out.println("Name your poke bowl:");
        String name = scanner.nextLine();
        System.out.println("What would you like for your base:");
        String base = scanner.nextLine();
        System.out.println("What size would you like? | S, M, or L");
        String sizeAnswer = scanner.nextLine();
        if (!(sizeAnswer.equalsIgnoreCase("s") ||
                sizeAnswer.equalsIgnoreCase("m") ||
                sizeAnswer.equalsIgnoreCase("l"))) {
            System.out.println("Invalid size, defaulting to Medium.");
            sizeAnswer = "M";
        }

        System.out.println("Would you like to add toppings?");
        String toppingsAnswer = scanner.nextLine();
        if (toppingsAnswer.equalsIgnoreCase("yes")) {
            pokeBowlToppings = toppingsScreen();
        }

        System.out.println("Would you like to add extras?");
        String extrasAnswer = scanner.nextLine();
        if (extrasAnswer.equalsIgnoreCase("yes")) {
            pokeBowlExtras = extrasScreen(pokeBowlToppings);
        }

        PokeBowl pokeBowl = new PokeBowl(name, base, sizeAnswer.toUpperCase());

        if (!pokeBowlToppings.isEmpty()) {
            for (Topping t : pokeBowlToppings) {
                pokeBowl.addTopping(t);
            }
        }

        if (!pokeBowlExtras.isEmpty()) {
            for (Extra e : pokeBowlExtras) {
                pokeBowl.addExtra(e);
            }
        }

        return pokeBowl;
    }

    public static PokeBowl signatureBowlScreen() {
        // Build toppings library just for signatures
        Topping salmon = new Topping("Salmon", 2.00, true);
        Topping tuna = new Topping("Tuna", 2.25, true);
        Topping spicyTuna = new Topping("Spicy Tuna", 2.50, true);
        Topping spicySalmon = new Topping("Spicy Salmon", 2.50, true);
        Topping shrimp = new Topping("Shrimp", 1.75, true);
        Topping tofu = new Topping("Tofu", 1.00, true);
        Topping crabMix = new Topping("Crab Mix", 1.25, true);
        Topping avocado = new Topping("Avocado", 1.00, true);
        Topping seaweedSalad = new Topping("Seaweed Salad", 0.75, false);
        Topping cucumber = new Topping("Cucumber", 0.25, false);
        Topping mango = new Topping("Mango", 0.50, false);
        Topping greenOnion = new Topping("Green Onion", 0.25, false);
        Topping masago = new Topping("Masago", 0.75, false);
        Topping pickledGinger = new Topping("Pickled Ginger", 0.25, false);
        Topping jalapeno = new Topping("Jalapeño", 0.25, false);
        Topping nori = new Topping("Nori", 0.10, false);
        Topping spicyMayo = new Topping("Spicy Mayo", 0.50, false);
        Topping eelSauce = new Topping("Eel Sauce", 0.50, false);
        Topping ponzuSauce = new Topping("Ponzu Sauce", 0.50, false);
        Topping sesameOil = new Topping("Sesame Oil", 0.25, false);
        Topping yuzuDressing = new Topping("Yuzu Dressing", 0.50, false);
        Topping wasabiAioli = new Topping("Wasabi Aioli", 0.75, false);
        Topping sriracha = new Topping("Sriracha", 0.25, false);
        Topping furikake = new Topping("Furikake", 0.25, false);
        Topping crispyOnions = new Topping("Crispy Onions", 0.50, false);
        Topping tempuraFlakes = new Topping("Tempura Flakes", 0.50, false);

        System.out.println("\nSignature Bowls (10% off):");
        System.out.println("1) Richie Special  - L | White Rice");
        System.out.println("   Spicy Tuna, Avocado, Seaweed Salad, Cucumber, Green Onion, Masago,");
        System.out.println("   Spicy Mayo, Eel Sauce, Furikake, Crispy Onions, Tempura Flakes");
        System.out.println();
        System.out.println("2) Hawaiian Classic - M | White Rice");
        System.out.println("   Salmon, Crab Mix, Avocado, Cucumber, Mango, Green Onion,");
        System.out.println("   Sesame Oil, Ponzu Sauce, Furikake");
        System.out.println();
        System.out.println("3) Spicy Volcano - M | Brown Rice");
        System.out.println("   Spicy Salmon, Shrimp, Jalapeño, Masago, Green Onion,");
        System.out.println("   Sriracha, Spicy Mayo, Tempura Flakes");
        System.out.println();
        System.out.println("4) Veggie Zen - M | Mixed Greens");
        System.out.println("   Tofu, Avocado, Cucumber, Seaweed Salad, Pickled Ginger,");
        System.out.println("   Sesame Oil, Ponzu Sauce, Furikake, Crispy Onions");
        System.out.println();

        System.out.println("Pick a Signature Bowl (1-4):");
        String answer = scanner.nextLine();

        SignaturePokeBowl bowl;

        switch (answer) {
            case "1":
                bowl = new SignaturePokeBowl("Richie Special", "White Rice", "L", "Richie Special");
                bowl.addTopping(spicyTuna);
                bowl.addTopping(avocado);
                bowl.addTopping(seaweedSalad);
                bowl.addTopping(cucumber);
                bowl.addTopping(greenOnion);
                bowl.addTopping(masago);
                bowl.addTopping(spicyMayo);
                bowl.addTopping(eelSauce);
                bowl.addTopping(furikake);
                bowl.addTopping(crispyOnions);
                bowl.addTopping(tempuraFlakes);
                break;
            case "2":
                bowl = new SignaturePokeBowl("Hawaiian Classic", "White Rice", "M", "Hawaiian Classic");
                bowl.addTopping(salmon);
                bowl.addTopping(crabMix);
                bowl.addTopping(avocado);
                bowl.addTopping(cucumber);
                bowl.addTopping(mango);
                bowl.addTopping(greenOnion);
                bowl.addTopping(sesameOil);
                bowl.addTopping(ponzuSauce);
                bowl.addTopping(furikake);
                break;
            case "3":
                bowl = new SignaturePokeBowl("Spicy Volcano", "Brown Rice", "M", "Spicy Volcano");
                bowl.addTopping(spicySalmon);
                bowl.addTopping(shrimp);
                bowl.addTopping(jalapeno);
                bowl.addTopping(masago);
                bowl.addTopping(greenOnion);
                bowl.addTopping(sriracha);
                bowl.addTopping(spicyMayo);
                bowl.addTopping(tempuraFlakes);
                break;
            case "4":
                bowl = new SignaturePokeBowl("Veggie Zen", "Mixed Greens", "M", "Veggie Zen");
                bowl.addTopping(tofu);
                bowl.addTopping(avocado);
                bowl.addTopping(cucumber);
                bowl.addTopping(seaweedSalad);
                bowl.addTopping(pickledGinger);
                bowl.addTopping(sesameOil);
                bowl.addTopping(ponzuSauce);
                bowl.addTopping(furikake);
                bowl.addTopping(crispyOnions);
                break;
            default:
                System.out.println("Invalid choice. Defaulting to Richie Special.");
                bowl = new SignaturePokeBowl("Richie Special", "White Rice", "L", "Richie Special");
                bowl.addTopping(spicyTuna);
                bowl.addTopping(avocado);
                bowl.addTopping(seaweedSalad);
                bowl.addTopping(cucumber);
                bowl.addTopping(greenOnion);
                bowl.addTopping(masago);
                bowl.addTopping(spicyMayo);
                bowl.addTopping(eelSauce);
                bowl.addTopping(furikake);
                bowl.addTopping(crispyOnions);
                bowl.addTopping(tempuraFlakes);
                break;
        }

        System.out.println("\nWould you like to add extras to this Signature bowl? (yes/no)");
        String extrasAnswer = scanner.nextLine();
        if (extrasAnswer.equalsIgnoreCase("yes")) {
            ArrayList<Extra> extras = extrasScreen(bowl.getToppings());
            for (Extra e : extras) {
                bowl.addExtra(e);
            }
        }

        System.out.println("You chose: " + bowl.getName() + " (Signature, 10% off)");
        return bowl;
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

    public static ArrayList<Extra> extrasScreen(ArrayList<Topping> selectedToppings) {
        ArrayList<Extra> extraList = new ArrayList<>();

        if(selectedToppings == null || selectedToppings.isEmpty()) {
            System.out.println("No toppings selected, skipping extras.");
            return extraList;
        }

        boolean hasPremium = false;
        Topping firstPremium = null;

        for(Topping t : selectedToppings) {
            if(t.isPremium()) {
                hasPremium = true;
                if(firstPremium == null) {
                    firstPremium = t;
                }
            }
        }

        if(hasPremium) {
            System.out.println("Would you like extra meat (double protein) for +$2? | type yes or no");
            String extraMeatAnswer = scanner.nextLine();
            if(extraMeatAnswer.equalsIgnoreCase("yes")) {
                Extra extraProtein = new Extra(firstPremium);
                extraList.add(extraProtein);
                System.out.printf("Added extra protein for $%.2f\n", extraProtein.getUpcharge());
            }
        }

        System.out.println("Would you like extra toppings? (yes/no)");
        String extraToppingsAnswer = scanner.nextLine();
        if(extraToppingsAnswer.equalsIgnoreCase("yes")) {

            System.out.println("Your available toppings for extras:");
            for(Topping t : selectedToppings) {
                Extra tempExtra = new Extra(t);
                System.out.printf("%s (+$%.2f)\n", t.getName(), tempExtra.getUpcharge());
            }

            boolean picking = true;
            while(picking) {
                System.out.println("Enter topping name for an extra portion | Type 'done' when finished");
                String answer = scanner.nextLine();

                if(answer.equalsIgnoreCase("done")) {
                    break;
                } else {
                    boolean found = false;
                    for(Topping t : selectedToppings) {
                        if(answer.equalsIgnoreCase(t.getName())) {
                            Extra extra = new Extra(t);
                            extraList.add(extra);
                            System.out.printf("✓ Added extra %s (+$%.2f)\n",
                                    t.getName(), extra.getUpcharge());
                            found = true;
                            break;
                        }
                    }
                    if(found == false) {
                        System.out.println("Not found, please try again.");
                    }
                }
            }
        }

        if(!extraList.isEmpty()) {
            System.out.println("\nHere are your extras:");
            for(Extra e : extraList) {
                System.out.println(e.getName());
            }
        }

        return extraList;
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