const app = {
    // Data
    toppings: [
        {name: 'Salmon', price: 2.00, premium: true},
        {name: 'Tuna', price: 2.25, premium: true},
        {name: 'Spicy Tuna', price: 2.50, premium: true},
        {name: 'Spicy Salmon', price: 2.50, premium: true},
        {name: 'Shrimp', price: 1.75, premium: true},
        {name: 'Tofu', price: 1.00, premium: true},
        {name: 'Crab Mix', price: 1.25, premium: true},
        {name: 'Avocado', price: 1.00, premium: true},
        {name: 'Seaweed Salad', price: 0.75, premium: false},
        {name: 'Cucumber', price: 0.25, premium: false},
        {name: 'Mango', price: 0.50, premium: false},
        {name: 'Green Onion', price: 0.25, premium: false},
        {name: 'Masago', price: 0.75, premium: false},
        {name: 'Pickled Ginger', price: 0.25, premium: false},
        {name: 'JalapeÃ±o', price: 0.25, premium: false},
        {name: 'Nori', price: 0.10, premium: false},
        {name: 'Spicy Mayo', price: 0.50, premium: false},
        {name: 'Eel Sauce', price: 0.50, premium: false},
        {name: 'Ponzu Sauce', price: 0.50, premium: false},
        {name: 'Sesame Oil', price: 0.25, premium: false},
        {name: 'Yuzu Dressing', price: 0.50, premium: false},
        {name: 'Wasabi Aioli', price: 0.75, premium: false},
        {name: 'Sriracha', price: 0.25, premium: false},
        {name: 'Furikake', price: 0.25, premium: false},
        {name: 'Crispy Onions', price: 0.50, premium: false},
        {name: 'Tempura Flakes', price: 0.50, premium: false}
    ],

    drinks: [
        'Green Tea', 'Mango Iced Tea', 'Lychee Lemonade', 'Yuzu Soda',
        'Coconut Water', 'Passion Fruit Juice', 'Matcha Milk Tea', 'Thai Iced Tea',
        'Pineapple Refresher', 'Watermelon Refresher', 'Strawberry Lemonade',
        'Peach Green Tea', 'Honeydew Juice', 'Brown Sugar Milk Tea', 'Taro Milk Tea',
        'Black Milk Tea', 'Jasmine Milk Tea', 'Rose Milk Tea', 'Lavender Lemonade',
        'Sparkling Lychee Water'
    ],

    sides: [
        'Edamame', 'Seaweed Salad', 'Miso Soup', 'Cucumber Salad', 'Kimchi',
        'Crab Salad', 'Spicy Tuna Mix', 'Spicy Crab Mix', 'Salmon Poke Scoop',
        'Tuna Poke Scoop', 'Gyoza', 'Takoyaki', 'Spring Rolls', 'Crispy Tofu Bites',
        'Tamago Slices', 'Mini Rice Bowl', 'Imitation Crab', 'Mango Slices',
        'Pineapple Cubes', 'Wasabi Peas'
    ],

    // State
    currentOrder: [],
    currentBowl: {
        size: null,
        sizePrice: 0,
        base: null,
        toppings: [],
        extraMeat: false,
        extraToppings: []
    },
    currentDrink: {
        size: null,
        price: 0,
        flavor: null
    },

    // Initialize
    init() {
        this.renderToppings();
        this.renderDrinkFlavors();
        this.renderSides();
        this.updateBowlPrice();
    },

    // Navigation
    showScreen(screenId) {
        document.querySelectorAll('.screen').forEach(screen => {
            screen.classList.remove('active');
        });
        document.getElementById(screenId).classList.add('active');
    },

    startOrder() {
        this.currentOrder = [];
        this.showScreen('menuScreen');
        this.updateOrderDisplay();
    },

    selectMenuItem(type) {
        if (type === 'bowl') {
            this.resetBowl();
            this.showScreen('bowlScreen');
        } else if (type === 'drink') {
            this.resetDrink();
            this.showScreen('drinkScreen');
        } else if (type === 'side') {
            this.showScreen('sideScreen');
        }
    },

    backToMenu() {
        this.showScreen('menuScreen');
    },

    cancelOrder() {
        if (confirm('Are you sure you want to cancel this order?')) {
            this.currentOrder = [];
            this.showScreen('welcomeScreen');
        }
    },

    // Bowl Building
    resetBowl() {
        this.currentBowl = {
            size: null,
            sizePrice: 0,
            base: null,
            toppings: [],
            extraMeat: false,
            extraToppings: []
        };

        // Reset UI
        document.querySelectorAll('#bowlScreen .size-option').forEach(el => {
            el.classList.remove('selected');
        });
        document.querySelectorAll('#bowlScreen .option-btn').forEach(el => {
            el.classList.remove('selected');
        });
        document.querySelectorAll('#toppingsList input').forEach(el => {
            el.checked = false;
        });
        document.querySelectorAll('#extraToppingsList input').forEach(el => {
            el.checked = false;
        });

        this.updateBowlPrice();
    },

    selectSize(size, price) {
        this.currentBowl.size = size;
        this.currentBowl.sizePrice = price;

        document.querySelectorAll('#bowlScreen .size-option').forEach(el => {
            el.classList.remove('selected');
        });
        event.currentTarget.classList.add('selected');

        this.updateBowlPrice();
    },

    selectBase(base) {
        this.currentBowl.base = base;

        document.querySelectorAll('#bowlScreen .option-btn').forEach(el => {
            if (el.textContent.trim() === base || el.textContent.includes(base)) {
                el.classList.add('selected');
            } else if (!el.onclick.toString().includes('toggleExtraMeat')) {
                el.classList.remove('selected');
            }
        });
    },

    toggleTopping(toppingName, checked) {
        if (checked) {
            const topping = this.toppings.find(t => t.name === toppingName);
            if (!this.currentBowl.toppings.find(t => t.name === toppingName)) {
                this.currentBowl.toppings.push(topping);
            }
        } else {
            this.currentBowl.toppings = this.currentBowl.toppings.filter(t => t.name !== toppingName);
        }
        this.updateBowlPrice();
    },

    toggleExtraMeat(hasExtra) {
        this.currentBowl.extraMeat = hasExtra;

        const buttons = document.querySelectorAll('#bowlScreen .builder-section:nth-child(5) .option-btn');
        buttons.forEach((btn, idx) => {
            if ((idx === 0 && hasExtra) || (idx === 1 && !hasExtra)) {
                btn.classList.add('selected');
            } else {
                btn.classList.remove('selected');
            }
        });

        this.updateBowlPrice();
    },

    toggleExtraTopping(toppingName, checked) {
        if (checked) {
            if (!this.currentBowl.extraToppings.includes(toppingName)) {
                this.currentBowl.extraToppings.push(toppingName);
            }
        } else {
            this.currentBowl.extraToppings = this.currentBowl.extraToppings.filter(t => t !== toppingName);
        }
        this.updateBowlPrice();
    },

    calculateBowlPrice() {
        let total = this.currentBowl.sizePrice;

        // Add topping prices with premium markup
        this.currentBowl.toppings.forEach(topping => {
            const price = topping.premium ? topping.price * 1.5 : topping.price;
            total += price;
        });

        // Add extra meat
        if (this.currentBowl.extraMeat) {
            total += 2.00;
        }

        // Add extra toppings
        total += this.currentBowl.extraToppings.length * 0.50;

        return total;
    },

    updateBowlPrice() {
        const price = this.calculateBowlPrice();
        document.getElementById('currentBowlPrice').textContent = price.toFixed(2);
    },

    addBowlToOrder() {
        if (!this.currentBowl.size) {
            alert('Please select a size for your bowl');
            return;
        }
        if (!this.currentBowl.base) {
            alert('Please select a base for your bowl');
            return;
        }

        const bowl = {
            type: 'bowl',
            ...this.currentBowl,
            price: this.calculateBowlPrice()
        };

        this.currentOrder.push(bowl);
        this.showNotification('Bowl added to order!');
        this.backToMenu();
        this.updateOrderDisplay();
    },

    // Drink Building
    resetDrink() {
        this.currentDrink = {
            size: null,
            price: 0,
            flavor: null
        };

        document.querySelectorAll('#drinkScreen .size-option').forEach(el => {
            el.classList.remove('selected');
        });
        document.querySelectorAll('#drinkScreen .option-btn').forEach(el => {
            el.classList.remove('selected');
        });

        document.getElementById('currentDrinkPrice').textContent = '0.00';
    },

    selectDrinkSize(size, price) {
        this.currentDrink.size = size;
        this.currentDrink.price = price;

        document.querySelectorAll('#drinkScreen .size-option').forEach(el => {
            el.classList.remove('selected');
        });
        event.currentTarget.classList.add('selected');

        document.getElementById('currentDrinkPrice').textContent = price.toFixed(2);
    },

    selectDrinkFlavor(flavor) {
        this.currentDrink.flavor = flavor;

        document.querySelectorAll('#drinkScreen .option-btn').forEach(el => {
            el.classList.remove('selected');
        });
        event.currentTarget.classList.add('selected');
    },

    addDrinkToOrder() {
        if (!this.currentDrink.size) {
            alert('Please select a drink size');
            return;
        }
        if (!this.currentDrink.flavor) {
            alert('Please select a drink flavor');
            return;
        }

        const drink = {
            type: 'drink',
            ...this.currentDrink
        };

        this.currentOrder.push(drink);
        this.showNotification('Drink added to order!');
        this.backToMenu();
        this.updateOrderDisplay();
    },

    // Sides
    selectSide(sideName) {
        const side = {
            type: 'side',
            name: sideName,
            price: 2.50
        };

        this.currentOrder.push(side);
        this.showNotification('Side added to order!');
        this.backToMenu();
        this.updateOrderDisplay();
    },

    // Order Management
    removeItem(index) {
        if (confirm('Remove this item from your order?')) {
            this.currentOrder.splice(index, 1);
            this.updateOrderDisplay();
            this.showNotification('Item removed');
        }
    },

    calculateOrderTotal() {
        return this.currentOrder.reduce((sum, item) => sum + item.price, 0);
    },

    updateOrderDisplay() {
        const itemsList = document.getElementById('orderItemsList');
        const itemCount = document.getElementById('itemCount');
        const orderTotal = document.getElementById('orderTotal');
        const checkoutBtn = document.getElementById('checkoutBtn');

        itemCount.textContent = this.currentOrder.length;
        orderTotal.textContent = this.calculateOrderTotal().toFixed(2);

        if (this.currentOrder.length === 0) {
            itemsList.innerHTML = `
                <div class="empty-order">
                    <span style="font-size: 4em;">🛒</span>
                    <p>Your order is empty. Add some items!</p>
                </div>
            `;
            checkoutBtn.disabled = true;
        } else {
            itemsList.innerHTML = this.currentOrder.map((item, index) => {
                return this.renderOrderItem(item, index);
            }).join('');
            checkoutBtn.disabled = false;
        }
    },

    renderOrderItem(item, index) {
        if (item.type === 'bowl') {
            const toppingsList = item.toppings.map(t =>
                `${t.name} ${t.premium ? 'â­' : ''}`
            ).join(', ');

            const extras = [];
            if (item.extraMeat) extras.push('Extra Meat');
            if (item.extraToppings.length > 0) {
                extras.push(`Extra: ${item.extraToppings.join(', ')}`);
            }

            return `
                <div class="order-item">
                    <button class="remove-item" onclick="app.removeItem(${index})">Ã—</button>
                    <h4>ðŸ¥— Poke Bowl - ${item.size.charAt(0).toUpperCase() + item.size.slice(1)}</h4>
                    <div class="order-item-details">
                        <strong>Base:</strong> ${item.base}<br>
                        ${toppingsList ? `<strong>Toppings:</strong> ${toppingsList}<br>` : ''}
                        ${extras.length > 0 ? `<strong>Extras:</strong> ${extras.join(', ')}` : ''}
                    </div>
                    <div class="order-item-price">$${item.price.toFixed(2)}</div>
                </div>
            `;
        } else if (item.type === 'drink') {
            return `
                <div class="order-item">
                    <button class="remove-item" onclick="app.removeItem(${index})">Ã—</button>
                    <h4>ðŸ¥¤ ${item.flavor}</h4>
                    <div class="order-item-details">
                        <strong>Size:</strong> ${item.size.charAt(0).toUpperCase() + item.size.slice(1)}
                    </div>
                    <div class="order-item-price">$${item.price.toFixed(2)}</div>
                </div>
            `;
        } else if (item.type === 'side') {
            return `
                <div class="order-item">
                    <button class="remove-item" onclick="app.removeItem(${index})">Ã—</button>
                    <h4>ðŸœ ${item.name}</h4>
                    <div class="order-item-price">$${item.price.toFixed(2)}</div>
                </div>
            `;
        }
    },

    // Checkout
    checkout() {
        if (this.currentOrder.length === 0) {
            alert('Please add items to your order first');
            return;
        }

        this.generateReceipt();
        this.showScreen('receiptScreen');
    },

    generateReceipt() {
        const now = new Date();
        const receiptNumber = now.getTime().toString().slice(-8);
        const receiptDate = now.toLocaleString();

        document.getElementById('receiptNumber').textContent = receiptNumber;
        document.getElementById('receiptDate').textContent = receiptDate;
        document.getElementById('receiptTotal').textContent = this.calculateOrderTotal().toFixed(2);

        const receiptItemsList = document.getElementById('receiptItemsList');
        receiptItemsList.innerHTML = this.currentOrder.map(item => {
            if (item.type === 'bowl') {
                const toppingsList = item.toppings.map(t =>
                    `${t.name} ${t.premium ? '(Premium)' : ''}`
                ).join(', ');

                const extras = [];
                if (item.extraMeat) extras.push('Extra Meat (+$2.00)');
                if (item.extraToppings.length > 0) {
                    extras.push(`Extra Toppings: ${item.extraToppings.join(', ')} (+$${(item.extraToppings.length * 0.50).toFixed(2)})`);
                }

                return `
                    <div class="receipt-item">
                        <h4>Poke Bowl - ${item.size.charAt(0).toUpperCase() + item.size.slice(1)} - $${item.price.toFixed(2)}</h4>
                        <div class="receipt-details">
                            Base: ${item.base}<br>
                            ${toppingsList ? `Toppings: ${toppingsList}<br>` : ''}
                            ${extras.length > 0 ? extras.join('<br>') : ''}
                        </div>
                    </div>
                `;
            } else if (item.type === 'drink') {
                return `
                    <div class="receipt-item">
                        <h4>${item.flavor} - ${item.size.charAt(0).toUpperCase() + item.size.slice(1)} - $${item.price.toFixed(2)}</h4>
                    </div>
                `;
            } else if (item.type === 'side') {
                return `
                    <div class="receipt-item">
                        <h4>${item.name} - $${item.price.toFixed(2)}</h4>
                    </div>
                `;
            }
        }).join('');
    },

    startNewOrder() {
        this.currentOrder = [];
        this.showScreen('welcomeScreen');
    },

    // Rendering
    renderToppings() {
        const toppingsList = document.getElementById('toppingsList');
        const extraToppingsList = document.getElementById('extraToppingsList');

        // Regular toppings
        toppingsList.innerHTML = this.toppings.map((topping, index) => {
            const price = topping.premium ? (topping.price * 1.5).toFixed(2) : topping.price.toFixed(2);
            return `
                <div>
                    <input type="checkbox"
                           id="topping-${index}"
                           class="topping-checkbox"
                           onchange="app.toggleTopping('${topping.name}', this.checked)">
                    <label for="topping-${index}"
                           class="topping-label ${topping.premium ? 'premium' : ''}">
                        ${topping.name}
                        <span class="price-tag">+$${price}</span>
                    </label>
                </div>
            `;
        }).join('');

        // Extra non-premium toppings
        const nonPremiumToppings = this.toppings.filter(t => !t.premium);
        extraToppingsList.innerHTML = nonPremiumToppings.map((topping, index) => {
            return `
                <div>
                    <input type="checkbox"
                           id="extra-${index}"
                           class="topping-checkbox"
                           onchange="app.toggleExtraTopping('${topping.name}', this.checked)">
                    <label for="extra-${index}" class="topping-label">
                        ${topping.name}
                        <span class="price-tag">+$0.50</span>
                    </label>
                </div>
            `;
        }).join('');
    },

    renderDrinkFlavors() {
        const drinkFlavorsList = document.getElementById('drinkFlavorsList');
        drinkFlavorsList.innerHTML = this.drinks.map(drink => {
            return `
                <button class="option-btn" onclick="app.selectDrinkFlavor('${drink}')">
                    ${drink}
                </button>
            `;
        }).join('');
    },

    renderSides() {
        const sidesList = document.getElementById('sidesList');
        sidesList.innerHTML = this.sides.map(side => {
            return `
                <button class="option-btn" onclick="app.selectSide('${side}')">
                    ${side}
                    <span class="price-tag">$2.50</span>
                </button>
            `;
        }).join('');
    },

    showNotification(message) {
        const notification = document.getElementById('notification');
        notification.textContent = message;
        notification.classList.add('show');

        setTimeout(() => {
            notification.classList.remove('show');
        }, 3000);
    }
};

// Initialize app when page loads
window.addEventListener('DOMContentLoaded', () => {
    app.init();
});