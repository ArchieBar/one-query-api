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
