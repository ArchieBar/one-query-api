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
