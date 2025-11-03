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
  OneQueryFilterStep<R> from(TableLike<?> table);

  /** Add a <code>FROM</code> clause to the query. */
  @NotNull
  OneQueryFilterStep<R> from(TableLike<?>... tables);

  /** Add a <code>FROM</code> clause to the query. */
  @NotNull
  OneQueryFilterStep<R> from(Collection<? extends TableLike<?>> tables);
}
