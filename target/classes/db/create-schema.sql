-- Create the product information table
CREATE TABLE PRODUCT
(
id number(11) NOT NULL auto_increment,
sku varchar(200) NOT NULL,
prod_name varchar(200) NOT NULL,
category varchar(200) NOT NULL,
last_updated date,
PRIMARY KEY (id)
);

-- Create the Product price table
CREATE TABLE PRICE
(
id number(11) not null PRIMARY KEY,
price DECIMAL(10,2),
FOREIGN KEY (id) REFERENCES PRODUCT(id)
        ON DELETE CASCADE
);
