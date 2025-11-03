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
import java.util.NoSuchElementException;
import one.query.api.mapper.OneQueryMapper;
import org.jooq.Field;

public abstract class AbstractOneQueryMapper implements OneQueryMapper {
  protected final ObjectMapper objectMapper;
  private final Map<String, Field<?>> configurationMap;

  protected AbstractOneQueryMapper(final Map<String, Field<?>> configurationMap) {
    this.configurationMap = configurationMap;
    this.objectMapper = new ObjectMapper();
  }

  protected AbstractOneQueryMapper(
      final ObjectMapper objectMapper, Map<String, Field<?>> configurationMap) {
    this.objectMapper = objectMapper;
    this.configurationMap = configurationMap;
  }

  @Override
  public boolean existsKey(final String key) {
    return configurationMap.containsKey(key);
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> T convertValueSafely(final Object rawValue, final Class<T> targetType) {
    if (rawValue == null) return null;

    if (targetType.isAssignableFrom(rawValue.getClass())) {
      return (T) rawValue;
    }

    try {
      return objectMapper.convertValue(rawValue, targetType);
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException(
          "Failed to cast %s to type %s".formatted(rawValue, targetType.getSimpleName()), e);
    }
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> Field<T> getField(final String key) {
    if (!configurationMap.containsKey(key)) {
      throw new NoSuchElementException("Field by key: [%s] not found".formatted(key));
    }

    return (Field<T>) configurationMap.get(key);
  }
}
