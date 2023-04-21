/*Tables: User, Product, ShoppingBasket, ShoppingBasketProduct, Order, OrderItem, PaymentCard, Qrcode */

DROP TABLE IF EXISTS User;
DROP TABLE IF EXISTS Product;
DROP TABLE IF EXISTS ShoppingBasket;
DROP TABLE IF EXISTS ShoppingBasketProduct;
DROP TABLE IF EXISTS OrderInfo;
DROP TABLE IF EXISTS OrderItem;
DROP TABLE IF EXISTS PaymentCard;
DROP TABLE IF EXISTS Voucher;


CREATE TABLE User (

    id INTEGER PRIMARY KEY,
    uuid VARCHAR(255) NOT NULL UNIQUE,
    username TEXT NOT NULL UNIQUE,
    password TEXT NOT NULL,
    name TEXT NOT NULL,
    public_key TEXT NOT NULL,
    accumulated_discount FLOAT DEFAULT 0.0,
    total_paid_since_last_voucher FLOAT DEFAULT 0.0,
    payment_card INTEGER REFERENCES PaymentCard(id)

);

CREATE TABLE Product (
    id INTEGER PRIMARY KEY,
    uuid VARCHAR(255) NOT NULL UNIQUE,
    name TEXT NOT NULL,
    price_tag FLOAT NOT NULL
);


CREATE TABLE OrderInfo (

    id INTEGER PRIMARY KEY,
    total_amount DECIMAL(10,2),
    customer_id INTEGER REFERENCES User(id),
    voucher_id INTEGER REFERENCES Voucher(id) DEFAULT -1,
    date_order DATE DEFAULT CURRENT_DATE


);


CREATE TABLE OrderItem (

    id INTEGER PRIMARY KEY,
    order_id INTEGER REFERENCES OrderInfo(id),
    final_quantity INTEGER DEFAULT 1,
    product INTEGER REFERENCES Product(id)
);


CREATE TABLE PaymentCard (
    id INTEGER PRIMARY KEY,
    card_number VARCHAR(16) NOT NULL,
    card_holder_name VARCHAR(255) NOT NULL,
    expiration_month INTEGER NOT NULL,
    expiration_year INTEGER NOT NULL,
    cvv_code VARCHAR(4) NOT NULL
);



CREATE TABLE Voucher(
    id INTEGER PRIMARY KEY,
    uuid VARCHAR(255) NOT NULL UNIQUE,
    owner INTEGER REFERENCES User(id)
);


INSERT INTO Product (uuid, name, price_tag) VALUES ('625a0656-d7b6-11ed-afa1-0242ac120002', 'product0 ', 3.99);
INSERT INTO Product (uuid, name, price_tag) VALUES ('77f2fe28-d7b6-11ed-afa1-0242ac120002', 'product1 ', 100.0);
INSERT INTO Product (uuid, name, price_tag) VALUES ('807e8a08-d7b6-11ed-afa1-0242ac120002', 'product2 ', 0.95);