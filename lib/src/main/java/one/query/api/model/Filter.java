package one.query.api.model;

import java.util.Collection;
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
}
