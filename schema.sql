--clear
DROP TABLE IF EXISTS order_item_extras CASCADE;
DROP TABLE IF EXISTS order_item_toppings CASCADE;
DROP TABLE IF EXISTS order_items CASCADE;
DROP TABLE IF EXISTS orders CASCADE;
DROP TABLE IF EXISTS drinks CASCADE;
DROP TABLE IF EXISTS sides CASCADE;
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

--sides
CREATE TABLE sides (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

--drinks
CREATE TABLE drinks (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
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
    item_type VARCHAR(20) NOT NULL,
    item_name VARCHAR(100),
    base VARCHAR(50),
    size VARCHAR(10),
    flavor VARCHAR(100),
    side_type VARCHAR(100),
    price DECIMAL(10, 2) NOT NULL
);

--product + toppings (for bowls)
CREATE TABLE order_item_toppings (
    id SERIAL PRIMARY KEY,
    order_item_id INTEGER NOT NULL REFERENCES order_items(id) ON DELETE CASCADE,
    topping_id INTEGER NOT NULL REFERENCES toppings(id),
    price DECIMAL(10, 2) NOT NULL
);

--product + extras (for bowls)
CREATE TABLE order_item_extras (
    id SERIAL PRIMARY KEY,
    order_item_id INTEGER NOT NULL REFERENCES order_items(id) ON DELETE CASCADE,
    topping_id INTEGER NOT NULL REFERENCES toppings(id),
    upcharge DECIMAL(10, 2) NOT NULL
);

--add restaurant
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

--create sides (just names)
INSERT INTO sides (name) VALUES
('Edamame'),
('Seaweed Salad'),
('Miso Soup'),
('Cucumber Salad'),
('Kimchi'),
('Crab Salad'),
('Spicy Tuna Mix'),
('Spicy Crab Mix'),
('Salmon Poke Scoop'),
('Tuna Poke Scoop'),
('Gyoza'),
('Takoyaki'),
('Spring Rolls'),
('Crispy Tofu Bites'),
('Tamago Slices'),
('Mini Rice Bowl'),
('Imitation Crab'),
('Mango Slices'),
('Pineapple Cubes'),
('Wasabi Peas');

--create drinks (just names)
INSERT INTO drinks (name) VALUES
('Green Tea'),
('Mango Iced Tea'),
('Lychee Lemonade'),
('Yuzu Soda'),
('Coconut Water'),
('Passion Fruit Juice'),
('Matcha Milk Tea'),
('Thai Iced Tea'),
('Pineapple Refresher'),
('Watermelon Refresher'),
('Strawberry Lemonade'),
('Peach Green Tea'),
('Honeydew Juice'),
('Brown Sugar Milk Tea'),
('Taro Milk Tea'),
('Black Milk Tea'),
('Jasmine Milk Tea'),
('Rose Milk Tea'),
('Lavender Lemonade'),
('Sparkling Lychee Water');