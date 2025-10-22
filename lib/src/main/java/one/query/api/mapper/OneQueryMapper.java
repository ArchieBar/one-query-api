package one.query.api.mapper;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import one.query.api.configuration.OneQueryConfiguration;
import one.query.api.model.Filter;
import one.query.api.model.Prefix;
import one.query.api.model.Sort;
import org.jooq.Field;
import org.jooq.SQLDialect;
import org.jooq.SortOrder;
import org.jooq.TableField;

/**
 * The class responsible for converting passed parameters to the required types
 *
 * @author Artur Perun
 */
public interface OneQueryMapper {

  /* ---------- system methods -------------------------------------------------------------------------------------- */

  /** Method for obtaining a mapper dialect */
  SQLDialect getDialect();

  /** Checks if the key exists in the configuration */
  boolean existsKey(final String key);

  /* ---------- .createFilter() ------------------------------------------------------------------------------------- */

  /** Method that creates a {@link Filter} object based on the passed values */
  default <T> Filter<T> createFilter(final String key, final Prefix prefix, final T value) {
    Field<T> field = getField(key);
    T convertValue = convertValueSafely(value, field.getType());
    return Filter.of(field, prefix, List.of(convertValue));
  }

  /** Method that creates a {@link Filter} object based on the passed values */
  default <T> Filter<T> createFilter(final String key, final Prefix prefix, final T... values) {
    Field<T> field = getField(key);
    List<T> convertValues =
        Arrays.stream(values)
            .map(rawValue -> convertValueSafely(rawValue, field.getType()))
            .toList();
    return Filter.of(field, prefix, convertValues);
  }

  /** Method that creates a {@link Filter} object based on the passed values */
  default <T> Filter<T> createFilter(final String key, final Prefix prefix, final List<T> values) {
    Field<T> field = getField(key);
    List<T> convertValues =
        values.stream().map(rawValue -> convertValueSafely(rawValue, field.getType())).toList();
    return Filter.of(field, prefix, convertValues);
  }

  /** Method that creates a {@link Filter} object based on the passed values */
  default <T> Filter<T> createFilter(
      final Field<T> field, final Prefix prefix, final Object value) {
    T convertValue = convertValueSafely(value, field.getType());
    return Filter.of(field, prefix, List.of(convertValue));
  }

  /** Method that creates a {@link Filter} object based on the passed values */
  default <T> Filter<T> createFilter(
      final Field<T> field, final Prefix prefix, final Object... values) {
    List<T> convertValues =
        Arrays.stream(values)
            .map(rawValue -> convertValueSafely(rawValue, field.getType()))
            .toList();
    return Filter.of(field, prefix, convertValues);
  }

  /** Method that creates a {@link Filter} object based on the passed values */
  default <T> Filter<T> createFilter(
      final Field<T> field, final Prefix prefix, final List<Object> values) {
    List<T> convertValues =
        values.stream().map(rawValue -> convertValueSafely(rawValue, field.getType())).toList();
    return Filter.of(field, prefix, convertValues);
  }

  <T> T convertValueSafely(final Object rawValue, final Class<T> targetType);

  /* ---------- .createSort() --------------------------------------------------------------------------------------- */

  /**
   * Method that creates a {@link Sort} object based on the passed values. The method accepts a key
   * from properties with sort direction
   */
  default Sort createSort(final String keyWithDirection) {
    return keyWithDirection.startsWith("-")
        ? new Sort(getField(keyWithDirection.substring(1)), SortOrder.DESC)
        : new Sort(getField(keyWithDirection), SortOrder.ASC);
  }

  /**
   * Method that creates a {@link Sort} object based on the passed values. The method accepts a key
   * from properties with sort direction
   */
  default List<Sort> createSort(final String... keysWithDirection) {
    return Arrays.stream(keysWithDirection).map(this::createSort).toList();
  }

  /**
   * Method that creates a {@link Sort} object based on the passed values. The method accepts a key
   * from properties with sort direction
   */
  default List<Sort> createSort(final Collection<String> keysWithDirection) {
    return keysWithDirection.stream().map(this::createSort).toList();
  }

  default Sort createSort(final String key, final SortOrder direction) {
    return new Sort(getField(key), direction);
  }

  /* ---------- .getTable() & .getTableField() ---------------------------------------------------------------------- */

  /**
   * Method that finds {@link TableField} using a key from properties. Goes through all the schemas
   * that were passed when creating the instance and tries to find the required class and field in
   * it
   */
  <T> Field<T> getField(String value);

  /* ---------- .register() ----------------------------------------------------------------------------------------- */

  /** Method to allow a mapper to be registered in the registry */
  default OneQueryMapper register() {
    OneQueryConfiguration.registerMapper(this);
    return this;
  }

  /**
   * Method to allow a mapper to be registered in the registry. Accepts {@link SQLDialect} which
   * will be bound to this mapper implementation
   */
  default OneQueryMapper register(final SQLDialect dialect) {
    OneQueryConfiguration.registerMapper(dialect, this);
    return this;
  }
}
