package xyz.paradoxv5.servlet.jpa;

import java.util.function.Supplier;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
  
*/
public class EntityManagerSupplier implements Supplier<EntityManager>, AutoCloseable {
  protected final EntityManagerFactory factory;
  public EntityManagerSupplier(String persistenceUnitName) {
    factory = Persistence.createEntityManagerFactory(persistenceUnitName);
  }
  @Override public void close() {
    factory.close();
  }
  
  @Override public EntityManager get() {
    return factory.createEntityManager();
  }
  public EntityManagerWrapper getWrapped() {
    return new EntityManagerWrapper(get());
  }
}