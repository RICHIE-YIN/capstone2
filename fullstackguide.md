# 🎓 THE ULTIMATE 20-HOUR GUIDE: LEARN & BUILD
## CLI → Deployed Web App (Zero Bugs, Maximum Learning)

**Your Mission:** Transform your Poke CLI into a deployed web app while actually understanding what you're building.

**Timeline:** 20 hours (includes learning time)  
**Philosophy:** Teach concepts → Practice → Verify → Move forward

---

## 📅 THE LEARNING PLAN

**Hours 0-7:** Database & JDBC Fundamentals  
**Hours 7-13:** REST API with Javalin  
**Hours 13-17:** Frontend Development  
**Hours 17-20:** Deployment & Polish

**Each section has:**
- 📖 **Concept** - The "why"
- 🔍 **Example** - See it working
- 💡 **Your Turn** - Build it yourself
- ✅ **Checkpoint** - Verify success
- 🐛 **Troubleshooting** - Fix common errors

---

# 📦 HOUR 0: PROJECT SETUP (1 HOUR)

## Update Your pom.xml

**Replace your entire `pom.xml`:**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.richie</groupId>
    <artifactId>foodhall</artifactId>
    <version>1.0-SNAPSHOT</version>

    <properties>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

    <dependencies>
        <!-- Testing -->
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter</artifactId>
            <version>5.10.0</version>
            <scope>test</scope>
        </dependency>

        <!-- Database -->
        <dependency>
            <groupId>org.postgresql</groupId>
            <artifactId>postgresql</artifactId>
            <version>42.6.0</version>
        </dependency>

        <!-- Web Framework -->
        <dependency>
            <groupId>io.javalin</groupId>
            <artifactId>javalin</artifactId>
            <version>5.6.3</version>
        </dependency>

        <!-- JSON -->
        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-databind</artifactId>
            <version>2.15.2</version>
        </dependency>

        <!-- Logging -->
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-simple</artifactId>
            <version>2.0.9</version>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.11.0</version>
                <configuration>
                    <source>17</source>
                    <target>17</target>
                </configuration>
            </plugin>

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
</project>
```

**Test it:**
```bash
mvn clean install
```

**✅ Should see:** `BUILD SUCCESS`

---

# 🏃 HOURS 1-3: POSTGRESQL & DATABASE DESIGN

## Hour 1: Understanding Databases

### 📖 CONCEPT: Why PostgreSQL?

**Your current approach (files):**
```
orders/
├── 20241101-120534.txt
├── 20241101-143022.txt
└── 20241101-152341.txt
```

**Problems:**
- Hard to search: "Find all orders over $50" = open every file
- No relationships: Can't easily link orders to customers
- Scalability: 10,000 orders = 10,000 files

**With PostgreSQL:**
```sql
SELECT * FROM orders WHERE total > 50;  -- Instant!
SELECT o.*, c.* FROM orders o JOIN customers c ON o.customer_id = c.id;  -- Related data!
```

**Key benefits:**
- ✅ Fast queries on millions of records
- ✅ ACID transactions (all-or-nothing saves)
- ✅ Foreign keys link related data
- ✅ Industry standard

### Install PostgreSQL

**Mac:**
```bash
brew install postgresql@15
brew services start postgresql@15
psql --version
```

**Windows:**
1. Download from postgresql.org
2. Install with defaults
3. **Write down your password!**
4. Verify: `psql --version`

### 💡 YOUR TURN: Create Database

**Task:** Create a database called `foodhall`

**Steps:**
1. Connect to PostgreSQL: `psql postgres`
2. Create database: `CREATE DATABASE foodhall;`
3. Connect to it: `\c foodhall`
4. Verify: `SELECT current_database();`

**✅ CHECKPOINT:**
```bash
psql foodhall -c "SELECT current_database();"
# Output should be: foodhall
```

**🐛 Troubleshooting:**
- "command not found" → PATH issue, use full path or reinstall
- "authentication failed" → Check password
- "database exists" → Good! Already created

---

## Hour 2-3: Database Schema Design

### 📖 CONCEPT: Tables & Relationships

**Remember your Java classes:**
```java
Order order = new Order("Richie");
PokeBowl bowl = new PokeBowl("My Bowl", "White Rice", "L");
bowl.addTopping(new Topping("Salmon", 2.00, true));
order.addItem(bowl);
```

**In database:**
```
orders table
├── order_items table (has order_id foreign key)
│   └── order_item_toppings table (has order_item_id foreign key)
└── toppings table
```

**Foreign keys = relationships**
- `order_items.order_id` → `orders.id` (each item belongs to an order)
- `order_item_toppings.topping_id` → `toppings.id` (which topping was used)

### 🔍 EXAMPLE: Toppings Table

```sql
CREATE TABLE toppings (
    id SERIAL PRIMARY KEY,              -- Auto-incrementing ID
    name VARCHAR(100) NOT NULL UNIQUE,  -- Name must be unique
    base_price DECIMAL(10, 2) NOT NULL, -- Price with 2 decimal places
    is_premium BOOLEAN DEFAULT false    -- Premium = 1.5x multiplier
);

INSERT INTO toppings (name, base_price, is_premium) VALUES
('Salmon', 2.00, true),
('Avocado', 1.00, true),
('Cucumber', 0.25, false);

-- Query it
SELECT * FROM toppings;
```

**Key SQL concepts:**
- `SERIAL` = auto-increment (1, 2, 3...)
- `PRIMARY KEY` = unique identifier
- `NOT NULL` = field is required
- `UNIQUE` = no duplicates allowed
- `DEFAULT` = value if not specified

### 💡 YOUR TURN: Create Complete Schema

**Task:** Create all database tables

**Create file `schema.sql` in project root:**

```sql
-- Clean slate (drop existing tables)
DROP TABLE IF EXISTS order_item_toppings CASCADE;
DROP TABLE IF EXISTS order_items CASCADE;
DROP TABLE IF EXISTS orders CASCADE;
DROP TABLE IF EXISTS toppings CASCADE;
DROP TABLE IF EXISTS restaurants CASCADE;

-- Restaurants table
CREATE TABLE restaurants (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    cuisine_type VARCHAR(50),
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Toppings table (standalone - no foreign keys)
CREATE TABLE toppings (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    base_price DECIMAL(10, 2) NOT NULL,
    is_premium BOOLEAN DEFAULT false
);

-- Orders table (customer orders)
CREATE TABLE orders (
    id SERIAL PRIMARY KEY,
    customer_name VARCHAR(100) NOT NULL,
    subtotal DECIMAL(10, 2) NOT NULL,
    tax DECIMAL(10, 2) NOT NULL,
    total DECIMAL(10, 2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Order items (products in an order)
CREATE TABLE order_items (
    id SERIAL PRIMARY KEY,
    order_id INTEGER NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
    item_type VARCHAR(20) NOT NULL,  -- 'bowl', 'drink', 'side'
    item_name VARCHAR(100),
    base VARCHAR(50),                 -- For bowls
    size VARCHAR(10),                 -- S, M, L
    flavor VARCHAR(100),              -- For drinks
    side_type VARCHAR(100),           -- For sides
    price DECIMAL(10, 2) NOT NULL
);

-- Order item toppings (which toppings on which bowls)
CREATE TABLE order_item_toppings (
    id SERIAL PRIMARY KEY,
    order_item_id INTEGER NOT NULL REFERENCES order_items(id) ON DELETE CASCADE,
    topping_id INTEGER NOT NULL REFERENCES toppings(id),
    price DECIMAL(10, 2) NOT NULL    -- Price snapshot at time of order
);

-- Seed restaurant
INSERT INTO restaurants (name, cuisine_type, description) 
VALUES ('Funkin'' Poke', 'Hawaiian', 'Fresh poke bowls with premium toppings');

-- Seed all toppings
INSERT INTO toppings (name, base_price, is_premium) VALUES
('Salmon', 2.00, true),
('Tuna', 2.25, true),
('Spicy Tuna', 2.50, true),
('Spicy Salmon', 2.50, true),
('Shrimp', 1.75, true),
('Tofu', 1.00, true),
('Crab Mix', 1.25, true),
('Avocado', 1.00, true),
('Seaweed Salad', 0.75, false),
('Cucumber', 0.25, false),
('Mango', 0.50, false),
('Green Onion', 0.25, false),
('Masago', 0.75, false),
('Pickled Ginger', 0.25, false),
('Jalapeño', 0.25, false),
('Nori', 0.10, false),
('Spicy Mayo', 0.50, false),
('Eel Sauce', 0.50, false),
('Ponzu Sauce', 0.50, false),
('Sesame Oil', 0.25, false),
('Yuzu Dressing', 0.50, false),
('Wasabi Aioli', 0.75, false),
('Sriracha', 0.25, false),
('Furikake', 0.25, false),
('Crispy Onions', 0.50, false),
('Tempura Flakes', 0.50, false);
```

**Load the schema:**
```bash
psql foodhall < schema.sql
```

**✅ CHECKPOINT:**
```bash
# Should have 5 tables
psql foodhall -c "\dt"

# Should have 26 toppings
psql foodhall -c "SELECT COUNT(*) FROM toppings;"

# Try a query
psql foodhall -c "SELECT name, base_price FROM toppings WHERE is_premium = true;"
```

**🐛 Troubleshooting:**
- "syntax error" → Check for typos, missing semicolons
- "already exists" → Good! Or drop tables first
- File path issues → Must run from project root

---

# 🏃 HOURS 3-7: JDBC & DAO PATTERN

## Hour 3-4: JDBC Fundamentals

### 📖 CONCEPT: What is JDBC?

**JDBC = Java Database Connectivity** (like ODBC for databases)

**You already know this pattern from files:**
```java
// Reading a file
BufferedReader reader = new BufferedReader(new FileReader("data.txt"));
String line = reader.readLine();
reader.close();
```

**JDBC is similar:**
```java
// Reading from database
Connection conn = DriverManager.getConnection(url, user, pass);
Statement stmt = conn.createStatement();
ResultSet rs = stmt.executeQuery("SELECT * FROM toppings");
conn.close();
```

**The JDBC workflow:**
```
1. Get Connection      → DriverManager.getConnection()
2. Create Statement    → conn.createStatement() or prepareStatement()
3. Execute Query       → stmt.executeQuery() / executeUpdate()
4. Process Results     → ResultSet (like iterator)
5. Close Resources     → conn.close() (or use try-with-resources)
```

### 🔍 EXAMPLE: Try-With-Resources

**Remember this from file I/O?**
```java
try (BufferedReader reader = new BufferedReader(new FileReader("file.txt"))) {
    String line = reader.readLine();
    // reader automatically closed when done!
}
```

**Same pattern for JDBC:**
```java
try (Connection conn = getConnection();
     Statement stmt = conn.createStatement();
     ResultSet rs = stmt.executeQuery("SELECT * FROM toppings")) {
    
    while (rs.next()) {
        String name = rs.getString("name");
        System.out.println(name);
    }
    // Everything auto-closed!
}
```

**Why this matters:** Database connections are expensive. Must close them or you'll run out!

### 🔍 EXAMPLE: Statement vs PreparedStatement

**Statement (dangerous with user input!):**
```java
String name = userInput;  // What if user types: "; DROP TABLE toppings; --"
Statement stmt = conn.createStatement();
ResultSet rs = stmt.executeQuery("SELECT * FROM toppings WHERE name = '" + name + "'");
// SQL INJECTION VULNERABILITY!
```

**PreparedStatement (safe!):**
```java
String name = userInput;  // Even malicious input is escaped
PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM toppings WHERE name = ?");
pstmt.setString(1, name);  // ? replaced safely
ResultSet rs = pstmt.executeQuery();
// SAFE!
```

**Rule:** Always use PreparedStatement when you have variables!

### 💡 YOUR TURN: Create DatabaseConnection

**Task:** Create a class that provides database connections

**Create directory:**
```bash
mkdir -p src/main/java/com/richie/dao
```

**Create file `src/main/java/com/richie/dao/DatabaseConnection.java`:**

```java
package com.richie.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    
    // Local development settings
    private static final String LOCAL_URL = "jdbc:postgresql://localhost:5432/foodhall";
    private static final String LOCAL_USER = "postgres";
    private static final String LOCAL_PASSWORD = "YOUR_PASSWORD_HERE";  // ⚠️ CHANGE THIS!
    
    /**
     * Get a database connection.
     * Checks for production DATABASE_URL first, falls back to local.
     */
    public static Connection getConnection() throws SQLException {
        String dbUrl = System.getenv("DATABASE_URL");
        
        if (dbUrl != null) {
            // Production (Render/Heroku)
            if (dbUrl.startsWith("postgres://")) {
                dbUrl = dbUrl.replace("postgres://", "jdbc:postgresql://");
            }
            System.out.println("🔗 Using production database");
            return DriverManager.getConnection(dbUrl);
        } else {
            // Local development
            System.out.println("🔗 Using local database");
            return DriverManager.getConnection(LOCAL_URL, LOCAL_USER, LOCAL_PASSWORD);
        }
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

**⚠️ CRITICAL: Change LOCAL_PASSWORD to your PostgreSQL password!**

**✅ CHECKPOINT:**
```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="com.richie.dao.DatabaseConnection"

# Should output:
# 🔗 Using local database
# ✅ Connected to: foodhall
```

**🐛 Troubleshooting:**
- "No suitable driver" → Maven didn't download PostgreSQL driver, run `mvn clean install`
- "Connection refused" → PostgreSQL not running
- "authentication failed" → Wrong password in code
- "database does not exist" → Create foodhall database first

---

## Hour 4-5: DAO Pattern

### 📖 CONCEPT: What is DAO?

**DAO = Data Access Object** (separates database code from business logic)

**Bad approach (mixing concerns):**
```java
public class Order {
    public void save() {
        // Order class knows about database - bad!
        Connection conn = getConnection();
        PreparedStatement pstmt = conn.prepareStatement("INSERT...");
        // ...
    }
}
```

**Good approach (separation):**
```java
// Order class - business logic only
public class Order {
    private String customerName;
    // Just getters/setters
}

// OrderDAO class - database only
public class OrderDAO {
    public void save(Order order) {
        // All database code here
    }
}
```

**Why this matters:**
- ✅ Can test business logic without database
- ✅ Can swap databases (PostgreSQL → MySQL) without changing Order
- ✅ Clear responsibilities

### 🔍 EXAMPLE: Reading Data with ResultSet

**ResultSet is like an iterator:**

```java
ResultSet rs = stmt.executeQuery("SELECT name, base_price, is_premium FROM toppings");

// Move cursor to first row
while (rs.next()) {
    // Get data from current row
    String name = rs.getString("name");           // By column name
    double price = rs.getDouble("base_price");    // or rs.getDouble(2) by position
    boolean premium = rs.getBoolean("is_premium");
    
    // Create Java object
    Topping t = new Topping(name, price, premium);
}
```

**Think of it like:**
```
Cursor position:
[before first] ← start here
Row 1: Salmon, 2.00, true     ← rs.next() moves here
Row 2: Tuna, 2.25, true        ← rs.next() moves here
Row 3: Cucumber, 0.25, false   ← rs.next() moves here
[after last]                   ← rs.next() returns false
```

### 💡 YOUR TURN: Create ToppingDAO

**Task:** Create a DAO that can read toppings from database

**Create file `src/main/java/com/richie/dao/ToppingDAO.java`:**

```java
package com.richie.dao;

import com.richie.model.Topping;
import java.sql.*;
import java.util.ArrayList;

public class ToppingDAO {
    
    /**
     * Get all toppings from database.
     * Returns ArrayList of Topping objects.
     */
    public ArrayList<Topping> getAllToppings() {
        ArrayList<Topping> toppings = new ArrayList<>();
        String sql = "SELECT name, base_price, is_premium FROM toppings ORDER BY name";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                // Create Topping object from database row
                Topping t = new Topping(
                    rs.getString("name"),
                    rs.getDouble("base_price"),
                    rs.getBoolean("is_premium")
                );
                toppings.add(t);
            }
            
        } catch (SQLException e) {
            System.out.println("❌ Error loading toppings");
            e.printStackTrace();
        }
        
        return toppings;
    }
    
    /**
     * Get one topping by name.
     * Returns null if not found.
     */
    public Topping getToppingByName(String name) {
        String sql = "SELECT name, base_price, is_premium FROM toppings WHERE name = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            // Set the ? parameter
            pstmt.setString(1, name);
            
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return new Topping(
                    rs.getString("name"),
                    rs.getDouble("base_price"),
                    rs.getBoolean("is_premium")
                );
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;  // Not found
    }
    
    /**
     * Get topping ID by name (needed for saving orders).
     * Takes Connection parameter to work within a transaction.
     */
    public int getToppingId(Connection conn, String name) throws SQLException {
        String sql = "SELECT id FROM toppings WHERE name = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("id");
            }
        }
        
        throw new SQLException("Topping not found: " + name);
    }
}
```

**Now test it:**

Add this to ToppingDAO:

```java
public static void main(String[] args) {
    ToppingDAO dao = new ToppingDAO();
    
    System.out.println("=== Test 1: Get All ===");
    ArrayList<Topping> all = dao.getAllToppings();
    System.out.println("Found " + all.size() + " toppings");
    
    System.out.println("\n=== Test 2: Get Salmon ===");
    Topping salmon = dao.getToppingByName("Salmon");
    if (salmon != null) {
        System.out.println(salmon.getName() + ": $" + salmon.getPrice());
    }
}
```

**✅ CHECKPOINT:**
```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="com.richie.dao.ToppingDAO"

# Should output:
# Found 26 toppings
# Salmon: $3.0 (because premium = 2.00 * 1.5)
```

**🐛 Troubleshooting:**
- "getDouble error" → Fix typo: should be `rs.getDouble("base_price")` not `getString`
- Empty list → Check database has data
- Connection error → DatabaseConnection not configured

---

## Hour 5-7: OrderDAO with Transactions

### 📖 CONCEPT: Database Transactions

**Problem without transactions:**
```java
// Step 1: Insert order ✅
insertOrder(order);

// Step 2: Insert item 1 ✅
insertItem(item1);

// Step 3: Insert item 2 ❌ ERROR!
insertItem(item2);  // Crashes!

// Now you have an order with only 1 item - data is inconsistent!
```

**Solution: Transactions (all-or-nothing):**
```java
conn.setAutoCommit(false);  // Start transaction
try {
    insertOrder(order);      // Temporary
    insertItem(item1);       // Temporary
    insertItem(item2);       // Temporary
    conn.commit();           // ✅ Make all changes permanent
} catch (Exception e) {
    conn.rollback();         // ❌ Undo everything
}
```

**Real-world analogy:** Like a bank transfer
- Withdraw $100 from Account A
- Deposit $100 to Account B
- If deposit fails, withdrawal must be undone!

### 🔍 EXAMPLE: Basic Transaction

```java
Connection conn = null;
try {
    conn = DatabaseConnection.getConnection();
    conn.setAutoCommit(false);  // Start transaction
    
    // Do multiple operations
    PreparedStatement pstmt1 = conn.prepareStatement("INSERT INTO orders...");
    pstmt1.executeUpdate();
    
    PreparedStatement pstmt2 = conn.prepareStatement("INSERT INTO order_items...");
    pstmt2.executeUpdate();
    
    conn.commit();  // Success - save everything!
    
} catch (SQLException e) {
    if (conn != null) {
        conn.rollback();  // Error - undo everything!
    }
} finally {
    if (conn != null) {
        conn.close();
    }
}
```

### 💡 YOUR TURN: Create OrderDAO

**Task:** Create a DAO that saves complete orders with transactions

**Create file `src/main/java/com/richie/dao/OrderDAO.java`:**

```java
package com.richie.dao;

import com.richie.model.*;
import java.sql.*;

public class OrderDAO {
    
    private ToppingDAO toppingDAO = new ToppingDAO();
    
    /**
     * Save order with all items and toppings.
     * Uses transaction to ensure all-or-nothing.
     * Returns order ID if successful, -1 if failed.
     */
    public int saveOrder(Order order) {
        // SQL statements we'll need
        String orderSql = "INSERT INTO orders (customer_name, subtotal, tax, total) " +
                         "VALUES (?, ?, ?, ?) RETURNING id";
        
        String itemSql = "INSERT INTO order_items " +
                        "(order_id, item_type, item_name, base, size, flavor, side_type, price) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";
        
        String toppingSql = "INSERT INTO order_item_toppings " +
                           "(order_item_id, topping_id, price) VALUES (?, ?, ?)";
        
        // Don't use try-with-resources here - we need to control commit/rollback
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
                        pstmt.setNull(6, Types.VARCHAR);  // flavor (null for bowls)
                        pstmt.setNull(7, Types.VARCHAR);  // side_type (null for bowls)
                    } else if (product instanceof Drink) {
                        Drink drink = (Drink) product;
                        pstmt.setString(2, "drink");
                        pstmt.setString(3, drink.getFlavor());
                        pstmt.setNull(4, Types.VARCHAR);  // base (null for drinks)
                        pstmt.setString(5, drink.getSize());
                        pstmt.setString(6, drink.getFlavor());
                        pstmt.setNull(7, Types.VARCHAR);  // side_type
                    } else if (product instanceof Sides) {
                        Sides side = (Sides) product;
                        pstmt.setString(2, "side");
                        pstmt.setString(3, side.getType());
                        pstmt.setNull(4, Types.VARCHAR);  // base
                        pstmt.setNull(5, Types.VARCHAR);  // size
                        pstmt.setNull(6, Types.VARCHAR);  // flavor
                        pstmt.setString(7, side.getType());
                    }
                    
                    pstmt.setDouble(8, product.getPrice());
                    
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
                            // Get topping ID from database (using same connection!)
                            int toppingId = toppingDAO.getToppingId(conn, topping.getName());
                            
                            pstmt.setInt(1, itemId);
                            pstmt.setInt(2, toppingId);
                            pstmt.setDouble(3, topping.getPrice());
                            pstmt.executeUpdate();
                            System.out.println("    ✅ Topping: " + topping.getName());
                        }
                    }
                }
            }
            
            conn.commit();  // 🎉 SUCCESS - Make everything permanent!
            System.out.println("✅✅✅ Transaction committed!");
            return orderId;
            
        } catch (SQLException e) {
            System.out.println("❌ Error saving order");
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
            
            return -1;  // Failed
            
        } finally {
            // Always close connection
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
```

**Add test method:**

```java
public static void main(String[] args) {
    OrderDAO orderDao = new OrderDAO();
    ToppingDAO toppingDao = new ToppingDAO();
    
    // Create test order
    Order order = new Order("Test Customer");
    
    // Add bowl with toppings
    PokeBowl bowl = new PokeBowl("Test Bowl", "White Rice", "L");
    bowl.addTopping(toppingDao.getToppingByName("Salmon"));
    bowl.addTopping(toppingDao.getToppingByName("Avocado"));
    bowl.addTopping(toppingDao.getToppingByName("Spicy Mayo"));
    order.addItem(bowl);
    
    // Add drink
    Drink drink = new Drink("M", "Green Tea");
    order.addItem(drink);
    
    // Add side
    Sides side = new Sides("Edamame");
    order.addItem(side);
    
    // Save it
    System.out.println("\n=== Saving Order ===");
    int orderId = orderDao.saveOrder(order);
    
    if (orderId > 0) {
        System.out.println("\n✅ SUCCESS! Order ID: " + orderId);
        System.out.println("Total: $" + order.getTotal());
    } else {
        System.out.println("\n❌ FAILED");
    }
}
```

**✅ CHECKPOINT:**
```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="com.richie.dao.OrderDAO"

# Should output:
# ✅ Order saved: ID=1
# ✅ Item saved: ID=1
#   ✅ Topping: Salmon
#   ✅ Topping: Avocado
#   ✅ Topping: Spicy Mayo
# ✅ Item saved: ID=2
# ✅ Item saved: ID=3
# ✅✅✅ Transaction committed!
# ✅ SUCCESS! Order ID: 1

# Verify in database:
psql foodhall -c "SELECT * FROM orders;"
psql foodhall -c "SELECT * FROM order_items;"
psql foodhall -c "SELECT * FROM order_item_toppings;"
```

**🐛 Troubleshooting:**
- "column does not exist" → Check schema.sql matches INSERT statements
- "foreign key violation" → Parent record doesn't exist (order must exist before items)
- "Topping not found" → Topping name doesn't match database exactly
- Transaction rolled back → Check error message for specific issue

**🎉 HOURS 1-7 COMPLETE! Database layer is perfect.**

---

# 🏃 HOURS 7-13: REST API WITH JAVALIN

## Hour 7-8: Understanding REST & Javalin

### 📖 CONCEPT: What is REST?

**REST = Representational State Transfer** (using HTTP properly)

**Your CLI had this flow:**
```
1. User types: "add topping"
2. System shows: topping menu
3. User types: "1" (Salmon)
4. System adds to order
```

**REST API has this flow:**
```
1. Client sends: GET /api/toppings
2. Server returns: JSON array of toppings
3. Client sends: POST /api/orders with JSON data
4. Server saves and returns: order ID
```

**HTTP Methods:**
- `GET` - Read data (doesn't change anything)
- `POST` - Create new data
- `PUT` - Update existing data
- `DELETE` - Delete data

**HTTP Status Codes:**
- `200 OK` - Success (GET)
- `201 Created` - Success (POST)
- `400 Bad Request` - Client error (invalid data)
- `404 Not Found` - Resource doesn't exist
- `500 Internal Server Error` - Server crashed

### 🔍 EXAMPLE: Flask vs Javalin

**You know Flask:**
```python
@app.route('/toppings')
def get_toppings():
    return jsonify(toppings)
```

**Javalin is similar:**
```java
app.get("/toppings", ctx -> {
    ctx.json(toppings);
});
```

**Key concepts:**
- `app.get()` = handle GET requests
- `ctx` = context (has request data + response methods)
- `ctx.json()` = return JSON response

### 💡 YOUR TURN: Create Basic Javalin Server

**Task:** Create a simple web server

**Create directory:**
```bash
mkdir -p src/main/java/com/richie/web
```

**Create file `src/main/java/com/richie/web/Main.java`:**

```java
package com.richie.web;

import io.javalin.Javalin;

public class Main {
    public static void main(String[] args) {
        // Create Javalin app
        Javalin app = Javalin.create().start(8080);
        
        // Define routes
        app.get("/", ctx -> {
            ctx.result("🍱 Funkin' Poke API");
        });
        
        app.get("/hello/{name}", ctx -> {
            String name = ctx.pathParam("name");
            ctx.result("Hello, " + name + "!");
        });
        
        System.out.println("🚀 Server running on http://localhost:8080");
    }
}
```

**✅ CHECKPOINT:**
```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="com.richie.web.Main"

# Server should start. In browser, visit:
# http://localhost:8080
# http://localhost:8080/hello/Richie

# Stop with Ctrl+C
```

**🐛 Troubleshooting:**
- "Port already in use" → Change 7000 to 8080
- "ClassNotFoundException" → Run `mvn clean install` first
- Server won't start → Check logs for specific error

---

## Hour 8-10: Build Complete API

### 📖 CONCEPT: The Context Object (ctx)

**The `ctx` parameter has everything:**

```java
app.get("/example", ctx -> {
    // ===== REQUEST DATA =====
    String pathParam = ctx.pathParam("id");        // /example/123
    String queryParam = ctx.queryParam("sort");    // /example?sort=asc
    String body = ctx.body();                      // POST/PUT body
    
    // ===== RESPONSE METHODS =====
    ctx.result("Plain text");                      // Text response
    ctx.json(object);                              // JSON response
    ctx.status(201);                               // Set HTTP status
    ctx.status(404).result("Not found");          // Chain methods
});
```

### 💡 YOUR TURN: Complete Main.java

**Task:** Add all routes and request classes

**Replace `Main.java` with complete version:**

```java
package com.richie.web;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import com.richie.dao.*;
import com.richie.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Main {
    
    // DAO instances
    private static ToppingDAO toppingDAO = new ToppingDAO();
    private static OrderDAO orderDAO = new OrderDAO();
    
    public static void main(String[] args) {
        int port = getPort();
        
        // Create Javalin app with static file support
        Javalin app = Javalin.create(config -> {
            config.staticFiles.add("/public", Location.CLASSPATH);
        }).start(port);
        
        System.out.println("🚀 Server running on http://localhost:" + port);
        
        // ===== ROUTES =====
        
        // Home
        app.get("/", ctx -> {
            ctx.result("🍱 Funkin' Poke API - Visit /api/toppings");
        });
        
        // Health check
        app.get("/api/health", ctx -> {
            ctx.json(Map.of("status", "healthy"));
        });
        
        // Get all toppings
        app.get("/api/toppings", ctx -> {
            try {
                ArrayList<Topping> toppings = toppingDAO.getAllToppings();
                ctx.json(toppings);
            } catch (Exception e) {
                ctx.status(500);
                ctx.json(Map.of("error", "Failed to load toppings"));
                e.printStackTrace();
            }
        });
        
        // Create order
        app.post("/api/orders", ctx -> {
            try {
                // Parse JSON request body
                OrderRequest request = ctx.bodyAsClass(OrderRequest.class);
                
                // Validate
                if (request.customerName == null || request.customerName.trim().isEmpty()) {
                    ctx.status(400);
                    ctx.json(Map.of("error", "Customer name required"));
                    return;
                }
                
                if (request.bowls.isEmpty()) {
                    ctx.status(400);
                    ctx.json(Map.of("error", "Order must have at least one bowl"));
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
                    ctx.json(Map.of("error", "Failed to save order"));
                }
                
            } catch (Exception e) {
                ctx.status(400);
                ctx.json(Map.of("error", e.getMessage()));
                e.printStackTrace();
            }
        });
    }
    
    /**
     * Get port from environment variable (for deployment) or default to 8080.
     */
    private static int getPort() {
        String port = System.getenv("PORT");
        return port != null ? Integer.parseInt(port) : 8080;
    }
    
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
```

**✅ CHECKPOINT - Test with curl:**

**Terminal 1 (keep running):**
```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="com.richie.web.Main"
```

**Terminal 2 (test):**

```bash
# Test 1: Health check
curl http://localhost:8080/api/health

# Test 2: Get toppings
curl http://localhost:8080/api/toppings

# Test 3: Create order
curl -X POST http://localhost:8080/api/orders \
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

# Should return JSON with success: true
```

**Verify in database:**
```bash
psql foodhall -c "SELECT * FROM orders ORDER BY id DESC LIMIT 1;"
```

**🎉 HOURS 7-13 COMPLETE! REST API works perfectly.**

---

# 🏃 HOURS 13-17: FRONTEND

## Hour 13-14: Setup Static Files

### 📖 CONCEPT: How Javalin Serves Static Files

**Javalin looks for files in:** `src/main/resources/public/`

**File structure:**
```
src/main/resources/public/
├── index.html    (structure)
├── style.css     (styling)
└── app.js        (logic)
```

**Already configured in Main.java:**
```java
config.staticFiles.add("/public", Location.CLASSPATH);
```

### 💡 YOUR TURN: Create Frontend Files

**Create directory:**
```bash
mkdir -p src/main/resources/public
```

**File 1: index.html**

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Funkin' Poke</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
    <div class="container">
        <header>
            <h1>🍱 Funkin' Poke</h1>
            <p>Build Your Perfect Bowl</p>
        </header>

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

**File 2: style.css**

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
    color: #333;
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
    padding: 30px;
    border-radius: 5px;
    text-align: center;
}
```

**File 3: app.js**

```javascript
// ===== STATE =====
let toppings = [];
let cart = {
    customerName: '',
    bowls: [],
    drinks: [],
    sides: []
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
        alert('Failed to load toppings. Please refresh the page.');
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
                ${t.name} - $${t.price.toFixed(2)} ${t.premium ? '⭐' : ''}
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
        Tax (7%): $${tax.toFixed(2)}<br>
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
                    <h2>🎉 Order Placed Successfully!</h2>
                    <p style="font-size: 18px; margin-top: 10px;">
                        Order #${result.orderId}
                    </p>
                    <p style="font-size: 24px; margin-top: 10px; font-weight: bold;">
                        Total: $${result.total.toFixed(2)}
                    </p>
                    <p style="margin-top: 15px;">
                        Thank you, ${customerName}!
                    </p>
                    <button onclick="location.reload()" 
                            style="background: white; color: #4CAF50; padding: 10px 30px; 
                                   border: none; border-radius: 5px; font-size: 16px; 
                                   cursor: pointer; margin-top: 20px; font-weight: bold;">
                        Place Another Order
                    </button>
                </div>
            `;
        } else {
            alert('Failed to place order. Please try again.');
        }
    } catch (error) {
        console.error('Checkout failed:', error);
        alert('Failed to place order. Please check console for details.');
    }
}
```

**✅ CHECKPOINT - Test Complete Flow:**

```bash
# Start server
mvn clean compile
mvn exec:java -Dexec.mainClass="com.richie.web.Main"

# Open browser: http://localhost:8080

# Test flow:
# 1. Enter your name
# 2. Name a bowl
# 3. Select base and size
# 4. Check some toppings
# 5. Add to cart
# 6. Verify price looks correct
# 7. Checkout
# 8. See success message

# Verify in database:
psql foodhall -c "SELECT * FROM orders ORDER BY id DESC LIMIT 1;"
```

**🎉 HOURS 13-17 COMPLETE! Full-stack app works locally!**

---

# 🏃 HOURS 17-20: DEPLOYMENT

## Hour 17-18: Prepare for Production

### Create .gitignore

```
target/
.idea/
*.iml
.DS_Store
*.class
```

### Test JAR Build

```bash
mvn clean package

# Test locally
java -jar target/foodhall-1.0-SNAPSHOT.jar

# Visit http://localhost:8080
# Stop with Ctrl+C
```

### Push to GitHub

```bash
git init
git add .
git commit -m "Ready for deployment"

# Create repo on GitHub, then:
git remote add origin https://github.com/YOUR_USERNAME/foodhall.git
git branch -M main
git push -u origin main
```

---

## Hour 18-19: Deploy to Render

### Step 1: Create Render Account

1. Go to https://render.com
2. Sign up with GitHub
3. Authorize Render

### Step 2: Deploy PostgreSQL

1. Click "New +" → "PostgreSQL"
2. Name: `foodhall-db`
3. Database: `foodhall`
4. Region: Oregon (US West)
5. Plan: **Free**
6. Click "Create Database"
7. Wait ~2 minutes

### Step 3: Seed Production Database

1. Click your database
2. Click "Connect"
3. Copy the PSQL command
4. Run in terminal
5. Paste entire schema.sql content
6. Verify: `SELECT COUNT(*) FROM toppings;`
7. Exit: `\q`

### Step 4: Deploy Web Service

1. Click "New +" → "Web Service"
2. Connect GitHub repo
3. Settings:
    - Name: `foodhall`
    - Region: Oregon
    - Branch: `main`
    - Build: `mvn clean package`
    - Start: `java -jar target/foodhall-1.0-SNAPSHOT.jar`
    - Plan: **Free**
4. Environment Variables:
    - Key: `DATABASE_URL`
    - Click "Generate" → Select database
5. Click "Create Web Service"
6. Wait 10-15 minutes

### Step 5: Test Production

1. Visit your Render URL
2. First load takes ~50 seconds (cold start)
3. Test full flow
4. **SUCCESS!** 🎉

---

## Hour 19-20: Documentation

### Create README.md

```markdown
# 🍱 Funkin' Poke - Web App

Build custom poke bowls online. Fresh toppings, instant checkout.

## 🚀 [Live Demo](https://your-app.onrender.com)

*First load may take 50 seconds (free hosting)*

## ✨ Features

- Custom poke bowl builder
- 26+ premium and regular toppings
- Real-time price calculation
- PostgreSQL persistence
- Responsive design

## 🛠️ Tech Stack

- Java 17 + Javalin
- PostgreSQL + JDBC
- Vanilla JavaScript
- Deployed on Render

## 📦 Local Setup

```bash
git clone https://github.com/yourusername/foodhall.git
createdb foodhall
psql foodhall < schema.sql
mvn clean package
java -jar target/foodhall-1.0-SNAPSHOT.jar
```

Visit http://localhost:8080

## 🎓 Built in 20 hours

CLI app → deployed web app as a learning project.
```

**Commit and push:**
```bash
git add README.md
git commit -m "Add documentation"
git push origin main
```

**🎉 HOURS 17-20 COMPLETE! YOU'RE DONE!**

---

# ✅ FINAL CHECKLIST

- [ ] Local app works
- [ ] Can place orders
- [ ] Orders save to database
- [ ] Deployed to Render
- [ ] Production app works
- [ ] GitHub repo has README
- [ ] No critical bugs

**If all checked: CONGRATULATIONS! 🎉🍱🚀**

---

# 🆘 TROUBLESHOOTING

**Database connection fails:**
- Check PostgreSQL is running
- Verify password in DatabaseConnection.java

**Maven build fails:**
- Check Java 17+: `java -version`
- Clear cache: `rm -rf ~/.m2/repository`
- Reinstall: `mvn clean install`

**Render deployment fails:**
- Check build logs
- Verify build command: `mvn clean package`
- Verify start command: `java -jar target/foodhall-1.0-SNAPSHOT.jar`
- Check DATABASE_URL is set

**Frontend not loading:**
- Files in `src/main/resources/public/`?
- Server restart needed?
- Check browser console for errors

---

**YOU DID IT! Amazing work.** 🎉

This is portfolio-ready. Share it proudly!
