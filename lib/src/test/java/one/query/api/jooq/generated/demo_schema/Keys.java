/* SPDX-License-Identifier: Apache-2.0 */
// Copyright 2024-2025 One Query API contributors
package one.query.api.jooq.generated.demo_schema;

import one.query.api.jooq.generated.demo_schema.tables.Categories;
import one.query.api.jooq.generated.demo_schema.tables.Customers;
import one.query.api.jooq.generated.demo_schema.tables.Employees;
import one.query.api.jooq.generated.demo_schema.tables.Orderdetails;
import one.query.api.jooq.generated.demo_schema.tables.Orders;
import one.query.api.jooq.generated.demo_schema.tables.Products;
import one.query.api.jooq.generated.demo_schema.tables.Shippers;
import one.query.api.jooq.generated.demo_schema.tables.Suppliers;
import one.query.api.jooq.generated.demo_schema.tables.records.CategoriesRecord;
import one.query.api.jooq.generated.demo_schema.tables.records.CustomersRecord;
import one.query.api.jooq.generated.demo_schema.tables.records.EmployeesRecord;
import one.query.api.jooq.generated.demo_schema.tables.records.OrderdetailsRecord;
import one.query.api.jooq.generated.demo_schema.tables.records.OrdersRecord;
import one.query.api.jooq.generated.demo_schema.tables.records.ProductsRecord;
import one.query.api.jooq.generated.demo_schema.tables.records.ShippersRecord;
import one.query.api.jooq.generated.demo_schema.tables.records.SuppliersRecord;
import org.jooq.ForeignKey;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;

/** A class modelling foreign key relationships and constraints of tables in DEMO_SCHEMA. */
@SuppressWarnings({"all", "unchecked", "rawtypes"})
public class Keys {

  // -------------------------------------------------------------------------
  // UNIQUE and PRIMARY KEY definitions
  // -------------------------------------------------------------------------

  public static final UniqueKey<CategoriesRecord> CONSTRAINT_6 =
      Internal.createUniqueKey(
          Categories.CATEGORIES,
          DSL.name("CONSTRAINT_6"),
          new TableField[] {Categories.CATEGORIES.CATEGORYID},
          true);
  public static final UniqueKey<CustomersRecord> CONSTRAINT_62 =
      Internal.createUniqueKey(
          Customers.CUSTOMERS,
          DSL.name("CONSTRAINT_62"),
          new TableField[] {Customers.CUSTOMERS.CUSTOMERID},
          true);
  public static final UniqueKey<EmployeesRecord> CONSTRAINT_4 =
      Internal.createUniqueKey(
          Employees.EMPLOYEES,
          DSL.name("CONSTRAINT_4"),
          new TableField[] {Employees.EMPLOYEES.EMPLOYEEID},
          true);
  public static final UniqueKey<OrderdetailsRecord> CONSTRAINT_B =
      Internal.createUniqueKey(
          Orderdetails.ORDERDETAILS,
          DSL.name("CONSTRAINT_B"),
          new TableField[] {Orderdetails.ORDERDETAILS.ORDERDETAILID},
          true);
  public static final UniqueKey<OrdersRecord> CONSTRAINT_8 =
      Internal.createUniqueKey(
          Orders.ORDERS, DSL.name("CONSTRAINT_8"), new TableField[] {Orders.ORDERS.ORDERID}, true);
  public static final UniqueKey<ProductsRecord> CONSTRAINT_F2 =
      Internal.createUniqueKey(
          Products.PRODUCTS,
          DSL.name("CONSTRAINT_F2"),
          new TableField[] {Products.PRODUCTS.PRODUCTID},
          true);
  public static final UniqueKey<ShippersRecord> CONSTRAINT_F =
      Internal.createUniqueKey(
          Shippers.SHIPPERS,
          DSL.name("CONSTRAINT_F"),
          new TableField[] {Shippers.SHIPPERS.SHIPPERID},
          true);
  public static final UniqueKey<SuppliersRecord> CONSTRAINT_A =
      Internal.createUniqueKey(
          Suppliers.SUPPLIERS,
          DSL.name("CONSTRAINT_A"),
          new TableField[] {Suppliers.SUPPLIERS.SUPPLIERID},
          true);

  // -------------------------------------------------------------------------
  // FOREIGN KEY definitions
  // -------------------------------------------------------------------------

  public static final ForeignKey<OrderdetailsRecord, OrdersRecord> CONSTRAINT_B8 =
      Internal.createForeignKey(
          Orderdetails.ORDERDETAILS,
          DSL.name("CONSTRAINT_B8"),
          new TableField[] {Orderdetails.ORDERDETAILS.ORDERID},
          Keys.CONSTRAINT_8,
          new TableField[] {Orders.ORDERS.ORDERID},
          true);
  public static final ForeignKey<OrderdetailsRecord, ProductsRecord> CONSTRAINT_B8C =
      Internal.createForeignKey(
          Orderdetails.ORDERDETAILS,
          DSL.name("CONSTRAINT_B8C"),
          new TableField[] {Orderdetails.ORDERDETAILS.PRODUCTID},
          Keys.CONSTRAINT_F2,
          new TableField[] {Products.PRODUCTS.PRODUCTID},
          true);
  public static final ForeignKey<OrdersRecord, EmployeesRecord> CONSTRAINT_8B =
      Internal.createForeignKey(
          Orders.ORDERS,
          DSL.name("CONSTRAINT_8B"),
          new TableField[] {Orders.ORDERS.EMPLOYEEID},
          Keys.CONSTRAINT_4,
          new TableField[] {Employees.EMPLOYEES.EMPLOYEEID},
          true);
  public static final ForeignKey<OrdersRecord, CustomersRecord> CONSTRAINT_8B7 =
      Internal.createForeignKey(
          Orders.ORDERS,
          DSL.name("CONSTRAINT_8B7"),
          new TableField[] {Orders.ORDERS.CUSTOMERID},
          Keys.CONSTRAINT_62,
          new TableField[] {Customers.CUSTOMERS.CUSTOMERID},
          true);
  public static final ForeignKey<OrdersRecord, ShippersRecord> CONSTRAINT_8B72 =
      Internal.createForeignKey(
          Orders.ORDERS,
          DSL.name("CONSTRAINT_8B72"),
          new TableField[] {Orders.ORDERS.SHIPPERID},
          Keys.CONSTRAINT_F,
          new TableField[] {Shippers.SHIPPERS.SHIPPERID},
          true);
  public static final ForeignKey<ProductsRecord, CategoriesRecord> CONSTRAINT_F2D =
      Internal.createForeignKey(
          Products.PRODUCTS,
          DSL.name("CONSTRAINT_F2D"),
          new TableField[] {Products.PRODUCTS.CATEGORYID},
          Keys.CONSTRAINT_6,
          new TableField[] {Categories.CATEGORIES.CATEGORYID},
          true);
  public static final ForeignKey<ProductsRecord, SuppliersRecord> CONSTRAINT_F2D1 =
      Internal.createForeignKey(
          Products.PRODUCTS,
          DSL.name("CONSTRAINT_F2D1"),
          new TableField[] {Products.PRODUCTS.SUPPLIERID},
          Keys.CONSTRAINT_A,
          new TableField[] {Suppliers.SUPPLIERS.SUPPLIERID},
          true);
}
