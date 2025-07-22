package io.codefine.onequery;

import jakarta.validation.constraints.NotNull;
import java.util.Collection;
import org.jooq.Record;
import org.jooq.TableLike;

/**
 * An auxiliary interface that helps to assemble a valid request. Adds a query table to the query
 * that was received in the {@code .query()} method
 *
 * <p>Example jooq builder from query:
 *
 * <pre><code>
 *     create.select()
 * </code></pre>
 *
 * @version 0.0.1
 * @author Artur Perun
 */
public interface OneQueryFromStep<R extends Record> {
  /** Add a <code>FROM</code> clause to the query. */
  @NotNull
  OneQueryFilterStep<R> from(TableLike<R> table);

  /** Add a <code>FROM</code> clause to the query. */
  @NotNull
  OneQueryFilterStep<R> from(TableLike<R>... tables);

  /** Add a <code>FROM</code> clause to the query. */
  @NotNull
  OneQueryFilterStep<R> from(Collection<? extends TableLike<R>> tables);
}
