package io.codefine.onequery.mapper.impl;

import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;

/**
 * Realisation for Postgres dialect.
 *
 * <p>Possibly to be removed in the future and brought to a unified implementation
 *
 * @author Artur Perun
 * @version 0.0.1
 */
public class PostgresMapper extends AbstractOneQueryMapper {
  public static final SQLDialect SQL_DIALECT = SQLDialect.POSTGRES;
  private final List<Schema> schemas;

  public PostgresMapper(Path propertiesPath, List<Schema> schemas) {
    super(propertiesPath);
    this.schemas = schemas;
  }

  public PostgresMapper(Map<String, String> mapper, List<Schema> schemas) {
    super(mapper);
    this.schemas = schemas;
  }

  @Override
  public SQLDialect getDialect() {
    return SQL_DIALECT;
  }

  @Override
  @SuppressWarnings("unchecked")
  public <R extends Record> Table<R> getTable(String table) {
    Table<R> tableR;

    for (Schema schema : schemas) {
      tableR = (Table<R>) schema.getTable(table.toUpperCase());
      if (tableR != null) {
        return tableR;
      }
    }

    throw new NoSuchElementException(String.format("Could not find table [%s]", table));
  }

  @Override
  @SuppressWarnings("unchecked")
  public <R extends Record, T> TableField<R, T> getTableField(String tableField) {
    String tableName = getTableNameByProperty(tableField);
    String fieldName = getFieldNameByProperty(tableField);
    TableField<R, T> tableFieldRT;

    try {
      for (Schema schema : schemas) {
        Table<?> table = schema.getTable(tableName.toUpperCase());
        if (table != null) {
          Field field = table.getClass().getField(fieldName);
          tableFieldRT = (TableField<R, T>) field.get(table);
          return tableFieldRT;
        }
      }
    } catch (NoSuchFieldException e) {
      throw new NoSuchElementException(
          String.format("Could not find field [%s] in table [%s]", fieldName, tableName), e);
    } catch (IllegalAccessException e) {
      throw new IllegalArgumentException(e);
    }

    throw new NoSuchElementException(
        String.format("Could not find field [%s] in table [%s]", fieldName, tableName));
  }
}
