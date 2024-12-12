package io.codefine.onequery;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
public abstract class AbstractIsolatedEnvironment {
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
