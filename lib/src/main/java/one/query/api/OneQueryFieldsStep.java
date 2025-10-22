package one.query.api;

import jakarta.validation.constraints.NotNull;
import org.jooq.Name;
import org.jooq.Record;

/**
 * An intermediate interface responsible for specifying field names in the CTE
 *
 * @see Name#fields(String...)
 * @author Artur Perun
 * @version 0.0.6
 */
public interface OneQueryFieldsStep<R extends Record> extends OneQueryCommonTableStep<R> {

  /**
   * A copy of the {@link Name#fields(String...)} method that specifies the name of the fields in
   * the CTE
   *
   * @param fields - field names
   * @return {@link OneQueryCommonTableStep}
   * @see Name#fields(String...)
   */
  @NotNull
  OneQueryCommonTableStep<R> fields(String... fields);
}
