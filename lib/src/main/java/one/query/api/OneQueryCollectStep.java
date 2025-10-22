package one.query.api;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import org.jooq.Record;
import org.jooq.RecordMapper;

/**
 * Interface casts the result to a specific collection, which are terminal methods
 *
 * <p>In version 0.0.1, only list casting is available
 *
 * @author Artur Perun
 * @version 0.0.1
 */
public interface OneQueryCollectStep<R extends Record>
    extends OneQueryFetchStep<R>, OneQueryFieldsStep<R> {
  /**
   * The method takes a class to which all objects in the response must be cast. If you need to
   * welcome an entity to the generated jooq class, you need to use the classes from the <b>{@code
   * pojos}</b> package.
   */
  @NotNull
  <C> List<C> toList(Class<C> clazz);

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
  <C> List<C> toList(RecordMapper<R, C> mapper);
}
