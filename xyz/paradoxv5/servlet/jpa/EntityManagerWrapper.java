package xyz.paradoxv5.servlet.jpa;
import javax.persistence.EntityManager;

/**
  
*/
public class EntityManagerWrapper implements java.util.function.Supplier<EntityManager>, AutoCloseable {
  protected final EntityManager entityManager;
  
  public EntityManagerWrapper(EntityManager entityManager) {
    this.entityManager = entityManager;
    entityManager.getTransaction().begin();
  }
  
  @Override public EntityManager get() { return entityManager; }
  
  public void commit() {
    try { entityManager.getTransaction().commit(); }
    catch(Exception e) {
      rollback();
      throw e;
    }
  }
  public void rollback() {
    entityManager.getTransaction().rollback();
  }
  
  @Override public void close() {
    commit();
    entityManager.close();
  }
}