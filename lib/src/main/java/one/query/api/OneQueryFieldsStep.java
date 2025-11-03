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
