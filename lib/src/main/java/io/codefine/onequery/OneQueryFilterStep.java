package io.codefine.onequery;

import io.codefine.onequery.model.Filter;
import jakarta.validation.constraints.NotNull;
import java.util.Collection;
import org.jooq.Condition;
import org.jooq.Record;

/**
 * Interface allowing access to filtering methods
 *
 * @author Artur Perun
 * @version 0.0.7
 */
public interface OneQueryFilterStep<R extends Record> extends OneQuerySortStep<R> {
  /** Method accepting {@link Filter}. The tableField must be in the format {@code [table.field]} */
  @NotNull
  <T> OneQuerySortStep<R> filter(@NotNull Filter<T> filter);

  /** Method accepting {@link Filter}. The tableField must be in the format {@code [table.field]} */
  @NotNull
  OneQuerySortStep<R> filter(@NotNull Filter<?>... filters);

  /** Method accepting {@link Filter}. The tableField must be in the format {@code [table.field]} */
  @NotNull
  OneQuerySortStep<R> filter(@NotNull Collection<Filter<?>> filters);

  /**
   * Adds a new condition to the query, connecting them to existing conditions with {@link
   * org.jooq.Operator#AND}.
   */
  @NotNull
  OneQuerySortStep<R> filterBy(@NotNull Condition condition);

  /**
   * Adds a new condition to the query, connecting them to existing conditions with {@link
   * org.jooq.Operator#AND}.
   */
  @NotNull
  OneQuerySortStep<R> filterBy(@NotNull Condition... conditions);

  /**
   * Adds a new condition to the query, connecting them to existing conditions with {@link
   * org.jooq.Operator#AND}.
   */
  @NotNull
  OneQuerySortStep<R> filterBy(@NotNull Collection<? extends Condition> conditions);
}
