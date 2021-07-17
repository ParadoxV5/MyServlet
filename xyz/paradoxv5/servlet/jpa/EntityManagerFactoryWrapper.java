package xyz.paradoxv5.servlet.jpa;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
// Javadoc imports not used by Java code
import javax.persistence.EntityManager;
/**
  This is a wrapper class over {@linkplain #factory an EntityManagerFactory}
  {@linkplain #EntityManagerFactoryWrapper(String) created based on a persistenceUnitName}
  that supplies {@link EntityManagerWrapper}s that
  {@linkplain EntityManagerWrapper#EntityManagerWrapper(EntityManager) wraps}
  the factory’s {@link EntityManager}s.
  <p>
  This wrapper {@code implements} {@code AutoCloseable} to {@linkplain #close() close the factory}
  for {@code try}-with-resources convenience.
*/
public class EntityManagerFactoryWrapper implements java.util.function.Supplier<EntityManagerWrapper>, AutoCloseable {
  /** The wrapped {@code EntityManagerFactory}
    @see #EntityManagerFactoryWrapper(String)
    @see #get()
  */
  protected final EntityManagerFactory factory;
  /** @param persistenceUnitName
    the String name with which the {@link #factory} will be initialized with
    (using {@link Persistence#createEntityManagerFactory(String)}
    @see #close()
  */
  public EntityManagerFactoryWrapper(String persistenceUnitName) {
    factory = Persistence.createEntityManagerFactory(persistenceUnitName);
  }
  /** {@link EntityManagerFactory#close()} the {@link #factory} */
  @Override public void close() {
    factory.close();
  }
  
  /** @return
    {@link EntityManagerWrapper#EntityManagerWrapper(EntityManager) new EntityManagerWrapper}{@code (}{@link #factory}{@link EntityManagerFactory#createEntityManager() .createEntityManager()}{@code )}
  */
  @Override public EntityManagerWrapper get() {
    return new EntityManagerWrapper(factory.createEntityManager());
  }
}