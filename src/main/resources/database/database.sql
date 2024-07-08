CREATE TABLE Supplier (
                          Supplier_id varchar(50) PRIMARY KEY,
                          Supplier_name varchar(50),
                          Supplier_address varchar(100),
                          Supplier_email varchar(100),
                          Supplier_telephone varchar(10)
);

CREATE TABLE Stock (
                       Stock_id varchar(50) PRIMARY KEY,
                       Qty varchar(16000)
);

CREATE TABLE Items (
                       Item_code varchar(50) PRIMARY KEY,
                       Description varchar(100),
                       unit_price double,
                       qty_on_hand int(100),
                       buying_price double
);

CREATE TABLE Staff (
                       Staff_id varchar(50) PRIMARY KEY,
                       Name varchar(100),
                       Address varchar(100),
                       Age varchar(3),
                       Contact_number varchar(10),
                       Job_Role varchar(20)
);

CREATE TABLE Item_order_details (
                                    Order_id varchar(50),
                                    Item_code varchar(50),
                                    Qty int,
                                    Date DATE,
                                    PRIMARY KEY (Order_id, Item_code),
                                    FOREIGN KEY (Order_id) REFERENCES Orders(Order_id) ON UPDATE CASCADE ON DELETE CASCADE,
                                    FOREIGN KEY (Item_code) REFERENCES Items(Item_code) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE Package (
                         Package_id varchar(50) PRIMARY KEY,
                         Package_type varchar(50),
                         Qty varchar(1000),
                         Price decimal(10,2)
);

CREATE TABLE Customer (
                          Customer_id varchar(50) PRIMARY KEY,
                          Name varchar(100),
                          Address varchar(100),
                          Contact_number varchar(10)
);

CREATE TABLE Orders (
                        Order_id varchar(50) PRIMARY KEY,
                        Customer_id varchar(50),
                        Date DATE,
                        FOREIGN KEY (Customer_id) REFERENCES Customer(Customer_id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE Order_package_details (
                                       Order_id varchar(50),
                                       Package_id varchar(50),
                                       Qty INT,
                                       unitPrice double,
                                       PRIMARY KEY (Order_id, Package_id),
                                       FOREIGN KEY (Order_id) REFERENCES Orders(Order_id) ON UPDATE CASCADE ON DELETE CASCADE,
                                       FOREIGN KEY (Package_id) REFERENCES Package(Package_id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE User (
                      User_id varchar(50) Primary key ,
                      First_name varchar(50),
                      Last_name varchar(50),
                      Email varchar(100),
                      Password varchar(15)
);
