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
    uuid INTEGER NOT NULL UNIQUE,
    username TEXT NOT NULL UNIQUE,
    password TEXT NOT NULL,
    name TEXT NOT NULL,
    public_key TEXT NOT NULL,
    accumulated_discount FLOAT DEFAULT 0.0,
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
    voucher_id INT REFERENCES Voucher(id) DEFAULT -1,
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
    cvv_code VARCHAR(4) NOT NULL
);



CREATE TABLE Qrcode (
  id INT PRIMARY KEY,
  qr_code_data VARCHAR(255)
);


CREATE TABLE Voucher(
    id INT AUTOINCREMENT PRIMARY KEY,
    code VARCHAR(255) NOT NULL UNIQUE
);





