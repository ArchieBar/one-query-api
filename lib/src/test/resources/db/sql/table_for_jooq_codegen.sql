-- File for jooq codegen

SET search_path TO demo_schema;

CREATE TABLE Categories
(
    CategoryID SERIAL PRIMARY KEY,
    CategoryName VARCHAR(25),
    Description VARCHAR(255)
);

CREATE TABLE Customers
(
    CustomerID SERIAL PRIMARY KEY,
    CustomerName VARCHAR(50),
    ContactName VARCHAR(50),
    Address VARCHAR(50),
    City VARCHAR(20),
    PostalCode VARCHAR(10),
    Country VARCHAR(15)
);

CREATE TABLE Employees
(
    EmployeeID SERIAL PRIMARY KEY,
    LastName VARCHAR(15),
    FirstName VARCHAR(15),
    BirthDate TIMESTAMP,
    Photo VARCHAR(25),
    Notes VARCHAR(1024)
);

CREATE TABLE Shippers(
    ShipperID SERIAL PRIMARY KEY,
    ShipperName VARCHAR(25),
    Phone VARCHAR(15)
);

CREATE TABLE Suppliers(
    SupplierID SERIAL PRIMARY KEY,
    SupplierName VARCHAR(50),
    ContactName VARCHAR(50),
    Address VARCHAR(50),
    City VARCHAR(20),
    PostalCode VARCHAR(10),
    Country VARCHAR(15),
    Phone VARCHAR(15)
);

CREATE TABLE Products(
    ProductID SERIAL PRIMARY KEY,
    ProductName VARCHAR(50),
    SupplierID INTEGER,
    CategoryID INTEGER,
    Unit VARCHAR(25),
    Price NUMERIC,
	FOREIGN KEY (CategoryID) REFERENCES Categories (CategoryID),
	FOREIGN KEY (SupplierID) REFERENCES Suppliers (SupplierID)
);

CREATE TABLE Orders(
    OrderID SERIAL PRIMARY KEY,
    CustomerID INTEGER,
    EmployeeID INTEGER,
    OrderDate TIMESTAMP,
    ShipperID INTEGER,
    FOREIGN KEY (EmployeeID) REFERENCES Employees (EmployeeID),
    FOREIGN KEY (CustomerID) REFERENCES Customers (CustomerID),
    FOREIGN KEY (ShipperID) REFERENCES Shippers (ShipperID)
);

CREATE TABLE OrderDetails(
    OrderDetailID SERIAL PRIMARY KEY,
    OrderID INTEGER,
    ProductID INTEGER,
    Quantity INTEGER,
	FOREIGN KEY (OrderID) REFERENCES Orders (OrderID),
	FOREIGN KEY (ProductID) REFERENCES Products (ProductID)
);