/*
 * Copyright 2025 One Query API contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package one.query.api;

import jakarta.validation.constraints.NotNull;
import one.query.api.model.Page;
import org.jooq.Record;

/**
 * An interface containing pagination methods returns the following step: {@link
 * OneQueryPaginationResultStep}.
 *
 * <p>Example jooq builder from query:
 *
 * <pre><code>
 *     create.select()
 *         .from()
 *         .join({@link org.jooq.TableLike})
 *         .on()
 *         .where()
 *         .and()
 * </code></pre>
 *
 * @version 0.0.1
 * @author Artur Perun, Artyom Sushchenko
 */
public interface OneQueryPaginationStep<R extends Record> {
  /**
   * The method takes {@link Page} and returns {@link OneQueryPaginationResultStep}. The total
   * number of elements is calculated "under the hood" using the {@code .selectCount()}, sort,
   * offset using {@code .offset()}, and limit number {@code .limit()}.
   *
   * <p>Page numbering must start with 0.
   */
  @NotNull
  OneQueryPaginationResultStep<R> paginate(@NotNull Page page);

  /**
   * Takes {@code page}, {@code size}, returns {@link OneQueryPaginationResultStep}, the total
   * number of elements is calculated "under the hood" using the {@code .selectCount()}, offset
   * using {@code .offset()}, and limit number {@code .limit()}.
   *
   * <p>Page numbering must start with 0.
   */
  @NotNull
  OneQueryPaginationResultStep<R> paginate(int page, int size);
}
