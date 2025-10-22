package one.query.api.tests.hikari;

import static one.query.api.jooq.generated.demo_schema.Tables.PRODUCTS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.jooq.impl.DSL.*;

import java.math.BigDecimal;
import java.util.List;
import one.query.api.AbstractIsolatedEnvironment;
import one.query.api.impl.OneQuery;
import one.query.api.jooq.generated.demo_schema.tables.records.ProductsRecord;
import one.query.api.model.Filter;
import one.query.api.model.Prefix;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class QueryFilterTest extends AbstractIsolatedEnvironment {
  @Test
  @DisplayName("Condition filter test (Filter<T>)")
  void test1_1() {
    var filter = new Filter<>(PRODUCTS.PRODUCTNAME, Prefix.EQ, List.of("Chais"));
    var res =
        OneQuery.query(ctx.selectFrom(PRODUCTS))
            .filter(filter)
            .toList(ProductsRecord::getProductname);

    assertThat(res).isNotEmpty().allMatch(s -> s.equals("Chais"));
  }

  @Test
  @DisplayName("Condition filter test (Filter<T>...)")
  void test1_2() {
    var filter1 = new Filter<>(PRODUCTS.PRODUCTNAME, Prefix.EQ, List.of("Chais"));
    var filter2 = new Filter<>(PRODUCTS.PRODUCTID, Prefix.EQ, List.of(1));
    var res =
        OneQuery.query(ctx.selectFrom(PRODUCTS))
            .filter(filter1, filter2)
            .toList(ProductsRecord.class);

    assertThat(res)
        .isNotEmpty()
        .allMatch(r -> r.getProductname().equals("Chais"))
        .allMatch(r -> r.getProductid().equals(1));
  }

  @Test
  @DisplayName("Condition filter test (Collection<Filter<T>>)")
  void test1_3() {
    var filter1 = new Filter<>(PRODUCTS.PRODUCTNAME, Prefix.EQ, List.of("Chais"));
    var filter2 = new Filter<>(PRODUCTS.PRODUCTID, Prefix.EQ, List.of(1));
    var res =
        OneQuery.query(ctx.selectFrom(PRODUCTS))
            .filter(List.of(filter1, filter2))
            .toList(ProductsRecord.class);

    assertThat(res)
        .isNotEmpty()
        .allMatch(r -> r.getProductname().equals("Chais"))
        .allMatch(r -> r.getProductid().equals(1));
  }

  @Test
  @DisplayName("Having filter test (Filter<T>)")
  void test2_1() {
    var sumValue = BigDecimal.valueOf(3003);
    var agrField = sum(PRODUCTS.PRODUCTID);

    var filter = new Filter<>(agrField, Prefix.EQ, List.of(sumValue));
    var res =
        OneQuery.query(ctx.select(agrField).from(PRODUCTS)).filter(filter).toList(BigDecimal.class);

    assertThat(res).hasSize(1).allMatch(n -> n.equals(sumValue));

    filter = new Filter<>(agrField, Prefix.EQ, List.of(BigDecimal.ONE));
    res =
        OneQuery.query(ctx.select(agrField).from(PRODUCTS)).filter(filter).toList(BigDecimal.class);

    assertThat(res).isEmpty();
  }

  @Test
  @DisplayName("Having filter test (Filter<T>...)")
  void test2_2() {
    var sumValue = BigDecimal.valueOf(3003);
    var agrField = sum(PRODUCTS.PRODUCTID);

    var filter1 = new Filter<>(agrField, Prefix.EQ, List.of(sumValue));
    var filter2 = new Filter<>(agrField, Prefix.EQ, List.of(sumValue));
    var res =
        OneQuery.query(ctx.select(agrField).from(PRODUCTS))
            .filter(filter1, filter2)
            .toList(BigDecimal.class);

    assertThat(res).hasSize(1).allMatch(n -> n.equals(sumValue));

    var filter3 = new Filter<>(agrField, Prefix.EQ, List.of(BigDecimal.ONE));
    res =
        OneQuery.query(ctx.select(agrField).from(PRODUCTS))
            .filter(filter1, filter3)
            .toList(BigDecimal.class);

    assertThat(res).isEmpty();
  }

  @Test
  @DisplayName("Having filter test (Collection<Filter<T>>)")
  void test2_3() {
    var sumValue = BigDecimal.valueOf(3003);
    var agrField = sum(PRODUCTS.PRODUCTID);

    var filter1 = new Filter<>(agrField, Prefix.EQ, List.of(sumValue));
    var filter2 = new Filter<>(agrField, Prefix.EQ, List.of(sumValue));
    var res =
        OneQuery.query(ctx.select(agrField).from(PRODUCTS))
            .filter(List.of(filter1, filter2))
            .toList(BigDecimal.class);

    assertThat(res).hasSize(1).allMatch(n -> n.equals(sumValue));

    var filter3 = new Filter<>(agrField, Prefix.EQ, List.of(BigDecimal.ONE));
    res =
        OneQuery.query(ctx.select(agrField).from(PRODUCTS))
            .filter(List.of(filter1, filter3))
            .toList(BigDecimal.class);

    assertThat(res).isEmpty();
  }

  @Test
  @DisplayName("Condition filter by test (Condition)")
  void test3_1() {
    var res =
        OneQuery.query(ctx.selectFrom(PRODUCTS))
            .filterBy(PRODUCTS.PRODUCTNAME.eq("Chais"))
            .toList(ProductsRecord::getProductname);

    assertThat(res).isNotEmpty().allMatch(s -> s.equals("Chais"));
  }

  @Test
  @DisplayName("Condition filter by test (Condition...)")
  void test3_2() {
    var condition1 = PRODUCTS.PRODUCTNAME.eq("Chais");
    var condition2 = PRODUCTS.PRODUCTID.eq(1);
    var res =
        OneQuery.query(ctx.selectFrom(PRODUCTS))
            .filterBy(condition1, condition2)
            .toList(ProductsRecord.class);

    assertThat(res)
        .isNotEmpty()
        .allMatch(r -> r.getProductname().equals("Chais"))
        .allMatch(r -> r.getProductid().equals(1));
  }

  @Test
  @DisplayName("Condition filter by test (Collection<Condition>)")
  void test3_3() {
    var condition1 = PRODUCTS.PRODUCTNAME.eq("Chais");
    var condition2 = PRODUCTS.PRODUCTID.eq(1);
    var res =
        OneQuery.query(ctx.selectFrom(PRODUCTS))
            .filterBy(List.of(condition1, condition2))
            .toList(ProductsRecord.class);

    assertThat(res)
        .isNotEmpty()
        .allMatch(r -> r.getProductname().equals("Chais"))
        .allMatch(r -> r.getProductid().equals(1));
  }
}
