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

import static one.query.api.jooq.generated.demo_schema.Tables.CATEGORIES;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import one.query.api.AbstractIsolatedEnvironment;
import one.query.api.impl.OneQuery;
import one.query.api.jooq.generated.demo_schema.tables.records.CategoriesRecord;
import one.query.api.model.PaginationResult;
import org.jooq.CommonTableExpression;
import org.jooq.Result;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CollectResultTest extends AbstractIsolatedEnvironment {
  @Test
  @DisplayName("Collect to paginate result test (Class<T>)")
  void test1_1() {
    var res =
        OneQuery.query(ctx.selectFrom(CATEGORIES))
            .paginate(0, 5)
            .toPaginationResult(CategoriesRecord.class);

    assertThat(res).isInstanceOf(PaginationResult.class);
    assertThat(res.content()).isNotEmpty().allMatch(CategoriesRecord.class::isInstance);
  }

  @Test
  @DisplayName("Collect to paginate result test (RecordMapper<R, C>)")
  void test1_2() {
    var res =
        OneQuery.query(ctx.selectFrom(CATEGORIES))
            .paginate(0, 5)
            .toPaginationResult(CategoriesRecord::getCategoryid);

    assertThat(res).isInstanceOf(PaginationResult.class);
    assertThat(res.content()).isNotEmpty().allMatch(Integer.class::isInstance);
  }

  @Test
  @DisplayName("Collect to list result test (Class<T>)")
  void test2_1() {
    var res = OneQuery.query(ctx.selectFrom(CATEGORIES)).toList(CategoriesRecord.class);

    assertThat(res)
        .isInstanceOf(List.class)
        .isNotEmpty()
        .allMatch(CategoriesRecord.class::isInstance);
  }

  @Test
  @DisplayName("Collect to list result test (RecordMapper<R, C>)")
  void test2_2() {
    var res = OneQuery.query(ctx.selectFrom(CATEGORIES)).toList(CategoriesRecord::getCategoryid);

    assertThat(res).isInstanceOf(List.class).isNotEmpty().allMatch(Integer.class::isInstance);
  }

  @Test
  @DisplayName("Fetch collect result test")
  void test3() {
    var res = OneQuery.query(ctx.selectFrom(CATEGORIES)).fetch();

    assertThat(res)
        .isInstanceOf(Result.class)
        .isNotEmpty()
        .allMatch(CategoriesRecord.class::isInstance);
  }

  @Test
  @DisplayName("To cte result test")
  void test4() {
    var res =
        OneQuery.query(ctx.select(CATEGORIES.CATEGORYNAME).from(CATEGORIES))
            .fields("field_name")
            .toCommonTable("name");

    assertThat(res).isInstanceOf(CommonTableExpression.class);
  }
}
