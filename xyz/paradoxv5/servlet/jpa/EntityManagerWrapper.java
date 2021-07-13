package xyz.paradoxv5.servlet.jpa;

import java.util.function.Supplier;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

/**
  
*/
public class EntityManagerWrapper implements Supplier<EntityManager>, AutoCloseable {
  protected final EntityManager entityManager;
  
  private EntityTransaction transaction;
  public boolean isTransactioned() { return transaction != null; }
  
  public EntityManagerWrapper(EntityManager entityManager) {
    this.entityManager = entityManager;
  }
  
  @Override public EntityManager get() {
    return entityManager;
  }
  
  public void beginTransaction() {
    if(transaction != null) return;
    transaction = get().getTransaction();
    transaction.begin();
  }
  public void commitTransaction() {
    if(transaction == null) return;
    try { transaction.commit(); }
    catch(Exception e) {
      rollbackTransaction();
      throw e;
    }
    transaction = null;
  }
  public void rollbackTransaction() {
    if(transaction == null) return;
    transaction.rollback();
    transaction = null;
  }
  
  @Override public void close() {
    commitTransaction();
    entityManager.close();
  }
}