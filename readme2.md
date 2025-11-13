# 🚀 4-DAY COMPLETE GUIDE: CLI → DEPLOYED WEB APP

**Your Mission:** Transform your Poke CLI into a live, deployed web app

**Timeline:** 4 days (6-8 hours per day)  
**What You Have:** Working Java CLI with OOP ✓  
**What You'll Build:** Full-stack web app on Render ✓

---

## 📅 THE 4-DAY PLAN

**Day 1:** Database fundamentals + JDBC basics (8 hours)  
**Day 2:** REST API with Javalin (8 hours)  
**Day 3:** Simple frontend + local testing (6 hours)  
**Day 4:** Deployment + polish (6 hours)

**Total:** ~28 hours of focused work

---

# 🎯 LEARNING APPROACH

**We'll build in layers:**
1. **Database first** - Understand persistence before web
2. **Test with code** - No frontend until backend works
3. **Simple frontend** - Just enough to work
4. **Deploy early** - Day 4 morning, not last minute

**Each section has:**
- 📖 Concept explanation (the "why")
- 🔍 Example with commentary
- 💡 Your turn (practice)
- ✅ Checkpoint (verify it works)
- 🐛 Common errors (when things break)

---

# 📦 WHAT YOU'LL REUSE

Your existing models work perfectly! Keep these:
- ✅ `Product.java` (abstract class)
- ✅ `PokeBowl.java`
- ✅ `Topping.java`
- ✅ `Drink.java`
- ✅ `Sides.java`
- ✅ `Order.java`
- ✅ `Taxable.java` interface

**Remove these:**
- ❌ `CLI.java` (replaced with web routes)
- ❌ `ReceiptFileManager.java` (replaced with database)

---

# 🏃 DAY 1: DATABASE & JDBC (8 HOURS)

## Hour 1: Setup PostgreSQL

### Why PostgreSQL?
- **Your current approach:** Files (one receipt per order)
    - ❌ Hard to search ("find all orders over $50")
    - ❌ Hard to manage multiple users
    - ❌ No relationships between data

- **With PostgreSQL:**
    - ✅ Query anything instantly
    - ✅ Multiple users, no file conflicts
    - ✅ Foreign keys link related data
    - ✅ Industry standard (used at Spotify, Instagram)

### Install PostgreSQL

**Mac:**
```bash
brew install postgresql@15
brew services start postgresql@15

# Verify
psql --version
# Should show: psql (PostgreSQL) 15.x
```

**Windows:**
1. Download from postgresql.org
2. Install (default settings)
3. **Remember your password!**
4. Add to PATH (installer usually does this)
5. Open Command Prompt and verify: `psql --version`

### Create Your Database

```bash
# Connect to PostgreSQL
psql postgres

# Inside psql:
CREATE DATABASE foodhall;

# Switch to new database
\c foodhall

# Verify you're connected
SELECT current_database();
# Should show: foodhall

# List all databases
\l

# Exit when done
\q
```

**✅ Checkpoint:**
- [ ] PostgreSQL installed and running
- [ ] `foodhall` database created
- [ ] Can connect with `psql foodhall`

**🐛 Common Errors:**
- "command not found: psql" → Not in PATH, restart terminal or use full path
- "could not connect to server" → PostgreSQL not running, check with `brew services list` (Mac)
- "password authentication failed" → Wrong password, reinstall if needed

---

## Hours 2-3: Database Schema Design

### Understanding Tables & Relationships

**Your CLI had this:**
```
Order (in memory)
├── PokeBowl objects
│   └── Topping objects
└── Receipt (file)
```

**Database has this:**
```
orders table
├── order_items table (foreign key to orders)
│   └── order_item_toppings table (foreign key to order_items)
└── toppings table
```

**Key difference:** Data is *normalized* (stored once, referenced many times)

### Create Schema File

Create `schema.sql` in your project root:

```sql
-- ============================================
-- RESTAURANTS TABLE
-- ============================================
-- Stores info about each restaurant in food hall
CREATE TABLE restaurants (
    id SERIAL PRIMARY KEY,              -- Auto-incrementing ID
    name VARCHAR(100) NOT NULL,         -- Restaurant name
    cuisine_type VARCHAR(50),           -- e.g., "Hawaiian"
    description TEXT,                   -- Short description
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================
-- TOPPINGS TABLE  
-- ============================================
-- All available toppings (your Topping class)
CREATE TABLE toppings (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,  -- Must be unique!
    base_price DECIMAL(10, 2) NOT NULL, -- e.g., 2.50
    is_premium BOOLEAN DEFAULT false    -- Premium = 1.5x price
);

-- ============================================
-- ORDERS TABLE
-- ============================================
-- Customer orders (your Order class)
CREATE TABLE orders (
    id SERIAL PRIMARY KEY,
    customer_name VARCHAR(100) NOT NULL,
    subtotal DECIMAL(10, 2) NOT NULL,
    tax DECIMAL(10, 2) NOT NULL,
    total DECIMAL(10, 2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================
-- ORDER_ITEMS TABLE
-- ============================================
-- Items in each order (bowls, drinks, sides)
CREATE TABLE order_items (
    id SERIAL PRIMARY KEY,
    order_id INTEGER NOT NULL,          -- Which order?
    item_type VARCHAR(20) NOT NULL,     -- 'bowl', 'drink', 'side'
    item_name VARCHAR(100),             -- Custom name
    base VARCHAR(50),                   -- For bowls: rice type
    size VARCHAR(10),                   -- S, M, L
    flavor VARCHAR(100),                -- For drinks
    side_type VARCHAR(100),             -- For sides
    price DECIMAL(10, 2) NOT NULL,      -- Price at time of order
    
    -- Foreign key: links to orders table
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE
    -- CASCADE means: if order deleted, delete these too
);

-- ============================================
-- ORDER_ITEM_TOPPINGS TABLE
-- ============================================
-- Which toppings on which bowls
CREATE TABLE order_item_toppings (
    id SERIAL PRIMARY KEY,
    order_item_id INTEGER NOT NULL,     -- Which bowl?
    topping_id INTEGER NOT NULL,        -- Which topping?
    price DECIMAL(10, 2) NOT NULL,      -- Price snapshot
    
    FOREIGN KEY (order_item_id) REFERENCES order_items(id) ON DELETE CASCADE,
    FOREIGN KEY (topping_id) REFERENCES toppings(id)
);

-- ============================================
-- SEED DATA
-- ============================================

-- Your restaurant
INSERT INTO restaurants (name, cuisine_type, description) 
VALUES ('Funkin'' Poke', 'Hawaiian', 'Fresh poke bowls with premium toppings');

-- All your toppings (from your existing code)
INSERT INTO toppings (name, base_price, is_premium) VALUES
-- Premium proteins
('Salmon', 2.00, true),
('Tuna', 2.25, true),
('Spicy Tuna', 2.50, true),
('Spicy Salmon', 2.50, true),
('Shrimp', 1.75, true),
('Tofu', 1.00, true),
('Crab Mix', 1.25, true),
('Avocado', 1.00, true),
-- Regular toppings
('Seaweed Salad', 0.75, false),
('Cucumber', 0.25, false),
('Mango', 0.50, false),
('Green Onion', 0.25, false),
('Masago', 0.75, false),
('Pickled Ginger', 0.25, false),
('Jalapeño', 0.25, false),
('Nori', 0.10, false),
-- Sauces
('Spicy Mayo', 0.50, false),
('Eel Sauce', 0.50, false),
('Ponzu Sauce', 0.50, false),
('Sesame Oil', 0.25, false),
('Yuzu Dressing', 0.50, false),
('Wasabi Aioli', 0.75, false),
('Sriracha', 0.25, false),
-- Toppings
('Furikake', 0.25, false),
('Crispy Onions', 0.50, false),
('Tempura Flakes', 0.50, false);
```

### Load Schema into Database

```bash
# Make sure you're in project root
psql foodhall < schema.sql

# Verify tables were created
psql foodhall
\dt  # List all tables
\d toppings  # Describe toppings table
```

**You should see:**
```
 Schema |        Name           | Type  |  Owner   
--------+-----------------------+-------+----------
 public | order_item_toppings   | table | postgres
 public | order_items           | table | postgres
 public | orders                | table | postgres
 public | restaurants           | table | postgres
 public | toppings              | table | postgres
```

### Test Your Data

```sql
-- In psql:

-- See all toppings
SELECT name, base_price, is_premium FROM toppings;

-- Count toppings
SELECT COUNT(*) FROM toppings;  -- Should be 26

-- Premium toppings only
SELECT name, base_price FROM toppings WHERE is_premium = true;

-- Exit
\q
```

**✅ Checkpoint:**
- [ ] `schema.sql` file created
- [ ] 5 tables created in database
- [ ] 1 restaurant inserted
- [ ] 26 toppings inserted
- [ ] Can query data with SQL

**🐛 Common Errors:**
- "syntax error" → Check for typos in SQL
- "already exists" → Tables already created, drop them first: `DROP TABLE table_name CASCADE;`
- "permission denied" → User doesn't have permissions, use `sudo -u postgres psql`

---

## Hours 3-5: JDBC Fundamentals

### What is JDBC?

**JDBC = Java Database Connectivity**

**You know this pattern:**
```java
// Reading a file
BufferedReader reader = new BufferedReader(new FileReader("data.txt"));
String line = reader.readLine();
reader.close();
```

**JDBC is similar:**
```java
// Reading from database
Connection conn = DriverManager.getConnection(url, user, password);
Statement stmt = conn.createStatement();
ResultSet rs = stmt.executeQuery("SELECT * FROM toppings");
conn.close();
```

### The JDBC Workflow

```
1. Connect to database → Connection
2. Prepare SQL query → Statement/PreparedStatement  
3. Execute query → ResultSet
4. Process results → while (rs.next())
5. Close everything → try-with-resources
```

### Update pom.xml

Add PostgreSQL driver:

```xml
<dependencies>
    <!-- Existing JUnit -->
    
    <!-- PostgreSQL JDBC Driver -->
    <dependency>
        <groupId>org.postgresql</groupId>
        <artifactId>postgresql</artifactId>
        <version>42.6.0</version>
    </dependency>
</dependencies>
```

### Create Database Connection Class

**Create package:** `com.richie.dao`

**Create file:** `DatabaseConnection.java`

```java
package com.richie.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Manages database connections.
 * Similar to how you opened files with BufferedReader.
 */
public class DatabaseConnection {
    
    // JDBC URL format: jdbc:postgresql://host:port/database_name
    private static final String URL = "jdbc:postgresql://localhost:5432/foodhall";
    private static final String USER = "postgres";  // or your username
    private static final String PASSWORD = "yourpassword";  // ⚠️ CHANGE THIS!
    
    /**
     * Get a connection to the database.
     * This is like opening a file - you must close it when done!
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
    
    /**
     * Test the connection.
     */
    public static void main(String[] args) {
        try (Connection conn = getConnection()) {
            System.out.println("✅ Connected to: " + conn.getCatalog());
        } catch (SQLException e) {
            System.out.println("❌ Connection failed!");
            e.printStackTrace();
        }
    }
}
```

**Test it:**
```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="com.richie.dao.DatabaseConnection"
```

**Should see:** `✅ Connected to: foodhall`

**✅ Checkpoint:**
- [ ] PostgreSQL dependency added
- [ ] DatabaseConnection class created
- [ ] Test connection works
- [ ] No SQLException

**🐛 Common Errors:**
- "No suitable driver" → Maven didn't download PostgreSQL jar, run `mvn clean install`
- "Connection refused" → PostgreSQL not running
- "authentication failed" → Wrong password in DatabaseConnection
- "database does not exist" → Typo in database name

---

### Understanding Try-With-Resources

**You know try-catch:**
```java
try {
    // risky code
} catch (Exception e) {
    // handle error
}
```

**Try-with-resources AUTO-CLOSES:**
```java
try (Connection conn = getConnection()) {
    // Use conn
    // Automatically closed when block ends!
} catch (SQLException e) {
    // Handle error
}
```

**Why?** Database connections are expensive. Must close them or you'll run out!

**Old way (don't do this):**
```java
Connection conn = null;
try {
    conn = getConnection();
    // use conn
} catch (SQLException e) {
    e.printStackTrace();
} finally {
    if (conn != null) {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
```

**New way (do this):**
```java
try (Connection conn = getConnection()) {
    // use conn
    // auto-closed!
} catch (SQLException e) {
    e.printStackTrace();
}
```

---

### Statement vs PreparedStatement

**Statement (simple, but DANGEROUS with user input):**
```java
String name = "Salmon";
Statement stmt = conn.createStatement();
ResultSet rs = stmt.executeQuery("SELECT * FROM toppings WHERE name = '" + name + "'");
// ⚠️ SQL injection risk if name comes from user!
```

**PreparedStatement (safe, prevents SQL injection):**
```java
String name = "Salmon";
PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM toppings WHERE name = ?");
pstmt.setString(1, name);  // Replace ? with value
ResultSet rs = pstmt.executeQuery();
// ✅ Safe! Escaped automatically
```

**Always use PreparedStatement when you have variables!**

---

### Reading Results with ResultSet

**ResultSet = cursor over query results**

```java
ResultSet rs = stmt.executeQuery("SELECT name, price FROM toppings");

// Loop through results
while (rs.next()) {  // Move to next row
    String name = rs.getString("name");      // Get column by name
    double price = rs.getDouble("price");    // Get as double
    System.out.println(name + ": $" + price);
}
```

**Like this:**
```
ResultSet cursor:
Row 1: [Salmon, 2.00] ← rs.next() moves here first
Row 2: [Tuna, 2.25]
Row 3: [Avocado, 1.00]
```

---

### 💡 YOUR TURN: Read All Toppings

Create `TestJDBC.java` in `com.richie.dao`:

```java
package com.richie.dao;

import java.sql.*;

public class TestJDBC {
    public static void main(String[] args) {
        // TODO: Get connection using try-with-resources
        // TODO: Create statement
        // TODO: Execute query: SELECT name, base_price, is_premium FROM toppings
        // TODO: Loop through results and print each topping
        
        // Expected output:
        // Salmon: $2.00 (Premium)
        // Tuna: $2.25 (Premium)
        // ...
    }
}
```

**Solution (don't peek until you try!):**

<details>
<summary>Click to reveal solution</summary>

```java
package com.richie.dao;

import java.sql.*;

public class TestJDBC {
    public static void main(String[] args) {
        String sql = "SELECT name, base_price, is_premium FROM toppings";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            System.out.println("=== ALL TOPPINGS ===");
            while (rs.next()) {
                String name = rs.getString("name");
                double price = rs.getDouble("base_price");
                boolean premium = rs.getBoolean("is_premium");
                
                String type = premium ? "(Premium)" : "";
                System.out.printf("%s: $%.2f %s\n", name, price, type);
            }
            
        } catch (SQLException e) {
            System.out.println("Error reading toppings!");
            e.printStackTrace();
        }
    }
}
```

Run it:
```bash
mvn exec:java -Dexec.mainClass="com.richie.dao.TestJDBC"
```

</details>

**✅ Checkpoint:**
- [ ] Can read all toppings from database
- [ ] Understand try-with-resources
- [ ] Understand ResultSet cursor

---

## Hours 5-7: DAO Pattern (Data Access Objects)

### What is DAO Pattern?

**Problem:** Mixing database code with business logic is messy:
```java
// Bad: Order class doing database work
public class Order {
    public void save() {
        Connection conn = getConnection();
        // SQL code here...
    }
}
```

**Solution:** Separate classes for database access:
```java
// Good: Separate concerns
OrderDAO dao = new OrderDAO();
dao.saveOrder(order);  // Order doesn't know about database!
```

**DAO = Data Access Object** - handles all database operations for one entity.

---

### Create ToppingDAO

**Create file:** `ToppingDAO.java` in `com.richie.dao`

```java
package com.richie.dao;

import com.richie.model.Topping;
import java.sql.*;
import java.util.ArrayList;

/**
 * Data Access Object for Topping.
 * Handles all database operations for toppings.
 */
public class ToppingDAO {
    
    /**
     * Get all toppings from database.
     * Similar to your CLI loading toppings from a list.
     */
    public ArrayList<Topping> getAllToppings() {
        ArrayList<Topping> toppings = new ArrayList<>();
        String sql = "SELECT name, base_price, is_premium FROM toppings ORDER BY name";
        
        // Try-with-resources: auto-closes conn, stmt, rs
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            // Loop through results
            while (rs.next()) {
                // Create Topping object from database row
                Topping t = new Topping(
                    rs.getString("name"),
                    rs.getDouble("base_price"),
                    rs.getBoolean("is_premium")
                );
                toppings.add(t);
            }
            
            System.out.println("✅ Loaded " + toppings.size() + " toppings");
            
        } catch (SQLException e) {
            System.out.println("❌ Error loading toppings!");
            e.printStackTrace();
        }
        
        return toppings;
    }
    
    /**
     * Get one topping by name.
     * Uses PreparedStatement to prevent SQL injection.
     */
    public Topping getToppingByName(String name) {
        // Use ? placeholder for safety
        String sql = "SELECT name, base_price, is_premium FROM toppings WHERE name = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            // Replace ? with actual value (escaped automatically)
            pstmt.setString(1, name);
            
            ResultSet rs = pstmt.executeQuery();
            
            // If found, return it
            if (rs.next()) {
                return new Topping(
                    rs.getString("name"),
                    rs.getDouble("base_price"),
                    rs.getBoolean("is_premium")
                );
            }
            
        } catch (SQLException e) {
            System.out.println("❌ Error finding topping: " + name);
            e.printStackTrace();
        }
        
        // Not found
        return null;
    }
    
    /**
     * Get topping ID (for saving orders).
     * Returns -1 if not found.
     */
    public int getToppingId(String name) {
        String sql = "SELECT id FROM toppings WHERE name = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("id");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return -1;  // Not found
    }
    
    /**
     * Test the DAO.
     */
    public static void main(String[] args) {
        ToppingDAO dao = new ToppingDAO();
        
        // Test 1: Get all
        System.out.println("\n=== TEST 1: Get All Toppings ===");
        ArrayList<Topping> all = dao.getAllToppings();
        System.out.println("Found " + all.size() + " toppings");
        
        // Test 2: Get one
        System.out.println("\n=== TEST 2: Get Salmon ===");
        Topping salmon = dao.getToppingByName("Salmon");
        if (salmon != null) {
            System.out.println(salmon.getName() + ": $" + salmon.getPrice());
        }
        
        // Test 3: Get ID
        System.out.println("\n=== TEST 3: Get ID for Salmon ===");
        int id = dao.getToppingId("Salmon");
        System.out.println("Salmon ID: " + id);
    }
}
```

**Test it:**
```bash
mvn exec:java -Dexec.mainClass="com.richie.dao.ToppingDAO"
```

**Should see:**
```
✅ Loaded 26 toppings
Found 26 toppings

Salmon: $3.0  (because premium = base * 1.5)

Salmon ID: 1
```

**✅ Checkpoint:**
- [ ] ToppingDAO created
- [ ] Can get all toppings
- [ ] Can get topping by name
- [ ] Can get topping ID
- [ ] All tests pass

---

### Create OrderDAO (Simplified Version)

**This is complex, so we'll build it in steps.**

**Step 1: Save just the order (no items yet)**

```java
package com.richie.dao;

import com.richie.model.Order;
import java.sql.*;

public class OrderDAO {
    
    /**
     * Save order to database (Step 1: just the order row).
     * Returns the generated order ID, or -1 if failed.
     */
    public int saveOrderBasic(Order order) {
        // RETURNING id gives us back the generated ID
        String sql = "INSERT INTO orders (customer_name, subtotal, tax, total) " +
                     "VALUES (?, ?, ?, ?) RETURNING id";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            // Set values for the ? placeholders
            pstmt.setString(1, order.getName());            // customer_name
            pstmt.setDouble(2, order.getSubtotal());        // subtotal
            pstmt.setDouble(3, order.getTax());             // tax
            pstmt.setDouble(4, order.getTotal());           // total
            
            // Execute and get result
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                int orderId = rs.getInt("id");
                System.out.println("✅ Order saved with ID: " + orderId);
                return orderId;
            }
            
        } catch (SQLException e) {
            System.out.println("❌ Error saving order!");
            e.printStackTrace();
        }
        
        return -1;  // Failed
    }
    
    /**
     * Test basic save.
     */
    public static void main(String[] args) {
        OrderDAO dao = new OrderDAO();
        
        // Create test order
        Order order = new Order("Test Customer");
        // Don't add items yet - just testing order save
        
        int id = dao.saveOrderBasic(order);
        System.out.println("Saved order ID: " + id);
        
        // Verify in database
        System.out.println("\nCheck database:");
        System.out.println("psql foodhall -c \"SELECT * FROM orders\"");
    }
}
```

**Test it:**
```bash
mvn exec:java -Dexec.mainClass="com.richie.dao.OrderDAO"

# Then verify in database:
psql foodhall -c "SELECT * FROM orders"
```

**You should see your order in the database!**

**✅ Checkpoint:**
- [ ] OrderDAO created
- [ ] Can save order to database
- [ ] Order appears in `orders` table
- [ ] Returns correct order ID

---

## Hour 7-8: Complete OrderDAO with Transactions

### Understanding Database Transactions

**Problem:** What if you save the order, but saving items fails?
```
1. Insert order ✅
2. Insert item 1 ✅
3. Insert item 2 ❌ ERROR!
```

Now you have an order with only 1 item - data is inconsistent!

**Solution: Transactions** - all or nothing:
```java
conn.setAutoCommit(false);  // Start transaction
try {
    // Insert order
    // Insert all items
    // Insert all toppings
    conn.commit();  // Success - save everything!
} catch (Exception e) {
    conn.rollback();  // Error - undo everything!
}
```

### Complete OrderDAO Implementation

**Update `OrderDAO.java`:**

```java
package com.richie.dao;

import com.richie.model.*;
import java.sql.*;

public class OrderDAO {
    
    private ToppingDAO toppingDAO = new ToppingDAO();
    
    /**
     * Save complete order with items and toppings.
     * Uses transaction to ensure all-or-nothing.
     */
    public int saveOrder(Order order) {
        // We need THREE SQL statements:
        String orderSql = "INSERT INTO orders (customer_name, subtotal, tax, total) " +
                         "VALUES (?, ?, ?, ?) RETURNING id";
        
        String itemSql = "INSERT INTO order_items " +
                        "(order_id, item_type, item_name, base, size, price) " +
                        "VALUES (?, ?, ?, ?, ?, ?) RETURNING id";
        
        String toppingSql = "INSERT INTO order_item_toppings " +
                           "(order_item_id, topping_id, price) VALUES (?, ?, ?)";
        
        // We DON'T use try-with-resources here because we need to control commit/rollback
        Connection conn = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);  // 🔥 START TRANSACTION
            
            int orderId;
            
            // ===== STEP 1: Save order =====
            try (PreparedStatement pstmt = conn.prepareStatement(orderSql)) {
                pstmt.setString(1, order.getName());
                pstmt.setDouble(2, order.getSubtotal());
                pstmt.setDouble(3, order.getTax());
                pstmt.setDouble(4, order.getTotal());
                
                ResultSet rs = pstmt.executeQuery();
                rs.next();
                orderId = rs.getInt("id");
                System.out.println("✅ Order saved: ID=" + orderId);
            }
            
            // ===== STEP 2: Save items =====
            for (Product product : order.getItems()) {
                int itemId;
                
                try (PreparedStatement pstmt = conn.prepareStatement(itemSql)) {
                    pstmt.setInt(1, orderId);
                    
                    // Set fields based on product type
                    if (product instanceof PokeBowl) {
                        PokeBowl bowl = (PokeBowl) product;
                        pstmt.setString(2, "bowl");
                        pstmt.setString(3, bowl.getName());
                        pstmt.setString(4, bowl.getBase());
                        pstmt.setString(5, bowl.getSize());
                    } else if (product instanceof Drink) {
                        Drink drink = (Drink) product;
                        pstmt.setString(2, "drink");
                        pstmt.setString(3, drink.getFlavor());
                        pstmt.setNull(4, Types.VARCHAR);
                        pstmt.setString(5, drink.getSize());
                    } else if (product instanceof Sides) {
                        Sides side = (Sides) product;
                        pstmt.setString(2, "side");
                        pstmt.setString(3, side.getType());
                        pstmt.setNull(4, Types.VARCHAR);
                        pstmt.setNull(5, Types.VARCHAR);
                    }
                    
                    pstmt.setDouble(6, product.getPrice());
                    
                    ResultSet rs = pstmt.executeQuery();
                    rs.next();
                    itemId = rs.getInt("id");
                    System.out.println("  ✅ Item saved: ID=" + itemId);
                }
                
                // ===== STEP 3: Save toppings (if bowl) =====
                if (product instanceof PokeBowl) {
                    PokeBowl bowl = (PokeBowl) product;
                    
                    try (PreparedStatement pstmt = conn.prepareStatement(toppingSql)) {
                        for (Topping topping : bowl.getToppings()) {
                            int toppingId = toppingDAO.getToppingId(topping.getName());
                            
                            if (toppingId == -1) {
                                throw new SQLException("Topping not found: " + topping.getName());
                            }
                            
                            pstmt.setInt(1, itemId);
                            pstmt.setInt(2, toppingId);
                            pstmt.setDouble(3, topping.getPrice());
                            pstmt.executeUpdate();
                            System.out.println("    ✅ Topping added: " + topping.getName());
                        }
                    }
                }
            }
            
            conn.commit();  // 🎉 SUCCESS - Save everything!
            System.out.println("✅✅✅ Transaction committed!");
            return orderId;
            
        } catch (SQLException e) {
            System.out.println("❌ Error saving order!");
            e.printStackTrace();
            
            // Undo everything
            if (conn != null) {
                try {
                    conn.rollback();  // 🔄 UNDO all changes
                    System.out.println("🔄 Transaction rolled back");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            
            return -1;
            
        } finally {
            // Close connection
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    /**
     * Test complete order save.
     */
    public static void main(String[] args) {
        OrderDAO dao = new OrderDAO();
        ToppingDAO toppingDAO = new ToppingDAO();
        
        // Create test order
        Order order = new Order("Richie Test");
        
        // Add bowl with toppings
        PokeBowl bowl = new PokeBowl("Test Bowl", "White Rice", "L");
        bowl.addTopping(toppingDAO.getToppingByName("Salmon"));
        bowl.addTopping(toppingDAO.getToppingByName("Avocado"));
        bowl.addTopping(toppingDAO.getToppingByName("Spicy Mayo"));
        order.addItem(bowl);
        
        // Add drink
        Drink drink = new Drink("M", "Green Tea");
        order.addItem(drink);
        
        // Add side
        Sides side = new Sides("Edamame");
        order.addItem(side);
        
        // Save it
        System.out.println("\n=== Saving Order ===");
        int orderId = dao.saveOrder(order);
        
        if (orderId > 0) {
            System.out.println("\n✅ SUCCESS! Order ID: " + orderId);
            System.out.println("Total: $" + order.getTotal());
            
            System.out.println("\n📊 Check database:");
            System.out.println("psql foodhall -c \"SELECT * FROM orders WHERE id = " + orderId + "\"");
            System.out.println("psql foodhall -c \"SELECT * FROM order_items WHERE order_id = " + orderId + "\"");
        }
    }
}
```

**Test it:**
```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="com.richie.dao.OrderDAO"
```

**You should see:**
```
✅ Order saved: ID=1
  ✅ Item saved: ID=1
    ✅ Topping added: Salmon
    ✅ Topping added: Avocado
    ✅ Topping added: Spicy Mayo
  ✅ Item saved: ID=2
  ✅ Item saved: ID=3
✅✅✅ Transaction committed!

✅ SUCCESS! Order ID: 1
Total: $29.85
```

**Verify in database:**
```bash
psql foodhall -c "SELECT * FROM orders"
psql foodhall -c "SELECT * FROM order_items"
psql foodhall -c "SELECT * FROM order_item_toppings"
```

**✅ MASSIVE CHECKPOINT - DAY 1 COMPLETE!**
- [ ] PostgreSQL installed and working
- [ ] Database schema created
- [ ] JDBC basics understood
- [ ] ToppingDAO working
- [ ] OrderDAO saves complete orders with transactions
- [ ] Can verify data in database

**🎉 You now have a working persistence layer!**

**🐛 Common Errors:**
- "column does not exist" → Check your schema matches the INSERT statements
- "foreign key violation" → Order of inserts is wrong, or referenced ID doesn't exist
- "transaction aborted" → Check what caused the error in stack trace
- "connection closed" → Don't close connection before commit

---

**STOP HERE FOR DAY 1**

Tomorrow: Build REST API with Javalin!

---

# 🏃 DAY 2: REST API WITH JAVALIN (8 HOURS)

## Hour 1: Javalin Setup & Concepts

### What is Javalin?

**You know Flask (Python):**
```python
@app.route('/hello')
def hello():
    return 'Hello World'
```

**Javalin is the Java equivalent:**
```java
app.get("/hello", ctx -> {
    ctx.result("Hello World");
});
```

**Why Javalin?**
- ✅ Lightweight (200kb)
- ✅ Simple API (like Flask!)
- ✅ Built for REST APIs
- ✅ Works with your existing code

### Add Dependencies

**Update `pom.xml`:**

```xml
<dependencies>
    <!-- Existing: JUnit, PostgreSQL -->
    
    <!-- Javalin Web Framework -->
    <dependency>
        <groupId>io.javalin</groupId>
        <artifactId>javalin</artifactId>
        <version>5.6.3</version>
    </dependency>
    
    <!-- JSON Processing (Javalin needs this) -->
    <dependency>
        <groupId>com.fasterxml.jackson.core</groupId>
        <artifactId>jackson-databind</artifactId>
        <version>2.15.2</version>
    </dependency>
    
    <!-- Logging (Javalin needs this) -->
    <dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>slf4j-simple</artifactId>
        <version>2.0.9</version>
    </dependency>
</dependencies>
```

**Run:**
```bash
mvn clean install
```

### Your First Javalin Server

**Create package:** `com.richie.web`

**Create file:** `Main.java`

```java
package com.richie.web;

import io.javalin.Javalin;

public class Main {
    public static void main(String[] args) {
        // Create Javalin app (like Flask app)
        Javalin app = Javalin.create().start(7000);
        
        // Define routes
        app.get("/", ctx -> {
            ctx.result("🍱 Welcome to Funkin' Poke API!");
        });
        
        app.get("/hello/{name}", ctx -> {
            String name = ctx.pathParam("name");
            ctx.result("Hello, " + name + "!");
        });
        
        System.out.println("🚀 Server running on http://localhost:7000");
    }
}
```

**Run it:**
```bash
mvn exec:java -Dexec.mainClass="com.richie.web.Main"
```

**Test it:**
```bash
# In another terminal
curl http://localhost:7000/
curl http://localhost:7000/hello/Richie
```

**Or open in browser:** `http://localhost:7000`

**✅ Checkpoint:**
- [ ] Javalin dependencies added
- [ ] Server starts on port 7000
- [ ] Can access routes in browser/curl
- [ ] No errors in console

**🐛 Common Errors:**
- "Port already in use" → Something else on 7000, change to 7001
- "ClassNotFoundException: Javalin" → Run `mvn clean install`
- "SLF4J warnings" → Ignore them or add slf4j-simple

---

## Hours 2-3: Understanding REST & HTTP

### What is REST?

**REST = Representational State Transfer**

**Translation:** "Use HTTP like it was designed"

**HTTP Methods:**
- `GET` - Read data (like your query operations)
- `POST` - Create data (like saving orders)
- `PUT` - Update data
- `DELETE` - Delete data

### REST API Design

**Your CLI flow:**
```
1. User picks topping → CLI shows toppings list
2. User builds bowl → CLI adds to order
3. User checks out → CLI saves to file
```

**REST API flow:**
```
1. GET /api/toppings → Returns JSON array
2. POST /api/orders → Accepts JSON, saves to DB
3. GET /api/orders/{id} → Returns order details
```

### Understanding Context (ctx)

**The `ctx` object has everything:**

```java
app.get("/example", ctx -> {
    // REQUEST data
    String pathParam = ctx.pathParam("id");        // /example/123
    String queryParam = ctx.queryParam("sort");    // /example?sort=asc
    String body = ctx.body();                      // POST/PUT body
    
    // RESPONSE methods
    ctx.result("Plain text");                      // Plain text response
    ctx.json(object);                              // JSON response
    ctx.status(201);                               // HTTP status code
    ctx.status(404).result("Not found");          // Status + message
});
```

### HTTP Status Codes You Need

```
200 OK - Success (GET)
201 Created - Success (POST)
400 Bad Request - Invalid input
404 Not Found - Resource doesn't exist
500 Internal Server Error - Server crashed
```

**Example:**
```java
app.post("/orders", ctx -> {
    try {
        // Try to save
        int id = dao.saveOrder(order);
        
        if (id > 0) {
            ctx.status(201);  // Created!
            ctx.json(Map.of("id", id));
        } else {
            ctx.status(500);  // Server error
            ctx.result("Failed to save");
        }
    } catch (Exception e) {
        ctx.status(400);  // Bad request
        ctx.result("Invalid data");
    }
});
```

---

## Hours 3-5: Build Toppings API

### GET /api/toppings

**Update `Main.java`:**

```java
package com.richie.web;

import io.javalin.Javalin;
import com.richie.dao.ToppingDAO;
import com.richie.model.Topping;

import java.util.ArrayList;

public class Main {
    
    private static ToppingDAO toppingDAO = new ToppingDAO();
    
    public static void main(String[] args) {
        Javalin app = Javalin.create().start(7000);
        
        // ===== HOME =====
        app.get("/", ctx -> {
            ctx.result("🍱 Funkin' Poke API - Use /api/toppings");
        });
        
        // ===== GET ALL TOPPINGS =====
        app.get("/api/toppings", ctx -> {
            try {
                ArrayList<Topping> toppings = toppingDAO.getAllToppings();
                
                // Javalin automatically converts to JSON!
                ctx.json(toppings);
                
            } catch (Exception e) {
                ctx.status(500);
                ctx.result("Error loading toppings");
                e.printStackTrace();
            }
        });
        
        System.out.println("🚀 Server running on http://localhost:7000");
    }
}
```

**Test it:**
```bash
mvn exec:java -Dexec.mainClass="com.richie.web.Main"

# In another terminal:
curl http://localhost:7000/api/toppings
```

**You should see JSON output:**
```json
[
  {
    "name": "Salmon",
    "price": 3.0,
    "premium": true
  },
  {
    "name": "Avocado",
    "price": 1.5,
    "premium": true
  }
  // ... all toppings
]
```

**✅ Checkpoint:**
- [ ] `/api/toppings` returns JSON
- [ ] All toppings visible
- [ ] Premium prices calculated correctly

**🐛 Common Errors:**
- "Cannot serialize Topping" → Make sure Topping has getters
- "Empty array" → Check database has data
- "500 error" → Check server logs for SQLException

---

## Hours 5-7: Build Orders API

### Understanding Request Bodies

**When client sends data:**
```json
{
  "customerName": "Richie",
  "bowls": [
    {
      "name": "Custom Bowl",
      "base": "White Rice",
      "size": "L",
      "toppings": ["Salmon", "Avocado"]
    }
  ]
}
```

**We need classes to receive this data:**

```java
// Javalin will parse JSON into these classes
public class OrderRequest {
    public String customerName;
    public List<BowlRequest> bowls;
}

public class BowlRequest {
    public String name;
    public String base;
    public String size;
    public List<String> toppings;
}
```

### Create Request Classes

**Add to `Main.java` (as nested classes):**

```java
public class Main {
    
    // ... existing code ...
    
    // ===== REQUEST/RESPONSE CLASSES =====
    
    public static class OrderRequest {
        public String customerName;
        public ArrayList<BowlRequest> bowls = new ArrayList<>();
        public ArrayList<DrinkRequest> drinks = new ArrayList<>();
        public ArrayList<SideRequest> sides = new ArrayList<>();
    }
    
    public static class BowlRequest {
        public String name;
        public String base;
        public String size;  // S, M, L
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
```

### POST /api/orders

**Add after toppings route:**

```java
// ===== CREATE ORDER =====
app.post("/api/orders", ctx -> {
    try {
        // Parse JSON request body
        OrderRequest request = ctx.bodyAsClass(OrderRequest.class);
        
        // Validate
        if (request.customerName == null || request.customerName.trim().isEmpty()) {
            ctx.status(400);
            ctx.result("Customer name required");
            return;
        }
        
        if (request.bowls.isEmpty() && request.drinks.isEmpty() && request.sides.isEmpty()) {
            ctx.status(400);
            ctx.result("Order must have at least one item");
            return;
        }
        
        // Build Order object
        Order order = new Order(request.customerName);
        
        // Add bowls
        for (BowlRequest bowlReq : request.bowls) {
            PokeBowl bowl = new PokeBowl(
                bowlReq.name,
                bowlReq.base,
                bowlReq.size
            );
            
            // Add toppings
            for (String toppingName : bowlReq.toppings) {
                Topping topping = toppingDAO.getToppingByName(toppingName);
                if (topping != null) {
                    bowl.addTopping(topping);
                } else {
                    System.out.println("⚠️  Topping not found: " + toppingName);
                }
            }
            
            order.addItem(bowl);
        }
        
        // Add drinks
        for (DrinkRequest drinkReq : request.drinks) {
            Drink drink = new Drink(drinkReq.size, drinkReq.flavor);
            order.addItem(drink);
        }
        
        // Add sides
        for (SideRequest sideReq : request.sides) {
            Sides side = new Sides(sideReq.type);
            order.addItem(side);
        }
        
        // Save to database
        int orderId = orderDAO.saveOrder(order);
        
        if (orderId > 0) {
            // Success!
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("orderId", orderId);
            response.put("customerName", order.getName());
            response.put("subtotal", order.getSubtotal());
            response.put("tax", order.getTax());
            response.put("total", order.getTotal());
            
            ctx.status(201);  // Created
            ctx.json(response);
        } else {
            ctx.status(500);
            ctx.result("Failed to save order");
        }
        
    } catch (Exception e) {
        ctx.status(400);
        ctx.result("Invalid request: " + e.getMessage());
        e.printStackTrace();
    }
});
```

**Test with curl:**

```bash
# Start server
mvn exec:java -Dexec.mainClass="com.richie.web.Main"

# In another terminal, create order
curl -X POST http://localhost:7000/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "customerName": "Richie",
    "bowls": [{
      "name": "Test Bowl",
      "base": "White Rice",
      "size": "L",
      "toppings": ["Salmon", "Avocado", "Spicy Mayo"]
    }],
    "drinks": [{
      "size": "M",
      "flavor": "Green Tea"
    }],
    "sides": [{
      "type": "Edamame"
    }]
  }'
```

**Expected response:**
```json
{
  "success": true,
  "orderId": 2,
  "customerName": "Richie",
  "subtotal": 25.35,
  "tax": 1.77,
  "total": 27.12
}
```

**Verify in database:**
```bash
psql foodhall -c "SELECT * FROM orders ORDER BY id DESC LIMIT 1"
```

**✅ MASSIVE CHECKPOINT - DAY 2 COMPLETE!**
- [ ] Javalin server works
- [ ] GET /api/toppings returns JSON
- [ ] POST /api/orders accepts JSON
- [ ] Orders save to database
- [ ] Can test with curl
- [ ] Database has real orders

**🎉 You now have a working REST API!**

---

**STOP HERE FOR DAY 2**

Tomorrow: Build a simple frontend!

---

# 🏃 DAY 3: SIMPLE FRONTEND (6 HOURS)

## Hour 1: Setup Static Files

### How Javalin Serves HTML/CSS/JS

**Javalin can serve files from:** `src/main/resources/public/`

**Update Main.java to enable static files:**

```java
import io.javalin.http.staticfiles.Location;

public class Main {
    public static void main(String[] args) {
        Javalin app = Javalin.create(config -> {
            // Serve static files from resources/public
            config.staticFiles.add("/public", Location.CLASSPATH);
        }).start(7000);
        
        // ... rest of your routes ...
    }
}
```

### Create Directory Structure

```bash
mkdir -p src/main/resources/public
cd src/main/resources/public
```

**You'll create 3 files here:**
- `index.html` - Structure
- `style.css` - Styling
- `app.js` - Logic

---

## Hours 2-4: Build Minimal Frontend

### index.html (Keep It Simple!)

**Create `src/main/resources/public/index.html`:**

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Funkin' Poke - Order Now</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
    <div class="container">
        <!-- HEADER -->
        <header>
            <h1>🍱 Funkin' Poke</h1>
            <p>Build Your Perfect Bowl</p>
        </header>

        <!-- MAIN CONTENT -->
        <main>
            <!-- Step 1: Customer Name -->
            <section class="section">
                <h2>1. Your Name</h2>
                <input type="text" id="customerName" placeholder="Enter your name">
            </section>

            <!-- Step 2: Build Bowl -->
            <section class="section">
                <h2>2. Build Your Bowl</h2>
                
                <label>Bowl Name</label>
                <input type="text" id="bowlName" placeholder="e.g., Richie's Special">
                
                <label>Base</label>
                <select id="bowlBase">
                    <option>White Rice</option>
                    <option>Brown Rice</option>
                    <option>Mixed Greens</option>
                </select>
                
                <label>Size</label>
                <div class="size-buttons">
                    <button class="size-btn" data-size="S">Small - $9.50</button>
                    <button class="size-btn active" data-size="M">Medium - $12.00</button>
                    <button class="size-btn" data-size="L">Large - $15.50</button>
                </div>
                
                <label>Toppings (select multiple)</label>
                <div id="toppings-list">
                    Loading toppings...
                </div>
                
                <button id="add-bowl" class="btn-primary">Add Bowl to Cart</button>
            </section>

            <!-- Step 3: Cart -->
            <section class="section">
                <h2>3. Your Cart</h2>
                <div id="cart">
                    <p class="empty">Cart is empty</p>
                </div>
                <div id="cart-total"></div>
                <button id="checkout" class="btn-success">Checkout</button>
            </section>
        </main>
    </div>

    <!-- Load JavaScript -->
    <script src="app.js"></script>
</body>
</html>
```

### style.css (Minimal but Clean)

**Create `src/main/resources/public/style.css`:**

```css
* {
    margin: 0;
    padding: 0;
    box-sizing: border-box;
}

body {
    font-family: Arial, sans-serif;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    min-height: 100vh;
    padding: 20px;
}

.container {
    max-width: 800px;
    margin: 0 auto;
    background: white;
    border-radius: 10px;
    box-shadow: 0 10px 40px rgba(0,0,0,0.3);
    overflow: hidden;
}

header {
    background: linear-gradient(135deg, #FF6B6B, #FFE66D);
    padding: 30px;
    text-align: center;
    color: white;
}

header h1 {
    font-size: 2.5em;
    margin-bottom: 5px;
}

main {
    padding: 30px;
}

.section {
    margin-bottom: 30px;
    padding-bottom: 20px;
    border-bottom: 2px solid #eee;
}

h2 {
    color: #333;
    margin-bottom: 15px;
}

label {
    display: block;
    margin: 15px 0 5px;
    font-weight: bold;
    color: #555;
}

input[type="text"],
select {
    width: 100%;
    padding: 10px;
    border: 2px solid #ddd;
    border-radius: 5px;
    font-size: 16px;
}

.size-buttons {
    display: flex;
    gap: 10px;
    margin-bottom: 15px;
}

.size-btn {
    flex: 1;
    padding: 10px;
    border: 2px solid #ddd;
    background: white;
    border-radius: 5px;
    cursor: pointer;
    font-size: 14px;
}

.size-btn.active {
    background: #667eea;
    color: white;
    border-color: #667eea;
}

#toppings-list {
    max-height: 300px;
    overflow-y: auto;
    border: 2px solid #eee;
    border-radius: 5px;
    padding: 10px;
}

.topping {
    padding: 8px;
    margin: 5px 0;
    background: #f9f9f9;
    border-radius: 4px;
    cursor: pointer;
}

.topping:hover {
    background: #e9e9e9;
}

.topping input {
    margin-right: 8px;
}

.topping.premium {
    background: #fff4e6;
}

.btn-primary,
.btn-success {
    padding: 12px 20px;
    border: none;
    border-radius: 5px;
    font-size: 16px;
    font-weight: bold;
    cursor: pointer;
    width: 100%;
    margin-top: 15px;
}

.btn-primary {
    background: #667eea;
    color: white;
}

.btn-primary:hover {
    background: #5568d3;
}

.btn-success {
    background: #4CAF50;
    color: white;
}

.btn-success:hover {
    background: #45a049;
}

#cart {
    min-height: 100px;
    border: 2px dashed #ddd;
    border-radius: 5px;
    padding: 15px;
}

.empty {
    text-align: center;
    color: #999;
    padding: 30px;
}

.cart-item {
    background: #f9f9f9;
    padding: 15px;
    margin: 10px 0;
    border-radius: 5px;
    border-left: 4px solid #667eea;
}

.cart-item h4 {
    margin-bottom: 5px;
}

.cart-item .details {
    font-size: 14px;
    color: #666;
    margin: 5px 0;
}

#cart-total {
    text-align: right;
    font-size: 20px;
    font-weight: bold;
    margin-top: 15px;
}

.success {
    background: #4CAF50;
    color: white;
    padding: 20px;
    border-radius: 5px;
    text-align: center;
}
```

### app.js (Core Logic)

**Create `src/main/resources/public/app.js`:**

```javascript
// ===== STATE =====
let toppings = [];
let cart = {
    customerName: '',
    bowls: []
};
let selectedSize = 'M';

// ===== LOAD TOPPINGS ON PAGE LOAD =====
document.addEventListener('DOMContentLoaded', async () => {
    await loadToppings();
    setupEventListeners();
});

// ===== LOAD TOPPINGS FROM API =====
async function loadToppings() {
    try {
        const response = await fetch('/api/toppings');
        toppings = await response.json();
        renderToppings();
    } catch (error) {
        console.error('Failed to load toppings:', error);
        alert('Failed to load toppings. Please refresh.');
    }
}

// ===== RENDER TOPPINGS AS CHECKBOXES =====
function renderToppings() {
    const container = document.getElementById('toppings-list');
    
    container.innerHTML = toppings.map(t => `
        <div class="topping ${t.premium ? 'premium' : ''}">
            <input type="checkbox" 
                   id="topping-${t.name}" 
                   value="${t.name}">
            <label for="topping-${t.name}">
                ${t.name} - $${t.price.toFixed(2)}
                ${t.premium ? '⭐' : ''}
            </label>
        </div>
    `).join('');
}

// ===== SETUP EVENT LISTENERS =====
function setupEventListeners() {
    // Size buttons
    document.querySelectorAll('.size-btn').forEach(btn => {
        btn.addEventListener('click', () => {
            document.querySelectorAll('.size-btn').forEach(b => 
                b.classList.remove('active'));
            btn.classList.add('active');
            selectedSize = btn.dataset.size;
        });
    });

    // Add bowl button
    document.getElementById('add-bowl').addEventListener('click', addBowl);

    // Checkout button
    document.getElementById('checkout').addEventListener('click', checkout);
}

// ===== ADD BOWL TO CART =====
function addBowl() {
    const name = document.getElementById('bowlName').value.trim();
    const base = document.getElementById('bowlBase').value;
    
    if (!name) {
        alert('Please name your bowl!');
        return;
    }

    // Get selected toppings
    const selectedToppings = Array.from(
        document.querySelectorAll('#toppings-list input:checked')
    ).map(input => input.value);

    // Add to cart
    cart.bowls.push({
        name: name,
        base: base,
        size: selectedSize,
        toppings: selectedToppings
    });

    // Reset form
    document.getElementById('bowlName').value = '';
    document.querySelectorAll('#toppings-list input:checked')
        .forEach(input => input.checked = false);

    renderCart();
}

// ===== RENDER CART =====
function renderCart() {
    const cartDiv = document.getElementById('cart');
    
    if (cart.bowls.length === 0) {
        cartDiv.innerHTML = '<p class="empty">Cart is empty</p>';
        document.getElementById('cart-total').innerHTML = '';
        return;
    }

    let total = 0;

    cartDiv.innerHTML = cart.bowls.map(bowl => {
        // Calculate price
        let price = 0;
        if (bowl.size === 'S') price = 9.50;
        else if (bowl.size === 'M') price = 12.00;
        else if (bowl.size === 'L') price = 15.50;

        // Add topping prices
        bowl.toppings.forEach(toppingName => {
            const topping = toppings.find(t => t.name === toppingName);
            if (topping) price += topping.price;
        });

        total += price;

        return `
            <div class="cart-item">
                <h4>${bowl.name}</h4>
                <div class="details">
                    Base: ${bowl.base} | Size: ${bowl.size}
                </div>
                <div class="details">
                    Toppings: ${bowl.toppings.join(', ') || 'None'}
                </div>
                <div style="font-weight: bold; color: #4CAF50;">
                    $${price.toFixed(2)}
                </div>
            </div>
        `;
    }).join('');

    const tax = total * 0.07;
    const finalTotal = total + tax;

    document.getElementById('cart-total').innerHTML = `
        Subtotal: $${total.toFixed(2)}<br>
        Tax: $${tax.toFixed(2)}<br>
        <div style="font-size: 24px; color: #4CAF50; margin-top: 5px;">
            Total: $${finalTotal.toFixed(2)}
        </div>
    `;
}

// ===== CHECKOUT =====
async function checkout() {
    const customerName = document.getElementById('customerName').value.trim();
    
    if (!customerName) {
        alert('Please enter your name!');
        return;
    }

    if (cart.bowls.length === 0) {
        alert('Cart is empty!');
        return;
    }

    cart.customerName = customerName;

    try {
        const response = await fetch('/api/orders', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(cart)
        });

        const result = await response.json();

        if (result.success) {
            // Success!
            document.querySelector('main').innerHTML = `
                <div class="success">
                    <h2>🎉 Order Placed!</h2>
                    <p style="font-size: 18px; margin-top: 10px;">
                        Order #${result.orderId}
                    </p>
                    <p style="font-size: 24px; margin-top: 10px;">
                        Total: $${result.total.toFixed(2)}
                    </p>
                    <p style="margin-top: 15px;">
                        Thank you, ${customerName}!
                    </p>
                    <button onclick="location.reload()" 
                            class="btn-primary" 
                            style="margin-top: 20px; width: auto; padding: 10px 30px;">
                        New Order
                    </button>
                </div>
            `;
        } else {
            alert('Failed to place order');
        }
    } catch (error) {
        console.error('Checkout failed:', error);
        alert('Failed to place order. Check console for details.');
    }
}
```

---

## Hours 4-6: Test Complete Flow

### Test Locally

1. **Start server:**
```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="com.richie.web.Main"
```

2. **Open browser:** `http://localhost:7000`

3. **Test full flow:**
    - Enter your name
    - Name a bowl
    - Select base and size
    - Check some toppings
    - Add to cart
    - Verify price is correct
    - Checkout
    - See success message

4. **Verify in database:**
```bash
psql foodhall -c "SELECT * FROM orders ORDER BY id DESC LIMIT 1"
psql foodhall -c "SELECT * FROM order_items WHERE order_id = (SELECT MAX(id) FROM orders)"
```

**✅ MASSIVE CHECKPOINT - DAY 3 COMPLETE!**
- [ ] Frontend displays in browser
- [ ] Toppings load from API
- [ ] Can build bowl and add to cart
- [ ] Price calculates correctly
- [ ] Checkout saves to database
- [ ] Success message shows
- [ ] Can place multiple orders

**🎉 You now have a working full-stack app!**

**🐛 Common Issues:**
- "Fetch failed" → Check server is running
- "Toppings not loading" → Check /api/toppings in browser
- "Checkout fails" → Check browser console and server logs
- "Wrong price" → Verify topping prices in database

---

**STOP HERE FOR DAY 3**

Tomorrow: Deploy to production!

---

# 🏃 DAY 4: DEPLOYMENT (6 HOURS)

## Hour 1: Prepare for Production

### Update DatabaseConnection for Production

**Edit `DatabaseConnection.java`:**

```java
package com.richie.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    
    public static Connection getConnection() throws SQLException {
        // Check for production DATABASE_URL first
        String dbUrl = System.getenv("DATABASE_URL");
        
        if (dbUrl != null) {
            // Production - Render format
            // Render gives: postgres://user:pass@host:port/db
            // JDBC needs: jdbc:postgresql://host:port/db
            
            if (dbUrl.startsWith("postgres://")) {
                dbUrl = dbUrl.replace("postgres://", "jdbc:postgresql://");
            }
            
            System.out.println("🔗 Using production database");
            return DriverManager.getConnection(dbUrl);
            
        } else {
            // Local development
            String url = "jdbc:postgresql://localhost:5432/foodhall";
            String user = "postgres";
            String password = "yourpassword";  // YOUR PASSWORD
            
            System.out.println("🔗 Using local database");
            return DriverManager.getConnection(url, user, password);
        }
    }
}
```

### Update Main.java for Dynamic Port

**Edit `Main.java`:**

```java
public class Main {
    public static void main(String[] args) {
        // Get port from environment or default to 7000
        int port = getPort();
        
        Javalin app = Javalin.create(config -> {
            config.staticFiles.add("/public", Location.CLASSPATH);
        }).start(port);
        
        // ... your routes ...
        
        System.out.println("🚀 Server running on port " + port);
    }
    
    private static int getPort() {
        String portStr = System.getenv("PORT");
        if (portStr != null) {
            return Integer.parseInt(portStr);
        }
        return 7000;  // Default for local
    }
}
```

### Update pom.xml for Production Build

**Add build configuration:**

```xml
<build>
    <plugins>
        <!-- Compiler -->
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <version>3.11.0</version>
            <configuration>
                <source>17</source>
                <target>17</target>
            </configuration>
        </plugin>
        
        <!-- Build fat JAR with dependencies -->
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-shade-plugin</artifactId>
            <version>3.5.0</version>
            <executions>
                <execution>
                    <phase>package</phase>
                    <goals>
                        <goal>shade</goal>
                    </goals>
                    <configuration>
                        <transformers>
                            <transformer implementation="org.apache.maven.plugins.shade.resource.ManifestResourceTransformer">
                                <mainClass>com.richie.web.Main</mainClass>
                            </transformer>
                        </transformers>
                        <filters>
                            <filter>
                                <artifact>*:*</artifact>
                                <excludes>
                                    <exclude>META-INF/*.SF</exclude>
                                    <exclude>META-INF/*.DSA</exclude>
                                    <exclude>META-INF/*.RSA</exclude>
                                </excludes>
                            </filter>
                        </filters>
                    </configuration>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

### Test Local Build

```bash
# Build JAR
mvn clean package

# You should see: target/project-1.0-SNAPSHOT.jar

# Test it
java -jar target/project-1.0-SNAPSHOT.jar

# Visit http://localhost:7000
# Test full flow

# Stop with Ctrl+C
```

**✅ Checkpoint:**
- [ ] DatabaseConnection handles environment variable
- [ ] Main.java uses PORT environment variable
- [ ] JAR builds successfully
- [ ] JAR runs locally
- [ ] App still works

---

## Hours 2-4: Deploy to Render

### Step 1: Push to GitHub

**If not already on GitHub:**

```bash
# Initialize git (if needed)
git init
git add .
git commit -m "Initial commit - ready for deployment"

# Create repo on GitHub (go to github.com)
# Then connect:
git remote add origin https://github.com/YOUR_USERNAME/food-hall.git
git branch -M main
git push -u origin main
```

**If already on GitHub:**

```bash
git add .
git commit -m "Ready for deployment"
git push origin main
```

### Step 2: Create Render Account

1. Go to [render.com](https://render.com)
2. Sign up with GitHub
3. Authorize Render to access your repos

### Step 3: Deploy PostgreSQL Database

1. Click "New +" → "PostgreSQL"
2. Settings:
    - **Name:** `foodhall-db`
    - **Database:** `foodhall`
    - **User:** (auto-generated)
    - **Region:** Oregon (US West) or closest to you
    - **Plan:** Free
3. Click "Create Database"
4. Wait ~2 minutes for provisioning
5. **Copy "Internal Database URL"** (starts with `postgres://`)

### Step 4: Seed Production Database

**On Render dashboard:**

1. Click your `foodhall-db` database
2. Click "Connect" tab
3. Copy the "PSQL Command"
4. Run in your terminal:

```bash
# The command looks like:
# PGPASSWORD=xxxxx psql -h oregon-postgres.render.com -U foodhall foodhall

# Paste the command, hit enter

# You're now connected to production database!
```

5. **Load your schema:**

```sql
-- Copy entire schema.sql content and paste here
-- Or if you have the file:
\i /path/to/your/schema.sql

-- Verify:
\dt
SELECT COUNT(*) FROM toppings;

-- Exit:
\q
```

**✅ Checkpoint:**
- [ ] Render account created
- [ ] PostgreSQL database created
- [ ] Can connect via psql
- [ ] Schema loaded
- [ ] 26 toppings in database

---

### Step 5: Deploy Web Service

1. On Render dashboard: "New +" → "Web Service"
2. Connect your GitHub repository
3. Select your `food-hall` repo (or whatever you named it)
4. Configure:
    - **Name:** `food-hall` (or choose unique name)
    - **Region:** Same as database (Oregon)
    - **Branch:** `main`
    - **Root Directory:** (leave blank)
    - **Environment:** `Java`
    - **Build Command:** `mvn clean package`
    - **Start Command:** `java -jar target/project-1.0-SNAPSHOT.jar`
    - **Plan:** Free

5. **Environment Variables** (click "Advanced"):
    - Add variable:
        - **Key:** `DATABASE_URL`
        - **Value:** Click "Generate" → Select your `foodhall-db`
    - This auto-fills the connection string!

6. Click "Create Web Service"

7. **Wait 10-15 minutes** for first build
    - Watch the logs in Render dashboard
    - You'll see Maven downloading dependencies
    - Then compilation
    - Then "Server running on port 10000"

8. Once deployed, Render gives you a URL like:
   `https://food-hall.onrender.com`

### Step 6: Test Production App

1. Visit your Render URL
2. Test full flow:
    - Enter name
    - Build bowl
    - Add toppings
    - Checkout
3. **It works!** 🎉

**✅ MASSIVE CHECKPOINT - DEPLOYED!**
- [ ] Web service deployed on Render
- [ ] App accessible via public URL
- [ ] Database connected
- [ ] Can place orders
- [ ] Orders save to production database

---

## Hours 4-5: Polish & Documentation

### Important: Free Tier Limitations

**Render Free Tier:**
- ✅ Free forever
- ⚠️  Sleeps after 15 min of inactivity
- ⚠️  First request after sleep takes ~50 seconds
- ✅ Good for portfolio/demos

**Tell users:** "First load may take a minute (free hosting)"

### Create Professional README

**Update `README.md`:**

```markdown
# 🍱 Funkin' Poke - Food Hall Web App

Build custom poke bowls and order online. Fresh toppings, instant checkout, powered by PostgreSQL.

## 🚀 Live Demo

**[View Live App](https://your-app.onrender.com)**

⚠️ *First load may take ~50 seconds (free tier waking up)*

## 📸 Screenshots

![Homepage](/screenshots/home.png)
*Custom bowl builder with 26+ toppings*

![Checkout](/screenshots/checkout.png)
*Real-time price calculation and checkout*

## ✨ Features

- **Custom Poke Bowls** - Choose base, size, and toppings
- **26+ Toppings** - Premium proteins and regular toppings
- **Real-Time Pricing** - Instant total calculation
- **Database Persistence** - All orders saved to PostgreSQL
- **Responsive Design** - Works on mobile and desktop

## 🛠️ Tech Stack

**Backend:**
- Java 17
- Javalin (lightweight web framework)
- PostgreSQL (database)
- JDBC (database connectivity)
- Maven (build tool)

**Frontend:**
- Vanilla JavaScript (no frameworks!)
- HTML5/CSS3
- Fetch API for REST calls

**Deployment:**
- Render.com (free tier)
- PostgreSQL managed database
- CI/CD from GitHub

## 🏗️ Architecture

```
Frontend (JS) ←→ REST API (Javalin) ←→ Database (PostgreSQL)
```

**Key Patterns:**
- DAO Pattern for data access
- REST API design
- Transaction management
- Responsive UI

## 📦 Installation (Local Development)

### Prerequisites
- Java 17+
- PostgreSQL 15+
- Maven 3.8+

### Setup

```bash
# 1. Clone
git clone https://github.com/yourusername/food-hall.git
cd food-hall

# 2. Create database
createdb foodhall

# 3. Load schema
psql foodhall < schema.sql

# 4. Update password
# Edit src/main/java/com/richie/dao/DatabaseConnection.java
# Change PASSWORD constant

# 5. Build and run
mvn clean package
java -jar target/project-1.0-SNAPSHOT.jar

# 6. Visit
http://localhost:7000
```

## 🔑 Environment Variables

**Local:** Edit `DatabaseConnection.java`
**Production:** Set in Render dashboard
- `DATABASE_URL` - PostgreSQL connection string (auto-generated by Render)
- `PORT` - Server port (auto-set by Render to 10000)

## 📊 Database Schema

```
restaurants
toppings
├── orders
│   └── order_items
│       └── order_item_toppings
```

**Foreign keys ensure referential integrity.**

## 🎓 Learning Journey

This project evolved from a CLI application to a full-stack web app:

| Started With | Became |
|--------------|--------|
| CLI Input | HTTP Requests |
| File I/O | PostgreSQL |
| BufferedWriter | PreparedStatement |
| Local testing | Cloud deployment |

**Key learnings:**
- JDBC patterns and transaction management
- REST API design principles
- Frontend/backend integration
- Production deployment workflow

## 📝 API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/toppings` | Get all available toppings |
| POST | `/api/orders` | Create new order |

### POST /api/orders Request

```json
{
  "customerName": "John Doe",
  "bowls": [{
    "name": "Custom Bowl",
    "base": "White Rice",
    "size": "L",
    "toppings": ["Salmon", "Avocado", "Spicy Mayo"]
  }]
}
```

### Response

```json
{
  "success": true,
  "orderId": 42,
  "total": 27.12
}
```

## 🚀 Deployment

Deployed on Render.com using:
1. PostgreSQL managed database
2. Web service with auto-deploy from GitHub
3. Environment variable for database connection

**Deployment steps:**
1. Push to GitHub
2. Create PostgreSQL on Render
3. Seed database with schema
4. Create web service
5. Link database via environment variable
6. Auto-deploy on push!

## 🐛 Known Issues

- Free tier sleeps after 15 min inactivity
- First load after sleep takes ~50 seconds
- No user authentication (MVP scope)

## 🔮 Future Enhancements

- [ ] User accounts and order history
- [ ] Admin panel for menu management
- [ ] Multiple restaurants in food hall
- [ ] Payment integration (Stripe)
- [ ] Order tracking

## 👤 Author

**Richie** - [GitHub](https://github.com/yourusername) | [LinkedIn](https://linkedin.com/in/yourprofile)

## 📄 License

MIT License - feel free to use for learning!

---

**Built in 4 days as a learning project** 🚀
