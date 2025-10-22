package one.query.api;

import jakarta.validation.constraints.NotNull;
import org.jooq.CommonTableExpression;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.ResultQuery;

/**
 * An interface that helps create a CTE, which can contain filtering, pagination, and sorting
 *
 * @author Artur Perun
 * @version 0.0.6
 */
public interface OneQueryCommonTableStep<R extends Record> {

  /**
   * A method that returns a CTE with the specified name
   *
   * @param name cte name
   * @return {@link CommonTableExpression}
   * @see Name#as(ResultQuery)
   */
  @NotNull
  CommonTableExpression<R> toCommonTable(String name);

  /**
   * Returns the total number of locations after calling {@link OneQueryPaginationStep#paginate}, if
   * {@link OneQueryPaginationStep#paginate} was not called, it will return {@code null}
   *
   * @return Total number of pagination results or {@code null}
   */
  Long getTotal();
}
