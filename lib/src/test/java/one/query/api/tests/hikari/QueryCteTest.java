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
package one.query.api.tests.hikari;

import static one.query.api.jooq.generated.demo_schema.Tables.PRODUCTS;
import static org.assertj.core.api.Assertions.assertThat;

import one.query.api.AbstractIsolatedEnvironment;
import one.query.api.impl.OneQuery;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class QueryCteTest extends AbstractIsolatedEnvironment {
  @Test
  @DisplayName("Cte fields test")
  void test1() {
    var fieldName = "custom_name";
    var res =
        OneQuery.query(ctx.select(PRODUCTS.PRICE).from(PRODUCTS))
            .fields(fieldName)
            .toCommonTable("cte");

    var field = res.field(fieldName);
    assertThat(field)
        .isNotNull()
        .matches(f -> f.getDataType().equals(PRODUCTS.PRICE.getDataType()));
  }
}
