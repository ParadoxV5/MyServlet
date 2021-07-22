package xyz.paradoxv5.servlet.jpa;
import java.util.function.Supplier;
import javax.persistence.EntityManager;
// Javadoc imports not used by Java code
import javax.persistence.EntityTransaction;

/**
  A wrapper class over {@link EntityManager} that automatically
    {@linkplain EntityTransaction#begin() begins} a
    {@linkplain EntityManager#getTransaction() transaction} in
    {@linkplain #EntityManagerWrapper(EntityManager) constructor} and
    {@linkplain EntityTransaction#commit() commits} it upon {@link #close()}.
  <p>
  This is a {@link Supplier} of {@code EntityManager} and uses the contracted
    {@link #get()} as the getter for {@link #entityManager}.
  
  @version 1.001
*/
public class EntityManagerWrapper implements Supplier<EntityManager>, AutoCloseable {
  /**
    The wrapped {@code EntityManager};
    the {@code public} getter is {@link #get()}
    
    @see EntityManagerWrapper
    @see #close()
  */
  protected final EntityManager entityManager;
  /** @return {@link #entityManager} */
  @Override public final EntityManager get() { return entityManager; }
  
  /**
    Takes in the {@code entityManager} to wrap and starts its
    {@linkplain EntityManager#getTransaction() transaction}
    
    @param entityManager {@link #entityManager}
    @see EntityManagerWrapper
  */
  public EntityManagerWrapper(EntityManager entityManager) {
    this.entityManager = entityManager;
    entityManager.getTransaction().begin();
  }
  
  
  /**
    {@linkplain EntityTransaction#commit() Commit} the current
    {@linkplain EntityManager#getTransaction() transaction}
    
    @see #entityManager
  */
  public void commit() {
    try { entityManager.getTransaction().commit(); }
    catch(Exception e) {
      rollback();
      throw e;
    }
  }
  /**
    {@linkplain EntityTransaction#rollback() Rollback} the current
    {@linkplain EntityManager#getTransaction() transaction}
    
    @see #entityManager
  */
  public void rollback() {
    entityManager.getTransaction().rollback();
  }
  
  /**
    {@link #commit()} and then
    {@linkplain EntityManager#close() close} the {@link #entityManager}
  */
  @Override public void close() {
    commit();
    entityManager.close();
  }
}