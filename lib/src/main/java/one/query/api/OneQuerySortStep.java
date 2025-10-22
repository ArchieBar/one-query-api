package one.query.api;

import jakarta.validation.constraints.NotNull;
import java.util.Collection;
import one.query.api.model.Sort;
import org.jooq.OrderField;
import org.jooq.Record;

/**
 * Interface allowing access to sorting methods
 *
 * @author Artur Perun
 * @version 0.0.1
 */
public interface OneQuerySortStep<R extends Record> extends OneQueryOptionalPaginationStep<R> {
  /** Method accepting {@link Sort}. The field must be in the format {@code [table.field]} */
  @NotNull
  OneQueryOptionalPaginationStep<R> sort(@NotNull Sort sort);

  /** Method accepting {@link Sort}. The field must be in the format {@code [table.field]} */
  @NotNull
  OneQueryOptionalPaginationStep<R> sort(@NotNull Sort... sorts);

  /** Method accepting {@link Sort}. The field must be in the format {@code [table.field]} */
  @NotNull
  OneQueryOptionalPaginationStep<R> sort(@NotNull Collection<Sort> sorts);

  /** Adds ordering fields. */
  @NotNull
  OneQueryOptionalPaginationStep<R> sortBy(@NotNull OrderField<?> field);

  /** Adds ordering fields. */
  @NotNull
  OneQueryOptionalPaginationStep<R> sortBy(@NotNull OrderField<?>... field);

  /** Adds ordering fields. */
  @NotNull
  OneQueryOptionalPaginationStep<R> sortBy(@NotNull Collection<? extends OrderField<?>> field);
}
