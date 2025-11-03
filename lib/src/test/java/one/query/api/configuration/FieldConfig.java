/*
 * Copyright 2025 One Query API contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
  P_ID("PId", PRODUCTS.PRODUCTID),
  P_CATEGORY_ID("PCId", PRODUCTS.CATEGORYID),
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
