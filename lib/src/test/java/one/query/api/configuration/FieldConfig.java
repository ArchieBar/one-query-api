package one.query.api.configuration;

import static one.query.api.jooq.generated.demo_schema.Tables.PRODUCTS;
import static one.query.api.jooq.generated.demo_schema.Tables.SHIPPERS;
import static one.query.api.jooq.generated.demo_schema.tables.Customers.CUSTOMERS;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import org.jooq.Field;

public enum FieldConfig {
  C_ID("CId", CUSTOMERS.CUSTOMERID),
  C_CNAME("CCname", CUSTOMERS.CUSTOMERNAME),
  C_CONTACT_NAME("CContactName", CUSTOMERS.CONTACTNAME),
  P_PRICE("PPrice", PRODUCTS.PRICE),
  S_SNAME("SSName", SHIPPERS.SHIPPERNAME);

  FieldConfig(final String key, final Field<?> field) {
    this.key = key;
    this.field = field;
  }

  private final String key;
  private final Field<?> field;

  public String getKey() {
    return key;
  }

  public Field<?> getField() {
    return field;
  }

  public static Map<String, Field<?>> collectMap() {
    return Arrays.stream(FieldConfig.values())
        .collect(Collectors.toMap(FieldConfig::getKey, FieldConfig::getField));
  }
}
