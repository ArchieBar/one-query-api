package one.query.api;

import jakarta.validation.constraints.NotNull;
import org.jooq.Condition;
import org.jooq.Operator;
import org.jooq.Record;
import org.jooq.TableLike;

/**
 * Helper interface, used to add a key to join tables if it was not added in the original query
 * query
 *
 * <p>Example jooq builder from query:
 *
 * <pre><code>
 *     create.select()
 *         .from()
 *         .join({@link TableLike})
 * </code></pre>
 *
 * @version 0.0.1
 * @author Artur Perun
 */
public interface OneQueryOnStep<R extends Record> {
  /**
   * Add an <code>ON</code> clause to the previous <code>JOIN</code>, connecting them with each
   * other with {@link Operator#AND}.
   */
  @NotNull
  OneQueryFilterStep<R> on(Condition condition);

  /**
   * Add an <code>ON</code> clause to the previous <code>JOIN</code>, connecting them with each
   * other with {@link Operator#AND}.
   */
  @NotNull
  OneQueryFilterStep<R> on(Condition... conditions);
}
