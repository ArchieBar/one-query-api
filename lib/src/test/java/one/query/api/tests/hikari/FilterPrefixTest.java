package one.query.api.tests.hikari;

import static one.query.api.jooq.generated.demo_schema.Tables.PRODUCTS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigInteger;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;
import one.query.api.AbstractIsolatedEnvironment;
import one.query.api.impl.OneQuery;
import one.query.api.jooq.generated.demo_schema.tables.records.ProductsRecord;
import one.query.api.model.Filter;
import one.query.api.model.Prefix;
import org.jooq.TableField;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class FilterPrefixTest extends AbstractIsolatedEnvironment {
  public Stream<Arguments> prefixArguments() {
    return Stream.of(
        Arguments.of(
            PRODUCTS.PRODUCTNAME,
            Prefix.EQ,
            List.of("Chais", "Chang"),
            (Predicate<String>) s -> s.equals("Chais") || s.equals("Chang")),
        Arguments.of(
            PRODUCTS.PRODUCTNAME,
            Prefix.NE,
            List.of("Chais", "Chang"),
            (Predicate<String>) s -> !s.equals("Chais") && !s.equals("Chang")),
        Arguments.of(
            PRODUCTS.PRODUCTNAME,
            Prefix.SW,
            List.of("Ch", "Chan"),
            (Predicate<String>) s -> s.startsWith("Ch") || s.startsWith("Chan")),
        Arguments.of(
            PRODUCTS.PRODUCTNAME,
            Prefix.EW,
            List.of("is", "ng"),
            (Predicate<String>) s -> s.endsWith("is") || s.endsWith("ng")),
        Arguments.of(
            PRODUCTS.PRODUCTNAME,
            Prefix.LIKE,
            List.of("ai", "ang"),
            (Predicate<String>)
                s ->
                    s.toLowerCase(Locale.ROOT).contains("ai")
                        || s.toLowerCase(Locale.ROOT).contains("ang")),
        Arguments.of(
            PRODUCTS.PRICE,
            Prefix.BW,
            List.of(
                BigInteger.valueOf(10),
                BigInteger.valueOf(15),
                BigInteger.valueOf(20),
                BigInteger.valueOf(25)),
            (Predicate<BigInteger>)
                i ->
                    (i.compareTo(BigInteger.valueOf(9L)) > 0
                            && i.compareTo(BigInteger.valueOf(16L)) < 0)
                        || (i.compareTo(BigInteger.valueOf(19L)) > 0
                            && i.compareTo(BigInteger.valueOf(26L)) < 0)),
        Arguments.of(
            PRODUCTS.PRICE, Prefix.IS_NULL, List.of(), (Predicate<BigInteger>) Objects::isNull),
        Arguments.of(
            PRODUCTS.PRICE,
            Prefix.IS_NOT_NULL,
            List.of(),
            (Predicate<BigInteger>) Objects::nonNull));
  }

  @ParameterizedTest(name = "Field: {0}, Prefix: {1}")
  @MethodSource("prefixArguments")
  @DisplayName("Prefix test")
  <T> void testP1(
      TableField<ProductsRecord, T> field, Prefix prefix, List<T> values, Predicate<T> predicate) {
    var filter = new Filter<>(field, prefix, values);
    var res =
        OneQuery.query(ctx.selectFrom(field.getTable())).filter(filter).toList(r -> r.get(field));

    assertThat(res).isNotEmpty().allMatch(predicate);
  }

  @Test
  @DisplayName("Invalid field type for like prefix")
  void test1() {
    var filter = new Filter<>(PRODUCTS.PRICE, Prefix.LIKE, List.of(BigInteger.valueOf(1)));
    var query = OneQuery.query(ctx.selectFrom(PRODUCTS));
    assertThrows(IllegalArgumentException.class, () -> query.filter(filter));
  }

  @Test
  @DisplayName("Invalid values type for bw prefix")
  void test2() {
    var filter = new Filter<>(PRODUCTS.PRICE, Prefix.BW, List.of(BigInteger.valueOf(1)));
    var query = OneQuery.query(ctx.selectFrom(PRODUCTS));
    assertThrows(IllegalArgumentException.class, () -> query.filter(filter));
  }

  @Test
  @DisplayName("Get id prefix")
  void test3() {
    assertThat(Prefix.EQ.getId()).isEqualTo(1);
  }

  @Test
  @DisplayName("Get name prefix")
  void test4() {
    assertThat(Prefix.EQ.getName()).isEqualTo("equals");
  }
}
