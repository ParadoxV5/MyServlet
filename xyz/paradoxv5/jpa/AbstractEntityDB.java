package xyz.paradoxv5.jpa;

import xyz.paradoxv5.CRUD;

import java.util.Set;
import java.util.function.Supplier;

import javax.persistence.EntityManager;
import java.io.Serializable;

/**
  
*/
public abstract class AbstractEntityDB<E extends AbstractEntity<K>, K extends Serializable> implements CRUD<E, K> {
  protected final Class<E> e;
  protected final Supplier<EntityManagerWrapper> entityManagerSupplier;
  public AbstractEntityDB(Class<E> klass, Supplier<EntityManagerWrapper> entityManagerSupplier) {
    this.e = klass;
    this.entityManagerSupplier = entityManagerSupplier;
  }
  
  @Override public synchronized E get(K primaryKey) {
    try(EntityManagerWrapper entityManager = entityManagerSupplier.get()) {
      return entityManager.get().find(e, primaryKey);
    }
  }
  
  public abstract String getQlforGetAll();
  @Override public Set<E> getAll() {
    try(EntityManagerWrapper entityManager = entityManagerSupplier.get()) {
      return entityManager.get().createQuery(getQlforGetAll(), e).getResultStream().collect(java.util.stream.Collectors.toSet());
    }
  }
  
  protected abstract void add0(EntityManager entityManager, E entity) throws Exception;
  @Override public synchronized void add(E entity) throws Exception {
    try(EntityManagerWrapper entityManager = entityManagerSupplier.get()) { try {
      add0(entityManager.get(), entity);
    } catch(Exception e) {
      entityManager.rollback();
      throw e;
    } }
  }
  
  protected abstract void update0(EntityManager entityManager, E entity) throws Exception;
  @Override public synchronized void update(E entity) throws Exception {
    try(EntityManagerWrapper entityManager = entityManagerSupplier.get()) { try {
      update0(entityManager.get(), entity);
    } catch(Exception e) {
      entityManager.rollback();
      throw e;
    } }
  }
  
  protected abstract void remove0(EntityManager entityManager, E entity) throws Exception;
  @Override public synchronized void remove(E entity) throws Exception {
    try(EntityManagerWrapper entityManager = entityManagerSupplier.get()) { try {
      remove0(entityManager.get(), entity);
    } catch(Exception e) {
      entityManager.rollback();
      throw e;
    } }
  }
}