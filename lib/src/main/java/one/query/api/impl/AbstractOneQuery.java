package one.query.api.impl;

/**
 * The abstract class serves as a repository for the jooq builder. Created in the {@link OneQuery}
 * constructor when {@code .query()} is called
 *
 * @version 0.0.1
 * @author Artur Perun
 */
abstract class AbstractOneQuery<Q> {
  private final Q delegate;

  AbstractOneQuery(Q delegate) {
    this.delegate = delegate;
  }

  final Q getDelegate() {
    return delegate;
  }
}
