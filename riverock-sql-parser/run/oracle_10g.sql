SELECT DISTINCT (street_address), (postal_code), (city) 
FROM  locations NATURAL_JOIN warehouse NATURAL_JOIN inventories NATURAL_JOIN products NATURAL_JOIN order_items NATURAL_JOIN orders 
where  order_status = 10;
