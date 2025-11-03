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
import one.query.api.model.Filter;
import org.jooq.Condition;
import org.jooq.Record;

/**
 * Interface allowing access to filtering methods
 *
 * @author Artur Perun
 * @version 0.0.7
 */
public interface OneQueryFilterStep<R extends Record> extends OneQuerySortStep<R> {
  /** Method accepting {@link Filter}. The tableField must be in the format {@code [table.field]} */
  @NotNull
  <T> OneQuerySortStep<R> filter(@NotNull Filter<T> filter);

  /** Method accepting {@link Filter}. The tableField must be in the format {@code [table.field]} */
  @NotNull
  OneQuerySortStep<R> filter(@NotNull Filter<?>... filters);

  /** Method accepting {@link Filter}. The tableField must be in the format {@code [table.field]} */
  @NotNull
  OneQuerySortStep<R> filter(@NotNull Collection<Filter<?>> filters);

  /**
   * Adds a new condition to the query, connecting them to existing conditions with {@link
   * org.jooq.Operator#AND}.
   */
  @NotNull
  OneQuerySortStep<R> filterBy(@NotNull Condition condition);

  /**
   * Adds a new condition to the query, connecting them to existing conditions with {@link
   * org.jooq.Operator#AND}.
   */
  @NotNull
  OneQuerySortStep<R> filterBy(@NotNull Condition... conditions);

  /**
   * Adds a new condition to the query, connecting them to existing conditions with {@link
   * org.jooq.Operator#AND}.
   */
  @NotNull
  OneQuerySortStep<R> filterBy(@NotNull Collection<? extends Condition> conditions);
}
