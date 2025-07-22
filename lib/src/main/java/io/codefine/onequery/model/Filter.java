package io.codefine.onequery.model;

import java.util.Collection;
import java.util.Objects;
import org.jooq.Field;

/**
 * A class that represents a filter.
 *
 * @author Artur Perun
 * @version 0.0.7
 */
public record Filter<T>(Field<T> field, Prefix prefix, Collection<T> values) {
  public static <T> Filter<T> of(Field<T> filed, Prefix prefix, Collection<T> values) {
    return new Filter<>(filed, prefix, values);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Filter<?> filter = (Filter<?>) o;
    return Objects.equals(field, filter.field)
        && prefix == filter.prefix
        && Objects.equals(values, filter.values);
  }

  @Override
  public String toString() {
    return "Filter{" + "tableField=" + field + ", prefix=" + prefix + ", values=" + values + '}';
  }
}
