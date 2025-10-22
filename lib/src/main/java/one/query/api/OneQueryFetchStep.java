package one.query.api;

import jakarta.validation.constraints.NotNull;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.exception.DataAccessException;

/**
 * The interface repeats the logic of returning values from the {@link org.jooq.ResultQuery}
 * interface and allows you to use Jooq types as the return value.
 *
 * @author Artur Perun
 * @version 0.0.5
 */
public interface OneQueryFetchStep<R extends Record> {
  /**
   * The method is a copy of the <code>.fetch()</code> method from the {@link org.jooq.ResultQuery}
   * interface in Jooq
   *
   * @return The result. This will never be <code>null</code>.
   * @throws DataAccessException if something went wrong executing the query.
   * @see org.jooq.ResultQuery#fetch()
   */
  @NotNull
  Result<R> fetch() throws DataAccessException;
}
