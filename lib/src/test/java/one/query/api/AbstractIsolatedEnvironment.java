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

import com.zaxxer.hikari.HikariDataSource;
import one.query.api.configuration.FieldConfig;
import one.query.api.configuration.JooqConfiguration;
import one.query.api.mapper.OneQueryMapper;
import one.query.api.mapper.impl.PostgresMapper;
import org.jooq.DSLContext;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultDSLContext;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class AbstractIsolatedEnvironment {
  private static HikariDataSource dataSource;
  protected static DSLContext ctx;
  protected final OneQueryMapper mapper = new PostgresMapper(FieldConfig.collectMap()).register();

  @BeforeAll
  static void setUp() {
    var cfg = JooqConfiguration.getHikariConfig(POSTGRES_CONTAINER.getJdbcUrl());
    dataSource =
        (HikariDataSource) ((DataSourceConnectionProvider) cfg.connectionProvider()).dataSource();
    ctx = new DefaultDSLContext(cfg);
  }

  @AfterAll
  static void clear() {
    dataSource.close();
  }

  @SuppressWarnings("resource")
  @Container
  protected static final PostgreSQLContainer<?> POSTGRES_CONTAINER =
      new PostgreSQLContainer<>(DockerImageName.parse("postgres:16.2-alpine3.19"))
          .withUsername("postgres")
          .withPassword("123456")
          .withDatabaseName("demo_db")
          .withExtraHost("localhost", "127.0.0.1")
          .withExposedPorts(5432)
          .withInitScript("db/sql/init_test_data.sql");
}
