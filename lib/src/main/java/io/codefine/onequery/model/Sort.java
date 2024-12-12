package io.codefine.onequery.model;

/**
 * A class that represents a sort.
 *
 * @author Artur Perun
 * @version 0.0.1
 */
public record Sort(String field, Direction direction) {

  public enum Direction {
    ASC(1, "ascending"),
    DESC(2, "descending");

    Direction(int id, String name) {
      this.id = id;
      this.name = name;
    }

    private final int id;
    private final String name;

    public int getId() {
      return id;
    }

    public String getName() {
      return name;
    }
  }
}
