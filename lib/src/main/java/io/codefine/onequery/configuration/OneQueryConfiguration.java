package io.codefine.onequery.configuration;

import io.codefine.onequery.impl.OneQuery;
import io.codefine.onequery.mapper.OneQueryMapper;
import java.util.Collection;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;
import org.jooq.SQLDialect;

/**
 * Configuration class for the API.
 *
 * <p>Serves as an implementation of mapper register, where static field Map: {@link SQLDialect},
 * {@link OneQueryMapper} is used to bind SQL dialect to a certain mapper.
 *
 * <p>Each mapper has a {@code .register()} method that allows you to register the mapper in a
 * registry with a dialect that must be defined in the class, but you can also register a mapper
 * using the static {@code .registerMapper()} method of the configuration class. You can safely
 * reuse default mappers, use the {@code .getMapper(SQLDialect)} method to get the registered
 * mapper.
 *
 * <p>If you are using SpringFramework, you can control dependencies using the features provided by
 * SpringFramework to manage your bins, but if not, this class will serve as a helper. But you still
 * need to call the {@code .register()} method on the mapper you create, as this configuration is
 * used to automatically load the mapper into {@link OneQuery}
 *
 * @author Artur Perun
 * @version 0.0.1
 */
@SuppressWarnings("unused")
public class OneQueryConfiguration {
  private static final Map<SQLDialect, OneQueryMapper> MAPPERS = new ConcurrentHashMap<>();

  private OneQueryConfiguration() {}

  /** Register mapper. Uses the {@code .getSqlDialect()} method to get the mapper dialect */
  public static void registerMapper(OneQueryMapper mapper) {
    MAPPERS.put(mapper.getDialect(), mapper);
  }

  /** Register mapper. */
  public static void registerMapper(SQLDialect dialect, OneQueryMapper mapper) {
    MAPPERS.put(dialect, mapper);
  }

  /** Register mappers. Uses the {@code .getSqlDialect()} method to get the mapper dialect */
  public static void registerMappers(OneQueryMapper... mappers) {
    for (OneQueryMapper mapper : mappers) {
      registerMapper(mapper);
    }
  }

  /** Register mappers. Uses the {@code .getSqlDialect()} method to get the mapper dialect */
  public static void registerMappers(Collection<OneQueryMapper> mappers) {
    for (OneQueryMapper mapper : mappers) {
      registerMapper(mapper);
    }
  }

  /**
   * Find a mapper by {@link SQLDialect} supported by JOOQ. If the mapper is not found by the passed
   * dialect, it will cause a {@link NoSuchElementException}
   */
  @SuppressWarnings("unchecked")
  public static <T extends OneQueryMapper> T getMapper(SQLDialect mapperDialect) {
    T mapper = (T) MAPPERS.get(mapperDialect);

    if (mapper == null) {
      throw new NoSuchElementException(
          String.format("SQL dialect: [%s] is not registered", mapperDialect.getName()));
    }

    return mapper;
  }
}
