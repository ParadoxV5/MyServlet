package xyz.paradoxv5.servlet.jpa;

import java.util.function.Supplier;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
  
*/
public class EntityManagerWrapperSupplier implements Supplier<EntityManagerWrapper>, AutoCloseable {
  protected final EntityManagerFactory factory;
  public EntityManagerWrapperSupplier(String persistenceUnitName) {
    factory = Persistence.createEntityManagerFactory(persistenceUnitName);
  }
  @Override public void close() {
    factory.close();
  }
  
  @Override public EntityManagerWrapper get() {
    return new EntityManagerWrapper(factory.createEntityManager());
  }
}