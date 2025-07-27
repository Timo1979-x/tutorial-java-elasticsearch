## Aggregation With 1:N Objects

### Demo

```
# delete index
DELETE /stores

# we have this data structure just for demo purposes
# create index with the mapping
PUT /stores
{
  "mappings": {
    "properties": {
      "name": { "type": "text" },
      "location": { "type": "keyword" },
      "products": {
        "type": "nested",
        "properties": {
            "name": { "type": "text" },
            "category": { "type": "keyword" },     
            "price": { "type": "integer" }                      
        }
      }
    }
  }
}

# insert records
POST /stores/_bulk
{ "create": { "_id": "1" } }
{
    "store_name": "Tech Haven",
    "location": "New York",
    "products": [
      { "name": "Laptop", "category": "Electronics", "price": 1200 },
      { "name": "Smartphone", "category": "Electronics", "price": 800 },
      { "name": "Headphones", "category": "Accessories", "price": 150 },
      { "name": "Smartwatch", "category": "Electronics", "price": 250 }
    ]
}
{ "create": { "_id": "2" } }
{
    "store_name": "Gourmet Delights",
    "location": "California",
    "products": [
      { "name": "Olive Oil", "category": "Groceries", "price": 20 },
      { "name": "Truffle Cheese", "category": "Dairy", "price": 35 },
      { "name": "Wine", "category": "Beverages", "price": 50 },
      { "name": "Dark Chocolate", "category": "Snacks", "price": 15 },
      { "name": "Coffee Beans", "category": "Beverages", "price": 30 }
    ]
}
{ "create": { "_id": "3" } }
{
    "store_name": "Sports Central",
    "location": "Texas",
    "products": [
      { "name": "Basketball", "category": "Sports", "price": 30 },
      { "name": "Tennis Racket", "category": "Sports", "price": 100 },
      { "name": "Running Shoes", "category": "Footwear", "price": 120 },
      { "name": "Yoga Mat", "category": "Sports", "price": 40 }
    ]
}
{ "create": { "_id": "4" } }
{
    "store_name": "Fashion Hub",
    "location": "California",
    "products": [
      { "name": "Jeans", "category": "Clothing", "price": 60 },
      { "name": "Sneakers", "category": "Footwear", "price": 90 },
      { "name": "Leather Jacket", "category": "Clothing", "price": 200 },
      { "name": "Sunglasses", "category": "Accessories", "price": 80 },
      { "name": "Wristwatch", "category": "Accessories", "price": 150 },
      { "name": "Backpack", "category": "Accessories", "price": 50 }
    ]
}
{ "create": { "_id": "5" } }
{
    "store_name": "Home Essentials",
    "location": "New York",
    "products": [
      { "name": "Vacuum Cleaner", "category": "Home Appliances", "price": 250 },
      { "name": "Air Purifier", "category": "Home Appliances", "price": 180 },
      { "name": "LED Lamp", "category": "Lighting", "price": 40 },
      { "name": "Curtains", "category": "Home Decor", "price": 70 },
      { "name": "Wall Clock", "category": "Home Decor", "price": 35 },
      { "name": "Kitchen Knife Set", "category": "Kitchenware", "price": 60 },
      { "name": "Blender", "category": "Kitchenware", "price": 90 },
      { "name": "Toaster", "category": "Kitchenware", "price": 50 }
    ]
}

# aggregation within nested objects - price stats
POST /stores/_search
{
  "size": 0, 
  "aggs": {
    "nested-products":{
      "nested": {
        "path": "products"
      },
      "aggs": {
        "price-stats": {
          "stats": {
            "field": "products.price"
          }
        }
      }
    }
  }
}

# aggregation within nested objects - group by category
POST /stores/_search
{
  "size": 0, 
  "aggs": {
    "nested-products":{
      "nested": {
        "path": "products"
      },
      "aggs": {
        "group-by-category-nested": {
          "terms": {
            "field": "products.category"
          }
        }
      }
    }
  }
}

```