package one.query.api.model;

import org.jooq.Field;
import org.jooq.SortOrder;

/**
 * A class that represents a sort.
 *
 * @author Artur Perun
 * @version 0.0.1
 */
public record Sort(Field<?> field, SortOrder sortOrder) {}
