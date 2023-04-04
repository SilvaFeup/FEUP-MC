/*Tables: User, Product, ShoppingBasket, ShoppingBasketProduct, Order, OrderItem, PaymentCard, Qrcode */

DROP TABLE IF EXISTS User;
DROP TABLE IF EXISTS Product;
DROP TABLE IF EXISTS ShoppingBasket;
DROP TABLE IF EXISTS ShoppingBasketProduct;
DROP TABLE IF EXISTS OrderInfo;
DROP TABLE IF EXISTS OrderItem;
DROP TABLE IF EXISTS PaymentCard;
DROP TABLE IF EXISTS Qrcode;


CREATE TABLE User (

    id INTEGER PRIMARY KEY,
    email TEXT NOT NULL UNIQUE,
    username TEXT NOT NULL UNIQUE,
    password TEXT NOT NULL,
    name TEXT NOT NULL,
    taxpayer_number TEXT NOT NULL,
    public_key TEXT NOT NULL,
    payment_card INTEGER REFERENCES PaymentCard(id)

);

CREATE TABLE Product (
    id INTEGER PRIMARY KEY,
    name TEXT NOT NULL,
    price_tag FLOAT NOT NULL,
    qr_code_id INTEGER REFERENCES Qrcode(id)
);


CREATE TABLE ShoppingBasket (
    id INTEGER PRIMARY KEY,
    accumulated_price FLOAT DEFAULT 0.0,
    n_items INTEGER DEFAULT 0,
    owner INTEGER REFERENCES User(id)

);

CREATE TABLE ShoppingBasketProduct (
    id INTEGER PRIMARY KEY,
    product INTEGER REFERENCES Product(id),
    shop_basket INTEGER REFERENCES ShoppingBasket(id),
    quantity INTEGER DEFAULT 1

);


CREATE TABLE OrderInfo (

    id INTEGER PRIMARY KEY,
    total_amount DECIMAL(10,2),
    customer_id INT REFERENCES User(id),
    voucher TEXT DEFAULT '0',
    date_order DATE DEFAULT CURRENT_DATE


);


CREATE TABLE OrderItem (

    id INTEGER PRIMARY KEY,
    order_id INTEGER REFERENCES OrderInfo(id),
    final_quantity INTEGER DEFAULT 1,
    product INTEGER REFERENCES User(id)
);


CREATE TABLE PaymentCard (
    id INT PRIMARY KEY,
    card_number VARCHAR(16) NOT NULL,
    card_holder_name VARCHAR(255) NOT NULL,
    expiration_month INT NOT NULL,
    expiration_year INT NOT NULL,
    cvv_code VARCHAR(4) NOT NULL,
    user_id INTEGER REFERENCES User(id)
);



CREATE TABLE Qrcode (
  id INT PRIMARY KEY,
  qr_code_data VARCHAR(255)
);


INSERT INTO User (email,username, password, name, taxpayer_number, public_key,payment_card) VALUES(
    'silva@gmail.com',
    'silva_cm',
    '25d55ad283aa400af464c76d713c07ad',
    'Joao',
    '222 222 222',  
    'OpenSSLRSAPublicKey{modulus=b2793dc5472b4928df549105133e1110e2c987c75c9c5ec81b7007de370a6268bd48e4aadb1266b8abb8f9e5ff21,publicExponent=10001}',
    '123456789'
);


