CREATE TABLE tb_products (
    code varchar(13) PRIMARY KEY NOT NULL UNIQUE,
    product_type_name varchar(32) REFERENCES tb_product_types NOT NULL ON UPDATE CASCADE ON DELETE CASCADE,
    name varchar(42) NOT NULL,
    description text,
    purchase_price_in_cents int NOT NULL,
    previous_purchase_price_in_cents int,
    sale_price_in_cents int NOT NULL,
    previous_sale_price_in_cents int,
    price_update_date timestamp NOT NULL,
    full_stock boolean NOT NULL
);