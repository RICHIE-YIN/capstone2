# 🎓 RICHIE'S CAPSTONE 2 PREP GUIDE
**Workbooks 4 & 5 → Build Your Deli App**

*Focused, practical, Capstone-ready.*

---

# 📋 HOW TO USE THIS GUIDE

**You finished Workbook 3, so you know:**
- Variables, loops, conditionals ✓
- File I/O, collections ✓
- Basic programming ✓

**This guide teaches:** Object-oriented thinking for your deli app.

**Pattern per phase:**
1. 📖 Concept (why)
2. 🔍 Example (how)
3. 💡 YOUR TURN (build it)
4. ✅ Self-Check

**Rule:** Type all code yourself. No copy-paste.

---

# 🧱 PHASE 1 – CLASSES & OBJECTS

## 📖 CONCEPT

**Current approach (procedural chaos):**
```java
String s1Bread = "White", s2Bread = "Wheat";
String s1Size = "8\"", s2Size = "4\"";
double s1Price = 7.0, s2Price = 5.5;
// Scale to 10 sandwiches = nightmare
```

**OOP approach:**
```java
Sandwich s1 = new Sandwich("White", "8\"");
Sandwich s2 = new Sandwich("Wheat", "4\"");
```

**Key:** Class = blueprint, Object = thing made from blueprint.

> 💡 **CAPSTONE CONNECTION:** Every item in your deli (Sandwich, Drink, Chips) will be a class!

---

## 🔍 EXAMPLE: Topping Class

```java
package com.richie.model;

public class Topping {
    // PRIVATE fields (encapsulation)
    private String name;
    private double basePrice;
    private boolean isPremium;
    
    // CONSTRUCTOR - initializes object
    public Topping(String name, double basePrice, boolean isPremium) {
        this.name = name;           // "this" = this object's field
        this.basePrice = basePrice;
        this.isPremium = isPremium;
    }
    
    // GETTER - controlled access
    public String getName() {
        return name;
    }
    
    // DERIVED GETTER - calculates value
    public double getPrice() {
        if (isPremium) {
            return basePrice * 1.5;
        } else {
            return basePrice;
        }
    }
    
    public boolean isPremium() {
        return isPremium;
    }
}
```

**Using it:**
```java
Topping bacon = new Topping("Bacon", 1.00, true);
System.out.println(bacon.getName() + ": $" + bacon.getPrice()); // Bacon: $1.50
```

---

## 💡 YOUR TURN: Chips Class

Build `Chips` with:
- `type` field (String)
- Constructor that takes type
- `getType()` getter
- `getPrice()` that returns 1.50

**Hint:** All chips cost the same, so no calculation needed!

---

# 🧱 PHASE 2 – STATIC VS INSTANCE

## 📖 CONCEPT

**Instance** = each object has own copy
**Static** = all objects share one copy

**When to use static:**
- Shared data (counter, tax rate)
- Utility methods (don't need object data)

> 💡 **CAPSTONE CONNECTION:** Your TAX_RATE and PriceFormatter will be static!

---

## 🔍 EXAMPLE: Static Counter & Utility

```java
public class Topping {
    private static int totalCreated = 0;  // SHARED
    
    private String name;  // UNIQUE per topping
    
    public Topping(String name, double basePrice, boolean isPremium) {
        this.name = name;
        this.basePrice = basePrice;
        this.isPremium = isPremium;
        totalCreated++;  // Increment shared counter
    }
    
    public static int getTotalCreated() {
        return totalCreated;
    }
}

// Usage
Topping.getTotalCreated();  // Class.method()
```

**PriceFormatter utility:**
```java
package com.richie.util;

public class PriceFormatter {
    private PriceFormatter() {}  // Prevent instantiation
    
    public static String format(double price) {
        return String.format("$%.2f", price);
    }
    
    public static double applyDiscount(double price, double percentOff) {
        return price * (1 - percentOff / 100);
    }
}

// Usage
PriceFormatter.format(12.5);  // $12.50
```

---

## 💡 YOUR TURN

Build PriceFormatter utility class and test both methods.

---

# 🧱 PHASE 3 – METHOD OVERLOADING

## 📖 CONCEPT

Same name, different parameters = overloading.

**Why:** Flexibility without different method names.

---

## 🔍 EXAMPLE

```java
public class Drink {
    private String size, flavor;
    
    // Main constructor
    public Drink(String size, String flavor) {
        this.size = size;
        this.flavor = flavor;
    }
    
    // Convenience constructor (uses this() to chain)
    public Drink(String size) {
        this(size, "Water");  // Calls 2-param constructor
    }
}

// Usage
Drink d1 = new Drink("large", "Lemonade");
Drink d2 = new Drink("medium");  // Defaults to Water
```

> 💡 **CAPSTONE CONNECTION:** Let customers quickly order items with default options!

---

## 💡 YOUR TURN

Add overloaded constructor to Sandwich that defaults bread to "White".

---

# 🧱 PHASE 4 – COMPOSITION (HAS-A)

## 📖 CONCEPT

Objects contain other objects.
- Order HAS Products
- Sandwich HAS Toppings

**Critical:** Initialize ArrayList or get NullPointerException!

> 💡 **CAPSTONE CONNECTION:** This is THE core pattern of your entire deli app!

---

## 🎨 VISUAL: Deli Object Relationships

```
Order
├── Sandwich 1
│   ├── Topping: Lettuce
│   ├── Topping: Bacon
│   └── Topping: Cheese
├── Sandwich 2
│   └── Topping: Turkey
├── Drink (Lemonade)
└── Chips (BBQ)
```

---

## 🔍 EXAMPLE: Sandwich Contains Toppings

```java
public class Sandwich {
    private String breadType, size;
    private ArrayList<Topping> toppings;
    
    public Sandwich(String breadType, String size) {
        this.breadType = breadType;
        this.size = size;
        this.toppings = new ArrayList<>();  // MUST initialize!
    }
    
    public void addTopping(Topping t) {
        toppings.add(t);
    }
    
    public double getPrice() {
        double total = 0;
        
        // Base price by size
        if (size.equals("4\"")) {
            total = 5.50;
        } else if (size.equals("8\"")) {
            total = 7.00;
        } else if (size.equals("12\"")) {
            total = 8.50;
        }
        
        // Add topping prices
        for (Topping t : toppings) {
            total += t.getPrice();  // Sandwich delegates to Topping
        }
        
        return total;
    }
    
    public ArrayList<Topping> getToppings() { return toppings; }
    public String getBreadType() { return breadType; }
    public String getSize() { return size; }
}
```

---

## 🔍 SEARCHING NESTED COLLECTIONS (CRITICAL FOR CAPSTONE)

```java
public class Order {
    private ArrayList<Sandwich> sandwiches;
    
    /**
     * Find sandwich containing specific topping.
     * Nested loop: Order → Sandwich → Topping
     */
    public Sandwich findSandwichWithTopping(String toppingName) {
        for (Sandwich s : sandwiches) {
            for (Topping t : s.getToppings()) {
                if (t.getName().equalsIgnoreCase(toppingName)) {
                    return s;
                }
            }
        }
        return null;  // Not found
    }
    
    /**
     * Get all sandwiches over a price.
     * Returns NEW list with filtered results.
     */
    public ArrayList<Sandwich> getSandwichesOverPrice(double maxPrice) {
        ArrayList<Sandwich> results = new ArrayList<>();
        for (Sandwich s : sandwiches) {
            if (s.getPrice() > maxPrice) {
                results.add(s);
            }
        }
        return results;
    }
    
    /**
     * Remove topping from ALL sandwiches.
     * Modifies existing sandwiches.
     */
    public void removeToppingFromAll(String toppingName) {
        for (Sandwich s : sandwiches) {
            ArrayList<Topping> toRemove = new ArrayList<>();
            
            // Find toppings to remove
            for (Topping t : s.getToppings()) {
                if (t.getName().equalsIgnoreCase(toppingName)) {
                    toRemove.add(t);
                }
            }
            
            // Remove them (can't remove during iteration!)
            for (Topping t : toRemove) {
                s.getToppings().remove(t);
            }
        }
    }
}
```

**Why this matters:** Your Capstone will need search/filter features.

---

## 💡 YOUR TURN: Build Order Class

```java
public class Order {
    private ArrayList<Product> items;  // Will use Product later
    
    public Order() {
        // TODO: Initialize items!
    }
    
    public void addItem(Product p) {
        // TODO: Add to items
    }
    
    public double getSubtotal() {
        // TODO: Sum all item prices
        return 0;
    }
}
```

**Test it:** Create Order, add 3 items, verify subtotal.

---

## 💡 YOUR TURN: Practice Nested Loops

In your Order class, add:
```java
/**
 * Find all sandwiches with a specific bread type
 */
public ArrayList<Sandwich> findSandwichesByBread(String breadType) {
    // TODO: Loop through items
    // TODO: Check if item is Sandwich
    // TODO: Check if bread matches
    // TODO: Add to results list
}
```

---

# 🧱 PHASE 5 – BASIC UNIT TESTING

## 📖 CONCEPT

Testing = verifying your code works BEFORE building the UI.

**Why test?**
- Catch bugs early
- Confidence when making changes
- Faster development (no manual testing!)

> 💡 **CAPSTONE CONNECTION:** Test pricing logic before wiring up menus!

---

## 🔍 EXAMPLE: Testing Topping

```java
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class com.richie.model.ToppingTest {
    @Test
    void getPrice_shouldReturn1_50_whenPremiumBacon() {
        // ARRANGE
        Topping bacon = new Topping("Bacon", 1.00, true);
        
        // ACT
        double price = bacon.getPrice();
        
        // ASSERT
        assertEquals(1.50, price, 0.01);  // 0.01 = tolerance for doubles
    }
    
    @Test
    void getPrice_shouldReturn0_50_whenRegularLettuce() {
        Topping lettuce = new Topping("Lettuce", 0.50, false);
        assertEquals(0.50, lettuce.getPrice(), 0.01);
    }
}
```

**Pattern:** ARRANGE → ACT → ASSERT

---

## 💡 YOUR TURN: Test Sandwich Pricing

Write tests for:
1. 4" sandwich (should be $5.50)
2. 8" sandwich with 2 toppings
3. Empty sandwich (no toppings)

---

# 🧱 PHASE 6 – INHERITANCE (IS-A)

## 📖 CONCEPT

Child class **IS-A** parent class.
- SignatureSandwich **IS-A** Sandwich
- Drink **IS-A** Product

**Benefits:**
- Reuse code (no duplication!)
- Organize related classes
- Enable polymorphism (next phase)

> 💡 **CAPSTONE CONNECTION:** All your items (Sandwich, Drink, Chips) will extend Product!

---

## 🔍 EXAMPLE: Product Hierarchy

```java
// PARENT CLASS
public class Product {
    protected String name;  // protected = child can access
    protected double basePrice;
    
    public Product(String name, double basePrice) {
        this.name = name;
        this.basePrice = basePrice;
    }
    
    public String getName() {
        return name;
    }
    
    public double getPrice() {
        return basePrice;
    }
}

// CHILD CLASS
public class Drink extends Product {
    private String size;
    
    public Drink(String name, String size, double basePrice) {
        super(name, basePrice);  // MUST be first line!
        this.size = size;
    }
    
    // Inherited: getName(), getPrice()
    
    // New method
    public String getSize() {
        return size;
    }
}
```

**Key:** `super()` calls parent constructor - MUST be first line!

---

## 🔍 EXAMPLE: Method Overriding

```java
public class SignatureSandwich extends Sandwich {
    private String signatureName;
    
    public SignatureSandwich(String signatureName, String breadType, String size) {
        super(breadType, size);  // Initialize Sandwich parts
        this.signatureName = signatureName;
    }
    
    @Override
    public double getPrice() {
        double regularPrice = super.getPrice();  // Get parent's price
        return regularPrice * 0.90;  // 10% discount
    }
    
    public String getSignatureName() {
        return signatureName;
    }
}
```

**@Override** = tells Java "I'm replacing parent's method"

> 💡 **CAPSTONE CONNECTION:** Signature sandwiches get automatic discount!

---

## 💡 YOUR TURN: Build Drink Class

```java
public class Drink extends Product {
    private String size;  // small, medium, large
    
    // TODO: Constructor (hint: use super!)
    
    @Override
    public double getPrice() {
        // TODO: Calculate based on size
        // small = basePrice
        // medium = basePrice * 1.5
        // large = basePrice * 2.0
    }
}
```

**Test it:** Create large drink with basePrice $2.00, verify price is $4.00

---

# 🧱 PHASE 7 – POLYMORPHISM

## 📖 CONCEPT

**Polymorphism** = "many forms"
- One variable type can hold different object types
- Each object behaves correctly for its type

**Example in real life:**
- "Animal" can be Dog, Cat, Bird
- When you say "make sound", each does the right thing (bark, meow, chirp)

> 💡 **CAPSTONE CONNECTION:** Store ALL products in one list, but each calculates price differently!

---

## 🔍 EXAMPLE: Polymorphic Collection

```java
ArrayList<Product> items = new ArrayList<>();

// Add different types
items.add(new Sandwich("White", "8\""));
items.add(new Drink("Lemonade", "large", 2.00));
items.add(new Chips("BBQ"));

// Each calls its OWN getPrice()!
double total = 0;
for (Product p : items) {
    total += p.getPrice();  // Polymorphism in action!
}
```

**Magic:** Java automatically calls the correct `getPrice()` for each type!

---

## 🔍 EXAMPLE: Type Checking with instanceof

```java
public void displayDetails(Product p) {
    System.out.println("Name: " + p.getName());
    System.out.println("Price: " + p.getPrice());
    
    // Need type-specific info?
    if (p instanceof Sandwich) {
        Sandwich s = (Sandwich) p;  // Cast to access Sandwich methods
        System.out.println("Bread: " + s.getBreadType());
    } else if (p instanceof Drink) {
        Drink d = (Drink) p;
        System.out.println("Size: " + d.getSize());
    }
}
```

**Pattern:** Check type with `instanceof`, then cast to access specific methods.

---

## 💡 YOUR TURN: Polymorphic Order

Modify your Order class:
```java
public class Order {
    private ArrayList<Product> items;  // Changed from Sandwich!
    
    // Now can add ANY product type
    public void addItem(Product p) {
        items.add(p);
    }
    
    public void displayAll() {
        for (Product p : items) {
            // TODO: Display name and price
            // TODO: If Sandwich, also show bread
            // TODO: If Drink, also show size
        }
    }
}
```

---

# 🧱 PHASE 8 – ABSTRACT CLASSES

## 📖 CONCEPT

**Abstract class** = can't create objects from it.

**Why?** Because it's too general!
- You can't order "a product" - you order a sandwich, drink, or chips
- You can't adopt "an animal" - you adopt a dog, cat, or bird

**Use abstract class when:**
- Multiple classes share code
- But parent class doesn't make sense on its own

> 💡 **CAPSTONE CONNECTION:** Product is abstract - customers order specific items!

---

## 🎨 DECISION TREE: When to Use What

```
Need to share code between classes?
│
├─ YES → Continue
│   │
│   ├─ Parent class makes sense on its own?
│   │   ├─ YES → Regular inheritance
│   │   └─ NO → Abstract class ✓
│   │
│   └─ No shared implementation, just contract?
│       └─ YES → Interface (next phase)
│
└─ NO → Just use composition
```

---

## 🔍 EXAMPLE: Abstract Product

```java
public abstract class Product {
    protected String name;
    
    public Product(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    // ABSTRACT METHOD - children MUST implement
    public abstract double getPrice();
    
    // CONCRETE METHOD - children inherit as-is
    public void displayInfo() {
        System.out.println(name + ": " + getPrice());
    }
}
```

**Key differences:**
- `abstract class Product` = can't do `new Product(...)`
- `abstract double getPrice()` = no body, children must provide

---

## 🔍 EXAMPLE: Concrete Child Classes

```java
public class Sandwich extends Product {
    private String breadType;
    private String size;
    
    public Sandwich(String breadType, String size) {
        super("Sandwich");  // Initialize Product's name
        this.breadType = breadType;
        this.size = size;
    }
    
    @Override
    public double getPrice() {
        if (size.equals("4\"")) {
            return 5.50;
        } else if (size.equals("8\"")) {
            return 7.00;
        } else {
            return 8.50;
        }
    }
}

public class Chips extends Product {
    private String type;
    
    public Chips(String type) {
        super("Chips");
        this.type = type;
    }
    
    @Override
    public double getPrice() {
        return 1.50;  // All chips same price
    }
}
```

---

## 💡 YOUR TURN: Make Product Abstract

1. Change `Product` to `abstract class Product`
2. Make `getPrice()` abstract: `public abstract double getPrice();`
3. Try to create: `new Product("Test", 5.00);` - what error do you get?
4. Verify Sandwich and Drink still work

---

# 🧱 PHASE 9 – INTERFACES

## 📖 CONCEPT

**Interface** = contract of what a class CAN DO.
- Not about what it IS (inheritance)
- About what it CAN DO (capabilities)

**Examples:**
- Taxable = can calculate tax
- Printable = can print to screen
- Comparable = can compare to another object

> 💡 **CAPSTONE CONNECTION:** Order implements Taxable to calculate tax on total!

---

## 🎨 ABSTRACT CLASS vs INTERFACE

| Feature | Abstract Class | Interface |
|---------|---------------|-----------|
| Purpose | Shared IS-A relationship | Contract/capability |
| Methods | Can have concrete methods | All abstract (until Java 8) |
| Fields | Can have instance fields | Only constants (final) |
| Inheritance | Extend ONE | Implement MANY |
| When to use | Common base with code | Define capability |

**Real example:**
- Bird IS-AN Animal (abstract class)
- Bird CAN FLY (Flyable interface)
- Bat IS-A Mammal (abstract class)
- Bat CAN FLY (Flyable interface)

---

## 🔍 EXAMPLE: Taxable Interface

```java
public interface Taxable {
    double TAX_RATE = 0.07;  // Constant (automatically public static final)
    
    double getSubtotal();     // Abstract method (no body)
    
    // DEFAULT method (has body, children can override)
    default double getTax() {
        return getSubtotal() * TAX_RATE;
    }
    
    default double getTotal() {
        return getSubtotal() + getTax();
    }
}
```

---

## 🔍 EXAMPLE: Order Implements Taxable

```java
public class Order implements Taxable {
    private ArrayList<Product> items;
    
    public Order() {
        this.items = new ArrayList<>();
    }
    
    public void addItem(Product p) {
        items.add(p);
    }
    
    @Override
    public double getSubtotal() {
        double sum = 0;
        for (Product p : items) {
            sum += p.getPrice();
        }
        return sum;
    }
    
    // getTax() and getTotal() inherited from Taxable!
}
```

**Using it:**
```java
Order order = new Order();
order.addItem(new Sandwich("White", "8\""));
order.addItem(new Drink("Coke", "large", 2.00));

System.out.println("Subtotal: " + order.getSubtotal());
System.out.println("Tax: " + order.getTax());
System.out.println("Total: " + order.getTotal());
```

---

## 💡 YOUR TURN: Customizable Interface

Create interface for items customers can customize:
```java
public interface Customizable {
    void addExtra(String extra);
    ArrayList<String> getExtras();
    boolean hasExtras();
}
```

Make Sandwich implement it:
```java
public class Sandwich extends Product implements Customizable {
    private ArrayList<String> extras;
    
    // TODO: Implement all three methods
}
```

---

# 🧱 PHASE 10 – INPUT VALIDATION

## 📖 CONCEPT

**Never trust user input!**
- Users make typos
- Users enter wrong types
- Users enter invalid values

**Solution:** Validate EVERYTHING before using it.

> 💡 **CAPSTONE CONNECTION:** The provided InputValidator handles all your validation needs!

---

## 🔍 EXAMPLE: Basic Validation Pattern

```java
public static double getDouble(Scanner scanner, String prompt) {
    while (true) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();
        
        try {
            double value = Double.parseDouble(input);
            if (value >= 0) {
                return value;
            } else {
                System.out.println("Please enter a positive number.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid number. Try again.");
        }
    }
}
```

**Pattern:** Loop until valid input received.

---

## 🔍 EXAMPLE: Choice Validation

```java
public static String getChoice(Scanner scanner, String prompt, String... validOptions) {
    while (true) {
        System.out.print(prompt);
        String choice = scanner.nextLine().trim().toLowerCase();
        
        for (String option : validOptions) {
            if (choice.equals(option.toLowerCase())) {
                return choice;
            }
        }
        
        System.out.println("Invalid choice. Try again.");
    }
}

// Usage
String size = InputValidator.getChoice(scanner, 
    "Size (4\", 8\", 12\"): ", 
    "4\"", "8\"", "12\"");
```

---

## 📦 COMPLETE InputValidator CLASS PROVIDED

**Location:** Copy from guide or starter code

**What it includes:**
- `getString()` - non-empty strings
- `getInt()` / `getDouble()` - with min/max validation
- `getChoice()` - from list of options
- `getYesNo()` - boolean from yes/no

**Your job:** Use it, don't rebuild it!

```java
// Example usage in your UI
String breadType = InputValidator.getChoice(scanner,
    "Bread type: ", "white", "wheat", "rye", "wrap");
    
int quantity = InputValidator.getInt(scanner,
    "Quantity: ", 1, 10);
```

---

# 🧱 PHASE 11 – FILE I/O (RECEIPTS)

## 📖 CONCEPT

**You already did this in Capstone One!** Same `BufferedWriter` pattern, different format.

**Capstone One:** Appended to one CSV file
**Capstone Two:** Create individual receipt files

> 💡 **CAPSTONE CONNECTION:** Every checkout saves a timestamped receipt file!

---

## 🔍 EXAMPLE: Your Ledger Pattern → Receipts

### **Capstone One (You know this):**
```java
try (BufferedWriter writer = new BufferedWriter(new FileWriter("transactions.csv", true))) {
    String entry = date + "|" + time + "|" + description + "|" + vendor + "|" + amount;
    writer.write(entry);
    writer.newLine();
}
```

### **Capstone Two (Same pattern!):**
```java
try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
    writer.write("DELI-CIOUS RECEIPT\n");
    writer.write("Date: " + LocalDateTime.now() + "\n");
    writer.write("===================\n");
    // Write items...
}
```

**Key difference:** No `true` parameter - each receipt is its own file!

---

## 🔍 EXAMPLE: ReceiptFileManager
```java
package com.richie.util;

import com.richie.model.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ReceiptFileManager {
    private static final String RECEIPTS_FOLDER = "receipts/";
    
    public static void saveReceipt(Order order) {
        // Create folder (like your data folder)
        File folder = new File(RECEIPTS_FOLDER);
        if (!folder.exists()) {
            folder.mkdir();
        }
        
        // Generate filename with timestamp
        LocalDateTime now = LocalDateTime.now();
        String timestamp = now.format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));
        String filename = RECEIPTS_FOLDER + timestamp + ".txt";
        
        // Write file (you know this!)
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            // Header
            writer.write("======================================\n");
            writer.write("        DELI-CIOUS RECEIPT\n");
            writer.write("======================================\n");
            writer.write("Date: " + now.format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm a")) + "\n\n");
            
            // Items
            for (Product p : order.getItems()) {
                writer.write(String.format("%-25s $%6.2f\n", p.getName(), p.getPrice()));
                
                if (p instanceof Sandwich) {
                    Sandwich s = (Sandwich) p;
                    writer.write("  Bread: " + s.getBreadType() + " | Size: " + s.getSize() + "\"\n");
                    if (!s.getToppings().isEmpty()) {
                        writer.write("  Toppings: ");
                        for (Topping t : s.getToppings()) {
                            writer.write(t.getName() + ", ");
                        }
                        writer.write("\n");
                    }
                } else if (p instanceof Drink) {
                    Drink d = (Drink) p;
                    writer.write("  " + d.getSize() + " " + d.getFlavor() + "\n");
                }
                writer.write("\n");
            }
            
            // Totals
            writer.write("======================================\n");
            writer.write(String.format("Subtotal:               $%8.2f\n", order.getSubtotal()));
            writer.write(String.format("Tax (7%%):               $%8.2f\n", order.getTax()));
            writer.write("--------------------------------------\n");
            writer.write(String.format("TOTAL:                  $%8.2f\n", order.getTotal()));
            writer.write("======================================\n");
            
            System.out.println("✅ Receipt saved: " + filename);
            
        } catch (IOException e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }
    
    // Optional: Display to screen before saving
    public static void displayReceipt(Order order) {
        System.out.println("\n======================================");
        System.out.println("           ORDER SUMMARY");
        System.out.println("======================================\n");
        
        for (Product p : order.getItems()) {
            System.out.printf("%-25s $%6.2f\n", p.getName(), p.getPrice());
        }
        
        System.out.println("\n======================================");
        System.out.printf("Subtotal:               $%8.2f\n", order.getSubtotal());
        System.out.printf("Tax (7%%):               $%8.2f\n", order.getTax());
        System.out.println("--------------------------------------");
        System.out.printf("TOTAL:                  $%8.2f\n", order.getTotal());
        System.out.println("======================================\n");
    }
}
```

---

## 📊 COMPARISON

| Ledger (Cap 1) | Receipts (Cap 2) |
|----------------|------------------|
| One CSV file | Multiple TXT files |
| `new FileWriter(file, true)` | `new FileWriter(file)` |
| `transactions.csv` | `20251104-143025.txt` |
| Pipe-delimited | Formatted text |
| Same `BufferedWriter` ✓ | Same `BufferedWriter` ✓ |
| `LocalDate.now()` | `LocalDateTime.now()` |

---

## 💡 YOUR TURN

1. Create `ReceiptFileManager.java` in `com.richie.util`
2. Implement `saveReceipt(Order order)`
3. Test it:
```java
Order order = new Order();
order.addItem(new Sandwich("BLT", "white", "8"));
order.addItem(new Drink("Coke", "large"));

ReceiptFileManager.displayReceipt(order);
ReceiptFileManager.saveReceipt(order);
```
4. Check `receipts/` folder for the file

---

## ✅ SELF-CHECK

- [ ] `receipts/` folder created automatically
- [ ] Filename has timestamp format `yyyyMMdd-HHmmss.txt`
- [ ] Receipt shows all products
- [ ] Totals are correct
- [ ] File readable with any text editor

**Time:** 1-2 hours (you know BufferedWriter already!)

---

# 🧱 PHASE 12 – UI ARCHITECTURE

## 📖 CONCEPT

**Separate concerns:**
- Model classes (Sandwich, Order) = data + logic
- UI class = display + input only
- Utility classes = helpers

**Why?** Easier to test, modify, and understand.

---

## 🔍 EXAMPLE: Clean Menu Structure

```java
public class UserInterface {
    private Scanner scanner;
    private Order currentOrder;
    
    public UserInterface() {
        this.scanner = new Scanner(System.in);
    }
    
    public void start() {
        boolean running = true;
        
        while (running) {
            displayHomeMenu();
            String choice = InputValidator.getChoice(scanner,
                "Select option: ", "1", "2", "0");
            
            if (choice.equals("1")) {
                startNewOrder();
            } else if (choice.equals("2")) {
                System.out.println("Thank you!");
                running = false;
            }
        }
    }
    
    private void displayHomeMenu() {
        System.out.println("\n=== DELI-CIOUS ===");
        System.out.println("1) New Order");
        System.out.println("2) Exit");
    }
    
    private void startNewOrder() {
        currentOrder = new Order();
        orderLoop();
    }
    
    private void orderLoop() {
        boolean ordering = true;
        
        while (ordering) {
            displayOrderMenu();
            String choice = InputValidator.getChoice(scanner,
                "Select option: ", "1", "2", "3", "4", "0");
            
            if (choice.equals("1")) {
                addSandwich();
            } else if (choice.equals("2")) {
                addDrink();
            } else if (choice.equals("3")) {
                addChips();
            } else if (choice.equals("4")) {
                checkout();
                ordering = false;
            } else {
                ordering = false;
            }
        }
    }
    
    private void displayOrderMenu() {
        System.out.println("\n=== ORDER MENU ===");
        System.out.println("1) Add Sandwich");
        System.out.println("2) Add Drink");
        System.out.println("3) Add Chips");
        System.out.println("4) Checkout");
        System.out.println("0) Cancel Order");
    }
    
    private void addSandwich() {
        String bread = InputValidator.getChoice(scanner,
            "Bread (white/wheat/rye/wrap): ",
            "white", "wheat", "rye", "wrap");
            
        String size = InputValidator.getChoice(scanner,
            "Size (4\"/8\"/12\"): ",
            "4\"", "8\"", "12\"");
        
        Sandwich s = new Sandwich(bread, size);
        
        // Add toppings loop here...
        
        currentOrder.addItem(s);
        System.out.println("Sandwich added!");
    }
    
    private void checkout() {
        ReceiptFileManager.displayReceipt(currentOrder);
        
        String confirm = InputValidator.getYesNo(scanner,
            "Confirm order? (yes/no): ");
        
        if (confirm) {
            ReceiptFileManager.saveReceipt(currentOrder);
            System.out.println("Order complete!");
        }
    }
}
```

---

# ✅ SELF-CHECK QUIZZES

## Phase 1-3: Basics

1. What's the difference between a class and an object?
2. Why use private fields?
3. What does `this` refer to?
4. When should you use `static`?
5. What is method overloading?

<details><summary>Answers</summary>

1. Class = blueprint, Object = instance made from blueprint
2. Encapsulation - control how data is accessed/modified
3. The current object's fields/methods
4. When data/methods are shared across all objects OR utility methods
5. Same method name, different parameters

</details>

---

## Phase 4-6: Collections & Inheritance

1. What happens if you forget to initialize an ArrayList?
2. Can you remove from a collection while iterating? If not, what's the pattern?
3. What does `super()` do?
4. What does `@Override` mean?
5. When is polymorphism useful?

<details><summary>Answers</summary>

1. NullPointerException when you try to use it
2. No - create separate list of items to remove, then remove them after iteration
3. Calls parent class constructor - must be first line in child constructor
4. Tells Java you're replacing a parent method - catches typos
5. When you want to store different types in one collection but have them behave correctly

</details>

---

## Phase 7-9: Advanced OOP

1. What's the difference between abstract class and interface?
2. Can you create an object from an abstract class?
3. How many classes can you extend? How many interfaces can you implement?
4. When should you use `instanceof`?
5. What does `protected` mean?

<details><summary>Answers</summary>

1. Abstract class = IS-A with some implementation; Interface = CAN-DO contract
2. No - it's abstract
3. Extend ONE class, implement MANY interfaces
4. When you need to check the actual type of an object before casting
5. Child classes can access it, but outside classes cannot (like private but for inheritance)

</details>

---

# 🐛 TOP 5 ERRORS & FIXES

## 1. NullPointerException

**What it means:** Tried to use something that's null.

**Common causes:**
```java
// Forgot to initialize ArrayList
private ArrayList<Topping> toppings;  // null!
toppings.add(t);  // CRASH

// Fix:
this.toppings = new ArrayList<>();

// Method returned null
Topping t = findTopping("Bacon");  // returns null if not found
t.getPrice();  // CRASH

// Fix: Check for null
if (t != null) {
    t.getPrice();
}
```

---

## 2. ClassCastException

**What it means:** Tried to cast to wrong type.

**Fix:**
```java
// BAD
Sandwich s = (Sandwich) product;  // product is actually a Drink!

// GOOD
if (product instanceof Sandwich) {
    Sandwich s = (Sandwich) product;
    // Now safe to use s
}
```

---

## 3. StackOverflowError (Infinite Recursion)

**What it means:** Method calls itself forever.

**Fix:**
```java
// BAD
@Override
public double getPrice() {
    return getPrice() * 0.90;  // Calls itself!
}

// GOOD
@Override
public double getPrice() {
    return super.getPrice() * 0.90;  // Calls parent's version
}
```

---

## 4. Method doesn't override

**What it means:** @Override annotation but method doesn't match parent.

**Fix:**
```java
// BAD - typo in method name
@Override
public double getprice() { ... }  // Lowercase 'p'

// GOOD
@Override
public double getPrice() { ... }
```

---

## 5. Forgot super() in constructor

**What it means:** Child constructor didn't initialize parent fields.

**Fix:**
```java
// BAD
public Sandwich(String bread, String size) {
    this.bread = bread;  // If bread is in Product, this won't work!
}

// GOOD
public Sandwich(String bread, String size) {
    super(bread);  // FIRST line - initialize parent
    this.size = size;
}
```

---

# 📋 QUICK REFERENCE CHEAT SHEET

## Syntax Quick Lookup

```java
// CLASS
public class ClassName {
    private String field;
    
    public ClassName(String field) {
        this.field = field;
    }
    
    public String getField() { return field; }
}

// INHERITANCE
public class Child extends Parent {
    public Child(String parentField, String childField) {
        super(parentField);  // FIRST line
        this.childField = childField;
    }
    
    @Override
    public void method() {
        super.method();  // Call parent's version
        // Child's additional logic
    }
}

// ABSTRACT CLASS
public abstract class Parent {
    public abstract double calculate();  // No body
    public void concrete() { }  // Has body
}

// INTERFACE
public interface Taxable {
    double getTotal();  // No body, no abstract keyword needed
    double TAX_RATE = 0.07;  // Constants only
}

public class Order implements Taxable {
    @Override
    public double getTotal() { /* implement */ }
}

// POLYMORPHISM
ArrayList<Product> items = new ArrayList<>();
items.add(new Sandwich(...));
items.add(new Drink(...));

for (Product p : items) {
    p.getPrice();  // Calls correct version for each type
}

// INSTANCEOF
if (product instanceof Sandwich) {
    Sandwich s = (Sandwich) product;
    s.getBreadType();  // Now can access Sandwich methods
}

// VALIDATION
String size = InputValidator.getChoice(scanner,
    "Size: ", "4\"", "8\"", "12\"");

// FILE I/O
try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
    writer.write("content");
} catch (IOException e) {
    System.out.println("Error: " + e.getMessage());
}
```

---

## When I Need To...

| Task | Use This |
|------|----------|
| Share data across all objects | `static` field |
| Create utility methods | `static` methods in util class |
| Make objects contain objects | Composition (ArrayList field) |
| Create specialized version | Inheritance (`extends`) |
| Force children to implement | Abstract method |
| Define capability contract | Interface (`implements`) |
| Check actual object type | `instanceof` |
| Call parent's method | `super.method()` |
| Initialize parent's fields | `super()` in constructor |
| Handle invalid input | InputValidator methods |
| Save data to file | BufferedWriter + try-with-resources |

---

# 🗺️ GUIDE → CAPSTONE 2 MAPPING

| You Learned | Your Deli App Uses It For |
|-------------|---------------------------|
| Classes & Objects | Topping, Sandwich, Drink, Chips |
| Encapsulation | Private fields, public getters |
| Static | PriceFormatter utility, TAX_RATE constant |
| Overloading | Multiple Sandwich constructors |
| Composition | Order HAS Products, Sandwich HAS Toppings |
| Testing | Verify pricing, tax, calculations |
| Inheritance | SignatureSandwich extends Sandwich |
| Polymorphism | ArrayList\<Product\> holds all types |
| Abstract Class | Product parent (can't create generic product) |
| Interfaces | Taxable (Order), Customizable (Sandwich) |
| instanceof | Type-specific receipt display |
| Input Validation | InputValidator for size, price, yes/no |
| File I/O | Save receipts to timestamped files |
| UI Architecture | Clean menu system, separation of concerns |

---

# 🎯 CAPSTONE 2: 4-DAY ROADMAP

## Day 1: Model Classes (3-4 hours)

**Morning:**
1. Create GitHub repo: `deli-pos-system` (public)
2. Clone to your workspace
3. Create Maven project with package structure:
    - `com.richie.model`
    - `com.richie.ui`
    - `com.richie.util`
4. Build core classes:
    - Topping ✓
    - Product (abstract) ✓
    - Sandwich extends Product ✓
    - Drink extends Product ✓
    - Chips extends Product ✓

**Afternoon:**
5. Build Order (implements Taxable) ✓
6. Write unit tests for EACH class ✓
7. **COMMIT after each class!**

**Checkpoint:** All model classes complete, tested, on GitHub.

---

## Day 2: Advanced Features (3-4 hours)

**Morning:**
1. SignatureSandwich (inheritance + override)
2. Test signature pricing (should apply discount)
3. Complete Taxable interface in Order
4. Test tax calculations

**Afternoon:**
5. PriceFormatter utility class
6. ReceiptFileManager utility class
7. Test file saving (check receipts folder)
8. Integration tests (Order → Products → Toppings)

**Checkpoint:** Can create order, calculate tax, save receipt.

---

## Day 3: User Interface (3-4 hours)

**Morning:**
1. Copy InputValidator utility (or build it)
2. Test all validation methods
3. UserInterface class (menu skeleton)
4. Home menu + New Order flow

**Afternoon:**
5. Implement all "Add" methods (Sandwich/Drink/Chips)
6. View Order feature
7. Checkout flow (with file saving)
8. Manual testing (full flow multiple times)

**Checkpoint:** Working app - can order, view, checkout.

---

## Day 4: Polish & Documentation (2-3 hours)

**Morning:**
1. Test edge cases:
    - Empty order checkout
    - Invalid input handling
    - Cancel order mid-way
2. Fix any bugs discovered
3. Add code comments to complex methods

**Afternoon:**
4. Create README.md:
    - Project description
    - Features list
    - Screenshots of menus
    - One interesting code snippet with explanation
    - How to run the project
5. Final manual testing
6. Final commit + push

**Checkpoint:** Portfolio-ready project on GitHub!

---

# 🎓 YOU'RE READY!

## What This Guide Taught You:

✅ **Core OOP:** Classes, objects, static, overloading, composition
✅ **Advanced OOP:** Inheritance, polymorphism, abstract, interfaces
✅ **Practical Skills:** Testing, file I/O, input validation, UI architecture
✅ **Debugging:** Top 5 errors + how to fix them
✅ **Capstone-specific:** Nested searches, type checking, polymorphic collections

## What to Do Now:

1. **Start Day 1** of the roadmap
2. **Reference cheat sheets** when coding
3. **Use debugging guide** when stuck
4. **Commit often** (after each class)
5. **Test everything** (don't wait until the end)

## Tips for Success:

- **Small steps:** One class at a time
- **Test immediately:** Don't build everything then test
- **Use YOUR TURN exercises:** They're practice for Capstone features
- **Copy InputValidator:** Save time, focus on core logic
- **Ask for help:** If stuck 30+ min on same problem

---

# 🚀 START BUILDING!

Close this guide. Open IntelliJ. Create your repo. Build Phase 1.

**You've got this, Richie!** 💪🚀

---

**REVISED LENGTH:** ~1,580 lines  
**CHANGES MADE:**
- ✅ Removed ternary operators (replaced with if/else)
- ✅ Shortened InputValidator section (concept + pattern, not full implementation)
- ✅ Shortened File I/O section (pattern, not every detail)
- ✅ Condensed debugging to top 5 errors
- ✅ Added "Capstone Connection" callouts throughout
- ✅ Added decision tree for abstract vs interface
- ✅ Added visual diagram for composition
- ✅ Added extra "Your Turn" exercise for nested loops
- ✅ Restructured quizzes into consolidated sections
- ✅ Kept all essential content, removed redundancy
