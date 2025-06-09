package io.codefine.onequery;

import io.codefine.onequery.model.PaginationResult;
import jakarta.validation.constraints.NotNull;
import org.jooq.Record;
import org.jooq.RecordMapper;

/**
 * The interface results in {@link PaginationResult}, which are terminal methods
 *
 * @version 0.0.1
 * @author Artur Perun, Artyom Sushchenko
 */
public interface OneQueryPaginationResultStep<R extends Record> extends OneQueryCollectStep<R> {
  /**
   * The method takes a class to which all objects in the response must be cast. If you need to
   * welcome an entity to the generated jooq class, you need to use the classes from the <b>{@code
   * pojos}</b> package.
   */
  @NotNull
  <C> PaginationResult<C> toPaginationResult(Class<C> clazz);

  /**
   * The method accepts an entity mapper with {@link Record} as a parameter.
   *
   * <p>Example mapper:
   *
   * <pre><code>
   *     record2 -> {
   *        String[] array = new String[2];
   *        array[0] = record2.value1();
   *        array[1] = record2.value2();
   *        return array;
   *     }
   * </code></pre>
   */
  @NotNull
  <C> PaginationResult<C> toPaginationResult(RecordMapper<R, C> mapper);
}
