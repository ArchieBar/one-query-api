package io.codefine.onequery.tests.hikari;

import static io.codefine.onequery.jooq.generated.DefaultCatalog.DEFAULT_CATALOG;
import static io.codefine.onequery.jooq.generated.demo_schema.Tables.PRODUCTS;
import static io.codefine.onequery.jooq.generated.demo_schema.Tables.SHIPPERS;
import static org.assertj.core.api.Assertions.assertThat;

import io.codefine.onequery.AbstractIsolatedEnvironment;
import io.codefine.onequery.configuration.JooqConfiguration;
import io.codefine.onequery.impl.OneQuery;
import io.codefine.onequery.jooq.generated.demo_schema.tables.daos.OrdersDao;
import io.codefine.onequery.jooq.generated.demo_schema.tables.daos.ShippersDao;
import io.codefine.onequery.mapper.OneQueryMapper;
import io.codefine.onequery.mapper.impl.PostgresMapper;
import io.codefine.onequery.model.Filter;
import io.codefine.onequery.model.Page;
import io.codefine.onequery.model.Sort;
import java.math.BigInteger;
import java.nio.file.Path;
import java.util.List;
import org.jooq.Record3;
import org.jooq.RecordMapper;
import org.jooq.impl.DefaultConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PostgresTest extends AbstractIsolatedEnvironment {
  private static final Path RESOURCES_PATH =
      Path.of("build/resources/test/mapper/mapper.properties");

  private final DefaultConfiguration postgresConfiguration =
      JooqConfiguration.getHikariConfig(POSTGRES_CONTAINER.getJdbcUrl());

  private final OrdersDao ordersDao = new OrdersDao(postgresConfiguration);
  private final ShippersDao shippersDao = new ShippersDao(postgresConfiguration);

  private final OneQueryMapper mapper =
      new PostgresMapper(RESOURCES_PATH, DEFAULT_CATALOG.getSchemas()).register();

  @Test
  @DisplayName("Response should be total 3 elements")
  void test1() {
    final RecordMapper<Record3<String, BigInteger, Integer>, String[]> recordMapper =
        r -> new String[] {r.value1(), r.value2().toString(), r.value3().toString()};

    Filter filterPriceTen = new Filter("Products.Price", Filter.Prefix.EQ, "10");
    Sort sortPrice = new Sort("Products.Price", Sort.Direction.ASC);
    Page page = new Page(0, 10);

    var res =
        OneQuery.query(
                ordersDao
                    .ctx()
                    .select(PRODUCTS.PRODUCTNAME, PRODUCTS.PRICE, PRODUCTS.CATEGORYID)
                    .from(PRODUCTS))
            .filter(filterPriceTen)
            .sort(sortPrice)
            .paginate(page)
            .toPaginationResult(recordMapper);

    assertThat(res.total()).isEqualTo(3);
  }

  @Test
  @DisplayName("Ne filter contains several values, correct create Condition with and operator")
  void test2() {
    final RecordMapper<Record3<Integer, String, String>, String[]> recordMapper =
        r -> new String[] {r.value1().toString(), r.value2(), r.value3()};

    Filter filter =
        new Filter(
            "Shippers.ShipperName", Filter.Prefix.NE, List.of("Speedy Express", "United Package"));

    var withFilter =
        OneQuery.query(
                shippersDao
                    .ctx()
                    .select(SHIPPERS.SHIPPERID, SHIPPERS.SHIPPERNAME, SHIPPERS.PHONE)
                    .from(SHIPPERS))
            .filter(filter)
            .toList(recordMapper);

    var withoutFilter =
        OneQuery.query(
                shippersDao
                    .ctx()
                    .select(SHIPPERS.SHIPPERID, SHIPPERS.SHIPPERNAME, SHIPPERS.PHONE)
                    .from(SHIPPERS))
            .toList(recordMapper);

    assertThat(withoutFilter).hasSize(3);
    assertThat(withoutFilter.stream().map(it -> it[1]))
        .containsAll(List.of("Speedy Express", "United Package", "Federal Shipping"));
    assertThat(withFilter).hasSize(1);
    assertThat(withFilter.get(0)[1]).isEqualTo("Federal Shipping");
  }

  @Test
  @DisplayName("Test fetch method")
  void test3() {
    var result = OneQuery.query(shippersDao.ctx().selectFrom(SHIPPERS)).paginate(0, 1).fetch();

    assertThat(result).hasSize(1);
  }
}
