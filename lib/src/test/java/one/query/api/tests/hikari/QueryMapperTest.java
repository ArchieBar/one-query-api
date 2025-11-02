package one.query.api.tests.hikari;

import static one.query.api.jooq.generated.demo_schema.Tables.PRODUCTS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigInteger;
import java.util.List;
import java.util.NoSuchElementException;
import one.query.api.AbstractIsolatedEnvironment;
import one.query.api.configuration.FieldConfig;
import one.query.api.impl.OneQuery;
import one.query.api.jooq.generated.demo_schema.tables.records.ProductsRecord;
import one.query.api.model.Prefix;
import org.jooq.SortOrder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class QueryMapperTest extends AbstractIsolatedEnvironment {
  @Test
  @DisplayName("Get sql dialect from mapper implementation")
  void test1() {
    var sqlDialect = mapper.getDialect();
    assertThat(sqlDialect).isNotNull();
  }

  @Test
  @DisplayName("Check exists key in configuration")
  void test2() {
    var existKey = mapper.existsKey(FieldConfig.P_PRICE.getKey());
    assertThat(existKey).isTrue();
  }

  @Test
  @DisplayName("Create Filter by singleton value")
  void test3_1() {
    var value = BigInteger.valueOf(10);
    var filter = mapper.createFilter(FieldConfig.P_PRICE.getKey(), Prefix.EQ, value);
    var res =
        OneQuery.query(ctx.selectFrom(PRODUCTS)).filter(filter).toList(ProductsRecord::getPrice);

    assertThat(res).isNotEmpty().allMatch(i -> i.equals(value));
  }

  @Test
  @DisplayName("Create Filter by array values")
  void test3_2() {
    var value1 = BigInteger.valueOf(10);
    var value2 = BigInteger.valueOf(18);
    var filter = mapper.createFilter(FieldConfig.P_PRICE.getKey(), Prefix.EQ, value1, value2);
    var res =
        OneQuery.query(ctx.selectFrom(PRODUCTS)).filter(filter).toList(ProductsRecord::getPrice);

    assertThat(res).isNotEmpty().allMatch(i -> i.equals(value1) || i.equals(value2));
  }

  @Test
  @DisplayName("Create Filter by list values")
  void test3_3() {
    var value1 = BigInteger.valueOf(10);
    var value2 = BigInteger.valueOf(18);
    var filter =
        mapper.createFilter(FieldConfig.P_PRICE.getKey(), Prefix.EQ, List.of(value1, value2));
    var res =
        OneQuery.query(ctx.selectFrom(PRODUCTS)).filter(filter).toList(ProductsRecord::getPrice);

    assertThat(res).isNotEmpty().allMatch(i -> i.equals(value1) || i.equals(value2));
  }

  @Test
  @DisplayName("Create Filter by field")
  void test3_4() {
    var value = BigInteger.valueOf(10);
    var filter = mapper.createFilter(PRODUCTS.PRICE, Prefix.EQ, value);
    var res =
        OneQuery.query(ctx.selectFrom(PRODUCTS)).filter(filter).toList(ProductsRecord::getPrice);

    assertThat(res).isNotEmpty().allMatch(i -> i.equals(value));
  }

  @Test
  @DisplayName("Create Filter by array values")
  void test3_5() {
    var value1 = BigInteger.valueOf(10);
    var value2 = BigInteger.valueOf(18);
    var filter = mapper.createFilter(PRODUCTS.PRICE, Prefix.EQ, value1, value2);
    var res =
        OneQuery.query(ctx.selectFrom(PRODUCTS)).filter(filter).toList(ProductsRecord::getPrice);

    assertThat(res).isNotEmpty().allMatch(i -> i.equals(value1) || i.equals(value2));
  }

  @Test
  @DisplayName("Create Filter by list values")
  void test3_6() {
    var value1 = BigInteger.valueOf(10);
    var value2 = BigInteger.valueOf(18);
    var filter = mapper.createFilter(PRODUCTS.PRICE, Prefix.EQ, List.of(value1, value2));
    var res =
        OneQuery.query(ctx.selectFrom(PRODUCTS)).filter(filter).toList(ProductsRecord::getPrice);

    assertThat(res).isNotEmpty().allMatch(i -> i.equals(value1) || i.equals(value2));
  }

  @Test
  @DisplayName("Convert object to target type")
  void test4_1() {
    Object rawValue = 10;
    var convertValue = mapper.convertValueSafely(rawValue, BigInteger.class);

    assertThat(convertValue).isInstanceOf(BigInteger.class);
  }

  @Test
  @DisplayName("Convert null values")
  void test4_2() {
    var convertValue = mapper.convertValueSafely(null, BigInteger.class);

    assertThat(convertValue).isNull();
  }

  @Test
  @DisplayName("Invalid convert values to target class")
  void test4_3() {
    Object rawValue = "ten";
    assertThrows(
        IllegalArgumentException.class,
        () -> mapper.convertValueSafely(rawValue, BigInteger.class));
  }

  @Test
  @DisplayName("Create Sort by key with direction")
  void test5_1() {
    var sort = mapper.createSort(FieldConfig.P_ID.getKey());
    var res =
        OneQuery.query(ctx.selectFrom(PRODUCTS)).sort(sort).toList(ProductsRecord::getProductid);

    assertThat(res.getFirst()).isEqualTo(1);

    sort = mapper.createSort("-" + FieldConfig.P_ID.getKey());
    res = OneQuery.query(ctx.selectFrom(PRODUCTS)).sort(sort).toList(ProductsRecord::getProductid);

    assertThat(res.getLast()).isEqualTo(1);
  }

  @Test
  @DisplayName("Create Sort by array keys with direction")
  void test5_2() {
    var sort = mapper.createSort(FieldConfig.P_CATEGORY_ID.getKey(), FieldConfig.P_ID.getKey());
    var res = OneQuery.query(ctx.selectFrom(PRODUCTS)).sort(sort).toList(ProductsRecord.class);

    assertThat(res.getFirst())
        .matches(pr -> pr.getProductid().equals(1))
        .matches(pr -> pr.getCategoryid().equals(1));

    sort =
        mapper.createSort(
            "-" + FieldConfig.P_CATEGORY_ID.getKey(), "-" + FieldConfig.P_ID.getKey());
    res = OneQuery.query(ctx.selectFrom(PRODUCTS)).sort(sort).toList(ProductsRecord.class);

    assertThat(res.getLast())
        .matches(pr -> pr.getProductid().equals(1))
        .matches(pr -> pr.getCategoryid().equals(1));
  }

  @Test
  @DisplayName("Create Sort by array keys with direction")
  void test5_3() {
    var sort =
        mapper.createSort(List.of(FieldConfig.P_CATEGORY_ID.getKey(), FieldConfig.P_ID.getKey()));
    var res = OneQuery.query(ctx.selectFrom(PRODUCTS)).sort(sort).toList(ProductsRecord.class);

    assertThat(res.getFirst())
        .matches(pr -> pr.getProductid().equals(1))
        .matches(pr -> pr.getCategoryid().equals(1));

    sort =
        mapper.createSort(
            List.of("-" + FieldConfig.P_CATEGORY_ID.getKey(), "-" + FieldConfig.P_ID.getKey()));
    res = OneQuery.query(ctx.selectFrom(PRODUCTS)).sort(sort).toList(ProductsRecord.class);

    assertThat(res.getLast())
        .matches(pr -> pr.getProductid().equals(1))
        .matches(pr -> pr.getCategoryid().equals(1));
  }

  @Test
  @DisplayName("Create Sort by key with direction")
  void test5_4() {
    var sort = mapper.createSort(FieldConfig.P_ID.getKey(), SortOrder.ASC);
    var res =
        OneQuery.query(ctx.selectFrom(PRODUCTS)).sort(sort).toList(ProductsRecord::getProductid);

    assertThat(res.getFirst()).isEqualTo(1);

    sort = mapper.createSort(FieldConfig.P_ID.getKey(), SortOrder.DESC);
    res = OneQuery.query(ctx.selectFrom(PRODUCTS)).sort(sort).toList(ProductsRecord::getProductid);

    assertThat(res.getLast()).isEqualTo(1);
  }

  @Test
  @DisplayName("Get field by field key from configuration")
  void test6_1() {
    var field = mapper.getField("PId");
    assertThat(FieldConfig.P_ID.getField()).isEqualTo(field);
  }

  @Test
  @DisplayName("Get field by field key error. No found key")
  void test6_2() {
    assertThrows(NoSuchElementException.class, () -> mapper.getField("unknown"));
  }
}
