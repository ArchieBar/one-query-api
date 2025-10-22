package one.query.api.tests.hikari;

import static one.query.api.configuration.FieldConfig.C_CNAME;
import static one.query.api.configuration.FieldConfig.C_CONTACT_NAME;
import static one.query.api.configuration.FieldConfig.C_ID;
import static one.query.api.jooq.generated.demo_schema.Tables.*;
import static one.query.api.model.Prefix.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.jooq.SortOrder.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.UUID;
import one.query.api.AbstractIsolatedEnvironment;
import one.query.api.configuration.FieldConfig;
import one.query.api.impl.OneQuery;
import one.query.api.jooq.generated.demo_schema.tables.records.CustomersRecord;
import one.query.api.model.Filter;
import one.query.api.model.Page;
import one.query.api.model.PaginationResult;
import one.query.api.model.Sort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PostgresTest extends AbstractIsolatedEnvironment {
  @Test
  @DisplayName("Base test")
  void test1() {
    Filter<String> filter =
        mapper.createFilter(
            C_CNAME.getKey(),
            EQ,
            List.of(
                "Alfreds Futterkiste",
                "Ana Trujillo Emparedados y helados",
                "Antonio Moreno Taquería"));
    Sort sort = mapper.createSort(C_ID.getKey(), DESC);
    Page page = new Page(0, 2);
    PaginationResult<CustomersRecord> result =
        OneQuery.query(ctx.selectFrom(CUSTOMERS))
            .filter(filter)
            .sort(sort)
            .paginate(page)
            .toPaginationResult(CustomersRecord.class);

    assertThat(result.total()).isEqualTo(3);
    assertThat(result.content())
        .hasSize(2)
        .anyMatch(customer -> customer.getCustomerid().equals(3))
        .anyMatch(customer -> customer.getCustomerid().equals(2))
        .first()
        .matches(customer -> customer.getCustomerid().equals(3));
  }

  @Test
  @DisplayName("Cast error exception")
  void test2() {
    FieldConfig field = C_ID;
    String key = field.getKey();
    UUID invalidIdType = UUID.randomUUID();

    IllegalArgumentException ex =
        assertThrows(
            IllegalArgumentException.class, () -> mapper.createFilter(key, EQ, invalidIdType));

    assertThat(ex.getMessage())
        .isEqualTo(
            "Failed to cast %s to type %s"
                .formatted(invalidIdType, field.getField().getType().getSimpleName()));
  }

  @Test
  @DisplayName("Test CTE")
  void test4() {
    var tmp =
        OneQuery.query(ctx.select(SHIPPERS.SHIPPERID, SHIPPERS.SHIPPERNAME).from(SHIPPERS))
            .paginate(0, 2)
            .fields("id", "name");
    var total = tmp.getTotal();
    var cte = tmp.toCommonTable("test");

    var res = ctx.with(cte).select(cte.field("id")).from(cte).fetch();

    assertThat(total).isNotNull().isEqualTo(3);
    assertThat(res).isNotEmpty().hasSize(2);
  }

  /* --------------------------------------------------- PREFIX TESTS ----------------------------------------------- */
  @Test
  @DisplayName("Like prefix test")
  void test5() {
    var filter = mapper.createFilter(C_CONTACT_NAME.getKey(), LIKE, List.of("Maria Anders"));
    var result =
        OneQuery.query(ctx.select(CUSTOMERS.CONTACTNAME).from(CUSTOMERS)).filter(filter).fetch();

    assertThat(result).hasSize(1).first().matches(row -> row.component1().equals("Maria Anders"));

    filter = mapper.createFilter(C_CONTACT_NAME.getKey(), LIKE, List.of("Maria"));
    result =
        OneQuery.query(ctx.select(CUSTOMERS.CONTACTNAME).from(CUSTOMERS)).filter(filter).fetch();

    assertThat(result)
        .hasSize(2)
        .anyMatch(row -> row.component1().equals("Maria Anders"))
        .anyMatch(row -> row.component1().equals("Maria Larsson"));

    filter = mapper.createFilter(C_CONTACT_NAME.getKey(), LIKE, List.of("Anders"));
    result =
        OneQuery.query(ctx.select(CUSTOMERS.CONTACTNAME).from(CUSTOMERS)).filter(filter).fetch();

    assertThat(result).hasSize(1).first().matches(row -> row.component1().equals("Maria Anders"));

    filter = mapper.createFilter(C_CONTACT_NAME.getKey(), LIKE, List.of("ia An"));
    result =
        OneQuery.query(ctx.select(CUSTOMERS.CONTACTNAME).from(CUSTOMERS)).filter(filter).fetch();

    assertThat(result).hasSize(1).first().matches(row -> row.component1().equals("Maria Anders"));

    filter = mapper.createFilter(C_CONTACT_NAME.getKey(), LIKE, List.of("anders"));
    result =
        OneQuery.query(ctx.select(CUSTOMERS.CONTACTNAME).from(CUSTOMERS)).filter(filter).fetch();

    assertThat(result).hasSize(1).first().matches(row -> row.component1().equals("Maria Anders"));

    filter = mapper.createFilter(C_CONTACT_NAME.getKey(), LIKE, List.of("maria anders"));
    result =
        OneQuery.query(ctx.select(CUSTOMERS.CONTACTNAME).from(CUSTOMERS)).filter(filter).fetch();

    assertThat(result).hasSize(1).first().matches(row -> row.component1().equals("Maria Anders"));
  }
}
