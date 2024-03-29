CREATE TABLE tb_product_types (
    name varchar(32) PRIMARY KEY NOT NULL UNIQUE,
    category category,
    average_price_in_cents int,
    full_stock_factor int2 NOT NULL
);