package io.codefine.onequery;

import io.codefine.onequery.configuration.FieldConfig;
import io.codefine.onequery.configuration.JooqConfiguration;
import io.codefine.onequery.mapper.OneQueryMapper;
import io.codefine.onequery.mapper.impl.PostgresMapper;
import org.jooq.DSLContext;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultDSLContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
public abstract class AbstractIsolatedEnvironment {
  protected final DefaultConfiguration postgresConfiguration =
      JooqConfiguration.getHikariConfig(POSTGRES_CONTAINER.getJdbcUrl());
  protected final DSLContext ctx = new DefaultDSLContext(postgresConfiguration);
  protected final OneQueryMapper mapper = new PostgresMapper(FieldConfig.collectMap()).register();

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
