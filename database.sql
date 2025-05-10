use hmonahan2;

SELECT * FROM InvoiceItem;

DROP TABLE IF EXISTS InvoiceItem;
DROP TABLE IF EXISTS Invoice;
DROP TABLE IF EXISTS Item;
DROP TABLE IF EXISTS Email;
DROP TABLE IF EXISTS Address;
DROP TABLE IF EXISTS Company;
DROP TABLE IF EXISTS Person;

CREATE TABLE Person (
    personId INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    uuid CHAR(36) NOT NULL UNIQUE,
    firstName VARCHAR(255) NOT NULL,
    lastName VARCHAR(255) NOT NULL,
    phone VARCHAR(20),
    createdDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    lastModified TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_person_name (lastName, firstName)
);

CREATE TABLE Company (
    companyId INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    uuid CHAR(36) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    contactId INT NOT NULL,
    FOREIGN KEY (contactId) REFERENCES Person(personId) ON DELETE CASCADE,
    createdDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE Address (
    addressId INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    street VARCHAR(255) NOT NULL,
    city VARCHAR(255) NOT NULL,
    state varchar(255) not null,
    zip int not null,
    companyId int not null,
    foreign key (companyId) references Company(companyId)
);

CREATE TABLE Email (
    emailId INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    address VARCHAR(255) NOT NULL,
    personId INT NOT NULL,
    isPrimary BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (personId) REFERENCES Person(personId) ON DELETE CASCADE,
    CONSTRAINT unique_email_address UNIQUE (address)
);

CREATE TABLE Item (
    itemId INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    uuid CHAR(36) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    type ENUM('E', 'M', 'C') NOT NULL COMMENT 'E=Equipment, M=Material, C=Contract',
    price DECIMAL(10,2) ,
    modelNumber VARCHAR(255),
    unit VARCHAR(50),
    subcontractorId INT,
    FOREIGN KEY (subcontractorId) REFERENCES Company(companyId) ON DELETE CASCADE,
    createdDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_item_name (name)
);

CREATE TABLE Invoice (
    invoiceId INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    customerId INT NOT NULL,
    salespersonId INT NOT NULL,
    uuid CHAR(36) NOT NULL UNIQUE,
    date DATE NOT NULL,
    taxRate DECIMAL(5,2) DEFAULT 0.00,
    notes TEXT,
    status ENUM('DRAFT', 'SENT', 'PAID', 'CANCELLED') DEFAULT 'DRAFT',
    FOREIGN KEY (customerId) REFERENCES Company(companyId) ON DELETE CASCADE,
    FOREIGN KEY (salespersonId) REFERENCES Person(personId),
    createdDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_invoice_date (date)
);

CREATE TABLE InvoiceItem (
    invoiceItemId INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    itemId INT NOT NULL,
    invoiceId INT NOT NULL,
    uuid CHAR(36) UNIQUE,
    itemType CHAR(1),
    quantity DOUBLE DEFAULT 1.00,
    unitPrice DOUBLE,
    startDate CHAR(10),
    endDate CHAR(10),    
    hoursRented DOUBLE,
    FOREIGN KEY (itemId) REFERENCES Item(itemId),
    FOREIGN KEY (invoiceId) REFERENCES Invoice(invoiceId) ON DELETE CASCADE
);

INSERT INTO Person (uuid, firstName, lastName, phone) VALUES 
('55ce455d-26a5-4629-ad54-a24e0b8ed665', 'Ayling', 'Cesaro', '387-714-8304'),
('a1b2c3d4-e5f6-7890-1234-567890abcdef', 'John', 'Doe', '555-123-4567'),
('b2c3d4e5-f6a7-8901-2345-67890abcdef1', 'Jane', 'Smith', '555-987-6543'),
('bbab487d-984d-9876-bbab-55a5defa9090', 'James', 'Lost', '555-570-2890');

INSERT INTO Email (personId, address, isPrimary) VALUES
(1, 'ayling.cesaro@example.com', TRUE),
(1, 'rickybobby@reeckybooby.com', FALSE),
(1, 'bigbuckarro57@reeckybooby.com', FALSE),
(2, 'john.doe@example.com', TRUE),
(3, 'jane.smith@example.com', TRUE),
(4, 'lost.james@example.com', TRUE);

INSERT INTO Company (uuid, name, contactId) VALUES
('c1d2e3f4-a5b6-7890-1234-567890abcdef', 'ABC Construction', 
 (SELECT personId FROM Person WHERE lastName = 'Cesaro')),
('d2e3f4a5-b6c7-8901-2345-67890abcdef1', 'XYZ Builders',
 (SELECT personId FROM Person WHERE lastName = 'Doe'));

INSERT INTO Address (street, city, state, zip, companyId) VALUES
('123 Main St', 'Lincoln', 'Nebraska','68588',
 (SELECT companyId FROM Company WHERE name = 'ABC Construction')),
('456 Oak Ave', 'Des Moines','Iowa', '50309',
 (SELECT companyId FROM Company WHERE name = 'XYZ Builders'));

INSERT INTO Item (uuid, name, type, price, modelNumber, unit, subcontractorId) VALUES
('e3f4a5b6-c7d8-9012-3456-7890abcdef12', '2x4 Lumber', 'M', 3.50, 'LUM-2X4-8', 'each', 
 (SELECT companyId FROM Company WHERE companyId = '1')),
('f4a5b6c7-d8e9-0123-4567-890abcdef123', 'Roofing Nails', 'M', 0.05, 'NAIL-RF-1', 'box',
 (SELECT companyId FROM Company WHERE companyId = '2'));

INSERT INTO Invoice (customerId, salespersonId, uuid, date, status) VALUES
((SELECT companyId FROM Company WHERE name = 'ABC Construction'),
 (SELECT personId FROM Person WHERE lastName = 'Smith'), 'a5a5bfc7-d8e9-0163-4f67-890abffef123', '2023-10-15', 'PAID'),
((SELECT companyId FROM Company WHERE name = 'XYZ Builders'),
 (SELECT personId FROM Person WHERE lastName = 'Smith'), 'bba8bfc7-dbe9-056c-4b67-89babfbef193', '2023-10-16', 'SENT');

INSERT INTO InvoiceItem (itemId, invoiceId, uuid, itemType, quantity, unitPrice, startDate, endDate, hoursRented) VALUES
((SELECT itemId FROM Item WHERE name = '2x4 Lumber'),
 (SELECT invoiceId FROM Invoice WHERE date = '2023-10-15'), 'e6a5bfce-68e9-e1b3-4f67-890eeefef123', null, 100, 3.50, null, null, null),
((SELECT itemId FROM Item WHERE name = 'Roofing Nails'),
 (SELECT invoiceId FROM Invoice WHERE date = '2023-10-16'), '55a5b5ce-65e9-e5b3-4567-850ee5fef153', null, 50, 0.05, null, null, null);

 
 