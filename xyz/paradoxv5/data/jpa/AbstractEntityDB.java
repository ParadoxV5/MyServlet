package xyz.paradoxv5.data.jpa;

import xyz.paradoxv5.data.CRUD;
import java.io.Serializable;
import java.util.function.Supplier;
import javax.persistence.EntityManager;
import java.util.stream.*;

/** The {@code abstract} superclass of a database class (a {@link CRUD}) for an {@link AbstractEntity} type
  @param <E>
    The type of entity elements the class CRUDs
  @param <K>
    The type of the entity’ primary key (“ID”), for {@link #get(Serializable)}
  @apiNote
    This superclass uses {@link EntityManagerWrapper}s (supplied by {@link #emWrapperSupplier} which is
    initialized from {@link #AbstractEntityDB(Class, String, Supplier) the constructor}) to manage transactions
  @version
    1.11
*/
public abstract class AbstractEntityDB<E extends AbstractEntity<K>, K extends Serializable> implements CRUD<E, K> {
  /** The persistent constant holding {@code <E>}’s class, as compiling discards type variables */
  protected final Class<E> e;
  /** The QL string for {@link #getAll()} */
  protected final String ql_getAll;
  /** The supplier that provides the {@code EntityManagerWrapper}s */
  protected final Supplier<EntityManagerWrapper> emWrapperSupplier;
  /** {@link AbstractEntityDB} constructor
    @param e {@link #e}
    @param ql_getAll {@link #ql_getAll}
    @param emWrapperSupplier {@link #emWrapperSupplier}
  */
  public AbstractEntityDB(Class<E> e, String ql_getAll, Supplier<EntityManagerWrapper> emWrapperSupplier) {
    this.e = e;
    this.ql_getAll = ql_getAll;
    this.emWrapperSupplier = emWrapperSupplier;
  }
  
  @Override public synchronized E get(K primaryKey) {
    try(EntityManagerWrapper emWrapper = emWrapperSupplier.get()) {
      return emWrapper.get().find(e, primaryKey);
    }
  }
  
  /** @apiNote
    This implementation retrieves using the string provided by {@link #ql_getAll} and
    {@linkplain javax.persistence.TypedQuery#getResultStream() stream}
    {@linkplain Stream#collect(Collector) to a}
    {@linkplain Collectors#toSet() set}.
  */
  @Override public java.util.Set<E> getAll() {
    try(EntityManagerWrapper emWrapper = emWrapperSupplier.get()) {
      return emWrapper.get().createQuery(ql_getAll, e).getResultStream().collect(Collectors.toSet());
    }
  }
  
  /** Add the {@code entity} to the {@code entityManager}
    @param entityManager the entity manager
    @param entity the entity
    @throws Exception if an exception {@code throw}s
    @see #add(AbstractEntity)
  */
  protected abstract void add0(EntityManager entityManager, E entity) throws Exception;
  /** @apiNote
    This preprogramming manages the {@code EntityManagerWrapper}; implementations provide
    {@link #add0(EntityManager, AbstractEntity)} which does the processing part.
  */
  @Override public synchronized void add(E entity) throws Exception {
    try(EntityManagerWrapper emWrapper = emWrapperSupplier.get()) { try {
      add0(emWrapper.get(), entity);
    } catch(Exception e) {
      emWrapper.rollback();
      throw e;
    } }
  }
  
  /** Update the {@code entity} in the {@code entityManager}
    @param entityManager the entity manager
    @param entity the entity
    @throws Exception if an exception {@code throw}s
    @see #update(AbstractEntity)
  */
  protected abstract void update0(EntityManager entityManager, E entity) throws Exception;
  /** @apiNote
    This preprogramming manages the {@code EntityManagerWrapper}; implementations provide
    {@link #update0(EntityManager, AbstractEntity)} which does the processing part.
  */
  @Override public synchronized void update(E entity) throws Exception {
    try(EntityManagerWrapper emWrapper = emWrapperSupplier.get()) { try {
      update0(emWrapper.get(), entity);
    } catch(Exception e) {
      emWrapper.rollback();
      throw e;
    } }
  }
  
  /** Remove the {@code entity} from the {@code entityManager}
    @param entityManager the entity manager
    @param entity the entity
    @throws Exception if an exception {@code throw}s
    @see #remove(AbstractEntity)
  */
  protected abstract void remove0(EntityManager entityManager, E entity) throws Exception;
  /** @apiNote
    This preprogramming manages the {@code EntityManagerWrapper}; implementations provide
    {@link #remove0(EntityManager, AbstractEntity)} which does the processing part.
  */
  @Override public synchronized void remove(E entity) throws Exception {
    try(EntityManagerWrapper emWrapper = emWrapperSupplier.get()) { try {
      EntityManager entityManager = emWrapper.get();
      remove0(entityManager, entityManager.merge(entity));
    } catch(Exception e) {
      emWrapper.rollback();
      throw e;
    } }
  }
}