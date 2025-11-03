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
package one.query.api.mapper.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.jooq.Field;
import org.jooq.SQLDialect;

public class PostgresMapper extends AbstractOneQueryMapper {
  public static final SQLDialect SQL_DIALECT = SQLDialect.POSTGRES;

  public PostgresMapper(final Map<String, Field<?>> configurationMap) {
    super(configurationMap);
  }

  public PostgresMapper(
      final ObjectMapper objectMapper, final Map<String, Field<?>> configurationMap) {
    super(objectMapper, configurationMap);
  }

  @Override
  public SQLDialect getDialect() {
    return SQL_DIALECT;
  }
}
