package io.codefine.onequery;

import org.jooq.Record;

/**
 * An intermediate interface that represents a common step for some interfaces. Serves as a `node`
 * to connect the required methods in certain interfaces
 *
 * @author Artur Perun
 * @version 0.0.1
 */
public interface OneQueryOptionalPaginationStep<R extends Record>
    extends OneQueryPaginationStep<R>, OneQueryCollectStep<R>, OneQueryFetchStep<R> {}
