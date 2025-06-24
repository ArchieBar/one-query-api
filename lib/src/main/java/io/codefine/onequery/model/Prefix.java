package io.codefine.onequery.model;

import io.codefine.onequery.impl.OneQuery;

/** List of available filtering prefixes. */
public enum Prefix {
  /**
   * {@code Equals} represents the equality of two fields, the field that is passed as {@code
   * tableField} must be equal to the value passed. The first element in the array will be treated
   * as {@code AND}, all subsequent values will be treated as {@code OR}
   *
   * <p>Incoming value example: {@code EQ : [1, 2, ...N]}, example heralded value: {@code field = 1
   * or field = 2 or ... field = N}
   *
   * <p><b>Important</b>, the above is relevant to the implementation of getting conditions in
   * {@link OneQuery}
   */
  EQ(1, "equals"),

  /**
   * {@code Not equlas} represents an inequality of two fields, the field that is passed as {@code
   * tableField} must not be equal to the passed value. The first element in the array will be
   * treated as {@code AND}, all subsequent values will be treated as {@code AND}
   *
   * <p>Incoming value example: {@code NE : [1, 2, ...N]}, example of a heralded value: {@code field
   * <> 1 and field <> 2 and ... field <> N}
   *
   * <p><b>Important</b>, the above is relevant to the implementation of getting conditions in
   * {@link OneQuery}
   */
  NE(2, "not equals"),

  /**
   * {@code StartsWith} represents the equality of two fields, the field that is passed as {@code
   * tableField} must start with the passed value. The first element in the array will be treated as
   * {@code AND}, all subsequent values will be treated as {@code OR}
   *
   * <p>Incoming value example: {@code SW : [1, 2, ...N]}, example of a heralded value: {@code field
   * like 1% or field like 2% or ... field like N%}
   *
   * <p><b>Important</b>, the above is relevant to the implementation of getting conditions in
   * {@link OneQuery}
   */
  SW(3, "starts with"),

  /**
   * {@code EndsWith} represents the equality of two fields, the field that is passed as {@code
   * tableField} must end with the passed value. The first element in the array will be treated as
   * {@code AND}, all subsequent values will be treated as {@code OR}
   *
   * <p>Incoming value example: {@code EW : [1, 2, ...N]}, example of a heralded value: {@code field
   * like %1 or field like %2 or ... field like %N}
   *
   * <p><b>Important</b>, the above is relevant to the implementation of getting conditions in
   * {@link OneQuery}
   */
  EW(4, "ends with"),

  /**
   * {@code Between} represents the equality of two fields, the field passed as {@code tableField}
   * must be between the two values passed. The first pair of values will be considered where an
   * even element has a {@code from} relationship and an odd element has a {@code to} relationship.
   * The first pair will be added as {@code AND} subsequent pairs will be added as {@code OR}
   *
   * <p>Incoming value example: {@code BW : [1, 2, 3, 4...N1, N2]}, example of a heralded value:
   * {@code field between 1 and 2 or field between 3 and 4 or ... field between N1 and N2}
   *
   * <p><b>Important</b>, the above is relevant to the implementation of getting conditions in
   * {@link OneQuery}
   */
  BW(5, "between"),

  /**
   * {@code Equals} represents the equality of two fields, the field that is passed as {@code
   * tableField} must be equal to the null value passed.
   *
   * <p><b>Important</b>, the above is relevant to the implementation of getting conditions in
   * {@link OneQuery}
   */
  IS_NULL(6, "is null"),

  /**
   * {@code Equals} represents the equality of two fields, the field that is passed as {@code
   * tableField} must be equal to the not null value passed.
   *
   * <p><b>Important</b>, the above is relevant to the implementation of getting conditions in
   * {@link OneQuery}
   */
  IS_NOT_NULL(7, "is not null");

  Prefix(int id, String name) {
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
