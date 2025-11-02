package one.query.api.impl;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import one.query.api.OneQueryCollectStep;
import one.query.api.OneQueryCommonTableStep;
import one.query.api.OneQueryFetchStep;
import one.query.api.OneQueryFieldsStep;
import one.query.api.OneQueryFilterStep;
import one.query.api.OneQueryFromStep;
import one.query.api.OneQueryOnStep;
import one.query.api.OneQueryOptionalPaginationStep;
import one.query.api.OneQueryPaginationResultStep;
import one.query.api.OneQueryPaginationStep;
import one.query.api.OneQuerySortStep;
import one.query.api.model.Filter;
import one.query.api.model.Page;
import one.query.api.model.PaginationResult;
import one.query.api.model.Prefix;
import one.query.api.model.Sort;
import org.apache.commons.lang3.NotImplementedException;
import org.jooq.AggregateFunction;
import org.jooq.CommonTableExpression;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.OrderField;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.jooq.Result;
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
import org.jooq.exception.DataAccessException;
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
 * @version 0.0.7
 */
public final class OneQuery<R extends Record> extends AbstractOneQuery<SelectQuery<R>>
    implements OneQueryOnStep<R>,
        OneQueryFromStep<R>,
        OneQueryFilterStep<R>,
        OneQuerySortStep<R>,
        OneQueryOptionalPaginationStep<R>,
        OneQueryPaginationStep<R>,
        OneQueryCollectStep<R>,
        OneQueryFetchStep<R>,
        OneQueryFieldsStep<R>,
        OneQueryCommonTableStep<R>,
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
   * A field that contains the variable names for the CTE if called {@link
   * OneQueryFieldsStep#fields(String...)}
   */
  private String[] fields;

  /* ---------- constructors ---------------------------------------------------------------------------------------- */

  /**
   * Closed constructor that is called from some static methods {@code .query()}. Accepts a {@link
   * SelectQuery} containing the jooq builder's query parameters.
   */
  private OneQuery(final SelectQuery<R> delegate) {
    super(delegate);
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
   * Accepts a common interface for all requests {@link SelectFinalStep}
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
   */
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
  @SafeVarargs
  public final OneQueryFilterStep<R> from(final TableLike<?>... tables) {
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
  public <T> OneQuerySortStep<R> filter(final Filter<T> filter) {
    SelectQuery<R> delegate = getDelegate();
    Condition condition = getCondition(filter);

    if (filter.field() instanceof AggregateFunction) {
      delegate.addHaving(condition);
    } else {
      delegate.addConditions(condition);
    }

    return this;
  }

  @Override
  public OneQuerySortStep<R> filter(final Filter<?>... filters) {
    List.of(filters)
        .forEach(
            filter -> {
              SelectQuery<R> delegate = getDelegate();
              Condition condition = getCondition(filter);

              if (filter.field() instanceof AggregateFunction) {
                delegate.addHaving(condition);
              } else {
                delegate.addConditions(condition);
              }
            });
    return this;
  }

  @Override
  public OneQuerySortStep<R> filter(final Collection<Filter<?>> filters) {
    filters.forEach(
        filter -> {
          SelectQuery<R> delegate = getDelegate();
          Condition condition = getCondition(filter);

          if (filter.field() instanceof AggregateFunction) {
            delegate.addHaving(condition);
          } else {
            delegate.addConditions(condition);
          }
        });
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
    List.of(sorts).forEach(sort -> getDelegate().addOrderBy(getSortField(sort)));
    return this;
  }

  @Override
  public OneQueryOptionalPaginationStep<R> sort(final Collection<Sort> sorts) {
    sorts.forEach(sort -> getDelegate().addOrderBy(getSortField(sort)));
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

  @Override
  public Long getTotal() {
    return total;
  }

  /* ---------- .fields() ------------------------------------------------------------------------------------------- */

  @Override
  public OneQueryCommonTableStep<R> fields(final String... fields) {
    this.fields = fields;
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

  /* ---------- .fetch() -------------------------------------------------------------------------------------------- */

  @Override
  public Result<R> fetch() throws DataAccessException {
    return getDelegate().fetch();
  }

  /* ---------- .toCommonTable() ------------------------------------------------------------------------------------ */

  @Override
  public CommonTableExpression<R> toCommonTable(final String name) {
    return DSL.name(name).fields(fields).as(getDelegate());
  }

  /* ---------- private methods ------------------------------------------------------------------------------------- */

  /** Method that adds {@code limit} to a query based on {@code offset} and {@code size} */
  private void limit(final long offset, final long size) {
    getDelegate().addLimit(offset, size);
  }

  /* ---------- private methods filter ------------------------------------------------------------------------------ */

  /**
   * The method accepts the {@link Filter} collection, the implementation of the method is tied to
   * using mapper methods to get {@link TableField} and creating conditions in a loop. For each
   * individual prefix there is a different algorithm for creating a condition, so for example for
   * the prefix {@link Prefix#BW} the odd element of the array reflects {@code from}, the even -
   * {@code to}, and for the prefix {@link Prefix#EQ} the first element of the array adds the
   * condition {@code AND}, and all subsequent {@code OR}.
   *
   * @see Prefix
   */
  @SuppressWarnings("java:S1854")
  private <T> Condition getCondition(final Filter<T> filter) {
    if (filter.field() == null) {
      throw new IllegalStateException("Table field must not be null");
    }

    if (filter.prefix() == null) {
      throw new IllegalStateException("Prefix must not be null");
    }

    if (filter.values() == null) {
      throw new IllegalStateException("Value must not be null");
    }

    Field<T> field = filter.field();
    Prefix prefix = filter.prefix();
    List<T> value = List.copyOf(filter.values());

    return switch (prefix) {
      case BW -> getBetweenCondition(field, value);
      case EQ -> getEqualsCondition(field, value);
      case NE -> getNotEqualsCondition(field, value);
      case SW -> getStartsWithCondition(field, value);
      case EW -> getEndsWithCondition(field, value);
      case LIKE -> getLikeCondition(field, value);
      case IS_NULL -> getEqualsNullCondition(field);
      case IS_NOT_NULL -> getNotEqualsNullCondition(field);
    };
  }

  /**
   * @see Prefix#BW
   */
  private <T> Condition getBetweenCondition(final Field<T> field, final List<T> conditionValue) {
    if (conditionValue.size() % 2 != 0) {
      throw new IllegalArgumentException(
          String.format(
              "Conditions must be a multiple of 2. Values size: [%s]", conditionValue.size()));
    }
    Condition condition = field.between(conditionValue.getFirst(), conditionValue.get(1));
    for (int i = 2; i < conditionValue.size(); i += 2) {
      condition = condition.or(field.between(conditionValue.get(i), conditionValue.get(i + 1)));
    }
    return condition;
  }

  /**
   * @see Prefix#EQ
   */
  private <T> Condition getEqualsCondition(final Field<T> field, final List<T> conditionValue) {
    Condition condition = field.eq((conditionValue.getFirst()));
    for (int i = 1; i < conditionValue.size(); i++) {
      condition = condition.or(field.eq(conditionValue.get(i)));
    }
    return condition;
  }

  /**
   * @see Prefix#NE
   */
  private <T> Condition getNotEqualsCondition(final Field<T> field, final List<T> conditionValue) {
    Condition condition = field.ne(conditionValue.getFirst());
    for (int i = 1; i < conditionValue.size(); i++) {
      condition = condition.and(field.ne(conditionValue.get(i)));
    }
    return condition;
  }

  /**
   * @see Prefix#SW
   */
  private <T> Condition getStartsWithCondition(final Field<T> field, final List<T> conditionValue) {
    Condition condition = field.startsWith(conditionValue.getFirst());
    for (int i = 1; i < conditionValue.size(); i++) {
      condition = condition.or(field.startsWith(conditionValue.get(i)));
    }
    return condition;
  }

  /**
   * @see Prefix#EW
   */
  private <T> Condition getEndsWithCondition(final Field<T> field, final List<T> conditionValue) {
    Condition condition = field.endsWith(conditionValue.getFirst());
    for (int i = 1; i < conditionValue.size(); i++) {
      condition = condition.or(field.endsWith(conditionValue.get(i)));
    }
    return condition;
  }

  private <T> Condition getLikeCondition(final Field<T> field, final List<T> conditionValue) {
    if (field.getType() != String.class) {
      throw new IllegalArgumentException("Field must be of type String");
    }

    @SuppressWarnings("unchecked")
    Field<String> stringField = (Field<String>) field;

    List<String> patterns =
        conditionValue.stream().filter(Objects::nonNull).map(value -> "%" + value + "%").toList();

    Condition condition = DSL.noCondition();
    for (String pattern : patterns) {
      condition = condition.or(stringField.likeIgnoreCase(pattern));
    }

    return condition;
  }

  private <T> Condition getEqualsNullCondition(final Field<T> field) {
    return field.isNull();
  }

  private <T> Condition getNotEqualsNullCondition(final Field<T> field) {
    return field.isNotNull();
  }

  /* ---------- private methods sort -------------------------------------------------------------------------------- */

  /** Creates a {@link SortField} from a {@link Sort} object */
  private SortField<?> getSortField(final Sort sort) {
    if (sort == null) throw new IllegalArgumentException("Not supported sorted by null sort field");
    return sort.field().sort(sort.sortOrder());
  }
}
