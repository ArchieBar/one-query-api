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
