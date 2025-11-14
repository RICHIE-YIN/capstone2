```declarative

psql foodhall

psql foodhall -f schema.sql

SELECT * FROM toppings;

SELECT * FROM drinks;

SELECT * FROM sides;
```
```declarative
mvn clean compile exec:java -Dexec.mainClass="com.richie.web.Main"

curl -X GET http://localhost:8080/api/health

curl -X GET http://localhost:8080/api/toppings

curl -X GET http://localhost:8080/api/sides

curl -X GET http://localhost:8080/api/drinks

curl -X GET http://localhost:8080/api/signature-bowls
```

POST: Signature bowl order (with extras)
Tests:
signature: true
buildSignatureBowl
Extras pricing
Drink added
```declarative
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "customerName": "Richie",
    "bowls": [
      {
        "name": "Richie Special",
        "signature": true,
        "toppings": [],
        "extras": [
          { "toppingName": "Spicy Tuna" },
          { "toppingName": "Avocado" }
        ]
      }
    ],
    "drinks": [
      { "size": "M", "flavor": "Lavender Lemonade" }
    ],
    "sides": []
  }'

```
POST: Custom bowl with toppings + extras + drink + side
Tests:
Custom PokeBowl
Basic toppings array → DAO lookup
Extras (extra meat / extra toppings)
All three item types in one order
```declarative
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "customerName": "Kayla",
    "bowls": [
      {
        "name": "Kaylas Custom",
        "base": "White Rice",
        "size": "M",
        "signature": false,
        "toppings": [
          "Spicy Salmon",
          "Crab Mix",
          "Avocado",
          "Cucumber",
          "Spicy Mayo",
          "Eel Sauce"
        ],
        "extras": [
          { "toppingName": "Spicy Salmon" },
          { "toppingName": "Avocado" }
        ]
      }
    ],
    "drinks": [
      { "size": "L", "flavor": "Thai Iced Tea" }
    ],
    "sides": [
      { "type": "Takoyaki" }
    ]
  }'
```
POST: Signature-only order (no drinks/sides)
```declarative
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "customerName": "Bowl Only",
    "bowls": [
      {
        "name": "Spicy Volcano",
        "signature": true,
        "toppings": [],
        "extras": []
      }
    ],
    "drinks": [],
    "sides": []
  }'
```
POST: Drink-only order (tests validator "no bowl but drink/side is fine")
```declarative
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "customerName": "Thirsty Only",
    "bowls": [],
    "drinks": [
      { "size": "S", "flavor": "Green Tea" }
    ],
    "sides": []
  }'
```
POST: INVALID order (empty – should hit OrderValidator)
```declarative
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "customerName": "Ghost Order",
    "bowls": [],
    "drinks": [],
    "sides": []
  }'
```
POST: INVALID signature name (tests buildSignatureBowl error path)
```declarative
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "customerName": "Typer",
    "bowls": [
      {
        "name": "Richie Speical",   // typo on purpose
        "signature": true,
        "toppings": [],
        "extras": []
      }
    ],
    "drinks": [],
    "sides": []
  }'cd pr
```