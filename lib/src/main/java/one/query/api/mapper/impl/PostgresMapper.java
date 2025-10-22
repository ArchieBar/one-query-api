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
