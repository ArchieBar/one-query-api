package one.query.api.tests.hikari;

import static one.query.api.jooq.generated.demo_schema.Tables.CATEGORIES;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import one.query.api.AbstractIsolatedEnvironment;
import one.query.api.impl.OneQuery;
import one.query.api.jooq.generated.demo_schema.tables.records.CategoriesRecord;
import one.query.api.model.Page;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class QueryPaginateTest extends AbstractIsolatedEnvironment {
  @Test
  @DisplayName("Paginate test (Page)")
  void test1_1() {
    var page = new Page(0, 5);
    var res =
        OneQuery.query(ctx.selectFrom(CATEGORIES))
            .paginate(page)
            .toPaginationResult(CategoriesRecord.class);

    assertThat(res)
        .matches(pr -> pr.total() == 8)
        .matches(pr -> pr.content().size() == 5)
        .matches(pr -> pr.number() == 0);
  }

  @Test
  @DisplayName("Paginate test (int, int)")
  void test1_2() {
    var res =
        OneQuery.query(ctx.selectFrom(CATEGORIES))
            .paginate(0, 5)
            .toPaginationResult(CategoriesRecord.class);

    assertThat(res)
        .matches(pr -> pr.total() == 8)
        .matches(pr -> pr.content().size() == 5)
        .matches(pr -> pr.number() == 0);
  }

  @Test
  @DisplayName("Paginate test .getTotal()")
  void test2() {
    var res = OneQuery.query(ctx.selectFrom(CATEGORIES)).paginate(0, 5).getTotal();

    assertThat(res).isEqualTo(8);
  }

  @Test
  @DisplayName("Try create invalid page, number is less 0")
  void test3_1() {
    assertThrows(IllegalArgumentException.class, () -> new Page(-1, 5));
  }

  @Test
  @DisplayName("Try create invalid page, size is less 1")
  void test3_2() {
    assertThrows(IllegalArgumentException.class, () -> new Page(0, 0));
  }
}
