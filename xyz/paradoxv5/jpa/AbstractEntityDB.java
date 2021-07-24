package xyz.paradoxv5.jpa;

import xyz.paradoxv5.CRUD;
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
    This superclass uses {@link EntityManagerWrapper}s (supplied by {@link #entityManagerSupplier} which is
    initialized from {@link #AbstractEntityDB(Class, Supplier) the constructor}) to manage transactions
  @version
    1.01
*/
public abstract class AbstractEntityDB<E extends AbstractEntity<K>, K extends Serializable> implements CRUD<E, K> {
  /** The persistent constant holding {@code <E>}’s class, as compiling discards type variables */
  protected final Class<E> e;
  /** The supplier that provides the {@code EntityManagerWrapper}s */
  protected final Supplier<EntityManagerWrapper> entityManagerSupplier;
  /** {@link AbstractEntityDB} constructor
    @param klass {@link #e}
    @param entityManagerSupplier {@link #entityManagerSupplier}
  */
  public AbstractEntityDB(Class<E> klass, Supplier<EntityManagerWrapper> entityManagerSupplier) {
    this.e = klass;
    this.entityManagerSupplier = entityManagerSupplier;
  }
  
  @Override public synchronized E get(K primaryKey) {
    try(EntityManagerWrapper entityManager = entityManagerSupplier.get()) {
      return entityManager.get().find(e, primaryKey);
    }
  }
  
  /** The QL string for {@link #getAll()} */
  public abstract String getQlforGetAll();
  /** @apiNote
    This implementation retrieves using the string provided by {@link #getQlforGetAll()} and
    {@linkplain javax.persistence.TypedQuery#getResultStream() stream}
    {@linkplain Stream#collect(Collector) to a}
    {@linkplain Collectors#toSet() set}.
  */
  @Override public java.util.Set<E> getAll() {
    try(EntityManagerWrapper entityManager = entityManagerSupplier.get()) {
      return entityManager.get().createQuery(getQlforGetAll(), e).getResultStream().collect(Collectors.toSet());
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
    try(EntityManagerWrapper entityManager = entityManagerSupplier.get()) { try {
      add0(entityManager.get(), entity);
    } catch(Exception e) {
      entityManager.rollback();
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
    try(EntityManagerWrapper entityManager = entityManagerSupplier.get()) { try {
      update0(entityManager.get(), entity);
    } catch(Exception e) {
      entityManager.rollback();
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
    try(EntityManagerWrapper entityManager = entityManagerSupplier.get()) { try {
      remove0(entityManager.get(), entity);
    } catch(Exception e) {
      entityManager.rollback();
      throw e;
    } }
  }
}