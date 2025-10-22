package one.query.api.configuration;

import com.zaxxer.hikari.HikariDataSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import org.jooq.SQLDialect;
import org.jooq.conf.RenderNameCase;
import org.jooq.conf.Settings;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultConfiguration;

public class JooqConfiguration {
  private static DefaultConfiguration configuration;

  private static final String PROPERTY_NAME_DB_DRIVER = "db.driver";
  private static final String PROPERTY_NAME_DB_PASSWORD = "db.password";
  private static final String PROPERTY_NAME_DB_USERNAME = "db.username";
  private static final String PROPERTY_NAME_JOOQ_SQL_DIALECT = "sql.dialect";

  private JooqConfiguration() {}

  public static DefaultConfiguration getHikariConfig(String jdbcUrl) {
    HikariDataSource dataSource = new HikariDataSource();
    Properties properties = new Properties();

    try (FileInputStream inputStream =
        new FileInputStream("build/resources/test/data.source/postgres.properties")) {
      properties.load(inputStream);
    } catch (IOException ignored) {
      throw new RuntimeException("Failed to load properties");
    }

    dataSource.setDriverClassName(properties.getProperty(PROPERTY_NAME_DB_DRIVER));
    dataSource.setJdbcUrl(jdbcUrl);
    dataSource.setUsername(properties.getProperty(PROPERTY_NAME_DB_USERNAME));
    dataSource.setPassword(properties.getProperty(PROPERTY_NAME_DB_PASSWORD));

    DataSourceConnectionProvider connectionProvider = new DataSourceConnectionProvider(dataSource);

    configuration = new DefaultConfiguration();

    var settings =
        new Settings()
            .withRenderNameCase(RenderNameCase.LOWER)
            .withExecuteWithOptimisticLocking(true);
    configuration.set(connectionProvider);
    configuration.setSettings(settings);

    var sqlDialectName = properties.getProperty(PROPERTY_NAME_JOOQ_SQL_DIALECT);
    configuration.setSQLDialect(SQLDialect.valueOf(sqlDialectName));
    return configuration;
  }
}
