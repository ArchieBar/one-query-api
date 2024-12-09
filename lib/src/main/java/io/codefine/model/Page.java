package io.codefine.model;

/**
 * A class that represents a page.
 *
 * @author Artur Perun
 * @version 0.0.1
 */
public record Page(long number, long size) {
  public Page {
    if (number < 0) {
      throw new IllegalArgumentException("Page number must be greater than or equal to 0");
    }
    if (size < 1) {
      throw new IllegalArgumentException("Page size must be greater than 0");
    }
  }

  public long offset() {
    return number * size;
  }
}
