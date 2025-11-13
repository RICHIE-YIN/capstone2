--clear
DROP TABLE IF EXISTS order_item_toppings CASCADE;
DROP TABLE IF EXISTS order_items CASCADE;
DROP TABLE IF EXISTS orders CASCADE;
DROP TABLE IF EXISTS toppings CASCADE;
DROP TABLE IF EXISTS restaurants CASCADE;

--restaurant
CREATE TABLE restaurants (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    cuisine_type VARCHAR(50),
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

--toppings
CREATE TABLE toppings (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    base_price DECIMAL(10, 2) NOT NULL,
    is_premium BOOLEAN DEFAULT false
);

--orders
CREATE TABLE orders (
    id SERIAL PRIMARY KEY,
    customer_name VARCHAR(100) NOT NULL,
    subtotal DECIMAL(10, 2) NOT NULL,
    tax DECIMAL(10, 2) NOT NULL,
    total DECIMAL(10, 2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

--products (bowl/drink/sides)
CREATE TABLE order_items (
    id SERIAL PRIMARY KEY,
    order_id INTEGER NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
    item_type VARCHAR(20) NOT NULL,  -- product type
    item_name VARCHAR(100),
    base VARCHAR(50),                 -- base for bowls
    size VARCHAR(10),                 -- s, m, or l for bowls
    flavor VARCHAR(100),              -- for drinks
    side_type VARCHAR(100),           -- for sides
    price DECIMAL(10, 2) NOT NULL
);

--product + toppings (for bowls)
CREATE TABLE order_item_toppings (
    id SERIAL PRIMARY KEY,
    order_item_id INTEGER NOT NULL REFERENCES order_items(id) ON DELETE CASCADE,
    topping_id INTEGER NOT NULL REFERENCES toppings(id),
    price DECIMAL(10, 2) NOT NULL    -- Price snapshot at time of order
);

--adds to restaurant
INSERT INTO restaurants (name, cuisine_type, description)
VALUES ('Funkin'' Poke', 'Hawaiian', 'Fresh poke bowls with premium toppings');

--create toppings
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