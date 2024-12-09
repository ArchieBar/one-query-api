package io.codefine.model;

import java.util.List;

/**
 * A class that represents a pagination result.
 *
 * @author Artur Perun
 * @version 0.0.1
 */
public record PaginationResult<T>(List<T> content, long number, long total) {}
