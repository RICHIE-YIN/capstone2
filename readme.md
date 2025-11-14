# 🍱 **The Poke Spot — Full Stack Ordering System**
**CLI → JDBC → PostgreSQL → Javalin → Frontend → Deployment**  
Originally a Capstone 2 CLI project.  
Evolved into a full-stack, database-backed, API-driven ordering kiosk.

---

# 🚀 **Overview**
Capstone 2 required:  
➡️ *Build an advanced OOP Java CLI (Sandwich Shop)*  
What I delivered instead:  
➡️ A fully functional **restaurant kiosk ordering ecosystem** with:

- A polished **Java CLI Poke Shop**
- **PostgreSQL** database integration using **raw JDBC**
- A fully designed **Javalin REST API**
- A complete **frontend UI** built with HTML/CSS/JS**
- **Downloadable receipts**
- **Full deployment**, ready for live demo

I finished the CLI early… so I built the whole stack.

---

# 🧱 **1. CLI Application — The Core**
Before the database, API, or UI existed, Funkin’ Poke started as a **pure Java OOP console application**. This CLI established the architecture that made full-stack expansion easy.

## 💡 **Core OOP Design**
### **Parent Class**
#### `Product`
- Base class for all menu items
- Contains `name`, `price`, and formatting helpers
- Used by drinks, sides, toppings, bowls, and extras

### **Interfaces**
#### `Customizable`
For anything that can add toppings (e.g. poke bowls)

#### `Taxable`
Defines tax behavior — clean separation from pricing logic

---

# 🥗 **2. Bowl System (Custom + Signature)**
### **`PokeBowl`**
A fully customizable bowl with:
- Base price
- List of toppings
- Premium proteins
- Extra items
- Automatic price + tax calculations

### **`SignaturePokeBowl`**
Preconfigured bowls that:
- Extend `PokeBowl`
- Come loaded with preset toppings
- Still allow customization

---

# 🍣 **3. Toppings, Extras, Drinks, and Sides**
### **`Topping`**
- Name
- Price
- Indicates if it's premium (protein)

### **`Extra`**
- Wraps a `Topping`
- Automatically calculates upcharge:
    - +$2.00 (premium protein)
    - +$0.50 (regular topping)
- Displays as “Extra ___”

### **`Drink`**, **`Side`**
- Extend `Product`
- Simple and clean

---

# 📦 **4. Order Processing**
### **`Order`**
Each order tracks:
- Customer name
- Line items
- Subtotal
- Tax
- Total
- Full receipt text

### **Receipt Generation**
#### `PriceFormatter`
- Ensures consistent `$#.##` formatting

#### `ReceiptFileManager`
- Generates `.txt` receipt files locally
- Used when exiting the CLI

---

# 🖥️ **5. CLI User Flow**
The CLI was designed like a real ordering kiosk.

### **Main Menu**
```
Press 1 to order | Press 2 to quit
```

### **Order Menu**
Options include:
- Build Your Own Bowl
- Signature Bowls
- Sides
- Drinks
- Extras
- View Current Order
- Checkout
- Cancel Order (returns to main)

### **Checkout**
- Shows full receipt
- Saves file
- Adds to session order list

---

# 🗄️ **6. PostgreSQL Integration (JDBC)**
### **Database Tables**
- `toppings`
- `drinks`
- `sides`
- `extras`
- `orders`

### **DAO Layer**
- `ToppingDAO`
- `SidesDAO`
- `DrinksDAO`
- `ExtraDAO`
- `OrderDAO`

### **Features**
- Pure JDBC
- Prepared statements
- Auto-ID with:
  ```sql
  SELECT COALESCE(MAX(id), 0) + 1 FROM orders;
  ```

### **DatabaseConnection**
Supports:
- Local PostgreSQL
- Production (`DATABASE_URL` env var)

---

# 🔌 **7. Javalin REST API**
### **Endpoints**
#### Health:
```
GET /api/health
```

#### Menu:
```
GET /api/toppings
GET /api/drinks
GET /api/sides
```

#### Orders:
```
POST /api/orders
GET  /api/orders/{id}
```

---

# 🖥️ **8. Frontend Web Application**
### **Features**
- Topping selection
- Premium add-ons
- Extras
- Order preview
- Checkout
- Receipt preview
- Receipt download (.txt)

### **Structure**
```
/front
  index.html
  styles.css
  app.js
```

---

# 🤝 **9. Full Stack Interaction**
```
[Frontend] → fetch() → [Javalin API] → JDBC → [PostgreSQL]
[CLI] → DAO → DB → Local receipts
```

---

# ☁️ **10. Deployment**
- Backend & DB: Railway
- Frontend: Static hosting
- Environment variables configured

---

# 📦 **11. Run Locally**
### Backend:
```bash
mvn clean compile exec:java -Dexec.mainClass="com.richie.web.Main"
```

### Env Vars:
```
DATABASE_URL=postgres://user:pass@host:5432/dbname
```

### Frontend:
Open `index.html`

---

# 🤖 **12. AI Usage**
AI was used responsibly:

- To build a custom workbook for JDBC/API/frontend learning
- For debugging
- For boilerplate text generation
- For ideas and speed

All architecture, logic, and implementation were written by me.
AI acted as a coach, not a coder.

---

# 🏁 **Conclusion**
This project started as a CLI…  
and ended as a fully deployed, database-backed, API-driven ordering kiosk.

Richie-built. End to end.
