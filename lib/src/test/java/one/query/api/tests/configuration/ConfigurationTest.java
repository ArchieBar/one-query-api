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
package one.query.api.tests.configuration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import one.query.api.configuration.FieldConfig;
import one.query.api.configuration.OneQueryConfiguration;
import one.query.api.mapper.OneQueryMapper;
import one.query.api.mapper.impl.AbstractOneQueryMapper;
import one.query.api.mapper.impl.PostgresMapper;
import org.jooq.SQLDialect;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ConfigurationTest {

  @Test
  @DisplayName("Register mapper without dialect in params")
  void test1() {
    PostgresMapper registeredMapper =
        new PostgresMapper(new ObjectMapper(), FieldConfig.collectMap());
    OneQueryConfiguration.registerMapper(registeredMapper);

    OneQueryMapper mapper = OneQueryConfiguration.getMapper(SQLDialect.POSTGRES);
    assertThat(mapper).isNotNull();

    OneQueryConfiguration.removeMapper(SQLDialect.POSTGRES);
  }

  @Test
  @DisplayName("Register mapper with default dialect")
  void test2() {
    PostgresMapper registeredMapper =
        new PostgresMapper(new ObjectMapper(), FieldConfig.collectMap());
    OneQueryConfiguration.registerMapper(SQLDialect.DEFAULT, registeredMapper);

    OneQueryMapper mapper = OneQueryConfiguration.getMapper(SQLDialect.DEFAULT);
    assertThat(mapper).isNotNull();

    OneQueryConfiguration.removeMapper(SQLDialect.DEFAULT);
  }

  @Test
  @DisplayName("Registered array mappers")
  void test3() {
    PostgresMapper postgresMapper =
        new PostgresMapper(new ObjectMapper(), FieldConfig.collectMap());
    TestMapper testMapper = new TestMapper();
    OneQueryConfiguration.registerMappers(postgresMapper, testMapper);

    OneQueryMapper mapper = OneQueryConfiguration.getMapper(SQLDialect.POSTGRES);
    assertThat(mapper).isNotNull();

    mapper = OneQueryConfiguration.getMapper(SQLDialect.DEFAULT);
    assertThat(mapper).isNotNull();

    OneQueryConfiguration.removeMapper(SQLDialect.POSTGRES);
    OneQueryConfiguration.removeMapper(SQLDialect.DEFAULT);
  }

  @Test
  @DisplayName("Registered collection mappers")
  void test4() {
    PostgresMapper postgresMapper =
        new PostgresMapper(new ObjectMapper(), FieldConfig.collectMap());
    TestMapper testMapper = new TestMapper();
    OneQueryConfiguration.registerMappers(List.of(postgresMapper, testMapper));

    OneQueryMapper mapper = OneQueryConfiguration.getMapper(SQLDialect.POSTGRES);
    assertThat(mapper).isNotNull();

    mapper = OneQueryConfiguration.getMapper(SQLDialect.DEFAULT);
    assertThat(mapper).isNotNull();

    OneQueryConfiguration.removeMapper(SQLDialect.POSTGRES);
    OneQueryConfiguration.removeMapper(SQLDialect.DEFAULT);
  }

  @Test
  @DisplayName("Not such mapper exception")
  void test5() {
    NoSuchElementException ex =
        assertThrows(
            NoSuchElementException.class,
            () -> OneQueryConfiguration.getMapper(SQLDialect.DEFAULT));

    assertThat(ex).isNotNull();
  }

  @Test
  @DisplayName("Register mapper by another sql dialect")
  void test6() {
    var mapper = new PostgresMapper(new ObjectMapper(), FieldConfig.collectMap());
    mapper.register(SQLDialect.DEFAULT);

    var registeredMapper = OneQueryConfiguration.getMapper(SQLDialect.DEFAULT);
    assertThat(registeredMapper).isEqualTo(mapper);
  }

  static class TestMapper extends AbstractOneQueryMapper {
    public TestMapper() {
      super(new ObjectMapper(), new HashMap<>());
    }

    @Override
    public SQLDialect getDialect() {
      return SQLDialect.DEFAULT;
    }
  }
}
