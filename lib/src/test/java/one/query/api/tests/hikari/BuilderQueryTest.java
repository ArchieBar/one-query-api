package one.query.api.tests.hikari;

import static one.query.api.jooq.generated.demo_schema.Tables.*;
import static one.query.api.jooq.generated.demo_schema.Tables.CATEGORIES;
import static one.query.api.jooq.generated.demo_schema.Tables.PRODUCTS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigInteger;
import java.util.List;
import one.query.api.AbstractIsolatedEnvironment;
import one.query.api.impl.OneQuery;
import org.apache.commons.lang3.NotImplementedException;
import org.jooq.JoinType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BuilderQueryTest extends AbstractIsolatedEnvironment {
  @Test
  @DisplayName("Final step builder test")
  void test1() {
    var res =
        OneQuery.query(
                ctx.selectFrom(CUSTOMERS)
                    .where(CUSTOMERS.COUNTRY.eq("Berlin"))
                    .orderBy(CUSTOMERS.CONTACTNAME))
            .fetch();
    assertThat(res).isNotNull();
  }

  @Test
  @DisplayName("Connect step builder test")
  void test2() {
    var res =
        OneQuery.query(ctx.selectFrom(CUSTOMERS).where(CUSTOMERS.COUNTRY.eq("Berlin"))).fetch();
    assertThat(res).isNotNull();
  }

  @Test
  @DisplayName("Optional on step builder test")
  @SuppressWarnings("java:S1874")
  void test3() {
    var optionalOnStep =
        ctx.select(PRODUCTS.PRICE).from(PRODUCTS).join(CATEGORIES, JoinType.LEFT_OUTER_JOIN);
    var ex = assertThrows(NotImplementedException.class, () -> OneQuery.query(optionalOnStep));

    assertThat(ex).isNotNull();
  }

  @Test
  @DisplayName("On step builder test (Condition)")
  void test4_1() {
    var res =
        OneQuery.query(ctx.select(PRODUCTS.PRICE).from(PRODUCTS).join(CATEGORIES))
            .on(PRODUCTS.CATEGORYID.eq(CATEGORIES.CATEGORYID))
            .fetch();

    assertThat(res).isNotNull();
  }

  @Test
  @DisplayName("On step builder test (Condition...)")
  void test4_2() {
    var res =
        OneQuery.query(ctx.select(PRODUCTS.PRICE).from(PRODUCTS).join(CATEGORIES))
            .on(
                PRODUCTS.CATEGORYID.eq(CATEGORIES.CATEGORYID),
                PRODUCTS.PRICE.between(BigInteger.valueOf(0), BigInteger.valueOf(10)))
            .fetch();

    assertThat(res).isNotNull();
  }

  @Test
  @DisplayName("From step builder test")
  @SuppressWarnings("java:S1874")
  void test5() {
    var fromStep = ctx.select(PRODUCTS.PRICE).into(PRODUCTS);
    var ex = assertThrows(NotImplementedException.class, () -> OneQuery.query(fromStep));

    assertThat(ex).isNotNull();
  }

  @Test
  @DisplayName("Into step builder test")
  void test6_1() {
    var res = OneQuery.query(ctx.select(PRODUCTS.PRICE)).from(PRODUCTS).fetch();

    assertThat(res).isNotNull();
  }

  @Test
  @DisplayName("Into step builder test")
  void test6_2() {
    var res =
        OneQuery.query(ctx.select(PRODUCTS.PRICE, CATEGORIES.CATEGORYNAME))
            .from(PRODUCTS, CATEGORIES)
            .fetch();

    assertThat(res).isNotNull();
  }

  @Test
  @DisplayName("Into step builder test")
  void test6_3() {
    var res =
        OneQuery.query(ctx.select(PRODUCTS.PRICE, CATEGORIES.CATEGORYNAME))
            .from(List.of(PRODUCTS, CATEGORIES))
            .fetch();

    assertThat(res).isNotNull();
  }
}
