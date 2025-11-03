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
import one.query.api.model.Sort;
import org.jooq.OrderField;
import org.jooq.Record;

/**
 * Interface allowing access to sorting methods
 *
 * @author Artur Perun
 * @version 0.0.1
 */
public interface OneQuerySortStep<R extends Record> extends OneQueryOptionalPaginationStep<R> {
  /** Method accepting {@link Sort}. The field must be in the format {@code [table.field]} */
  @NotNull
  OneQueryOptionalPaginationStep<R> sort(@NotNull Sort sort);

  /** Method accepting {@link Sort}. The field must be in the format {@code [table.field]} */
  @NotNull
  OneQueryOptionalPaginationStep<R> sort(@NotNull Sort... sorts);

  /** Method accepting {@link Sort}. The field must be in the format {@code [table.field]} */
  @NotNull
  OneQueryOptionalPaginationStep<R> sort(@NotNull Collection<Sort> sorts);

  /** Adds ordering fields. */
  @NotNull
  OneQueryOptionalPaginationStep<R> sortBy(@NotNull OrderField<?> field);

  /** Adds ordering fields. */
  @NotNull
  OneQueryOptionalPaginationStep<R> sortBy(@NotNull OrderField<?>... field);

  /** Adds ordering fields. */
  @NotNull
  OneQueryOptionalPaginationStep<R> sortBy(@NotNull Collection<? extends OrderField<?>> field);
}
