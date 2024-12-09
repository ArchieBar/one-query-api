package io.codefine.mapper.impl;

import io.codefine.mapper.OneQueryMapper;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Properties;

/**
 * The class, which is an abstraction over the API mapper, essentially serves as a property store
 * for converting the client's representation of fields to fields in our database. It has methods to
 * retrieve table or field names by key or value in stored properties. Takes a path to a .properties
 * file with a conversion definition where {@code [key]=[table.field]}. You can pass the conversion
 * using Map<{@link String}, {@link String}> at your discretion. Passing values other than the
 * {@code [table.field]} format will raise an exception {@link IllegalArgumentException}
 *
 * @author Artur Perun
 * @version 0.0.1
 */
abstract class AbstractOneQueryMapper implements OneQueryMapper {
  private final Properties properties = new Properties();

  AbstractOneQueryMapper(Path propertiesPath) {
    loadProperties(propertiesPath);
  }

  AbstractOneQueryMapper(Map<String, String> mapper) {
    loadProperties(mapper);
  }

  private void loadProperties(Path propertiesPath) {
    try (FileReader fileReader = new FileReader(propertiesPath.toFile())) {
      properties.load(fileReader);

      checkProperties(properties);
    } catch (IOException e) {
      throw new IllegalArgumentException(e.getMessage());
    }
  }

  private void loadProperties(Map<String, String> mapper) {
    List<String> invalidValues = new ArrayList<>();

    for (String value : mapper.values()) {
      if (value.split("\\.").length != 2) {
        invalidValues.add(value);
      }
    }

    if (!invalidValues.isEmpty()) {
      StringBuilder message = new StringBuilder("Incorrect data format in map: ");

      for (int i = 0; i < invalidValues.size(); ++i) {
        message.append('[').append(invalidValues.get(i)).append(']');

        if (i != invalidValues.size() - 1) {
          message.append(", ");
        }
      }

      message.append(". Should be [table.field]");

      throw new IllegalArgumentException(message.toString());
    }

    properties.putAll(mapper);
  }

  @Override
  public Map<String, String> getMap() {
    Map<String, String> mapFromProperties = new HashMap<>();
    for (String key : properties.stringPropertyNames()) {
      mapFromProperties.put(key, properties.getProperty(key));
    }

    return mapFromProperties;
  }

  @Override
  public String getProperty(String key) {
    String value = properties.getProperty(key);

    if (value == null) {
      throw new NoSuchElementException(
          String.format("Could not find value for key [%s] in mapper", key));
    }

    return value;
  }

  private static void checkProperties(Properties properties) {
    List<String> invalidValues = new ArrayList<>();

    properties.forEach(
        (key, value) -> {
          if (!(value instanceof String stringValue) || stringValue.split("\\.").length != 2) {
            invalidValues.add(value.toString());
          }
        });

    if (!invalidValues.isEmpty()) {
      StringBuilder message = new StringBuilder("Incorrect data format: ");

      for (int i = 0; i < invalidValues.size(); ++i) {
        message.append('[').append(invalidValues.get(i)).append(']');

        if (i != invalidValues.size() - 1) {
          message.append(", ");
        }
      }

      message.append(". Should be [table.field]");

      throw new IllegalArgumentException(message.toString());
    }
  }
}
