package io.codefine.onequery.mapper;

import io.codefine.onequery.configuration.OneQueryConfiguration;
import io.codefine.onequery.model.Filter;
import io.codefine.onequery.model.Sort;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.Table;
import org.jooq.TableField;

public interface OneQueryMapper {

  /* ---------- system methods -------------------------------------------------------------------------------------- */

  /** Method for obtaining a mapper dialect */
  SQLDialect getDialect();

  /** Method for obtaining a printout of properties */
  Map<String, String> getMap();

  /* ---------- <String> .getProperty() ----------------------------------------------------------------------------- */

  /**
   * Method for obtaining a property by key. Pass {@code [key]} and get the property {@code
   * [table.field]}
   */
  String getProperty(String key);

  /**
   * Method for obtaining a table name by key. Pass {@code [key]} and get the table name {@code
   * [table]}
   */
  default String getTableNameByKey(String key) {
    String value = getProperty(key);
    String[] split = value.split("\\.");
    if (split.length < 2) {
      throw new IllegalArgumentException(
          String.format("Incorrect format in mapper: [%s]. Should be [table.field]", value));
    }
    return split[0].toUpperCase();
  }

  /**
   * Method for obtaining a table name by property. Pass {@code [table.key]} and get the table name
   * {@code [table]}
   */
  default String getTableNameByProperty(String property) {
    String[] split = property.split("\\.");
    if (split.length < 2) {
      throw new IllegalArgumentException(
          String.format("Incorrect format: [%s]. Should be [table.field]", property));
    }
    return split[0].toUpperCase();
  }

  /**
   * Method for obtaining a field name by key. Pass {@code [key]} and get the field name {@code
   * [field]}
   */
  default String getFieldNameByKey(String key) {
    String value = getProperty(key);
    String[] split = value.split("\\.");
    if (split.length < 2) {
      throw new IllegalArgumentException(
          String.format("Incorrect format in mapper: [%s]. Should be [table.field]", value));
    }
    return split[1].toUpperCase();
  }

  /**
   * Method for obtaining a field name by property. Pass {@code [table.field]} and get the field
   * name {@code [field]}
   */
  default String getFieldNameByProperty(String property) {
    String[] split = property.split("\\.");
    if (split.length < 2) {
      throw new IllegalArgumentException(
          String.format("Incorrect format: [%s]. Should be [table.field]", property));
    }
    return split[1].toUpperCase();
  }

  /* ---------- .convertFilter() & .createFilter() ------------------------------------------------------------------ */

  /**
   * Method that converts {@link Filter} results with {@code tableField} = {@code properties.key} to
   * {@link Filter} with field value {@code tableField} = {@code table.filed}
   */
  default Filter convertFilter(Filter filter) {
    return new Filter(getProperty(filter.tableField()), filter.prefix(), filter.values());
  }

  /** Method that creates a {@link Filter} object based on the passed values */
  default Filter createFilter(String key, Filter.Prefix prefix, String value) {
    return new Filter(getProperty(key), prefix, value);
  }

  /** Method that creates a {@link Filter} object based on the passed values */
  default Filter createFilter(String key, Filter.Prefix prefix, String... values) {
    return new Filter(getProperty(key), prefix, values);
  }

  /** Method that creates a {@link Filter} object based on the passed values */
  default Filter createFilter(String key, Filter.Prefix prefix, Collection<String> values) {
    return new Filter(getProperty(key), prefix, values);
  }

  /* ---------- .convertSort() & .createSort() ---------------------------------------------------------------------- */

  /**
   * Method that converts {@link Sort} results with {@code field} = {@code properties.key} to {@link
   * Sort} with field value {@code filed} = {@code table.filed}
   */
  default Sort convertSort(Sort sort) {
    return new Sort(getProperty(sort.field()), sort.direction());
  }

  /**
   * Method that converts {@link Sort} results with {@code field} = {@code properties.key} to {@link
   * Sort} with field value {@code filed} = {@code table.filed}
   */
  default List<Sort> convertSort(Sort... sorts) {
    return Arrays.stream(sorts).map(this::convertSort).toList();
  }

  /**
   * Method that converts {@link Sort} results with {@code field} = {@code properties.key} to {@link
   * Sort} with field value {@code filed} = {@code table.filed}
   */
  default List<Sort> convertSort(Collection<Sort> sorts) {
    return sorts.stream().map(this::convertSort).toList();
  }

  /**
   * Method that creates a {@link Sort} object based on the passed values. The method accepts a key
   * from properties with sort direction
   */
  default Sort createSort(String keyWithDirection) {
    return keyWithDirection.startsWith("-")
        ? new Sort(getProperty(keyWithDirection.substring(1)), Sort.Direction.DESC)
        : new Sort(getProperty(keyWithDirection), Sort.Direction.ASC);
  }

  /**
   * Method that creates a {@link Sort} object based on the passed values. The method accepts a key
   * from properties with sort direction
   */
  default List<Sort> createSort(String... keysWithDirection) {
    return Arrays.stream(keysWithDirection).map(this::createSort).toList();
  }

  /**
   * Method that creates a {@link Sort} object based on the passed values. The method accepts a key
   * from properties with sort direction
   */
  default List<Sort> createSort(Collection<String> keysWithDirection) {
    return keysWithDirection.stream().map(this::createSort).toList();
  }

  /* ---------- .getTable() & .getTableField() ---------------------------------------------------------------------- */

  /**
   * Method that finds {@link Table} by a key from properties. Goes through all the schemas that
   * were passed in when the instance was created and tries to find the required class
   */
  <R extends Record> Table<R> getTable(String value);

  /**
   * Method that finds {@link TableField} using a key from properties. Goes through all the schemas
   * that were passed when creating the instance and tries to find the required class and field in
   * it
   */
  <R extends Record, T> TableField<R, T> getTableField(String value);

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
  default OneQueryMapper register(SQLDialect dialect) {
    OneQueryConfiguration.registerMapper(dialect, this);
    return this;
  }
}
