package xyz.paradoxv5.servlet.jpa;

import xyz.paradoxv5.servlet.CRUD;

import java.util.Set;
import javax.persistence.EntityManager;
import java.io.Serializable;

/**
  
*/
public abstract class AbstractEntityDB<E extends AbstractEntity<K>, K extends Serializable> implements CRUD<E> {
  protected final Class<E> e;
  protected final EntityManagerSupplier entityManagerSupplier;
  public AbstractEntityDB(Class<E> klass, EntityManagerSupplier entityManagerSupplier) {
    this.e = klass;
    this.entityManagerSupplier = entityManagerSupplier;
  }
  
  @Override public synchronized <K> E get(K primaryKey) {
    try(EntityManagerWrapper entityManager = entityManagerSupplier.getWrapped()) {
      E user = entityManager.get().find(e, primaryKey);
      return user;
    }
  }
  @Override public abstract Set<E> getAll();
  
  protected abstract void add0(EntityManager entityManager, E entity) throws Exception;
  @Override public synchronized void add(E entity) throws Exception {
    try(EntityManagerWrapper entityManager = entityManagerSupplier.getWrapped()) { try {
      add0(entityManager.get(), entity);
    } catch(Exception e) {
      entityManager.rollback();
      throw e;
    } }
  }
  
  protected abstract void update0(EntityManager entityManager, E entity) throws Exception;
  @Override public synchronized void update(E entity) throws Exception {
    try(EntityManagerWrapper entityManager = entityManagerSupplier.getWrapped()) { try {
      update0(entityManager.get(), entity);
    } catch(Exception e) {
      entityManager.rollback();
      throw e;
    } }
  }
  
  protected abstract void remove0(EntityManager entityManager, E entity) throws Exception;
  @Override public synchronized void remove(E entity) throws Exception {
    try(EntityManagerWrapper entityManager = entityManagerSupplier.getWrapped()) { try {
      remove0(entityManager.get(), entity);
    } catch(Exception e) {
      entityManager.rollback();
      throw e;
    } }
  }
}