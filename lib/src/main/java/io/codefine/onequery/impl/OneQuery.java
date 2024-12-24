package io.codefine.onequery.impl;

import io.codefine.onequery.OneQueryCollectStep;
import io.codefine.onequery.OneQueryFilterStep;
import io.codefine.onequery.OneQueryFromStep;
import io.codefine.onequery.OneQueryOnStep;
import io.codefine.onequery.OneQueryOptionalPaginationStep;
import io.codefine.onequery.OneQueryPaginationResultStep;
import io.codefine.onequery.OneQueryPaginationStep;
import io.codefine.onequery.OneQuerySortStep;
import io.codefine.onequery.configuration.OneQueryConfiguration;
import io.codefine.onequery.mapper.OneQueryMapper;
import io.codefine.onequery.model.Filter;
import io.codefine.onequery.model.Page;
import io.codefine.onequery.model.PaginationResult;
import io.codefine.onequery.model.Sort;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import org.apache.commons.lang3.NotImplementedException;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.OrderField;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.jooq.SQLDialect;
import org.jooq.SelectConnectByStep;
import org.jooq.SelectFinalStep;
import org.jooq.SelectFromStep;
import org.jooq.SelectIntoStep;
import org.jooq.SelectOnStep;
import org.jooq.SelectOptionalOnStep;
import org.jooq.SelectQuery;
import org.jooq.SortField;
import org.jooq.TableField;
import org.jooq.TableLike;
import org.jooq.impl.DSL;

/**
 * A class that provides configuration on top of JOOQ to help filter, sort and paginate, the main
 * purpose of the API is pagination as the API will reassemble your query and count the total number
 * of items, but it also provides handy conversion methods for sorting and filtering that you can
 * use without pagination.
 *
 * <p>The implementation of the class is strictly tied to the resulting jooq interface type, if an
 * invalid interface is passed an auxiliary interface will be returned to help `refine` the query to
 * a valid one and call the {@code .filter()}, {@code .sort()} and {@code .paginate()} methods.
 *
 * <p>There are some interfaces that are not supported, for example: {@link SelectFromStep} which
 * will be returned when {@code .select().into()} is called, calling {@code .query()} with such
 * interfaces will cause {@link NotImplementedException} (possible fix in future versions), there is
 * an example jooq builder for each of the interfaces.
 *
 * @author Artur Perun, Artyom Sushchenko
 * @version 0.0.1
 */
public final class OneQuery<R extends Record> extends AbstractOneQuery<SelectQuery<R>>
    implements OneQueryOnStep<R>,
        OneQueryFromStep<R>,
        OneQueryFilterStep<R>,
        OneQuerySortStep<R>,
        OneQueryOptionalPaginationStep<R>,
        OneQueryPaginationStep<R>,
        OneQueryCollectStep<R>,
        OneQueryPaginationResultStep<R> {

  /* ---------- variables ------------------------------------------------------------------------------------------- */

  /**
   * The method that stores the intermediate interface is {@link SelectOnStep}, the variable is
   * necessary because the interface does not have the {@code .getQuery()} method
   */
  private SelectOnStep<R> onStep;

  /**
   * Variable that stores the object {@link Page}. Required for the correct creation of the {@link
   * PaginationResult} object.
   */
  private Page currentPage;

  /**
   * A variable that stores the total number of elements in after filtering. Required for the
   * correct return of the {@link PaginationResult} object
   */
  private Long total;

  /**
   * Mapper is used to get {@link TableField} objects in some methods, be automatically from {@link
   * SQLDialect} when calling {@code .query()}
   */
  private OneQueryMapper mapper;

  /* ---------- constructors ---------------------------------------------------------------------------------------- */

  /**
   * Closed constructor that is called from some static methods {@code .query()}. Accepts a {@link
   * SelectQuery} containing the jooq builder's query parameters. Gets {@link SQLDialect} from the
   * query configuration to get the desired mapper.
   */
  private OneQuery(final SelectQuery<R> delegate) {
    super(delegate);
    // todo нужно проверить на NPE
    SQLDialect dialect = Objects.requireNonNull(delegate.configuration()).dialect();
    mapper = loadMapperByDialect(dialect);
  }

  /**
   * A helper constructor that accepts {@link SelectOnStep} that does not have a {@code .getQuery()}
   * method
   */
  private OneQuery(final SelectOnStep<R> onStep) {
    super(null);
    this.onStep = onStep;
  }

  /* ---------- .query() -------------------------------------------------------------------------------------------- */

  /**
   * Accepts a common interface for all requests {@link SelectFinalStep}, this method is not stable
   * and needs improvement, unforeseen errors are possible.
   *
   * <p>Example jooq builder:
   *
   * <pre><code>
   *     create.select()
   *         .from()
   *         .join()
   *         .on()
   *         .where()
   *         .and()
   *         .groupBy()
   *         .having()
   *         .orderBy()
   *         .limit()
   *         .offset()
   *         .forUpdate()
   *         .of()
   *         .noWait();
   * </code></pre>
   *
   * <p><b>Not recommended for use</b>.
   *
   * @deprecated since version 0.0.1
   */
  @SuppressWarnings({"java:S1133", "java:S1123"})
  @Deprecated(since = "0.0.1", forRemoval = true)
  public static <T extends Record> OneQueryFilterStep<T> query(final SelectFinalStep<T> query) {
    return new OneQuery<>(query.getQuery());
  }

  /**
   * Accepts {@link SelectConnectByStep} and returns {@link OneQueryPaginationStep}
   *
   * <p>Example jooq builder:
   *
   * <pre><code>
   *     create.select()
   *         .from()
   *         .join({@link TableLike})
   *         .on()
   *         .where()
   *         .and()
   * </code></pre>
   */
  public static <T extends Record> OneQueryFilterStep<T> query(final SelectConnectByStep<T> query) {
    return new OneQuery<>(query.getQuery());
  }

  /**
   * Accepts {@link SelectOptionalOnStep} and returns {@link OneQueryPaginationStep}
   *
   * <p>Example jooq builder:
   *
   * <pre><code>
   *     create.select()
   *         .from()
   *         .join({@link TableLike}, {@link org.jooq.JoinType})
   * </code></pre>
   *
   * @deprecated since version 0.0.1. Currently, returns {@link NotImplementedException}.
   */
  @SuppressWarnings({"java:S1133", "java:S1123", "unused"})
  @Deprecated(since = "0.0.1")
  public static <T extends Record> OneQueryFilterStep<T> query(
      final SelectOptionalOnStep<T> query) {
    throw new NotImplementedException();
  }

  /**
   * Accepts {@link SelectOnStep} and returns {@link OneQueryOnStep} - Helper interface to help
   * create a valid request
   *
   * <p>Example jooq builder:
   *
   * <pre><code>
   *     create.select()
   *         .from()
   *         .join({@link TableLike})
   * </code></pre>
   */
  public static <T extends Record> OneQueryOnStep<T> query(final SelectOnStep<T> query) {
    return new OneQuery<>(query);
  }

  /**
   * Accepts {@link SelectFromStep} and returns {@link OneQueryFromStep} - Helper interface to help
   * create a valid request
   *
   * <p>Example jooq builder:
   *
   * <pre><code>
   *     create.select()
   *         .into()
   * </code></pre>
   *
   * @deprecated since version 0.0.1. Currently, returns {@link NotImplementedException}.
   */
  @SuppressWarnings({"java:S1133", "java:S1123", "unused"})
  @Deprecated(since = "0.0.1")
  public static <T extends Record> OneQueryFromStep<T> query(final SelectFromStep<T> query) {
    throw new NotImplementedException();
  }

  /**
   * Accepts {@link SelectIntoStep} and returns {@link OneQueryFromStep} - Helper interface to help
   * create a valid request
   *
   * <p>Example jooq builder:
   *
   * <pre><code>
   *     create.select()
   * </code></pre>
   */
  public static <T extends Record> OneQueryFromStep<T> query(final SelectIntoStep<T> query) {
    return new OneQuery<>(query.getQuery());
  }

  /* ---------- .on() ----------------------------------------------------------------------------------------------- */

  @Override
  public OneQueryFilterStep<R> on(final Condition condition) {
    return new OneQuery<>(onStep.on(condition).getQuery());
  }

  @Override
  public OneQueryFilterStep<R> on(final Condition... conditions) {
    return new OneQuery<>(onStep.on(conditions).getQuery());
  }

  /* ---------- .from() --------------------------------------------------------------------------------------------- */

  @Override
  public OneQueryFilterStep<R> from(final TableLike<?> table) {
    getDelegate().addFrom(table);
    return this;
  }

  @Override
  public OneQueryFilterStep<R> from(final TableLike<?>... tables) {
    getDelegate().addFrom(tables);
    return this;
  }

  @Override
  public OneQueryFilterStep<R> from(final Collection<? extends TableLike<?>> tables) {
    getDelegate().addFrom(tables);
    return this;
  }

  /* ---------- .filter() & .filterBy() ----------------------------------------------------------------------------- */

  @Override
  public OneQuerySortStep<R> filter(final Filter filter) {
    getDelegate().addConditions(getConditions(List.of(filter)));
    return this;
  }

  @Override
  public OneQuerySortStep<R> filter(final Filter... filters) {
    getDelegate().addConditions(getConditions(List.of(filters)));
    return this;
  }

  @Override
  public OneQuerySortStep<R> filter(final Collection<Filter> filters) {
    getDelegate().addConditions(getConditions(filters));
    return this;
  }

  @Override
  public OneQuerySortStep<R> filterBy(final Condition condition) {
    getDelegate().addConditions(condition);
    return this;
  }

  @Override
  public OneQuerySortStep<R> filterBy(final Condition... conditions) {
    getDelegate().addConditions(conditions);
    return this;
  }

  @Override
  public OneQuerySortStep<R> filterBy(final Collection<? extends Condition> conditions) {
    getDelegate().addConditions(conditions);
    return this;
  }

  /* ---------- .sort() & .sortBy() --------------------------------------------------------------------------------- */

  @Override
  public OneQueryOptionalPaginationStep<R> sort(final Sort sort) {
    getDelegate().addOrderBy(getSortField(sort));
    return this;
  }

  @Override
  public OneQueryOptionalPaginationStep<R> sort(final Sort... sorts) {
    getDelegate().addOrderBy(getSortFields(List.of(sorts)));
    return this;
  }

  @Override
  public OneQueryOptionalPaginationStep<R> sort(final Collection<Sort> sorts) {
    getDelegate().addOrderBy(getSortFields(sorts));
    return this;
  }

  @Override
  public OneQueryOptionalPaginationStep<R> sortBy(final OrderField<?> field) {
    getDelegate().addOrderBy(field);
    return this;
  }

  @Override
  public OneQueryOptionalPaginationStep<R> sortBy(final OrderField<?>... fields) {
    getDelegate().addOrderBy(fields);
    return this;
  }

  @Override
  public OneQueryOptionalPaginationStep<R> sortBy(
      final Collection<? extends OrderField<?>> fields) {
    getDelegate().addOrderBy(fields);
    return this;
  }

  /* ---------- .paginate() ----------------------------------------------------------------------------------------- */

  @Override
  public OneQueryPaginationResultStep<R> paginate(final Page page) {
    this.currentPage = page;
    DSLContext ctx = DSL.using(getDelegate().configuration());
    total = ctx.selectCount().from(getDelegate()).fetchOneInto(Long.class);

    limit(currentPage.offset(), currentPage.size());
    return this;
  }

  @Override
  public OneQueryPaginationResultStep<R> paginate(final int page, final int size) {
    this.currentPage = new Page(page, size);
    DSLContext ctx = DSL.using(getDelegate().configuration());
    total = ctx.selectCount().from(getDelegate()).fetchOneInto(Long.class);

    limit(currentPage.offset(), currentPage.size());

    return this;
  }

  /* ---------- .toPaginationResult() ------------------------------------------------------------------------------- */

  @Override
  public <C> PaginationResult<C> toPaginationResult(final Class<C> clazz) {
    List<C> result = getDelegate().fetchInto(clazz);
    return new PaginationResult<>(result, currentPage.number(), total);
  }

  @Override
  public <C> PaginationResult<C> toPaginationResult(final RecordMapper<R, C> mapper) {
    List<C> result = getDelegate().fetch(mapper);
    return new PaginationResult<>(result, currentPage.number(), total);
  }

  /* ---------- .toList() ------------------------------------------------------------------------------------------- */

  @Override
  public <C> List<C> toList(final Class<C> clazz) {
    return getDelegate().fetchInto(clazz);
  }

  @Override
  public <C> List<C> toList(final RecordMapper<R, C> mapper) {
    return getDelegate().fetch(mapper);
  }

  /* ---------- private methods ------------------------------------------------------------------------------------- */

  /** Method that adds {@code limit} to a query based on {@code offset} and {@code size} */
  private void limit(final long offset, final long size) {
    getDelegate().addLimit(offset, size);
  }

  /** Method for getting a mapper on a passed dialect */
  private OneQueryMapper loadMapperByDialect(final SQLDialect dialect) {
    return switch (dialect) {
      case POSTGRES -> OneQueryConfiguration.getMapper(SQLDialect.POSTGRES);
      case DEFAULT ->
          throw new IllegalArgumentException(
              "DEFAULT dialect is not supported. "
                  + "You must specify the dialect to be used in the JOOQ configuration.");
      default -> throw new NotImplementedException("Unsupported dialect: " + dialect);
    };
  }

  /* ---------- private methods filter ------------------------------------------------------------------------------ */

  /**
   * The method accepts the {@link Filter} collection, the implementation of the method is tied to
   * using mapper methods to get {@link TableField} and creating conditions in a loop. For each
   * individual prefix there is a different algorithm for creating a condition, so for example for
   * the prefix {@link Filter.Prefix#BW} the odd element of the array reflects {@code from}, the
   * even - {@code to}, and for the prefix {@link Filter.Prefix#EQ} the first element of the array
   * adds the condition {@code AND}, and all subsequent {@code OR}.
   *
   * @see Filter.Prefix
   * @see Filter.Prefix#EQ
   * @see Filter.Prefix#NE
   * @see Filter.Prefix#SW
   * @see Filter.Prefix#EW
   * @see Filter.Prefix#BD
   * @see Filter.Prefix#BW
   */
  private <T> List<Condition> getConditions(final Collection<Filter> filters) {
    final List<Condition> conditions = new ArrayList<>();

    for (Filter filter : filters) {
      if (filter.tableField() == null) {
        throw new IllegalStateException("Table field must not be null");
      }

      TableField<R, T> tableFiled = mapper.getTableField(filter.tableField());
      Filter.Prefix prefix = filter.prefix();
      String[] value = filter.values();

      if (prefix == null) {
        throw new IllegalStateException("Prefix must not be null");
      }

      if (value == null) {
        throw new IllegalStateException("Value must not be null");
      }

      switch (prefix) {
        case BD -> conditions.add(getBetweenDateCondition(tableFiled, value));
        case BW -> conditions.add(getBetweenCondition(tableFiled, value));
        case EQ -> conditions.add(getEqualsCondition(tableFiled, value));
        case NE -> conditions.add(getNotEqualsCondition(tableFiled, value));
        case SW -> conditions.add(getStartsWithCondition(tableFiled, value));
        case EW -> conditions.add(getEndsWithCondition(tableFiled, value));
      }
    }
    return conditions;
  }

  /**
   * @see Filter.Prefix#BD
   */
  // todo Возможно проблема с форматом даты
  @SuppressWarnings("unchecked")
  private <T> Condition getBetweenDateCondition(
      final TableField<R, T> tableFiled, final String[] conditionValue) {
    if (conditionValue.length % 2 != 0) {
      throw new IllegalArgumentException(
          String.format(
              "Conditions must be a multiple of 2. Values size: [%s]", conditionValue.length));
    }
    Condition condition =
        tableFiled.between(
            (T) OffsetDateTime.parse(conditionValue[0]),
            (T) OffsetDateTime.parse(conditionValue[1]));
    for (int i = 2; i < conditionValue.length; i += 2) {
      condition =
          condition.or(
              tableFiled.between(
                  (T) OffsetDateTime.parse(conditionValue[i]),
                  (T) OffsetDateTime.parse(conditionValue[i + 1])));
    }
    return condition;
  }

  /**
   * @see Filter.Prefix#BW
   */
  @SuppressWarnings("unchecked")
  private <T> Condition getBetweenCondition(
      final TableField<R, T> tableFiled, final String[] conditionValue) {
    if (conditionValue.length % 2 != 0) {
      throw new IllegalArgumentException(
          String.format(
              "Conditions must be a multiple of 2. Values size: [%s]", conditionValue.length));
    }
    Condition condition = tableFiled.between((T) conditionValue[0], (T) conditionValue[1]);
    for (int i = 2; i < conditionValue.length; i += 2) {
      condition =
          condition.or(tableFiled.between((T) conditionValue[i], (T) conditionValue[i + 1]));
    }
    return condition;
  }

  /**
   * @see Filter.Prefix#EQ
   */
  @SuppressWarnings("unchecked")
  private <T> Condition getEqualsCondition(
      final TableField<R, T> tableFiled, final String[] conditionValue) {
    Condition condition = tableFiled.eq((T) conditionValue[0]);
    for (int i = 1; i < conditionValue.length; i++) {
      condition = condition.or(tableFiled.eq((T) conditionValue[i]));
    }
    return condition;
  }

  /**
   * @see Filter.Prefix#NE
   */
  @SuppressWarnings("unchecked")
  private <T> Condition getNotEqualsCondition(
      final TableField<R, T> tableFiled, final String[] conditionValue) {
    Condition condition = tableFiled.ne((T) conditionValue[0]);
    for (int i = 1; i < conditionValue.length; i++) {
      condition = condition.or(tableFiled.ne((T) conditionValue[i]));
    }
    return condition;
  }

  /**
   * @see Filter.Prefix#SW
   */
  @SuppressWarnings("unchecked")
  private <T> Condition getStartsWithCondition(
      final TableField<R, T> tableFiled, final String[] conditionValue) {
    Condition condition = tableFiled.startsWith((T) conditionValue[0]);
    for (int i = 1; i < conditionValue.length; i++) {
      condition = condition.or(tableFiled.startsWith((T) conditionValue[i]));
    }
    return condition;
  }

  /**
   * @see Filter.Prefix#EW
   */
  @SuppressWarnings("unchecked")
  private <T> Condition getEndsWithCondition(
      final TableField<R, T> tableFiled, final String[] conditionValue) {
    Condition condition = tableFiled.endsWith((T) conditionValue[0]);
    for (int i = 1; i < conditionValue.length; i++) {
      condition = condition.or(tableFiled.endsWith((T) conditionValue[i]));
    }
    return condition;
  }

  /* ---------- private methods sort -------------------------------------------------------------------------------- */

  /**
   * Method to get the sort field, gets {@link TableField} using the mapper implementation and
   * transforms it depending on the sort direction from {@link Sort}
   */
  private <T> Collection<SortField<T>> getSortFields(final Collection<Sort> sorting) {
    Collection<SortField<T>> querySortFields = new ArrayList<>();

    if (sorting == null) {
      return querySortFields;
    }

    for (Sort sort : sorting) {
      SortField<T> querySortField = getSortField(sort);
      querySortFields.add(querySortField);
    }

    return querySortFields;
  }

  private <T> SortField<T> getSortField(final Sort sort) {
    String sortFieldName = sort.field();
    Sort.Direction sortDirection = sort.direction();
    TableField<R, T> tableField = mapper.getTableField(sortFieldName);
    return Sort.Direction.ASC.equals(sortDirection) ? tableField.asc() : tableField.desc();
  }
}
