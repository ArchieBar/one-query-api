package one.query.api.tests.hikari;

import static one.query.api.jooq.generated.demo_schema.Tables.PRODUCTS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import one.query.api.AbstractIsolatedEnvironment;
import one.query.api.impl.OneQuery;
import one.query.api.jooq.generated.demo_schema.tables.records.ProductsRecord;
import one.query.api.model.Sort;
import org.jooq.SortOrder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class QuerySortTest extends AbstractIsolatedEnvironment {
  @Test
  @DisplayName("Sort test (Sort)")
  void test1_1() {
    var sort = new Sort(PRODUCTS.PRODUCTID, SortOrder.ASC);
    var res =
        OneQuery.query(ctx.selectFrom(PRODUCTS)).sort(sort).toList(ProductsRecord::getProductid);

    assertThat(res.getFirst()).isEqualTo(1);

    sort = new Sort(PRODUCTS.PRODUCTID, SortOrder.DESC);
    res = OneQuery.query(ctx.selectFrom(PRODUCTS)).sort(sort).toList(ProductsRecord::getProductid);

    assertThat(res.getLast()).isEqualTo(1);
  }

  @Test
  @DisplayName("Sort test (Sort...)")
  void test1_2() {
    var sort1 = new Sort(PRODUCTS.CATEGORYID, SortOrder.ASC);
    var sort2 = new Sort(PRODUCTS.PRODUCTID, SortOrder.ASC);
    var res =
        OneQuery.query(ctx.selectFrom(PRODUCTS)).sort(sort1, sort2).toList(ProductsRecord.class);

    assertThat(res.getFirst())
        .matches(pr -> pr.getProductid().equals(1))
        .matches(pr -> pr.getCategoryid().equals(1));

    sort1 = new Sort(PRODUCTS.CATEGORYID, SortOrder.DESC);
    sort2 = new Sort(PRODUCTS.PRODUCTID, SortOrder.DESC);
    res = OneQuery.query(ctx.selectFrom(PRODUCTS)).sort(sort1, sort2).toList(ProductsRecord.class);

    assertThat(res.getLast())
        .matches(pr -> pr.getProductid().equals(1))
        .matches(pr -> pr.getCategoryid().equals(1));
  }

  @Test
  @DisplayName("Sort test (Collection<Sort>)")
  void test1_3() {
    var sort1 = new Sort(PRODUCTS.CATEGORYID, SortOrder.ASC);
    var sort2 = new Sort(PRODUCTS.PRODUCTID, SortOrder.ASC);
    var res =
        OneQuery.query(ctx.selectFrom(PRODUCTS))
            .sort(List.of(sort1, sort2))
            .toList(ProductsRecord.class);

    assertThat(res.getFirst())
        .matches(pr -> pr.getProductid().equals(1))
        .matches(pr -> pr.getCategoryid().equals(1));

    sort1 = new Sort(PRODUCTS.CATEGORYID, SortOrder.DESC);
    sort2 = new Sort(PRODUCTS.PRODUCTID, SortOrder.DESC);
    res =
        OneQuery.query(ctx.selectFrom(PRODUCTS))
            .sort(List.of(sort1, sort2))
            .toList(ProductsRecord.class);

    assertThat(res.getLast())
        .matches(pr -> pr.getProductid().equals(1))
        .matches(pr -> pr.getCategoryid().equals(1));
  }

  @Test
  @DisplayName("Sort by test (OrderField<?>)")
  void test2_1() {
    var sort = PRODUCTS.PRODUCTID.asc();
    var res =
        OneQuery.query(ctx.selectFrom(PRODUCTS)).sortBy(sort).toList(ProductsRecord::getProductid);

    assertThat(res.getFirst()).isEqualTo(1);

    sort = PRODUCTS.PRODUCTID.desc();
    res =
        OneQuery.query(ctx.selectFrom(PRODUCTS)).sortBy(sort).toList(ProductsRecord::getProductid);

    assertThat(res.getLast()).isEqualTo(1);
  }

  @Test
  @DisplayName("Sort by test (OrderField<?>...)")
  void test2_2() {
    var sort1 = PRODUCTS.CATEGORYID.asc();
    var sort2 = PRODUCTS.PRODUCTID.asc();
    var res =
        OneQuery.query(ctx.selectFrom(PRODUCTS)).sortBy(sort1, sort2).toList(ProductsRecord.class);

    assertThat(res.getFirst())
        .matches(pr -> pr.getProductid().equals(1))
        .matches(pr -> pr.getCategoryid().equals(1));

    sort1 = PRODUCTS.CATEGORYID.desc();
    sort2 = PRODUCTS.PRODUCTID.desc();
    res =
        OneQuery.query(ctx.selectFrom(PRODUCTS)).sortBy(sort1, sort2).toList(ProductsRecord.class);

    assertThat(res.getLast())
        .matches(pr -> pr.getProductid().equals(1))
        .matches(pr -> pr.getCategoryid().equals(1));
  }

  @Test
  @DisplayName("Sort by test (Collection<OrderField<?>>)")
  void test2_3() {
    var sort1 = PRODUCTS.CATEGORYID.asc();
    var sort2 = PRODUCTS.PRODUCTID.asc();
    var res =
        OneQuery.query(ctx.selectFrom(PRODUCTS))
            .sortBy(List.of(sort1, sort2))
            .toList(ProductsRecord.class);

    assertThat(res.getFirst())
        .matches(pr -> pr.getProductid().equals(1))
        .matches(pr -> pr.getCategoryid().equals(1));

    sort1 = PRODUCTS.CATEGORYID.desc();
    sort2 = PRODUCTS.PRODUCTID.desc();
    res =
        OneQuery.query(ctx.selectFrom(PRODUCTS))
            .sortBy(List.of(sort1, sort2))
            .toList(ProductsRecord.class);

    assertThat(res.getLast())
        .matches(pr -> pr.getProductid().equals(1))
        .matches(pr -> pr.getCategoryid().equals(1));
  }

  @Test
  @DisplayName("Sort is null")
  void test3() {
    Sort sort = null;
    var query = OneQuery.query(ctx.selectFrom(PRODUCTS));
    assertThrows(IllegalArgumentException.class, () -> query.sort(sort));
  }
}
