package xyz.paradoxv5.servlet.jpa;
import java.util.function.Supplier;
import javax.persistence.EntityManager;
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
  
  @version 1.1
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
    
    @param entityManager
      {@link #entityManager}
    @see EntityManagerWrapper
    @apiNote
      Uses {@link #begin0()} to begin
  */
  public EntityManagerWrapper(EntityManager entityManager) {
    this.entityManager = entityManager;
    if(!entityManager.getTransaction().isActive()) begin0();
  }
  /**
    {@linkplain EntityTransaction#begin() Begin} the current
    {@linkplain EntityManager#getTransaction() transaction}
    
    @see #EntityManagerWrapper(EntityManager)
    @see #commit0()
    @see #rollback0()
  */
  protected void begin0() {
    entityManager.getTransaction().begin();
  }
  
  /**
    {@linkplain EntityTransaction#commit() Commit} the current
    {@linkplain EntityManager#getTransaction() transaction},
    {@link #rollback0()} if {@link RuntimeException} {@code throw}ed
    
    @see #commit()
    @apiNote
      Uses {@link #rollback0()} to rollback
  */
  protected void commit0() {
    EntityTransaction transaction = entityManager.getTransaction();
    if(transaction.isActive())
      try { entityManager.getTransaction().commit(); }
      catch(RuntimeException e) {
        rollback0();
        throw e;
      }
  }
  /**
    {@linkplain EntityTransaction#commit() Commit} the current
    {@linkplain EntityManager#getTransaction() transaction},
    {@link EntityTransaction#rollback() rollback} if {@link RuntimeException}
    {@code throw}ed, and {@linkplain EntityTransaction#begin() begin} a new one
    
    @see #entityManager
    @apiNote
      Uses {@link #commit0()} to commit, then {@link #begin0()} to begin
  */
  public void commit() {
    try { commit0(); }
    finally { begin0(); }
  }
  
  /**
    {@linkplain EntityTransaction#rollback() Rollback} the current
    {@linkplain EntityManager#getTransaction() transaction}
    
    @see #rollback()
  */
  protected void rollback0() {
    EntityTransaction transaction = entityManager.getTransaction();
    if(transaction.isActive())
      entityManager.getTransaction().rollback();
  }
  /**
    {@linkplain EntityTransaction#rollback() Rollback} the current
    {@linkplain EntityManager#getTransaction() transaction} and
    {@linkplain EntityTransaction#begin() begin} a new one
    
    @see #entityManager
    @apiNote
      Uses {@link #rollback0()} to commit, then {@link #begin0()} to begin
  */
  public void rollback() {
    try { rollback0(); }
    finally { begin0(); }
  }
  
  /**
    {@linkplain EntityTransaction#commit() Commit} the current
    {@linkplain EntityManager#getTransaction() transaction} and then
    {@linkplain EntityManager#close() close} the {@link #entityManager}
    
    @apiNote
      Uses {@link #commit0()} to commit
  */
  @Override public void close() {
    try { commit0(); }
    finally { entityManager.close(); }
  }
}