# 🎓 RICHIE'S WORKBOOK 4 & 5 MASTERY GUIDE
**Goal:** Learn OOP concepts deeply through understanding + practice, so you can confidently build Capstone 2 without constantly referring back.

---

# 📚 HOW TO USE THIS GUIDE

This is NOT a copy-paste guide. This is a **learn-by-doing** guide.

## The Pattern for Each Concept:
1. **📖 CONCEPT** - I explain WHY this matters and WHEN to use it
2. **🔍 EXAMPLE** - I show you working code with detailed comments
3. **💡 YOUR TURN** - You build something similar from scratch (NO copy-paste)
4. **✅ VERIFY** - Check your understanding with a mini-quiz
5. **🔨 PRACTICE** - Build something real for your deli app

## Rules:
- **Read the concept** before looking at code
- **Type all code yourself** - no copy-paste (muscle memory matters!)
- **Try the "Your Turn" section** before moving forward
- **Test your code** after every exercise
- **Commit to GitHub** after each phase

---

# 📚 WHERE YOU ARE

You've completed Workbook 3, so you know:
- ✅ File I/O (BufferedReader/Writer)
- ✅ Collections (ArrayList, HashMap)
- ✅ Loops and basic validation
- ✅ String manipulation and parsing

**What's missing:** Object-oriented thinking. Right now you're thinking procedurally. Time to think in objects.

---

# 🧱 PHASE 1 – UNDERSTANDING CLASSES & OBJECTS

## 📖 CONCEPT: What's the Point of Classes?

**Problem:** Imagine tracking sandwich orders without classes:
```java
String sandwich1Bread = "White";
String sandwich1Size = "8\"";
double sandwich1Price = 7.00;
ArrayList<String> sandwich1Toppings = new ArrayList<>();

String sandwich2Bread = "Wheat";
String sandwich2Size = "4\"";
double sandwich2Price = 5.50;
ArrayList<String> sandwich2Toppings = new ArrayList<>();

// This is messy and doesn't scale!
```

**Solution:** A class groups related data + behavior:
```java
Sandwich sandwich1 = new Sandwich("White", "8\"");
Sandwich sandwich2 = new Sandwich("Wheat", "4\"");
// Clean, organized, scalable
```

### Why This Matters for Your Deli App:
- You'll have 10+ sandwiches per order
- Each sandwich has its own toppings, size, price
- Without classes, you'd have 100+ scattered variables
- With classes, you have organized, reusable blueprints

---

## 🔍 EXAMPLE: Building Your First Class

Let's build a `Topping` class. **Read every comment carefully:**

```java
package com.richie.model;

/**
 * Topping represents a single ingredient that can be added to a sandwich.
 * This is the BLUEPRINT - it describes what data a topping needs.
 */
public class Topping {
    
    // PRIVATE fields - data hidden from outside world (encapsulation)
    private String name;
    private double basePrice;
    private boolean isPremium;
    
    /**
     * CONSTRUCTOR - special method that runs when you create a new Topping.
     * It initializes the object with starting values.
     * 
     * @param name - what is this topping called?
     * @param basePrice - how much does it cost normally?
     * @param isPremium - does it cost extra?
     */
    public Topping(String name, double basePrice, boolean isPremium) {
        // "this.name" = the object's name field
        // "name" = the parameter passed in
        // We use "this" to distinguish between the two
        this.name = name;
        this.basePrice = basePrice;
        this.isPremium = isPremium;
    }
    
    /**
     * GETTER - allows outside code to READ the name.
     * Why not just make name public? Because we want CONTROL.
     * Maybe later we want to format names in uppercase - we can
     * change this one method instead of hunting down everywhere name is used.
     */
    public String getName() {
        return name;
    }
    
    /**
     * DERIVED GETTER - calculates a value instead of returning a stored field.
     * This is business logic: premium toppings cost 50% more.
     */
    public double getPrice() {
        if (isPremium) {
            return basePrice * 1.5;  // Premium markup
        }
        return basePrice;
    }
    
    /**
     * Boolean getters use "is" instead of "get" - it reads like English:
     * "Is this topping premium?" not "Get premium"
     */
    public boolean isPremium() {
        return isPremium;
    }
    
    // Setters would go here if we needed them (we might not!)
}
```

**Using this class:**
```java
// Create two topping OBJECTS from the Topping BLUEPRINT
Topping lettuce = new Topping("Lettuce", 0.25, false);
Topping bacon = new Topping("Bacon", 1.00, true);

// Each object has its own data
System.out.println(lettuce.getName() + ": $" + lettuce.getPrice()); // Lettuce: $0.25
System.out.println(bacon.getName() + ": $" + bacon.getPrice());     // Bacon: $1.50
```

---

## 💡 YOUR TURN: Build the Chips Class

**DO NOT look ahead. Close the example above and try this:**

Create a `Chips` class that:
- Has a `type` field (String) - like "BBQ", "Classic", "Sour Cream"
- Constructor that takes the type
- `getType()` getter
- `getPrice()` getter that ALWAYS returns 1.50 (all chips same price)

**Write it yourself. Test it. Make sure it compiles.**

<details>
<summary>📝 Solution (only look after trying!)</summary>

```java
package com.richie.model;

public class Chips {
    private String type;
    
    public Chips(String type) {
        this.type = type;
    }
    
    public String getType() {
        return type;
    }
    
    public double getPrice() {
        return 1.50;
    }
}
```
</details>

---

## ✅ VERIFY YOUR UNDERSTANDING

**Answer without running code:**

1. What's the difference between a class and an object?
2. Why are fields `private` instead of `public`?
3. What does `this` mean in `this.name = name;`?
4. What's a "derived getter"?

<details>
<summary>Answers</summary>

1. **Class** = blueprint (the recipe), **Object** = instance (the actual sandwich you made from the recipe)
2. `private` = encapsulation. We control access. Can change internal logic without breaking other code.
3. `this.name` = the object's field, `name` = the parameter. They have same name, so we use `this` to distinguish.
4. A getter that calculates a value instead of just returning a stored field (like `getPrice()` for premium toppings)
</details>

---

## 🔨 PRACTICE: Build the Drink Class

**Requirements:**
- `size` field (String) - "small", "medium", "large"
- `flavor` field (String)
- Constructor for both fields
- Getters for both fields
- `getPrice()` that returns:
    - Small: $2.00
    - Medium: $2.50
    - Large: $3.00
    - Default: $0.00 (if invalid size)

**Write it from scratch. Test with different sizes. Commit to GitHub.**

---

# 🧱 PHASE 2 – STATIC VS INSTANCE (WHEN TO SHARE)

## 📖 CONCEPT: Why Do We Need Static?

**The Question:** Should EVERY topping object track how many total toppings exist?

**Without static (bad):**
```java
Topping lettuce = new Topping("Lettuce", 0.25, false);
lettuce.totalCreated = 1;  // Lettuce tracks it

Topping bacon = new Topping("Bacon", 1.00, true);
bacon.totalCreated = 2;  // Bacon tracks it

// Problem: Each object has its own count! They don't agree!
```

**With static (good):**
```java
// One shared counter for the ENTIRE class
static int totalCreated = 0;

// All toppings share this one number
```

### The Rule:
- **Instance (normal):** Each object needs its own copy
    - Example: Each sandwich has its own size, bread, toppings
- **Static (shared):** All objects share one copy
    - Example: Tax rate, counter of total objects, utility methods

---

## 🔍 EXAMPLE: Adding a Static Counter

```java
public class Topping {
    // STATIC - shared by ALL Topping objects
    private static int totalCreated = 0;
    
    // INSTANCE - each Topping has its own
    private String name;
    private double basePrice;
    private boolean isPremium;
    
    public Topping(String name, double basePrice, boolean isPremium) {
        this.name = name;
        this.basePrice = basePrice;
        this.isPremium = isPremium;
        
        // Every time we create a topping, increment the SHARED counter
        totalCreated++;
    }
    
    // STATIC METHOD - called on the class, not an object
    public static int getTotalCreated() {
        return totalCreated;
    }
    
    // Instance methods (same as before)...
}
```

**Using it:**
```java
// Check before creating any
System.out.println(Topping.getTotalCreated());  // 0
// ^ Notice: Topping.method() not object.method()

Topping t1 = new Topping("Lettuce", 0.25, false);
Topping t2 = new Topping("Bacon", 1.00, true);

// Check after creating two
System.out.println(Topping.getTotalCreated());  // 2
```

---

## 💡 YOUR TURN: Decide Static or Instance

For each, decide if it should be **static** or **instance**:

1. The sales tax rate (7%)
2. A customer's name
3. The current order number (incrementing counter)
4. A sandwich's bread type
5. A utility method to format prices: `formatPrice(double price)`

<details>
<summary>Answers</summary>

1. **Static** - same tax rate for everyone
2. **Instance** - each customer has their own name
3. **Static** - shared counter across all orders
4. **Instance** - each sandwich has its own bread
5. **Static** - utility method doesn't need object data
</details>

---

## 🔨 PRACTICE: Build a Static Utility Class

Create `PriceFormatter.java`:
- Make the constructor **private** (so nobody can create instances)
- Add a **static** method `format(double price)` that returns a String like "$12.50"
- Add a **static** method `applyDiscount(double price, double percentOff)` that returns the discounted price

Test it:
```java
double price = 12.5;
System.out.println(PriceFormatter.format(price));  // $12.50

double sale = PriceFormatter.applyDiscount(price, 10);  // 10% off
System.out.println(PriceFormatter.format(sale));  // $11.25
```

**Why is this useful?** Every time you need to display a price in your deli app, call `PriceFormatter.format()`. If you later decide to change formatting (add currency symbol, different decimal places), you change ONE place.

---

# 🧱 PHASE 3 – METHOD OVERLOADING (FLEXIBILITY)

## 📖 CONCEPT: Same Name, Different Ways

**The Problem:** Sometimes you want to create an object with different amounts of information.

**Example:** Creating a drink:
- Sometimes you know the size and flavor: `Drink("medium", "Lemonade")`
- Sometimes just the size (default to water): `Drink("medium")`

**Without overloading (bad):**
```java
public class Drink {
    public Drink(String size, String flavor) { ... }
    public DrinkWithDefaultFlavor(String size) { ... }  // Different name = confusing
}
```

**With overloading (good):**
```java
public class Drink {
    public Drink(String size, String flavor) { ... }
    public Drink(String size) { ... }  // Same name, different parameters
}
```

### The Rules:
- Same method name
- Different parameters (number, type, or order)
- Compiler picks the right one based on what you pass

---

## 🔍 EXAMPLE: Overloaded Constructors

```java
public class Drink {
    private String size;
    private String flavor;
    
    /**
     * MAIN CONSTRUCTOR - has all the parameters.
     * This is where the actual initialization happens.
     */
    public Drink(String size, String flavor) {
        this.size = size;
        this.flavor = flavor;
    }
    
    /**
     * CONVENIENCE CONSTRUCTOR - provides defaults for some parameters.
     * Uses "this()" to call the main constructor.
     * This avoids duplicating initialization code!
     */
    public Drink(String size) {
        this(size, "Water");  // Call the 2-parameter constructor
        // ^ Must be first line in constructor
    }
    
    // Getters...
    public double getPrice() {
        switch (size.toLowerCase()) {
            case "small": return 2.00;
            case "medium": return 2.50;
            case "large": return 3.00;
            default: return 0.00;
        }
    }
}
```

**Using it:**
```java
// Use the 2-parameter constructor
Drink d1 = new Drink("large", "Lemonade");

// Use the 1-parameter constructor (defaults to Water)
Drink d2 = new Drink("medium");

System.out.println(d1.getFlavor());  // Lemonade
System.out.println(d2.getFlavor());  // Water
```

**Why `this()` matters:** If you duplicate initialization code in both constructors, and later change how initialization works, you have to update it in TWO places. `this()` = one source of truth.

---

## 💡 YOUR TURN: Overload a Method

In your `Chips` class, add a method called `describe()`:
- One version takes no parameters and returns: "Chips: [type]"
- Another version takes a boolean `includePrice` and returns: "Chips: [type] - $1.50" (if true)

Test both versions.

---

## ✅ VERIFY YOUR UNDERSTANDING

**True or False:**

1. Overloaded methods must have different return types
2. Overloaded constructors help avoid code duplication
3. `this()` can appear anywhere in a constructor
4. The compiler picks which overloaded method to call based on arguments

<details>
<summary>Answers</summary>

1. **FALSE** - different parameters, not return types
2. **TRUE** - use `this()` to reuse initialization logic
3. **FALSE** - must be first line
4. **TRUE** - it matches based on what you pass
</details>

---

# 🧱 PHASE 4 – COMPOSITION (OBJECTS INSIDE OBJECTS)

## 📖 CONCEPT: The "Has-A" Relationship

**Real world:** A car HAS-A engine. A sandwich HAS-A list of toppings. An order HAS-A list of sandwiches.

**Why this matters:** Your deli app is all about composition:
- Order HAS Products (sandwiches, drinks, chips)
- Sandwich HAS Toppings
- Sandwich HAS a bread type, size, etc.

**The key insight:** Objects contain other objects. This is how we model complex systems.

---

## 🔍 EXAMPLE: Sandwich Contains Toppings

```java
package com.richie.model;

import java.util.ArrayList;

public class Sandwich {
    // Simple fields
    private String breadType;
    private String size;
    
    // COMPOSITION - Sandwich HAS-A list of Toppings
    private ArrayList<Topping> toppings;
    
    public Sandwich(String breadType, String size) {
        this.breadType = breadType;
        this.size = size;
        
        // CRITICAL: Initialize the ArrayList!
        // Without this line, toppings would be null and you'd get NullPointerException
        this.toppings = new ArrayList<>();
    }
    
    /**
     * Add a topping to this sandwich.
     * Notice: we RECEIVE a Topping object that was created elsewhere.
     */
    public void addTopping(Topping topping) {
        toppings.add(topping);
    }
    
    public ArrayList<Topping> getToppings() {
        return toppings;
    }
    
    /**
     * Calculate price by iterating through contained toppings.
     * This is the POWER of composition - Sandwich delegates to Topping.
     */
    public double getPrice() {
        double total = 0;
        
        // Base price depends on size
        switch (size.toLowerCase()) {
            case "4\"": total = 5.50; break;
            case "8\"": total = 7.00; break;
            case "12\"": total = 8.50; break;
        }
        
        // Add up topping prices
        for (Topping topping : toppings) {
            total += topping.getPrice();  // Topping knows its own price
        }
        
        return total;
    }
    
    public String getBreadType() { return breadType; }
    public String getSize() { return size; }
}
```

**Using it:**
```java
// Create the container
Sandwich mySandwich = new Sandwich("Wheat", "8\"");

// Add contained objects
mySandwich.addTopping(new Topping("Lettuce", 0.25, false));
mySandwich.addTopping(new Topping("Bacon", 1.00, true));

// Sandwich delegates to toppings to calculate total
System.out.println("Total: $" + mySandwich.getPrice());  // $8.75
```

**Key insight:** The Sandwich doesn't know HOW toppings calculate their price. It just asks them. This is **loose coupling** - objects work together but aren't tightly dependent on each other's internal details.

---

## 💡 YOUR TURN: Build the Order Class

Create an `Order` class that:
- HAS-A list of Products (you'll need to create a simple Product class first)
- Has a method `addProduct(Product p)`
- Has a method `getTotalPrice()` that loops through products and sums their prices
- Has a method `printReceipt()` that displays all products

For now, make `Product` a simple class with:
- `name` field
- Constructor
- `getPrice()` method (return a dummy value like 5.00 for now)

**Test it by creating an order with 3 products.**

---

## 🔨 PRACTICE: Nested Display

In your `Sandwich` class, add a method `printDetails()` that displays:
```
8" Wheat sandwich
  - Lettuce ($0.25)
  - Bacon ($1.50)
Total: $8.75
```

**Hint:** You'll need to loop through `toppings` and call methods on each Topping object.

This teaches you to navigate nested structures - critical for your deli app!

---

# 🧱 PHASE 5 – UNIT TESTING (PROVE IT WORKS)

## 📖 CONCEPT: Why Test?

**You:** "My code works, I tested it manually."
**Interviewer:** "Show me your tests."
**You:** "..."

**Professional developers write automated tests because:**
1. Manual testing is slow and tedious
2. Tests document HOW your code should work
3. When you change code later, tests catch bugs immediately
4. Employers EXPECT to see tests in your portfolio

---

## 🔍 EXAMPLE: Your First JUnit Test

**Step 1:** Add JUnit to `pom.xml` (between `<dependencies>` tags):
```xml
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter</artifactId>
    <version>5.9.3</version>
    <scope>test</scope>
</dependency>
```

**Step 2:** Create `ToppingTest.java` in `src/test/java/com/richie/model/`:

```java
package com.richie.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ToppingTest {
    
    /**
     * @Test tells JUnit "this is a test method"
     * Method name should describe what you're testing
     */
    @Test
    void getPrice_shouldReturnBasePrice_whenNotPremium() {
        // ARRANGE - set up test data
        // Create a regular (non-premium) topping
        Topping lettuce = new Topping("Lettuce", 0.25, false);
        double expectedPrice = 0.25;
        
        // ACT - call the method being tested
        double actualPrice = lettuce.getPrice();
        
        // ASSERT - verify the result matches expectation
        // Third parameter (0.01) is delta for comparing doubles
        assertEquals(expectedPrice, actualPrice, 0.01);
    }
    
    @Test
    void getPrice_shouldApply50PercentMarkup_whenPremium() {
        // ARRANGE
        Topping bacon = new Topping("Bacon", 1.00, true);
        double expectedPrice = 1.50;  // 1.00 * 1.5
        
        // ACT
        double actualPrice = bacon.getPrice();
        
        // ASSERT
        assertEquals(expectedPrice, actualPrice, 0.01);
    }
}
```

**Step 3:** Run tests in IntelliJ:
- Right-click on test class → "Run ToppingTest"
- Green = passing ✅
- Red = failing ❌

---

## 💡 YOUR TURN: Test Your Drink Class

Write 3 tests for your `Drink` class:
1. Test that a small drink costs $2.00
2. Test that a medium drink costs $2.50
3. Test that a large drink costs $3.00

**Follow the AAA pattern:** Arrange, Act, Assert

<details>
<summary>Solution template (try first!)</summary>

```java
@Test
void getPrice_shouldReturn2Dollars_whenSizeIsSmall() {
    // ARRANGE
    Drink drink = new Drink("small", "Water");
    double expected = 2.00;
    
    // ACT
    double actual = drink.getPrice();
    
    // ASSERT
    assertEquals(expected, actual, 0.01);
}
```
</details>

---

## ✅ VERIFY YOUR UNDERSTANDING

1. What does AAA stand for in testing?
2. Why do we use assertEquals instead of `if (expected == actual)`?
3. When should you write tests - before or after writing code?
4. What's the benefit of automated tests over manual testing?

<details>
<summary>Answers</summary>

1. **Arrange, Act, Assert** - setup, execute, verify
2. `assertEquals` gives better error messages when tests fail, and JUnit tracks pass/fail
3. **Ideally before** (Test-Driven Development) - helps clarify what you're building
4. **Speed + reliability** - run 100 tests in seconds, catches bugs immediately when you change code
</details>

---

# 🧱 PHASE 6 – INHERITANCE (IS-A RELATIONSHIP)

## 📖 CONCEPT: Why Inheritance?

**Scenario:** You have regular sandwiches and signature sandwiches (like BLT, Philly Cheesesteak).

**Signature sandwiches:**
- ARE sandwiches (have bread, size, toppings)
- BUT have preset toppings
- AND get a discount

**Without inheritance:**
```java
class Sandwich { /* all the code */ }
class SignatureSandwich { /* duplicate all the same code + new features */ }
// DRY principle violated!
```

**With inheritance:**
```java
class Sandwich { /* common code */ }
class SignatureSandwich extends Sandwich { /* only the DIFFERENT stuff */ }
// DRY principle honored!
```

---

## 🔍 EXAMPLE: SignatureSandwich Extends Sandwich

```java
package com.richie.model;

/**
 * SignatureSandwich IS-A Sandwich.
 * It inherits all fields and methods from Sandwich.
 * We only add what's DIFFERENT.
 */
public class SignatureSandwich extends Sandwich {
    
    private String signatureName;  // NEW field
    
    /**
     * Constructor MUST call parent constructor using super()
     */
    public SignatureSandwich(String signatureName, String breadType, String size) {
        // FIRST: Call parent constructor to initialize inherited fields
        super(breadType, size);
        
        // THEN: Initialize our own fields
        this.signatureName = signatureName;
        
        // FINALLY: Add preset toppings
        addPresetToppings();
    }
    
    /**
     * Private helper method to add signature toppings
     */
    private void addPresetToppings() {
        if (signatureName.equalsIgnoreCase("BLT")) {
            // addTopping() is inherited from Sandwich!
            addTopping(new Topping("Bacon", 1.00, true));
            addTopping(new Topping("Lettuce", 0.25, false));
            addTopping(new Topping("Tomato", 0.50, false));
        } else if (signatureName.equalsIgnoreCase("Philly")) {
            addTopping(new Topping("Steak", 2.00, true));
            addTopping(new Topping("Peppers", 0.50, false));
            addTopping(new Topping("Onions", 0.50, false));
        }
    }
    
    /**
     * OVERRIDE parent's getPrice() method.
     * We change the behavior - signature sandwiches get 10% off.
     */
    @Override
    public double getPrice() {
        // super.getPrice() calls the parent's version
        double regularPrice = super.getPrice();
        return regularPrice * 0.90;  // 10% discount
    }
    
    public String getSignatureName() {
        return signatureName;
    }
}
```

**Using it:**
```java
// Create a signature sandwich - toppings added automatically
SignatureSandwich blt = new SignatureSandwich("BLT", "White", "8\"");
System.out.println("Price: $" + blt.getPrice());  // Uses overridden method

// Create a regular sandwich
Sandwich custom = new Sandwich("Wheat", "8\"");
custom.addTopping(new Topping("Turkey", 1.50, true));
System.out.println("Price: $" + custom.getPrice());  // Uses original method
```

---

## 💡 YOUR TURN: Understand Inheritance Flow

**Answer these questions (no code yet):**

1. What does `extends` mean?
2. What does `super()` do? Why must it be first line?
3. What does `@Override` mean?
4. Can SignatureSandwich access private fields of Sandwich?

<details>
<summary>Answers</summary>

1. `extends` = "inherits from" - child gets parent's fields/methods
2. `super()` calls parent constructor to initialize inherited fields. Must be first because parent must be initialized before child adds to it
3. `@Override` = "I'm replacing parent's method with my version"
4. **NO** - private is private even to children. Use getters/setters or make fields `protected`
</details>

---

## 🔨 PRACTICE: Create Your Own Inheritance

Create a `SpecialtyDrink` class that extends `Drink`:
- Add a field `hasWhippedCream` (boolean)
- Override `getPrice()` to add $0.50 if it has whipped cream
- Test it by creating both a regular Drink and a SpecialtyDrink

**Challenge:** Use `super()` correctly and test the price difference!

---

# 🧱 PHASE 7 – POLYMORPHISM (THE MAGIC)

## 📖 CONCEPT: Same Call, Different Behavior

**Polymorphism** = "many forms"

**The magic:** A parent reference can hold any child object:
```java
Sandwich s1 = new Sandwich("Wheat", "4\"");         // Regular
Sandwich s2 = new SignatureSandwich("BLT", "White", "8\"");  // Signature!
```

**Both are stored as `Sandwich`, but calling `getPrice()` does different things!**

---

## 🔍 EXAMPLE: Polymorphism in Action

```java
// Create a list of Sandwiches (parent type)
ArrayList<Sandwich> order = new ArrayList<>();

// Add both regular and signature sandwiches
order.add(new Sandwich("Wheat", "4\""));
order.add(new SignatureSandwich("BLT", "White", "8\""));
order.add(new Sandwich("Rye", "12\""));
order.add(new SignatureSandwich("Philly", "White", "12\""));

// Loop through and calculate total
double total = 0;
for (Sandwich sandwich : order) {
    double price = sandwich.getPrice();  // Calls the RIGHT method for each type!
    System.out.println("Sandwich: $" + price);
    total += price;
}

System.out.println("Total: $" + total);
```

**What happens:**
1. First sandwich: regular → calls `Sandwich.getPrice()`
2. Second sandwich: signature → calls `SignatureSandwich.getPrice()` (overridden version)
3. Java figures out which method to call at RUNTIME based on actual object type

**Why this is powerful:** You don't need separate code for each type. One loop handles everything!

---

## 💡 YOUR TURN: Polymorphic Collection

Create an ArrayList that holds different types of drinks:
- Add 2 regular Drinks
- Add 2 SpecialtyDrinks (from previous exercise)
- Loop through and print each drink's price
- Observe that the right `getPrice()` is called for each

**Question:** How does Java know which `getPrice()` to call?

---

# 🧱 PHASE 8 – ABSTRACT CLASSES (ENFORCE THE CONTRACT)

## 📖 CONCEPT: When You Can't Instantiate

**Problem:** You want Product to be a parent for Sandwich, Drink, Chips. But what IS a generic "Product"? It doesn't make sense to create one:

```java
Product p = new Product();  // What is this? Too vague!
```

**Solution:** Make Product **abstract** - it's a template, not a real thing:

```java
public abstract class Product {
    // Can have fields and methods like normal
    // BUT can also have abstract methods (no implementation)
    // AND cannot be instantiated
}
```

---

## 🔍 EXAMPLE: Abstract Product Class

```java
package com.richie.model;

/**
 * Product is abstract because:
 * 1. We don't want anyone creating generic "Product" objects
 * 2. We want to FORCE children to implement getPrice() their own way
 */
public abstract class Product {
    
    // Concrete fields - all products have these
    protected String name;  // protected = children can access
    protected String size;
    
    // Concrete constructor - children call this with super()
    public Product(String name, String size) {
        this.name = name;
        this.size = size;
    }
    
    /**
     * ABSTRACT METHOD - no implementation!
     * Children MUST provide their own implementation.
     * Why? Because each product type calculates price differently.
     */
    public abstract double getPrice();
    
    /**
     * CONCRETE METHOD - all children inherit this as-is.
     * They CAN override if needed, but don't have to.
     */
    public void printInfo() {
        System.out.printf("%s (%s) - $%.2f%n", name, size, getPrice());
    }
    
    // Concrete getters
    public String getName() { return name; }
    public String getSize() { return size; }
}
```

**Now update Sandwich to extend Product:**

```java
public class Sandwich extends Product {
    private String breadType;
    private ArrayList<Topping> toppings;
    
    public Sandwich(String breadType, String size) {
        // Call Product constructor
        super("Custom Sandwich", size);
        
        this.breadType = breadType;
        this.toppings = new ArrayList<>();
    }
    
    /**
     * MUST implement this because Product says so!
     * @Override ensures we're actually overriding
     */
    @Override
    public double getPrice() {
        double total = 0;
        switch (size.toLowerCase()) {
            case "4\"": total = 5.50; break;
            case "8\"": total = 7.00; break;
            case "12\"": total = 8.50; break;
        }
        for (Topping topping : toppings) {
            total += topping.getPrice();
        }
        return total;
    }
    
    // Sandwich-specific methods...
    public void addTopping(Topping topping) {
        toppings.add(topping);
    }
    
    public ArrayList<Topping> getToppings() {
        return toppings;
    }
    
    public String getBreadType() { return breadType; }
}
```

**Key insights:**
1. `Product` can't be instantiated: `new Product()` = compiler error
2. Any class extending Product MUST implement `getPrice()`
3. Concrete methods like `printInfo()` are inherited
4. Now you can have a polymorphic collection: `ArrayList<Product>`

---

## 💡 YOUR TURN: Make Drink and Chips Extend Product

**Update your Drink class:**
- Extend Product
- Call `super("Drink", size)` in constructor
- Implement `getPrice()` (you already have this)

**Update your Chips class:**
- Extend Product
- Call `super("Chips", "Regular")` in constructor (chips don't have size variation)
- Implement `getPrice()` (return 1.50)

**Test polymorphism:**
```java
ArrayList<Product> items = new ArrayList<>();
items.add(new Sandwich("Wheat", "8\""));
items.add(new Drink("medium", "Lemonade"));
items.add(new Chips("BBQ"));

for (Product p : items) {
    p.printInfo();  // Inherited method works for all!
}
```

---

## ✅ VERIFY YOUR UNDERSTANDING

1. What's the difference between abstract and concrete methods?
2. Can you create an instance of an abstract class?
3. Why use abstract classes instead of regular inheritance?
4. What happens if a child class doesn't implement an abstract method?

<details>
<summary>Answers</summary>

1. **Abstract** = no body, children must implement. **Concrete** = has body, children inherit as-is
2. **NO** - that's the point! Abstract = blueprint only
3. **Enforce contracts** - guarantee children implement certain methods. Prevent instantiation of vague parent
4. **Compiler error** - child must be abstract too, or implement the method
</details>

---

# 🧱 PHASE 9 – INSTANCEOF & TYPE CHECKING

## 📖 CONCEPT: When You Need to Know the Actual Type

**Scenario:** You have `ArrayList<Product>` but need to access Sandwich-specific methods like `getBreadType()`.

**Problem:**
```java
Product p = new Sandwich("Wheat", "8\"");
p.getBreadType();  // COMPILER ERROR - Product doesn't have this method!
```

**Solution:** Check type, then cast:
```java
if (p instanceof Sandwich) {
    Sandwich s = (Sandwich) p;  // Now we can access Sandwich methods
    System.out.println("Bread: " + s.getBreadType());
}
```

---

## 🔍 EXAMPLE: Receipt with Product Details

```java
public class Order {
    private ArrayList<Product> items;
    
    public Order() {
        this.items = new ArrayList<>();
    }
    
    public void addProduct(Product product) {
        items.add(product);
    }
    
    /**
     * Print detailed receipt - different format for each product type
     */
    public void printReceipt() {
        System.out.println("===== RECEIPT =====");
        
        for (Product item : items) {
            // Check actual type to provide specific details
            if (item instanceof Sandwich) {
                Sandwich s = (Sandwich) item;  // Cast to access Sandwich methods
                System.out.printf("%s %s on %s%n", 
                    s.getSize(), s.getName(), s.getBreadType());
                
                // Show toppings
                for (Topping t : s.getToppings()) {
                    System.out.printf("  + %s%n", t.getName());
                }
                
            } else if (item instanceof Drink) {
                Drink d = (Drink) item;
                System.out.printf("%s %s%n", d.getSize(), d.getFlavor());
                
            } else if (item instanceof Chips) {
                Chips c = (Chips) item;
                System.out.printf("%s Chips%n", c.getType());
            }
            
            // This works for ALL types (polymorphism)
            System.out.printf("Price: $%.2f%n%n", item.getPrice());
        }
        
        System.out.println("===================");
        System.out.printf("TOTAL: $%.2f%n", getTotalPrice());
    }
    
    public double getTotalPrice() {
        double total = 0;
        for (Product item : items) {
            total += item.getPrice();  // Polymorphic call
        }
        return total;
    }
    
    public ArrayList<Product> getItems() {
        return items;
    }
}
```

**When to use instanceof:**
- Displaying type-specific information
- Performing type-specific operations
- Validating before casting

**When NOT to use instanceof:**
- If you find yourself checking types constantly, your design might be wrong
- Prefer polymorphism when possible

---

## 💡 YOUR TURN: Add Type-Specific Info

In your Order class, update `printReceipt()` to:
- For Sandwiches: show bread type and list all toppings
- For Drinks: show flavor
- For Chips: show type

Test with an order containing all three types.

---

# 🧱 PHASE 10 – FILE PERSISTENCE (SAVE YOUR WORK)

## 📖 CONCEPT: Orders Should Survive

**Right now:** When your program closes, all orders disappear.

**Goal:** Save receipts to files so they persist.

**Strategy:**
1. Generate unique filename (timestamp)
2. Write order details to file
3. Use try-with-resources for automatic file closing

---

## 🔍 EXAMPLE: ReceiptFileManager

```java
package com.richie.util;

import com.richie.model.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utility class for saving receipts to files.
 * Static methods = don't need to create an instance.
 */
public class ReceiptFileManager {
    
    /**
     * Save an order to a timestamped receipt file.
     * 
     * @param order The order to save
     * @throws IOException if file operations fail
     */
    public static void saveReceipt(Order order) throws IOException {
        // Generate unique filename based on current timestamp
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");
        String timestamp = LocalDateTime.now().format(formatter);
        String filename = "receipts/" + timestamp + ".txt";
        
        // Create receipts directory if it doesn't exist
        File directory = new File("receipts");
        if (!directory.exists()) {
            directory.mkdirs();
        }
        
        // Try-with-resources ensures file closes even if exception occurs
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            // Write header
            writer.write("===== DELI ORDER RECEIPT =====\n");
            writer.write("Date: " + LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "\n\n");
            
            // Write each product
            for (Product item : order.getItems()) {
                if (item instanceof Sandwich) {
                    writeSandwich(writer, (Sandwich) item);
                } else if (item instanceof Drink) {
                    writeDrink(writer, (Drink) item);
                } else if (item instanceof Chips) {
                    writeChips(writer, (Chips) item);
                }
                writer.write("\n");
            }
            
            // Write total
            writer.write("==============================\n");
            writer.write(String.format("TOTAL: $%.2f%n", order.getTotalPrice()));
        }
        
        System.out.println("✓ Receipt saved to: " + filename);
    }
    
    /**
     * Helper method to write sandwich details.
     * Private = only used within this class.
     */
    private static void writeSandwich(BufferedWriter writer, Sandwich sandwich) 
            throws IOException {
        writer.write(String.format("%s %s on %s: $%.2f%n",
            sandwich.getSize(), sandwich.getName(), 
            sandwich.getBreadType(), sandwich.getPrice()));
        
        for (Topping topping : sandwich.getToppings()) {
            writer.write(String.format("  + %s ($%.2f)%n",
                topping.getName(), topping.getPrice()));
        }
    }
    
    private static void writeDrink(BufferedWriter writer, Drink drink) 
            throws IOException {
        writer.write(String.format("%s %s: $%.2f%n",
            drink.getSize(), drink.getFlavor(), drink.getPrice()));
    }
    
    private static void writeChips(BufferedWriter writer, Chips chips) 
            throws IOException {
        writer.write(String.format("%s Chips: $%.2f%n",
            chips.getType(), chips.getPrice()));
    }
}
```

**Using it:**
```java
Order order = new Order();
// ... add products ...

try {
    ReceiptFileManager.saveReceipt(order);
} catch (IOException e) {
    System.out.println("Error saving receipt: " + e.getMessage());
}
```

---

## 💡 YOUR TURN: Test File Saving

1. Create several orders with different products
2. Save each order using `ReceiptFileManager`
3. Look in the `receipts/` folder - you should see timestamped files
4. Open them to verify the format

**Challenge:** Add error handling if the directory can't be created.

---

# 🧱 PHASE 11 – USER INTERFACE (PUTTING IT ALL TOGETHER)

## 📖 CONCEPT: Separation of Concerns

**Your teacher emphasizes this pattern:**
- `UserInterface` = handles display + user input
- `Model classes` = handle business logic
- `Util classes` = handle helper functions

**Why?** If you mix everything together, changes become nightmares. Separate = maintainable.

---

## 🔍 EXAMPLE: UserInterface Structure

```java
package com.richie.ui;

import com.richie.model.*;
import com.richie.util.*;
import java.util.Scanner;
import java.io.IOException;

/**
 * Handles ALL user interaction.
 * Business logic lives in model classes, not here!
 */
public class UserInterface {
    private Scanner scanner;
    private Order currentOrder;
    
    public UserInterface() {
        this.scanner = new Scanner(System.in);
        this.currentOrder = new Order();
    }
    
    /**
     * Main display loop - this is the entry point.
     * Called from main() in your Program class.
     */
    public void display() {
        boolean running = true;
        
        while (running) {
            showHomeMenu();
            String choice = scanner.nextLine().trim();
            
            switch (choice) {
                case "1":
                    processNewOrder();
                    break;
                case "2":
                    processViewOrder();
                    break;
                case "0":
                    running = false;
                    System.out.println("Thanks for visiting!");
                    break;
                default:
                    System.out.println("❌ Invalid choice.");
            }
        }
        
        scanner.close();
    }
    
    /**
     * Private helper methods keep display() clean.
     * Notice: each method has ONE job.
     */
    private void showHomeMenu() {
        System.out.println("\n╔════════════════════════════════════╗");
        System.out.println("║         DELI ORDER SYSTEM          ║");
        System.out.println("╚════════════════════════════════════╝");
        System.out.println("1) New Order");
        System.out.println("2) View Current Order");
        System.out.println("0) Exit");
        System.out.print("Choice: ");
    }
    
    /**
     * Handles the "New Order" flow.
     * Notice: delegates to more specific methods.
     */
    private void processNewOrder() {
        currentOrder = new Order();  // Start fresh
        boolean ordering = true;
        
        while (ordering) {
            showOrderMenu();
            String choice = scanner.nextLine().trim();
            
            switch (choice) {
                case "1":
                    processAddSandwich();
                    break;
                case "2":
                    processAddDrink();
                    break;
                case "3":
                    processAddChips();
                    break;
                case "4":
                    processCheckout();
                    ordering = false;  // Done ordering
                    break;
                case "0":
                    System.out.println("Order cancelled.");
                    currentOrder = new Order();  // Reset
                    ordering = false;
                    break;
                default:
                    System.out.println("❌ Invalid choice.");
            }
        }
    }
    
    private void showOrderMenu() {
        System.out.println("\n--- NEW ORDER ---");
        System.out.println("1) Add Sandwich");
        System.out.println("2) Add Drink");
        System.out.println("3) Add Chips");
        System.out.println("4) Checkout");
        System.out.println("0) Cancel Order");
        System.out.print("Choice: ");
    }
    
    /**
     * Notice: each "process" method focuses on ONE thing.
     * This makes code easier to read, test, and maintain.
     */
    private void processAddSandwich() {
        System.out.print("Bread type (White/Wheat/Rye/Wrap): ");
        String bread = scanner.nextLine().trim();
        
        System.out.print("Size (4\"/8\"/12\"): ");
        String size = scanner.nextLine().trim();
        
        Sandwich sandwich = new Sandwich(bread, size);
        
        // Add toppings loop
        boolean addingToppings = true;
        while (addingToppings) {
            System.out.print("Add topping? (y/n): ");
            String response = scanner.nextLine().trim().toLowerCase();
            
            if (response.equals("y")) {
                System.out.print("Topping name: ");
                String name = scanner.nextLine().trim();
                
                System.out.print("Base price: $");
                double price = Double.parseDouble(scanner.nextLine().trim());
                
                System.out.print("Premium? (y/n): ");
                boolean premium = scanner.nextLine().trim().equalsIgnoreCase("y");
                
                sandwich.addTopping(new Topping(name, price, premium));
                System.out.println("✓ Added " + name);
            } else {
                addingToppings = false;
            }
        }
        
        currentOrder.addProduct(sandwich);
        System.out.println("✓ Sandwich added to order!");
    }
    
    private void processAddDrink() {
        System.out.print("Size (Small/Medium/Large): ");
        String size = scanner.nextLine().trim();
        
        System.out.print("Flavor: ");
        String flavor = scanner.nextLine().trim();
        
        currentOrder.addProduct(new Drink(size, flavor));
        System.out.println("✓ Drink added!");
    }
    
    private void processAddChips() {
        System.out.print("Chip type (BBQ/Classic/Sour Cream): ");
        String type = scanner.nextLine().trim();
        
        currentOrder.addProduct(new Chips(type));
        System.out.println("✓ Chips added!");
    }
    
    private void processViewOrder() {
        if (currentOrder.getItems().isEmpty()) {
            System.out.println("⚠️ Your order is empty.");
        } else {
            currentOrder.printReceipt();
        }
    }
    
    private void processCheckout() {
        if (currentOrder.getItems().isEmpty()) {
            System.out.println("⚠️ Cannot checkout - order is empty.");
            return;
        }
        
        System.out.println("\n--- CHECKOUT ---");
        currentOrder.printReceipt();
        
        System.out.print("\nConfirm order? (y/n): ");
        String confirm = scanner.nextLine().trim().toLowerCase();
        
        if (confirm.equals("y")) {
            try {
                ReceiptFileManager.saveReceipt(currentOrder);
                System.out.println("✅ Order complete! Receipt saved.");
                currentOrder = new Order();  // Reset for next customer
            } catch (IOException e) {
                System.out.println("❌ Error saving receipt: " + e.getMessage());
            }
        } else {
            System.out.println("Order not confirmed.");
        }
    }
}
```

**Main program:**
```java
package com.richie;

import com.richie.ui.UserInterface;

public class DeliProgram {
    public static void main(String[] args) {
        UserInterface ui = new UserInterface();
        ui.display();
    }
}
```

**Key patterns from your teacher's style:**
1. ✅ Small methods with clear names
2. ✅ One responsibility per method
3. ✅ Private helpers keep public API clean
4. ✅ Business logic in model, not UI

---

## 💡 YOUR TURN: Add Input Validation

Right now, if someone enters an invalid size, the program continues with bad data.

**Create `InputValidator.java` in util package:**
```java
public class InputValidator {
    
    public static String promptForSandwichSize(Scanner scanner) {
        // Loop until valid input
        // Accept: "4\"", "8\"", "12\""
        // Reject: anything else
    }
    
    public static String promptForDrinkSize(Scanner scanner) {
        // Loop until valid input
        // Accept: "small", "medium", "large" (case insensitive)
    }
    
    public static boolean promptForYesNo(Scanner scanner, String message) {
        // Display message
        // Return true for "y" or "yes", false for "n" or "no"
    }
}
```

**Then use it in UserInterface:**
```java
String size = InputValidator.promptForSandwichSize(scanner);
```

**Why separate?** Reusable. Testable. Clean.

---

# 🎯 FINAL INTEGRATION: YOUR DELI APP

## Your Complete Project Structure

```
deli-pos-system/
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/
│   │           └── richie/
│   │               ├── DeliProgram.java
│   │               ├── model/
│   │               │   ├── Product.java (abstract)
│   │               │   ├── Sandwich.java (extends Product)
│   │               │   ├── SignatureSandwich.java (extends Sandwich)
│   │               │   ├── Drink.java (extends Product)
│   │               │   ├── Chips.java (extends Product)
│   │               │   ├── Topping.java
│   │               │   └── Order.java
│   │               ├── ui/
│   │               │   └── UserInterface.java
│   │               └── util/
│   │                   ├── ReceiptFileManager.java
│   │                   └── InputValidator.java
│   └── test/
│       └── java/
│           └── com/
│               └── richie/
│                   └── model/
│                       ├── ToppingTest.java
│                       ├── SandwichTest.java
│                       ├── DrinkTest.java
│                       ├── OrderTest.java
│                       └── ProductIntegrationTest.java
├── receipts/ (created at runtime)
└── pom.xml
```

---

## 🏆 CAPSTONE 2 READINESS CHECKLIST

Go through this honestly. If you can't do something, go back to that phase.

### Classes & Objects
- [ ] I can create a class with private fields
- [ ] I can write constructors with parameters
- [ ] I can create getters and setters
- [ ] I understand what `this` means
- [ ] I can create derived getters (calculated values)

### Static vs Instance
- [ ] I know when to use `static`
- [ ] I can create a static counter
- [ ] I can create a utility class with static methods
- [ ] I understand the difference between class-level and object-level

### Method Overloading
- [ ] I can overload constructors
- [ ] I can use `this()` to call another constructor
- [ ] I understand method signatures

### Composition
- [ ] I can create objects that contain other objects
- [ ] I can use ArrayList to hold collections
- [ ] I can loop through nested objects
- [ ] I understand "has-a" relationships

### Testing
- [ ] I can write a JUnit test
- [ ] I follow the AAA pattern
- [ ] I can test calculations and logic
- [ ] I run tests before committing code

### Inheritance
- [ ] I can create a child class with `extends`
- [ ] I use `super()` correctly in constructors
- [ ] I can override methods with `@Override`
- [ ] I understand "is-a" relationships

### Polymorphism
- [ ] I can store child objects in parent references
- [ ] I understand how overridden methods are called
- [ ] I can use polymorphic collections

### Abstract Classes
- [ ] I can create an abstract parent class
- [ ] I can define abstract methods
- [ ] I understand why to use abstract vs concrete
- [ ] I can force children to implement methods

### Type Checking
- [ ] I can use `instanceof` to check types
- [ ] I can safely cast to child types
- [ ] I know when to use it (sparingly!)

### File I/O
- [ ] I can write to files with BufferedWriter
- [ ] I use try-with-resources correctly
- [ ] I can create directories if they don't exist
- [ ] I handle IOExceptions properly

### User Interface
- [ ] I can create menu loops
- [ ] I separate UI logic from business logic
- [ ] I break methods into small, focused pieces
- [ ] I validate user input

---

# 🚀 START CAPSTONE 2: 4-DAY PLAN

## Day 1: Foundation (3-4 hours)
**Morning:**
1. Create GitHub repo: `deli-pos-system` (public)
2. Clone to `C:/pluralsight/workshops/`
3. Create Maven project in IntelliJ
4. Set up package structure

**Afternoon:**
1. Build `Product` (abstract), `Sandwich`, `Drink`, `Chips`
2. Build `Topping`
3. Write unit tests for all model classes
4. **Commit after each class!**

**Goal:** All model classes done, tested, committed.

---

## Day 2: Core Features (3-4 hours)
**Morning:**
1. Build `Order` class with composition
2. Test adding products and calculating totals
3. Build `SignatureSandwich` with inheritance

**Afternoon:**
1. Create `UserInterface` skeleton
2. Implement home menu loop
3. Implement "Add Sandwich" flow
4. Test manually

**Goal:** Can create an order with sandwiches through UI.

---

## Day 3: Complete Features (3-4 hours)
**Morning:**
1. Add drinks and chips to UI
2. Implement view order feature
3. Build `ReceiptFileManager`
4. Test file saving

**Afternoon:**
1. Build `InputValidator`
2. Add validation throughout UI
3. Polish menu displays
4. Test full flow multiple times

**Goal:** Complete working app with file persistence.

---

## Day 4: Polish & Document (2-3 hours)
**Morning:**
1. Write integration tests
2. Fix any bugs found
3. Add code comments

**Afternoon:**
1. Create README.md with:
    - Project description
    - Screenshots of menus
    - One interesting code snippet with explanation
2. Record a short demo video (optional but impressive)
3. Final commit and push

**Goal:** Portfolio-ready project.

---

# 📝 STUDYING TIPS BASED ON YOUR TEACHER'S STYLE

Looking at https://github.com/BrightBoost:

## What She Values:
1. **Clean method names** - `processAddSandwich()` not `doStuff()`
2. **Small methods** - each does ONE thing
3. **Package organization** - model, ui, util separation
4. **Tests exist** - she writes them, so should you
5. **Comments explain WHY** - not what (code shows what)

## What You Already Do Well (from your GitHub):
1. ✅ Meaningful variable names
2. ✅ Breaking code into methods
3. ✅ Handling edge cases

## Level Up:
1. **Write tests FIRST** - it clarifies what you're building
2. **Smaller methods** - if a method has 3+ responsibilities, split it
3. **More comments** - especially WHY you made design choices

---

# 🎓 FINAL THOUGHTS

You've got this, Richie. This guide teaches you to THINK in objects, not just copy code.

**Remember:**
- Type everything yourself (muscle memory)
- Test after every change
- Commit often with clear messages
- If stuck for 30 min, take a break or ask for help

**The goal isn't perfection.** It's understanding. Build something that works, learn from it, improve next time.

Your Capstone 2 will prove you understand OOP. Employers will see it. Make it count.

Now close this guide and start coding. Refer back only when stuck.

You've got this! 🚀
    