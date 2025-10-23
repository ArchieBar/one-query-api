package one.query.api.tests.hikari;

import static one.query.api.jooq.generated.demo_schema.Tables.PRODUCTS;
import static org.assertj.core.api.Assertions.assertThat;

import one.query.api.AbstractIsolatedEnvironment;
import one.query.api.impl.OneQuery;
import org.junit.jupiter.api.Test;

class QueryCteTest extends AbstractIsolatedEnvironment {
  @Test
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
