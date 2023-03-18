# Design


### Models (classes)

- Order
- OrderProduct (Quantity, total price)
- Product (Info ...)
- Voucher

## Customer App

### Pages

- Main activity (Layout with option to Register or Login)
    -if register -> Register Activity
    -if login -> login activity
        -Login successfull -> Home activity
        -Login no successfull -> RegisterAcivity
    
- Home Activity 
    - Order button (To see info about order and confirm it to checkout) -> order activity
    - Order Products buttons (To see products in order, add products to the order) -> order_products activity
    - User can see past orders
    - User can see his vouchers

- Order Activity
    - Can go to orderConfirm Activity (Where it's shown the price of total order and the vouchers that the user has)

- OrderConfirm Activity
    - If confirmed then generate qr code to pay and send order to Terminal App to do the checkout
    - If order successfull then go to OrderSuccess activity


- After success return to past transactions and vouchers activity


## Terminal App

- Read Qr Code or NFC activity / app (Sendind the order to server to make the payment and confirm information)

- Display Order (Result) Activity (Info about the order that just been sent and confirmed by the server)

### Pages


## Rest Service

- Server App (Keeps alot of info about the user, past transactions and vouchers)
- Responds to requests





